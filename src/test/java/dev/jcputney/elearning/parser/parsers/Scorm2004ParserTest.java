package dev.jcputney.elearning.parser.parsers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import dev.jcputney.elearning.parser.exception.ModuleParsingException;
import dev.jcputney.elearning.parser.impl.LocalFileAccess;
import dev.jcputney.elearning.parser.input.scorm2004.Scorm2004Manifest;
import dev.jcputney.elearning.parser.input.scorm2004.ims.ss.objective.Scorm2004Objective;
import dev.jcputney.elearning.parser.input.scorm2004.ims.ss.objective.Scorm2004ObjectiveMapping;
import dev.jcputney.elearning.parser.input.scorm2004.ims.ss.objective.Scorm2004Objectives;
import dev.jcputney.elearning.parser.input.scorm2004.ims.ss.random.RandomizationControls;
import dev.jcputney.elearning.parser.input.scorm2004.ims.ss.rollup.RollupAction;
import dev.jcputney.elearning.parser.input.scorm2004.ims.ss.rollup.RollupCondition;
import dev.jcputney.elearning.parser.input.scorm2004.ims.ss.rollup.RollupRule;
import dev.jcputney.elearning.parser.input.scorm2004.ims.ss.rollup.RollupRules;
import dev.jcputney.elearning.parser.input.scorm2004.ims.ss.sequencing.ControlMode;
import dev.jcputney.elearning.parser.input.scorm2004.ims.ss.sequencing.LimitConditions;
import dev.jcputney.elearning.parser.input.scorm2004.ims.ss.sequencing.RuleAction;
import dev.jcputney.elearning.parser.input.scorm2004.ims.ss.sequencing.RuleCondition;
import dev.jcputney.elearning.parser.input.scorm2004.ims.ss.sequencing.RuleConditions;
import dev.jcputney.elearning.parser.input.scorm2004.ims.ss.sequencing.Sequencing;
import dev.jcputney.elearning.parser.input.scorm2004.ims.ss.sequencing.SequencingRule;
import dev.jcputney.elearning.parser.input.scorm2004.ims.ss.sequencing.SequencingRules;
import dev.jcputney.elearning.parser.input.scorm2004.ims.ss.types.ChildActivitySet;
import dev.jcputney.elearning.parser.input.scorm2004.ims.ss.types.ConditionCombinationType;
import dev.jcputney.elearning.parser.input.scorm2004.ims.ss.types.ConditionOperatorType;
import dev.jcputney.elearning.parser.input.scorm2004.ims.ss.types.RandomizationTiming;
import dev.jcputney.elearning.parser.input.scorm2004.ims.ss.types.RollupActionType;
import dev.jcputney.elearning.parser.input.scorm2004.ims.ss.types.RollupRuleConditionType;
import dev.jcputney.elearning.parser.input.scorm2004.ims.ss.types.SequencingRuleConditionType;
import dev.jcputney.elearning.parser.output.metadata.scorm2004.Scorm2004Metadata;
import java.io.IOException;
import java.util.List;
import javax.xml.stream.XMLStreamException;
import org.junit.jupiter.api.Test;

/**
 * Tests for the SCORM 2004 parser.
 */
public class Scorm2004ParserTest {

  @Test
  void testParseContentPackagingMetadataSCORM20043rdEdition()
      throws XMLStreamException, IOException, ModuleParsingException {
    String modulePath = "src/test/resources/modules/scorm2004/ContentPackagingMetadata_SCORM20043rdEdition/";
    Scorm2004Parser parser = new Scorm2004Parser(new LocalFileAccess(modulePath));

    Scorm2004Metadata metadata = parser.parse();
    assertNotNull(metadata);
    Scorm2004Manifest manifest = metadata.getManifest();
    assertNotNull(manifest);

    parser.loadExternalMetadata(manifest);

    assertNotNull(manifest.getMetadata().getLom().getGeneral());
    assertEquals(manifest.getMetadata().getLom().getGeneral().getTitle().getLangStrings().size(),
        2);
    assertEquals(manifest.getMetadata().getLom().getGeneral().getKeywords().size(), 3);
    assertEquals("Golf Explained - Metadata Example",
        manifest.getOrganizations().getDefault().getTitle());
    assertEquals("Golf Explained",
        manifest.getMetadata().getLom().getGeneral().getTitle().getLangStrings().get(0).getValue());
    assertEquals("Explicó Golf",
        manifest.getMetadata().getLom().getGeneral().getTitle().getLangStrings().get(1).getValue());
    assertEquals("Golf Explained - Metadata Example", manifest.getTitle());
    assertEquals(39, manifest.getResources().getResourceList().get(0).getFiles().size());
  }

