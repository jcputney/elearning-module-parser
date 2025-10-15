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

package dev.jcputney.elearning.parser.api;

import dev.jcputney.elearning.parser.impl.access.LocalFileAccess;
import dev.jcputney.elearning.parser.impl.access.ZipFileAccess;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.commons.lang3.StringUtils;

/**
 * An interface for accessing files within a package or directory. This interface is a core
 * component of the module parser architecture, providing a unified way to access files regardless
 * of their storage mechanism (local file system, ZIP archive, cloud storage, etc.).
 *
 * <p>Implementations of this interface should provide methods for:
 * <ul>
 *   <li>Checking if a file exists</li>
 *   <li>Listing files within a directory</li>
 *   <li>Retrieving file contents as an InputStream</li>
 * </ul>
 *
 * <p>An implementation of this interface should be provided to parsers for reading files within
 * a module package. The default implementation is the
 * {@link LocalFileAccess} class, but other implementations
 * are available for different storage mechanisms:
 * <ul>
 *   <li>{@link ZipFileAccess} - For accessing files in ZIP archives</li>
 *   <li>S3FileAccess implementations - For accessing files in AWS S3 buckets</li>
 * </ul>
 *
 * <p>This interface follows the Strategy pattern, allowing different file access strategies to be
 * interchanged without affecting the parsers that use them.
 */
public interface FileAccess {

  /**
   * Retrieves the root path used for file access.
   *
   * @return The root path used for file access.
   */
  String getRootPath();

  /**
   * Checks if a file exists at the given path.
   *
   * @param path The path to check.
   * @return True if the file exists, false otherwise.
   * @throws IllegalArgumentException if a path is null
   */
  default boolean fileExists(String path) {
    if (path == null) {
      throw new IllegalArgumentException("Path cannot be null");
    }
    return fileExistsInternal(path);
  }

  /**
   * Internal method to check if a file exists at the given path. This method is called by the
   * default implementation of {@link #fileExists(String)}.
   *
   * @param path The path to check (guaranteed to be non-null).
   * @return True if the file exists, false otherwise.
   */
  boolean fileExistsInternal(String path);

  /**
   * Lists all files within a specified directory path.
   *
   * @param directoryPath The directory to list files from.
   * @return A list of file paths within the directory.
   * @throws IOException if there's an error accessing the directory or listing its contents.
   * @throws IllegalArgumentException if directoryPath is null
   */
  default List<String> listFiles(String directoryPath) throws IOException {
    if (directoryPath == null) {
      throw new IllegalArgumentException("Directory path cannot be null");
    }
    return listFilesInternal(directoryPath);
  }

  /**
   * Checks if a file entry represents a root-level directory or if multiple top-level directories
   * are present in the provided set.
   *
   * @param topLevelDirs A set of strings representing the top-level directories encountered so far.
   * This set is modified by the method to add new entries if applicable.
   * @param entryName The name of the file entry to evaluate, potentially containing a directory
   * path.
   * @return True if the file entry is at the root level or if more than one top-level directory is
   * present; false otherwise.
   */
  default boolean checkForRootPath(Set<String> topLevelDirs, String entryName) {
    int slashIndex = entryName.indexOf('/');

    if (slashIndex > 0) {
      String topLevel = entryName.substring(0, slashIndex);
      topLevelDirs.add(topLevel);

      return topLevelDirs.size() > 1;
    } else {
      // File at root level
      return true;
    }
  }

  /**
   * Internal method to list all files within a specified directory path. This method is called by
   * the default implementation of {@link #listFiles(String)}.
   *
   * @param directoryPath The directory to list files from (guaranteed to be non-null).
   * @return A list of file paths within the directory.
   * @throws IOException if there's an error accessing the directory or listing its contents.
   */
  List<String> listFilesInternal(String directoryPath) throws IOException;

