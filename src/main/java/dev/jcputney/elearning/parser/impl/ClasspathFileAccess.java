package dev.jcputney.elearning.parser.impl;

import dev.jcputney.elearning.parser.api.FileAccess;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * Implementation of {@link FileAccess} that reads files from the classpath, allowing testing of
 * modules stored in the resources directory without needing actual filesystem access.
 */
public class ClasspathFileAccess implements FileAccess {

  /**
   * Checks if a file exists on the classpath at the given path.
   *
   * @param path The file path to check.
   * @return True if the file exists on the classpath, false otherwise.
   */
  @Override
  public boolean fileExists(String path) {
    return getClass().getClassLoader().getResource(path) != null;
  }

  /**
   * Lists all files within a specified directory path on the classpath.
   *
   * @param directoryPath The directory to list files from.
   * @return A list of file paths within the specified directory.
   * @throws IOException If there is an issue accessing the files.
   */
  @Override
  public List<String> listFiles(String directoryPath) throws IOException {
    List<String> fileList = new ArrayList<>();
    Optional<String> jarPath = getJarPath();

    if (jarPath.isPresent()) {
      try (JarFile jarFile = new JarFile(jarPath.get())) {
        Enumeration<JarEntry> entries = jarFile.entries();
        while (entries.hasMoreElements()) {
          JarEntry entry = entries.nextElement();
          if (entry.getName().startsWith(directoryPath) && !entry.isDirectory()) {
            fileList.add(entry.getName());
          }
        }
      }
    } else {
      // Fall back to loading from classpath directly (not inside a JAR)
      var dirUrl = getClass().getClassLoader().getResource(directoryPath);
      if (dirUrl != null) {
        fileList.add(dirUrl.getFile());
      }
    }
    return fileList;
  }

  /**
   * Retrieves the contents of a file as an InputStream from the classpath.
   *
   * @param path The file path to retrieve contents from.
   * @return An InputStream of the file contents.
   * @throws IOException if the file cannot be read.
   */
  @Override
  public InputStream getFileContents(String path) throws IOException {
    InputStream inputStream = getClass().getClassLoader().getResourceAsStream(path);
    if (inputStream == null) {
      throw new IOException("File not found on classpath: " + path);
    }
    return inputStream;
  }

  /**
   * Helper method to check if the code is running from a JAR file and return its path.
   *
   * @return An Optional containing the JAR path if available, or empty if not running from a JAR.
   */
  private Optional<String> getJarPath() {
    String jarPath = Objects.requireNonNull(getClass().getResource("")).getPath();
    if (jarPath.startsWith("file:") && jarPath.contains("!")) {
      jarPath = jarPath.substring(5, jarPath.indexOf("!"));
      return Optional.of(jarPath);
    }
    return Optional.empty();
  }
}
