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
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import dev.jcputney.elearning.parser.input.lom.types.Kind;
import dev.jcputney.elearning.parser.input.lom.types.Resource;
import java.io.File;
import org.junit.jupiter.api.Test;

/**
 * Tests for the {@link Relation} class.
 */
class RelationTest {

  private final XmlMapper xmlMapper = new XmlMapper();

  /**
   * Tests the deserialization of a Relation object from XML.
   */
  @Test
  void testDeserializeRelation() throws Exception {
    // Given
    File file = new File(
        "src/test/resources/modules/scorm2004/ContentPackagingMetadata_SCORM20043rdEdition/metadata_course.xml");

    // When
    LOM lom = xmlMapper.readValue(file, LOM.class);

    // Then
    assertNotNull(lom);
    assertNotNull(lom.getRelations());
    assertFalse(lom
        .getRelations()
        .isEmpty());

    Relation relation = lom
        .getRelations()
        .get(0);

    // Test kind
    assertNotNull(relation.getKind());
    assertEquals("LOMv1.0", relation
        .getKind()
        .getSource());
    assertEquals(Kind.IS_BASED_ON, relation
        .getKind()
        .getValue());

    // Test resource
    assertNotNull(relation.getResource());
    assertFalse(relation
        .getResource()
        .isEmpty());

    Resource resource = relation
        .getResource()
        .get(0);

    // Test resource identifier
    assertNotNull(resource.getIdentifiers());
    assertFalse(resource
        .getIdentifiers()
        .isEmpty());
    assertEquals("URI", resource
        .getIdentifiers()
        .get(0)
        .getCatalog());
    assertEquals("com.scorm.golfsamples.contentpackaging.singlesco.20043rd",
        resource
            .getIdentifiers()
            .get(0)
            .getEntry());

    // Test resource description
    assertNotNull(resource.getDescriptions());
    assertNotNull(resource
        .getDescriptions()
        .getLangStrings());
    assertFalse(resource
        .getDescriptions()
        .getLangStrings()
        .isEmpty());
    assertEquals("en-us", resource
        .getDescriptions()
        .getLangStrings()
        .get(0)
        .getLanguage());
    assertTrue(resource
        .getDescriptions()
        .getLangStrings()
        .get(0)
        .getValue()
        .contains("This course was derived from the Single SCO golf example"));
  }
}
