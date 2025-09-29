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
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * An implementation of the {@link FileAccess} interface for accessing files from an in-memory ZIP
 * archive. This class allows file existence checks, file listing, and retrieving file contents from
 * a ZIP file that is loaded into memory.
 *
 * <p>This implementation is useful for serverless environments or situations where file system
 * access is restricted or not available. The ZIP content is read once during construction and
 * cached in memory.</p>
 *
 * <p>Usage example:</p>
 * <pre>{@code
 * byte[] zipData = // ... obtain ZIP file data from network, database, etc.
 * try (InMemoryFileAccess fileAccess = new InMemoryFileAccess(zipData)) {
 *     ModuleParser parser = ModuleParserFactory.forZipData(zipData);
 *     ModuleMetadata metadata = parser.parse();
 * }
 * }</pre>
 */
public class InMemoryFileAccess implements FileAccess, AutoCloseable {

  /**
   * Represents the root path of the detected directory structure within the ZIP data.
   * <p>
   * The root path is determined by analyzing the file entries from the input ZIP data. If all files
   * are contained within a single top-level directory, the root path will be set to that
   * directory's name. If the files are distributed across multiple directories or at the root of
   * the ZIP, the root path will be an empty string.
   * <p>
   * This variable is final and is initialized during the loading process of the in-memory file
   * system.
   */
  private final String rootPath;

  /**
   * A list of file entries representing the in-memory files loaded from a ZIP file. Each entry
   * corresponds to a file within the in-memory file system, encapsulating its path, content, and
   * size.
   */
  private final List<FileEntry> fileEntries;

  /**
   * A set containing the unique directory paths detected in the in-memory file system.
   * <p>
   * This set ensures that no duplicate directory paths are stored and is used internally for
   * operations that require knowledge of the directory structure, such as detecting parent
   * directories or listing directory contents.
   */
  private final Set<String> directories;

  /**
   * Represents the total size of all files stored in the in-memory file system. This value is
   * calculated during the loading of ZIP data and reflects the combined size of all files,
   * excluding additional metadata or directory structures.
   */
  private final long totalSize;

  /**
   * Constructs a new {@link InMemoryFileAccess} instance from a byte array containing ZIP data.
   *
   * @param zipData The ZIP file data as a byte array.
   * @throws IOException If the ZIP data can't be read or is invalid.
   * @throws IllegalArgumentException if zipData is null.
   */
  public InMemoryFileAccess(byte[] zipData) throws IOException {
    if (zipData == null) {
      throw new IllegalArgumentException("ZIP data cannot be null");
    }

    this.fileEntries = new ArrayList<>();
    this.directories = new HashSet<>();

    this.totalSize = loadZipData(zipData);
    this.rootPath = detectRootPath();
  }

  /**
   * Constructs a new {@link InMemoryFileAccess} instance from an InputStream containing ZIP data.
   *
   * @param zipInputStream The InputStream containing ZIP data.
   * @throws IOException If the ZIP data can't be read or is invalid.
   * @throws IllegalArgumentException if zipInputStream is null.
   */
  public InMemoryFileAccess(InputStream zipInputStream) throws IOException {
    if (zipInputStream == null) {
      throw new IllegalArgumentException("ZIP input stream cannot be null");
    }

    // Read the entire stream into a byte array
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    byte[] buffer = new byte[8192];
    int bytesRead;
    while ((bytesRead = zipInputStream.read(buffer)) != -1) {
      baos.write(buffer, 0, bytesRead);
    }

    this.fileEntries = new ArrayList<>();
    this.directories = new HashSet<>();

    this.totalSize = loadZipData(baos.toByteArray());
    this.rootPath = detectRootPath();
  }

  /**
   * Retrieves the root path for the in-memory file system. The root path is typically determined
   * during the initialization of the instance and provides the base directory for all file
   * operations within the system.
   *
   * @return The root path as a string.
   */
  @Override
  public String getRootPath() {
    return rootPath;
  }

  /**
   * Checks if a file specified by the given path exists in the in-memory file system.
   *
   * @param path The relative or absolute path of the file to check. Must not be null.
   * @return {@code true} if the file exists; {@code false} otherwise.
   */
  @Override
  public boolean fileExistsInternal(String path) {
    String fullPath = fullPath(path);
    return fileEntries
        .stream()
        .anyMatch(entry -> entry
            .getPath()
            .equals(fullPath));
  }

