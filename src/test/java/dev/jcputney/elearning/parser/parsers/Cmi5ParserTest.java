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
package dev.jcputney.elearning.parser.parsers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.fasterxml.jackson.databind.JsonNode;
import dev.jcputney.elearning.parser.enums.ModuleType;
import dev.jcputney.elearning.parser.exception.ModuleException;
import dev.jcputney.elearning.parser.impl.access.LocalFileAccess;
import dev.jcputney.elearning.parser.input.cmi5.AU;
import dev.jcputney.elearning.parser.input.cmi5.Block;
import dev.jcputney.elearning.parser.input.cmi5.Cmi5Manifest;
import dev.jcputney.elearning.parser.input.xapi.types.LangString;
import dev.jcputney.elearning.parser.input.cmi5.types.LaunchMethod;
import dev.jcputney.elearning.parser.input.cmi5.types.MoveOn;
import dev.jcputney.elearning.parser.input.xapi.types.TextType;
import dev.jcputney.elearning.parser.output.metadata.cmi5.Cmi5Metadata;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

/**
 * Comprehensive tests for the Cmi5Parser class.
 */
public class Cmi5ParserTest {

  public static final String EN_US = "en-US";
  private static final String BASE_MODULE_PATH = "src/test/resources/modules/cmi5";

  /**
   * Tests parsing a CMI5 course with mastery score in framed style.
   */
  @Test
  void testParseCmi5CourseMasteryscoreFramed() throws ModuleException {
    String modulePath = BASE_MODULE_PATH + "/masteryscore_framed";
    Cmi5Parser parser = new Cmi5Parser(new LocalFileAccess(modulePath));
    Cmi5Metadata metadata = (Cmi5Metadata) parser.parseAndValidate().metadata();
    assertNotNull(metadata);
    assertEquals(ModuleType.CMI5, metadata.getModuleType());

    Cmi5Manifest manifest = metadata.getManifest();
    assertNotNull(manifest);

    assertEquals("Introduction to Geology - Framed Style - Mastery Score", manifest.getTitle());
    assertEquals(
        "This course will introduce you into the basics of geology. This includes subjects such as\nplate tectonics, geological materials and the history of the Earth.",
        manifest.getDescription());
    assertEquals("index.html", manifest
        .getLaunchUrl());
    assertEquals(
        "https://w3id.org/xapi/cmi5/catapult/lts/course/geology-intro-single-au-mastery-score-framed",
        manifest
            .getCourse()
            .getId());
    assertEquals(1, manifest
        .getAssignableUnits()
        .size());
    assertEquals(MoveOn.COMPLETED_OR_PASSED, manifest
        .getAssignableUnits()
        .get(0)
        .getMoveOn());
    assertEquals(LaunchMethod.OWN_WINDOW, manifest
        .getAssignableUnits()
        .get(0)
        .getLaunchMethod());

    // Test metadata extraction
    Map<String, Double> masteryScores = metadata.getMasteryScores();
    assertNotNull(masteryScores);
    assertEquals(1, masteryScores.size());
    assertEquals(0.3, masteryScores.get(
        "https://w3id.org/xapi/cmi5/catapult/lts/course/geology-intro-single-au-mastery-score-framed/1"));

    Map<String, String> moveOnCriteria = metadata.getMoveOnCriteria();
    assertNotNull(moveOnCriteria);
    assertEquals("COMPLETED_OR_PASSED", moveOnCriteria.get(
        "https://w3id.org/xapi/cmi5/catapult/lts/course/geology-intro-single-au-mastery-score-framed/1"));

    Map<String, String> launchMethods = metadata.getLaunchMethods();
    assertNotNull(launchMethods);
    assertEquals("OWN_WINDOW", launchMethods.get(
        "https://w3id.org/xapi/cmi5/catapult/lts/course/geology-intro-single-au-mastery-score-framed/1"));
  }

