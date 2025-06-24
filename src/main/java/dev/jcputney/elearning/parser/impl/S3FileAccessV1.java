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

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ListObjectsRequest;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import dev.jcputney.elearning.parser.api.FileAccess;
import dev.jcputney.elearning.parser.util.LogMarkers;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

/**
 * Optimized implementation of FileAccess using AWS S3 SDK v1 with batch operations, streaming
 * support, and intelligent caching.
 */
@Slf4j
@SuppressWarnings("unused")
public class S3FileAccessV1 implements FileAccess {

  private static final long STREAMING_THRESHOLD = 5 * 1024 * 1024; // 5MB
  private static final int MAX_CACHE_SIZE = 1000;
  private static final Set<String> COMMON_MODULE_FILES = Set.of(
      "imsmanifest.xml", "cmi5.xml", "xAPI.js", "sendStatement.js",
      "manifest.xml", "tincan.xml", "MANIFEST.MF"
  );

  private final AmazonS3 s3Client;
  private final String bucketName;
  private final ExecutorService executorService;

  // Caches for performance
  private final Map<String, Boolean> fileExistsCache = new ConcurrentHashMap<>();
  private final Map<String, List<String>> directoryListCache = new ConcurrentHashMap<>();
  private final Map<String, byte[]> smallFileCache = new ConcurrentHashMap<>();
  private final Map<String, Long> fileSizeCache = new ConcurrentHashMap<>();
  private volatile List<String> allFilesCache = null;

  @Getter
  private volatile String rootPath;

  /**
   * Constructs an optimized S3FileAccessV1 instance with the specified S3 client and bucket name.
   *
   * @param s3Client The S3 client to use for accessing files.
   * @param bucketName The name of the S3 bucket to access.
   * @param rootPath The root path of the S3 bucket to access.
   */
  public S3FileAccessV1(AmazonS3 s3Client, String bucketName, String rootPath) {
    this(s3Client, bucketName, rootPath, true);
  }

  /**
   * Constructs an optimized S3FileAccessV1 instance with the specified S3 client and bucket name.
   *
   * @param s3Client The S3 client to use for accessing files.
   * @param bucketName The name of the S3 bucket to access.
   * @param rootPath The root path of the S3 bucket to access.
   * @param eagerCache Whether to eagerly cache all files on initialization.
   */
  public S3FileAccessV1(AmazonS3 s3Client, String bucketName, String rootPath, boolean eagerCache) {
    this.s3Client = s3Client;
    this.bucketName = bucketName;
    this.executorService = Executors.newFixedThreadPool(10);

    String processedPath = rootPath;
    if (processedPath == null) {
      processedPath = "";
    }

    // Lazy initialization of root path detection
    this.rootPath = processedPath;

    if (this.rootPath.endsWith("/")) {
      this.rootPath = this.rootPath.substring(0, this.rootPath.length() - 1);
    }

    if (eagerCache) {
      // Eagerly cache all files to avoid individual S3 API calls later
      try {
        log.debug("Eagerly caching all files for S3 bucket: {} with prefix: {}", bucketName, this.rootPath);
        getAllFiles();
      } catch (IOException e) {
        log.warn("Failed to eagerly cache files from S3, will fall back to lazy loading: {}", e.getMessage());
      }
    }
  }

