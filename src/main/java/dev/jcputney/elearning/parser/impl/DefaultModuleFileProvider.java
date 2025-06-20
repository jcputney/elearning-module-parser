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
import dev.jcputney.elearning.parser.util.LoggingUtils;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import org.slf4j.Logger;

/**
 * Default implementation of the ModuleFileProvider interface.
 *
 * <p>This class delegates file access operations to an underlying FileAccess instance,
 * providing a consistent interface for parsers to use without directly depending on the FileAccess
 * interface.
 */
public class DefaultModuleFileProvider implements ModuleFileProvider {

  /**
   * The name of the xAPI JavaScript file.
   */
  public static final String XAPI_JS_FILE = "xAPI.js";
  /**
   * The name of the xAPI send statement file.
   */
  public static final String XAPI_SEND_STATEMENT_FILE = "sendStatement.js";
  private static final Logger log = LoggingUtils.getLogger(DefaultModuleFileProvider.class);
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
   * @throws IllegalArgumentException if path is null
   */
  @Override
  public InputStream getFileContents(String path) throws IOException {
    if (path == null) {
      throw new IllegalArgumentException("Path cannot be null");
    }
    log.debug("Getting file contents for path: {}", path);
    return fileAccess.getFileContents(path);
  }

  /**
   * Checks if a file exists at the specified path.
   *
   * @param path The path to check.
   * @return true if the file exists, false otherwise.
   * @throws IllegalArgumentException if path is null
   */
  @Override
  public boolean fileExists(String path) {
    if (path == null) {
      throw new IllegalArgumentException("Path cannot be null");
    }
    log.debug("Checking if file exists at path: {}", path);
    return fileAccess.fileExists(path);
  }

  /**
   * Gets the root path of the module.
   *
   * @return The root path of the module.
   */
  @Override
  public String getRootPath() {
    log.debug("Getting root path");
    return fileAccess.getRootPath();
  }

  /**
   * Checks if the module contains xAPI-related files.
   *
   * @return true if xAPI is enabled, false otherwise.
   */
  @Override
  public boolean hasXapiSupport() {
    log.debug("Checking for xAPI support");
    boolean xapiJsExists = false;
    boolean sendStatementExists = false;
    
    try {
      xapiJsExists = fileAccess.fileExists(XAPI_JS_FILE);
    } catch (Exception e) {
      log.debug("Error checking for xAPI JS file: {}", e.getMessage());
    }
    
    try {
      sendStatementExists = fileAccess.fileExists(XAPI_SEND_STATEMENT_FILE);
    } catch (Exception e) {
      log.debug("Error checking for xAPI Statement file: {}", e.getMessage());
    }
    
    boolean hasXapi = xapiJsExists || sendStatementExists;

    if (hasXapi) {
      log.info("xAPI support detected in module");
      if (xapiJsExists) {
        log.debug("Found xAPI JS file: {}", XAPI_JS_FILE);
      }
      if (sendStatementExists) {
        log.debug("Found xAPI Statement file: {}", XAPI_SEND_STATEMENT_FILE);
      }
    } else {
      log.debug("No xAPI support detected in module");
    }

    return hasXapi;
  }

  /**
   * Lists all files in the specified directory.
   *
   * @param directory The directory to list files from.
   * @return A list of file names in the directory.
   * @throws IOException If an error occurs while listing the files.
   * @throws IllegalArgumentException if directory is null
   */
  @Override
  public List<String> listFiles(String directory) throws IOException {
    if (directory == null) {
      throw new IllegalArgumentException("Directory cannot be null");
    }
    log.debug("Listing files in directory: {}", directory);
    return fileAccess.listFiles(directory);
  }
}
