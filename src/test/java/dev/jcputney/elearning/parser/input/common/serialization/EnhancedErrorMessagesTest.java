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
package dev.jcputney.elearning.parser.input.common.serialization;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import dev.jcputney.elearning.parser.impl.access.LocalFileAccess;
import dev.jcputney.elearning.parser.impl.access.ZipFileAccess;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

/**
 * Test class to verify enhanced error messages provide useful context.
 */
class EnhancedErrorMessagesTest {

  @Test
  void testLocalFileAccessEnhancedErrors(@TempDir Path tempDir) {
    LocalFileAccess fileAccess = new LocalFileAccess(tempDir.toString());

    // Test file not found error
    NoSuchFileException exception = assertThrows(NoSuchFileException.class, () -> {
      fileAccess.getFileContents("nonexistent.txt");
    });

    // Verify the error message contains enhanced context
    assertTrue(exception
        .getMessage()
        .contains("nonexistent.txt"));
    assertTrue(exception
        .getMessage()
        .contains("full path"));
    assertTrue(exception
        .getMessage()
        .contains("root"));
  }

  @Test
  void testLocalFileAccessDirectoryNotFound(@TempDir Path tempDir) {
    LocalFileAccess fileAccess = new LocalFileAccess(tempDir.toString());

    // Test directory not found error
    IOException exception = assertThrows(IOException.class, () -> {
      fileAccess.listFiles("nonexistent-directory");
    });

    // Verify the error message contains enhanced context
    assertTrue(exception
        .getMessage()
        .contains("Directory not found"));
    assertTrue(exception
        .getMessage()
        .contains("nonexistent-directory"));
    assertTrue(exception
        .getMessage()
        .contains("full path"));
    assertTrue(exception
        .getMessage()
        .contains("root"));
  }

  @Test
  void testZipFileAccessEnhancedErrors() {
    // Test with invalid ZIP file path
    IOException exception = assertThrows(IOException.class, () -> {
      new ZipFileAccess("/nonexistent/path/file.zip");
    });

    // Verify the error message contains enhanced context
    assertTrue(exception
        .getMessage()
        .contains("Failed to open ZIP file"));
    assertTrue(exception
        .getMessage()
        .contains("/nonexistent/path/file.zip"));
  }

  @Test
  void testInvalidRootDirectoryError(@TempDir Path tempDir) throws IOException {
    // Create a regular file
    Path regularFile = tempDir.resolve("regular-file.txt");
    Files.createFile(regularFile);

    // Try to use the regular file as a root directory
    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
      new LocalFileAccess(regularFile.toString());
    });

    // Verify the error message contains enhanced context
    assertTrue(exception
        .getMessage()
        .contains("Invalid root directory"));
    assertTrue(exception
        .getMessage()
        .contains("path points to a file"));
    assertTrue(exception
        .getMessage()
        .contains(regularFile.toString()));
  }
}