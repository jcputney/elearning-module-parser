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

import dev.jcputney.elearning.parser.exception.ModuleParsingException;
import dev.jcputney.elearning.parser.impl.LocalFileAccess;
import dev.jcputney.elearning.parser.input.aicc.AiccManifest;
import dev.jcputney.elearning.parser.output.metadata.aicc.AiccMetadata;
import org.junit.jupiter.api.Test;

/**
 * Tests for the AICC parser.
 */
public class AiccParserTest {

  @Test
  void testParseAiccCourse() throws ModuleParsingException {
    String modulePath = "src/test/resources/modules/aicc/package";
    AiccParser parser = new AiccParser(new LocalFileAccess(modulePath));
    AiccMetadata metadata = parser.parse();
    assertNotNull(metadata);
    AiccManifest manifest = metadata.getManifest();
    assertNotNull(manifest);
    assertEquals("UniversitySite AICC Testing Tool", manifest.getTitle());
    assertEquals("Descriptive Text", manifest.getDescription());
    assertEquals("default.htm", manifest.getLaunchUrl());
    assertEquals("Profiscience Partners", manifest.getCourse().getCourse().getCourseCreator());
    assertEquals("HTML", manifest.getCourse().getCourse().getCourseSystem());
    assertEquals("1", manifest.getCourse().getCourse().getTotalAus());
    assertEquals("1", manifest.getCourse().getCourseBehavior().getMaxNormal());
    assertEquals(1, manifest.getAssignableUnits().size());
    assertEquals("A1", manifest.getAssignableUnits().get(0).getSystemId());
    assertEquals("C,N", manifest.getAssignableUnits().get(0).getTimeLimitAction());
  }
}
