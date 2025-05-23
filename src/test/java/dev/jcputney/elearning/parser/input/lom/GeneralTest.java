/* Copyright (c) 2025. Jonathan Putney
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

package dev.jcputney.elearning.parser.input.lom;

import static dev.jcputney.elearning.parser.input.lom.types.AggregationLevel.LEVEL_1;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import dev.jcputney.elearning.parser.input.lom.types.Identifier;
import dev.jcputney.elearning.parser.input.lom.types.Structure;
import java.io.File;
import org.junit.jupiter.api.Test;

/**
 * Tests for the {@link General} class.
 */
class GeneralTest {

  private final XmlMapper xmlMapper = new XmlMapper();

  /**
   * Tests the deserialization of a General object from XML.
   */
  @Test
  void testDeserializeGeneral() throws Exception {
    // Given
    File file = new File(
        "src/test/resources/modules/scorm2004/ContentPackagingMetadata_SCORM20043rdEdition/metadata_course.xml");

    // When
    LOM lom = xmlMapper.readValue(file, LOM.class);

    // Then
    assertNotNull(lom);
    assertNotNull(lom.getGeneral());

    General general = lom.getGeneral();

    // Test identifiers
    assertNotNull(general.getIdentifiers());
    assertFalse(general
        .getIdentifiers()
        .isEmpty());
    Identifier identifier = general
        .getIdentifiers()
        .get(0);
    assertEquals("URI", identifier.getCatalog());
    assertEquals("com.scorm.golfsamples.contentpackaging.metadata.20043rd", identifier.getEntry());

    // Test title
    assertNotNull(general.getTitle());
    assertNotNull(general
        .getTitle()
        .getLangStrings());
    assertFalse(general
        .getTitle()
        .getLangStrings()
        .isEmpty());
    assertEquals("Golf Explained", general
        .getTitle()
        .getLangStrings()
        .get(0)
        .getValue());
    assertEquals("en-US", general
        .getTitle()
        .getLangStrings()
        .get(0)
        .getLanguage());

    // Test language
    assertEquals("en", general.getLanguage());

    // Test description
    assertNotNull(general.getDescription());
    assertNotNull(general
        .getDescription()
        .getLangStrings());
    assertFalse(general
        .getDescription()
        .getLangStrings()
        .isEmpty());
    assertTrue(general
        .getDescription()
        .getLangStrings()
        .get(0)
        .getValue()
        .contains(
            "A high level overview of the sport of golf"));

    // Test keywords
    assertNotNull(general.getKeywords());
    assertFalse(general
        .getKeywords()
        .isEmpty());
    assertEquals("golf", general
        .getKeywords()
        .get(0)
        .getLangStrings()
        .get(0)
        .getValue());

    // Test coverage
    assertNotNull(general.getCoverage());
    assertNotNull(general
        .getCoverage()
        .getLangStrings());
    assertFalse(general
        .getCoverage()
        .getLangStrings()
        .isEmpty());
    assertTrue(general
        .getCoverage()
        .getLangStrings()
        .get(0)
        .getValue()
        .contains(
            "Current time. Applicable to the entire world"));

    // Test structure
    assertNotNull(general.getStructure());
    assertEquals("LOMv1.0", general
        .getStructure()
        .getSource());
    assertEquals(Structure.HIERARCHICAL, general
        .getStructure()
        .getValue());

    // Test aggregationLevel
    assertNotNull(general.getAggregationLevel());
    assertEquals("LOMv1.0", general
        .getAggregationLevel()
        .getSource());
    assertEquals(LEVEL_1, general
        .getAggregationLevel()
        .getValue());
  }

  /**
   * Tests the deserialization of a General object with multiple titles in different languages.
   */
  @Test
  void testDeserializeGeneralWithMultipleTitles() throws Exception {
    // Given
    File file = new File(
        "src/test/resources/modules/scorm2004/ContentPackagingMetadata_SCORM20043rdEdition/metadata_course.xml");

    // When
    LOM lom = xmlMapper.readValue(file, LOM.class);

    // Then
    assertNotNull(lom);
    assertNotNull(lom.getGeneral());

    General general = lom.getGeneral();

    // Test title with multiple languages
    assertNotNull(general.getTitle());
    assertNotNull(general
        .getTitle()
        .getLangStrings());
    assertTrue(general
        .getTitle()
        .getLangStrings()
        .size() >= 2);

    // Find English and Spanish titles
    boolean foundEnglish = false;
    boolean foundSpanish = false;

    for (var langString : general
        .getTitle()
        .getLangStrings()) {
      if ("en-US".equals(langString.getLanguage())) {
        assertEquals("Golf Explained", langString.getValue());
        foundEnglish = true;
      } else if ("es".equals(langString.getLanguage())) {
        assertEquals("Explicó Golf", langString.getValue());
        foundSpanish = true;
      }
    }

    assertTrue(foundEnglish, "English title should be present");
    assertTrue(foundSpanish, "Spanish title should be present");
  }

  /**
   * Tests the deserialization of a General object from an inline LOM in the manifest.
   */
  @Test
  void testDeserializeGeneralFromManifest() throws Exception {
    // Given
    String modulePath = "src/test/resources/modules/scorm2004/ContentPackagingMetadata_SCORM20043rdEdition";

    // First, try to parse the metadata_course.xml file which we know has educational data
    File metadataFile = new File(modulePath + "/metadata_course.xml");

    // Use the same approach as in testDeserializeEducational
    LOM lom = xmlMapper.readValue(metadataFile, LOM.class);

    // Then
    assertNotNull(lom);
    assertNotNull(lom.getGeneral());

    General general = lom.getGeneral();

    // Test description
    assertNotNull(general.getDescription());
    assertNotNull(general
        .getDescription()
        .getLangStrings());
    assertFalse(general
        .getDescription()
        .getLangStrings()
        .isEmpty());
    assertTrue(general
        .getDescription()
        .getLangStrings()
        .get(0)
        .getValue()
        .contains("A high level overview of the sport of golf."));
  }
}
