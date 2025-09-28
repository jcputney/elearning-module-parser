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
import dev.jcputney.elearning.parser.api.ModuleFileProvider;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

/**
 * Default implementation of the ModuleFileProvider interface.
 *
 * <p>This class delegates file access operations to an underlying FileAccess instance,
 * providing a consistent interface for parsers to use without directly depending on the FileAccess
 * interface.
 */
@SuppressWarnings("ClassCanBeRecord")
public class DefaultModuleFileProvider implements ModuleFileProvider {

  /**
   * The name of the xAPI JavaScript file.
   */
  public static final String XAPI_JS_FILE = "xAPI.js";
  /**
   * The name of the xAPI send statement file.
   */
  public static final String XAPI_SEND_STATEMENT_FILE = "sendStatement.js";
  private final FileAccess fileAccess;

  /**
   * Constructs a DefaultModuleFileProvider with the specified FileAccess instance.
   *
   * @param fileAccess An instance of FileAccess for reading files in the module package.
   * @throws IllegalArgumentException if fileAccess is null
   */
  public DefaultModuleFileProvider(FileAccess fileAccess) {
    if (fileAccess == null) {
      throw new IllegalArgumentException("FileAccess cannot be null");
    }
    this.fileAccess = fileAccess;
  }

  /**
   * Gets the contents of a file as an InputStream.
   *
   * @param path The path to the file.
   * @return An InputStream containing the file contents.
   * @throws IOException If an error occurs while reading the file.
   * @throws IllegalArgumentException if a path is null
   */
  @Override
  public InputStream getFileContents(String path) throws IOException {
    if (path == null) {
      throw new IllegalArgumentException("Path cannot be null");
    }
    return fileAccess.getFileContents(path);
  }

  /**
   * Checks if a file exists at the specified path.
   *
   * @param path The path to check.
   * @return true if the file exists, false otherwise.
   * @throws IllegalArgumentException if a path is null
   */
  @Override
  public boolean fileExists(String path) {
    if (path == null) {
      throw new IllegalArgumentException("Path cannot be null");
    }
    return fileAccess.fileExists(path);
  }

  /**
   * Gets the root path of the module.
   *
   * @return The root path of the module.
   */
  @Override
  public String getRootPath() {
    return fileAccess.getRootPath();
  }

  /**
   * Checks if the module contains xAPI-related files.
   *
   * @return true if xAPI is enabled, false otherwise.
   */
  @Override
  public boolean hasXapiSupport() {
    // Use batch operation to check both files at once
    List<String> xapiFiles = List.of(XAPI_JS_FILE, XAPI_SEND_STATEMENT_FILE);
    Map<String, Boolean> existenceMap = fileAccess.fileExistsBatch(xapiFiles);

    boolean xapiJsExists = existenceMap.getOrDefault(XAPI_JS_FILE, false);
    boolean sendStatementExists = existenceMap.getOrDefault(XAPI_SEND_STATEMENT_FILE, false);

    return xapiJsExists || sendStatementExists;
  }

  /**
   * Lists all files in the specified directory.
   *
   * @param directory The directory to list files from.
   * @return A list of file names in the directory.
   * @throws IOException If an error occurs while listing the files.
   * @throws IllegalArgumentException if the directory is null
   */
  @Override
  public List<String> listFiles(String directory) throws IOException {
    if (directory == null) {
      throw new IllegalArgumentException("Directory cannot be null");
    }
    return fileAccess.listFiles(directory);
  }

  /**
   * Checks if multiple files exist in a batch operation.
   *
   * <p>This implementation delegates to the underlying FileAccess implementation.
   *
   * @param paths List of file paths to check
   * @return Map where keys are the file paths and values indicate whether the file exists
   * @throws IllegalArgumentException if paths is null or contains null elements
   */
  @Override
  public Map<String, Boolean> fileExistsBatch(List<String> paths) {
    if (paths == null) {
      throw new IllegalArgumentException("Paths list cannot be null");
    }

    return fileAccess.fileExistsBatch(paths);
  }

  /**
   * Prefetches common module files for faster subsequent access.
   *
   * <p>This implementation delegates to the underlying FileAccess implementation.
   */
  @Override
  public void prefetchCommonFiles() {
    fileAccess.prefetchCommonFiles();
  }

  /**
   * Gets the total size of all files in the module.
   *
   * <p>This implementation delegates to the underlying FileAccess implementation.
   *
   * @return Total size of all files in bytes, or -1 if not supported
   * @throws IOException if there's an error accessing file sizes
   */
  @Override
  public long getTotalSize() throws IOException {
    return fileAccess.getTotalSize();
  }
}
