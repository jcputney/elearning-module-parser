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

import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import dev.jcputney.elearning.parser.input.common.LangStringDeserializer;
import dev.jcputney.elearning.parser.input.lom.types.CopyrightAndOtherRestrictions;
import dev.jcputney.elearning.parser.input.lom.types.Cost;
import dev.jcputney.elearning.parser.input.lom.types.LangString;
import java.io.File;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests for the {@link Rights} class.
 */
class RightsTest {

  private XmlMapper xmlMapper;

  @BeforeEach
  void setUp() {
    xmlMapper = new XmlMapper();
    SimpleModule module = new SimpleModule();
    module.addDeserializer(LangString.class, new LangStringDeserializer());
    xmlMapper.registerModule(module);
  }

  /**
   * Tests the deserialization of a Rights object from XML.
   */
  @Test
  void testDeserializeRights() throws Exception {
    // Given
    File file = new File(
        "src/test/resources/modules/scorm2004/ContentPackagingMetadata_SCORM20043rdEdition/metadata_course.xml");

    // When
    LOM lom = xmlMapper.readValue(file, LOM.class);

    // Then
    assertNotNull(lom);
    assertNotNull(lom.getRights());

    Rights rights = lom.getRights();

    // Test cost
    assertNotNull(rights.getCost());
    assertEquals("LOMv1.0", rights
        .getCost()
        .getSource());
    assertEquals(Cost.NO, rights
        .getCost()
        .getValue());

    // Test copyrightAndOtherRestrictions
    assertNotNull(rights.getCopyrightAndOtherRestrictions());
    assertEquals("LOMv1.0", rights
        .getCopyrightAndOtherRestrictions()
        .getSource());
    assertEquals(CopyrightAndOtherRestrictions.YES, rights
        .getCopyrightAndOtherRestrictions()
        .getValue());

    // Test description
    assertNotNull(rights.getDescriptions());
    assertNotNull(rights
        .getDescriptions()
        .getLangStrings());
    assertFalse(rights
        .getDescriptions()
        .getLangStrings()
        .isEmpty());
    assertTrue(rights
        .getDescriptions()
        .getLangStrings()
        .get(0)
        .getValue()
        .contains("This content may be freely distributed subject"));
  }
}