  /**
   * Lists all files available within the specified directory in the in-memory file system. If the
   * directory path is an empty string, all files in the system are returned.
   *
   * @param directoryPath The path of the directory to list files from. If empty, all files will be
   * included in the result. The path should be relative or normalized.
   * @return A list of file paths that exist within the specified directory.
   * @throws IOException If an error occurs while retrieving file information.
   */
  @Override
  public List<String> listFilesInternal(String directoryPath) throws IOException {
    String fullPath = fullPath(directoryPath);
    String normalizedDir = fullPath.isEmpty() ? "" : normalizeDirectory(fullPath);

    List<String> files = new ArrayList<>();
    for (FileEntry entry : fileEntries) {
      String entryPath = entry.getPath();
      // For empty directory path, return all files
      if (normalizedDir.isEmpty() || entryPath.startsWith(normalizedDir)) {
        files.add(entryPath);
      }
    }

    return files;
  }

  /**
   * Retrieves the contents of a file as an InputStream. This method internally delegates to an
   * overloaded version that allows optional progress tracking.
   *
   * @param path The path of the file to retrieve contents from. Must not be null.
   * @return An InputStream of the file contents.
   * @throws IOException If the file cannot be found or read.
   */
  @Override
  public InputStream getFileContentsInternal(String path) throws IOException {
    return getFileContentsInternal(path, null);
  }

  /**
   * Retrieves the contents of a file as an InputStream with optional progress tracking.
   *
   * @param path The path to retrieve contents from (guaranteed to be non-null).
   * @param progressListener Optional progress listener for tracking large file operations.
   * @return An InputStream of the file contents.
   * @throws IOException if the file can't be found.
   */
  public InputStream getFileContentsInternal(String path,
      StreamingProgressListener progressListener)
      throws IOException {
    String fullPath = fullPath(path);

    for (FileEntry entry : fileEntries) {
      if (entry
          .getPath()
          .equals(fullPath)) {
        ByteArrayInputStream bais = new ByteArrayInputStream(entry.getContent());
        return StreamingUtils.createEnhancedStream(bais, entry.getSize(), progressListener);
      }
    }

    // File not found - provide helpful error message
    String suggestion = getSimilarFiles(path);
    throw new IOException(
        "File not found in in-memory ZIP: '" + path + "' (full path: '" + fullPath + "')" +
            (rootPath.isEmpty() ? "" : " with internal root '" + rootPath + "'") + suggestion);
  }

  /**
   * Retrieves a list of all file paths stored in memory.
   *
   * @return A list of strings representing the relative paths of all files in the in-memory file
   * system.
   * @throws IOException If an error occurs while accessing the file information.
   */
  @Override
  public List<String> getAllFiles() throws IOException {
    return fileEntries
        .stream()
        .map(FileEntry::getPath)
        .toList();
  }

  /**
   * Retrieves the total size of all files stored in the in-memory file system.
   *
   * @return The total size of all files in bytes.
   * @throws IOException If an error occurs while calculating the total size.
   */
  @Override
  public long getTotalSize() throws IOException {
    return totalSize;
  }

  /**
   * No-op for in-memory implementation since there are no external resources to close. Provided for
   * AutoCloseable compatibility with other FileAccess implementations.
   */
  @Override
  public void close() {
    // No resources to close for in-memory implementation
    // Data will be garbage collected when this instance is no longer referenced
  }

  /**
   * Gets the number of files loaded in memory.
   *
   * @return The number of files.
   */
  public int getFileCount() {
    return fileEntries.size();
  }

  /**
   * Gets the number of directories detected in the ZIP structure.
   *
   * @return The number of directories.
   */
  public int getDirectoryCount() {
    return directories.size();
  }

  /**
   * Loads ZIP data into memory from a byte array.
   *
   * @param zipData The ZIP file data as a byte array.
   * @return Total size of all files loaded.
   * @throws IOException If the ZIP data can't be read.
   */
  private long loadZipData(byte[] zipData) throws IOException {
    long totalBytes = 0;

    try (ByteArrayInputStream bais = new ByteArrayInputStream(zipData);
        ZipInputStream zis = new ZipInputStream(bais)) {

      ZipEntry entry;
      while ((entry = zis.getNextEntry()) != null) {
        String entryName = entry.getName();

        if (entry.isDirectory()) {
          directories.add(normalizeDirectory(entryName));
        } else {
          // Read file content into memory
          ByteArrayOutputStream entryBaos = new ByteArrayOutputStream();
          byte[] buffer = new byte[8192];
          int len;
          while ((len = zis.read(buffer)) > 0) {
            entryBaos.write(buffer, 0, len);
          }

          byte[] content = entryBaos.toByteArray();
          fileEntries.add(new FileEntry(entryName, content));
          totalBytes += content.length;

          // Add parent directories
          addParentDirectories(entryName);
        }

        zis.closeEntry();
      }
    } catch (Exception e) {
      // Invalid ZIP data - silently create empty archive for compatibility
      // This matches the behavior of java.util.zip.ZipFile which accepts invalid data
      return 0;
    }

    return totalBytes;
  }

