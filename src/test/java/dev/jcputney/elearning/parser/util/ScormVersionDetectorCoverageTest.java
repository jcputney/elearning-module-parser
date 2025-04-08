/*
 * qlty-ignore: +qlty:similar-code
 *
 * Copyright (c) 2025. Jonathan Putney
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
 * Additional test class for ScormVersionDetector to ensure JaCoCo coverage.
 * This class uses a different approach to test the ScormVersionDetector class.
 */
class ScormVersionDetectorCoverageTest {

  @Test
  void testDetectScormVersionWithNullFileAccess() {
    // Test that passing null to detectScormVersion throws IllegalArgumentException
    assertThrows(IllegalArgumentException.class, () -> {
      ScormVersionDetector.detectScormVersion(null);
    });
  }

  @Test
  void testDetectScorm12Version() throws ParserConfigurationException, IOException, SAXException {
    // Create a mock FileAccess with a SCORM 1.2 manifest
    MockFileAccess fileAccess = new MockFileAccess();
    fileAccess.addFile(Scorm12Parser.MANIFEST_FILE, 
        "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
        "<manifest>\n" +
        "  <metadata>\n" +
        "    <schema>ADL SCORM</schema>\n" +
        "    <schemaversion>1.2</schemaversion>\n" +
        "  </metadata>\n" +
        "</manifest>");

    // Test that detectScormVersion correctly identifies SCORM 1.2
    ModuleType result = ScormVersionDetector.detectScormVersion(fileAccess);
    assertEquals(ModuleType.SCORM_12, result);
  }

  @Test
  void testDetectScorm2004Version() throws ParserConfigurationException, IOException, SAXException {
    // Create a mock FileAccess with a SCORM 2004 manifest
    MockFileAccess fileAccess = new MockFileAccess();
    fileAccess.addFile(Scorm12Parser.MANIFEST_FILE, 
        "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
        "<manifest>\n" +
        "  <metadata>\n" +
        "    <schema>ADL SCORM</schema>\n" +
        "    <schemaversion>2004 4th Edition</schemaversion>\n" +
        "  </metadata>\n" +
        "</manifest>");

    // Test that detectScormVersion correctly identifies SCORM 2004
    ModuleType result = ScormVersionDetector.detectScormVersion(fileAccess);
    assertEquals(ModuleType.SCORM_2004, result);
  }

  @Test
  void testDetectScorm2004VersionFromNamespace() throws ParserConfigurationException, IOException, SAXException {
    // Create a mock FileAccess with a SCORM 2004 manifest identified by namespace
    MockFileAccess fileAccess = new MockFileAccess();
    fileAccess.addFile(Scorm12Parser.MANIFEST_FILE, 
        "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
        "<manifest xmlns:adlcp=\"http://www.adlnet.org/xsd/adlcp_v1p3\">\n" +
        "  <metadata>\n" +
        "    <schema>Some Other Schema</schema>\n" +
        "    <schemaversion>1.0</schemaversion>\n" +
        "  </metadata>\n" +
        "</manifest>");

    // Test that detectScormVersion correctly identifies SCORM 2004 from namespace
    ModuleType result = ScormVersionDetector.detectScormVersion(fileAccess);
    assertEquals(ModuleType.SCORM_2004, result);
  }

  @Test
  void testDefaultToScorm12() throws ParserConfigurationException, IOException, SAXException {
    // Create a mock FileAccess with a manifest that doesn't specify version
    MockFileAccess fileAccess = new MockFileAccess();
    fileAccess.addFile(Scorm12Parser.MANIFEST_FILE, 
        "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
        "<manifest>\n" +
        "  <metadata>\n" +
        "    <!-- No schema or schemaversion elements -->\n" +
        "  </metadata>\n" +
        "</manifest>");

    // Test that detectScormVersion defaults to SCORM 1.2
    ModuleType result = ScormVersionDetector.detectScormVersion(fileAccess);
    assertEquals(ModuleType.SCORM_12, result);
  }

  /**
   * Simple mock implementation of FileAccess for testing.
   */
  private static class MockFileAccess implements FileAccess {
    private final Map<String, String> files = new HashMap<>();

    void addFile(String path, String content) {
      files.put(path, content);
    }

    @Override
    public String getRootPath() {
      return "/mock/root";
    }

    @Override
    public boolean fileExistsInternal(String path) {
      return files.containsKey(path);
    }

    @Override
    public List<String> listFilesInternal(String directoryPath) {
      return Collections.emptyList();
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