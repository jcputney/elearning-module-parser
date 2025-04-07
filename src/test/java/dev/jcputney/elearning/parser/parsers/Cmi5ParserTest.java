/* Copyright (c) 2024. Jonathan Putney
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

package dev.jcputney.elearning.parser.parsers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import dev.jcputney.elearning.parser.exception.ModuleParsingException;
import dev.jcputney.elearning.parser.impl.LocalFileAccess;
import dev.jcputney.elearning.parser.input.cmi5.AU;
import dev.jcputney.elearning.parser.input.cmi5.Block;
import dev.jcputney.elearning.parser.input.cmi5.Cmi5Manifest;
import dev.jcputney.elearning.parser.input.cmi5.types.LangString;
import dev.jcputney.elearning.parser.input.cmi5.types.LaunchMethod;
import dev.jcputney.elearning.parser.input.cmi5.types.MoveOn;
import dev.jcputney.elearning.parser.input.cmi5.types.TextType;
import dev.jcputney.elearning.parser.output.metadata.cmi5.Cmi5Metadata;
import org.junit.jupiter.api.Test;

/**
 * Tests for the Cmi5Parser class.
 */
public class Cmi5ParserTest {

  public static final String EN_US = "en-US";

  @Test
  void testParseCmi5CourseMasteryscoreFramed() throws ModuleParsingException {
    String modulePath = "src/test/resources/modules/cmi5/masteryscore_framed";
    Cmi5Parser parser = new Cmi5Parser(new LocalFileAccess(modulePath));
    Cmi5Metadata metadata = parser.parse();
    assertNotNull(metadata);
    Cmi5Manifest manifest = metadata.getManifest();
    assertNotNull(manifest);

    assertEquals("Introduction to Geology - Framed Style - Mastery Score", manifest.getTitle());
    assertEquals(
        "This course will introduce you into the basics of geology. This includes subjects such as\nplate tectonics, geological materials and the history of the Earth.",
        manifest.getDescription());
    assertEquals("index.html", manifest.getLaunchUrl());
    assertEquals(
        "https://w3id.org/xapi/cmi5/catapult/lts/course/geology-intro-single-au-mastery-score-framed",
        manifest.getCourse().getId());
    assertEquals(1, manifest.getAssignableUnits().size());
    assertEquals(MoveOn.COMPLETED_OR_PASSED, manifest.getAssignableUnits().get(0).getMoveOn());
    assertEquals(LaunchMethod.OWN_WINDOW, manifest.getAssignableUnits().get(0).getLaunchMethod());
  }

