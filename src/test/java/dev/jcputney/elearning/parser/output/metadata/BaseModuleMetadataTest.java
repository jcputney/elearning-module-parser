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
