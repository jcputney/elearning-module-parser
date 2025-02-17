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
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.Getter;

/**
 * Implementation of FileAccess for local file access.
 *
 * @author jcputney
 * @version 1.0
 * @see FileAccess
 */
public class LocalFileAccess implements FileAccess {
  @Getter
  private final String rootPath;

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

  @Override
  public boolean fileExists(String path) {
    Path filePath = Paths.get(fullPath(path));
    return Files.exists(filePath);
  }

  @Override
  public List<String> listFiles(String directoryPath) throws IOException {
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
          .map(path -> path.replace(rootPath + File.separator, "")) // Remove root path
          .collect(Collectors.toList());
    } catch (IOException e) {
      throw new IOException("Failed to list files in directory: " + directoryPath, e);
    }
  }

  @Override
  public InputStream getFileContents(String path) throws IOException {
    Path filePath = Paths.get(fullPath(path));

    // Check file existence and read permissions
    if (!fileExists(path)) {
      throw new NoSuchFileException("File not found: " + path);
    }
    if (!Files.isReadable(filePath)) {
      throw new IOException("File is not readable: " + path);
    }

    // Use try-with-resources when returning InputStream (assume caller will close it)
    return Files.newInputStream(filePath, StandardOpenOption.READ);
  }
}