  @Test
  void testParseCmi5CourseMasteryscoreResponsive() throws ModuleParsingException {
    String modulePath = "src/test/resources/modules/cmi5/masteryscore_responsive";
    Cmi5Parser parser = new Cmi5Parser(new LocalFileAccess(modulePath));
    Cmi5Metadata metadata = parser.parse();
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
        manifest.getCourse().getId());
    assertEquals(1, manifest.getAssignableUnits().size());
    assertEquals(MoveOn.COMPLETED_OR_PASSED, manifest.getAssignableUnits().get(0).getMoveOn());
    assertEquals(LaunchMethod.OWN_WINDOW, manifest.getAssignableUnits().get(0).getLaunchMethod());
  }

  @Test
  void testParseCmi5CourseMultiAuFramed() throws ModuleParsingException {
    String modulePath = "src/test/resources/modules/cmi5/multi_au_framed";
    Cmi5Parser parser = new Cmi5Parser(new LocalFileAccess(modulePath));
    Cmi5Metadata metadata = parser.parse();
    assertNotNull(metadata);
    Cmi5Manifest manifest = metadata.getManifest();
    assertNotNull(manifest);

    assertEquals("Introduction to Geology - Multi AU at Root", manifest.getTitle());
    assertEquals(
        "This course will introduce you into the basics of geology. This includes subjects such as\nplate tectonics, geological materials and the history of the Earth.",
        manifest.getDescription());
    assertEquals("index.html?pages=1&complete=launch", manifest.getLaunchUrl());
    assertEquals("https://w3id.org/xapi/cmi5/catapult/lts/course/geology-intro-multi-au-framed",
        manifest.getCourse().getId());
    assertEquals(8, manifest.getAssignableUnits().size());
    assertEquals(MoveOn.COMPLETED_OR_PASSED, manifest.getAssignableUnits().get(0).getMoveOn());
    assertNull(manifest.getAssignableUnits().get(0).getLaunchMethod());
    assertEquals(MoveOn.COMPLETED_AND_PASSED, manifest.getAssignableUnits().get(7).getMoveOn());
    assertNull(manifest.getAssignableUnits().get(7).getLaunchMethod());
  }

  @Test
  void testParseCmi5CoursePrePostTestFramed() throws ModuleParsingException {
    String modulePath = "src/test/resources/modules/cmi5/pre_post_test_framed";
    Cmi5Parser parser = new Cmi5Parser(new LocalFileAccess(modulePath));
    Cmi5Metadata metadata = parser.parse();
    assertNotNull(metadata);
    Cmi5Manifest manifest = metadata.getManifest();
    assertNotNull(manifest);

    // Test course properties
    assertEquals("Introduction to Geology - Pre/Post Test", manifest.getTitle());
    assertEquals(
        "This course will introduce you into the basics of geology. This includes subjects such as\nplate tectonics, geological materials and the history of the Earth.",
        manifest.getDescription());

    // Test course title and description (TextType and LangString)
    TextType courseTitle = manifest.getCourse().getTitle();
    assertNotNull(courseTitle);
    assertEquals(1, courseTitle.getStrings().size());

    LangString courseTitleLangString = courseTitle.getStrings().get(0);
    assertNotNull(courseTitleLangString);
    assertEquals("Introduction to Geology - Pre/Post Test", courseTitleLangString.getValue());
    assertEquals(EN_US, courseTitleLangString.getLang());

    TextType courseDescription = manifest.getCourse().getDescription();
    assertNotNull(courseDescription);
    assertEquals(1, courseDescription.getStrings().size());

    LangString courseDescriptionLangString = courseDescription.getStrings().get(0);
    assertNotNull(courseDescriptionLangString);
    assertEquals(
        "This course will introduce you into the basics of geology. This includes subjects such as\nplate tectonics, geological materials and the history of the Earth.",
        courseDescriptionLangString.getValue());
    assertEquals(EN_US, courseDescriptionLangString.getLang());

    // Test blocks
    assertEquals(2, manifest.getBlocks().size());

    Block block1 = manifest.getBlocks().get(0);
    assertNotNull(block1);
    assertEquals("https://w3id.org/xapi/cmi5/catapult/lts/course/geology-intro-preposttest/block1",
        block1.getId());

    // Test block title and description (TextType and LangString)
    TextType block1Title = block1.getTitle();
    assertNotNull(block1Title);
    assertEquals(1, block1Title.getStrings().size());

    LangString block1TitleLangString = block1Title.getStrings().get(0);
    assertNotNull(block1TitleLangString);
    assertEquals("Introduction to Geology", block1TitleLangString.getValue());
    assertEquals(EN_US, block1TitleLangString.getLang());

    // Test AUs
    assertEquals(3, block1.getAssignableUnits().size());

    AU preTest = block1.getAssignableUnits().get(0);
    assertNotNull(preTest);
    assertEquals(
        "https://w3id.org/xapi/cmi5/catapult/lts/course/geology-intro-preposttest/block1/pre",
        preTest.getId());
    assertEquals(MoveOn.PASSED, preTest.getMoveOn());
    assertEquals("pre1.html", preTest.getUrl());

    // Test AU title and description (TextType and LangString)
    TextType preTestTitle = preTest.getTitle();
    assertNotNull(preTestTitle);
    assertEquals(1, preTestTitle.getStrings().size());

    LangString preTestTitleLangString = preTestTitle.getStrings().get(0);
    assertNotNull(preTestTitleLangString);
    assertEquals("Pre-test", preTestTitleLangString.getValue());
    assertEquals(EN_US, preTestTitleLangString.getLang());
  }
}
