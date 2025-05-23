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
import dev.jcputney.elearning.parser.input.lom.types.LangString;
import dev.jcputney.elearning.parser.input.lom.types.Purpose;
import dev.jcputney.elearning.parser.input.lom.types.Taxon;
import dev.jcputney.elearning.parser.input.lom.types.TaxonPath;
import java.util.List;
import org.junit.jupiter.api.Test;

/**
 * Tests for the {@link Classification} class.
 */
class ClassificationTest {

  private final XmlMapper xmlMapper = new XmlMapper();

  @Test
  void testDeserializeEmptyClassification() throws Exception {
    // Given
    String xml = "<classification xmlns=\"http://ltsc.ieee.org/xsd/LOM\"></classification>";

    // When
    Classification classification = xmlMapper.readValue(xml, Classification.class);

    // Then
    assertNotNull(classification);
    assertNull(classification.getPurpose());
    assertNull(classification.getTaxonPaths());
    assertNull(classification.getDescription());
    assertNull(classification.getKeywords());
  }

  @Test
  void testDeserializeClassificationWithPurpose() throws Exception {
    // Given
    String xml = "<classification xmlns=\"http://ltsc.ieee.org/xsd/LOM\">"
        + "  <purpose>"
        + "    <source>LOMv1.0</source>"
        + "    <value>educational objective</value>"
        + "  </purpose>"
        + "</classification>";

    // When
    Classification classification = xmlMapper.readValue(xml, Classification.class);

    // Then
    assertNotNull(classification);
    assertNotNull(classification.getPurpose());
    assertEquals("LOMv1.0", classification
        .getPurpose()
        .getSource());
    assertEquals(Purpose.EDUCATIONAL_OBJECTIVE, classification
        .getPurpose()
        .getValue());
  }

  @Test
  void testDeserializeClassificationWithTaxonPath() throws Exception {
    // Given
    String xml = "<classification xmlns=\"http://ltsc.ieee.org/xsd/LOM\">"
        + "  <taxonPath>"
        + "    <source>"
        + "      <string language=\"en\">Taxonomy Source</string>"
        + "    </source>"
        + "    <taxon>"
        + "      <id>1</id>"
        + "      <entry>"
        + "        <string language=\"en\">Category 1</string>"
        + "      </entry>"
        + "    </taxon>"
        + "    <taxon>"
        + "      <id>1.1</id>"
        + "      <entry>"
        + "        <string language=\"en\">Subcategory 1.1</string>"
        + "      </entry>"
        + "    </taxon>"
        + "  </taxonPath>"
        + "</classification>";

    // When
    Classification classification = xmlMapper.readValue(xml, Classification.class);

    // Then
    assertNotNull(classification);
    assertNotNull(classification.getTaxonPaths());
    assertEquals(1, classification
        .getTaxonPaths()
        .size());

    TaxonPath taxonPath = classification
        .getTaxonPaths()
        .get(0);
    assertNotNull(taxonPath.getSource());
    assertEquals("en", taxonPath
        .getSource()
        .getLangString()
        .getLanguage());
    assertEquals("Taxonomy Source", taxonPath
        .getSource()
        .getLangString()
        .getValue());

    assertNotNull(taxonPath.getTaxons());
    assertEquals(2, taxonPath
        .getTaxons()
        .size());

    Taxon taxon1 = taxonPath
        .getTaxons()
        .get(0);
    assertEquals("1", taxon1.getId());
    assertEquals("en", taxon1
        .getEntry()
        .getLangString()
        .getLanguage());
    assertEquals("Category 1", taxon1
        .getEntry()
        .getLangString()
        .getValue());

    Taxon taxon2 = taxonPath
        .getTaxons()
        .get(1);
    assertEquals("1.1", taxon2.getId());
    assertEquals("en", taxon2
        .getEntry()
        .getLangString()
        .getLanguage());
    assertEquals("Subcategory 1.1", taxon2
        .getEntry()
        .getLangString()
        .getValue());
  }

  @Test
  void testDeserializeClassificationWithDescription() throws Exception {
    // Given
    String xml = "<classification xmlns=\"http://ltsc.ieee.org/xsd/LOM\">"
        + "  <description>"
        + "    <string language=\"en\">This is a classification description</string>"
        + "  </description>"
        + "</classification>";

    // When
    Classification classification = xmlMapper.readValue(xml, Classification.class);

    // Then
    assertNotNull(classification);
    assertNotNull(classification.getDescription());
    assertNotNull(classification
        .getDescription()
        .getLangStrings()
        .get(0));
    assertEquals("en", classification
        .getDescription()
        .getLangStrings()
        .get(0)
        .getLanguage());
    assertEquals("This is a classification description",
        classification
            .getDescription()
            .getLangStrings()
            .get(0)
            .getValue());
  }