  /**
   * Tests parsing a CMI5 course with mastery score in responsive style.
   */
  @Test
  void testParseCmi5CourseMasteryscoreResponsive() throws ModuleException {
    String modulePath = BASE_MODULE_PATH + "/masteryscore_responsive";
    Cmi5Parser parser = new Cmi5Parser(new LocalFileAccess(modulePath));
    Cmi5Metadata metadata = (Cmi5Metadata) parser.parseAndValidate().metadata();
    assertNotNull(metadata);
    Cmi5Manifest manifest = metadata.getManifest();
    assertNotNull(manifest);

    assertEquals("Introduction to Geology - Responsive Style - Mastery Score", manifest.getTitle());
    assertEquals(
        "This course will introduce you into the basics of geology. This includes subjects such as\nplate tectonics, geological materials and the history of the Earth.",
        manifest.getDescription());
    assertEquals("index.html", manifest.getLaunchUrl());
    assertEquals(
        "https://w3id.org/xapi/cmi5/catapult/lts/course/geology-intro-single-au-mastery-score-responsive",
        manifest
            .getCourse()
            .getId());
    assertEquals(1, manifest
        .getAssignableUnits()
        .size());
    assertEquals(MoveOn.COMPLETED_OR_PASSED, manifest
        .getAssignableUnits()
        .get(0)
        .getMoveOn());
    assertEquals(LaunchMethod.OWN_WINDOW, manifest
        .getAssignableUnits()
        .get(0)
        .getLaunchMethod());
  }

  /**
   * Tests parsing a CMI5 course with multiple assignable units.
   */
  @Test
  void testParseCmi5CourseMultiAuFramed() throws ModuleException {
    String modulePath = BASE_MODULE_PATH + "/multi_au_framed";
    Cmi5Parser parser = new Cmi5Parser(new LocalFileAccess(modulePath));
    Cmi5Metadata metadata = (Cmi5Metadata) parser.parseAndValidate().metadata();
    assertNotNull(metadata);
    Cmi5Manifest manifest = metadata.getManifest();
    assertNotNull(manifest);

    assertEquals("Introduction to Geology - Multi AU at Root", manifest.getTitle());
    assertEquals(
        "This course will introduce you into the basics of geology. This includes subjects such as\nplate tectonics, geological materials and the history of the Earth.",
        manifest.getDescription());
    assertEquals("index.html?pages=1&complete=launch", manifest.getLaunchUrl());
    assertEquals("https://w3id.org/xapi/cmi5/catapult/lts/course/geology-intro-multi-au-framed",
        manifest
            .getCourse()
            .getId());
    assertEquals(8, manifest
        .getAssignableUnits()
        .size());
    assertEquals(MoveOn.COMPLETED_OR_PASSED, manifest
        .getAssignableUnits()
        .get(0)
        .getMoveOn());
    assertNull(manifest
        .getAssignableUnits()
        .get(0)
        .getLaunchMethod());
    assertEquals(MoveOn.COMPLETED_AND_PASSED, manifest
        .getAssignableUnits()
        .get(7)
        .getMoveOn());
    assertNull(manifest
        .getAssignableUnits()
        .get(7)
        .getLaunchMethod());

    // Test metadata extraction for multiple AUs
    Map<String, Map<String, Object>> auDetails = metadata.getAuDetails();
    assertNotNull(auDetails);
    assertEquals(8, auDetails.size());

    // Check a specific AU's details
    Map<String, Object> firstAuDetails = auDetails.get(
        "https://w3id.org/xapi/cmi5/catapult/lts/course/geology-intro-multi-au-framed/1");
    assertNotNull(firstAuDetails);
    assertEquals("index.html?pages=1&complete=launch", firstAuDetails.get("url"));
    assertEquals("Introduction to Geology", firstAuDetails.get("title"));
    assertEquals(
        "This course will introduce you into the basics of geology. This includes subjects such as\nplate tectonics, geological materials and the history of the Earth.",
        firstAuDetails.get("description"));

    Map<String, String> moveOnCriteria = metadata.getMoveOnCriteria();
    assertNotNull(moveOnCriteria);
    assertEquals(8, moveOnCriteria.size());
    assertEquals("COMPLETED_OR_PASSED", moveOnCriteria.get(
        "https://w3id.org/xapi/cmi5/catapult/lts/course/geology-intro-multi-au-framed/1"));
    assertEquals("COMPLETED_AND_PASSED", moveOnCriteria.get(
        "https://w3id.org/xapi/cmi5/catapult/lts/course/geology-intro-multi-au-framed/quiz"));
  }

