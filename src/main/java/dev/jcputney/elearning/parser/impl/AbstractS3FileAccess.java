/*
 * Copyright (c) 2024-2025. Jonathan Putney
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package dev.jcputney.elearning.parser.impl;

import dev.jcputney.elearning.parser.api.FileAccess;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Abstract base class for S3 FileAccess implementations with common caching and optimization logic.
 * This class provides the shared functionality between AWS SDK v1 and v2 implementations.
 */
public abstract class AbstractS3FileAccess implements FileAccess {

  /**
   * Threshold for streaming files instead of caching them in memory. Files larger than this size
   * will be streamed directly from S3.
   */
  protected static final long STREAMING_THRESHOLD = 5 * 1024 * 1024L; // 5MB

  /**
   * Maximum size of the small file cache. This limits the number of small files (less than
   * STREAMING_THRESHOLD) that can be cached in memory.
   */
  protected static final int MAX_CACHE_SIZE = 1000;

  /**
   * Common module files that are frequently accessed and should be prefetched. This set contains
   * file names that are typically present in SCORM/xAPI modules.
   */
  protected static final Set<String> COMMON_MODULE_FILES = Set.of(
      "imsmanifest.xml", "cmi5.xml", "xAPI.js", "sendStatement.js",
      "manifest.xml", "tincan.xml", "MANIFEST.MF"
  );

  /**
   * The name of the S3 bucket to access. This is used to construct the full S3 paths for file
   * operations.
   */
  protected final String bucketName;

  /**
   * Executor service for parallel operations, such as file existence checks and prefetching. This
   * allows for efficient asynchronous operations without blocking the main thread.
   */
  protected final ExecutorService executorService;

  /**
   * Cache for file existence checks to avoid repeated S3 API calls. This cache stores the existence
   * status of files by their relative paths.
   */
  protected final Map<String, Boolean> fileExistsCache = new ConcurrentHashMap<>();

  /**
   * Cache for directory listings to avoid repeated S3 API calls. This cache stores the list of
   * files in each directory path.
   */
  protected final Map<String, List<String>> directoryListCache = new ConcurrentHashMap<>();

  /**
   * Cache for small files (less than STREAMING_THRESHOLD) to avoid repeated S3 API calls. This
   * cache is used to store the contents of small files as byte arrays for quick access.
   */
  protected final Map<String, byte[]> smallFileCache = new ConcurrentHashMap<>();

  /**
   * Cache for file sizes, used to avoid repeated S3 API calls for size checks. This is particularly
   * useful for large files where we want to avoid streaming the entire content just to get the
   * size.
   */
  protected final Map<String, Long> fileSizeCache = new ConcurrentHashMap<>();

  /**
   * A thread-safe cache storing the list of all file paths within the module.
   * <p>
   * This cache is intended to improve performance for file-related operations by storing the result
   * of a full scan of the S3 bucket or prefix. The cache is stored in an {@link AtomicReference} to
   * ensure safe publication across threads once populated.
   * <p>
   * Modifications to this cache should be controlled to maintain data consistency across the class,
   * particularly when the underlying S3 bucket contents change.
   */
  protected final AtomicReference<List<String>> allFilesCache = new AtomicReference<>();

  /**
   * The root path within the S3 bucket to access. This is used to construct full paths for files
   * and directories. It is lazily initialized to allow subclasses to set it up after their S3
   * client is ready.
   */
  protected volatile String rootPath;

  /**
   * Constructs an abstract S3FileAccess instance.
   *
   * @param bucketName The name of the S3 bucket to access.
   * @param rootPath The root path of the S3 bucket to access.
   */
  protected AbstractS3FileAccess(String bucketName, String rootPath) {
    this.bucketName = bucketName;
    this.executorService = Executors.newFixedThreadPool(10);
    initializeRootPath(rootPath);
  }

  /**
   * Checks if a file exists at the specified path with caching.
   *
   * @param path The path of the file to check (guaranteed to be non-null).
   * @return True if the file exists, false otherwise.
   */
  @Override
  public boolean fileExistsInternal(String path) {
    // First, check the file exists cache
    Boolean cached = fileExistsCache.get(path);
    if (cached != null) {
      return cached;
    }

    // If we have the allFilesCache populated, use it to determine existence
    List<String> allFiles = allFilesCache.get();
    if (allFiles != null) {
      boolean exists = allFiles.contains(path);
      if (!exists) {
        // SDK-specific implementations may cache absolute keys; fall back to that form
        exists = allFiles.contains(fullPath(path));
      }
      fileExistsCache.put(path, exists);
      return exists;
    }

    // Fall back to S3 API call if no cache is available
    return fileExistsCache.computeIfAbsent(path, this::checkFileExistsOnS3);
  }

