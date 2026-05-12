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
package dev.jcputney.elearning.parser.input.scorm2004.ims.ss.rollup;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import dev.jcputney.elearning.parser.input.scorm2004.ims.ss.types.ChildActivitySet;
import dev.jcputney.elearning.parser.input.scorm2004.ims.ss.types.RollupActionType;
import dev.jcputney.elearning.parser.input.scorm2004.ims.ss.types.RollupRuleConditionType;
import org.junit.jupiter.api.Test;

/**
 * Tests for the {@link RollupRules} class.
 */
class RollupRulesTest {

  private final XmlMapper xmlMapper = new XmlMapper();

  @Test
  void testDeserializeRollupRules() throws Exception {
    // Given
    String xml = "<rollupRules xmlns=\"http://www.imsglobal.org/xsd/imsss\">" +
        "<rollupRule>" +
        "<rollupConditions>" +
        "<rollupCondition condition=\"completed\"/>" +
        "</rollupConditions>" +
        "<rollupAction action=\"satisfied\"/>" +
        "</rollupRule>" +
        "</rollupRules>";

    // When
    RollupRules rollupRules = xmlMapper.readValue(xml, RollupRules.class);

    // Then
    assertNotNull(rollupRules);
    assertTrue(rollupRules.isRollupObjectiveSatisfied());
    assertTrue(rollupRules.isRollupProgressCompletion());
    assertEquals(1.0, rollupRules.getObjectiveMeasureWeight());

    assertNotNull(rollupRules.getRollupRuleList());
    assertEquals(1, rollupRules.getRollupRuleList().size());

    RollupRule rule = rollupRules.getRollupRuleList().get(0);
    assertEquals(ChildActivitySet.ALL, rule.getChildActivitySet());

    assertNotNull(rule.getRollupConditions());
    assertEquals(1, rule.getRollupConditions().getRollupConditionList().size());
    assertEquals(RollupRuleConditionType.COMPLETED,
        rule.getRollupConditions().getRollupConditionList().get(0).getCondition());

    assertNotNull(rule.getRollupAction());
    assertEquals(RollupActionType.SATISFIED, rule.getRollupAction().getAction());
  }

  @Test
  void testDeserializeRollupRulesWithAttributes() throws Exception {
    // Given
    String xml = "<rollupRules xmlns=\"http://www.imsglobal.org/xsd/imsss\" " +
        "rollupObjectiveSatisfied=\"false\" rollupProgressCompletion=\"false\" objectiveMeasureWeight=\"0.5\">"
        +
        "<rollupRule>" +
        "<rollupConditions>" +
        "<rollupCondition condition=\"completed\"/>" +
        "</rollupConditions>" +
        "<rollupAction action=\"satisfied\"/>" +
        "</rollupRule>" +
        "</rollupRules>";

    // When
    RollupRules rollupRules = xmlMapper.readValue(xml, RollupRules.class);

    // Then
    assertNotNull(rollupRules);
    assertFalse(rollupRules.isRollupObjectiveSatisfied());
    assertFalse(rollupRules.isRollupProgressCompletion());
    assertEquals(0.5, rollupRules.getObjectiveMeasureWeight());

    assertNotNull(rollupRules.getRollupRuleList());
    assertEquals(1, rollupRules.getRollupRuleList().size());
  }

  @Test
  void testDeserializeRollupRulesWithMultipleRules() throws Exception {
    // Given
    String xml = "<rollupRules xmlns=\"http://www.imsglobal.org/xsd/imsss\">" +
        "<rollupRule>" +
        "<rollupConditions>" +
        "<rollupCondition condition=\"completed\"/>" +
        "</rollupConditions>" +
        "<rollupAction action=\"satisfied\"/>" +
        "</rollupRule>" +
        "<rollupRule>" +
        "<rollupConditions>" +
        "<rollupCondition condition=\"satisfied\"/>" +
        "</rollupConditions>" +
        "<rollupAction action=\"completed\"/>" +
        "</rollupRule>" +
        "</rollupRules>";

    // When
    RollupRules rollupRules = xmlMapper.readValue(xml, RollupRules.class);

    // Then
    assertNotNull(rollupRules);
    assertNotNull(rollupRules.getRollupRuleList());
    assertEquals(2, rollupRules.getRollupRuleList().size());

    RollupRule rule1 = rollupRules.getRollupRuleList().get(0);
    assertEquals(RollupRuleConditionType.COMPLETED,
        rule1.getRollupConditions().getRollupConditionList().get(0).getCondition());
    assertEquals(RollupActionType.SATISFIED, rule1.getRollupAction().getAction());

    RollupRule rule2 = rollupRules.getRollupRuleList().get(1);
    assertEquals(RollupRuleConditionType.SATISFIED,
        rule2.getRollupConditions().getRollupConditionList().get(0).getCondition());
    assertEquals(RollupActionType.COMPLETED, rule2.getRollupAction().getAction());
  }

  @Test
  void testDeserializeEmptyRollupRules() throws Exception {
    // Given
    String xml = "<rollupRules xmlns=\"http://www.imsglobal.org/xsd/imsss\"/>";

    // When
    RollupRules rollupRules = xmlMapper.readValue(xml, RollupRules.class);

    // Then
    assertNotNull(rollupRules);
    // Default values are set during deserialization
    assertTrue(rollupRules.isRollupObjectiveSatisfied());
    assertTrue(rollupRules.isRollupProgressCompletion());
    assertEquals(1.0, rollupRules.getObjectiveMeasureWeight());
  }

  @Test
  void testDefaultConstructor() {
    // When
    RollupRules rollupRules = new RollupRules();

    // Then
    assertNotNull(rollupRules);
    // Note: Default values are not set in the default constructor
    // Only test that the object is created successfully
  }
}
