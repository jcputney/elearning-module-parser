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
package dev.jcputney.elearning.parser.input.scorm2004.ims.ss.objective;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import dev.jcputney.elearning.parser.exception.ManifestParseException;
import dev.jcputney.elearning.parser.exception.ModuleParsingException;
import dev.jcputney.elearning.parser.impl.access.InMemoryFileAccess;
import dev.jcputney.elearning.parser.impl.access.LocalFileAccess;
import dev.jcputney.elearning.parser.input.scorm2004.Scorm2004Manifest;
import dev.jcputney.elearning.parser.input.scorm2004.adl.sequencing.ADLObjective;
import dev.jcputney.elearning.parser.input.scorm2004.adl.sequencing.ADLObjectives;
import dev.jcputney.elearning.parser.input.scorm2004.adl.sequencing.MapInfo;
import dev.jcputney.elearning.parser.input.scorm2004.ims.ss.sequencing.Sequencing;
import dev.jcputney.elearning.parser.output.metadata.scorm2004.Scorm2004Metadata;
import dev.jcputney.elearning.parser.parsers.Scorm2004Parser;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
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

  @Test
  void parseSameIdImsPrimaryAndAdlObjectiveKeepsSeparateContainers()
      throws Exception {
    String sequencingXml = """
        <imsss:objectives>
          <imsss:primaryObjective objectiveID="PRIMARYOBJ">
            <imsss:mapInfo targetObjectiveID="gObj-A" readSatisfiedStatus="false"
                            writeSatisfiedStatus="true"/>
          </imsss:primaryObjective>
        </imsss:objectives>
        <adlseq:objectives>
          <adlseq:objective objectiveID="PRIMARYOBJ">
            <adlseq:mapInfo targetObjectiveID="gObj-B" readRawScore="false"
                            writeRawScore="true" writeCompletionStatus="true"/>
          </adlseq:objective>
        </adlseq:objectives>
        """;
    Scorm2004Manifest manifest = parseManifestWithSequencing(sequencingXml);
    Sequencing sequencing = firstSequencing(manifest);

    Scorm2004Objectives objectives = sequencing.getObjectives();
    assertNotNull(objectives);
    Scorm2004Objective primaryObjective = objectives.getPrimaryObjective();
    assertNotNull(primaryObjective);
    assertEquals("PRIMARYOBJ", primaryObjective.getObjectiveID());

    List<Scorm2004ObjectiveMapping> primaryMaps = primaryObjective.getMapInfo();
    assertNotNull(primaryMaps);
    assertEquals(1, primaryMaps.size());
    Scorm2004ObjectiveMapping primaryMap = primaryMaps.get(0);
    assertEquals("gObj-A", primaryMap.getTargetObjectiveID());
    assertFalse(primaryMap.isReadSatisfiedStatus());
    assertTrue(primaryMap.isWriteSatisfiedStatus());

    ADLObjectives adlObjectives = sequencing.getAdlObjectives();
    assertNotNull(adlObjectives);
    ADLObjective adlObjective = onlyElement(adlObjectives.getObjectiveList());
    assertEquals("PRIMARYOBJ", adlObjective.getObjectiveID());

    MapInfo adlMapInfo = onlyElement(adlObjective.getMapInfoList());
    assertEquals("gObj-B", adlMapInfo.getTargetObjectiveID());
    assertFalse(adlMapInfo.isReadRawScore());
    assertTrue(adlMapInfo.isWriteRawScore());
    assertTrue(adlMapInfo.isWriteCompletionStatus());
    assertEquals(Set.of("gObj-A", "gObj-B"), manifest.getGlobalObjectiveIds());

    Scorm2004Metadata metadata = parseMetadata(sequencingXml);
    assertEquals(Set.of("gObj-A", "gObj-B"), metadata.getGlobalObjectiveIds());
  }

  @Test
  void parseAdlMapInfoExposesScoreAndProgressFlagsFromXml()
      throws Exception {
    MapInfo mapInfo = onlyAdlMapInfo("""
        <adlseq:objectives>
          <adlseq:objective objectiveID="PRIMARYOBJ">
            <adlseq:mapInfo targetObjectiveID="gObj-B"
                            readRawScore="false"
                            readMinScore="false"
                            readMaxScore="false"
                            readCompletionStatus="false"
                            readProgressMeasure="false"
                            writeRawScore="true"
                            writeMinScore="true"
                            writeMaxScore="true"
                            writeCompletionStatus="true"
                            writeProgressMeasure="true"/>
          </adlseq:objective>
        </adlseq:objectives>
        """);

    assertEquals("gObj-B", mapInfo.getTargetObjectiveID());
    assertFalse(mapInfo.isReadRawScore());
    assertFalse(mapInfo.isReadMinScore());
    assertFalse(mapInfo.isReadMaxScore());
    assertFalse(mapInfo.isReadCompletionStatus());
    assertFalse(mapInfo.isReadProgressMeasure());
    assertTrue(mapInfo.isWriteRawScore());
    assertTrue(mapInfo.isWriteMinScore());
    assertTrue(mapInfo.isWriteMaxScore());
    assertTrue(mapInfo.isWriteCompletionStatus());
    assertTrue(mapInfo.isWriteProgressMeasure());
  }

  @Test
  void parseAdlMapInfoUsesSchemaDefaultsWhenScoreAndProgressFlagsAreOmitted()
      throws Exception {
    MapInfo mapInfo = onlyAdlMapInfo("""
        <adlseq:objectives>
          <adlseq:objective objectiveID="PRIMARYOBJ">
            <adlseq:mapInfo targetObjectiveID="gObj-B"/>
          </adlseq:objective>
        </adlseq:objectives>
        """);

    assertEquals("gObj-B", mapInfo.getTargetObjectiveID());
    assertTrue(mapInfo.isReadRawScore());
    assertTrue(mapInfo.isReadMinScore());
    assertTrue(mapInfo.isReadMaxScore());
    assertTrue(mapInfo.isReadCompletionStatus());
    assertTrue(mapInfo.isReadProgressMeasure());
    assertFalse(mapInfo.isWriteRawScore());
    assertFalse(mapInfo.isWriteMinScore());
    assertFalse(mapInfo.isWriteMaxScore());
    assertFalse(mapInfo.isWriteCompletionStatus());
    assertFalse(mapInfo.isWriteProgressMeasure());
  }

  @Test
  void parseNonNumericMinNormalizedMeasureThrowsManifestParseException() {
    assertThrows(ManifestParseException.class, () -> parseManifestWithSequencing("""
        <imsss:objectives>
          <imsss:objective objectiveID="OBJ-1">
            <imsss:minNormalizedMeasure>not-a-number</imsss:minNormalizedMeasure>
          </imsss:objective>
        </imsss:objectives>
        """));
  }

  @Test
  void parseEmptyAdlObjectivesKeepsContainerAndSupportsMetadata() throws Exception {
    String objectivesXml = "<adlseq:objectives/>";
    Sequencing sequencing = parseSequencing(objectivesXml);

    ADLObjectives adlObjectives = sequencing.getAdlObjectives();
    assertNotNull(adlObjectives);
    assertTrue(adlObjectives.getObjectiveList() == null
        || adlObjectives.getObjectiveList().isEmpty());

    Scorm2004Metadata metadata = parseMetadata(objectivesXml);
    assertNotNull(metadata);
    assertTrue(metadata.getGlobalObjectiveIds().isEmpty());
  }

  private MapInfo onlyAdlMapInfo(String objectivesXml) throws Exception {
    Sequencing sequencing = parseSequencing(objectivesXml);
    ADLObjectives adlObjectives = sequencing.getAdlObjectives();
    assertNotNull(adlObjectives);

    ADLObjective adlObjective = onlyElement(adlObjectives.getObjectiveList());
    return onlyElement(adlObjective.getMapInfoList());
  }

  private Sequencing parseSequencing(String sequencingChildrenXml) throws Exception {
    return firstSequencing(parseManifestWithSequencing(sequencingChildrenXml));
  }

  private Scorm2004Manifest parseManifestWithSequencing(String sequencingChildrenXml)
      throws Exception {
    Scorm2004Parser parser = new Scorm2004Parser(new InMemoryFileAccess(
        zipWithManifest(buildManifest(sequencingChildrenXml))));
    return parser.parseManifest(Scorm2004Parser.MANIFEST_FILE);
  }

  private Scorm2004Metadata parseMetadata(String sequencingChildrenXml) throws Exception {
    Scorm2004Parser parser = new Scorm2004Parser(new InMemoryFileAccess(
        zipWithManifest(buildManifest(sequencingChildrenXml))));
    return parser.parseOnly();
  }

  private Sequencing firstSequencing(Scorm2004Manifest manifest) {
    return manifest
        .getOrganizations()
        .getDefault()
        .getItems()
        .get(0)
        .getSequencing();
  }

  private String buildManifest(String sequencingChildrenXml) {
    return """
        <?xml version="1.0" encoding="UTF-8"?>
        <manifest identifier="same-id-objective-regression" version="1.3"
                  xmlns="http://www.imsglobal.org/xsd/imscp_v1p1"
                  xmlns:adlcp="http://www.adlnet.org/xsd/adlcp_v1p3"
                  xmlns:adlseq="http://www.adlnet.org/xsd/adlseq_v1p3"
                  xmlns:imsss="http://www.imsglobal.org/xsd/imsss">
          <metadata>
            <schema>ADL SCORM</schema>
            <schemaversion>2004 4th Edition</schemaversion>
          </metadata>
          <organizations default="ORG-1">
            <organization identifier="ORG-1">
              <title>SCORM 2004 Objective Regression</title>
              <item identifier="ITEM-1" identifierref="RES-1">
                <title>Item 1</title>
                <imsss:sequencing>
        %s
                </imsss:sequencing>
              </item>
            </organization>
          </organizations>
          <resources>
            <resource identifier="RES-1" type="webcontent" adlcp:scormType="sco"
                      href="index.html">
              <file href="index.html"/>
            </resource>
          </resources>
        </manifest>
        """.formatted(sequencingChildrenXml.indent(10));
  }

  private byte[] zipWithManifest(String manifestXml) throws IOException {
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    try (ZipOutputStream zos = new ZipOutputStream(baos)) {
      zos.putNextEntry(new ZipEntry(Scorm2004Parser.MANIFEST_FILE));
      zos.write(manifestXml.getBytes(StandardCharsets.UTF_8));
      zos.closeEntry();

      zos.putNextEntry(new ZipEntry("index.html"));
      zos.write("<html><body>Item 1</body></html>".getBytes(StandardCharsets.UTF_8));
      zos.closeEntry();
    }
    return baos.toByteArray();
  }

  private <T> T onlyElement(List<T> list) {
    assertNotNull(list);
    assertEquals(1, list.size());
    return list.get(0);
  }
}