  /**
   * Batch check if multiple files exist - much more efficient for module parsing.
   *
   * @param paths List of file paths to check
   * @return Map of a path to existence boolean
   */
  @Override
  public Map<String, Boolean> fileExistsBatch(List<String> paths) {
    // Get cached results first
    Map<String, Boolean> results = new ConcurrentHashMap<>();
    List<String> uncachedPaths = new ArrayList<>();

    for (String path : paths) {
      Boolean cached = fileExistsCache.get(path);
      if (cached != null) {
        results.put(path, cached);
      } else {
        uncachedPaths.add(path);
      }
    }

    // Check uncached paths in parallel
    if (!uncachedPaths.isEmpty()) {
      List<CompletableFuture<Map.Entry<String, Boolean>>> futures = uncachedPaths
          .stream()
          .map(path -> CompletableFuture.supplyAsync(() -> {
            boolean exists = checkFileExistsOnS3(path);
            fileExistsCache.put(path, exists);
            return Map.entry(path, exists);
          }, executorService))
          .toList();

      futures.forEach(future -> {
        try {
          Map.Entry<String, Boolean> result = future.join();
          results.put(result.getKey(), result.getValue());
        } catch (Exception e) {
          // Failed to check file existence for batch operation
        }
      });
    }

    return results;
  }

  /**
   * Prefetches common files that are not already present in the small file cache.
   * <p>
   * This method identifies files that are listed in the COMMON_MODULE_FILES collection but are not
   * yet loaded into the small file cache. For each file that is missing from the cache, a prefetch
   * task is initiated asynchronously using the provided executorService. All asynchronous tasks are
   * executed concurrently, and the method blocks until all tasks complete.
   * <p>
   * Key logic details: - Filters the COMMON_MODULE_FILES to identify files absent from the small
   * file cache. - Asynchronously prefetches missing files using independent tasks. - Waits for all
   * asynchronous tasks to complete before returning.
   * <p>
   * This method is designed to optimize the availability of commonly used files and reduce the
   * latency during their access by loading them in advance.
   */
  @Override
  public void prefetchCommonFiles() {
    List<String> filesToPrefetch = COMMON_MODULE_FILES
        .stream()
        .filter(file -> !smallFileCache.containsKey(file))
        .toList();
    if (filesToPrefetch.isEmpty()) {
      return;
    }

    List<CompletableFuture<Void>> prefetchTasks = filesToPrefetch
        .stream()
        .map(file -> CompletableFuture.runAsync(() -> prefetchSingleFile(file), executorService))
        .toList();

    CompletableFuture
        .allOf(prefetchTasks.toArray(new CompletableFuture[0]))
        .join();
  }

  /**
   * Lists the files in the specified directory path with caching and pagination support.
   *
   * @param directoryPath The path of the directory to list files from (guaranteed to be non-null).
   * @return A list of file paths in the specified directory.
   */
  @Override
  public List<String> listFilesInternal(String directoryPath) throws IOException {
    return directoryListCache.computeIfAbsent(directoryPath, this::listFilesOnS3);
  }

  /**
   * Gets the contents of a file as an InputStream with intelligent streaming/caching.
   *
   * @param path The path of the file to get contents from (guaranteed to be non-null).
   * @return An InputStream containing the file contents.
   */
  @Override
  public InputStream getFileContentsInternal(String path) throws IOException {
    return getFileContentsBase(path);
  }

  /**
   * Determines the internal root directory within the S3 bucket with lazy initialization.
   *
   * @return The detected internal root directory or the original path if none is detected.
   */
  public String getInternalRootDirectory() {
    if (rootPath == null || rootPath.isEmpty()) {
      synchronized (this) {
        if (rootPath == null || rootPath.isEmpty()) {
          rootPath = detectInternalRootDirectory("");
        }
      }
    }
    return rootPath;
  }

  /**
   * Clear all caches - useful for testing or when bucket contents change.
   */
  @Override
  public void clearCaches() {
    fileExistsCache.clear();
    directoryListCache.clear();
    smallFileCache.clear();
    fileSizeCache.clear();
    allFilesCache.set(null);
    // All caches cleared
  }

  /**
   * Gets a list of all files in the module.
   *
   * <p>This method scans the entire S3 bucket/prefix once and caches the results
   * for subsequent calls, improving performance for file existence checks.
   *
   * @return List of all file paths in the module
   * @throws IOException if there's an error accessing the S3 bucket
   */
  @Override
  public List<String> getAllFiles() throws IOException {
    List<String> cached = allFilesCache.get();
    if (cached != null) {
      return cached;
    }

    synchronized (this) {
      // Double-check after acquiring a lock
      cached = allFilesCache.get();
      if (cached != null) {
        return cached;
      }

      // Scanning all files in S3 bucket with prefix
      List<String> allFiles = listFilesOnS3("");

      // Populate the file existence cache with all found files
      for (String file : allFiles) {
        fileExistsCache.put(file, true);
      }

      allFilesCache.set(Collections.unmodifiableList(allFiles));
      // Found total files in the module
      return allFilesCache.get();
    }
  }