  /**
   * Tests parsing a CMI5 course with pre/post tests.
   */
  @Test
  void testParseCmi5CoursePrePostTestFramed() throws ModuleException {
    String modulePath = BASE_MODULE_PATH + "/pre_post_test_framed";
    Cmi5Parser parser = new Cmi5Parser(new LocalFileAccess(modulePath));
    Cmi5Metadata metadata = (Cmi5Metadata) parser.parseAndValidate().metadata();
    assertNotNull(metadata);
    Cmi5Manifest manifest = metadata.getManifest();
    assertNotNull(manifest);

    // Test course properties
    assertEquals("Introduction to Geology - Pre/Post Test", manifest.getTitle());
    assertEquals(
        "This course will introduce you into the basics of geology. This includes subjects such as\nplate tectonics, geological materials and the history of the Earth.",
        manifest.getDescription());

    // Test course title and description (TextType and LangString)
    TextType courseTitle = manifest
        .getCourse()
        .getTitle();
    assertNotNull(courseTitle);
    assertEquals(1, courseTitle
        .getStrings()
        .size());

    LangString courseTitleLangString = courseTitle
        .getStrings()
        .get(0);
    assertNotNull(courseTitleLangString);
    assertEquals("Introduction to Geology - Pre/Post Test", courseTitleLangString.getValue());
    assertEquals(EN_US, courseTitleLangString.getLang());

    TextType courseDescription = manifest
        .getCourse()
        .getDescription();
    assertNotNull(courseDescription);
    assertEquals(1, courseDescription
        .getStrings()
        .size());

    LangString courseDescriptionLangString = courseDescription
        .getStrings()
        .get(0);
    assertNotNull(courseDescriptionLangString);
    assertEquals(
        "This course will introduce you into the basics of geology. This includes subjects such as\nplate tectonics, geological materials and the history of the Earth.",
        courseDescriptionLangString.getValue());
    assertEquals(EN_US, courseDescriptionLangString.getLang());

    // Test blocks
    assertEquals(2, manifest
        .getBlocks()
        .size());

    Block block1 = manifest
        .getBlocks()
        .get(0);
    assertNotNull(block1);
    assertEquals("https://w3id.org/xapi/cmi5/catapult/lts/course/geology-intro-preposttest/block1",
        block1.getId());

    // Test block title and description (TextType and LangString)
    TextType block1Title = block1.getTitle();
    assertNotNull(block1Title);
    assertEquals(1, block1Title
        .getStrings()
        .size());

    LangString block1TitleLangString = block1Title
        .getStrings()
        .get(0);
    assertNotNull(block1TitleLangString);
    assertEquals("Introduction to Geology", block1TitleLangString.getValue());
    assertEquals(EN_US, block1TitleLangString.getLang());

    // Test AUs
    assertEquals(3, block1
        .getAssignableUnits()
        .size());

    AU preTest = block1
        .getAssignableUnits()
        .get(0);
    assertNotNull(preTest);
    assertEquals(
        "https://w3id.org/xapi/cmi5/catapult/lts/course/geology-intro-preposttest/block1/pre",
        preTest.getId());
    assertEquals(MoveOn.PASSED, preTest.getMoveOn());
    assertEquals("pre1.html", preTest.getUrl());

    // Test AU title and description (TextType and LangString)
    TextType preTestTitle = preTest.getTitle();
    assertNotNull(preTestTitle);
    assertEquals(1, preTestTitle
        .getStrings()
        .size());

    LangString preTestTitleLangString = preTestTitle
        .getStrings()
        .get(0);
    assertNotNull(preTestTitleLangString);
    assertEquals("Pre-test", preTestTitleLangString.getValue());
    assertEquals(EN_US, preTestTitleLangString.getLang());
  }

