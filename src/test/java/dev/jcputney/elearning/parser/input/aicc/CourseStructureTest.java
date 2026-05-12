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