  /**
   * Retrieves statistics about various internal caches used in the class.
   *
   * @return A map where the keys are the cache names (e.g., "fileExistsCache",
   * "directoryListCache", etc.) and the values are the respective sizes of these caches.
   */
  public Map<String, Integer> getCacheStats() {
    return Map.of(
        "fileExistsCache", fileExistsCache.size(),
        "directoryListCache", directoryListCache.size(),
        "smallFileCache", smallFileCache.size(),
        "fileSizeCache", fileSizeCache.size()
    );
  }

  /**
   * Gets the total size of all files in the module.
   *
   * <p>This method calculates the sum of all file sizes in the module using
   * the cached file sizes from the S3 bucket.
   *
   * @return Total size of all files in bytes
   * @throws IOException if there's an error accessing file sizes
   */
  @Override
  public long getTotalSize() throws IOException {
    // First, ensure we have all files cached
    List<String> allFiles = getAllFiles();

    if (allFiles.isEmpty()) {
      return 0;
    }

    // Calculating total size for files

    // If we have all sizes cached already, sum them
    if (fileSizeCache.size() >= allFiles.size()) {
      // Total module size calculated from the cache
      return fileSizeCache
          .values()
          .stream()
          .mapToLong(Long::longValue)
          .sum();
    }

    // Otherwise, we need to fetch sizes for uncached files
    long totalSize = 0;
    List<CompletableFuture<Long>> futures = new ArrayList<>();

    for (String file : allFiles) {
      // Remove the root path prefix to get the relative path
      String relativePath = file;
      if (file.startsWith(rootPath + "/")) {
        relativePath = file.substring(rootPath.length() + 1);
      }

      final String finalRelativePath = relativePath; // Make it final for lambda
      Long cachedSize = fileSizeCache.get(relativePath);
      if (cachedSize != null) {
        totalSize += cachedSize;
      } else {
        // Fetch size asynchronously
        futures.add(CompletableFuture.supplyAsync(() -> {
          long size = getFileSizeOnS3(finalRelativePath);
          // Only cache non-zero sizes to avoid caching failed lookups
          if (size > 0) {
            fileSizeCache.put(finalRelativePath, size);
          }
          return size;
        }, executorService));
      }
    }

    // Wait for all size fetches to complete
    for (CompletableFuture<Long> future : futures) {
      try {
        totalSize += future.join();
      } catch (Exception e) {
        // Failed to get file size during total calculation
      }
    }

    // Total module size calculated
    return totalSize;
  }

  /**
   * Shutdown the executor service when the instance is no longer needed.
   */
  public void shutdown() {
    executorService.shutdown();
  }

  /**
   * Constructs the full S3 path by combining the root path with the relative path.
   *
   * @param relativePath The relative path within the module
   * @return The full S3 key path
   */
  @Override
  public String fullPath(String relativePath) {
    if (relativePath == null) {
      throw new IllegalArgumentException("Path cannot be null");
    }

    // Handle absolute paths by removing the leading slash
    String path = relativePath;
    if (path.startsWith("/")) {
      path = path.substring(1);
    }

    if (path.isEmpty()) {
      return rootPath == null || rootPath.isEmpty() ? "" : rootPath;
    }
    if (rootPath == null || rootPath.isEmpty()) {
      return path;
    }
    return rootPath + "/" + path;
  }

  /**
   * Retrieves the root path of the current instance.
   *
   * @return the root path as a String
   */
  @Override
  public String getRootPath() {
    return this.rootPath;
  }

  /**
   * Reconfigures the root path for the S3 file access. This method clears all internal caches and
   * re-initializes the root path to the specified value.
   *
   * @param newRootPath The new root path to set. This value will be normalized and stored as the
   * root path.
   */
  protected final void reconfigureRootPath(String newRootPath) {
    clearCaches();
    initializeRootPath(newRootPath);
  }

