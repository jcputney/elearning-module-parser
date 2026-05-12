/*
 * Copyright (c) 2024-2026 Jonathan Putney
 *
 * Licensed under the Apache License, Version 2.0 (the "License").
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at the project root LICENSE file
 * or at http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0
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
