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

package dev.jcputney.elearning.parser.input.scorm2004.ims.ss.objective;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

/**
 * Tests for the SCORM 2004 objective classes.
 */
public class Scorm2004ObjectiveTest {

  public static final String OBJECTIVE_1 = "com.example.objective1";
  public static final String OBJECTIVE_2 = "com.example.objective2";

  @Test
  void testScorm2004ObjectiveMapping() {
    // Create a Scorm2004ObjectiveMapping instance
    Scorm2004ObjectiveMapping mapping = new Scorm2004ObjectiveMapping();
    mapping.setTargetObjectiveID(OBJECTIVE_1);
    mapping.setReadSatisfiedStatus(true);
    mapping.setReadNormalizedMeasure(true);
    mapping.setWriteSatisfiedStatus(true);
    mapping.setWriteNormalizedMeasure(true);

    // Verify the properties
    assertNotNull(mapping);
    assertEquals(OBJECTIVE_1, mapping.getTargetObjectiveID());
    assertTrue(mapping.isReadSatisfiedStatus());
    assertTrue(mapping.isReadNormalizedMeasure());
    assertTrue(mapping.isWriteSatisfiedStatus());
    assertTrue(mapping.isWriteNormalizedMeasure());

    // Test default constructor
    Scorm2004ObjectiveMapping defaultMapping = new Scorm2004ObjectiveMapping();
    assertNotNull(defaultMapping);

    // Test default values
    Scorm2004ObjectiveMapping defaultValuesMapping = new Scorm2004ObjectiveMapping();
    defaultValuesMapping.setTargetObjectiveID(OBJECTIVE_2);
    assertNotNull(defaultValuesMapping);
    assertEquals(OBJECTIVE_2, defaultValuesMapping.getTargetObjectiveID());
    assertTrue(defaultValuesMapping.isReadSatisfiedStatus()); // Default is true
    assertTrue(defaultValuesMapping.isReadNormalizedMeasure()); // Default is true
    assertFalse(defaultValuesMapping.isWriteSatisfiedStatus()); // Default is false
    assertFalse(defaultValuesMapping.isWriteNormalizedMeasure()); // Default is false
  }

  @Test
  void testScorm2004Objective() {
    // Create a list of mappings
    List<Scorm2004ObjectiveMapping> mapInfo = new ArrayList<>();
    Scorm2004ObjectiveMapping mapping1 = new Scorm2004ObjectiveMapping();
    mapping1.setTargetObjectiveID(OBJECTIVE_1);
    mapping1.setWriteSatisfiedStatus(true);
    mapInfo.add(mapping1);
    Scorm2004ObjectiveMapping mapping2 = new Scorm2004ObjectiveMapping();
    mapping2.setTargetObjectiveID(OBJECTIVE_2);
    mapping2.setWriteNormalizedMeasure(true);
    mapInfo.add(mapping2);

    // Create a Scorm2004Objective instance
    Scorm2004Objective objective = new Scorm2004Objective();
    objective.setObjectiveID("local.objective1");
    objective.setSatisfiedByMeasure(true);
    objective.setMinNormalizedMeasure(0.8);
    objective.setMapInfo(mapInfo);

    // Verify the properties
    assertNotNull(objective);
    assertEquals("local.objective1", objective.getObjectiveID());
    assertTrue(objective.getSatisfiedByMeasure());
    assertEquals(0.8, objective.getMinNormalizedMeasure());
    assertNotNull(objective.getMapInfo());
    assertEquals(2, objective
        .getMapInfo()
        .size());
    assertEquals(OBJECTIVE_1, objective
        .getMapInfo()
        .get(0)
        .getTargetObjectiveID());
    assertTrue(objective
        .getMapInfo()
        .get(0)
        .isWriteSatisfiedStatus());
    assertEquals(OBJECTIVE_2, objective
        .getMapInfo()
        .get(1)
        .getTargetObjectiveID());
    assertTrue(objective
        .getMapInfo()
        .get(1)
        .isWriteNormalizedMeasure());

    // Test default constructor
    Scorm2004Objective defaultObjective = new Scorm2004Objective();
    assertNotNull(defaultObjective);

    // Test default values
    Scorm2004Objective defaultValuesObjective = new Scorm2004Objective();
    defaultValuesObjective.setObjectiveID("local.objective2");
    assertNotNull(defaultValuesObjective);
    assertEquals("local.objective2", defaultValuesObjective.getObjectiveID());
    assertFalse(defaultValuesObjective.getSatisfiedByMeasure()); // Default is false
  }

  @Test
  void testScorm2004Objectives() {
    // Create a primary objective
    Scorm2004Objective primaryObjective = new Scorm2004Objective();
    primaryObjective.setObjectiveID("primary.objective");
    primaryObjective.setMinNormalizedMeasure(0.7);
    primaryObjective.setSatisfiedByMeasure(true);

    // Create a list of objectives
    List<Scorm2004Objective> objectiveList = new ArrayList<>();
    Scorm2004Objective objective1 = new Scorm2004Objective();
    objective1.setObjectiveID("secondary.objective1");
    objectiveList.add(objective1);
    Scorm2004Objective objective2 = new Scorm2004Objective();
    objective2.setObjectiveID("secondary.objective2");
    objectiveList.add(objective2);

    // Create a Scorm2004Objectives instance
    Scorm2004Objectives objectives = new Scorm2004Objectives();
    objectives.setPrimaryObjective(primaryObjective);
    objectives.setObjectiveList(objectiveList);

    // Verify the properties
    assertNotNull(objectives);
    assertNotNull(objectives.getPrimaryObjective());
    assertEquals("primary.objective", objectives
        .getPrimaryObjective()
        .getObjectiveID());
    assertTrue(objectives
        .getPrimaryObjective()
        .getSatisfiedByMeasure());
    assertEquals(0.7, objectives
        .getPrimaryObjective()
        .getMinNormalizedMeasure());

    assertNotNull(objectives.getObjectiveList());
    assertEquals(2, objectives
        .getObjectiveList()
        .size());
    assertEquals("secondary.objective1", objectives
        .getObjectiveList()
        .get(0)
        .getObjectiveID());
    assertEquals("secondary.objective2", objectives
        .getObjectiveList()
        .get(1)
        .getObjectiveID());

    // Test default constructor
    Scorm2004Objectives defaultObjectives = new Scorm2004Objectives();
    assertNotNull(defaultObjectives);
  }
}