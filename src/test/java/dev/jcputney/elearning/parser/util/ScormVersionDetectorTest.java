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
import org.junit.jupiter.api.Test;

/**
 * Tests for the {@link ScormVersionDetector} class.
 */
class ScormVersionDetectorTest {

  @Test
  void detectScormVersion_withNullFileAccess_throwsIllegalArgumentException() {
    assertThrows(IllegalArgumentException.class,
        () -> ScormVersionDetector.detectScormVersion(null));
  }

  @Test
  void detectScormVersion_withScorm12Manifest_returnsScorm12() throws Exception {
    // Create a mock FileAccess with a SCORM 1.2 manifest
    MockFileAccess fileAccess = new MockFileAccess("root/path");
    fileAccess.setFileContents(Scorm12Parser.MANIFEST_FILE,
        "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
            + "<manifest>\n"
            + "  <metadata>\n"
            + "    <schema>ADL SCORM</schema>\n"
            + "    <schemaversion>1.2</schemaversion>\n"
            + "  </metadata>\n"
            + "</manifest>");

    ModuleType result = ScormVersionDetector.detectScormVersion(fileAccess);

    assertEquals(ModuleType.SCORM_12, result);
  }

  @Test
  void detectScormVersion_withScorm2004Manifest_returnsScorm2004() throws Exception {
    // Create a mock FileAccess with a SCORM 2004 manifest
    MockFileAccess fileAccess = new MockFileAccess("root/path");
    fileAccess.setFileContents(Scorm12Parser.MANIFEST_FILE,
        "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
            + "<manifest>\n"
            + "  <metadata>\n"
            + "    <schema>ADL SCORM</schema>\n"
            + "    <schemaversion>2004 4th Edition</schemaversion>\n"
            + "  </metadata>\n"
            + "</manifest>");

    ModuleType result = ScormVersionDetector.detectScormVersion(fileAccess);

    assertEquals(ModuleType.SCORM_2004, result);
  }

  @Test
  void detectScormVersion_withScorm2004Namespace_returnsScorm2004() throws Exception {
    // Create a mock FileAccess with a SCORM 2004 manifest (using namespace)
    MockFileAccess fileAccess = new MockFileAccess("root/path");
    fileAccess.setFileContents(Scorm12Parser.MANIFEST_FILE,
        "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
            + "<manifest xmlns:adlcp=\"http://www.adlnet.org/xsd/adlcp_v1p3\">\n"
            + "  <metadata>\n"
            + "    <schema>Some Other Schema</schema>\n"
            + "    <schemaversion>1.0</schemaversion>\n"
            + "  </metadata>\n"
            + "</manifest>");

    ModuleType result = ScormVersionDetector.detectScormVersion(fileAccess);

    assertEquals(ModuleType.SCORM_2004, result);
  }

  @Test
  void detectScormVersion_withAmbiguousManifest_defaultsToScorm12() throws Exception {
    // Create a mock FileAccess with an ambiguous manifest
    MockFileAccess fileAccess = new MockFileAccess("root/path");
    fileAccess.setFileContents(Scorm12Parser.MANIFEST_FILE,
        "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
            + "<manifest>\n"
            + "  <metadata>\n"
            + "    <schema>Some Other Schema</schema>\n"
            + "    <schemaversion>1.0</schemaversion>\n"
            + "  </metadata>\n"
            + "</manifest>");

    ModuleType result = ScormVersionDetector.detectScormVersion(fileAccess);

    assertEquals(ModuleType.SCORM_12, result);
  }

  @Test
  void detectScormVersion_withMissingSchemaElements_defaultsToScorm12() throws Exception {
    // Create a mock FileAccess with a manifest missing schema elements
    MockFileAccess fileAccess = new MockFileAccess("root/path");
    fileAccess.setFileContents(Scorm12Parser.MANIFEST_FILE,
        "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
            + "<manifest>\n"
            + "  <metadata>\n"
            + "    <!-- No schema or schemaversion elements -->\n"
            + "  </metadata>\n"
            + "</manifest>");

    ModuleType result = ScormVersionDetector.detectScormVersion(fileAccess);

    assertEquals(ModuleType.SCORM_12, result);
  }

  /**
   * A mock implementation of {@link FileAccess} for testing.
   */
  private static class MockFileAccess implements FileAccess {

    private final String rootPath;
    private final Map<String, String> fileContents = new HashMap<>();

    public MockFileAccess(String rootPath) {
      this.rootPath = rootPath;
    }

    public void setFileContents(String path, String contents) {
      fileContents.put(path, contents);
    }

    @Override
    public String getRootPath() {
      return rootPath;
    }

    @Override
    public boolean fileExistsInternal(String path) {
      return fileContents.containsKey(path);
    }

    @Override
    public List<String> listFilesInternal(String directoryPath) throws IOException {
      return Collections.emptyList();
    }

    @Override
    public InputStream getFileContentsInternal(String path) throws IOException {
      String contents = fileContents.getOrDefault(path, "");
      return new ByteArrayInputStream(contents.getBytes(StandardCharsets.UTF_8));
    }
  }
}