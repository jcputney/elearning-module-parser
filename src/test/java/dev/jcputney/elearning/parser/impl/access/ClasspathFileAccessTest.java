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
package dev.jcputney.elearning.parser.impl.access;

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