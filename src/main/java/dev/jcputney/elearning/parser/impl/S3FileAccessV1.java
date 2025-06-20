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
import java.util.stream.Collectors;
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
  }

  /**
   * Checks if a file exists at the specified path with caching.
   *
   * @param path The path of the file to check (guaranteed to be non-null).
   * @return True if the file exists, false otherwise.
   */
  @Override
  public boolean fileExistsInternal(String path) {
    return fileExistsCache.computeIfAbsent(path, this::checkFileExistsOnS3);
  }

  /**
   * Batch check if multiple files exist - much more efficient for module parsing.
   *
   * @param paths List of file paths to check
   * @return Map of path to existence boolean
   */
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
  public void clearCaches() {
    fileExistsCache.clear();
    directoryListCache.clear();
    smallFileCache.clear();
    fileSizeCache.clear();
    // All caches cleared
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
            .collect(Collectors.toList()));
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