  /**
   * Tests that the parser correctly handles a missing manifest file.
   */
  @Test
  void testParse_withMissingManifestFile_throwsException(@TempDir Path tempDir) {
    // Create an empty directory with no cmi5.xml file
    Cmi5Parser parser = new Cmi5Parser(new LocalFileAccess(tempDir.toString()));
    assertThrows(ModuleException.class, parser::parseOnly);
  }

  /**
   * Tests that the parser correctly handles an invalid manifest file.
   */
  @Test
  void testParse_withInvalidManifestFile_throwsException(@TempDir Path tempDir)
      throws IOException {
    // Create a directory with an invalid cmi5.xml file
    Path manifestPath = tempDir.resolve("cmi5.xml");
    Files.writeString(manifestPath, "<invalid>This is not a valid CMI5 manifest</invalid>");

    Cmi5Parser parser = new Cmi5Parser(new LocalFileAccess(tempDir.toString()));
    assertThrows(ModuleException.class, parser::parseOnly);
  }

  /**
   * Tests that the parser correctly handles a minimal valid CMI5 package.
   */
  @Test
  void testParse_withMinimalValidPackage_succeeds(@TempDir Path tempDir)
      throws IOException, ModuleException {
    // Create a minimal valid CMI5 package
    // Write the manifest to a file
    Path manifestPath = tempDir.resolve("cmi5.xml");
    // language=XML
    String manifestXml = """
        <?xml version="1.0" encoding="UTF-8"?>
        <courseStructure xmlns="https://w3id.org/xapi/profiles/cmi5/v1/CourseStructure.xsd">
          <course id="https://example.com/minimal-course">
            <title>
              <langstring lang="en-US">Minimal CMI5 Course</langstring>
            </title>
          </course>
          <au id="https://example.com/minimal-course/au1" moveOn="Completed">
            <title>
              <langstring lang="en-US">Minimal AU</langstring>
            </title>
            <url>index.html</url>
          </au>
        </courseStructure>""";
    Files.writeString(manifestPath, manifestXml);

    // Create a minimal index.html file
    Path indexPath = tempDir.resolve("index.html");
    Files.writeString(indexPath, "<html><body>Minimal CMI5 Course</body></html>");

    Cmi5Parser parser = new Cmi5Parser(new LocalFileAccess(tempDir.toString()));
    Cmi5Metadata metadata = (Cmi5Metadata) parser.parseAndValidate().metadata();

    assertNotNull(metadata);
    assertEquals(ModuleType.CMI5, metadata.getModuleType());

    Cmi5Manifest parsedManifest = metadata.getManifest();
    assertNotNull(parsedManifest);
    assertEquals("https://example.com/minimal-course", parsedManifest
        .getCourse()
        .getId());
    assertEquals(1, parsedManifest
        .getAssignableUnits()
        .size());
    assertEquals("https://example.com/minimal-course/au1",
        parsedManifest
            .getAssignableUnits()
            .get(0)
            .getId());
    assertEquals(MoveOn.COMPLETED, parsedManifest
        .getAssignableUnits()
        .get(0)
        .getMoveOn());
  }

