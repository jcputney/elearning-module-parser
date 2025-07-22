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
import dev.jcputney.elearning.parser.api.ParsingEventListener;
import dev.jcputney.elearning.parser.exception.FileAccessException;
import dev.jcputney.elearning.parser.exception.RuntimeFileAccessException;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * A decorator implementation of {@link FileAccess} that adds caching capability to any
 * {@link FileAccess} implementation. This class caches the results of file existence checks, file
 * listings, and file contents to improve performance for frequently accessed resources.
 *
 * <p>The cache uses a {@link ConcurrentHashMap} to ensure thread safety for concurrent access.
 * For file contents, the entire file is read into memory and stored as a byte array to allow for
 * multiple reads without reopening the file.
 *
 * <p>This implementation is particularly useful for scenarios where the same files are accessed
 * repeatedly, such as during module parsing.
 */
public class CachedFileAccess implements FileAccess {

  private final FileAccess delegate;

  private final ParsingEventListener eventListener;

  // Cache for file existence checks
  private final Map<String, Boolean> fileExistsCache = new ConcurrentHashMap<>();

  // Cache for file listings
  private final Map<String, List<String>> listFilesCache = new ConcurrentHashMap<>();

  // Cache for file contents
  private final Map<String, byte[]> fileContentsCache = new ConcurrentHashMap<>();

  // Cache statistics for monitoring
  private long cacheHits = 0;
  private long cacheMisses = 0;

  /**
   * Constructs a new {@link CachedFileAccess} instance that wraps the specified {@link FileAccess}
   * implementation.
   *
   * @param delegate The {@link FileAccess} implementation to delegate to.
   * @throws IllegalArgumentException if the delegate is null
   */
  public CachedFileAccess(FileAccess delegate) {
    this(delegate, null);
  }

  /**
   * Constructs a new {@link CachedFileAccess} instance that wraps the specified {@link FileAccess}
   * implementation with an optional event listener.
   *
   * @param delegate The {@link FileAccess} implementation to delegate to.
   * @param eventListener Optional listener for caching events (can be null)
   * @throws IllegalArgumentException if the delegate is null
   */
  public CachedFileAccess(FileAccess delegate, ParsingEventListener eventListener) {
    if (delegate == null) {
      throw new IllegalArgumentException("FileAccess delegate cannot be null");
    }
    this.delegate = delegate;
    this.eventListener = eventListener != null ? eventListener : ParsingEventListener.NO_OP;
  }

  /**
   * Gets the root path from the delegate {@link FileAccess} implementation.
   *
   * @return The root path.
   */
  @Override
  public String getRootPath() {
    return delegate.getRootPath();
  }

  /**
   * Checks if a file exists at the given path, using the cache if available.
   *
   * @param path The path to check (guaranteed to be non-null).
   * @return True if the file exists, false otherwise.
   */
  @Override
  public boolean fileExistsInternal(String path) {
    Boolean cached = fileExistsCache.get(path);
    if (cached != null) {
      cacheHits++;
      eventListener.onParsingProgress("Cache hit: fileExists for " + path, -1);
      return cached;
    }

    cacheMisses++;
    eventListener.onParsingProgress("Cache miss: fileExists for " + path, -1);
    boolean exists = delegate.fileExists(path);
    fileExistsCache.put(path, exists);
    return exists;
  }

  /**
   * Lists all files within a specified directory path, using the cache if available.
   *
   * @param directoryPath The directory to list files from (guaranteed to be non-null).
   * @return A list of file paths within the directory.
   * @throws IOException if there's an error accessing the directory or listing its contents.
   */
  @Override
  public List<String> listFilesInternal(String directoryPath) throws IOException {
    try {
      List<String> cached = listFilesCache.get(directoryPath);
      if (cached != null) {
        cacheHits++;
        eventListener.onParsingProgress("Cache hit: listFiles for " + directoryPath, -1);
        return cached;
      }

      cacheMisses++;
      eventListener.onParsingProgress("Cache miss: listFiles for " + directoryPath, -1);
      return listFilesCache.computeIfAbsent(directoryPath, p -> {
        try {
          // Get the file listing from the delegate
          List<String> files = delegate.listFiles(p);
          return Collections.unmodifiableList(files);
        } catch (IOException e) {
          // Create detailed metadata for the exception
          Map<String, Object> metadata = new HashMap<>();
          metadata.put("path", p);
          metadata.put("operation", "listFiles");
          metadata.put("fileAccess", delegate
              .getClass()
              .getSimpleName());

          // Wrap the IOException in a FileAccessException to be compatible with computeIfAbsent
          throw new RuntimeFileAccessException(new FileAccessException(
              String.format("Failed to list files in directory '%s'", p), e, metadata));
        }
      });
    } catch (RuntimeFileAccessException e) {
      // For any other exception, wrap it in an IOException with a detailed message
      throw new IOException("Error listing files in directory: %s using %s".formatted(directoryPath,
          delegate
              .getClass()
              .getSimpleName()), e.getCause());
    }
  }

