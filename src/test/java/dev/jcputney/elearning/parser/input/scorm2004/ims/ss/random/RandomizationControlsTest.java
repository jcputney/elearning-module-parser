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
package dev.jcputney.elearning.parser.input.scorm2004.ims.ss.random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import dev.jcputney.elearning.parser.input.scorm2004.ims.ss.types.RandomizationTiming;
import org.junit.jupiter.api.Test;

/**
 * Tests for the RandomizationControls class.
 */
public class RandomizationControlsTest {

  @Test
  void testDefaultConstructor() {
    // Create a RandomizationControls instance using the default constructor
    RandomizationControls controls = new RandomizationControls();

    // Verify the instance is created
    assertNotNull(controls);

    assertEquals(RandomizationTiming.NEVER, controls.getRandomizationTiming());
    assertEquals(RandomizationTiming.NEVER, controls.getSelectionTiming());
    assertFalse(controls.isReorderChildren()); // boolean defaults to false in Java
    assertNull(controls.getSelectCount());
  }

  @Test
  void testBuilderWithDefaults() {
    // Create a RandomizationControls instance using the builder with default values
    RandomizationControls controls = new RandomizationControls();

    // Verify the default values
    assertNotNull(controls);
    assertEquals(RandomizationTiming.NEVER, controls.getRandomizationTiming());
    assertEquals(RandomizationTiming.NEVER, controls.getSelectionTiming());
    assertFalse(controls.isReorderChildren());
    assertNull(controls.getSelectCount());
  }

  @Test
  void testBuilderWithCustomValues() {
    // Create a RandomizationControls instance using the builder with custom values
    RandomizationControls controls = new RandomizationControls();
    controls.setRandomizationTiming(RandomizationTiming.ON_EACH_NEW_ATTEMPT);
    controls.setSelectionTiming(RandomizationTiming.ONCE);
    controls.setReorderChildren(true);
    controls.setSelectCount(5);

    // Verify the custom values
    assertNotNull(controls);
    assertEquals(RandomizationTiming.ON_EACH_NEW_ATTEMPT, controls.getRandomizationTiming());
    assertEquals(RandomizationTiming.ONCE, controls.getSelectionTiming());
    assertTrue(controls.isReorderChildren());
    assertEquals(5, controls.getSelectCount());
  }

  @Test
  void testBuilderWithPartialCustomValues() {
    // Create a RandomizationControls instance using the builder with some custom values
    RandomizationControls controls = new RandomizationControls();
    controls.setRandomizationTiming(RandomizationTiming.ONCE);
    controls.setReorderChildren(true);

    // Verify the values
    assertNotNull(controls);
    assertEquals(RandomizationTiming.ONCE, controls.getRandomizationTiming());
    assertEquals(RandomizationTiming.NEVER, controls.getSelectionTiming()); // Default value
    assertTrue(controls.isReorderChildren());
    assertNull(controls.getSelectCount()); // Default value
  }
}
