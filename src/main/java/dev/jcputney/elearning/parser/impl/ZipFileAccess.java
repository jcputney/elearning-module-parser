package dev.jcputney.elearning.parser.impl;

import dev.jcputney.elearning.parser.api.FileAccess;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * An implementation of the {@link FileAccess} interface for accessing files within a ZIP archive.
 * This class allows file existence checks, file listing, and retrieving file contents from a ZIP
 * file.
 */
public class ZipFileAccess implements FileAccess, AutoCloseable {

  private final ZipFile zipFile;

  /**
   * Constructs a new {@link ZipFileAccess} instance for the specified ZIP file path.
   *
   * @param zipFilePath The path to the ZIP file.
   * @throws IOException If the ZIP file cannot be opened.
   */
  public ZipFileAccess(String zipFilePath) throws IOException {
    this.zipFile = new ZipFile(zipFilePath);
  }

  /**
   * Checks if a file exists within the ZIP archive.
   *
   * @param path The file path to check.
   * @return True if the file exists in the ZIP archive, false otherwise.
   */
  @Override
  public boolean fileExists(String path) {
    return zipFile.getEntry(path) != null;
  }

  /**
   * Lists all files within a specified directory in the ZIP archive.
   *
   * @param directoryPath The directory to list files from (e.g., "folder/").
   * @return A list of file paths within the directory.
   */
  @Override
  public List<String> listFiles(String directoryPath) {
    List<String> fileList = new ArrayList<>();
    Enumeration<? extends ZipEntry> entries = zipFile.entries();

    while (entries.hasMoreElements()) {
      ZipEntry entry = entries.nextElement();
      String entryName = entry.getName();

      // Check if the entry is within the specified directory
      if (entryName.startsWith(directoryPath) && !entry.isDirectory()) {
        fileList.add(entryName);
      }
    }
    return fileList;
  }

  /**
   * Retrieves the contents of a file within the ZIP archive as an InputStream.
   *
   * @param path The file path to retrieve contents from.
   * @return An InputStream of the file contents.
   * @throws IOException if the file cannot be read.
   */
  @Override
  public InputStream getFileContents(String path) throws IOException {
    ZipEntry entry = zipFile.getEntry(path);

    if (entry == null) {
      throw new IOException("File not found in ZIP archive: " + path);
    }

    return zipFile.getInputStream(entry);
  }

  /**
   * Closes the ZIP file to release resources.
   *
   * @throws IOException if an error occurs while closing the ZIP file.
   */
  public void close() throws IOException {
    zipFile.close();
  }
}