  @Test
  void testParseContentPackagingOneFilePerSCOSCORM20043rdEdition()
      throws XMLStreamException, IOException, ModuleParsingException {
    String modulePath = "src/test/resources/modules/scorm2004/ContentPackagingOneFilePerSCO_SCORM20043rdEdition";
    Scorm2004Parser parser = new Scorm2004Parser(new LocalFileAccess(modulePath));
    Scorm2004Manifest manifest = parser.parseManifest(Scorm2004Parser.MANIFEST_FILE);
    parser.loadExternalMetadata(manifest);
    assertNull(manifest.getMetadata().getLom());
    assertEquals("Golf Explained - CP One File Per SCO",
        manifest.getOrganizations().getDefault().getTitle());
  }

  @Test
  void testParseSequencingRandomTestSCORM20043rdEdition()
      throws XMLStreamException, IOException, ModuleParsingException {
    String modulePath = "src/test/resources/modules/scorm2004/SequencingRandomTest_SCORM20043rdEdition";
    Scorm2004Parser parser = new Scorm2004Parser(new LocalFileAccess(modulePath));
    Scorm2004Manifest manifest = parser.parseManifest(Scorm2004Parser.MANIFEST_FILE);

    // Verify the manifest was parsed correctly
    assertNotNull(manifest);
    assertEquals("Golf Explained - Sequencing Random Test", 
        manifest.getOrganizations().getDefault().getTitle());

    // Get the content wrapper item and verify its sequencing
    var contentWrapper = manifest.getOrganizations().getDefault().getItems().get(0);
    assertEquals("content_wrapper", contentWrapper.getIdentifier());

    // Verify the content wrapper's sequencing
    Sequencing contentWrapperSequencing = contentWrapper.getSequencing();
    assertNotNull(contentWrapperSequencing);

    // Verify control mode
    ControlMode controlMode = contentWrapperSequencing.getControlMode();
    assertNotNull(controlMode);
    assertTrue(controlMode.isChoice());
    assertTrue(controlMode.isFlow());

    // Verify rollup rules
    RollupRules rollupRules = contentWrapperSequencing.getRollupRules();
    assertNotNull(rollupRules);
    assertEquals(0.0, rollupRules.getObjectiveMeasureWeight());
    assertFalse(rollupRules.isRollupObjectiveSatisfied());
    assertFalse(rollupRules.isRollupProgressCompletion());

    List<RollupRule> rules = rollupRules.getRollupRuleList();
    assertNotNull(rules);
    assertEquals(1, rules.size());

    RollupRule rule = rules.get(0);
    assertEquals(ChildActivitySet.ALL, rule.getChildActivitySet());

    List<RollupCondition> conditions = rule.getRollupConditions().getRollupConditionList();
    assertNotNull(conditions);
    assertEquals(1, conditions.size());
    assertEquals(RollupRuleConditionType.COMPLETED, conditions.get(0).getCondition());

    RollupAction action = rule.getRollupAction();
    assertNotNull(action);
    assertEquals(RollupActionType.SATISFIED, action.getAction());

    // Verify objectives
    Scorm2004Objectives objectives = contentWrapperSequencing.getObjectives();
    assertNotNull(objectives);

    Scorm2004Objective primaryObjective = objectives.getPrimaryObjective();
    assertNotNull(primaryObjective);
    assertEquals("content_completed", primaryObjective.getObjectiveID());

    List<Scorm2004ObjectiveMapping> mapInfo = primaryObjective.getMapInfo();
    assertNotNull(mapInfo);
    assertEquals(1, mapInfo.size());

    Scorm2004ObjectiveMapping mapping = mapInfo.get(0);
    assertEquals("com.scorm.golfsamples.sequencing.randomtest.content_completed", mapping.getTargetObjectiveID());
    assertTrue(mapping.isWriteSatisfiedStatus());

    // Get the post test item and verify its sequencing
    var postTestItem = manifest.getOrganizations().getDefault().getItems().get(1);
    assertEquals("posttest_item", postTestItem.getIdentifier());

    // Verify the post test's sequencing
    Sequencing postTestSequencing = postTestItem.getSequencing();
    assertNotNull(postTestSequencing);

    // Verify control mode
    controlMode = postTestSequencing.getControlMode();
    assertNotNull(controlMode);
    assertFalse(controlMode.isChoice());
    assertTrue(controlMode.isFlow());

    // Verify sequencing rules
    SequencingRules sequencingRules = postTestSequencing.getSequencingRules();
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
    LimitConditions limitConditions = postTestSequencing.getLimitConditions();
    assertNotNull(limitConditions);
    assertEquals(2, limitConditions.getAttemptLimit());

    // Verify randomization controls
    RandomizationControls randomizationControls = postTestSequencing.getRandomizationControls();
    assertNotNull(randomizationControls);
    assertEquals(RandomizationTiming.ON_EACH_NEW_ATTEMPT, randomizationControls.getRandomizationTiming());
    assertTrue(randomizationControls.isReorderChildren());
  }
}
