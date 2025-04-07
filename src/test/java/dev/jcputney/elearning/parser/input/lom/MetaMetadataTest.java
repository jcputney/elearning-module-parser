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
import dev.jcputney.elearning.parser.input.lom.types.CatalogEntry;
import dev.jcputney.elearning.parser.input.lom.types.ContributeMeta;
import dev.jcputney.elearning.parser.input.lom.types.Date;
import dev.jcputney.elearning.parser.input.lom.types.Identifier;
import dev.jcputney.elearning.parser.input.lom.types.RoleMeta;
import dev.jcputney.elearning.parser.input.lom.types.SourceValuePair;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests for the {@link MetaMetadata} class.
 */
class MetaMetadataTest {

  private XmlMapper xmlMapper;

  @BeforeEach
  void setUp() {
    xmlMapper = new XmlMapper();
  }

  @Test
  void testDeserializeEmptyMetaMetadata() throws Exception {
    // Given
    String xml = "<metaMetadata xmlns=\"http://ltsc.ieee.org/xsd/LOM\"></metaMetadata>";

    // When
    MetaMetadata metaMetadata = xmlMapper.readValue(xml, MetaMetadata.class);

    // Then
    assertNotNull(metaMetadata);
    assertNull(metaMetadata.getIdentifier());
    assertNull(metaMetadata.getCatalogEntries());
    assertNull(metaMetadata.getContribute());
    assertNull(metaMetadata.getMetadataSchema());
    assertNull(metaMetadata.getLanguage());
    assertNull(metaMetadata.getCustomElements());
  }

  @Test
  void testDeserializeMetaMetadataWithIdentifier() throws Exception {
    // Given
    String xml = "<metaMetadata xmlns=\"http://ltsc.ieee.org/xsd/LOM\">"
        + "  <identifier>"
        + "    <catalog>URI</catalog>"
        + "    <entry>http://example.com/metadata/123</entry>"
        + "  </identifier>"
        + "</metaMetadata>";

    // When
    MetaMetadata metaMetadata = xmlMapper.readValue(xml, MetaMetadata.class);

    // Then
    assertNotNull(metaMetadata);
    assertNotNull(metaMetadata.getIdentifier());
    assertEquals(1, metaMetadata.getIdentifier().size());

    Identifier identifier = metaMetadata.getIdentifier().get(0);
    assertEquals("URI", identifier.getCatalog());
    assertEquals("http://example.com/metadata/123", identifier.getEntry());
  }

  @Test
  void testDeserializeMetaMetadataWithCatalogEntries() throws Exception {
    // Given
    String xml = "<metaMetadata xmlns=\"http://ltsc.ieee.org/xsd/LOM\">"
        + "  <catalogentry>"
        + "    <catalog>Catalog1</catalog>"
        + "    <entry>"
        + "      <string language=\"en\">Entry1</string>"
        + "    </entry>"
        + "  </catalogentry>"
        + "</metaMetadata>";

    // When
    MetaMetadata metaMetadata = xmlMapper.readValue(xml, MetaMetadata.class);

    // Then
    assertNotNull(metaMetadata);
    assertNotNull(metaMetadata.getCatalogEntries());
    assertEquals(1, metaMetadata.getCatalogEntries().size());

    CatalogEntry entry = metaMetadata.getCatalogEntries().get(0);
    assertEquals("Catalog1", entry.getCatalog());
    assertNotNull(entry.getEntry());
    assertNotNull(entry.getEntry().getLangString());
    assertEquals("en", entry.getEntry().getLangString().getLanguage());
    assertEquals("Entry1", entry.getEntry().getLangString().getValue());
  }

  @Test
  void testDeserializeMetaMetadataWithContribute() throws Exception {
    // Given
    String xml = "<metaMetadata xmlns=\"http://ltsc.ieee.org/xsd/LOM\">"
        + "  <contribute>"
        + "    <role>"
        + "      <source>LOMv1.0</source>"
        + "      <value>creator</value>"
        + "    </role>"
        + "    <entity>John Doe</entity>"
        + "    <date>"
        + "      <dateTime>2023-01-15</dateTime>"
        + "    </date>"
        + "  </contribute>"
        + "</metaMetadata>";

    // When
    MetaMetadata metaMetadata = xmlMapper.readValue(xml, MetaMetadata.class);

    // Then
    assertNotNull(metaMetadata);
    assertNotNull(metaMetadata.getContribute());
    assertEquals(1, metaMetadata.getContribute().size());

    ContributeMeta contribute = metaMetadata.getContribute().get(0);
    assertNotNull(contribute.getRole());
    assertEquals("LOMv1.0", contribute.getRole().getSource());
    assertEquals(RoleMeta.CREATOR, contribute.getRole().getValue());

    assertNotNull(contribute.getEntities());
    assertEquals(1, contribute.getEntities().size());
    assertEquals("John Doe", contribute.getEntities().get(0));

    assertNotNull(contribute.getDate());
    assertEquals("2023-01-15", contribute.getDate().getDateTime());
  }

