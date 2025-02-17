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

/**
 * An interface for accessing files within a package or directory. Implementations of this interface
 * should provide methods for checking if a file exists, listing files within a directory, and
 * retrieving file contents as an InputStream. An implementation of this interface should be
 * provided to parsers for reading files within a module package. The default implementation is the
 * {@link dev.jcputney.elearning.parser.impl.LocalFileAccess} class.
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
   * @param path The file path to check.
   * @return True if the file exists, false otherwise.
   */
  boolean fileExists(String path);

  /**
   * Lists all files within a specified directory path.
   *
   * @param directoryPath The directory to list files from.
   * @return A list of file paths within the directory.
   */
  List<String> listFiles(String directoryPath) throws IOException;

  /**
   * Retrieves the contents of a file as an InputStream.
   *
   * @param path The file path to retrieve contents from.
   * @return An InputStream of the file contents.
   * @throws IOException if the file cannot be read.
   */
  InputStream getFileContents(String path) throws IOException;



  /**
   * Constructs the full path for the given relative or absolute path.
   *
   * @param path The relative or absolute path for which the full path is to be generated.
   *             If the path starts with a "/", it is treated as an absolute path and the
   *             leading "/" is removed. Otherwise, the path is treated as relative and
   *             the rootPath is prefixed to form the full path.
   * @return The constructed full path as a string.
   */
  default String fullPath(String path) {
    if (path.startsWith("/")) {
      return path.substring(1);
    }
    return getRootPath() + "/" + path;
  }
}
