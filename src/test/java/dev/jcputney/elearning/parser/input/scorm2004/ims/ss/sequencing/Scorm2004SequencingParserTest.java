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

package dev.jcputney.elearning.parser.input.scorm2004.ims.ss.sequencing;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import dev.jcputney.elearning.parser.exception.ManifestParseException;
import dev.jcputney.elearning.parser.exception.ModuleParsingException;
import dev.jcputney.elearning.parser.impl.access.LocalFileAccess;
import dev.jcputney.elearning.parser.input.scorm2004.Scorm2004Manifest;
import dev.jcputney.elearning.parser.input.scorm2004.ims.ss.random.RandomizationControls;
import dev.jcputney.elearning.parser.input.scorm2004.ims.ss.rollup.RollupAction;
import dev.jcputney.elearning.parser.input.scorm2004.ims.ss.rollup.RollupCondition;
import dev.jcputney.elearning.parser.input.scorm2004.ims.ss.rollup.RollupRule;
import dev.jcputney.elearning.parser.input.scorm2004.ims.ss.rollup.RollupRules;
import dev.jcputney.elearning.parser.input.scorm2004.ims.ss.types.ChildActivitySet;
import dev.jcputney.elearning.parser.input.scorm2004.ims.ss.types.ConditionCombinationType;
import dev.jcputney.elearning.parser.input.scorm2004.ims.ss.types.ConditionOperatorType;
import dev.jcputney.elearning.parser.input.scorm2004.ims.ss.types.RandomizationTiming;
import dev.jcputney.elearning.parser.input.scorm2004.ims.ss.types.RollupActionType;
import dev.jcputney.elearning.parser.input.scorm2004.ims.ss.types.RollupRuleConditionType;
import dev.jcputney.elearning.parser.input.scorm2004.ims.ss.types.SequencingRuleConditionType;
import dev.jcputney.elearning.parser.parsers.Scorm2004Parser;
import java.io.IOException;
import java.util.List;
import javax.xml.stream.XMLStreamException;
import org.junit.jupiter.api.Test;

/**
 * Tests for parsing SCORM 2004 sequencing elements from manifest files. This test focuses
 * specifically on the sequencing classes and their parsing.
 */
public class Scorm2004SequencingParserTest {

  public static final String PACKAGE_PATH = "src/test/resources/modules/scorm2004/SequencingRandomTest_SCORM20043rdEdition";

  @Test
  void testParseContentWrapperSequencing()
      throws XMLStreamException, IOException, ModuleParsingException, ManifestParseException {
    // Parse the SequencingRandomTest_SCORM20043rdEdition manifest
    Scorm2004Parser parser = new Scorm2004Parser(new LocalFileAccess(PACKAGE_PATH));
    Scorm2004Manifest manifest = parser.parseManifest(Scorm2004Parser.MANIFEST_FILE);

    // Verify the manifest was parsed correctly
    assertNotNull(manifest);

    // Get the content wrapper item and verify its sequencing
    var contentWrapper = manifest
        .getOrganizations()
        .getDefault()
        .getItems()
        .get(0);
    assertEquals("content_wrapper", contentWrapper.getIdentifier());

    // Verify the content wrapper's sequencing
    Sequencing sequencing = contentWrapper.getSequencing();
    assertNotNull(sequencing);

    // Verify control mode
    ControlMode controlMode = sequencing.getControlMode();
    assertNotNull(controlMode);
    assertTrue(controlMode.isChoice());
    assertTrue(controlMode.isFlow());

    // Verify rollup rules
    RollupRules rollupRules = sequencing.getRollupRules();
    assertNotNull(rollupRules);
    assertEquals(0.0, rollupRules.getObjectiveMeasureWeight());
    assertFalse(rollupRules.isRollupObjectiveSatisfied());
    assertFalse(rollupRules.isRollupProgressCompletion());

    List<RollupRule> rules = rollupRules.getRollupRuleList();
    assertNotNull(rules);
    assertEquals(1, rules.size());

    RollupRule rule = rules.get(0);
    assertEquals(ChildActivitySet.ALL, rule.getChildActivitySet());

    List<RollupCondition> conditions = rule
        .getRollupConditions()
        .getRollupConditionList();
    assertNotNull(conditions);
    assertEquals(1, conditions.size());
    assertEquals(RollupRuleConditionType.COMPLETED, conditions
        .get(0)
        .getCondition());

    RollupAction action = rule.getRollupAction();
    assertNotNull(action);
    assertEquals(RollupActionType.SATISFIED, action.getAction());
  }

