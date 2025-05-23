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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import dev.jcputney.elearning.parser.input.lom.types.AggregationLevel;
import dev.jcputney.elearning.parser.input.lom.types.CatalogEntry;
import dev.jcputney.elearning.parser.input.lom.types.Identifier;
import dev.jcputney.elearning.parser.input.lom.types.LangString;
import dev.jcputney.elearning.parser.input.lom.types.Structure;
import dev.jcputney.elearning.parser.input.lom.types.UnboundLangString;
import java.util.List;
import org.junit.jupiter.api.Test;

/**
 * Tests for the {@link General} class.
 */
class GeneralTest {

  private final XmlMapper xmlMapper = new XmlMapper();

  @Test
  void testDeserializeEmptyGeneral() throws Exception {
    // Given
    String xml = "<general xmlns=\"http://ltsc.ieee.org/xsd/LOM\"></general>";

    // When
    General general = xmlMapper.readValue(xml, General.class);

    // Then
    assertNotNull(general);
    assertNull(general.getIdentifiers());
    assertNull(general.getTitle());
    assertNull(general.getCatalogEntries());
    assertNull(general.getLanguage());
    assertNull(general.getDescription());
    assertNull(general.getKeywords());
    assertNull(general.getCoverage());
    assertNull(general.getStructure());
    assertNull(general.getAggregationLevel());
  }

  @Test
  void testDeserializeGeneralWithBasicFields() throws Exception {
    // Given
    String xml = "<general xmlns=\"http://ltsc.ieee.org/xsd/LOM\">"
        + "  <title>"
        + "    <string language=\"en\">Test Title</string>"
        + "  </title>"
        + "  <language>en</language>"
        + "  <description>"
        + "    <string language=\"en\">This is a test description</string>"
        + "  </description>"
        + "</general>";

    // When
    General general = xmlMapper.readValue(xml, General.class);

    // Then
    assertNotNull(general);
    assertNotNull(general.getTitle());
    assertNotNull(general
        .getTitle()
        .getLangStrings());
    assertEquals(1, general
        .getTitle()
        .getLangStrings()
        .size());
    assertEquals("en", general
        .getTitle()
        .getLangStrings()
        .get(0)
        .getLanguage());
    assertEquals("Test Title", general
        .getTitle()
        .getLangStrings()
        .get(0)
        .getValue());

    assertEquals("en", general.getLanguage());

    assertNotNull(general.getDescription());
    assertNotNull(general
        .getDescription()
        .getLangStrings());
    assertEquals(1, general
        .getDescription()
        .getLangStrings()
        .size());
    assertEquals("en", general
        .getDescription()
        .getLangStrings()
        .get(0)
        .getLanguage());
    assertEquals("This is a test description",
        general
            .getDescription()
            .getLangStrings()
            .get(0)
            .getValue());
  }

  @Test
  void testDeserializeGeneralWithIdentifiersAndCatalogEntries() throws Exception {
    // Given
    String xml = "<general xmlns=\"http://ltsc.ieee.org/xsd/LOM\">"
        + "  <identifier>"
        + "    <catalog>URI</catalog>"
        + "    <entry>http://example.com/course/123</entry>"
        + "  </identifier>"
        + "  <catalogentry>"
        + "    <catalog>Catalog1</catalog>"
        + "    <entry>"
        + "      <string language=\"en\">Entry1</string>"
        + "    </entry>"
        + "  </catalogentry>"
        + "</general>";

    // When
    General general = xmlMapper.readValue(xml, General.class);

    // Then
    assertNotNull(general);

    // Check identifiers
    assertNotNull(general.getIdentifiers());
    assertEquals(1, general
        .getIdentifiers()
        .size());
    Identifier identifier = general
        .getIdentifiers()
        .get(0);
    assertEquals("URI", identifier.getCatalog());
    assertEquals("http://example.com/course/123", identifier.getEntry());

    // Check catalog entries
    assertNotNull(general.getCatalogEntries());
    assertEquals(1, general
        .getCatalogEntries()
        .size());
    CatalogEntry entry = general
        .getCatalogEntries()
        .get(0);
    assertEquals("Catalog1", entry.getCatalog());
    assertEquals("en", entry
        .getEntry()
        .getLangStrings()
        .get(0)
        .getLanguage());
    assertEquals("Entry1", entry
        .getEntry()
        .getLangStrings()
        .get(0)
        .getValue());
  }

