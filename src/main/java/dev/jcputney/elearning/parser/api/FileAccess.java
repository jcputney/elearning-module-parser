/*
 * Copyright (c) 2024. Jonathan Putney
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
 * {@link dev.jcputney.elearning.parser.impl.LocalFileAccess} class, but other implementations
 * are available for different storage mechanisms:
 * <ul>
 *   <li>{@link dev.jcputney.elearning.parser.impl.ZipFileAccess} - For accessing files in ZIP archives</li>
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
   * @throws IllegalArgumentException if path is null
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
   * @throws IllegalArgumentException if path is null
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
   * @throws IllegalArgumentException if path is null
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
}
