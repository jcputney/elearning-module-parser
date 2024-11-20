package dev.jcputney.elearning.parser.impl;

import dev.jcputney.elearning.parser.api.FileAccess;
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

/**
 * Implementation of FileAccess for local file access.
 *
 * @author jcputney
 * @version 1.0
 * @see FileAccess
 */
public class LocalFileAccess implements FileAccess {

  @Override
  public boolean fileExists(String path) {
    Path filePath = Paths.get(path);
    return Files.exists(filePath);
  }

  @Override
  public List<String> listFiles(String directoryPath) throws IOException {
    Path dirPath = Paths.get(directoryPath);

    // Validate directory
    if (!Files.exists(dirPath) || !Files.isDirectory(dirPath)) {
      throw new IOException("Provided path is not a valid directory: " + directoryPath);
    }

    try (Stream<Path> paths = Files.list(dirPath)) {
      // Use stream API for more secure listing with filtering on regular files only
      return paths
          .filter(Files::isRegularFile) // Only list regular files
          .map(Path::toString)
          .collect(Collectors.toList());
    } catch (IOException e) {
      throw new IOException("Failed to list files in directory: " + directoryPath, e);
    }
  }

  @Override
  public InputStream getFileContents(String path) throws IOException {
    Path filePath = Paths.get(path);

    // Check file existence and read permissions
    if (!Files.exists(filePath)) {
      throw new NoSuchFileException("File not found: " + path);
    }
    if (!Files.isReadable(filePath)) {
      throw new IOException("File is not readable: " + path);
    }

    // Use try-with-resources when returning InputStream (assume caller will close it)
    return Files.newInputStream(filePath, StandardOpenOption.READ);
  }
}
