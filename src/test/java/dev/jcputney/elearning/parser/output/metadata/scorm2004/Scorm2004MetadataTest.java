/*
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
 */

package dev.jcputney.elearning.parser.output.metadata.scorm2004;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import dev.jcputney.elearning.parser.enums.ModuleType;
import dev.jcputney.elearning.parser.input.scorm2004.Scorm2004Manifest;
import dev.jcputney.elearning.parser.input.scorm2004.SequencingUsageDetector.SequencingLevel;
import dev.jcputney.elearning.parser.input.scorm2004.ims.cp.Scorm2004CourseMetadata;
import dev.jcputney.elearning.parser.input.scorm2004.ims.cp.Scorm2004Item;
import dev.jcputney.elearning.parser.input.scorm2004.ims.cp.Scorm2004Organization;
import dev.jcputney.elearning.parser.input.scorm2004.ims.cp.Scorm2004Organizations;
import dev.jcputney.elearning.parser.input.scorm2004.ims.ss.objective.Scorm2004Objective;
import dev.jcputney.elearning.parser.input.scorm2004.ims.ss.objective.Scorm2004ObjectiveMapping;
import dev.jcputney.elearning.parser.input.scorm2004.ims.ss.objective.Scorm2004Objectives;
import dev.jcputney.elearning.parser.input.scorm2004.ims.ss.sequencing.ControlMode;
import dev.jcputney.elearning.parser.input.scorm2004.ims.ss.sequencing.Sequencing;
import dev.jcputney.elearning.parser.input.scorm2004.ims.ss.sequencing.SequencingRule;
import dev.jcputney.elearning.parser.input.scorm2004.ims.ss.sequencing.SequencingRules;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

/**
 * Test class for Scorm2004Metadata.
 */
class Scorm2004MetadataTest {

  @Mock
  private Scorm2004Manifest mockManifest;

  @Mock
  private Scorm2004Organizations mockOrganizations;

  @Mock
  private Scorm2004Organization mockOrganization;

  @Mock
  private Scorm2004Item mockItem;

  @Mock
  private Scorm2004Item mockSubItem;

  @Mock
  private Sequencing mockSequencing;

  @Mock
  private Scorm2004Objectives mockObjectives;

  @Mock
  private Scorm2004Objective mockObjective;

  @Mock
  private Scorm2004ObjectiveMapping mockObjectiveMapping;

