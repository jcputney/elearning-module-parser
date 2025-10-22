/*
 * Copyright (c) 2024-2025. Jonathan Putney
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

package dev.jcputney.elearning.parser.output.metadata.aicc;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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
}
