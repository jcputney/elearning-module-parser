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
 * Comprehensive tests for the Descriptor class.
 */
public class DescriptorTest {

  /**
   * Tests the builder and getters for the Descriptor class.
   */
  @Test
  void testDescriptorBuilderAndGetters() {
    // Create a Descriptor using the builder
    Descriptor descriptor = new Descriptor("A1", "DEV-001", "Test AU", "Test Assignable Unit");

    // Verify the getters
    assertEquals("A1", descriptor.getSystemId());
    assertEquals("DEV-001", descriptor.getDeveloperId());
    assertEquals("Test AU", descriptor.getTitle());
    assertEquals("Test Assignable Unit", descriptor.getDescription());
  }

  /**
   * Tests the builder with null values.
   */
  @Test
  void testDescriptorBuilderWithNullValues() {
    // Create a Descriptor using the builder with null values
    Descriptor descriptor = new Descriptor();
    descriptor.setSystemId(null);
    descriptor.setDeveloperId(null);
    descriptor.setTitle(null);
    descriptor.setDescription(null);

    // Verify the getters return null
    assertNull(descriptor.getSystemId());
    assertNull(descriptor.getDeveloperId());
    assertNull(descriptor.getTitle());
    assertNull(descriptor.getDescription());
  }

  /**
   * Tests the builder with only required fields.
   */
  @Test
  void testDescriptorBuilderWithRequiredFieldsOnly() {
    // Create a Descriptor using the builder with only required fields
    Descriptor descriptor = new Descriptor();
    descriptor.setSystemId("A1");
    descriptor.setDeveloperId("DEV-001");
    descriptor.setTitle("Test AU");

    // Verify the getters
    assertEquals("A1", descriptor.getSystemId());
    assertEquals("DEV-001", descriptor.getDeveloperId());
    assertEquals("Test AU", descriptor.getTitle());
    assertNull(descriptor.getDescription());
  }

  /**
   * Tests the default constructor for the Descriptor class.
   */
  @Test
  void testDescriptorDefaultConstructor() {
    // Create a Descriptor using the default constructor
    Descriptor descriptor = new Descriptor();

    // Verify that the properties are null
    assertNull(descriptor.getSystemId());
    assertNull(descriptor.getDeveloperId());
    assertNull(descriptor.getTitle());
    assertNull(descriptor.getDescription());
  }
}