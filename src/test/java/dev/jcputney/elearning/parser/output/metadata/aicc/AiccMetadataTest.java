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
package dev.jcputney.elearning.parser.output.metadata.aicc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.jcputney.elearning.parser.input.aicc.AiccManifest;
import dev.jcputney.elearning.parser.input.aicc.AssignableUnit;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Test;

/**
 * Test class for AiccMetadata.
 */
class AiccMetadataTest {

  @Test
  void testHasMultipleLaunchableUnits_NullManifest_ReturnsFalse() {
    // Arrange
    AiccMetadata metadata = new AiccMetadata() {
      {
        manifest = null;
      }
    };

    // Act
    boolean result = metadata.hasMultipleLaunchableUnits();

    // Assert
    assertFalse(result);
  }

  @Test
  void testHasMultipleLaunchableUnits_NullAssignableUnits_ReturnsFalse() {
    // Arrange
    AiccManifest mockManifest = mock(AiccManifest.class);
    when(mockManifest.getAssignableUnits()).thenReturn(null);

    AiccMetadata metadata = AiccMetadata.create(mockManifest, false, "test.crs");

    // Act
    boolean result = metadata.hasMultipleLaunchableUnits();

    // Assert
    assertFalse(result);
  }

  @Test
  void testHasMultipleLaunchableUnits_EmptyAssignableUnits_ReturnsFalse() {
    // Arrange
    AiccManifest mockManifest = mock(AiccManifest.class);
    when(mockManifest.getAssignableUnits()).thenReturn(Collections.emptyList());

    AiccMetadata metadata = AiccMetadata.create(mockManifest, false, "test.crs");

    // Act
    boolean result = metadata.hasMultipleLaunchableUnits();

    // Assert
    assertFalse(result);
  }

  @Test
  void testHasMultipleLaunchableUnits_SingleAssignableUnit_ReturnsFalse() {
    // Arrange
    AiccManifest mockManifest = mock(AiccManifest.class);
    AssignableUnit mockAU = mock(AssignableUnit.class);

    when(mockManifest.getAssignableUnits()).thenReturn(List.of(mockAU));

    AiccMetadata metadata = AiccMetadata.create(mockManifest, false, "test.crs");

    // Act
    boolean result = metadata.hasMultipleLaunchableUnits();

    // Assert
    assertFalse(result);
  }

  @Test
  void testHasMultipleLaunchableUnits_MultipleAssignableUnits_ReturnsTrue() {
    // Arrange
    AiccManifest mockManifest = mock(AiccManifest.class);
    AssignableUnit mockAU1 = mock(AssignableUnit.class);
    AssignableUnit mockAU2 = mock(AssignableUnit.class);

    when(mockManifest.getAssignableUnits()).thenReturn(List.of(mockAU1, mockAU2));

    AiccMetadata metadata = AiccMetadata.create(mockManifest, false, "test.crs");

    // Act
    boolean result = metadata.hasMultipleLaunchableUnits();

    // Assert
    assertTrue(result);
  }

  @Test
  void testHasMultipleLaunchableUnits_ThreeAssignableUnits_ReturnsTrue() {
    // Arrange
    AiccManifest mockManifest = mock(AiccManifest.class);
    AssignableUnit mockAU1 = mock(AssignableUnit.class);
    AssignableUnit mockAU2 = mock(AssignableUnit.class);
    AssignableUnit mockAU3 = mock(AssignableUnit.class);

    when(mockManifest.getAssignableUnits()).thenReturn(List.of(mockAU1, mockAU2, mockAU3));

    AiccMetadata metadata = AiccMetadata.create(mockManifest, false, "test.crs");

    // Act
    boolean result = metadata.hasMultipleLaunchableUnits();

    // Assert
    assertTrue(result);
  }

  @Test
  void testGetManifestFile_ReturnsCorrectFilename() {
    // Arrange
    AiccManifest mockManifest = mock(AiccManifest.class);
    when(mockManifest.getAssignableUnits()).thenReturn(Collections.emptyList());

    String expectedFilename = "course.crs";
    AiccMetadata metadata = AiccMetadata.create(mockManifest, false, expectedFilename);

    // Act
    String actualFilename = metadata.getManifestFile();

    // Assert
    assertEquals(expectedFilename, actualFilename);
  }

  @Test
  void testGetManifestFile_JsonRoundTrip_PreservesValue() throws Exception {
    // Arrange
    AiccManifest mockManifest = mock(AiccManifest.class);
    when(mockManifest.getAssignableUnits()).thenReturn(Collections.emptyList());

    String expectedFilename = "my_course.crs";
    AiccMetadata originalMetadata = AiccMetadata.create(mockManifest, false, expectedFilename);

    ObjectMapper mapper = new ObjectMapper();

    // Act - Serialize to JSON and deserialize back
    String json = mapper.writeValueAsString(originalMetadata);
    AiccMetadata deserializedMetadata = mapper.readValue(json, AiccMetadata.class);

    // Assert
    assertNotNull(deserializedMetadata.getManifestFile(),
        "manifestFile should not be null after JSON deserialization");
    assertEquals(expectedFilename, deserializedMetadata.getManifestFile(),
        "manifestFile should be preserved through JSON round-trip");
  }
}
