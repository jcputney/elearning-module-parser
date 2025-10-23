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

import dev.jcputney.elearning.parser.exception.ManifestParseException;
import dev.jcputney.elearning.parser.exception.ModuleParsingException;
import dev.jcputney.elearning.parser.impl.access.LocalFileAccess;
import dev.jcputney.elearning.parser.input.scorm2004.Scorm2004Manifest;
import dev.jcputney.elearning.parser.parsers.Scorm2004Parser;
import java.io.IOException;
import java.util.List;
import javax.xml.stream.XMLStreamException;
import org.junit.jupiter.api.Test;

/**
 * Tests for parsing SCORM 2004 objectives from manifest files. This test focuses specifically on
 * the objective classes and their parsing.
 */
public class Scorm2004ObjectiveParserTest {

  @Test
  void testParsePostTestObjectives()
      throws XMLStreamException, IOException, ModuleParsingException, ManifestParseException {
    // Parse the SequencingRandomTest_SCORM20043rdEdition manifest
    String modulePath = "src/test/resources/modules/scorm2004/SequencingRandomTest_SCORM20043rdEdition";
    Scorm2004Parser parser = new Scorm2004Parser(new LocalFileAccess(modulePath));
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

    // Verify the post test's objectives
    Scorm2004Objectives objectives = postTestItem
        .getSequencing()
        .getObjectives();
    assertNotNull(objectives);

    // Verify the primary objective
    Scorm2004Objective primaryObjective = objectives.getPrimaryObjective();
    assertNotNull(primaryObjective);
    assertEquals("course_score", primaryObjective.getObjectiveID());

    // Verify the primary objective's mapInfo
    List<Scorm2004ObjectiveMapping> primaryMapInfo = primaryObjective.getMapInfo();
    assertNotNull(primaryMapInfo);
    assertEquals(1, primaryMapInfo.size());

    Scorm2004ObjectiveMapping primaryMapping = primaryMapInfo.get(0);
    assertEquals("com.scorm.golfsamples.sequencing.randomtest.course_score",
        primaryMapping.getTargetObjectiveID());
    assertFalse(primaryMapping.isReadSatisfiedStatus());
    assertTrue(primaryMapping.isReadNormalizedMeasure());

    // Verify the secondary objectives
    List<Scorm2004Objective> objectiveList = objectives.getObjectiveList();
    assertNotNull(objectiveList);
    assertEquals(1, objectiveList.size());

    Scorm2004Objective secondaryObjective = objectiveList.get(0);
    assertEquals("content_completed", secondaryObjective.getObjectiveID());

    // Verify the secondary objective's mapInfo
    List<Scorm2004ObjectiveMapping> secondaryMapInfo = secondaryObjective.getMapInfo();
    assertNotNull(secondaryMapInfo);
    assertEquals(1, secondaryMapInfo.size());

    Scorm2004ObjectiveMapping secondaryMapping = secondaryMapInfo.get(0);
    assertEquals("com.scorm.golfsamples.sequencing.randomtest.content_completed",
        secondaryMapping.getTargetObjectiveID());
    assertTrue(secondaryMapping.isReadSatisfiedStatus());
    assertTrue(secondaryMapping.isReadNormalizedMeasure()); // Default is true when not specified
  }

  @Test
  void testParseContentWrapperObjectives()
      throws XMLStreamException, IOException, ModuleParsingException, ManifestParseException {
    // Parse the SequencingRandomTest_SCORM20043rdEdition manifest
    String modulePath = "src/test/resources/modules/scorm2004/SequencingRandomTest_SCORM20043rdEdition";
    Scorm2004Parser parser = new Scorm2004Parser(new LocalFileAccess(modulePath));
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

    // Verify the content wrapper's objectives
    Scorm2004Objectives objectives = contentWrapper
        .getSequencing()
        .getObjectives();
    assertNotNull(objectives);

    // Verify the primary objective
    Scorm2004Objective primaryObjective = objectives.getPrimaryObjective();
    assertNotNull(primaryObjective);
    assertEquals("content_completed", primaryObjective.getObjectiveID());

    // Verify the primary objective's mapInfo
    List<Scorm2004ObjectiveMapping> mapInfo = primaryObjective.getMapInfo();
    assertNotNull(mapInfo);
    assertEquals(1, mapInfo.size());

    Scorm2004ObjectiveMapping mapping = mapInfo.get(0);
    assertEquals("com.scorm.golfsamples.sequencing.randomtest.content_completed",
        mapping.getTargetObjectiveID());
    assertTrue(mapping.isWriteSatisfiedStatus());
    assertFalse(mapping.isWriteNormalizedMeasure());
  }
}