  /**
   * Tests that parser metadata keeps course-level and AU-level context templates separate, while
   * also exposing entitlement keys and existing cmi5 launch/completion inputs by AU id.
   */
  @Test
  void testParse_withContextTemplatesAndEntitlementKey_exposesScopedLaunchInputs(
      @TempDir Path tempDir)
      throws IOException, ModuleException {
    String auId = "https://example.com/context-course/au1";
    String secondAuId = "https://example.com/context-course/au2";
    String manifestXml = """
        <?xml version="1.0" encoding="UTF-8"?>
        <courseStructure xmlns="https://w3id.org/xapi/profiles/cmi5/v1/CourseStructure.xsd">
          <course id="https://example.com/context-course">
            <title>
              <langstring lang="en-US">Context Course</langstring>
            </title>
            <description>
              <langstring lang="en-US">Course with context template data.</langstring>
            </description>
            <contextTemplate>
              <contextActivities>
                <grouping>
                  <activity id="https://example.com/context-course/grouping"/>
                </grouping>
              </contextActivities>
              <extensions>
                <extension id="https://example.com/extensions/course">course-value</extension>
              </extensions>
            </contextTemplate>
          </course>
          <au id="https://example.com/context-course/au1"
              moveOn="CompletedOrPassed"
              masteryScore="0.75"
              launchMethod="OwnWindow"
              activityType="http://adlnet.gov/expapi/activities/course">
            <title>
              <langstring lang="en-US">Context AU</langstring>
            </title>
            <description>
              <langstring lang="en-US">AU with override context.</langstring>
            </description>
            <url>au1.html</url>
            <launchParameters>{"mode":"review"}</launchParameters>
            <entitlementKey>course-structure-entitlement</entitlementKey>
            <contextTemplate>
              <contextActivities>
                <category>
                  <activity id="https://example.com/context-course/au1/category"/>
                </category>
              </contextActivities>
              <extensions>
                <extension id="https://example.com/extensions/au">au-value</extension>
              </extensions>
            </contextTemplate>
          </au>
          <au id="https://example.com/context-course/au2" moveOn="Completed">
            <title>
              <langstring lang="en-US">Plain AU</langstring>
            </title>
            <description>
              <langstring lang="en-US">AU without context template.</langstring>
            </description>
            <url>au2.html</url>
          </au>
        </courseStructure>""";

    Files.writeString(tempDir.resolve("cmi5.xml"), manifestXml);
    Files.writeString(tempDir.resolve("au1.html"), "<html><body>AU 1</body></html>");
    Files.writeString(tempDir.resolve("au2.html"), "<html><body>AU 2</body></html>");

    Cmi5Parser parser = new Cmi5Parser(new LocalFileAccess(tempDir.toString()));
    Cmi5Metadata metadata = (Cmi5Metadata) parser.parseAndValidate().metadata();

    JsonNode courseContextTemplate = metadata.getCourseContextTemplate();
    assertNotNull(courseContextTemplate);
    assertEquals("https://example.com/context-course/grouping",
        courseContextTemplate.at("/contextActivities/grouping/activity/id").asText());

    Map<String, JsonNode> auContextTemplates = metadata.getAssignableUnitContextTemplates();
    assertEquals(1, auContextTemplates.size());
    assertEquals("https://example.com/context-course/au1/category",
        auContextTemplates
            .get(auId)
            .at("/contextActivities/category/activity/id")
            .asText());

    assertEquals("course-structure-entitlement", metadata
        .getEntitlementKeys()
        .get(auId));
    assertEquals(0.75, metadata
        .getMasteryScores()
        .get(auId));
    assertEquals("COMPLETED_OR_PASSED", metadata
        .getMoveOnCriteria()
        .get(auId));
    assertEquals("COMPLETED", metadata
        .getMoveOnCriteria()
        .get(secondAuId));
    assertEquals("OWN_WINDOW", metadata
        .getLaunchMethods()
        .get(auId));
    assertEquals("http://adlnet.gov/expapi/activities/course", metadata
        .getActivityTypes()
        .get(auId));
    assertEquals("{\"mode\":\"review\"}", metadata
        .getLaunchParameters()
        .get(auId));
    assertEquals("au1.html", metadata
        .getAssignableUnitUrls()
        .get(0));
  }

  /**
   * Tests that the parser correctly handles a CMI5 package with missing required fields.
   */
  @Test
  void testParse_withMissingRequiredFields_throwsException(@TempDir Path tempDir)
      throws IOException {
    // Create a CMI5 package with missing required fields
    // language=XML
    String manifestXml = """
        <?xml version="1.0" encoding="UTF-8"?>
        <courseStructure xmlns="https://w3id.org/xapi/profiles/cmi5/v1/CourseStructure.xsd">
          <course id="https://example.com/minimal-course">
            <!-- Missing title -->
          </course>
          <au id="https://example.com/minimal-course/au1" moveOn="Completed">
            <!-- Missing title -->
            <!-- Missing url -->
          </au>
        </courseStructure>""";
    Path manifestPath = tempDir.resolve("cmi5.xml");
    Files.writeString(manifestPath, manifestXml);

    Cmi5Parser parser = new Cmi5Parser(new LocalFileAccess(tempDir.toString()));
    assertThrows(ModuleException.class, parser::parseOnly);
  }
}
