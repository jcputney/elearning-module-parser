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

package dev.jcputney.elearning.parser.api;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

/**
 * Abstract base class for {@link FileAccess} implementations that work with archive-like storage
 * structures (ZIP files, in-memory archives, cloud storage buckets) where root path detection and
 * path normalization are needed.
 *
 * <p>This class provides common functionality for:
 * <ul>
 *   <li>Automatic detection of root directories within archive structures</li>
 *   <li>Path conversion between storage paths and relative paths</li>
 *   <li>Consistent handling of single-root vs multi-root archives</li>
 * </ul>
 *
 * <p><b>Usage Pattern:</b> Subclasses should:
 * <ol>
 *   <li>Call the constructor after loading file metadata</li>
 *   <li>Implement {@link #getStorageFilePaths()} to provide file paths from storage</li>
 *   <li>Use {@link #stripRootPath(String)} when returning paths to callers</li>
 *   <li>Use {@link #fullPath(String)} when accessing storage with caller-provided paths</li>
 * </ol>
 *
 * <p><b>Root Path Detection:</b> This class automatically detects if all files in the archive
 * share a common root directory. For example, if a ZIP contains:
 * <pre>
 *   module-v1.0/imsmanifest.xml
 *   module-v1.0/content/page1.html
 *   module-v1.0/resources/style.css
 * </pre>
 * The root path will be detected as "module-v1.0", and {@link #stripRootPath(String)} will
 * convert storage paths to relative paths like "imsmanifest.xml", "content/page1.html", etc.
 *
 * @see FileAccess
 * @see dev.jcputney.elearning.parser.impl.access.ZipFileAccess
 * @see dev.jcputney.elearning.parser.impl.access.InMemoryFileAccess
 */
public abstract class AbstractArchiveFileAccess implements FileAccess, AutoCloseable {

  /**
   * The detected root path within the archive. Empty string if files are at the root level or if
   * multiple top-level directories exist.
   */
  protected String rootPath;

  /**
   * Constructs an AbstractArchiveFileAccess. Subclasses must call {@link #initializeRootPath()}
   * after loading file metadata.
   */
  protected AbstractArchiveFileAccess() {
    // Subclasses initialize rootPath by calling initializeRootPath()
  }

  /**
   * Gets the detected root path within the archive.
   *
   * @return The root path, or an empty string if files are at the root level.
   */
  @Override
  public String getRootPath() {
    return rootPath != null ? rootPath : "";
  }

  /**
   * Default implementation of close for archive-based implementations. Subclasses that manage
   * external resources (like file handles) should override this.
   *
   * @throws IOException if an error occurs while closing resources
   */
  @Override
  public void close() throws IOException {
    // Default no-op implementation for in-memory archives
    // Subclasses with external resources should override
  }

  /**
   * Initializes the root path by detecting it from storage file paths. Subclasses must call this
   * method after loading their file metadata.
   */
  protected final void initializeRootPath() {
    this.rootPath = detectRootPath();
  }

  /**
   * Template method that subclasses must implement to provide all file paths from storage. These
   * paths should be in their original storage format (e.g., with root prefix if present).
   *
   * <p>This method is called during construction to detect the root path.
   *
   * @return An iterable of all file paths in their storage format
   */
  protected abstract Iterable<String> getStorageFilePaths();

  /**
   * Detects the common root directory by analyzing file paths from storage.
   *
   * <p>This method determines if all files in the archive are contained within a single
   * top-level directory. If so, it returns that directory name as the root path. Otherwise, it
   * returns an empty string, indicating files are at the root level or span multiple top-level
   * directories.
   *
   * <p><b>Detection Algorithm:</b>
   * <ol>
   *   <li>Extract the first path component from each file path</li>
   *   <li>If all files share the same first component, that becomes the root path</li>
   *   <li>If files have different first components, or are at the root level, return empty string</li>
   * </ol>
   *
   * @return The detected root directory name, or empty string if no common root exists
   */
  protected String detectRootPath() {
    Set<String> topLevelDirs = new HashSet<>();

    for (String storagePath : getStorageFilePaths()) {
      if (checkForRootPath(topLevelDirs, storagePath)) {
        // Multiple top-level directories or root-level files detected
        return "";
      }
    }

    // If exactly one top-level directory exists, it's the root
    return topLevelDirs.size() == 1 ? topLevelDirs
        .iterator()
        .next() : "";
  }

  /**
   * Normalizes a directory path by ensuring it ends with a slash.
   *
   * @param dir The directory path to normalize.
   * @return The normalized directory path ending with "/".
   */
  protected String normalizeDirectory(String dir) {
    if (dir == null || dir.isEmpty()) {
      return "";
    }
    if (!dir.endsWith("/")) {
      return dir + "/";
    }
    return dir;
  }

  /**
   * Checks if a file entry represents a root-level directory or if multiple top-level directories
   * are present in the provided set.
   *
   * @param topLevelDirs A set of strings representing the top-level directories encountered so far.
   * This set is modified by the method to add new entries if applicable.
   * @param entryName The name of the file entry to evaluate, potentially containing a directory
   * path.
   * @return True if the file entry is at the root level or if more than one top-level directory is
   * present; false otherwise.
   */
  protected boolean checkForRootPath(Set<String> topLevelDirs, String entryName) {
    int slashIndex = entryName.indexOf('/');

    if (slashIndex > 0) {
      String topLevel = entryName.substring(0, slashIndex);
      topLevelDirs.add(topLevel);

      return topLevelDirs.size() > 1;
    } else {
      // File at root level
      return true;
    }
  }

  /**
   * Strips the root path prefix from an internal storage path to create a relative path.
   *
   * <p>This is the inverse operation of {@link FileAccess#fullPath(String)}.
   *
   * <p>Example: If rootPath="module-root" and storagePath="module-root/page.html",
   * this method returns "page.html".
   *
   * @param storagePath The absolute storage path to convert to a relative path.
   * @return The path relative to rootPath, or the original path if no root prefix exists.
   * @throws IllegalArgumentException if storagePath is null
   */
  protected String stripRootPath(String storagePath) {
    if (storagePath == null) {
      throw new IllegalArgumentException("Storage path cannot be null");
    }

    String root = getRootPath();
    if (root == null || root.isEmpty()) {
      return storagePath;
    }

    String rootPrefix = root + "/";
    if (storagePath.startsWith(rootPrefix)) {
      return storagePath.substring(rootPrefix.length());
    }

    return storagePath;
  }
}