  @Test
  void testDeserializeGeneralWithStructureAndAggregationLevel() throws Exception {
    // Given
    String xml = "<general xmlns=\"http://ltsc.ieee.org/xsd/LOM\">"
        + "  <structure>"
        + "    <source>LOMv1.0</source>"
        + "    <value>hierarchical</value>"
        + "  </structure>"
        + "  <aggregationLevel>"
        + "    <source>LOMv1.0</source>"
        + "    <value>2</value>"
        + "  </aggregationLevel>"
        + "</general>";

    // When
    General general = xmlMapper.readValue(xml, General.class);

    // Then
    assertNotNull(general);

    // Check structure
    assertNotNull(general.getStructure());
    assertEquals("LOMv1.0", general
        .getStructure()
        .getSource());
    assertEquals(Structure.HIERARCHICAL, general
        .getStructure()
        .getValue());

    // Check aggregation level
    assertNotNull(general.getAggregationLevel());
    assertEquals("LOMv1.0", general
        .getAggregationLevel()
        .getSource());
    assertEquals(AggregationLevel.LEVEL_2, general
        .getAggregationLevel()
        .getValue());
  }

  @Test
  void testDeserializeGeneralWithKeywords() throws Exception {
    // Given
    String xml = "<general xmlns=\"http://ltsc.ieee.org/xsd/LOM\">"
        + "  <keyword>"
        + "    <string language=\"en\">XML</string>"
        + "  </keyword>"
        + "  <keyword>"
        + "    <string language=\"en\">Learning</string>"
        + "  </keyword>"
        + "  <keyword>"
        + "    <string language=\"fr\">Apprentissage</string>"
        + "  </keyword>"
        + "</general>";

    // When
    General general = xmlMapper.readValue(xml, General.class);

    // Then
    assertNotNull(general);
    assertNotNull(general.getKeywords());
    assertEquals(3, general
        .getKeywords()
        .size());

    UnboundLangString keyword1 = general
        .getKeywords()
        .get(0);
    assertEquals("en", keyword1
        .getLangStrings()
        .get(0)
        .getLanguage());
    assertEquals("XML", keyword1
        .getLangStrings()
        .get(0)
        .getValue());

    UnboundLangString keyword2 = general
        .getKeywords()
        .get(1);
    assertEquals("en", keyword2
        .getLangStrings()
        .get(0)
        .getLanguage());
    assertEquals("Learning", keyword2
        .getLangStrings()
        .get(0)
        .getValue());

    UnboundLangString keyword3 = general
        .getKeywords()
        .get(2);
    assertEquals("fr", keyword3
        .getLangStrings()
        .get(0)
        .getLanguage());
    assertEquals("Apprentissage", keyword3
        .getLangStrings()
        .get(0)
        .getValue());
  }

  @Test
  void testDeserializeGeneralWithCoverage() throws Exception {
    // Given
    String xml = "<general xmlns=\"http://ltsc.ieee.org/xsd/LOM\">"
        + "  <coverage>"
        + "    <string language=\"en\">21st century</string>"
        + "    <string language=\"fr\">21ème siècle</string>"
        + "  </coverage>"
        + "</general>";

    // When
    General general = xmlMapper.readValue(xml, General.class);

    // Then
    assertNotNull(general);
    assertNotNull(general.getCoverage());
    assertNotNull(general
        .getCoverage()
        .getLangStrings());
    assertEquals(2, general
        .getCoverage()
        .getLangStrings()
        .size());

    List<LangString> langStrings = general
        .getCoverage()
        .getLangStrings();
    assertEquals("en", langStrings
        .get(0)
        .getLanguage());
    assertEquals("21st century", langStrings
        .get(0)
        .getValue());
    assertEquals("fr", langStrings
        .get(1)
        .getLanguage());
    assertEquals("21ème siècle", langStrings
        .get(1)
        .getValue());
  }

