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

package dev.jcputney.elearning.parser.util;

import java.util.List;
import org.apache.commons.lang3.Strings;

/**
 * Utility methods for file operations.
 */
public final class FileUtils {

  /**
   * Private constructor to prevent instantiation.
   */
  private FileUtils() {
    throw new AssertionError("Utility class should not be instantiated");
  }

  /**
   * Finds a file in the given list using case-insensitive matching.
   *
   * <p>This method performs a case-insensitive search for a file with the specified target name
   * in the provided list of file paths. It returns the actual filename as it appears in the list,
   * preserving the original casing.
   *
   * @param files The list of file paths to search.
   * @param targetName The target filename to search for (case-insensitive).
   * @return The actual filename from the list that matches the target (preserving original casing),
   *         or null if no match is found.
   * @throws IllegalArgumentException if files is null or targetName is null or empty.
   */
  public static String findFileIgnoreCase(List<String> files, String targetName) {
    if (files == null) {
      throw new IllegalArgumentException("Files list cannot be null");
    }
    if (targetName == null || targetName.isEmpty()) {
      throw new IllegalArgumentException("Target name cannot be null or empty");
    }

    return files.stream()
        .filter(file -> Strings.CI.equals(file, targetName))
        .findFirst()
        .orElse(null);
  }
}
