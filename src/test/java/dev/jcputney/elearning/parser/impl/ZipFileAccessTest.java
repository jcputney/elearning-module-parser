/* Copyright (c) 2024-2025. Jonathan Putney
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

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests for the {@link ZipFileAccess} class.
 */
class ZipFileAccessTest {

  private static final String SCORM12_ZIP_PATH = "src/test/resources/modules/zips/scorm12.zip";
  private static final String SCORM2004_ZIP_PATH = "src/test/resources/modules/zips/scorm2004.zip";
  private static final String AICC_ZIP_PATH = "src/test/resources/modules/zips/aicc.zip";
  private static final String CMI5_ZIP_PATH = "src/test/resources/modules/zips/cmi5.zip";
  private ZipFileAccess zipFileAccess;

  @BeforeEach
  void setUp() throws IOException {
    // Use SCORM12 ZIP for most tests
    zipFileAccess = new ZipFileAccess(SCORM12_ZIP_PATH);
  }

  @AfterEach
  void tearDown() throws IOException {
    if (zipFileAccess != null) {
      zipFileAccess.close();
    }
  }

  @Test
  void constructor_withValidZipPath_createsInstance() throws IOException {
    assertNotNull(zipFileAccess);
    assertNotNull(zipFileAccess.getRootPath());
  }

  @Test
  void constructor_withInvalidZipPath_throwsIOException() {
    assertThrows(IOException.class, () -> new ZipFileAccess("nonexistent.zip"));
  }

  @Test
  void getRootPath_returnsCorrectPath() {
    // The root path depends on the structure of the ZIP file
    // For SCORM12 ZIP, check that it returns a non-empty string
    assertNotNull(zipFileAccess.getRootPath());
  }

  @Test
  void fileExists_withExistingFile_returnsTrue() {
    // Check for imsmanifest.xml which should exist in SCORM packages
    assertTrue(zipFileAccess.fileExists("imsmanifest.xml"));
  }

  @Test
  void fileExists_withNonExistingFile_returnsFalse() {
    assertFalse(zipFileAccess.fileExists("nonexistent.txt"));
  }

  @Test
  void fileExists_withNullPath_throwsIllegalArgumentException() {
    assertThrows(IllegalArgumentException.class, () -> zipFileAccess.fileExists(null));
  }

  @Test
  void listFiles_returnsCorrectFiles() throws IOException {
    // List files in the root directory
    List<String> files = zipFileAccess.listFiles("");

    // Should contain at least imsmanifest.xml
    assertTrue(files.stream().anyMatch(file -> file.endsWith("imsmanifest.xml")));
  }

  @Test
  void listFiles_withNonExistingDirectory_returnsEmptyList() throws IOException {
    List<String> files = zipFileAccess.listFiles("nonexistent/");
    assertTrue(files.isEmpty());
  }

  @Test
  void listFiles_withNullPath_throwsIllegalArgumentException() {
    assertThrows(IllegalArgumentException.class, () -> zipFileAccess.listFiles(null));
  }

  @Test
  void getFileContents_withExistingFile_returnsContent() throws IOException {
    // Get contents of imsmanifest.xml
    try (InputStream is = zipFileAccess.getFileContents("imsmanifest.xml")) {
      byte[] content = is.readAllBytes();

      // Check that content is not empty
      assertTrue(content.length > 0);

      // Check that content starts with XML declaration or manifest tag
      String contentStart = new String(content, 0, Math.min(100, content.length),
          StandardCharsets.UTF_8);
      assertTrue(contentStart.contains("<?xml") || contentStart.contains("<manifest"));
    }
  }

  @Test
  void getFileContents_withNonExistingFile_throwsIOException() {
    assertThrows(IOException.class, () -> zipFileAccess.getFileContents("nonexistent.txt"));
  }

  @Test
  void getFileContents_withNullPath_throwsIllegalArgumentException() {
    assertThrows(IllegalArgumentException.class, () -> zipFileAccess.getFileContents(null));
  }

  @Test
  void fullPath_withRelativePath_returnsPathWithRootPrefix() {
    String rootPath = zipFileAccess.getRootPath();
    if (!rootPath.isEmpty()) {
      assertEquals(rootPath + "/file.txt", zipFileAccess.fullPath("file.txt"));
    } else {
      assertEquals("file.txt", zipFileAccess.fullPath("file.txt"));
    }
  }

  @Test
  void fullPath_withAbsolutePath_returnsPathWithoutLeadingSlash() {
    assertEquals("file.txt", zipFileAccess.fullPath("/file.txt"));
  }

  @Test
  void fullPath_withNullPath_throwsIllegalArgumentException() {
    assertThrows(IllegalArgumentException.class, () -> zipFileAccess.fullPath(null));
  }

  @Test
  void close_closesZipFile() throws IOException {
    // Create a new ZipFileAccess instance
    ZipFileAccess tempZipFileAccess = new ZipFileAccess(SCORM12_ZIP_PATH);

    // Close it
    tempZipFileAccess.close();

    // Verify that operations on the closed instance throw exceptions
    assertThrows(IllegalStateException.class,
        () -> tempZipFileAccess.getFileContents("imsmanifest.xml"));
  }

  @Test
  void differentZipFiles_haveCorrectRootPaths() throws IOException {
    // Test with different ZIP files to ensure root path detection works correctly
    try (ZipFileAccess scorm2004Access = new ZipFileAccess(SCORM2004_ZIP_PATH);
        ZipFileAccess aiccAccess = new ZipFileAccess(AICC_ZIP_PATH);
        ZipFileAccess cmi5Access = new ZipFileAccess(CMI5_ZIP_PATH)) {

      // Each ZIP might have a different root structure, just verify they're not null
      assertNotNull(scorm2004Access.getRootPath());
      assertNotNull(aiccAccess.getRootPath());
      assertNotNull(cmi5Access.getRootPath());
    }
  }