  /**
   * Retrieves the contents of a file as an InputStream, using the cache if available. The file
   * contents are read fully into memory on the first access and cached as a byte array. Subsequent
   * calls return a new ByteArrayInputStream wrapping the cached byte array.
   *
   * @param path The path to retrieve contents from (guaranteed to be non-null).
   * @return An InputStream of the file contents.
   * @throws IOException if the file can't be read.
   */
  @Override
  public InputStream getFileContentsInternal(String path) throws IOException {
    try {
      byte[] cached = fileContentsCache.get(path);
      if (cached != null) {
        cacheHits++;
        eventListener.onParsingProgress("Cache hit: getFileContents for " + path, -1);
        return new ByteArrayInputStream(cached);
      }

      cacheMisses++;
      eventListener.onParsingProgress("Cache miss: getFileContents for " + path, -1);
      byte[] contents = fileContentsCache.computeIfAbsent(path, p -> {
        try (InputStream is = delegate.getFileContents(p);
            ByteArrayOutputStream os = new ByteArrayOutputStream()) {
          // Read the file contents into a byte array
          byte[] buffer = new byte[8192];
          int bytesRead;
          while ((bytesRead = is.read(buffer)) != -1) {
            os.write(buffer, 0, bytesRead);
          }
          return os.toByteArray();
        } catch (IOException e) {
          // Create detailed metadata for the exception
          Map<String, Object> metadata = new HashMap<>();
          metadata.put("path", p);
          metadata.put("operation", "getFileContents");
          metadata.put("fileAccess", delegate
              .getClass()
              .getSimpleName());

          // Wrap the IOException in a FileAccessException to be compatible with computeIfAbsent
          throw new RuntimeFileAccessException(new FileAccessException(
              String.format("Failed to read file contents from '%s'", p), e, metadata));
        }
      });

      return new ByteArrayInputStream(contents);
    } catch (RuntimeFileAccessException e) {
      // For any other exception, wrap it in an IOException with a detailed message
      throw new IOException("Error reading file contents for path: %s using %s".formatted(path,
          delegate
              .getClass()
              .getSimpleName()), e.getCause());
    }
  }

  /**
   * Clears all caches, forcing subsequent calls to retrieve fresh data from the delegate.
   */
  public void clearCache() {
    int totalCacheEntries =
        fileExistsCache.size() + listFilesCache.size() + fileContentsCache.size();
    fileExistsCache.clear();
    listFilesCache.clear();
    fileContentsCache.clear();
    cacheHits = 0;
    cacheMisses = 0;
    eventListener.onParsingProgress("Cache cleared: " + totalCacheEntries + " entries removed", -1);
  }

  /**
   * Clears the cache for a specific path, forcing subsequent calls for that path to retrieve fresh
   * data from the delegate.
   *
   * @param path The path to clear from the cache.
   * @throws IllegalArgumentException if a path is null
   */
  public void clearCache(String path) {
    if (path == null) {
      throw new IllegalArgumentException("Path cannot be null");
    }
    int entriesRemoved = 0;
    if (fileExistsCache.remove(path) != null) {
      entriesRemoved++;
    }
    if (listFilesCache.remove(path) != null) {
      entriesRemoved++;
    }
    if (fileContentsCache.remove(path) != null) {
      entriesRemoved++;
    }
    if (entriesRemoved > 0) {
      eventListener.onParsingProgress(
          "Cache cleared for path: " + path + " (" + entriesRemoved + " entries)", -1);
    }
  }

  /**
   * Gets the total size of all files by delegating to the underlying FileAccess implementation.
   *
   * <p>Note: This method is not cached as file sizes might change between calls.
   *
   * @return Total size of all files in bytes, or -1 if not supported
   * @throws IOException if there's an error accessing file sizes
   */
  @Override
  public long getTotalSize() throws IOException {
    return delegate.getTotalSize();
  }

  /**
   * Gets cache statistics for monitoring.
   *
   * @return map containing cache hits, misses, and hit ratio
   */
  public Map<String, Object> getCacheStatistics() {
    Map<String, Object> stats = new HashMap<>();
    stats.put("hits", cacheHits);
    stats.put("misses", cacheMisses);
    long total = cacheHits + cacheMisses;
    double hitRatio = total > 0 ? (double) cacheHits / total : 0.0;
    stats.put("hitRatio", hitRatio);
    stats.put("fileExistsCacheSize", fileExistsCache.size());
    stats.put("listFilesCacheSize", listFilesCache.size());
    stats.put("fileContentsCacheSize", fileContentsCache.size());

    // Report cache performance
    eventListener.onParsingProgress(
        String.format("Cache stats: %.1f%% hit ratio (%d hits, %d misses)",
            hitRatio * 100, cacheHits, cacheMisses), -1);

    return stats;
  }
}
