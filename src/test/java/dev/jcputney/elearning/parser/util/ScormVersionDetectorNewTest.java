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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import dev.jcputney.elearning.parser.api.FileAccess;
import dev.jcputney.elearning.parser.enums.ModuleType;
import dev.jcputney.elearning.parser.parsers.Scorm12Parser;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.xml.parsers.ParserConfigurationException;
import org.junit.jupiter.api.Test;
import org.xml.sax.SAXException;

/**
 * New test class for ScormVersionDetector to ensure proper coverage.
 * This class uses a custom implementation of FileAccess for testing.
 */
class ScormVersionDetectorNewTest {

  @Test
  void testNullFileAccessThrowsException() {
    assertThrows(IllegalArgumentException.class, () -> ScormVersionDetector.detectScormVersion(null));
  }

  @Test
  void testScorm12Detection() throws IOException, ParserConfigurationException, SAXException {
    // Create custom FileAccess implementation
    TestFileAccess fileAccess = new TestFileAccess();

    // Set up to return SCORM 1.2 manifest content
    String scorm12Manifest = 
        "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
        "<manifest>\n" +
        "  <metadata>\n" +
        "    <schema>ADL SCORM</schema>\n" +
        "    <schemaversion>1.2</schemaversion>\n" +
        "  </metadata>\n" +
        "</manifest>";

    fileAccess.addFile(Scorm12Parser.MANIFEST_FILE, scorm12Manifest);

    // Test detection
    ModuleType result = ScormVersionDetector.detectScormVersion(fileAccess);
    assertEquals(ModuleType.SCORM_12, result);
  }

  @Test
  void testScorm2004Detection() throws IOException, ParserConfigurationException, SAXException {
    // Create custom FileAccess implementation
    TestFileAccess fileAccess = new TestFileAccess();

    // Set up to return SCORM 2004 manifest content
    String scorm2004Manifest = 
        "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
        "<manifest>\n" +
        "  <metadata>\n" +
        "    <schema>ADL SCORM</schema>\n" +
        "    <schemaversion>2004 4th Edition</schemaversion>\n" +
        "  </metadata>\n" +
        "</manifest>";

    fileAccess.addFile(Scorm12Parser.MANIFEST_FILE, scorm2004Manifest);

    // Test detection
    ModuleType result = ScormVersionDetector.detectScormVersion(fileAccess);
    assertEquals(ModuleType.SCORM_2004, result);
  }

  @Test
  void testScorm2004DetectionFromNamespace() throws IOException, ParserConfigurationException, SAXException {
    // Create custom FileAccess implementation
    TestFileAccess fileAccess = new TestFileAccess();

    // Set up to return SCORM 2004 manifest with namespace
    String scorm2004WithNamespace = 
        "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
        "<manifest xmlns:adlcp=\"http://www.adlnet.org/xsd/adlcp_v1p3\">\n" +
        "  <metadata>\n" +
        "    <schema>Some Other Schema</schema>\n" +
        "    <schemaversion>1.0</schemaversion>\n" +
        "  </metadata>\n" +
        "</manifest>";

    fileAccess.addFile(Scorm12Parser.MANIFEST_FILE, scorm2004WithNamespace);

    // Test detection
    ModuleType result = ScormVersionDetector.detectScormVersion(fileAccess);
    assertEquals(ModuleType.SCORM_2004, result);
  }

  @Test
  void testDefaultToScorm12() throws IOException, ParserConfigurationException, SAXException {
    // Create custom FileAccess implementation
    TestFileAccess fileAccess = new TestFileAccess();

    // Set up to return manifest with no version info
    String noVersionManifest = 
        "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
        "<manifest>\n" +
        "  <metadata>\n" +
        "    <!-- No schema or schemaversion elements -->\n" +
        "  </metadata>\n" +
        "</manifest>";

    fileAccess.addFile(Scorm12Parser.MANIFEST_FILE, noVersionManifest);

    // Test detection
    ModuleType result = ScormVersionDetector.detectScormVersion(fileAccess);
    assertEquals(ModuleType.SCORM_12, result);
  }

  @Test
  void testNonScormSchemaDefaultsToScorm12() throws IOException, ParserConfigurationException, SAXException {
    // Create custom FileAccess implementation
    TestFileAccess fileAccess = new TestFileAccess();

    // Set up to return manifest with non-SCORM schema
    String nonScormManifest = 
        "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
        "<manifest>\n" +
        "  <metadata>\n" +
        "    <schema>Some Other Schema</schema>\n" +
        "    <schemaversion>1.0</schemaversion>\n" +
        "  </metadata>\n" +
        "</manifest>";

    fileAccess.addFile(Scorm12Parser.MANIFEST_FILE, nonScormManifest);

    // Test detection
    ModuleType result = ScormVersionDetector.detectScormVersion(fileAccess);
    assertEquals(ModuleType.SCORM_12, result);
  }

  /**
   * Custom FileAccess implementation for testing.
   */
  private static class TestFileAccess implements FileAccess {
    private final Map<String, String> files = new HashMap<>();

    void addFile(String path, String content) {
      files.put(path, content);
    }

    @Override
    public String getRootPath() {
      return "/test/root";
    }

    @Override
    public boolean fileExistsInternal(String path) {
      return files.containsKey(path);
    }

    @Override
    public List<String> listFilesInternal(String directoryPath) {
      return List.copyOf(files.keySet());
    }

    @Override
    public InputStream getFileContentsInternal(String path) throws IOException {
      String content = files.get(path);
      if (content == null) {
        throw new IOException("File not found: " + path);
      }
      return new ByteArrayInputStream(content.getBytes(StandardCharsets.UTF_8));
    }
  }
}
