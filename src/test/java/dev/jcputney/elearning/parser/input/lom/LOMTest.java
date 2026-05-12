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
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import java.io.File;
import org.junit.jupiter.api.Test;

/**
 * Tests for the {@link LOM} class.
 */
class LOMTest {

  private final XmlMapper xmlMapper = new XmlMapper();

  @Test
  void testDeserializeEmptyLOM() throws Exception {
    // Given
    String xml = "<lom xmlns=\"http://ltsc.ieee.org/xsd/LOM\"></lom>";

    // When
    LOM lom = xmlMapper.readValue(xml, LOM.class);

    // Then
    assertNotNull(lom);
    assertNull(lom.getGeneral());
    assertNull(lom.getLifecycle());
    assertNull(lom.getMetaMetadata());
    assertNull(lom.getTechnical());
    assertNull(lom.getEducational());
    assertNull(lom.getRights());
  }

  @Test
  void testDeserializeLOMWithGeneral() throws Exception {
    // Given
    String xml = "<lom xmlns=\"http://ltsc.ieee.org/xsd/LOM\">"
        + "  <general>"
        + "    <title>"
        + "      <string language=\"en\">Test Title</string>"
        + "    </title>"
        + "    <language>en</language>"
        + "    <description>"
        + "      <string language=\"en\">Test Description</string>"
        + "    </description>"
        + "  </general>"
        + "</lom>";

    // When
    LOM lom = xmlMapper.readValue(xml, LOM.class);

    // Then
    assertNotNull(lom);
    assertNotNull(lom.getGeneral());
    assertNotNull(lom.getGeneral().getTitle());
    assertEquals("en", lom.getGeneral().getLanguage());
    assertNotNull(lom.getGeneral().getDescription());
  }

  @Test
  void testGetTitle() throws Exception {
    // Given
    String xml = "<lom xmlns=\"http://ltsc.ieee.org/xsd/LOM\">"
        + "  <general>"
        + "    <title>"
        + "      <string language=\"en\">Test Title</string>"
        + "    </title>"
        + "  </general>"
        + "</lom>";

    // When
    LOM lom = xmlMapper.readValue(xml, LOM.class);
    String title = lom.getTitle();

    // Then
    assertEquals("Test Title", title);
  }

  @Test
  void testGetTitleWithNoGeneral() throws Exception {
    // Given
    String xml = "<lom xmlns=\"http://ltsc.ieee.org/xsd/LOM\"></lom>";

    // When
    LOM lom = xmlMapper.readValue(xml, LOM.class);
    String title = lom.getTitle();

    // Then
    assertNull(title);
  }

  @Test
  void testGetTitleWithNoTitle() throws Exception {
    // Given
    String xml = "<lom xmlns=\"http://ltsc.ieee.org/xsd/LOM\">"
        + "  <general>"
        + "  </general>"
        + "</lom>";

    // When
    LOM lom = xmlMapper.readValue(xml, LOM.class);
    String title = lom.getTitle();

    // Then
    assertNull(title);
  }

  @Test
  void testGetTitleWithEmptyTitle() throws Exception {
    // Given
    String xml = "<lom xmlns=\"http://ltsc.ieee.org/xsd/LOM\">"
        + "  <general>"
        + "    <title>"
        + "    </title>"
        + "  </general>"
        + "</lom>";

    // When
    LOM lom = xmlMapper.readValue(xml, LOM.class);
    String title = lom.getTitle();

    // Then
    assertNull(title);
  }

  @Test
  void testGetDescription() throws Exception {
    // Given
    String xml = "<lom xmlns=\"http://ltsc.ieee.org/xsd/LOM\">"
        + "  <general>"
        + "    <description>"
        + "      <string language=\"en\">Test Description</string>"
        + "    </description>"
        + "  </general>"
        + "</lom>";

    // When
    LOM lom = xmlMapper.readValue(xml, LOM.class);
    String description = lom.getDescription();

    // Then
    assertEquals("Test Description", description);
  }

  @Test
  void testGetDescriptionWithNoGeneral() throws Exception {
    // Given
    String xml = "<lom xmlns=\"http://ltsc.ieee.org/xsd/LOM\"></lom>";

    // When
    LOM lom = xmlMapper.readValue(xml, LOM.class);
    String description = lom.getDescription();

    // Then
    assertNull(description);
  }

  @Test
  void testGetDescriptionWithNoDescription() throws Exception {
    // Given
    String xml = "<lom xmlns=\"http://ltsc.ieee.org/xsd/LOM\">"
        + "  <general>"
        + "  </general>"
        + "</lom>";

    // When
    LOM lom = xmlMapper.readValue(xml, LOM.class);
    String description = lom.getDescription();

    // Then
    assertNull(description);
  }

  @Test
  void testGetDescriptionWithEmptyDescription() throws Exception {
    // Given
    String xml = "<lom xmlns=\"http://ltsc.ieee.org/xsd/LOM\">"
        + "  <general>"
        + "    <description>"
        + "    </description>"
        + "  </general>"
        + "</lom>";

    // When
    LOM lom = xmlMapper.readValue(xml, LOM.class);
    String description = lom.getDescription();

    // Then
    assertNull(description);
  }

  @Test
  void testDeserializeFromFile() throws Exception {
    // Given
    File file = new File(
        "src/test/resources/modules/scorm12/ContentPackagingWithMetadata_SCORM12/metadata.xml");

    // When
    LOM lom = xmlMapper.readValue(file, LOM.class);

    // Then
    assertNotNull(lom);
    assertNotNull(lom.getGeneral());
    assertNotNull(lom.getLifecycle());
    assertNotNull(lom.getMetaMetadata());
    assertNotNull(lom.getTechnical());
    assertNotNull(lom.getRights());
  }
}
