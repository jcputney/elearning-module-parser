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
package dev.jcputney.elearning.parser.input.scorm12.adl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import dev.jcputney.elearning.parser.input.scorm12.adl.prerequisite.AndExpression;
import dev.jcputney.elearning.parser.input.scorm12.adl.prerequisite.ItemReference;
import dev.jcputney.elearning.parser.input.scorm12.adl.prerequisite.OrExpression;
import dev.jcputney.elearning.parser.input.scorm12.adl.prerequisite.ParseError;
import dev.jcputney.elearning.parser.input.scorm12.adl.prerequisite.PrerequisiteExpression;
import org.junit.jupiter.api.Test;

/**
 * Tests for the {@link Scorm12Prerequisites} class.
 */
class Scorm12PrerequisitesTest {

  /**
   * Tests that getParsedExpression() returns null for null value.
   */
  @Test
  void getParsedExpressionReturnsNullForNullValue() {
    Scorm12Prerequisites prerequisites = new Scorm12Prerequisites();
    prerequisites.setValue(null);

    PrerequisiteExpression result = prerequisites.getParsedExpression();

    assertNull(result);
  }

  /**
   * Tests that getParsedExpression() returns null for empty value.
   */
  @Test
  void getParsedExpressionReturnsNullForEmptyValue() {
    Scorm12Prerequisites prerequisites = new Scorm12Prerequisites();
    prerequisites.setValue("");

    PrerequisiteExpression result = prerequisites.getParsedExpression();

    assertNull(result);
  }

  /**
   * Tests that getParsedExpression() returns null for blank value.
   */
  @Test
  void getParsedExpressionReturnsNullForBlankValue() {
    Scorm12Prerequisites prerequisites = new Scorm12Prerequisites();
    prerequisites.setValue("   ");

    PrerequisiteExpression result = prerequisites.getParsedExpression();

    assertNull(result);
  }

  /**
   * Tests that getParsedExpression() parses a simple item reference.
   */
  @Test
  void getParsedExpressionParsesSimpleItemReference() {
    Scorm12Prerequisites prerequisites = new Scorm12Prerequisites();
    prerequisites.setValue("ITEM-001");
    prerequisites.setType("aicc_script");

    PrerequisiteExpression result = prerequisites.getParsedExpression();

    assertInstanceOf(ItemReference.class, result);
    assertEquals("ITEM-001", ((ItemReference) result).identifier());
  }

  /**
   * Tests that getParsedExpression() parses without type attribute. Many SCORM 1.2 packages omit
   * the type attribute even when using AICC script format.
   */
  @Test
  void getParsedExpressionParsesWithoutTypeAttribute() {
    Scorm12Prerequisites prerequisites = new Scorm12Prerequisites();
    prerequisites.setValue("ITEM-001 & ITEM-002");
    // Note: type is not set

    PrerequisiteExpression result = prerequisites.getParsedExpression();

    assertInstanceOf(AndExpression.class, result);
    AndExpression and = (AndExpression) result;
    assertEquals(2, and
        .operands()
        .size());
  }

  /**
   * Tests that getParsedExpression() returns ParseError for invalid expression.
   */
  @Test
  void getParsedExpressionReturnsParseErrorForInvalidExpression() {
    Scorm12Prerequisites prerequisites = new Scorm12Prerequisites();
    prerequisites.setValue("ITEM-001 &"); // Missing operand after &

    PrerequisiteExpression result = prerequisites.getParsedExpression();

    assertInstanceOf(ParseError.class, result);
    ParseError error = (ParseError) result;
    assertNotNull(error.errorMessage());
  }

  /**
   * Tests that getParsedExpression() parses complex expressions.
   */
  @Test
  void getParsedExpressionParsesComplexExpression() {
    Scorm12Prerequisites prerequisites = new Scorm12Prerequisites();
    prerequisites.setValue("(ITEM-001 & ITEM-002) | ITEM-003");

    PrerequisiteExpression result = prerequisites.getParsedExpression();

    assertNotNull(result);
    // Should be an OR expression with AND and ItemReference as operands
    assertInstanceOf(OrExpression.class, result);
  }

  /**
   * Tests equals() and hashCode() for Scorm12Prerequisites.
   */
  @Test
  void testEqualsAndHashCode() {
    Scorm12Prerequisites prereq1 = new Scorm12Prerequisites();
    prereq1.setValue("ITEM-001");
    prereq1.setType("aicc_script");

    Scorm12Prerequisites prereq2 = new Scorm12Prerequisites();
    prereq2.setValue("ITEM-001");
    prereq2.setType("aicc_script");

    Scorm12Prerequisites prereq3 = new Scorm12Prerequisites();
    prereq3.setValue("ITEM-002");
    prereq3.setType("aicc_script");

    assertEquals(prereq1, prereq2);
    assertEquals(prereq1.hashCode(), prereq2.hashCode());

    // Different values should not be equal
    assertNotNull(prereq1);
    assertNotNull(prereq3);
  }

  /**
   * Tests getters and setters.
   */
  @Test
  void testGettersAndSetters() {
    Scorm12Prerequisites prerequisites = new Scorm12Prerequisites();

    prerequisites.setValue("ITEM-001 & ITEM-002");
    prerequisites.setType("aicc_script");

    assertEquals("ITEM-001 & ITEM-002", prerequisites.getValue());
    assertEquals("aicc_script", prerequisites.getType());
  }
}
