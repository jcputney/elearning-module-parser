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
import dev.jcputney.elearning.parser.api.StreamingProgressListener;
import dev.jcputney.elearning.parser.util.StreamingUtils;
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
      Path path = Paths.get(rootPath);
      String context = Files.exists(path) ? 
          (Files.isRegularFile(path) ? "path points to a file" : "path exists but is not a directory") :
          "path does not exist";
      throw new IllegalArgumentException(
          "Invalid root directory for LocalFileAccess: '" + rootPath + "' (" + context + ")");
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
    if (!Files.exists(dirPath)) {
      throw new IOException("Directory not found: '" + directoryPath + "' (full path: '" + 
          dirPath.toAbsolutePath() + "') in root '" + getRootPath() + "'");
    }
    if (!Files.isDirectory(dirPath)) {
      String fileType = Files.isRegularFile(dirPath) ? "regular file" : "special file";
      throw new IOException("Path is not a directory: '" + directoryPath + "' (is a " + fileType + 
          ") in root '" + getRootPath() + "'");
    }

    try (Stream<Path> paths = Files.list(dirPath)) {
      // Use stream API for more secure listing with filtering on regular files only
      Path basePath = Paths.get(getRootPath());
      return paths
          .filter(Files::isRegularFile) // Only list regular files
          .map(basePath::relativize)
          .map(Path::toString)
          .toList();
    } catch (IOException e) {
      throw new IOException("Failed to list files in directory: '" + directoryPath + 
          "' (full path: '" + dirPath.toAbsolutePath() + "') in root '" + getRootPath() + 
          "': " + e.getMessage(), e);
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
    return getFileContentsInternal(path, null);
  }

  /**
   * Gets the contents of a file as an InputStream with optional progress tracking.
   *
   * @param path The path of the file to read.
   * @param progressListener Optional progress listener for tracking large file operations.
   * @return An InputStream for reading the file contents.
   * @throws IOException if an error occurs while reading the file.
   */
  public InputStream getFileContentsInternal(String path, StreamingProgressListener progressListener) throws IOException {
    Path filePath = Paths.get(fullPath(path));

    // Check file existence and read permissions
    if (!fileExistsInternal(path)) {
      throw new NoSuchFileException("File not found: '" + path + "' (full path: '" + 
          filePath.toAbsolutePath() + "') in root '" + getRootPath() + "'");
    }
    if (!Files.isReadable(filePath)) {
      String details = "";
      try {
        long size = Files.size(filePath);
        boolean isDirectory = Files.isDirectory(filePath);
        details = " (size: " + size + " bytes, type: " + (isDirectory ? "directory" : "file") + ")";
      } catch (IOException ignored) {
        details = " (unable to read file attributes)";
      }
      throw new IOException("File is not readable: '" + path + "' (full path: '" + 
          filePath.toAbsolutePath() + "')" + details + " in root '" + getRootPath() + "'");
    }

    InputStream inputStream = Files.newInputStream(filePath, StandardOpenOption.READ);
    
    // Get file size for progress tracking
    long fileSize = Files.size(filePath);
    
    // Apply streaming enhancements
    return StreamingUtils.createEnhancedStream(inputStream, fileSize, progressListener);
  }

  /**
   * Gets the total size of all files in the module.
   * 
   * <p>This method recursively walks the directory tree and sums the sizes of all files.
   *
   * @return Total size of all files in bytes
   * @throws IOException if there's an error accessing file sizes
   */
  @Override
  public long getTotalSize() throws IOException {
    Path rootDir = Paths.get(rootPath);
    
    try (Stream<Path> paths = Files.walk(rootDir)) {
      return paths
          .filter(Files::isRegularFile)
          .mapToLong(path -> {
            try {
              return Files.size(path);
            } catch (IOException e) {
              // Log error and continue with 0 for this file
              return 0;
            }
          })
          .sum();
    }
  }
}
