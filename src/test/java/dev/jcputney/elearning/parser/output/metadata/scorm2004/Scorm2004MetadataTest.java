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

import dev.jcputney.elearning.parser.enums.ModuleType;
import dev.jcputney.elearning.parser.input.scorm2004.Scorm2004Manifest;
import dev.jcputney.elearning.parser.input.scorm2004.ims.cp.Scorm2004CourseMetadata;
import dev.jcputney.elearning.parser.input.scorm2004.ims.cp.Scorm2004Item;
import dev.jcputney.elearning.parser.input.scorm2004.ims.cp.Scorm2004Organization;
import dev.jcputney.elearning.parser.input.scorm2004.ims.cp.Scorm2004Organizations;
import dev.jcputney.elearning.parser.input.scorm2004.ims.ss.objective.Scorm2004Objective;
import dev.jcputney.elearning.parser.input.scorm2004.ims.ss.objective.Scorm2004ObjectiveMapping;
import dev.jcputney.elearning.parser.input.scorm2004.ims.ss.objective.Scorm2004Objectives;
import dev.jcputney.elearning.parser.input.scorm2004.ims.ss.sequencing.Sequencing;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

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
  void testHasSequencing_ItemWithSequencing_ReturnsTrue() {
    // Arrange
    when(mockManifest.getOrganizations()).thenReturn(mockOrganizations);
    when(mockOrganizations.getOrganizationList()).thenReturn(List.of(mockOrganization));
    when(mockOrganization.getItems()).thenReturn(List.of(mockItem));
    when(mockItem.getSequencing()).thenReturn(mockSequencing);

    // Act
    boolean result = Scorm2004Metadata.hasSequencing(mockManifest);

    // Assert
    assertTrue(result);
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
    when(mockOrganizations.getOrganizationList()).thenReturn(Arrays.asList(mockOrganization, mockOrg2));
    
    // First org has no sequencing
    when(mockOrganization.getItems()).thenReturn(List.of(mockItem));
    when(mockItem.getSequencing()).thenReturn(null);
    when(mockItem.getItems()).thenReturn(null);
    
    // Second org has sequencing
    when(mockOrg2.getItems()).thenReturn(List.of(mockItem2));
    when(mockItem2.getSequencing()).thenReturn(mockSequencing);

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

    // Act
    dev.jcputney.elearning.parser.output.metadata.scorm2004.Scorm2004Metadata metadata = 
        dev.jcputney.elearning.parser.output.metadata.scorm2004.Scorm2004Metadata.create(mockManifest, false);

    // Assert
    assertNotNull(metadata);
    assertTrue(metadata.isHasSequencing());
    assertEquals(ModuleType.SCORM_2004, metadata.getModuleType());
    assertEquals(Optional.of(true), metadata.getMetadata("hasSequencing"));
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
    dev.jcputney.elearning.parser.output.metadata.scorm2004.Scorm2004Metadata metadata = 
        dev.jcputney.elearning.parser.output.metadata.scorm2004.Scorm2004Metadata.create(mockManifest, true);

    // Assert
    assertNotNull(metadata);
    assertFalse(metadata.isHasSequencing());
    assertEquals(Optional.of(false), metadata.getMetadata("hasSequencing"));
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

    dev.jcputney.elearning.parser.output.metadata.scorm2004.Scorm2004Metadata metadata = 
        dev.jcputney.elearning.parser.output.metadata.scorm2004.Scorm2004Metadata.create(mockManifest, false);

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

    dev.jcputney.elearning.parser.output.metadata.scorm2004.Scorm2004Metadata metadata = 
        dev.jcputney.elearning.parser.output.metadata.scorm2004.Scorm2004Metadata.create(mockManifest, false);

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

    // Act
    boolean result = Scorm2004Metadata.hasSequencing(mockManifest);

    // Assert
    assertTrue(result);
  }
}