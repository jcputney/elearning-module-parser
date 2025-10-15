/*
 * qlty-ignore: +qlty:similar-code
 *
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
 *
 * qlty-ignore: -qlty:similar-code
 */

package dev.jcputney.elearning.parser.input.common.serialization;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import dev.jcputney.elearning.parser.impl.access.LocalFileAccess;
import dev.jcputney.elearning.parser.impl.access.ZipFileAccess;
import java.io.IOException;
import java.nio.file.NoSuchFileException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

/**
 * Test class to verify enhanced error messages provide useful context.
 */
class EnhancedErrorMessagesTest {

  @Test
  void testLocalFileAccessEnhancedErrors(@TempDir java.nio.file.Path tempDir) {
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
  void testLocalFileAccessDirectoryNotFound(@TempDir java.nio.file.Path tempDir) {
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
  void testInvalidRootDirectoryError(@TempDir java.nio.file.Path tempDir) throws IOException {
    // Create a regular file
    java.nio.file.Path regularFile = tempDir.resolve("regular-file.txt");
    java.nio.file.Files.createFile(regularFile);

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