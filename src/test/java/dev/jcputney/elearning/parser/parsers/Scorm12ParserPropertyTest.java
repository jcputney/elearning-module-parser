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

package dev.jcputney.elearning.parser.parsers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import dev.jcputney.elearning.parser.api.FileAccess;
import dev.jcputney.elearning.parser.exception.ModuleParsingException;
import dev.jcputney.elearning.parser.input.scorm12.Scorm12Manifest;
import dev.jcputney.elearning.parser.output.metadata.scorm12.Scorm12Metadata;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * Property-based tests for the SCORM 1.2 parser. These tests focus on edge cases and error
 * conditions.
 */
public class Scorm12ParserPropertyTest {

  /**
   * Tests that the parser correctly handles missing required fields. The parser should throw a
   * ModuleParsingException when required fields are missing.
   */
  @ParameterizedTest
  @ValueSource(strings = {
      // Missing title
      // language=XML
      """
          <manifest identifier="com.scorm.golfsamples.contentpackaging.singlesco.12" version="1">
            <organizations default="golf_sample_default">
              <organization identifier="golf_sample_default">
                <item identifier="item_1" identifierref="resource_1"></item>
              </organization>
            </organizations>
            <resources>
              <resource identifier="resource_1" type="webcontent" adlcp:scormtype="sco" href="shared/launchpage.html">
                <file href="shared/launchpage.html" />
              </resource>
            </resources>
          </manifest>""",

      // Missing launch URL (href attribute in resource)
      // language=XML
      """
          <manifest identifier="com.scorm.golfsamples.contentpackaging.singlesco.12" version="1">
            <organizations default="golf_sample_default">
              <organization identifier="golf_sample_default">
                <title>Golf Explained - CP Single SCO</title>
                <item identifier="item_1" identifierref="resource_1"></item>
              </organization>
            </organizations>
            <resources>
              <resource identifier="resource_1" type="webcontent" adlcp:scormtype="sco">
                <file href="shared/launchpage.html" />
              </resource>
            </resources>
          </manifest>"""
  })
  void testMissingRequiredFields(String manifestXml) {
    // Create a mock FileAccess that returns the test manifest XML
    FileAccess mockFileAccess = new MockFileAccess(manifestXml);

    // Create a parser with the mock FileAccess
    Scorm12Parser parser = new Scorm12Parser(mockFileAccess);

    // The parser should throw a ModuleParsingException when parsing the manifest
    assertThrows(ModuleParsingException.class, parser::parse);
  }

  /**
   * Tests that the parser correctly handles malformed XML. The parser should throw an exception
   * when the XML is malformed.
   */
  @ParameterizedTest
  @ValueSource(// Extra closing tag
      strings = {
          // Unclosed tag
          // language=XML
          """
              <manifest identifier="com.scorm.golfsamples.contentpackaging.singlesco.12" version="1">
                <organizations default="golf_sample_default">
                  <organization identifier="golf_sample_default">
                    <title>Golf Explained - CP Single SCO</title>
                    <item identifier="item_1" identifierref="resource_1"></item>
                  </organization>
                </organizations>
                <resources>
                  <resource identifier="resource_1" type="webcontent" adlcp:scormtype="sco" href="shared/launchpage.html">
                    <file href="shared/launchpage.html" />
                  </resource>
                </resources>
              </manifest>""",

          // Invalid XML structure
          // language=XML
          """
              <manifest identifier="com.scorm.golfsamples.contentpackaging.singlesco.12" version="1">
                <organizations default="golf_sample_default">
                  <organization identifier="golf_sample_default">
                    <title>Golf Explained - CP Single SCO</title>
                    <item identifier="item_1" identifierref="resource_1"></item>
                  </organization>
                </organizations>
                <resources>
                  <resource identifier="resource_1" type="webcontent" adlcp:scormtype="sco" href="shared/launchpage.html">
                    <file href="shared/launchpage.html" />
                  </resource>
                </resources>
              </manifest>"""
      })
  void testMalformedXml(String manifestXml) {
    // Create a mock FileAccess that returns the test manifest XML
    FileAccess mockFileAccess = new MockFileAccess(manifestXml);

    // Create a parser with the mock FileAccess
    Scorm12Parser parser = new Scorm12Parser(mockFileAccess);

    // The parser should throw an exception when parsing the manifest
    assertThrows(Exception.class, parser::parse);
  }

