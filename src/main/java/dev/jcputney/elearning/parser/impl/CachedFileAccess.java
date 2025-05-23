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
import dev.jcputney.elearning.parser.exception.FileAccessException;
import dev.jcputney.elearning.parser.exception.RuntimeFileAccessException;
import dev.jcputney.elearning.parser.util.LoggingUtils;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.slf4j.Logger;

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

  private static final Logger log = LoggingUtils.getLogger(CachedFileAccess.class);

  private final FileAccess delegate;

  // Cache for file existence checks
  private final Map<String, Boolean> fileExistsCache = new ConcurrentHashMap<>();

  // Cache for file listings
  private final Map<String, List<String>> listFilesCache = new ConcurrentHashMap<>();

  // Cache for file contents
  private final Map<String, byte[]> fileContentsCache = new ConcurrentHashMap<>();

  /**
   * Constructs a new {@link CachedFileAccess} instance that wraps the specified {@link FileAccess}
   * implementation.
   *
   * @param delegate The {@link FileAccess} implementation to delegate to.
   * @throws IllegalArgumentException if delegate is null
   */
  public CachedFileAccess(FileAccess delegate) {
    if (delegate == null) {
      throw new IllegalArgumentException("FileAccess delegate cannot be null");
    }
    this.delegate = delegate;
    log.debug("Created CachedFileAccess wrapping {}", delegate
        .getClass()
        .getSimpleName());
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
    return fileExistsCache.computeIfAbsent(path, p -> {
      log.debug("Cache miss for fileExists: {}", p);
      return delegate.fileExists(p);
    });
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
      return listFilesCache.computeIfAbsent(directoryPath, p -> {
        log.debug("Cache miss for listFiles: {}", p);
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

          // Log the error with detailed information
          log.error("Error listing files in directory {}: {}", p, e.getMessage());
          log.debug("Exception details:", e);

          // Wrap the IOException in a FileAccessException to be compatible with computeIfAbsent
          throw new RuntimeFileAccessException(new FileAccessException(
              "Error listing files in directory: %s".formatted(p), e, metadata));
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
      byte[] contents = fileContentsCache.computeIfAbsent(path, p -> {
        log.debug("Cache miss for getFileContents: {}", p);
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

          // Log the error with detailed information
          log.error("Error reading file contents for {}: {}", p, e.getMessage());
          log.debug("Exception details:", e);

          // Wrap the IOException in a FileAccessException to be compatible with computeIfAbsent
          throw new RuntimeFileAccessException(new FileAccessException(
              "Error reading file contents for: " + p, e, metadata));
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
    log.debug("Clearing all caches");
    fileExistsCache.clear();
    listFilesCache.clear();
    fileContentsCache.clear();
  }

  /**
   * Clears the cache for a specific path, forcing subsequent calls for that path to retrieve fresh
   * data from the delegate.
   *
   * @param path The path to clear from the cache.
   * @throws IllegalArgumentException if path is null
   */
  public void clearCache(String path) {
    if (path == null) {
      throw new IllegalArgumentException("Path cannot be null");
    }
    log.debug("Clearing cache for path: {}", path);
    fileExistsCache.remove(path);
    listFilesCache.remove(path);
    fileContentsCache.remove(path);
  }
}