  /**
   * Checks if a file exists at the specified path with caching.
   *
   * @param path The path of the file to check (guaranteed to be non-null).
   * @return True if the file exists, false otherwise.
   */
  @Override
  public boolean fileExistsInternal(String path) {
    // First check the file exists cache
    Boolean cached = fileExistsCache.get(path);
    if (cached != null) {
      return cached;
    }
    
    // If we have the allFilesCache populated, use it to determine existence
    List<String> allFiles = allFilesCache;
    if (allFiles != null) {
      String fullFilePath = fullPath(path);
      boolean exists = allFiles.contains(fullFilePath);
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
   * @return Map of path to existence boolean
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
          log.debug("Failed to check file existence for batch operation", e);
        }
      });
    }

    return results;
  }

  /**
   * Prefetch common module files in parallel for faster subsequent access.
   */
  @Override
  public void prefetchCommonFiles() {
    List<String> commonFiles = COMMON_MODULE_FILES
        .stream()
        .filter(file -> !smallFileCache.containsKey(file))
        .toList();

    if (commonFiles.isEmpty()) {
      return;
    }

    log.debug(LogMarkers.S3_VERBOSE, "Prefetching {} common module files", commonFiles.size());

    List<CompletableFuture<Void>> futures = commonFiles
        .stream()
        .map(file -> CompletableFuture.runAsync(() -> {
          try {
            String fullFilePath = fullPath(file);
            if (checkFileExistsOnS3(file)) {
              // Get file size first
              long size = getFileSizeOnS3(file);
              fileSizeCache.put(file, size);

              // Only cache small files
              if (size <= STREAMING_THRESHOLD) {
                byte[] content = s3Client
                    .getObjectAsString(bucketName, fullFilePath)
                    .getBytes();
                smallFileCache.put(file, content);
                log.debug(LogMarkers.S3_VERBOSE, "Prefetched file: {} ({} bytes)", file,
                    content.length);
              }
            }
          } catch (Exception e) {
            // Failed to prefetch file - this is expected for missing files
          }
        }, executorService))
        .toList();

    CompletableFuture
        .allOf(futures.toArray(new CompletableFuture[0]))
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
    // Check cache first for small files
    byte[] cachedContent = smallFileCache.get(path);
    if (cachedContent != null) {
      log.debug(LogMarkers.S3_VERBOSE, "Returning cached content for file: {}", path);
      return new ByteArrayInputStream(cachedContent);
    }

    // Get file size to determine strategy
    Long cachedSize = fileSizeCache.get(path);
    long fileSize = cachedSize != null ? cachedSize : getFileSizeOnS3(path);

    if (cachedSize == null) {
      fileSizeCache.put(path, fileSize);
    }

    String fullFilePath = fullPath(path);

    if (fileSize <= STREAMING_THRESHOLD) {
      // Cache small files for future use
      log.debug(LogMarkers.S3_VERBOSE, "Caching small file: {} ({} bytes)", path, fileSize);
      String content = s3Client.getObjectAsString(bucketName, fullFilePath);
      byte[] contentBytes = content.getBytes();

      // Implement simple cache size management
      if (smallFileCache.size() >= MAX_CACHE_SIZE) {
        // Remove oldest entry (simple FIFO)
        String oldestKey = smallFileCache
            .keySet()
            .iterator()
            .next();
        smallFileCache.remove(oldestKey);
      }

      smallFileCache.put(path, contentBytes);
      return new ByteArrayInputStream(contentBytes);
    } else {
      // Stream large files directly
      log.debug(LogMarkers.S3_VERBOSE, "Streaming large file: {} ({} bytes)", path, fileSize);
      S3Object s3Object = s3Client.getObject(new GetObjectRequest(bucketName, fullFilePath));
      return s3Object.getObjectContent();
    }
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
    allFilesCache = null;
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
    List<String> cached = allFilesCache;
    if (cached != null) {
      return cached;
    }
    
    synchronized (this) {
      // Double-check after acquiring lock
      cached = allFilesCache;
      if (cached != null) {
        return cached;
      }
      
      log.debug("Scanning all files in S3 bucket: {} with prefix: {}", bucketName, rootPath);
      List<String> allFiles = listFilesOnS3("");
      
      // Populate file existence cache with all found files
      for (String file : allFiles) {
        fileExistsCache.put(file, true);
      }
      
      allFilesCache = allFiles;
      log.debug("Found {} total files in module", allFiles.size());
      return allFiles;
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
    // First ensure we have all files cached
    List<String> allFiles = getAllFiles();
    
    if (allFiles.isEmpty()) {
      return 0;
    }
    
    log.debug("Calculating total size for {} files", allFiles.size());
    
    // If we have all sizes cached already, just sum them
    if (fileSizeCache.size() >= allFiles.size()) {
      long totalSize = fileSizeCache.values().stream()
          .mapToLong(Long::longValue)
          .sum();
      log.debug("Total module size (from cache): {} bytes", totalSize);
      return totalSize;
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
          fileSizeCache.put(finalRelativePath, size);
          return size;
        }, executorService));
      }
    }
    
    // Wait for all size fetches to complete
    for (CompletableFuture<Long> future : futures) {
      try {
        totalSize += future.join();
      } catch (Exception e) {
        log.debug("Failed to get file size during total calculation", e);
      }
    }
    
    log.debug("Total module size: {} bytes", totalSize);
    return totalSize;
  }

  /**
   * Shutdown the executor service when the instance is no longer needed.
   */
  public void shutdown() {
    executorService.shutdown();
  }

  // Private helper methods

  private boolean checkFileExistsOnS3(String path) {
    try {
      return s3Client.doesObjectExist(bucketName, fullPath(path));
    } catch (AmazonServiceException e) {
      log.debug("Failed to check file existence for path: {}", path, e);
      return false;
    }
  }

  private long getFileSizeOnS3(String path) {
    try {
      return s3Client
          .getObjectMetadata(bucketName, fullPath(path))
          .getContentLength();
    } catch (AmazonServiceException e) {
      log.debug("Failed to get file size for path: {}", path, e);
      return 0;
    }
  }

  private List<String> listFilesOnS3(String directoryPath) {
    try {
      List<String> allKeys = new ArrayList<>();
      String prefix = fullPath(directoryPath);

      ListObjectsRequest request = new ListObjectsRequest()
          .withBucketName(bucketName)
          .withPrefix(prefix)
          .withMaxKeys(1000);

      ObjectListing listing;
      do {
        listing = s3Client.listObjects(request);
        allKeys.addAll(listing
            .getObjectSummaries()
            .stream()
            .map(S3ObjectSummary::getKey)
            .filter(key -> !key.endsWith("/")) // Filter out directory markers
            .toList());
        request.setMarker(listing.getNextMarker());
      } while (listing.isTruncated());

      log.debug(LogMarkers.S3_VERBOSE, "Listed {} files in directory: {}", allKeys.size(),
          directoryPath);
      return allKeys;
    } catch (AmazonServiceException e) {
      log.debug("Failed to list files in directory: {}", directoryPath, e);
      return List.of();
    }
  }

  private String detectInternalRootDirectory(String rootPath) {
    try {
      List<String> commonPrefixes = s3Client
          .listObjects(bucketName, fullPath(rootPath))
          .getCommonPrefixes();
      if (commonPrefixes.size() != 1) {
        return rootPath;
      }
      return commonPrefixes.get(0);
    } catch (AmazonServiceException e) {
      log.debug("Failed to detect internal root directory", e);
      return rootPath;
    }
  }
}
