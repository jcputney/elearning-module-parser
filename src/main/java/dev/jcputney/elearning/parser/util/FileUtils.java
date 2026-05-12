/*
 * Copyright (c) 2024-2026 Jonathan Putney
 *
 * Licensed under the Apache License, Version 2.0 (the "License").
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at the project root LICENSE file
 * or at http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0
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
   * or null if no match is found.
   * @throws IllegalArgumentException if files is null or targetName is null or empty.
   */
  public static String findFileIgnoreCase(List<String> files, String targetName) {
    if (files == null) {
      throw new IllegalArgumentException("Files list cannot be null");
    }
    if (targetName == null || targetName.isEmpty()) {
      throw new IllegalArgumentException("Target name cannot be null or empty");
    }

    return files
        .stream()
        .filter(file -> Strings.CI.equals(file, targetName))
        .findFirst()
        .orElse(null);
  }
}
