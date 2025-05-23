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

package dev.jcputney.elearning.parser.input.lom;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import dev.jcputney.elearning.parser.input.lom.types.Identifier;
import dev.jcputney.elearning.parser.input.lom.types.Kind;
import dev.jcputney.elearning.parser.input.lom.types.Resource;
import dev.jcputney.elearning.parser.input.lom.types.SourceValuePair;
import dev.jcputney.elearning.parser.input.lom.types.UnboundLangString;
import java.util.List;
import org.junit.jupiter.api.Test;

/**
 * Tests for the {@link Relation} class.
 */
class RelationTest {

  private final XmlMapper xmlMapper = new XmlMapper();

  @Test
  void testDeserializeEmptyRelation() throws Exception {
    // Given
    String xml = "<relation xmlns=\"http://ltsc.ieee.org/xsd/LOM\"></relation>";

    // When
    Relation relation = xmlMapper.readValue(xml, Relation.class);

    // Then
    assertNotNull(relation);
    assertNull(relation.getKind());
    assertNull(relation.getResource());
  }

  @Test
  void testDeserializeRelationWithKind() throws Exception {
    // Given
    String xml = "<relation xmlns=\"http://ltsc.ieee.org/xsd/LOM\">"
        + "  <kind>"
        + "    <source>LOMv1.0</source>"
        + "    <value>ispartof</value>"
        + "  </kind>"
        + "</relation>";

    // When
    Relation relation = xmlMapper.readValue(xml, Relation.class);

    // Then
    assertNotNull(relation);
    assertNotNull(relation.getKind());
    assertEquals("LOMv1.0", relation.getKind().getSource());
    assertEquals(Kind.IS_PART_OF, relation.getKind().getValue());
    assertNull(relation.getResource());
  }

  @Test
  void testDeserializeRelationWithResource() throws Exception {
    // Given
    String xml = "<relation xmlns=\"http://ltsc.ieee.org/xsd/LOM\">"
        + "  <resource>"
        + "    <identifier>"
        + "      <catalog>URI</catalog>"
        + "      <entry>http://example.com/resource</entry>"
        + "    </identifier>"
        + "    <description>"
        + "      <string language=\"en\">Related resource description</string>"
        + "    </description>"
        + "  </resource>"
        + "</relation>";

    // When
    Relation relation = xmlMapper.readValue(xml, Relation.class);

    // Then
    assertNotNull(relation);
    assertNull(relation.getKind());
    assertNotNull(relation.getResource());
    assertEquals(1, relation.getResource().size());
    
    Resource resource = relation.getResource().get(0);
    assertNotNull(resource.getIdentifiers());
    assertEquals(1, resource.getIdentifiers().size());
    assertEquals("URI", resource.getIdentifiers().get(0).getCatalog());
    assertEquals("http://example.com/resource", resource.getIdentifiers().get(0).getEntry());
    
    assertNotNull(resource.getDescriptions());
    assertNotNull(resource.getDescriptions().getLangStrings());
    assertEquals(1, resource.getDescriptions().getLangStrings().size());
    assertEquals("en", resource.getDescriptions().getLangStrings().get(0).getLanguage());
    assertEquals("Related resource description", resource.getDescriptions().getLangStrings().get(0).getValue());
  }