  @Test
  void testDeserializeClassificationWithKeywords() throws Exception {
    // Given
    String xml = "<classification xmlns=\"http://ltsc.ieee.org/xsd/LOM\">"
        + "  <keyword>"
        + "    <string language=\"en\">Keyword1</string>"
        + "    <string language=\"en\">Keyword2</string>"
        + "    <string language=\"fr\">MotClé</string>"
        + "  </keyword>"
        + "</classification>";

    // When
    Classification classification = xmlMapper.readValue(xml, Classification.class);

    // Then
    assertNotNull(classification);
    assertNotNull(classification.getKeywords());
    assertNotNull(classification
        .getKeywords()
        .getLangStrings());
    assertEquals(3, classification
        .getKeywords()
        .getLangStrings()
        .size());

    List<LangString> langStrings = classification
        .getKeywords()
        .getLangStrings();
    assertEquals("en", langStrings
        .get(0)
        .getLanguage());
    assertEquals("Keyword1", langStrings
        .get(0)
        .getValue());
    assertEquals("en", langStrings
        .get(1)
        .getLanguage());
    assertEquals("Keyword2", langStrings
        .get(1)
        .getValue());
    assertEquals("fr", langStrings
        .get(2)
        .getLanguage());
    assertEquals("MotClé", langStrings
        .get(2)
        .getValue());
  }

  @Test
  void testDeserializeCompleteClassification() throws Exception {
    // Given
    String xml = "<classification xmlns=\"http://ltsc.ieee.org/xsd/LOM\">"
        + "  <purpose>"
        + "    <source>LOMv1.0</source>"
        + "    <value>educational objective</value>"
        + "  </purpose>"
        + "  <taxonPath>"
        + "    <source>"
        + "      <string language=\"en\">Taxonomy Source</string>"
        + "    </source>"
        + "    <taxon>"
        + "      <id>1</id>"
        + "      <entry>"
        + "        <string language=\"en\">Category 1</string>"
        + "      </entry>"
        + "    </taxon>"
        + "  </taxonPath>"
        + "  <description>"
        + "    <string language=\"en\">This is a classification description</string>"
        + "  </description>"
        + "  <keyword>"
        + "    <string language=\"en\">Keyword1</string>"
        + "    <string language=\"en\">Keyword2</string>"
        + "  </keyword>"
        + "</classification>";

    // When
    Classification classification = xmlMapper.readValue(xml, Classification.class);

    // Then
    assertNotNull(classification);

    // Check purpose
    assertNotNull(classification.getPurpose());
    assertEquals("LOMv1.0", classification
        .getPurpose()
        .getSource());
    assertEquals(Purpose.EDUCATIONAL_OBJECTIVE, classification
        .getPurpose()
        .getValue());

    // Check taxonPath
    assertNotNull(classification.getTaxonPaths());
    assertEquals(1, classification
        .getTaxonPaths()
        .size());
    TaxonPath taxonPath = classification
        .getTaxonPaths()
        .get(0);
    assertNotNull(taxonPath.getSource());
    assertEquals("Taxonomy Source", taxonPath
        .getSource()
        .getLangString()
        .getValue());
    assertNotNull(taxonPath.getTaxons());
    assertEquals(1, taxonPath
        .getTaxons()
        .size());
    assertEquals("1", taxonPath
        .getTaxons()
        .get(0)
        .getId());
    assertEquals("Category 1", taxonPath
        .getTaxons()
        .get(0)
        .getEntry()
        .getLangString()
        .getValue());

    // Check description
    assertNotNull(classification.getDescription());
    assertEquals("This is a classification description",
        classification
            .getDescription()
            .getLangStrings()
            .get(0)
            .getValue());

    // Check keywords
    assertNotNull(classification.getKeywords());
    assertNotNull(classification
        .getKeywords()
        .getLangStrings());
    assertEquals(2, classification
        .getKeywords()
        .getLangStrings()
        .size());
    assertEquals("Keyword1", classification
        .getKeywords()
        .getLangStrings()
        .get(0)
        .getValue());
    assertEquals("Keyword2", classification
        .getKeywords()
        .getLangStrings()
        .get(1)
        .getValue());
  }
}