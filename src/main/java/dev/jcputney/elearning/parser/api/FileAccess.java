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
}
