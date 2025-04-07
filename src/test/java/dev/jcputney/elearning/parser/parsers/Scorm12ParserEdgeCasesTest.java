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
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import dev.jcputney.elearning.parser.exception.ModuleParsingException;
import dev.jcputney.elearning.parser.impl.LocalFileAccess;
import dev.jcputney.elearning.parser.input.scorm12.Scorm12Manifest;
import dev.jcputney.elearning.parser.output.metadata.scorm12.Scorm12Metadata;
import javax.xml.stream.XMLStreamException;
import org.junit.jupiter.api.Test;

/**
 * Tests for edge cases and error conditions in the SCORM 1.2 parser.
 */
class Scorm12ParserEdgeCasesTest {

  @Test
  void testParseScorm12Course_MissingRequiredFields() {
    String modulePath = "src/test/resources/modules/scorm12/MissingRequiredFields_SCORM12/";
    Scorm12Parser parser = new Scorm12Parser(new LocalFileAccess(modulePath));

    // The parser should throw a ModuleParsingException because the title is missing
    ModuleParsingException exception = assertThrows(ModuleParsingException.class, parser::parse);
    assertTrue(exception.getMessage().contains("missing a required <title> element"));
  }

  @Test
  void testParseScorm12Course_MissingLaunchUrl() {
    String modulePath = "src/test/resources/modules/scorm12/MissingLaunchUrl_SCORM12/";
    Scorm12Parser parser = new Scorm12Parser(new LocalFileAccess(modulePath));

    // The parser should throw a ModuleParsingException because the launch URL is missing
    ModuleParsingException exception = assertThrows(ModuleParsingException.class, parser::parse);
    assertTrue(exception.getMessage().contains("missing a required <launchUrl>"));
  }

  @Test
  void testParseScorm12Course_MalformedXml() {
    String modulePath = "src/test/resources/modules/scorm12/MalformedXml_SCORM12/";
    Scorm12Parser parser = new Scorm12Parser(new LocalFileAccess(modulePath));

    // The parser should throw an exception because the XML is malformed
    Exception exception = assertThrows(Exception.class, parser::parse);
    // The exact exception type might be XMLStreamException or ModuleParsingException depending on the implementation
    assertTrue(
        exception instanceof XMLStreamException || exception instanceof ModuleParsingException);
  }

  @Test
  void testParseScorm12Course_InvalidFileReferences() throws ModuleParsingException {
    String modulePath = "src/test/resources/modules/scorm12/InvalidFileReferences_SCORM12/";
    Scorm12Parser parser = new Scorm12Parser(new LocalFileAccess(modulePath));

    // The parser should still parse the manifest even if the referenced files don't exist
    Scorm12Metadata metadata = parser.parse();
    assertNotNull(metadata);
    Scorm12Manifest manifest = metadata.getManifest();
    assertNotNull(manifest);

    // Verify that the manifest was parsed correctly
    assertEquals("Invalid File References Test", manifest.getTitle());
    assertEquals("content/nonexistent.html", manifest.getLaunchUrl());

    // Verify that the resources were parsed correctly
    assertEquals(2, manifest.getResources().getResourceList().size());

    // Verify that the files in the resources were marked as not existing
    assertFalse(manifest.getResources().getResourceList().get(0).getFiles().get(0).isExists());
    assertFalse(manifest.getResources().getResourceList().get(0).getFiles().get(1).isExists());
    assertFalse(manifest.getResources().getResourceList().get(0).getFiles().get(2).isExists());
    assertFalse(manifest.getResources().getResourceList().get(1).getFiles().get(0).isExists());
  }

  @Test
  void testParseScorm12Course_ComplexNestedOrganization() throws ModuleParsingException {
    String modulePath = "src/test/resources/modules/scorm12/ComplexNestedOrganization_SCORM12/";
    Scorm12Parser parser = new Scorm12Parser(new LocalFileAccess(modulePath));

    // The parser should parse the manifest with the complex nested organization
    Scorm12Metadata metadata = parser.parse();
    assertNotNull(metadata);
    Scorm12Manifest manifest = metadata.getManifest();
    assertNotNull(manifest);

    // Verify that the manifest was parsed correctly
    assertEquals("Complex Nested Organization Test", manifest.getTitle());
    assertEquals("content/module1.html", manifest.getLaunchUrl());

    // Verify that the organizations were parsed correctly
    assertEquals(2, manifest.getOrganizations().getOrganizationList().size());
    assertEquals("default_org", manifest.getOrganizations().getDefault().getIdentifier());
    assertEquals("alternate_org",
        manifest.getOrganizations().getOrganizationList().get(1).getIdentifier());

    // Verify that the items in the default organization were parsed correctly
    assertEquals(3, manifest.getOrganizations().getDefault().getItems().size());

    // Verify that the nested items were parsed correctly
    var module1 = manifest.getOrganizations().getDefault().getItems().get(0);
    assertEquals("module_1", module1.getIdentifier());
    assertEquals(2, module1.getItems().size());

    var chapter11 = module1.getItems().get(0);
    assertEquals("chapter_1_1", chapter11.getIdentifier());
    assertEquals(2, chapter11.getItems().size());

    var section111 = chapter11.getItems().get(0);
    assertEquals("section_1_1_1", section111.getIdentifier());
    assertEquals(2, section111.getItems().size());

    var topic1111 = section111.getItems().get(0);
    assertEquals("topic_1_1_1_1", topic1111.getIdentifier());
    assertTrue(topic1111.getItems() == null || topic1111.getItems().isEmpty(),
        "Expected topic1111 to have no items");

    // Verify that the resources were parsed correctly
    assertEquals(12, manifest.getResources().getResourceList().size());
  }
}
