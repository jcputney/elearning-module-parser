/*
 * Copyright (c) 2024. Jonathan Putney
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

  @Test
  void testParseCmi5Course_masteryscore_framed() throws ModuleParsingException {
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
  void testParseCmi5Course_masteryscore_responsive() throws ModuleParsingException {
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
  void testParseCmi5Course_multi_au_framed() throws ModuleParsingException {
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

}