  @Mock
  private Scorm2004CourseMetadata mockMetadata;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    lenient()
        .when(mockItem.isVisible())
        .thenReturn(true);
    lenient()
        .when(mockItem.getIdentifierRef())
        .thenReturn("itemResource");
    lenient()
        .when(mockSubItem.isVisible())
        .thenReturn(true);
    lenient()
        .when(mockSubItem.getIdentifierRef())
        .thenReturn("subItemResource");
    when(mockSequencing.getControlMode()).thenReturn(new ControlMode());
  }

  @Test
  void testHasSequencing_NullManifest_ReturnsFalse() {
    // Act
    boolean result = Scorm2004Metadata.hasSequencing(null);

    // Assert
    assertFalse(result);
  }

  @Test
  void testHasSequencing_NullOrganizations_ReturnsFalse() {
    // Arrange
    when(mockManifest.getOrganizations()).thenReturn(null);

    // Act
    boolean result = Scorm2004Metadata.hasSequencing(mockManifest);

    // Assert
    assertFalse(result);
    verify(mockManifest).getOrganizations();
  }

  @Test
  void testHasSequencing_EmptyOrganizations_ReturnsFalse() {
    // Arrange
    when(mockManifest.getOrganizations()).thenReturn(mockOrganizations);
    when(mockOrganizations.getOrganizationList()).thenReturn(Collections.emptyList());

    // Act
    boolean result = Scorm2004Metadata.hasSequencing(mockManifest);

    // Assert
    assertFalse(result);
  }

  @Test
  void testHasSequencing_MinimalSequencing_ReturnsFalse() {
    // Arrange
    when(mockManifest.getOrganizations()).thenReturn(mockOrganizations);
    when(mockOrganizations.getOrganizationList()).thenReturn(List.of(mockOrganization));
    when(mockOrganization.getItems()).thenReturn(List.of(mockItem));
    when(mockItem.getSequencing()).thenReturn(mockSequencing);

    // Act
    boolean result = Scorm2004Metadata.hasSequencing(mockManifest);

    // Assert
    assertFalse(result);
  }

  @Test
  void testHasSequencing_ItemWithAdvancedSequencing_ReturnsTrue() {
    // Arrange
    when(mockManifest.getOrganizations()).thenReturn(mockOrganizations);
    when(mockOrganizations.getOrganizationList()).thenReturn(List.of(mockOrganization));
    when(mockOrganization.getItems()).thenReturn(List.of(mockItem));
    when(mockItem.getSequencing()).thenReturn(mockSequencing);
    SequencingRules rules = new SequencingRules();
    rules.setPreConditionRules(List.of(new SequencingRule()));
    when(mockSequencing.getSequencingRules()).thenReturn(rules);

    // Act
    boolean result = Scorm2004Metadata.hasSequencing(mockManifest);

    // Assert
    assertTrue(result);
  }

  @Test
  void testHasSequencing_ManifestNamespaces_ReturnsFalse() {
    // Arrange
    when(mockManifest.getImsssNamespaceUri()).thenReturn("http://www.imsglobal.org/xsd/imsss");
    when(mockManifest.getOrganizations()).thenReturn(null);

    // Act
    boolean result = Scorm2004Metadata.hasSequencing(mockManifest);

    // Assert
    assertFalse(result);
  }

  @Test
  void testHasSequencing_ItemWithoutSequencing_ReturnsFalse() {
    // Arrange
    when(mockManifest.getOrganizations()).thenReturn(mockOrganizations);
    when(mockOrganizations.getOrganizationList()).thenReturn(List.of(mockOrganization));
    when(mockOrganization.getItems()).thenReturn(List.of(mockItem));
    when(mockItem.getSequencing()).thenReturn(null);
    when(mockItem.getItems()).thenReturn(null);

    // Act
    boolean result = Scorm2004Metadata.hasSequencing(mockManifest);

    // Assert
    assertFalse(result);
  }

  @Test
  void testHasSequencing_SubItemWithSequencing_ReturnsTrue() {
    // Arrange
    when(mockManifest.getOrganizations()).thenReturn(mockOrganizations);
    when(mockOrganizations.getOrganizationList()).thenReturn(List.of(mockOrganization));
    when(mockOrganization.getItems()).thenReturn(List.of(mockItem));
    when(mockItem.getSequencing()).thenReturn(null);
    when(mockItem.getItems()).thenReturn(List.of(mockSubItem));
    when(mockSubItem.getSequencing()).thenReturn(mockSequencing);
    SequencingRules rules = new SequencingRules();
    rules.setPreConditionRules(List.of(new SequencingRule()));
    when(mockSequencing.getSequencingRules()).thenReturn(rules);

    // Act
    boolean result = Scorm2004Metadata.hasSequencing(mockManifest);

    // Assert
    assertTrue(result);
  }

  @Test
  void testHasSequencing_MultipleOrganizations_ChecksAll() {
    // Arrange
    Scorm2004Organization mockOrg2 = mock(Scorm2004Organization.class);
    Scorm2004Item mockItem2 = mock(Scorm2004Item.class);

    when(mockManifest.getOrganizations()).thenReturn(mockOrganizations);
    when(mockOrganizations.getOrganizationList()).thenReturn(
        Arrays.asList(mockOrganization, mockOrg2));

    // First org has no sequencing
    when(mockOrganization.getItems()).thenReturn(List.of(mockItem));
    when(mockItem.getSequencing()).thenReturn(null);
    when(mockItem.getItems()).thenReturn(null);

    // Second org has sequencing
    when(mockOrg2.getItems()).thenReturn(List.of(mockItem2));
    when(mockItem2.getSequencing()).thenReturn(mockSequencing);
    SequencingRules rules = new SequencingRules();
    rules.setPreConditionRules(List.of(new SequencingRule()));
    when(mockSequencing.getSequencingRules()).thenReturn(rules);

    // Act
    boolean result = Scorm2004Metadata.hasSequencing(mockManifest);

    // Assert
    assertTrue(result);
  }

  @Test
  void testHasSequencing_NullItemsList_ReturnsFalse() {
    // Arrange
    when(mockManifest.getOrganizations()).thenReturn(mockOrganizations);
    when(mockOrganizations.getOrganizationList()).thenReturn(List.of(mockOrganization));
    when(mockOrganization.getItems()).thenReturn(null);

    // Act
    boolean result = Scorm2004Metadata.hasSequencing(mockManifest);

    // Assert
    assertFalse(result);
  }

  @Test
  void testCreate_WithSequencing_SetsHasSequencingTrue() {
    // Arrange
    when(mockManifest.getOrganizations()).thenReturn(mockOrganizations);
    when(mockOrganizations.getOrganizationList()).thenReturn(List.of(mockOrganization));
    when(mockOrganization.getItems()).thenReturn(List.of(mockItem));
    when(mockItem.getSequencing()).thenReturn(mockSequencing);
    when(mockManifest.getMetadata()).thenReturn(mockMetadata);
    when(mockMetadata.getSchemaVersion()).thenReturn("2004 3rd Edition");
    SequencingRules rules = new SequencingRules();
    rules.setPreConditionRules(List.of(new SequencingRule()));
    when(mockSequencing.getSequencingRules()).thenReturn(rules);

    // Act
    Scorm2004Metadata metadata =
        Scorm2004Metadata.create(mockManifest, false);

    // Assert
    assertNotNull(metadata);
    assertTrue(metadata.isHasSequencing());
    assertEquals(SequencingLevel.FULL, metadata.getSequencingLevel());
    assertEquals(ModuleType.SCORM_2004, metadata.getModuleType());
    List<String> indicatorValues = metadata.getSequencingIndicators();
    assertEquals(List.of("ACTIVITY_SEQUENCING", "SEQUENCING_CONTROL_MODE", "SEQUENCING_RULES"),
        indicatorValues);
    assertEquals(SequencingLevel.FULL.name(), metadata
        .getSequencingLevel()
        .name());
  }

  @Test
  void testCreate_WithoutSequencing_SetsHasSequencingFalse() {
    // Arrange
    when(mockManifest.getOrganizations()).thenReturn(mockOrganizations);
    when(mockOrganizations.getOrganizationList()).thenReturn(List.of(mockOrganization));
    when(mockOrganization.getItems()).thenReturn(List.of(mockItem));
    when(mockItem.getSequencing()).thenReturn(null);
    when(mockItem.getItems()).thenReturn(null);
    when(mockManifest.getMetadata()).thenReturn(null);

    // Act
    Scorm2004Metadata metadata = Scorm2004Metadata.create(mockManifest, true);

    // Assert
    assertNotNull(metadata);
    assertFalse(metadata.isHasSequencing());
    assertEquals(SequencingLevel.NONE, metadata.getSequencingLevel());
    assertTrue(metadata
        .getSequencingIndicators()
        .isEmpty());
    assertEquals(SequencingLevel.NONE.name(), metadata
        .getSequencingLevel()
        .name());
  }

  @Test
  void testCreate_WithMinimalSequencingReportsMinimalLevel() {
    // Arrange
    when(mockManifest.getOrganizations()).thenReturn(mockOrganizations);
    when(mockOrganizations.getOrganizationList()).thenReturn(List.of(mockOrganization));
    when(mockOrganization.getItems()).thenReturn(List.of(mockItem));
    when(mockItem.getSequencing()).thenReturn(mockSequencing);
    when(mockManifest.getMetadata()).thenReturn(mockMetadata);
    when(mockMetadata.getSchemaVersion()).thenReturn("2004 3rd Edition");

    // Act
    Scorm2004Metadata metadata =
        Scorm2004Metadata.create(mockManifest, false);

    // Assert
    assertFalse(metadata.isHasSequencing());
    assertEquals(SequencingLevel.MINIMAL, metadata.getSequencingLevel());
    assertEquals(SequencingLevel.MINIMAL.name(), metadata
        .getSequencingLevel()
        .name());
    List<String> indicatorValues = metadata.getSequencingIndicators();
    assertEquals(List.of("ACTIVITY_SEQUENCING", "SEQUENCING_CONTROL_MODE"), indicatorValues);
  }

  @Test
  void testGetGlobalObjectiveIds_WithValidObjectives_ReturnsIds() {
    // Arrange
    when(mockManifest.getOrganizations()).thenReturn(mockOrganizations);
    when(mockOrganizations.getOrganizationList()).thenReturn(List.of(mockOrganization));
    when(mockOrganization.getItems()).thenReturn(List.of(mockItem));
    when(mockItem.getSequencing()).thenReturn(mockSequencing);
    when(mockSequencing.getObjectives()).thenReturn(mockObjectives);
    when(mockObjectives.getObjectiveList()).thenReturn(List.of(mockObjective));
    when(mockObjective.getMapInfo()).thenReturn(List.of(mockObjectiveMapping));
    when(mockObjectiveMapping.getTargetObjectiveID()).thenReturn("objective1");

    Scorm2004Metadata metadata =
        Scorm2004Metadata.create(mockManifest, false);

    // Act
    Set<String> globalIds = metadata.getGlobalObjectiveIds();

    // Assert
    assertEquals(1, globalIds.size());
    assertTrue(globalIds.contains("objective1"));
  }

  @Test
  void testGetGlobalObjectiveIds_EmptyObjectives_ReturnsEmptySet() {
    // Arrange
    when(mockManifest.getOrganizations()).thenReturn(mockOrganizations);
    when(mockOrganizations.getOrganizationList()).thenReturn(List.of(mockOrganization));
    when(mockOrganization.getItems()).thenReturn(Collections.emptyList());

    Scorm2004Metadata metadata =
        Scorm2004Metadata.create(mockManifest, false);

    // Act
    Set<String> globalIds = metadata.getGlobalObjectiveIds();

    // Assert
    assertTrue(globalIds.isEmpty());
  }

  @Test
  void testHasSequencing_DeepNestedSequencing_ReturnsTrue() {
    // Arrange
    Scorm2004Item mockDeepItem = mock(Scorm2004Item.class);

    when(mockManifest.getOrganizations()).thenReturn(mockOrganizations);
    when(mockOrganizations.getOrganizationList()).thenReturn(List.of(mockOrganization));
    when(mockOrganization.getItems()).thenReturn(List.of(mockItem));
    when(mockItem.getSequencing()).thenReturn(null);
    when(mockItem.getItems()).thenReturn(List.of(mockSubItem));
    when(mockSubItem.getSequencing()).thenReturn(null);
    when(mockSubItem.getItems()).thenReturn(List.of(mockDeepItem));
    when(mockDeepItem.getSequencing()).thenReturn(mockSequencing);
    SequencingRules rules = new SequencingRules();
    rules.setPreConditionRules(List.of(new SequencingRule()));
    when(mockSequencing.getSequencingRules()).thenReturn(rules);

    // Act
    boolean result = Scorm2004Metadata.hasSequencing(mockManifest);

    // Assert
    assertTrue(result);
  }

  @Test
  void testHasSequencing_MixedNullAndNonNullItems_HandlesCorrectly() {
    // Arrange
    when(mockManifest.getOrganizations()).thenReturn(mockOrganizations);
    when(mockOrganizations.getOrganizationList()).thenReturn(List.of(mockOrganization));
    when(mockOrganization.getItems()).thenReturn(Arrays.asList(null, mockItem, null));
    when(mockItem.getSequencing()).thenReturn(mockSequencing);
    SequencingRules mixedRules = new SequencingRules();
    mixedRules.setPreConditionRules(List.of(new SequencingRule()));
    when(mockSequencing.getSequencingRules()).thenReturn(mixedRules);

    // Act
    boolean result = Scorm2004Metadata.hasSequencing(mockManifest);

    // Assert
    assertTrue(result);
  }
}
