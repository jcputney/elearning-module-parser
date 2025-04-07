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
import dev.jcputney.elearning.parser.input.lom.types.Date;
import dev.jcputney.elearning.parser.input.lom.types.LangString;
import dev.jcputney.elearning.parser.input.lom.types.SingleLangString;
import dev.jcputney.elearning.parser.input.lom.types.UnboundLangString;
import org.junit.jupiter.api.Test;

/**
 * Tests for the {@link Annotation} class.
 */
class AnnotationTest {

  private final XmlMapper xmlMapper = new XmlMapper();

  @Test
  void testDeserializeEmptyAnnotation() throws Exception {
    // Given
    String xml = "<annotation xmlns=\"http://ltsc.ieee.org/xsd/LOM\"></annotation>";

    // When
    Annotation annotation = xmlMapper.readValue(xml, Annotation.class);

    // Then
    assertNotNull(annotation);
    assertNull(annotation.getEntity());
    assertNull(annotation.getDate());
    assertNull(annotation.getDescription());
  }

  @Test
  void testDeserializeAnnotationWithEntity() throws Exception {
    // Given
    String xml = "<annotation xmlns=\"http://ltsc.ieee.org/xsd/LOM\">"
        + "  <entity>BEGIN:VCARD\nFN:John Doe\nEMAIL:john.doe@example.com\nEND:VCARD</entity>"
        + "</annotation>";

    // When
    Annotation annotation = xmlMapper.readValue(xml, Annotation.class);

    // Then
    assertNotNull(annotation);
    assertNotNull(annotation.getEntity());
    assertEquals("BEGIN:VCARD\nFN:John Doe\nEMAIL:john.doe@example.com\nEND:VCARD", annotation.getEntity());
    assertNull(annotation.getDate());
    assertNull(annotation.getDescription());
  }

  @Test
  void testDeserializeAnnotationWithDate() throws Exception {
    // Given
    String xml = "<annotation xmlns=\"http://ltsc.ieee.org/xsd/LOM\">"
        + "  <date>"
        + "    <dateTime>2023-04-01T12:00:00</dateTime>"
        + "    <description>"
        + "      <string language=\"en\">Creation date</string>"
        + "    </description>"
        + "  </date>"
        + "</annotation>";

    // When
    Annotation annotation = xmlMapper.readValue(xml, Annotation.class);

    // Then
    assertNotNull(annotation);
    assertNull(annotation.getEntity());
    assertNotNull(annotation.getDate());
    assertEquals("2023-04-01T12:00:00", annotation.getDate().getDateTime());
    assertNotNull(annotation.getDate().getDescription());
    assertNotNull(annotation.getDate().getDescription().getLangStrings());
    assertEquals(1, annotation.getDate().getDescription().getLangStrings().size());
    assertEquals("en", annotation.getDate().getDescription().getLangStrings().get(0).getLanguage());
    assertEquals("Creation date", annotation.getDate().getDescription().getLangStrings().get(0).getValue());
    assertNull(annotation.getDescription());
  }

  @Test
  void testDeserializeAnnotationWithDescription() throws Exception {
    // Given
    String xml = "<annotation xmlns=\"http://ltsc.ieee.org/xsd/LOM\">"
        + "  <description>"
        + "    <string language=\"en\">This is an annotation description</string>"
        + "  </description>"
        + "</annotation>";

    // When
    Annotation annotation = xmlMapper.readValue(xml, Annotation.class);

    // Then
    assertNotNull(annotation);
    assertNull(annotation.getEntity());
    assertNull(annotation.getDate());
    assertNotNull(annotation.getDescription());
    assertNotNull(annotation.getDescription().getLangString());
    assertEquals("en", annotation.getDescription().getLangString().getLanguage());
    assertEquals("This is an annotation description", annotation.getDescription().getLangString().getValue());
  }

  @Test
  void testDeserializeCompleteAnnotation() throws Exception {
    // Given
    String xml = "<annotation xmlns=\"http://ltsc.ieee.org/xsd/LOM\">"
        + "  <entity>BEGIN:VCARD\nFN:John Doe\nEMAIL:john.doe@example.com\nEND:VCARD</entity>"
        + "  <date>"
        + "    <dateTime>2023-04-01T12:00:00</dateTime>"
        + "    <description>"
        + "      <string language=\"en\">Creation date</string>"
        + "    </description>"
        + "  </date>"
        + "  <description>"
        + "    <string language=\"en\">This is an annotation description</string>"
        + "  </description>"
        + "</annotation>";

    // When
    Annotation annotation = xmlMapper.readValue(xml, Annotation.class);

    // Then
    assertNotNull(annotation);
    
    // Check entity
    assertNotNull(annotation.getEntity());
    assertEquals("BEGIN:VCARD\nFN:John Doe\nEMAIL:john.doe@example.com\nEND:VCARD", annotation.getEntity());
    
    // Check date
    assertNotNull(annotation.getDate());
    assertEquals("2023-04-01T12:00:00", annotation.getDate().getDateTime());
    assertNotNull(annotation.getDate().getDescription());
    assertNotNull(annotation.getDate().getDescription().getLangStrings());
    assertEquals(1, annotation.getDate().getDescription().getLangStrings().size());
    assertEquals("en", annotation.getDate().getDescription().getLangStrings().get(0).getLanguage());
    assertEquals("Creation date", annotation.getDate().getDescription().getLangStrings().get(0).getValue());
    
    // Check description
    assertNotNull(annotation.getDescription());
    assertNotNull(annotation.getDescription().getLangString());
    assertEquals("en", annotation.getDescription().getLangString().getLanguage());
    assertEquals("This is an annotation description", annotation.getDescription().getLangString().getValue());
  }
}