  @Test
  void testDeserializeMetaMetadataWithMetadataSchema() throws Exception {
    // Given
    String xml = "<metaMetadata xmlns=\"http://ltsc.ieee.org/xsd/LOM\">"
        + "  <metadataSchema>LOMv1.0</metadataSchema>"
        + "  <metadataSchema>SCORM 1.2</metadataSchema>"
        + "</metaMetadata>";

    // When
    MetaMetadata metaMetadata = xmlMapper.readValue(xml, MetaMetadata.class);

    // Then
    assertNotNull(metaMetadata);
    assertNotNull(metaMetadata.getMetadataSchema());
    assertEquals(2, metaMetadata.getMetadataSchema().size());
    assertEquals("LOMv1.0", metaMetadata.getMetadataSchema().get(0));
    assertEquals("SCORM 1.2", metaMetadata.getMetadataSchema().get(1));
  }

  @Test
  void testDeserializeMetaMetadataWithLanguage() throws Exception {
    // Given
    String xml = "<metaMetadata xmlns=\"http://ltsc.ieee.org/xsd/LOM\">"
        + "  <language>en</language>"
        + "</metaMetadata>";

    // When
    MetaMetadata metaMetadata = xmlMapper.readValue(xml, MetaMetadata.class);

    // Then
    assertNotNull(metaMetadata);
    assertNotNull(metaMetadata.getLanguage());
    assertEquals("en", metaMetadata.getLanguage());
  }

  @Test
  void testDeserializeMetaMetadataWithMultipleFields() throws Exception {
    // Given
    String xml = "<metaMetadata xmlns=\"http://ltsc.ieee.org/xsd/LOM\">"
        + "  <identifier>"
        + "    <catalog>URI</catalog>"
        + "    <entry>http://example.com/metadata/123</entry>"
        + "  </identifier>"
        + "  <contribute>"
        + "    <role>"
        + "      <source>LOMv1.0</source>"
        + "      <value>creator</value>"
        + "    </role>"
        + "    <entity>John Doe</entity>"
        + "    <date>"
        + "      <dateTime>2023-01-15</dateTime>"
        + "    </date>"
        + "  </contribute>"
        + "  <metadataSchema>LOMv1.0</metadataSchema>"
        + "  <language>en</language>"
        + "</metaMetadata>";

    // When
    MetaMetadata metaMetadata = xmlMapper.readValue(xml, MetaMetadata.class);

    // Then
    assertNotNull(metaMetadata);

    // Check identifier
    assertNotNull(metaMetadata.getIdentifier());
    assertEquals(1, metaMetadata.getIdentifier().size());
    Identifier identifier = metaMetadata.getIdentifier().get(0);
    assertEquals("URI", identifier.getCatalog());
    assertEquals("http://example.com/metadata/123", identifier.getEntry());

    // Check contribute
    assertNotNull(metaMetadata.getContribute());
    assertEquals(1, metaMetadata.getContribute().size());
    ContributeMeta contribute = metaMetadata.getContribute().get(0);
    assertNotNull(contribute.getRole());
    assertEquals("LOMv1.0", contribute.getRole().getSource());
    assertEquals(RoleMeta.CREATOR, contribute.getRole().getValue());
    assertNotNull(contribute.getEntities());
    assertEquals(1, contribute.getEntities().size());
    assertEquals("John Doe", contribute.getEntities().get(0));
    assertNotNull(contribute.getDate());
    assertEquals("2023-01-15", contribute.getDate().getDateTime());

    // Check metadata schema
    assertNotNull(metaMetadata.getMetadataSchema());
    assertEquals(1, metaMetadata.getMetadataSchema().size());
    assertEquals("LOMv1.0", metaMetadata.getMetadataSchema().get(0));

    // Check language
    assertNotNull(metaMetadata.getLanguage());
    assertEquals("en", metaMetadata.getLanguage());
  }
}