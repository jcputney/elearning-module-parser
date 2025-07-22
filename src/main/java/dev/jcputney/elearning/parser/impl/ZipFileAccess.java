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
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import lombok.Getter;

/**
 * An implementation of the {@link FileAccess} interface for accessing files within a ZIP archive.
 * This class allows file existence checks, file listing, and retrieving file contents from a ZIP
 * file.
 */
public class ZipFileAccess implements FileAccess, AutoCloseable {

  @Getter
  private final String rootPath;

  private final ZipFile zipFile;
  private final String zipFilePath;

  /**
   * Constructs a new {@link ZipFileAccess} instance for the specified ZIP path.
   *
   * @param zipFilePath The path to the ZIP file.
   * @throws IOException If the ZIP file can't be opened.
   */
  public ZipFileAccess(String zipFilePath) throws IOException {
    this.zipFilePath = zipFilePath;
    try {
      this.zipFile = new ZipFile(zipFilePath);
    } catch (IOException e) {
      throw new IOException(
          "Failed to open ZIP file: '" + zipFilePath + "' (" + e.getMessage() + ")", e);
    }
    this.rootPath = getInternalRootDirectory();
  }

  /**
   * Checks if a file exists within the ZIP archive.
   *
   * @param path The path to check (guaranteed to be non-null).
   * @return True if the file exists in the ZIP archive, false otherwise.
   */
  @Override
  public boolean fileExistsInternal(String path) {
    return zipFile.getEntry(fullPath(path)) != null;
  }

  /**
   * Lists all files within a specified directory in the ZIP archive.
   *
   * @param directoryPath The directory to list files from, for example, "folder/" (guaranteed to be
   * non-null).
   * @return A list of file paths within the directory.
   */
  @Override
  public List<String> listFilesInternal(String directoryPath) {
    List<String> fileList = new ArrayList<>();
    Enumeration<? extends ZipEntry> entries = zipFile.entries();

    while (entries.hasMoreElements()) {
      ZipEntry entry = entries.nextElement();
      String entryName = entry.getName();

      // Check if the entry is within the specified directory
      if (entryName.startsWith(fullPath(directoryPath)) && !entry.isDirectory()) {
        fileList.add(entryName);
      }
    }
    return fileList;
  }

  /**
   * Retrieves the contents of a file within the ZIP archive as an InputStream.
   *
   * @param path The path to retrieve contents from (guaranteed to be non-null).
   * @return An InputStream of the file contents.
   * @throws IOException if the file can't be read.
   */
  @Override
  public InputStream getFileContentsInternal(String path) throws IOException {
    return getFileContentsInternal(path, null);
  }

  /**
   * Retrieves the contents of a file within the ZIP archive as an InputStream with optional
   * progress tracking.
   *
   * @param path The path to retrieve contents from (guaranteed to be non-null).
   * @param progressListener Optional progress listener for tracking large file operations.
   * @return An InputStream of the file contents.
   * @throws IOException if the file can't be read.
   */
  public InputStream getFileContentsInternal(String path,
      StreamingProgressListener progressListener) throws IOException {
    ZipEntry entry = zipFile.getEntry(fullPath(path));

    if (entry == null) {
      // Provide helpful information about available files
      String suggestion = getSimilarFiles(path);
      throw new IOException("File not found in ZIP archive: '" + path + "' (full path: '" +
          fullPath(path) + "') in ZIP file '" + zipFilePath + "'" +
          (rootPath.isEmpty() ? "" : " with internal root '" + rootPath + "'") + suggestion);
    }

    InputStream inputStream = zipFile.getInputStream(entry);

    // Get file size for progress tracking
    long fileSize = entry.getSize();

    // Apply streaming enhancements
    return StreamingUtils.createEnhancedStream(inputStream, fileSize, progressListener);
  }

  /**
   * Closes the ZIP file to release resources.
   *
   * @throws IOException if an error occurs while closing the ZIP file.
   */
  public void close() throws IOException {
    try {
      zipFile.close();
    } catch (IOException e) {
      throw new IOException(
          "Failed to close ZIP file: '" + zipFilePath + "' (" + e.getMessage() + ")", e);
    }
  }