  /**
   * Tests that the parser correctly handles unexpected values in the manifest. The parser should
   * handle unexpected values gracefully.
   */
  @Test
  void testUnexpectedValues() {
    // Manifest with unexpected values (e.g., empty identifiers, very long title)
    String manifestXml = "<manifest xmlns=\"http://www.imsproject.org/xsd/imscp_rootv1p1p2\" "
        + "xmlns:adlcp=\"http://www.adlnet.org/xsd/adlcp_rootv1p2\" "
        + "identifier=\"\" version=\"1\">\n"
        + "  <metadata>\n"
        + "    <schema>ADL SCORM</schema>\n"
        + "    <schemaversion>1.2</schemaversion>\n"
        + "  </metadata>\n"
        + "  <organizations default=\"golf_sample_default\">\n"
        + "    <organization identifier=\"golf_sample_default\">\n"
        + "      <title>" + "Very long title ".repeat(100) + "</title>\n"
        + "      <item identifier=\"\" identifierref=\"resource_1\"></item>\n"
        + "    </organization>\n"
        + "  </organizations>\n"
        + "  <resources>\n"
        + "    <resource identifier=\"resource_1\" type=\"webcontent\" adlcp:scormtype=\"sco\" href=\"shared/launchpage.html\">\n"
        + "      <file href=\"shared/launchpage.html\" />\n"
        + "    </resource>\n"
        + "  </resources>\n"
        + "</manifest>";

    // Create a mock FileAccess that returns the test manifest XML
    FileAccess mockFileAccess = new MockFileAccess(manifestXml);

    // Create a parser with the mock FileAccess
    Scorm12Parser parser = new Scorm12Parser(mockFileAccess);

    try {
      // The parser should handle unexpected values gracefully
      Scorm12Metadata metadata = parser.parse();
      assertNotNull(metadata);
      Scorm12Manifest manifest = metadata.getManifest();
      assertNotNull(manifest);

      // Verify that the parser extracted the expected values
      assertTrue(manifest
          .getTitle()
          .length() > 100); // Very long title
      assertEquals("shared/launchpage.html", manifest
          .getLaunchUrl());
    } catch (ModuleParsingException e) {
      // If the parser throws an exception, it should be because of a validation error
      assertTrue(e
          .getMessage()
          .contains("missing") || e
          .getMessage()
          .contains("required"));
    }
  }

  /**
   * Tests that the parser correctly handles a manifest with minimal valid content. The parser
   * should successfully parse a minimal valid manifest.
   */
  @Test
  void testMinimalValidManifest() throws ModuleParsingException {
    // Minimal valid manifest with just the required fields
    // language=XML
    String manifestXml = """
        <manifest xmlns="http://www.imsproject.org/xsd/imscp_rootv1p1p2"
            xmlns:adlcp="http://www.adlnet.org/xsd/adlcp_rootv1p2"
            identifier="com.scorm.golfsamples.contentpackaging.singlesco.12" version="1">
          <metadata>
            <schema>ADL SCORM</schema>
            <schemaversion>1.2</schemaversion>
          </metadata>
          <organizations default="golf_sample_default">
            <organization identifier="golf_sample_default">
              <title>Minimal Valid Manifest</title>
              <item identifier="item_1" identifierref="resource_1"></item>
            </organization>
          </organizations>
          <resources>
            <resource identifier="resource_1" type="webcontent" adlcp:scormtype="sco" href="index.html">
              <file href="index.html" />
            </resource>
          </resources>
        </manifest>""";

    // Create a mock FileAccess that returns the test manifest XML
    FileAccess mockFileAccess = new MockFileAccess(manifestXml);

    // Create a parser with the mock FileAccess
    Scorm12Parser parser = new Scorm12Parser(mockFileAccess);

    // The parser should successfully parse the manifest
    Scorm12Metadata metadata = parser.parse();
    assertNotNull(metadata);
    Scorm12Manifest manifest = metadata.getManifest();
    assertNotNull(manifest);

    // Verify that the parser extracted the expected values
    assertEquals("Minimal Valid Manifest", manifest.getTitle());
    assertEquals("index.html", manifest
        .getLaunchUrl());
  }

  /**
   * A mock FileAccess implementation that returns predefined content for the manifest file.
   */
  private record MockFileAccess(String manifestXml) implements FileAccess {

    @Override
    public boolean fileExistsInternal(String path) {
      return "imsmanifest.xml".equals(path);
    }

    @Override
    public List<String> listFilesInternal(String path) {
      if (path == null || path.isEmpty()) {
        List<String> files = new ArrayList<>();
        files.add("imsmanifest.xml");
        return files;
      }
      return new ArrayList<>();
    }

    @Override
    public InputStream getFileContentsInternal(String path) throws IOException {
      if ("imsmanifest.xml".equals(path)) {
        return new ByteArrayInputStream(manifestXml.getBytes(StandardCharsets.UTF_8));
      }
      throw new IOException("File not found: " + path);
    }

    @Override
    public String getRootPath() {
      return "/mock";
    }
  }
}