  /**
   * Base implementation for getting file contents with intelligent streaming/caching. Protected to
   * allow subclasses to extend with additional functionality.
   *
   * @param path The path of the file to get contents from (guaranteed to be non-null).
   * @return An InputStream containing the file contents.
   * @throws IOException If an error occurs while getting file contents.
   */
  protected InputStream getFileContentsBase(String path) throws IOException {
    // Check the cache first for small files
    byte[] cachedContent = smallFileCache.get(path);
    if (cachedContent != null) {
      // Returning cached content for a file
      long fileSize = getCachedFileSize(path);
      return getInputStreamWrapper(new ByteArrayInputStream(cachedContent), fileSize);
    }

    // Get file size to determine strategy
    long fileSize = getCachedFileSize(path);

    String fullFilePath = fullPath(path);

    if (fileSize <= STREAMING_THRESHOLD) {
      // Cache small files for future use
      // Caching small file
      byte[] content = getS3ObjectAsBytes(fullFilePath);

      // Implement simple cache size management
      if (smallFileCache.size() >= MAX_CACHE_SIZE) {
        // Remove the oldest entry (simple FIFO)
        String oldestKey = smallFileCache
            .keySet()
            .iterator()
            .next();
        smallFileCache.remove(oldestKey);
      }

      smallFileCache.put(path, content);
      return getInputStreamWrapper(new ByteArrayInputStream(content), fileSize);
    } else {
      // Stream large files directly
      InputStream stream = getS3ObjectStream(fullFilePath);
      return getInputStreamWrapper(stream, fileSize);
    }
  }

  /**
   * Wraps the provided input stream with additional processing or functionality.
   *
   * @param stream the original input stream to be wrapped
   * @param fileSize the size of the file associated with the input stream, in bytes
   * @return an InputStream instance which provides a wrapped version of the original input stream
   */
  protected abstract InputStream getInputStreamWrapper(InputStream stream, long fileSize);

  /**
   * Get the cached file size or fetch it from S3 if not cached.
   *
   * @param path The path of the file
   * @return The file size in bytes
   */
  protected long getCachedFileSize(String path) {
    Long cachedSize = fileSizeCache.get(path);

    // If cached size is 0, treat it as invalid and refetch
    if (cachedSize != null && cachedSize == 0) {
      cachedSize = null;
      fileSizeCache.remove(path);
    }

    long fileSize = cachedSize != null ? cachedSize : getFileSizeOnS3(path);

    if (cachedSize == null && fileSize > 0) {
      fileSizeCache.put(path, fileSize);
    }

    return fileSize;
  }

  /**
   * Check if a file exists on S3 using the specific SDK implementation.
   *
   * @param path The relative path to check
   * @return True if the file exists, false otherwise
   */
  protected abstract boolean checkFileExistsOnS3(String path);

  /**
   * Get the size of a file on S3 using the specific SDK implementation.
   *
   * @param path The relative path of the file
   * @return The file size in bytes, or 0, if error
   */
  protected abstract long getFileSizeOnS3(String path);

  /**
   * List files in a directory on S3 using the specific SDK implementation.
   *
   * @param directoryPath The directory path to the list
   * @return List of file paths
   */
  protected abstract List<String> listFilesOnS3(String directoryPath);

  /**
   * Get the contents of a small S3 object as a byte array.
   *
   * @param fullPath The full S3 key path
   * @return The file contents as bytes
   * @throws IOException if there's an error reading the file
   */
  protected abstract byte[] getS3ObjectAsBytes(String fullPath) throws IOException;

  /**
   * Get a stream for a large S3 object.
   *
   * @param fullPath The full S3 key path
   * @return An InputStream for the file contents
   * @throws IOException if there's an error opening the stream
   */
  protected abstract InputStream getS3ObjectStream(String fullPath) throws IOException;

  /**
   * Detect the internal root directory using the specific SDK implementation.
   *
   * @param rootPath The current root path
   * @return The detected internal root directory
   */
  protected abstract String detectInternalRootDirectory(String rootPath);

  /**
   * Prefetches a single file by retrieving its metadata and, if the file is within a predefined
   * size threshold, caching its content. This method performs a best-effort retrieval, ignoring
   * transient errors.
   *
   * @param file the name or identifier of the file to be prefetched
   */
  private void prefetchSingleFile(String file) {
    try {
      final String s3Path = fullPath(file);
      if (!checkFileExistsOnS3(file)) {
        return;
      }
      long size = getFileSizeOnS3(file);
      if (size <= 0) {
        return; // avoid caching failed lookups or empty files
      }
      fileSizeCache.put(file, size);
      if (size <= STREAMING_THRESHOLD) {
        byte[] content = getS3ObjectAsBytes(s3Path);
        smallFileCache.put(file, content);
      }
    } catch (Exception ignored) {
      // Best-effort prefetch: ignore failures (e.g., missing files or transient errors)
    }
  }

  /**
   * Initializes and normalizes the root path for the class. Ensures that the path does not end with
   * a trailing slash and handles null values by defaulting to an empty string.
   *
   * @param rootPath The initial root path to be set. Can be null.
   */
  private void initializeRootPath(String rootPath) {
    String processedPath = rootPath;
    if (processedPath == null) {
      processedPath = "";
    }

    if (processedPath.endsWith("/")) {
      processedPath = processedPath.substring(0, processedPath.length() - 1);
    }

    this.rootPath = processedPath;
  }
}
