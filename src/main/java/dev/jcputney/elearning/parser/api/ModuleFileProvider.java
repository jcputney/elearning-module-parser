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

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

/**
 * Interface for providing file access operations specific to module parsing.
 *
 * <p>This interface encapsulates file access operations that are specific to parsing
 * eLearning modules, such as getting manifest files, checking for xAPI files, etc. It separates
 * file access concerns from parsing logic, making the parsers more focused on their primary
 * responsibility.
 *
 * <p>Implementations of this interface should handle the details of accessing files
 * from different storage mechanisms (e.g., ZIP files, local file system, S3, etc.) and provide a
 * consistent interface for parsers to use.
 */
public interface ModuleFileProvider {

  /**
   * Gets the contents of a file as an InputStream.
   *
   * @param path The path to the file.
   * @return An InputStream containing the file contents.
   * @throws IOException If an error occurs while reading the file.
   * @throws IllegalArgumentException if path is null
   */
  InputStream getFileContents(String path) throws IOException;

  /**
   * Checks if a file exists at the specified path.
   *
   * @param path The path to check.
   * @return true if the file exists, false otherwise.
   * @throws IllegalArgumentException if path is null
   */
  boolean fileExists(String path);

  /**
   * Gets the root path of the module.
   *
   * @return The root path of the module.
   */
  String getRootPath();

  /**
   * Checks if the module contains xAPI-related files.
   *
   * @return true if xAPI is enabled, false otherwise.
   */
  boolean hasXapiSupport();

  /**
   * Lists all files in the specified directory.
   *
   * @param directory The directory to list files from.
   * @return A list of file names in the directory.
   * @throws IOException If an error occurs while listing the files.
   * @throws IllegalArgumentException if directory is null
   */
  List<String> listFiles(String directory) throws IOException;

  /**
   * Checks if multiple files exist in a batch operation.
   *
   * <p>Default implementation calls fileExists() for each path individually.
   * Implementations may override this to provide more efficient batch operations.
   *
   * @param paths List of file paths to check
   * @return Map where keys are the file paths and values indicate whether the file exists
   * @throws IllegalArgumentException if paths is null or contains null elements
   */
  default Map<String, Boolean> fileExistsBatch(List<String> paths) {
    if (paths == null) {
      throw new IllegalArgumentException("Paths list cannot be null");
    }

    Map<String, Boolean> results = new java.util.HashMap<>();
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
   * <p>Default implementation does nothing. Implementations that support
   * caching (like S3) may override this to pre-load commonly accessed files.
   */
  default void prefetchCommonFiles() {
    // Default implementation does nothing
  }

  /**
   * Gets the total size of all files in the module.
   *
   * <p>This method calculates the sum of all file sizes in the module.
   * Implementations that maintain file size caches can provide efficient calculation of the total
   * module size.
   *
   * @return Total size of all files in bytes, or -1 if not supported
   * @throws IOException if there's an error accessing file sizes
   */
  default long getTotalSize() throws IOException {
    // Default implementation returns -1 to indicate not supported
    return -1;
  }
}
