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
import static org.junit.jupiter.api.Assertions.assertThrows;

import dev.jcputney.elearning.parser.enums.ModuleType;
import dev.jcputney.elearning.parser.exception.ModuleParsingException;
import dev.jcputney.elearning.parser.impl.LocalFileAccess;
import dev.jcputney.elearning.parser.input.aicc.AiccManifest;
import dev.jcputney.elearning.parser.output.metadata.aicc.AiccMetadata;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

/**
 * Comprehensive tests for the AICC parser.
 */
class AiccParserTest {

  private static final String BASE_MODULE_PATH = "src/test/resources/modules/aicc";

  /**
   * Tests parsing a standard AICC course.
   */
  @Test
  void testParseAiccCourse() throws ModuleParsingException {
    String modulePath = BASE_MODULE_PATH + "/package";
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

  /**
   * Tests that the parser correctly handles missing course structure file.
   */
  @Test
  void testParse_withMissingCourseStructureFile_throwsException(@TempDir Path tempDir)
      throws IOException {
    // Create a minimal AICC package with missing course structure file
    Path auPath = tempDir.resolve("au.crs");
    Files.writeString(auPath, "course_id = Test Course\n");

    AiccParser parser = new AiccParser(new LocalFileAccess(tempDir.toString()));
    assertThrows(ModuleParsingException.class, parser::parse);
  }

  /**
   * Tests that the parser correctly handles invalid course structure file.
   */
  @Test
  void testParse_withInvalidCourseStructureFile_throwsException(@TempDir Path tempDir)
      throws IOException {
    // Create a minimal AICC package with invalid course structure file
    Path csPath = tempDir.resolve("course.cst");
    Files.writeString(csPath, "This is not a valid AICC course structure file");

    Path auPath = tempDir.resolve("au.crs");
    Files.writeString(auPath, "course_id = Test Course\n");

    AiccParser parser = new AiccParser(new LocalFileAccess(tempDir.toString()));
    assertThrows(ModuleParsingException.class, parser::parse);
  }

  /**
   * Tests that the parser correctly handles a minimal valid AICC package.
   */
  @Test
  void testParse_withMinimalValidPackage_succeeds(@TempDir Path tempDir) throws IOException,
      ModuleParsingException {
    // Create a minimal valid AICC package
    // Create course structure file (.cst)
    Path csPath = tempDir.resolve("course.cst");
    Files.writeString(csPath,
        """
            block,member
            ROOT,A1
            """);

    // Create course file (.crs)
    Path crsPath = tempDir.resolve("course.crs");
    Files.writeString(crsPath,
        """
            [COURSE]
            COURSE_ID = Minimal_AICC_Course
            COURSE_TITLE = Minimal AICC Course
            COURSE_CREATOR = Test Creator
            COURSE_SYSTEM = Test System
            Level = 1
            Version = 1.0
            Total_AUs = 1
            Total_Blocks = 0
            Max_Fields_CST = 100

            [COURSE_BEHAVIOR]
            MAX_NORMAL = 1

            [COURSE_DESCRIPTION]
            A minimal AICC course for testing = This is a minimal AICC course for testing purposes.
            """);

    // Create assignable unit file (.au)
    Path auPath = tempDir.resolve("au.au");
    Files.writeString(auPath,
        """
            System_ID,Command_Line,File_Name,Core_Vendor,Type
            A1,test.html,test.html,Test Vendor,
            """);

    // Create descriptor file (.des)
    Path desPath = tempDir.resolve("course.des");
    Files.writeString(desPath,
        """
            System_ID,Developer_ID,Title,Description
            A1,DEV-001,Test AU,Test Assignable Unit
            """);

    AiccParser parser = new AiccParser(new LocalFileAccess(tempDir.toString()));
    AiccMetadata metadata = parser.parse();

    assertNotNull(metadata);
    assertEquals(ModuleType.AICC, metadata.getModuleType());

    AiccManifest manifest = metadata.getManifest();
    assertNotNull(manifest);
    assertEquals("Minimal AICC Course", manifest.getTitle());
    assertEquals("A minimal AICC course for testing", manifest.getDescription());
    assertEquals("Test Creator", manifest.getCourse().getCourse().getCourseCreator());
    assertEquals("Test System", manifest.getCourse().getCourse().getCourseSystem());
    assertEquals("1", manifest.getCourse().getCourse().getTotalAus());
    assertEquals("1", manifest.getCourse().getCourseBehavior().getMaxNormal());
    assertEquals(1, manifest.getAssignableUnits().size());
  }

  /**
   * Tests that the parser correctly handles an AICC package with multiple assignable units.
   */
  @Test
  void testParse_withMultipleAssignableUnits_succeeds(@TempDir Path tempDir) throws IOException,
      ModuleParsingException {
    // Create an AICC package with multiple assignable units
    // Create course structure file (.cst)
    Path csPath = tempDir.resolve("course.cst");
    Files.writeString(csPath,
        """
            block,member
            ROOT,A1
            BLOCK1,A2
            BLOCK2,A3
            """);

    // Create course file (.crs)
    Path crsPath = tempDir.resolve("course.crs");
    Files.writeString(crsPath,
        """
            [COURSE]
            COURSE_ID = Multi_AU_AICC_Course
            COURSE_TITLE = Multi AU AICC Course
            COURSE_CREATOR = Test Creator
            COURSE_SYSTEM = Test System
            Level = 1
            Version = 1.0
            Total_AUs = 3
            Total_Blocks = 0
            Max_Fields_CST = 100

            [COURSE_BEHAVIOR]
            MAX_NORMAL = 3
            """);

    // Create assignable unit file (.au)
    Path auPath = tempDir.resolve("au.au");
    Files.writeString(auPath,
        """
            System_ID,Command_Line,File_Name,Core_Vendor,Type
            A1,intro.html,intro.html,Test Vendor,
            A2,main.html,main.html,Test Vendor,
            A3,assessment.html,assessment.html,Test Vendor,
            """);

    // Create descriptor file (.des)
    Path desPath = tempDir.resolve("course.des");
    Files.writeString(desPath,
        """
            System_ID,Developer_ID,Title,Description
            A1,DEV-001,Introduction,Introduction to the course
            A2,DEV-002,Main Content,Main content of the course
            A3,DEV-003,Assessment,Assessment for the course
            """);

    AiccParser parser = new AiccParser(new LocalFileAccess(tempDir.toString()));
    AiccMetadata metadata = parser.parse();

    assertNotNull(metadata);
    AiccManifest manifest = metadata.getManifest();
    assertNotNull(manifest);
    assertEquals("Multi AU AICC Course", manifest.getTitle());
    assertEquals(3, manifest.getAssignableUnits().size());
    assertEquals("A1", manifest.getAssignableUnits().get(0).getSystemId());
    assertEquals("A2", manifest.getAssignableUnits().get(1).getSystemId());
    assertEquals("A3", manifest.getAssignableUnits().get(2).getSystemId());
  }
}
