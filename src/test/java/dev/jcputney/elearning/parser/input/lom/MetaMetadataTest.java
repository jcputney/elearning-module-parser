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

import static dev.jcputney.elearning.parser.input.lom.types.RoleMeta.CREATOR;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import dev.jcputney.elearning.parser.input.lom.types.ContributeMeta;
import dev.jcputney.elearning.parser.input.lom.types.Identifier;
import java.io.File;
import org.junit.jupiter.api.Test;

/**
 * Tests for the {@link MetaMetadata} class.
 */
class MetaMetadataTest {

  private final XmlMapper xmlMapper = new XmlMapper();

  /**
   * Tests the deserialization of a MetaMetadata object from XML.
   */
  @Test
  void testDeserializeMetaMetadata() throws Exception {
    // Given
    File file = new File(
        "src/test/resources/modules/scorm2004/ContentPackagingMetadata_SCORM20043rdEdition/metadata_course.xml");

    // When
    LOM lom = xmlMapper.readValue(file, LOM.class);

    // Then
    assertNotNull(lom);
    assertNotNull(lom.getMetaMetadata());

    MetaMetadata metaMetadata = lom.getMetaMetadata();

    // Test identifiers
    assertNotNull(metaMetadata.getIdentifier());
    assertFalse(metaMetadata
        .getIdentifier()
        .isEmpty());
    Identifier identifier = metaMetadata
        .getIdentifier()
        .get(0);
    assertEquals("URI", identifier.getCatalog());
    assertEquals("com.scorm.golfsamples.contentpackaging.metadata.20043rd.courselevelmetadata",
        identifier.getEntry());

    // Test contribute
    assertNotNull(metaMetadata.getContribute());
    assertFalse(metaMetadata
        .getContribute()
        .isEmpty());

    // Test first contribution (creator)
    ContributeMeta creatorContribution = null;

    for (ContributeMeta contribute : metaMetadata.getContribute()) {
      if (contribute.getRole() != null && "creator".equalsIgnoreCase(contribute
          .getRole()
          .getValue()
          .toString())) {
        creatorContribution = contribute;
        break;
      }
    }

    assertNotNull(creatorContribution, "Creator contribution should be present");
    assertNotNull(creatorContribution.getRole());
    assertEquals("LOMv1.0", creatorContribution
        .getRole()
        .getSource());
    assertEquals(CREATOR, creatorContribution
        .getRole()
        .getValue());

    // Test entity (vCard)
    assertNotNull(creatorContribution.getEntities());
    assertFalse(creatorContribution
        .getEntities()
        .isEmpty());
    String entity = creatorContribution
        .getEntities()
        .get(0);
    assertTrue(entity.contains("BEGIN:VCARD"));
    assertTrue(entity.contains("FN:Mike Rustici"));
    assertTrue(entity.contains("ORG:Rustici Software"));
    assertTrue(entity.contains("END:VCARD"));

    // Test date
    assertNotNull(creatorContribution.getDate());
    assertNotNull(creatorContribution
        .getDate()
        .getDateTime());
    assertEquals("2009-01-23", creatorContribution
        .getDate()
        .getDateTime());
    assertNotNull(creatorContribution
        .getDate()
        .getDescription());
    assertNotNull(creatorContribution
        .getDate()
        .getDescription()
        .getLangStrings());
    assertFalse(creatorContribution
        .getDate()
        .getDescription()
        .getLangStrings()
        .isEmpty());
    assertTrue(creatorContribution
        .getDate()
        .getDescription()
        .getLangStrings()
        .get(0)
        .getValue()
        .contains("This is the date this sample metadata was first created"));

    // Test metadataSchema
    assertNotNull(metaMetadata.getMetadataSchema());
    assertFalse(metaMetadata
        .getMetadataSchema()
        .isEmpty());
    assertTrue(metaMetadata
        .getMetadataSchema()
        .contains("LOMv1.0"));
    assertTrue(metaMetadata
        .getMetadataSchema()
        .contains("SCORM_CAM_v1.3"));

    // Test language
    assertEquals("en-us", metaMetadata.getLanguage());
  }
}