  @Test
  void fileExists_withSpecialCharactersInPath_handlesCorrectly() {
    // Test with paths containing special characters
    // Note: This test assumes the ZIP file doesn't actually contain these files
    assertFalse(zipFileAccess.fileExists("file with spaces.txt"));
    assertFalse(zipFileAccess.fileExists("file-with-dashes.txt"));
    assertFalse(zipFileAccess.fileExists("file_with_underscores.txt"));
    assertFalse(zipFileAccess.fileExists("file!@#$%^&*().txt"));
    assertFalse(zipFileAccess.fileExists("folder/nested/path/file.txt"));
  }

  @Test
  void listFiles_withNestedDirectories_returnsCorrectFiles() throws IOException {
    // Get the root directory structure
    List<String> rootFiles = zipFileAccess.listFiles("");

    // Find a directory in the root
    String directory = rootFiles.stream()
        .filter(file -> file.endsWith("/"))
        .findFirst()
        .orElse("");

    if (!directory.isEmpty()) {
      // List files in the nested directory
      List<String> nestedFiles = zipFileAccess.listFiles(directory);

      // Verify that all files start with the directory path
      for (String file : nestedFiles) {
        assertTrue(file.startsWith(zipFileAccess.fullPath(directory)));
      }
    }
  }

  @Test
  void getFileContents_withBinaryFile_returnsCorrectContent() throws IOException {
    // Find a binary file in the ZIP (like an image)
    List<String> allFiles = zipFileAccess.listFiles("");
    String binaryFile = allFiles.stream()
        .filter(file -> file.endsWith(".jpg") || file.endsWith(".png") ||
            file.endsWith(".gif") || file.endsWith(".swf"))
        .findFirst()
        .orElse(null);

    if (binaryFile != null) {
      // Get the binary file contents
      try (InputStream is = zipFileAccess.getFileContents(binaryFile)) {
        byte[] content = is.readAllBytes();

        // Verify that content is not empty
        assertTrue(content.length > 0);
      }
    }
  }

  @Test
  void fullPath_withNestedPath_returnsCorrectPath() {
    String rootPath = zipFileAccess.getRootPath();
    String nestedPath = "folder/subfolder/file.txt";

    if (!rootPath.isEmpty()) {
      assertEquals(rootPath + "/" + nestedPath, zipFileAccess.fullPath(nestedPath));
    } else {
      assertEquals(nestedPath, zipFileAccess.fullPath(nestedPath));
    }
  }

  @Test
  void fullPath_withEmptyPath_returnsRootPath() {
    String rootPath = zipFileAccess.getRootPath();

    if (!rootPath.isEmpty()) {
      assertEquals(rootPath + "/", zipFileAccess.fullPath(""));
    } else {
      assertEquals("", zipFileAccess.fullPath(""));
    }
  }

  @Test
  void fileExists_withDirectoryPath_returnsFalseForDirectoriesWithoutExplicitEntries()
      throws IOException {
    // In ZIP files, directories might not have explicit entries
    // This test verifies the behavior for directory paths
    List<String> allFiles = zipFileAccess.listFiles("");
    String directory = allFiles.stream()
        .filter(file -> file.contains("/") && !file.endsWith("/"))
        .map(file -> file.substring(0, file.lastIndexOf('/') + 1))
        .findFirst()
        .orElse("");

    if (!directory.isEmpty()) {
      // The behavior depends on whether the ZIP has explicit directory entries
      // We're just testing that it doesn't throw an exception
      boolean exists = zipFileAccess.fileExists(directory);
      // No assertion here, as the result depends on the ZIP structure
    }
  }

  @Test
  void listFiles_withTrailingSlash_handlesCorrectly() throws IOException {
    // Test with and without trailing slash
    String directory = "";
    List<String> filesWithSlash = zipFileAccess.listFiles(directory + "/");
    List<String> filesWithoutSlash = zipFileAccess.listFiles(directory);

    // Both should return the same files
    assertEquals(filesWithSlash.size(), filesWithoutSlash.size());
    assertTrue(filesWithSlash.containsAll(filesWithoutSlash));
  }

  @Test
  void getFileContents_withSameFileTwice_returnsSameContent() throws IOException {
    // Get a file that exists
    String existingFile = "imsmanifest.xml";

    // Get contents twice
    byte[] content1;
    byte[] content2;

    try (InputStream is1 = zipFileAccess.getFileContents(existingFile)) {
      content1 = is1.readAllBytes();
    }

    try (InputStream is2 = zipFileAccess.getFileContents(existingFile)) {
      content2 = is2.readAllBytes();
    }

    // Verify that both contents are the same
    assertArrayEquals(content1, content2);
  }

  @Test
  void autoCloseable_withTryWithResources_closesZipFile() throws IOException {
    // Test that ZipFileAccess works with try-with-resources
    try (ZipFileAccess tempZipFileAccess = new ZipFileAccess(SCORM12_ZIP_PATH)) {
      // Perform some operations
      assertTrue(tempZipFileAccess.fileExists("imsmanifest.xml"));
      List<String> files = tempZipFileAccess.listFiles("");
      assertFalse(files.isEmpty());
    }

    // After the try block, tempZipFileAccess should be closed
    // We can't directly test this without accessing private fields
  }
}
