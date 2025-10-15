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
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import dev.jcputney.elearning.parser.input.aicc.prereq.AiccPrerequisiteParser;
import dev.jcputney.elearning.parser.input.common.serialization.DurationHHMMSSDeserializer;
import java.time.Duration;
import java.util.List;
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
    AssignableUnit assignableUnit = new AssignableUnit();
    assignableUnit.setSystemId("A1");
    assignableUnit.setCommandLine("test.html");
    assignableUnit.setFileName("test.html");
    assignableUnit.setCoreVendor("Test Vendor");
    assignableUnit.setType("Lesson");
    assignableUnit.setMaxScore("100");
    assignableUnit.setMasteryScore("80");
    assignableUnit.setMaxTimeAllowed("01:00:00");
    assignableUnit.setTimeLimitAction("C,N");
    assignableUnit.setSystemVendor("System Vendor");
    assignableUnit.setWebLaunch("http://example.com/test.html");
    assignableUnit.setAuPassword("password");
    assignableUnit.setPrerequisitesExpression("A1");
    assignableUnit.setPrerequisiteModel(AiccPrerequisiteParser
        .parse("A1")
        .orElse(null));
    AiccCompletionCriteria criteria = new AiccCompletionCriteria();
    criteria.setCompletionAction("A1");
    criteria.setCompletionLessonStatus("completed");
    criteria.setCompletionResultStatus("passed");
    assignableUnit.setCompletionCriteria(criteria);
    assignableUnit.setMasteryScoreNormalized(0.8);
    Duration normalizedDuration = DurationHHMMSSDeserializer.parseDuration("01:00:00");
    assignableUnit.setMaxTimeAllowedNormalized(normalizedDuration);
    assignableUnit.setTimeLimitActionNormalized(List.of("C", "N"));
    assignableUnit.setPrerequisitesMandatoryOverride(Boolean.TRUE);

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
    assertEquals("A1", assignableUnit.getPrerequisitesExpression());
    assertNotNull(assignableUnit.getPrerequisiteModel());
    assertEquals(List.of("A1"), assignableUnit.getPrerequisiteReferencedAuIds());
    assertTrue(assignableUnit
        .getPrerequisiteOptionalAuIds()
        .isEmpty());
    assertEquals(criteria, assignableUnit.getCompletionCriteria());
    assertEquals(0.8, assignableUnit.getMasteryScoreNormalized());
    assertEquals(normalizedDuration, assignableUnit.getMaxTimeAllowedNormalized());
    assertEquals(List.of("C", "N"), assignableUnit.getTimeLimitActionNormalized());
    assertTrue(assignableUnit.isPrerequisitesMandatory());
    assertNull(assignableUnit.getDescriptor());
  }

  /**
   * Tests the setter for the descriptor property.
   */
  @Test
  void testSetDescriptor() {
    // Create an AssignableUnit using the builder
    AssignableUnit assignableUnit = new AssignableUnit();
    assignableUnit.setSystemId("A1");
    assignableUnit.setCommandLine("test.html");
    assignableUnit.setFileName("test.html");
    assignableUnit.setCoreVendor("Test Vendor");

    // Create a descriptor
    Descriptor descriptor = new Descriptor();
    descriptor.setSystemId("A1");
    descriptor.setDeveloperId("DEV-001");
    descriptor.setTitle("Test AU");
    descriptor.setDescription("Test Assignable Unit");

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
    assertNull(assignableUnit.getPrerequisitesExpression());
    assertNull(assignableUnit.getPrerequisiteModel());
    assertNull(assignableUnit.getCompletionCriteria());
    assertNull(assignableUnit.getMasteryScoreNormalized());
    assertNull(assignableUnit.getMaxTimeAllowedNormalized());
    assertTrue(assignableUnit
        .getTimeLimitActionNormalized()
        .isEmpty());
    assertNull(assignableUnit.getPrerequisitesMandatoryOverride());
    assertFalse(assignableUnit.isPrerequisitesMandatory());
  }
}
