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
 * Comprehensive tests for the AssignableUnit class.
 */
public class AssignableUnitTest {

  /**
   * Tests the builder and getters for the AssignableUnit class.
   */
  @Test
  void testAssignableUnitBuilderAndGetters() {
    // Create an AssignableUnit using the builder
    AssignableUnit assignableUnit = AssignableUnit.builder()
        .systemId("A1")
        .commandLine("test.html")
        .fileName("test.html")
        .coreVendor("Test Vendor")
        .type("Lesson")
        .maxScore("100")
        .masteryScore("80")
        .maxTimeAllowed("01:00:00")
        .timeLimitAction("C,N")
        .systemVendor("System Vendor")
        .webLaunch("http://example.com/test.html")
        .auPassword("password")
        .build();

    // Verify the getters
    assertEquals("A1", assignableUnit.getSystemId());
    assertEquals("test.html", assignableUnit.getCommandLine());
    assertEquals("test.html", assignableUnit.getFileName());
    assertEquals("Test Vendor", assignableUnit.getCoreVendor());
    assertEquals("Lesson", assignableUnit.getType());
    assertEquals("100", assignableUnit.getMaxScore());
    assertEquals("80", assignableUnit.getMasteryScore());
    assertEquals("01:00:00", assignableUnit.getMaxTimeAllowed());
    assertEquals("C,N", assignableUnit.getTimeLimitAction());
    assertEquals("System Vendor", assignableUnit.getSystemVendor());
    assertEquals("http://example.com/test.html", assignableUnit.getWebLaunch());
    assertEquals("password", assignableUnit.getAuPassword());
    assertNull(assignableUnit.getDescriptor());
  }

  /**
   * Tests the setter for the descriptor property.
   */
  @Test
  void testSetDescriptor() {
    // Create an AssignableUnit using the builder
    AssignableUnit assignableUnit = AssignableUnit.builder()
        .systemId("A1")
        .commandLine("test.html")
        .fileName("test.html")
        .coreVendor("Test Vendor")
        .build();

    // Create a descriptor
    Descriptor descriptor = Descriptor.builder()
        .systemId("A1")
        .developerId("DEV-001")
        .title("Test AU")
        .description("Test Assignable Unit")
        .build();

    // Set the descriptor
    assignableUnit.setDescriptor(descriptor);

    // Verify the descriptor was set
    assertEquals(descriptor, assignableUnit.getDescriptor());
  }

  /**
   * Tests the default constructor for the AssignableUnit class.
   */
  @Test
  void testAssignableUnitDefaultConstructor() {
    // Create an AssignableUnit using the default constructor
    AssignableUnit assignableUnit = new AssignableUnit();

    // Verify that the properties are null
    assertNull(assignableUnit.getSystemId());
    assertNull(assignableUnit.getCommandLine());
    assertNull(assignableUnit.getFileName());
    assertNull(assignableUnit.getCoreVendor());
    assertNull(assignableUnit.getType());
    assertNull(assignableUnit.getMaxScore());
    assertNull(assignableUnit.getMasteryScore());
    assertNull(assignableUnit.getMaxTimeAllowed());
    assertNull(assignableUnit.getTimeLimitAction());
    assertNull(assignableUnit.getSystemVendor());
    assertNull(assignableUnit.getWebLaunch());
    assertNull(assignableUnit.getAuPassword());
    assertNull(assignableUnit.getDescriptor());
  }
}