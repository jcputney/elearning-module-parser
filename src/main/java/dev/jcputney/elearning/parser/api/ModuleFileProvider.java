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
}
