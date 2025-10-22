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

package dev.jcputney.elearning.parser.output.metadata;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import dev.jcputney.elearning.parser.enums.ModuleType;
import dev.jcputney.elearning.parser.input.PackageManifest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class BaseModuleMetadataTest {

  @Mock
  private TestManifest manifest;

  private TestBaseModuleMetadata metadata;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    metadata = new TestBaseModuleMetadata(manifest, ModuleType.SCORM_12, true);
  }

  @Test
  void constructorStoresManifestAndModuleInfo() {
    assertNotNull(metadata);
    assertSame(manifest, metadata.getManifest());
    assertEquals(ModuleType.SCORM_12, metadata.getModuleType());
    assertTrue(metadata.isXapiEnabled());
  }

  @Test
  void constantsMatchExpectedKeys() {
    assertEquals("title", BaseModuleMetadata.TITLE);
    assertEquals("description", BaseModuleMetadata.DESCRIPTION);
    assertEquals("launchUrl", BaseModuleMetadata.LAUNCH_URL);
    assertEquals("identifier", BaseModuleMetadata.IDENTIFIER);
    assertEquals("version", BaseModuleMetadata.VERSION);
    assertEquals("duration", BaseModuleMetadata.DURATION);
    assertEquals("moduleType", BaseModuleMetadata.MODULE_TYPE);
    assertEquals("xapiEnabled", BaseModuleMetadata.XAPI_ENABLED);
  }

  @Test
  void equalityDependsOnBaseState() {
    when(manifest.getTitle()).thenReturn("Title");
    metadata = new TestBaseModuleMetadata(manifest, ModuleType.SCORM_12, true);
    TestBaseModuleMetadata same = new TestBaseModuleMetadata(manifest, ModuleType.SCORM_12, true);

    assertEquals(metadata, same);
    assertEquals(metadata.hashCode(), same.hashCode());

    TestBaseModuleMetadata different = new TestBaseModuleMetadata(manifest, ModuleType.AICC, true);
    assertNotEquals(metadata, different);
  }

  private interface TestManifest extends PackageManifest {
    // Marker interface for Mockito
  }

  private static class TestBaseModuleMetadata extends BaseModuleMetadata<TestManifest> {

    TestBaseModuleMetadata(TestManifest manifest, ModuleType moduleType, boolean xapiEnabled) {
      super(manifest, moduleType, xapiEnabled);
    }

    @Override
    public boolean hasMultipleLaunchableUnits() {
      return false;
    }

    @Override
    public String getManifestFile() {
      return "test-manifest.xml";
    }
  }
}