  @Test
  void testDeserializeCompleteGeneral() throws Exception {
    // Given
    String xml = "<general xmlns=\"http://ltsc.ieee.org/xsd/LOM\">"
        + "  <identifier>"
        + "    <catalog>URI</catalog>"
        + "    <entry>http://example.com/course/123</entry>"
        + "  </identifier>"
        + "  <title>"
        + "    <string language=\"en\">Complete Test Course</string>"
        + "    <string language=\"fr\">Cours de Test Complet</string>"
        + "  </title>"
        + "  <catalogentry>"
        + "    <catalog>Catalog1</catalog>"
        + "    <entry>"
        + "      <string language=\"en\">Entry1</string>"
        + "    </entry>"
        + "  </catalogentry>"
        + "  <language>en</language>"
        + "  <description>"
        + "    <string language=\"en\">This is a complete test description</string>"
        + "    <string language=\"fr\">C'est une description de test complète</string>"
        + "  </description>"
        + "  <keyword>"
        + "    <string language=\"en\">XML</string>"
        + "  </keyword>"
        + "  <keyword>"
        + "    <string language=\"en\">Learning</string>"
        + "  </keyword>"
        + "  <coverage>"
        + "    <string language=\"en\">21st century</string>"
        + "  </coverage>"
        + "  <structure>"
        + "    <source>LOMv1.0</source>"
        + "    <value>hierarchical</value>"
        + "  </structure>"
        + "  <aggregationLevel>"
        + "    <source>LOMv1.0</source>"
        + "    <value>2</value>"
        + "  </aggregationLevel>"
        + "</general>";

    // When
    General general = xmlMapper.readValue(xml, General.class);

    // Then
    assertNotNull(general);

    // Check identifiers
    assertNotNull(general.getIdentifiers());
    assertEquals(1, general
        .getIdentifiers()
        .size());
    Identifier identifier = general
        .getIdentifiers()
        .get(0);
    assertEquals("URI", identifier.getCatalog());
    assertEquals("http://example.com/course/123", identifier.getEntry());

    // Check title
    assertNotNull(general.getTitle());
    assertNotNull(general
        .getTitle()
        .getLangStrings());
    assertEquals(2, general
        .getTitle()
        .getLangStrings()
        .size());
    assertEquals("en", general
        .getTitle()
        .getLangStrings()
        .get(0)
        .getLanguage());
    assertEquals("Complete Test Course", general
        .getTitle()
        .getLangStrings()
        .get(0)
        .getValue());
    assertEquals("fr", general
        .getTitle()
        .getLangStrings()
        .get(1)
        .getLanguage());
    assertEquals("Cours de Test Complet", general
        .getTitle()
        .getLangStrings()
        .get(1)
        .getValue());

    // Check catalog entries
    assertNotNull(general.getCatalogEntries());
    assertEquals(1, general
        .getCatalogEntries()
        .size());
    CatalogEntry entry = general
        .getCatalogEntries()
        .get(0);
    assertEquals("Catalog1", entry.getCatalog());
    assertEquals("en", entry
        .getEntry()
        .getLangStrings()
        .get(0)
        .getLanguage());
    assertEquals("Entry1", entry
        .getEntry()
        .getLangStrings()
        .get(0)
        .getValue());

    // Check language
    assertEquals("en", general.getLanguage());

    // Check description
    assertNotNull(general.getDescription());
    assertNotNull(general
        .getDescription()
        .getLangStrings());
    assertEquals(2, general
        .getDescription()
        .getLangStrings()
        .size());
    assertEquals("en", general
        .getDescription()
        .getLangStrings()
        .get(0)
        .getLanguage());
    assertEquals("This is a complete test description",
        general
            .getDescription()
            .getLangStrings()
            .get(0)
            .getValue());
    assertEquals("fr", general
        .getDescription()
        .getLangStrings()
        .get(1)
        .getLanguage());
    assertEquals("C'est une description de test complète",
        general
            .getDescription()
            .getLangStrings()
            .get(1)
            .getValue());

    // Check keywords
    assertNotNull(general.getKeywords());
    assertEquals(2, general
        .getKeywords()
        .size());
    assertEquals("en", general
        .getKeywords()
        .get(0)
        .getLangStrings()
        .get(0)
        .getLanguage());
    assertEquals("XML", general
        .getKeywords()
        .get(0)
        .getLangStrings()
        .get(0)
        .getValue());
    assertEquals("en", general
        .getKeywords()
        .get(1)
        .getLangStrings()
        .get(0)
        .getLanguage());
    assertEquals("Learning", general
        .getKeywords()
        .get(1)
        .getLangStrings()
        .get(0)
        .getValue());

    // Check coverage
    assertNotNull(general.getCoverage());
    assertNotNull(general
        .getCoverage()
        .getLangStrings());
    assertEquals(1, general
        .getCoverage()
        .getLangStrings()
        .size());
    assertEquals("en", general
        .getCoverage()
        .getLangStrings()
        .get(0)
        .getLanguage());
    assertEquals("21st century", general
        .getCoverage()
        .getLangStrings()
        .get(0)
        .getValue());

    // Check structure
    assertNotNull(general.getStructure());
    assertEquals("LOMv1.0", general
        .getStructure()
        .getSource());
    assertEquals(Structure.HIERARCHICAL, general
        .getStructure()
        .getValue());

    // Check aggregation level
    assertNotNull(general.getAggregationLevel());
    assertEquals("LOMv1.0", general
        .getAggregationLevel()
        .getSource());
    assertEquals(AggregationLevel.LEVEL_2, general
        .getAggregationLevel()
        .getValue());
  }
}
