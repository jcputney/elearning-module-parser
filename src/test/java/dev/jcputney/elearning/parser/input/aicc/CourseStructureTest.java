/*
 * qlty-ignore: +qlty:similar-code
 *
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
 *
 * qlty-ignore: -qlty:similar-code
 */

package dev.jcputney.elearning.parser.input.aicc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

/**
 * Comprehensive tests for the CourseStructure class.
 */
public class CourseStructureTest {

  /**
   * Tests the builder and getters for the CourseStructure class.
   */
  @Test
  void testCourseStructureBuilderAndGetters() {
    // Create a CourseStructure using the builder
    CourseStructure courseStructure = new CourseStructure();
    courseStructure.setBlock("ROOT");
    courseStructure.setMember("A1");

    // Verify the getters
    assertEquals("ROOT", courseStructure.getBlock());
    assertEquals("A1", courseStructure.getMember());
  }

  /**
   * Tests the builder with null values.
   */
  @Test
  void testCourseStructureBuilderWithNullValues() {
    // Create a CourseStructure using the builder with null values
    CourseStructure courseStructure = new CourseStructure();
    courseStructure.setBlock(null);
    courseStructure.setMember(null);

    // Verify the getters return null
    assertNull(courseStructure.getBlock());
    assertNull(courseStructure.getMember());
  }

  /**
   * Tests the default constructor for the CourseStructure class.
   */
  @Test
  void testCourseStructureDefaultConstructor() {
    // Create a CourseStructure using the default constructor
    CourseStructure courseStructure = new CourseStructure();

    // Verify that the properties are null
    assertNull(courseStructure.getBlock());
    assertNull(courseStructure.getMember());
  }
}