  /**
   * Retrieves the contents of a file as an InputStream.
   *
   * @param path The path to retrieve contents from.
   * @return An InputStream of the file contents.
   * @throws IOException if the file can't be read.
   * @throws IllegalArgumentException if a path is null
   */
  default InputStream getFileContents(String path) throws IOException {
    if (path == null) {
      throw new IllegalArgumentException("Path cannot be null");
    }
    return getFileContentsInternal(path);
  }

  /**
   * Internal method to retrieve the contents of a file as an InputStream. This method is called by
   * the default implementation of {@link #getFileContents(String)}.
   *
   * @param path The path to retrieve contents from (guaranteed to be non-null).
   * @return An InputStream of the file contents.
   * @throws IOException if the file can't be read.
   */
  InputStream getFileContentsInternal(String path) throws IOException;


  /**
   * Constructs the full path for the given relative or absolute path.
   *
   * <p>This method handles path normalization according to the following rules:
   * <ul>
   *   <li>If the path starts with a forward slash ("/"), it is treated as an absolute path and the
   *       leading "/" is removed.</li>
   *   <li>Otherwise, the path is treated as relative and the rootPath is prefixed to form the full path.</li>
   *   <li>If rootPath is empty, the relative path is returned as-is without any prefix.</li>
   * </ul>
   *
   * <p>This method is used internally by implementations to normalize paths before accessing files.
   *
   * @param path The relative or absolute path for which the full path is to be generated.
   * @return The constructed full path as a string.
   * @throws IllegalArgumentException if a path is null
   */
  default String fullPath(String path) {
    if (path == null) {
      throw new IllegalArgumentException("Path cannot be null");
    }
    if (path.startsWith("/")) {
      return path.substring(1);
    }
    return StringUtils.isEmpty(getRootPath()) ? path : getRootPath() + "/" + path;
  }

  /**
   * Checks if multiple files exist in a batch operation.
   *
   * <p>Default implementation calls fileExists() for each path individually.
   * Implementations that support batch operations (like S3) should override this method to provide
   * more efficient batch checking.
   *
   * @param paths List of file paths to check
   * @return Map where keys are the file paths and values indicate whether the file exists
   * @throws IllegalArgumentException if paths is null or contains null elements
   */
  default Map<String, Boolean> fileExistsBatch(List<String> paths) {
    if (paths == null) {
      throw new IllegalArgumentException("Paths list cannot be null");
    }

    Map<String, Boolean> results = new HashMap<>();
    for (String path : paths) {
      if (path != null) {
        results.put(path, fileExists(path));
      }
    }
    return results;
  }

  /**
   * Prefetches common module files for faster subsequent access.
   *
   * <p>The default implementation does nothing. Implementations that support
   * caching (like S3) may override this to preload commonly accessed files.
   */
  default void prefetchCommonFiles() {
    // Default implementation does nothing
  }

  /**
   * Gets a list of all files in the module.
   *
   * <p>This method should return a cached list of all files available in the module.
   * Implementations should scan the module once and cache the results for efficiency.
   *
   * @return List of all file paths in the module
   * @throws IOException if there's an error accessing the module contents
   */
  default List<String> getAllFiles() throws IOException {
    // Default implementation lists files from the root directory
    return listFiles("");
  }

  /**
   * Clears any internal caches maintained by this FileAccess instance.
   *
   * <p>This method should be called when the underlying storage might have changed
   * or when memory needs to be freed. Implementations that maintain caches should override this
   * method to clear their specific caches.
   */
  default void clearCaches() {
    // Default implementation does nothing
  }

  /**
   * Gets the total size of all files in the module.
   *
   * <p>This method calculates the sum of all file sizes in the module.
   * Implementations that maintain file size caches can provide efficient calculation of the total
   * module size.
   *
   * @return Total size of all files in bytes
   * @throws IOException if there's an error accessing file sizes
   */
  default long getTotalSize() throws IOException {
    // Default implementation returns -1 to indicate not supported
    return -1;
  }
}
