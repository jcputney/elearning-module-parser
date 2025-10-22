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

package dev.jcputney.elearning.parser.output.metadata.scorm12;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import dev.jcputney.elearning.parser.input.scorm12.Scorm12Manifest;
import dev.jcputney.elearning.parser.input.scorm12.ims.cp.Scorm12Resource;
import dev.jcputney.elearning.parser.input.scorm12.ims.cp.Scorm12Resources;
import dev.jcputney.elearning.parser.input.scorm2004.adl.types.ScormType;
import java.util.List;
import org.junit.jupiter.api.Test;

/**
 * Test class for Scorm12Metadata.
 */
class Scorm12MetadataTest {

  @Test
  void testHasMultipleLaunchableUnits_NullManifest_ReturnsFalse() {
    // Arrange
    Scorm12Metadata metadata = new Scorm12Metadata() {
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
  void testHasMultipleLaunchableUnits_NullResources_ReturnsFalse() {
    // Arrange
    Scorm12Manifest mockManifest = mock(Scorm12Manifest.class);
    when(mockManifest.getResources()).thenReturn(null);

    Scorm12Metadata metadata = Scorm12Metadata.create(mockManifest, false);

    // Act
    boolean result = metadata.hasMultipleLaunchableUnits();

    // Assert
    assertFalse(result);
  }

  @Test
  void testHasMultipleLaunchableUnits_SingleSCO_ReturnsFalse() {
    // Arrange
    Scorm12Manifest mockManifest = mock(Scorm12Manifest.class);
    Scorm12Resources mockResources = mock(Scorm12Resources.class);
    Scorm12Resource mockResource = mock(Scorm12Resource.class);

    when(mockManifest.getResources()).thenReturn(mockResources);
    when(mockResources.getResourceList()).thenReturn(List.of(mockResource));
    when(mockResource.getScormType()).thenReturn(ScormType.SCO);

    Scorm12Metadata metadata = Scorm12Metadata.create(mockManifest, false);

    // Act
    boolean result = metadata.hasMultipleLaunchableUnits();

    // Assert
    assertFalse(result);
  }

  @Test
  void testHasMultipleLaunchableUnits_MultipleSCOs_ReturnsTrue() {
    // Arrange
    Scorm12Manifest mockManifest = mock(Scorm12Manifest.class);
    Scorm12Resources mockResources = mock(Scorm12Resources.class);
    Scorm12Resource mockResource1 = mock(Scorm12Resource.class);
    Scorm12Resource mockResource2 = mock(Scorm12Resource.class);

    when(mockManifest.getResources()).thenReturn(mockResources);
    when(mockResources.getResourceList()).thenReturn(List.of(mockResource1, mockResource2));
    when(mockResource1.getScormType()).thenReturn(ScormType.SCO);
    when(mockResource2.getScormType()).thenReturn(ScormType.SCO);

    Scorm12Metadata metadata = Scorm12Metadata.create(mockManifest, false);

    // Act
    boolean result = metadata.hasMultipleLaunchableUnits();

    // Assert
    assertTrue(result);
  }

  @Test
  void testHasMultipleLaunchableUnits_OnlyAssets_ReturnsFalse() {
    // Arrange
    Scorm12Manifest mockManifest = mock(Scorm12Manifest.class);
    Scorm12Resources mockResources = mock(Scorm12Resources.class);
    Scorm12Resource mockResource1 = mock(Scorm12Resource.class);
    Scorm12Resource mockResource2 = mock(Scorm12Resource.class);

    when(mockManifest.getResources()).thenReturn(mockResources);
    when(mockResources.getResourceList()).thenReturn(List.of(mockResource1, mockResource2));
    when(mockResource1.getScormType()).thenReturn(ScormType.ASSET);
    when(mockResource2.getScormType()).thenReturn(ScormType.ASSET);

    Scorm12Metadata metadata = Scorm12Metadata.create(mockManifest, false);

    // Act
    boolean result = metadata.hasMultipleLaunchableUnits();

    // Assert
    assertFalse(result);
  }

  @Test
  void testHasMultipleLaunchableUnits_MixedSCOsAndAssets_ReturnsTrue() {
    // Arrange
    Scorm12Manifest mockManifest = mock(Scorm12Manifest.class);
    Scorm12Resources mockResources = mock(Scorm12Resources.class);
    Scorm12Resource mockSco1 = mock(Scorm12Resource.class);
    Scorm12Resource mockAsset = mock(Scorm12Resource.class);
    Scorm12Resource mockSco2 = mock(Scorm12Resource.class);

    when(mockManifest.getResources()).thenReturn(mockResources);
    when(mockResources.getResourceList()).thenReturn(List.of(mockSco1, mockAsset, mockSco2));
    when(mockSco1.getScormType()).thenReturn(ScormType.SCO);
    when(mockAsset.getScormType()).thenReturn(ScormType.ASSET);
    when(mockSco2.getScormType()).thenReturn(ScormType.SCO);

    Scorm12Metadata metadata = Scorm12Metadata.create(mockManifest, false);

    // Act
    boolean result = metadata.hasMultipleLaunchableUnits();

    // Assert
    assertTrue(result);
  }
}
