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
import dev.jcputney.elearning.parser.impl.access.LocalFileAccess;
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
    assertEquals("Profiscience Partners", manifest
        .getCourse()
        .getCourse()
        .getCourseCreator());
    assertEquals("HTML", manifest
        .getCourse()
        .getCourse()
        .getCourseSystem());
    assertEquals("1", manifest
        .getCourse()
        .getCourse()
        .getTotalAus());
    assertEquals("1", manifest
        .getCourse()
        .getCourseBehavior()
        .getMaxNormal());
    assertEquals(1, manifest
        .getAssignableUnits()
        .size());
    assertEquals("A1", manifest
        .getAssignableUnits()
        .get(0)
        .getSystemId());
    assertEquals("C,N", manifest
        .getAssignableUnits()
        .get(0)
        .getTimeLimitAction());
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
            A minimal AICC course for testing
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
            A1,DEV-001,Test AU,A minimal AICC course for testing
            """);

    AiccParser parser = new AiccParser(new LocalFileAccess(tempDir.toString()));
    AiccMetadata metadata = parser.parse();

    assertNotNull(metadata);
    assertEquals(ModuleType.AICC, metadata.getModuleType());

    AiccManifest manifest = metadata.getManifest();
    assertNotNull(manifest);
    assertEquals("Minimal AICC Course", manifest.getTitle());
    assertEquals("A minimal AICC course for testing", manifest.getDescription());
    assertEquals("Test Creator", manifest
        .getCourse()
        .getCourse()
        .getCourseCreator());
    assertEquals("Test System", manifest
        .getCourse()
        .getCourse()
        .getCourseSystem());
    assertEquals("1", manifest
        .getCourse()
        .getCourse()
        .getTotalAus());
    assertEquals("1", manifest
        .getCourse()
        .getCourseBehavior()
        .getMaxNormal());
    assertEquals(1, manifest
        .getAssignableUnits()
        .size());
  }

  @Test
  void testParse_iniKeyWithoutValue_succeeds(@TempDir Path tempDir)
      throws IOException, ModuleParsingException {
    Path csPath = tempDir.resolve("course.cst");
    Files.writeString(csPath,
        """
            block,member
            ROOT,A1
            """);

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
            Max_Fields_ORT
            
            [COURSE_BEHAVIOR]
            MAX_NORMAL = 1
            
            [COURSE_DESCRIPTION]
            A minimal AICC course for regression testing = Parser should ignore null properties.
            """);

    Path auPath = tempDir.resolve("au.au");
    Files.writeString(auPath,
        """
            System_ID,Command_Line,File_Name,Core_Vendor,Type
            A1,test.html,test.html,Test Vendor,
            """);

    Path desPath = tempDir.resolve("course.des");
    Files.writeString(desPath,
        """
            System_ID,Developer_ID,Title,Description
            A1,DEV-001,Test AU,Test Assignable Unit
            """);

    AiccParser parser = new AiccParser(new LocalFileAccess(tempDir.toString()));
    AiccMetadata metadata = parser.parse();

    assertNotNull(metadata);
    assertEquals("Minimal AICC Course", metadata
        .getManifest()
        .getTitle());
    assertEquals("test.html", metadata
        .getManifest()
        .getLaunchUrl());
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
    assertEquals(3, manifest
        .getAssignableUnits()
        .size());
    assertEquals("A1", manifest
        .getAssignableUnits()
        .get(0)
        .getSystemId());
    assertEquals("A2", manifest
        .getAssignableUnits()
        .get(1)
        .getSystemId());
    assertEquals("A3", manifest
        .getAssignableUnits()
        .get(2)
        .getSystemId());
  }

  /**
   * Tests that the parser correctly handles Course_Description sections with blank lines
   * between paragraphs, which can cause Apache Commons Configuration to return null keys.
   * This test verifies that the parser filters out null keys to prevent Jackson serialization errors.
   */
  @Test
  void testParse_withBlankLinesInCourseDescription_succeeds() throws ModuleParsingException {
    String modulePath = BASE_MODULE_PATH + "/multiline-description";
    AiccParser parser = new AiccParser(new LocalFileAccess(modulePath));
    AiccMetadata metadata = parser.parse();

    assertNotNull(metadata);
    AiccManifest manifest = metadata.getManifest();
    assertNotNull(manifest);
    assertEquals("Achieving Work-Life Balance", manifest.getTitle());

    // Verify that the description was parsed despite blank lines and null keys
    String description = manifest.getCourse().getCourseDescription();
    assertNotNull(description);
    // Verify the description contains the expected content
    assert description.contains("Web APIs") : "Description should contain 'Web APIs'";
    assert description.contains("ASP") : "Description should contain 'ASP' (from ASP.NET)";
  }

  /**
   * Tests that the parser correctly handles .crs files with UTF-8 BOM before the section headers.
   * Some authoring tools (e.g., Notepad on Windows) add a BOM to UTF-8 files, which can cause
   * INI parsing to fail if not properly handled.
   */
  @Test
  void testParse_withUtf8BomInCrsFile_succeeds(@TempDir Path tempDir)
      throws IOException, ModuleParsingException {
    // Create a minimal valid AICC package with UTF-8 BOM in .crs file
    Path csPath = tempDir.resolve("course.cst");
    Files.writeString(csPath,
        """
            block,member
            ROOT,A1
            """);

    // Create course file (.crs) with UTF-8 BOM (0xEF 0xBB 0xBF) before the content
    Path crsPath = tempDir.resolve("course.crs");
    byte[] utf8Bom = new byte[]{(byte) 0xEF, (byte) 0xBB, (byte) 0xBF};
    String crsContent =
        """
            [COURSE]
            COURSE_ID = BOM_Test_Course
            COURSE_TITLE = AICC Course with BOM
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
            A course with UTF-8 BOM for testing encoding handling
            """;
    byte[] contentBytes = crsContent.getBytes(java.nio.charset.StandardCharsets.UTF_8);
    byte[] combined = new byte[utf8Bom.length + contentBytes.length];
    System.arraycopy(utf8Bom, 0, combined, 0, utf8Bom.length);
    System.arraycopy(contentBytes, 0, combined, utf8Bom.length, contentBytes.length);
    Files.write(crsPath, combined);

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
            A1,DEV-001,Test AU,Test course with BOM
            """);

    // This should succeed even with BOM present
    AiccParser parser = new AiccParser(new LocalFileAccess(tempDir.toString()));
    AiccMetadata metadata = parser.parse();

    assertNotNull(metadata);
    assertEquals(ModuleType.AICC, metadata.getModuleType());

    AiccManifest manifest = metadata.getManifest();
    assertNotNull(manifest);
    assertEquals("AICC Course with BOM", manifest.getTitle());
    // Description comes from the .des file, not the .crs file
    assertEquals("Test course with BOM", manifest.getDescription());
    assertEquals("test.html", manifest.getLaunchUrl());

    // Verify that the COURSE_DESCRIPTION section from .crs was also parsed correctly despite BOM
    String courseDescription = manifest.getCourse().getCourseDescription();
    assertNotNull(courseDescription);
    assert courseDescription.contains("UTF-8 BOM") : "Course description should contain 'UTF-8 BOM'";
  }

  /**
   * Tests that the parser correctly handles multi-line descriptions from the descriptor file,
   * similar to Articulate Storyline AICC packages.
   */
  @Test
  void testParse_withMultiLineDescription_succeeds(@TempDir Path tempDir)
      throws IOException, ModuleParsingException {
    // Create a minimal valid AICC package with multi-line description
    Path csPath = tempDir.resolve("course.cst");
    Files.writeString(csPath,
        """
            block,member
            ROOT,A001
            """);

    Path crsPath = tempDir.resolve("course.crs");
    Files.writeString(crsPath,
        """
            [COURSE]
            COURSE_ID = _6Z0T2HT8b8I
            COURSE_TITLE = AICC NO QUIZ
            COURSE_CREATOR = Nikita Reese
            COURSE_SYSTEM = Articulate Storyline
            Level = 1
            Version = 3.0
            Total_AUs = 1
            Total_Blocks = 0
            Max_Fields_CST = 2

            [COURSE_BEHAVIOR]
            MAX_NORMAL = 99

            [COURSE_DESCRIPTION]
            Publishing Standard: AICC
            Slides: 3
            Quiz: NONE
            Results Status: Completed/Failed
            """);

    Path auPath = tempDir.resolve("course.au");
    Files.writeString(auPath,
        """
            System_ID,type,file_name,command_line,Max_Time_Allowed,time_limit_action,Max_Score,Core_Vendor,System_Vendor,Mastery_Score,Web_Launch,AU_Password
            A001,story,index_lms.html,,9999:59:59,C,N,,,Articulate Storyline,,,
            """);

    Path desPath = tempDir.resolve("course.des");
    Files.writeString(desPath,
        """
            system_id,developer_id,title,description
            A001,Articulate_Lesson_ID,AICC NO QUIZ,"Publishing Standard: AICC
            Slides: 3
            Quiz: NONE
            Results Status: Completed/Failed"
            """);

    AiccParser parser = new AiccParser(new LocalFileAccess(tempDir.toString()));
    AiccMetadata metadata = parser.parse();

    assertNotNull(metadata);
    AiccManifest manifest = metadata.getManifest();
    assertNotNull(manifest);

    // Verify the multi-line description is correctly parsed from the .des file
    String expectedDescription = """
        Publishing Standard: AICC
        Slides: 3
        Quiz: NONE
        Results Status: Completed/Failed""";
    assertEquals(expectedDescription, manifest.getDescription());
    assertEquals("AICC NO QUIZ", manifest.getTitle());
    assertEquals("index_lms.html", manifest.getLaunchUrl());
  }
}
