/*
 * Copyright (c) 2025. Jonathan Putney
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

package dev.jcputney.elearning.parser.input.scorm2004;

import dev.jcputney.elearning.parser.input.scorm2004.adl.sequencing.ConstrainChoiceConsiderations;
import dev.jcputney.elearning.parser.input.scorm2004.adl.sequencing.RollupConsiderations;
import dev.jcputney.elearning.parser.input.scorm2004.adl.types.ScormType;
import dev.jcputney.elearning.parser.input.scorm2004.ims.cp.Scorm2004Item;
import dev.jcputney.elearning.parser.input.scorm2004.ims.cp.Scorm2004Organization;
import dev.jcputney.elearning.parser.input.scorm2004.ims.cp.Scorm2004Organizations;
import dev.jcputney.elearning.parser.input.scorm2004.ims.cp.Scorm2004Resource;
import dev.jcputney.elearning.parser.input.scorm2004.ims.cp.Scorm2004Resources;
import dev.jcputney.elearning.parser.input.scorm2004.ims.ss.objective.Scorm2004Objective;
import dev.jcputney.elearning.parser.input.scorm2004.ims.ss.objective.Scorm2004Objectives;
import dev.jcputney.elearning.parser.input.scorm2004.ims.ss.random.RandomizationControls;
import dev.jcputney.elearning.parser.input.scorm2004.ims.ss.types.RandomizationTiming;
import dev.jcputney.elearning.parser.input.scorm2004.ims.ss.sequencing.ControlMode;
import dev.jcputney.elearning.parser.input.scorm2004.ims.ss.sequencing.DeliveryControls;
import dev.jcputney.elearning.parser.input.scorm2004.ims.ss.sequencing.Sequencing;
import dev.jcputney.elearning.parser.input.scorm2004.ims.ss.sequencing.SequencingCollection;
import dev.jcputney.elearning.parser.input.scorm2004.ims.ss.sequencing.SequencingRule;
import dev.jcputney.elearning.parser.input.scorm2004.ims.ss.sequencing.SequencingRules;
import dev.jcputney.elearning.parser.input.scorm2004.SequencingUsageDetector.SequencingLevel;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class SequencingUsageDetectorTest {

  @Test
  void detectReturnsFalseForEmptyManifest() {
    Scorm2004Manifest manifest = new Scorm2004Manifest();

    SequencingUsageDetector.Result result = SequencingUsageDetector.detect(manifest);

    assertFalse(result.hasSequencing());
    assertTrue(result.getIndicators().isEmpty());
  }

  @Test
  void detectNamespaceOnlyManifestDoesNotTriggerSequencing() {
    Scorm2004Manifest manifest = new Scorm2004Manifest();
    manifest.setImsssNamespaceUri("http://www.imsglobal.org/xsd/imsss");

    SequencingUsageDetector.Result result = SequencingUsageDetector.detect(manifest);

    assertFalse(result.hasSequencing());
    assertEquals(SequencingLevel.NONE, result.getLevel());
    assertTrue(result
        .getIndicators()
        .contains(SequencingUsageDetector.SequencingIndicator.IMSSS_NAMESPACE));
  }

  @Test
  void detectRecognizesActivitySequencingComponents() {
    Sequencing sequencing = new Sequencing();
    sequencing.setControlMode(new ControlMode());
    SequencingRules rules = new SequencingRules();
    rules.setPreConditionRules(List.of(new SequencingRule()));
    sequencing.setSequencingRules(rules);
    RandomizationControls randomizationControls = new RandomizationControls();
    randomizationControls.setRandomizationTiming(RandomizationTiming.ONCE);
    sequencing.setRandomizationControls(randomizationControls);

    Scorm2004Item item = new Scorm2004Item();
    item.setIdentifier("item-1");
    item.setIdentifierRef("resource-1");
    item.setVisible(true);
    item.setSequencing(sequencing);

    Scorm2004Organization organization = new Scorm2004Organization();
    organization.setItems(List.of(item));

    Scorm2004Organizations organizations = new Scorm2004Organizations();
    organizations.setOrganizationList(List.of(organization));

    Scorm2004Manifest manifest = new Scorm2004Manifest();
    manifest.setOrganizations(organizations);

    SequencingUsageDetector.Result result = SequencingUsageDetector.detect(manifest);

    assertTrue(result.hasSequencing());
    assertEquals(SequencingLevel.FULL, result.getLevel());
    Set<SequencingUsageDetector.SequencingIndicator> indicators = result.getIndicators();
    assertTrue(indicators.contains(SequencingUsageDetector.SequencingIndicator.ACTIVITY_SEQUENCING));
    assertTrue(indicators.contains(SequencingUsageDetector.SequencingIndicator.SEQUENCING_CONTROL_MODE));
    assertTrue(indicators.contains(SequencingUsageDetector.SequencingIndicator.SEQUENCING_RULES));
    assertTrue(indicators.contains(SequencingUsageDetector.SequencingIndicator.SEQUENCING_RANDOMIZATION));
  }

  @Test
  void detectDoesNotTriggerOnSingleWeakIndicator() {
    Scorm2004Item item = new Scorm2004Item();
    item.setIdentifier("item-weak");
    item.setIdentifierRef("resource-weak");
    item.setVisible(false);

    Scorm2004Organization organization = new Scorm2004Organization();
    organization.setItems(List.of(item));

    Scorm2004Organizations organizations = new Scorm2004Organizations();
    organizations.setOrganizationList(List.of(organization));

    Scorm2004Manifest manifest = new Scorm2004Manifest();
    manifest.setOrganizations(organizations);

    SequencingUsageDetector.Result result = SequencingUsageDetector.detect(manifest);

    assertFalse(result.hasSequencing());
    assertEquals(SequencingLevel.NONE, result.getLevel());
    assertTrue(result
        .getIndicators()
        .contains(SequencingUsageDetector.SequencingIndicator.ITEM_IS_VISIBLE_FALSE));
  }

  @Test
  void detectDoesNotTriggerOnMultipleCommonHeuristics() {
    Scorm2004Item item = new Scorm2004Item();
    item.setIdentifier("weak-combo");
    item.setIdentifierRef(null);
    item.setVisible(false);

    Scorm2004Organization organization = new Scorm2004Organization();
    organization.setItems(List.of(item));

    Scorm2004Organizations organizations = new Scorm2004Organizations();
    organizations.setOrganizationList(List.of(organization));

    Scorm2004Manifest manifest = new Scorm2004Manifest();
    manifest.setOrganizations(organizations);

    SequencingUsageDetector.Result result = SequencingUsageDetector.detect(manifest);

    assertFalse(result.hasSequencing());
    assertEquals(SequencingLevel.NONE, result.getLevel());
    Set<SequencingUsageDetector.SequencingIndicator> indicators = result.getIndicators();
    assertTrue(indicators.contains(SequencingUsageDetector.SequencingIndicator.ITEM_IS_VISIBLE_FALSE));
    assertTrue(indicators.contains(SequencingUsageDetector.SequencingIndicator.ITEM_NO_IDENTIFIER_REF));
  }

  @Test
  void detectRecognizesAdlExtensions() {
    Scorm2004Objective objective = new Scorm2004Objective();
    objective.setObjectiveID("obj1");
    Scorm2004Objectives adlObjectives = new Scorm2004Objectives();
    adlObjectives.setPrimaryObjective(objective);

    Sequencing sequencing = new Sequencing();
    sequencing.setAdlObjectives(adlObjectives);
    sequencing.setRollupConsiderations(new RollupConsiderations());
    sequencing.setConstrainChoiceConsiderations(new ConstrainChoiceConsiderations());

    Scorm2004Item item = new Scorm2004Item();
    item.setIdentifier("item-adl");
    item.setIdentifierRef("resource-adl");
    item.setSequencing(sequencing);

    Scorm2004Resource resource = new Scorm2004Resource();
    resource.setIdentifier("resource-adl");
    resource.setScormType(ScormType.SCO);

    Scorm2004Resources resources = new Scorm2004Resources();
    resources.setResourceList(List.of(resource));

    Scorm2004Organization organization = new Scorm2004Organization();
    organization.setItems(List.of(item));

    Scorm2004Organizations organizations = new Scorm2004Organizations();
    organizations.setOrganizationList(List.of(organization));

    Scorm2004Manifest manifest = new Scorm2004Manifest();
    manifest.setOrganizations(organizations);
    manifest.setResources(resources);

    SequencingUsageDetector.Result result = SequencingUsageDetector.detect(manifest);

    assertTrue(result.hasSequencing());
    assertEquals(SequencingLevel.FULL, result.getLevel());
    Set<SequencingUsageDetector.SequencingIndicator> indicators = result.getIndicators();
    assertTrue(indicators.contains(SequencingUsageDetector.SequencingIndicator.SEQUENCING_ADL_OBJECTIVES));
    assertTrue(indicators.contains(SequencingUsageDetector.SequencingIndicator.SEQUENCING_ROLLUP_CONSIDERATIONS));
    assertTrue(indicators.contains(SequencingUsageDetector.SequencingIndicator.SEQUENCING_CONSTRAIN_CHOICE));
    assertTrue(indicators.contains(SequencingUsageDetector.SequencingIndicator.RESOURCE_SCO));
  }

  @Test
  void detectAccountsForSchemaLocationHints() {
    Scorm2004Manifest manifest = new Scorm2004Manifest();
    manifest.setSchemaLocation("http://www.example.org imsss_v1p0.xsd http://www.example.org adlseq_v1p3.xsd");

    SequencingUsageDetector.Result result = SequencingUsageDetector.detect(manifest);

    assertFalse(result.hasSequencing());
    assertEquals(SequencingLevel.NONE, result.getLevel());
    Set<SequencingUsageDetector.SequencingIndicator> indicators = result.getIndicators();
    assertTrue(indicators.contains(SequencingUsageDetector.SequencingIndicator.SCHEMA_LOCATION_IMSSS));
    assertTrue(indicators.contains(SequencingUsageDetector.SequencingIndicator.SCHEMA_LOCATION_ADLSEQ));
  }

  @Test
  void detectMultipleScoesWithoutSequencingReturnsMultiLevel() {
    Scorm2004Resource resourceOne = new Scorm2004Resource();
    resourceOne.setIdentifier("res-1");
    resourceOne.setScormType(ScormType.SCO);
    resourceOne.setHref("sco1.html");

    Scorm2004Resource resourceTwo = new Scorm2004Resource();
    resourceTwo.setIdentifier("res-2");
    resourceTwo.setScormType(ScormType.SCO);
    resourceTwo.setHref("sco2.html");

    Scorm2004Resources resources = new Scorm2004Resources();
    resources.setResourceList(List.of(resourceOne, resourceTwo));

    Scorm2004Item itemOne = new Scorm2004Item();
    itemOne.setIdentifier("item-1");
    itemOne.setIdentifierRef("res-1");

    Scorm2004Item itemTwo = new Scorm2004Item();
    itemTwo.setIdentifier("item-2");
    itemTwo.setIdentifierRef("res-2");

    Scorm2004Organization organization = new Scorm2004Organization();
    organization.setItems(List.of(itemOne, itemTwo));

    Scorm2004Organizations organizations = new Scorm2004Organizations();
    organizations.setOrganizationList(List.of(organization));

    Scorm2004Manifest manifest = new Scorm2004Manifest();
    manifest.setOrganizations(organizations);
    manifest.setResources(resources);

    SequencingUsageDetector.Result result = SequencingUsageDetector.detect(manifest);

    assertFalse(result.hasSequencing());
    assertEquals(SequencingLevel.MULTI, result.getLevel());
  }

  @Test
  void detectSequencingCollectionRequiresReference() {
    Sequencing sequencing = new Sequencing();
    sequencing.setId("shared-seq");
    sequencing.setControlMode(new ControlMode());

    SequencingCollection collection = new SequencingCollection();
    collection.setSequencingList(List.of(sequencing));

    Scorm2004Manifest manifest = new Scorm2004Manifest();
    manifest.setSequencingCollection(collection);

    SequencingUsageDetector.Result result = SequencingUsageDetector.detect(manifest);

    assertFalse(result.hasSequencing());
    assertEquals(SequencingLevel.NONE, result.getLevel());
    assertTrue(result
        .getIndicators()
        .contains(SequencingUsageDetector.SequencingIndicator.SEQUENCING_COLLECTION));
    assertTrue(result
        .getIndicators()
        .contains(SequencingUsageDetector.SequencingIndicator.SEQUENCING_CONTROL_MODE));
  }

  @Test
  void detectSequencingCollectionReferenceEnablesSequencing() {
    Sequencing sequencing = new Sequencing();
    sequencing.setId("shared-seq");
    sequencing.setControlMode(new ControlMode());

    SequencingCollection collection = new SequencingCollection();
    collection.setSequencingList(List.of(sequencing));

    Sequencing referencing = new Sequencing();
    referencing.setIdRef("shared-seq");

    Scorm2004Item item = new Scorm2004Item();
    item.setIdentifier("item-ref");
    item.setIdentifierRef("res-ref");
    item.setSequencing(referencing);

    Scorm2004Organization organization = new Scorm2004Organization();
    organization.setItems(List.of(item));

    Scorm2004Organizations organizations = new Scorm2004Organizations();
    organizations.setOrganizationList(List.of(organization));

    Scorm2004Manifest manifest = new Scorm2004Manifest();
    manifest.setSequencingCollection(collection);
    manifest.setOrganizations(organizations);

    SequencingUsageDetector.Result result = SequencingUsageDetector.detect(manifest);

    assertFalse(result.hasSequencing());
    assertEquals(SequencingLevel.MINIMAL, result.getLevel());
    Set<SequencingUsageDetector.SequencingIndicator> indicators = result.getIndicators();
    assertTrue(indicators.contains(SequencingUsageDetector.SequencingIndicator.SEQUENCING_IDREF));
    assertTrue(indicators.contains(SequencingUsageDetector.SequencingIndicator.SEQUENCING_CONTROL_MODE));
  }

  @Test
  void detectDeliveryControlsWithNonDefaultValuesCountsAsSequencing() {
    DeliveryControls deliveryControls = new DeliveryControls();
    deliveryControls.setTracked(false);

    Sequencing sequencing = new Sequencing();
    sequencing.setDeliveryControls(deliveryControls);

    Scorm2004Item item = new Scorm2004Item();
    item.setIdentifier("item-delivery");
    item.setIdentifierRef("res-delivery");
    item.setSequencing(sequencing);

    Scorm2004Organization organization = new Scorm2004Organization();
    organization.setItems(List.of(item));

    Scorm2004Organizations organizations = new Scorm2004Organizations();
    organizations.setOrganizationList(List.of(organization));

    Scorm2004Manifest manifest = new Scorm2004Manifest();
    manifest.setOrganizations(organizations);

    SequencingUsageDetector.Result result = SequencingUsageDetector.detect(manifest);

    assertTrue(result.hasSequencing());
    assertEquals(SequencingLevel.FULL, result.getLevel());
    assertTrue(result
        .getIndicators()
        .contains(SequencingUsageDetector.SequencingIndicator.SEQUENCING_DELIVERY_CONTROLS));
  }
}
