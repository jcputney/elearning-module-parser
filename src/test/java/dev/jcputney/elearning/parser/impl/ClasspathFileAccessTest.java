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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests for the {@link ClasspathFileAccess} class.
 */
class ClasspathFileAccessTest {

  // Use a known directory in the test resources
  private static final String TEST_ROOT_PATH = "modules/scorm12";
  private ClasspathFileAccess classpathFileAccess;

  @BeforeEach
  void setUp() {
    classpathFileAccess = new ClasspathFileAccess(TEST_ROOT_PATH);
  }

  @Test
  void constructorWithValidPathCreatesInstance() {
    assertNotNull(classpathFileAccess);
    assertEquals(TEST_ROOT_PATH, classpathFileAccess.getRootPath());
  }

  @Test
  void constructorWithNullPathCreatesInstanceWithNullPath() {
    ClasspathFileAccess access = new ClasspathFileAccess(null);
    assertNull(access.getRootPath());
  }

  @Test
  void fileExistsWithExistingFileReturnsTrue() {
    // Check for a file that should exist in the test resources
    assertTrue(classpathFileAccess.fileExists(
        "modules/scorm12/ContentPackagingOneFilePerSCO_SCORM12/imsmanifest.xml"));
  }

  @Test
  void fileExistsWithNonExistingFileReturnsFalse() {
    assertFalse(classpathFileAccess.fileExists("modules/scorm12/nonexistent.txt"));
  }

  @Test
  void fileExistsWithNullPathThrowsIllegalArgumentException() {
    assertThrows(IllegalArgumentException.class, () -> classpathFileAccess.fileExists(null));
  }

  @Test
  void listFiles_withValidDirectory_returnsFiles() throws IOException {
    // List files in a known directory in the test resources
    List<String> files = classpathFileAccess.listFiles(
        "modules/scorm12/ContentPackagingOneFilePerSCO_SCORM12");

    // The implementation might return different results depending on whether it's running from a JAR or not,
    // so we'll just check that the list is not empty
    assertFalse(files.isEmpty());
  }

  @Test
  void listFiles_withNonExistentDirectory_returnsEmptyList() throws IOException {
    List<String> files = classpathFileAccess.listFiles("modules/nonexistent");
    assertTrue(files.isEmpty());
  }

  @Test
  void listFiles_withNullPath_throwsIllegalArgumentException() {
    assertThrows(IllegalArgumentException.class, () -> classpathFileAccess.listFiles(null));
  }

  @Test
  void getFileContents_withExistingFile_returnsContent() throws IOException {
    // Get contents of a known file in the test resources
    try (InputStream is = classpathFileAccess.getFileContents(
        "modules/scorm12/ContentPackagingOneFilePerSCO_SCORM12/imsmanifest.xml")) {
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
  void getFileContents_withNonExistentFile_throwsIOException() {
    assertThrows(IOException.class,
        () -> classpathFileAccess.getFileContents("modules/scorm12/nonexistent.txt"));
  }

  @Test
  void getFileContents_withNullPath_throwsIllegalArgumentException() {
    assertThrows(IllegalArgumentException.class, () -> classpathFileAccess.getFileContents(null));
  }

  @Test
  void fullPath_withRelativePath_returnsPathWithRootPrefix() {
    assertEquals(TEST_ROOT_PATH + "/file.txt", classpathFileAccess.fullPath("file.txt"));
  }

  @Test
  void fullPath_withAbsolutePath_returnsPathWithoutLeadingSlash() {
    assertEquals("file.txt", classpathFileAccess.fullPath("/file.txt"));
  }

  @Test
  void fullPath_withNullPath_throwsIllegalArgumentException() {
    assertThrows(IllegalArgumentException.class, () -> classpathFileAccess.fullPath(null));
  }

  @Test
  void fullPath_withEmptyPath_returnsRootPathWithSlash() {
    assertEquals(TEST_ROOT_PATH + "/", classpathFileAccess.fullPath(""));
  }
}