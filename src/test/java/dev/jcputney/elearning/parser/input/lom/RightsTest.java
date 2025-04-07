/*
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
 */

package dev.jcputney.elearning.parser.input.lom;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import dev.jcputney.elearning.parser.input.lom.types.CopyrightAndOtherRestrictions;
import dev.jcputney.elearning.parser.input.lom.types.Cost;
import dev.jcputney.elearning.parser.input.lom.types.LangString;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RightsTest {

  private XmlMapper xmlMapper;

  @BeforeEach
  void setUp() {
    xmlMapper = new XmlMapper();
  }

  @Test
  void testDeserializeEmptyRights() throws Exception {
    // Given
    String xml = "<rights xmlns=\"http://ltsc.ieee.org/xsd/LOM\"></rights>";

    // When
    Rights rights = xmlMapper.readValue(xml, Rights.class);

    // Then
    assertNotNull(rights);
    assertNull(rights.getCost());
    assertNull(rights.getCopyrightAndOtherRestrictions());
    assertNull(rights.getDescriptions());
  }

  @Test
  void testDeserializeRightsWithCost() throws Exception {
    // Given
    String xml = "<rights xmlns=\"http://ltsc.ieee.org/xsd/LOM\">"
        + "  <cost>"
        + "    <source>LOMv1.0</source>"
        + "    <value>yes</value>"
        + "  </cost>"
        + "</rights>";

    // When
    Rights rights = xmlMapper.readValue(xml, Rights.class);

    // Then
    assertNotNull(rights);
    assertNotNull(rights.getCost());
    assertEquals("LOMv1.0", rights.getCost().getSource());
    assertEquals(Cost.YES, rights.getCost().getValue());
  }

  @Test
  void testDeserializeRightsWithCopyrightAndOtherRestrictions() throws Exception {
    // Given
    String xml = "<rights xmlns=\"http://ltsc.ieee.org/xsd/LOM\">"
        + "  <copyrightAndOtherRestrictions>"
        + "    <source>LOMv1.0</source>"
        + "    <value>yes</value>"
        + "  </copyrightAndOtherRestrictions>"
        + "</rights>";

    // When
    Rights rights = xmlMapper.readValue(xml, Rights.class);

    // Then
    assertNotNull(rights);
    assertNotNull(rights.getCopyrightAndOtherRestrictions());
    assertEquals("LOMv1.0", rights.getCopyrightAndOtherRestrictions().getSource());
    assertEquals(CopyrightAndOtherRestrictions.YES, rights.getCopyrightAndOtherRestrictions().getValue());
  }

  @Test
  void testDeserializeRightsWithDescriptions() throws Exception {
    // Given
    String xml = "<rights xmlns=\"http://ltsc.ieee.org/xsd/LOM\">"
        + "  <description>"
        + "    <string language=\"en\">This content is copyrighted.</string>"
        + "    <string language=\"fr\">Ce contenu est protégé par des droits d'auteur.</string>"
        + "  </description>"
        + "</rights>";

    // When
    Rights rights = xmlMapper.readValue(xml, Rights.class);

    // Then
    assertNotNull(rights);
    assertNotNull(rights.getDescriptions());
    assertNotNull(rights.getDescriptions().getLangStrings());
    assertEquals(2, rights.getDescriptions().getLangStrings().size());
    
    List<LangString> langStrings = rights.getDescriptions().getLangStrings();
    assertEquals("en", langStrings.get(0).getLanguage());
    assertEquals("This content is copyrighted.", langStrings.get(0).getValue());
    assertEquals("fr", langStrings.get(1).getLanguage());
    assertEquals("Ce contenu est protégé par des droits d'auteur.", langStrings.get(1).getValue());
  }

  @Test
  void testDeserializeCompleteRights() throws Exception {
    // Given
    String xml = "<rights xmlns=\"http://ltsc.ieee.org/xsd/LOM\">"
        + "  <cost>"
        + "    <source>LOMv1.0</source>"
        + "    <value>yes</value>"
        + "  </cost>"
        + "  <copyrightAndOtherRestrictions>"
        + "    <source>LOMv1.0</source>"
        + "    <value>yes</value>"
        + "  </copyrightAndOtherRestrictions>"
        + "  <description>"
        + "    <string language=\"en\">This content is copyrighted.</string>"
        + "  </description>"
        + "</rights>";

    // When
    Rights rights = xmlMapper.readValue(xml, Rights.class);

    // Then
    assertNotNull(rights);
    
    // Check cost
    assertNotNull(rights.getCost());
    assertEquals("LOMv1.0", rights.getCost().getSource());
    assertEquals(Cost.YES, rights.getCost().getValue());
    
    // Check copyright and other restrictions
    assertNotNull(rights.getCopyrightAndOtherRestrictions());
    assertEquals("LOMv1.0", rights.getCopyrightAndOtherRestrictions().getSource());
    assertEquals(CopyrightAndOtherRestrictions.YES, rights.getCopyrightAndOtherRestrictions().getValue());
    
    // Check descriptions
    assertNotNull(rights.getDescriptions());
    assertNotNull(rights.getDescriptions().getLangStrings());
    assertEquals(1, rights.getDescriptions().getLangStrings().size());
    assertEquals("en", rights.getDescriptions().getLangStrings().get(0).getLanguage());
    assertEquals("This content is copyrighted.", rights.getDescriptions().getLangStrings().get(0).getValue());
  }
}