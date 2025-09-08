/* Copyright (c) 2025. Jonathan Putney
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