  @Test
  void testDeserializeCompleteRelation() throws Exception {
    // Given
    String xml = "<relation xmlns=\"http://ltsc.ieee.org/xsd/LOM\">"
        + "  <kind>"
        + "    <source>LOMv1.0</source>"
        + "    <value>requires</value>"
        + "  </kind>"
        + "  <resource>"
        + "    <identifier>"
        + "      <catalog>ISBN</catalog>"
        + "      <entry>1234567890</entry>"
        + "    </identifier>"
        + "    <description>"
        + "      <string language=\"en\">Required textbook</string>"
        + "      <string language=\"fr\">Manuel requis</string>"
        + "    </description>"
        + "  </resource>"
        + "</relation>";

    // When
    Relation relation = xmlMapper.readValue(xml, Relation.class);

    // Then
    assertNotNull(relation);
    
    // Check kind
    assertNotNull(relation.getKind());
    assertEquals("LOMv1.0", relation.getKind().getSource());
    assertEquals(Kind.REQUIRES, relation.getKind().getValue());
    
    // Check resource
    assertNotNull(relation.getResource());
    assertEquals(1, relation.getResource().size());
    
    Resource resource = relation.getResource().get(0);
    assertNotNull(resource.getIdentifiers());
    assertEquals(1, resource.getIdentifiers().size());
    assertEquals("ISBN", resource.getIdentifiers().get(0).getCatalog());
    assertEquals("1234567890", resource.getIdentifiers().get(0).getEntry());
    
    assertNotNull(resource.getDescriptions());
    assertNotNull(resource.getDescriptions().getLangStrings());
    assertEquals(2, resource.getDescriptions().getLangStrings().size());
    assertEquals("en", resource.getDescriptions().getLangStrings().get(0).getLanguage());
    assertEquals("Required textbook", resource.getDescriptions().getLangStrings().get(0).getValue());
    assertEquals("fr", resource.getDescriptions().getLangStrings().get(1).getLanguage());
    assertEquals("Manuel requis", resource.getDescriptions().getLangStrings().get(1).getValue());
  }

  @Test
  void testDeserializeRelationWithMultipleResources() throws Exception {
    // Given
    String xml = "<relation xmlns=\"http://ltsc.ieee.org/xsd/LOM\">"
        + "  <kind>"
        + "    <source>LOMv1.0</source>"
        + "    <value>references</value>"
        + "  </kind>"
        + "  <resource>"
        + "    <identifier>"
        + "      <catalog>URI</catalog>"
        + "      <entry>http://example.com/resource1</entry>"
        + "    </identifier>"
        + "    <description>"
        + "      <string language=\"en\">First reference</string>"
        + "    </description>"
        + "  </resource>"
        + "  <resource>"
        + "    <identifier>"
        + "      <catalog>URI</catalog>"
        + "      <entry>http://example.com/resource2</entry>"
        + "    </identifier>"
        + "    <description>"
        + "      <string language=\"en\">Second reference</string>"
        + "    </description>"
        + "  </resource>"
        + "</relation>";

    // When
    Relation relation = xmlMapper.readValue(xml, Relation.class);

    // Then
    assertNotNull(relation);
    
    // Check kind
    assertNotNull(relation.getKind());
    assertEquals("LOMv1.0", relation.getKind().getSource());
    assertEquals(Kind.REFERENCES, relation.getKind().getValue());
    
    // Check resources
    assertNotNull(relation.getResource());
    assertEquals(2, relation.getResource().size());
    
    // Check first resource
    Resource resource1 = relation.getResource().get(0);
    assertNotNull(resource1.getIdentifiers());
    assertEquals(1, resource1.getIdentifiers().size());
    assertEquals("URI", resource1.getIdentifiers().get(0).getCatalog());
    assertEquals("http://example.com/resource1", resource1.getIdentifiers().get(0).getEntry());
    
    assertNotNull(resource1.getDescriptions());
    assertNotNull(resource1.getDescriptions().getLangStrings());
    assertEquals(1, resource1.getDescriptions().getLangStrings().size());
    assertEquals("en", resource1.getDescriptions().getLangStrings().get(0).getLanguage());
    assertEquals("First reference", resource1.getDescriptions().getLangStrings().get(0).getValue());
    
    // Check second resource
    Resource resource2 = relation.getResource().get(1);
    assertNotNull(resource2.getIdentifiers());
    assertEquals(1, resource2.getIdentifiers().size());
    assertEquals("URI", resource2.getIdentifiers().get(0).getCatalog());
    assertEquals("http://example.com/resource2", resource2.getIdentifiers().get(0).getEntry());
    
    assertNotNull(resource2.getDescriptions());
    assertNotNull(resource2.getDescriptions().getLangStrings());
    assertEquals(1, resource2.getDescriptions().getLangStrings().size());
    assertEquals("en", resource2.getDescriptions().getLangStrings().get(0).getLanguage());
    assertEquals("Second reference", resource2.getDescriptions().getLangStrings().get(0).getValue());
  }
}