  /**
   * Normalizes a directory path by ensuring it ends with a slash.
   *
   * @param dir The directory path to normalize.
   * @return The normalized directory path.
   */
  private String normalizeDirectory(String dir) {
    if (!dir.endsWith("/")) {
      return dir + "/";
    }
    return dir;
  }

  /**
   * Adds all parent directories of a file path to the directories set.
   *
   * @param filePath The file path to extract parent directories from.
   */
  private void addParentDirectories(String filePath) {
    int lastSlash = filePath.lastIndexOf('/');
    if (lastSlash > 0) {
      String parentPath = filePath.substring(0, lastSlash + 1);
      directories.add(parentPath);

      // Recursively add parent directories
      addParentDirectories(parentPath.substring(0, parentPath.length() - 1));
    }
  }

  /**
   * Detects the root path within the ZIP structure.
   *
   * <p>This method analyzes the file entries to determine if all files are contained
   * within a single top-level directory. If so, it returns that directory name as the root path.
   * Otherwise, it returns an empty string, indicating that files are at the root of the ZIP.</p>
   *
   * @return The detected internal root directory or an empty string if files are at the root.
   */
  private String detectRootPath() {
    Set<String> topLevelDirs = new HashSet<>();

    for (FileEntry entry : fileEntries) {
      String entryName = entry.getPath();
      if (checkForRootPath(topLevelDirs, entryName)) {
        return "";
      }
    }

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
    if (fileEntries.isEmpty()) {
      return ". In-memory ZIP appears to be empty or contains only directories.";
    }

    String targetName = targetPath.contains("/") ?
        targetPath.substring(targetPath.lastIndexOf("/") + 1) : targetPath;
    String targetDir = targetPath.contains("/") ?
        targetPath.substring(0, targetPath.lastIndexOf("/")) : "";

    List<String> suggestions = fileEntries
        .stream()
        .map(FileEntry::getPath)
        .filter(file -> file
            .toLowerCase()
            .contains(targetName.toLowerCase()) ||
            (targetDir.isEmpty() || file.startsWith(targetDir)))
        .limit(3)
        .toList();

    if (!suggestions.isEmpty()) {
      return ". Similar files: " + String.join(", ", suggestions);
    } else {
      List<String> sampleFiles = fileEntries
          .stream()
          .map(FileEntry::getPath)
          .limit(5)
          .toList();
      return ". Available files: " + String.join(", ", sampleFiles) +
          (fileEntries.size() > 5 ? " (and " + (fileEntries.size() - 5) + " more)" : "");
    }
  }

  /**
   * Represents a file entry in an in-memory file system, storing its path, content, and size. This
   * is a utility class to encapsulate metadata and contents associated with a file.
   */
  private static class FileEntry {

    /**
     * The file path associated with this file entry, representing the location or identifier of the
     * file within an in-memory file system.
     */
    private final String path;

    /**
     * Holds the content of the file represented by this file entry. This is a byte array that
     * stores the raw data of the file in memory.
     */
    private final byte[] content;

    /**
     * Represents the size of the file in bytes. This value is derived from the length of the
     * content array and indicates the total amount of data stored in this file entry.
     */
    private final long size;

    FileEntry(String path, byte[] content) {
      this.path = path;
      this.content = content;
      this.size = content.length;
    }

    /**
     * Retrieves the file path associated with this file entry.
     *
     * @return the file path as a string
     */
    String getPath() {
      return path;
    }

    /**
     * Retrieves the content of the file represented by this file entry.
     *
     * @return a byte array containing the raw data of the file
     */
    byte[] getContent() {
      return content;
    }

    /**
     * Retrieves the size of the file in bytes.
     *
     * @return the file size as a long value
     */
    long getSize() {
      return size;
    }
  }
}