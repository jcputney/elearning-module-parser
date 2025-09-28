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

  /**
   * The {@code delegate} field is the primary {@link FileAccess} implementation that this
   * {@link CachedFileAccess} instance wraps. The delegate is responsible for performing actual file
   * access operations such as reading or writing to the filesystem.
   * <p>
   * This field serves as the backend file access provider, with caching layers and optional event
   * listeners applied in the {@link CachedFileAccess} implementation to optimize and enhance the
   * file access process.
   * <p>
   * The {@code delegate} must not be {@code null}, and it is initialized via the constructor to
   * ensure valid file access delegation.
   */
  private final FileAccess delegate;

  /**
   * An optional {@link ParsingEventListener} used to monitor or respond to caching-related events
   * occurring in the {@link CachedFileAccess} class. This listener is primarily intended for
   * providing diagnostic or logging capabilities without requiring a specific logging framework,
   * enabling greater flexibility for applications.
   * <p>
   * When present, the {@code eventListener} receives notifications about internal events related to
   * caching behavior, such as parsing progress, metadata loading, or detection processes. When
   * {@code null}, no events are received or processed.
   * <p>
   * The {@link ParsingEventListener} interface includes a default no-operation implementation,
   * meaning its methods can be selectively overridden as needed. This makes it adaptable for simple
   * logging, progress tracking, or debugging scenarios.
   * <p>
   * This field is immutable, ensuring thread-safe usage.
   */
  private final ParsingEventListener eventListener;

  /**
   * A thread-safe cache that maps file paths to their existence status. Used to improve performance
   * by avoiding repeated checks with the underlying {@link FileAccess} implementation.
   * <p>
   * The cache stores the paths as keys and a boolean indicating whether the file exists (`true` if
   * the file exists, `false` otherwise) as values. This helps in reducing redundant file existence
   * checks and optimizes operations that query file presence frequently.
   * <p>
   * The cache is maintained per path and is automatically used during relevant operations in the
   * containing class. It typically aids in minimizing filesystem access by leveraging previously
   * computed results.
   */
  private final Map<String, Boolean> fileExistsCache = new ConcurrentHashMap<>();

  /**
   * A cache for storing the results of directory file listings.
   * <p>
   * This map associates directory paths (as keys) with lists of file paths (as values). It is used
   * to cache the results of file listing operations performed by the {@link CachedFileAccess}
   * class, reducing the need to repeatedly access the underlying {@link FileAccess}
   * implementation.
   * <p>
   * The use of a {@link ConcurrentHashMap} ensures thread-safety, allowing multiple threads to
   * access and update the cache concurrently.
   */
  private final Map<String, List<String>> listFilesCache = new ConcurrentHashMap<>();

  /**
   * A cache for storing file contents, mapped by their paths. This is used to avoid redundant file
   * reads by caching the full content of each accessed file as a byte array. The cache is
   * thread-safe and backed by a {@link ConcurrentHashMap}.
   * <p>
   * Keys represent file paths as strings, and values are byte arrays containing the corresponding
   * file contents.
   * <p>
   * The cache is utilized by methods that retrieve file contents to speed up subsequent file access
   * and reduce I/O overhead. Cached contents are updated as needed when the underlying file system
   * changes, depending on the cache management logic in the enclosing class.
   */
  private final Map<String, byte[]> fileContentsCache = new ConcurrentHashMap<>();

  /**
   * Tracks the number of successful cache hits for file-related operations in the
   * {@link CachedFileAccess} class.
   * <p>
   * A cache hit occurs when a file's metadata or contents are successfully retrieved from the
   * cache, rather than fetching it anew from the underlying file system or delegate
   * {@link FileAccess} implementation.
   * <p>
   * This metric can be used to monitor cache performance and efficiency.
   */
  private long cacheHits = 0;

  /**
   * Tracks the number of cache misses encountered during operations that utilize caching.
   * <p>
   * A cache miss occurs when a requested item is not found in the cache, requiring the system to
   * retrieve it from the underlying data source. This variable is incremented each time such an
   * event occurs.
   */
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