  /**
   * Gets the total size of all files in the ZIP archive.
   *
   * <p>This method iterates through all entries in the ZIP and sums their uncompressed sizes.
   *
   * @return Total size of all files in bytes (uncompressed)
   * @throws IOException if there's an error accessing the ZIP file
   */
  @Override
  public long getTotalSize() throws IOException {
    long totalSize = 0;
    Enumeration<? extends ZipEntry> entries = zipFile.entries();

    while (entries.hasMoreElements()) {
      ZipEntry entry = entries.nextElement();
      if (!entry.isDirectory()) {
        // Get uncompressed size
        long size = entry.getSize();
        if (size >= 0) {
          totalSize += size;
        }
      }
    }

    return totalSize;
  }

  /**
   * Determines the internal root directory within the ZIP file.
   *
   * <p>This method analyzes the ZIP entries to determine if all files are contained
   * within a single top-level directory. If so, it returns that directory name as the root path.
   * Otherwise, it returns an empty string, indicating that files are at the root of the ZIP.</p>
   *
   * @return The detected internal root directory or an empty string if files are at the root.
   */
  private String getInternalRootDirectory() {
    // Keep track of all distinct top-level folders encountered.
    Set<String> topLevelDirs = new HashSet<>();

    Enumeration<? extends ZipEntry> entries = this.zipFile.entries();
    while (entries.hasMoreElements()) {
      ZipEntry entry = entries.nextElement();
      String entryName = entry.getName();

      // Split on '/', the first part is the top-level "directory"
      // unless the file is directly in the root and has no slash.
      int slashIndex = entryName.indexOf('/');

      if (slashIndex > 0) {
        // The substring up to the first slash is the top-level directory
        String topLevel = entryName.substring(0, slashIndex);
        topLevelDirs.add(topLevel);

        // If more than one distinct name, break early
        if (topLevelDirs.size() > 1) {
          return "";
        }
      } else {
        // If slashIndex == -1, means no slash in name, for example, "file.txt",
        // so there's a file right at the root.
        // That means it isn't a single-dir.
        return "";
      }
    }

    // If it gets here, either have exactly one top-level directory or none.
    // "None" shouldn't happen in a typical ZIP but handle the edge case.
    return topLevelDirs.size() == 1 ? topLevelDirs
        .iterator()
        .next() : "";
  }

  /**
   * Provides suggestions for similar files when a file is not found.
   *
   * @param targetPath The path that was not found.
   * @return A string with suggestions or empty string if no suggestions available.
   */
  private String getSimilarFiles(String targetPath) {
    List<String> allFiles = new ArrayList<>();
    Enumeration<? extends ZipEntry> entries = zipFile.entries();

    while (entries.hasMoreElements()) {
      ZipEntry entry = entries.nextElement();
      if (!entry.isDirectory()) {
        allFiles.add(entry.getName());
      }
    }

    if (allFiles.isEmpty()) {
      return ". ZIP archive appears to be empty or contains only directories.";
    }

    // Find files with similar names or in the same directory
    String targetName = targetPath.contains("/") ?
        targetPath.substring(targetPath.lastIndexOf("/") + 1) : targetPath;
    String targetDir = targetPath.contains("/") ?
        targetPath.substring(0, targetPath.lastIndexOf("/")) : "";

    List<String> suggestions = allFiles
        .stream()
        .filter(file -> file
            .toLowerCase()
            .contains(targetName.toLowerCase()) ||
            (targetDir.isEmpty() || file.startsWith(targetDir)))
        .limit(3)
        .toList();

    if (!suggestions.isEmpty()) {
      return ". Similar files: " + String.join(", ", suggestions);
    } else {
      return ". Available files: " + allFiles
          .stream()
          .limit(5)
          .reduce((a, b) -> a + ", " + b)
          .orElse("none") +
          (allFiles.size() > 5 ? " (and " + (allFiles.size() - 5) + " more)" : "");
    }
  }
}