  @Test
  void testParsePostTestSequencing()
      throws XMLStreamException, IOException, ModuleParsingException, ManifestParseException {
    // Parse the SequencingRandomTest_SCORM20043rdEdition manifest
    Scorm2004Parser parser = new Scorm2004Parser(new LocalFileAccess(PACKAGE_PATH));
    Scorm2004Manifest manifest = parser.parseManifest(Scorm2004Parser.MANIFEST_FILE);

    // Verify the manifest was parsed correctly
    assertNotNull(manifest);

    // Get the post test item and verify its sequencing
    var postTestItem = manifest
        .getOrganizations()
        .getDefault()
        .getItems()
        .get(1);
    assertEquals("posttest_item", postTestItem.getIdentifier());

    // Verify the post test's sequencing
    Sequencing sequencing = postTestItem.getSequencing();
    assertNotNull(sequencing);

    // Verify control mode
    ControlMode controlMode = sequencing.getControlMode();
    assertNotNull(controlMode);
    assertFalse(controlMode.isChoice());
    assertTrue(controlMode.isFlow());

    // Verify sequencing rules
    SequencingRules sequencingRules = sequencing.getSequencingRules();
    assertNotNull(sequencingRules);

    // Verify pre-condition rules
    List<SequencingRule> preConditionRules = sequencingRules.getPreConditionRules();
    assertNotNull(preConditionRules);
    assertEquals(2, preConditionRules.size());

    // First pre-condition rule
    SequencingRule preRule1 = preConditionRules.get(0);
    RuleConditions ruleConditions = preRule1.getRuleConditions();
    assertEquals(ConditionCombinationType.ANY, ruleConditions.getConditionCombination());

    List<RuleCondition> ruleConditionList = ruleConditions.getRuleConditionList();
    assertEquals(2, ruleConditionList.size());

    RuleCondition condition1 = ruleConditionList.get(0);
    assertEquals("content_completed", condition1.getReferencedObjective());
    assertEquals(ConditionOperatorType.NOT, condition1.getOperator());
    assertEquals(SequencingRuleConditionType.SATISFIED, condition1.getCondition());

    RuleAction ruleAction = preRule1.getRuleAction();
    assertEquals("disabled", ruleAction.getAction());

    // Verify limit conditions
    LimitConditions limitConditions = sequencing.getLimitConditions();
    assertNotNull(limitConditions);
    assertEquals(2, limitConditions.getAttemptLimit());

    // Verify randomization controls
    RandomizationControls randomizationControls = sequencing.getRandomizationControls();
    assertNotNull(randomizationControls);
    assertEquals(RandomizationTiming.ON_EACH_NEW_ATTEMPT,
        randomizationControls.getRandomizationTiming());
    assertTrue(randomizationControls.isReorderChildren());

    // Verify post-condition rules
    List<SequencingRule> postConditionRules = sequencingRules.getPostConditionRules();
    assertNotNull(postConditionRules);
    assertEquals(3, postConditionRules.size());

    // Last post-condition rule (exit all)
    SequencingRule postRule3 = postConditionRules.get(2);
    ruleConditions = postRule3.getRuleConditions();
    assertEquals(ConditionCombinationType.ANY, ruleConditions.getConditionCombination());

    ruleConditionList = ruleConditions.getRuleConditionList();
    assertEquals(2, ruleConditionList.size());

    condition1 = ruleConditionList.get(0);
    assertEquals(SequencingRuleConditionType.OBJECTIVE_STATUS_KNOWN, condition1.getCondition());

    ruleAction = postRule3.getRuleAction();
    assertEquals("exitAll", ruleAction.getAction());
  }

  @Test
  void testParseSequencingCollection()
      throws XMLStreamException, IOException, ModuleParsingException, ManifestParseException {
    // Parse the SequencingRandomTest_SCORM20043rdEdition manifest
    Scorm2004Parser parser = new Scorm2004Parser(new LocalFileAccess(PACKAGE_PATH));
    Scorm2004Manifest manifest = parser.parseManifest(Scorm2004Parser.MANIFEST_FILE);

    // Verify the manifest was parsed correctly
    assertNotNull(manifest);

    // Get the sequencing collection
    SequencingCollection sequencingCollection = manifest.getSequencingCollection();
    assertNotNull(sequencingCollection);

    // Verify the sequencing collection contains at least one sequencing
    List<Sequencing> sequencingList = sequencingCollection.getSequencingList();
    assertNotNull(sequencingList);
    assertFalse(sequencingList.isEmpty());

    // Verify the first sequencing in the collection
    Sequencing sequencing = sequencingList.get(0);
    assertNotNull(sequencing);

    // Verify the sequencing collection contains the expected sequencing
    assertEquals(1, sequencingList.size());

    // Verify the sequencing has sequencing rules
    SequencingRules sequencingRules = sequencing.getSequencingRules();
    assertNotNull(sequencingRules);

    // Verify post-condition rules
    List<SequencingRule> postConditionRules = sequencingRules.getPostConditionRules();
    assertNotNull(postConditionRules);
    assertFalse(postConditionRules.isEmpty());
  }
}
