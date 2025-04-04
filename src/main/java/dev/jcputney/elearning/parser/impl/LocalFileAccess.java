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

package dev.jcputney.elearning.parser.impl;

import dev.jcputney.elearning.parser.api.FileAccess;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.stream.Stream;
import lombok.Getter;

/**
 * Implementation of FileAccess for local file access. This class provides methods to check file
 * existence, list files in a directory, and read file contents.
 */
@SuppressWarnings("ClassCanBeRecord")
public class LocalFileAccess implements FileAccess {

  /**
   * The root path for file access. This is the base directory where files are accessed from.
   */
  @Getter
  private final String rootPath;

  /**
   * Constructs a LocalFileAccess instance with the specified root path.
   *
   * @param rootPath The root path for file access.
   * @throws IllegalArgumentException if the root path is null or not a valid directory.
   */
  public LocalFileAccess(String rootPath) {
    if (rootPath == null) {
      throw new IllegalArgumentException("Root path cannot be null");
    }
    if (rootPath.endsWith("/")) {
      rootPath = rootPath.substring(0, rootPath.length() - 1);
    }
    this.rootPath = rootPath;
    if (!Files.isDirectory(Paths.get(rootPath))) {
      throw new IllegalArgumentException("Provided path is not a valid directory: " + rootPath);
    }
  }

  /**
   * Checks if a file exists at the specified path.
   */
  @Override
  public boolean fileExistsInternal(String path) {
    Path filePath = Paths.get(fullPath(path));
    return Files.exists(filePath);
  }

  /**
   * Lists all files in the specified directory.
   *
   * @param directoryPath The path of the directory to list files from.
   * @return A list of file paths relative to the root path.
   * @throws IOException if an error occurs while listing files.
   */
  @Override
  public List<String> listFilesInternal(String directoryPath) throws IOException {
    Path dirPath = Paths.get(fullPath(directoryPath));

    // Validate directory
    if (!Files.exists(dirPath) || !Files.isDirectory(dirPath)) {
      throw new IOException("Provided path is not a valid directory: " + directoryPath);
    }

    try (Stream<Path> paths = Files.list(dirPath)) {
      // Use stream API for more secure listing with filtering on regular files only
      return paths
          .filter(Files::isRegularFile) // Only list regular files
          .map(Path::toString)
          .map(path -> path.replace(rootPath + File.separator, ""))
          .toList();
    } catch (IOException e) {
      throw new IOException("Failed to list files in directory: " + directoryPath, e);
    }
  }

  /**
   * Gets the contents of a file as an InputStream.
   *
   * @param path The path of the file to read.
   * @return An InputStream for reading the file contents.
   * @throws IOException if an error occurs while reading the file.
   */
  @Override
  public InputStream getFileContentsInternal(String path) throws IOException {
    Path filePath = Paths.get(fullPath(path));

    // Check file existence and read permissions
    if (!fileExistsInternal(path)) {
      throw new NoSuchFileException("File not found: " + path);
    }
    if (!Files.isReadable(filePath)) {
      throw new IOException("File is not readable: " + path);
    }

    return Files.newInputStream(filePath, StandardOpenOption.READ);
  }
}
