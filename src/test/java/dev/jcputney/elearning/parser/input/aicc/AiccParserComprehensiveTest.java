/*
 * qlty-ignore: +qlty:similar-code
 *
 * Copyright (c) 2025. Jonathan Putney
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
 *
 * qlty-ignore: -qlty:similar-code
 */

package dev.jcputney.elearning.parser.input.aicc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import dev.jcputney.elearning.parser.enums.ModuleType;
import dev.jcputney.elearning.parser.exception.ModuleParsingException;
import dev.jcputney.elearning.parser.impl.LocalFileAccess;
import dev.jcputney.elearning.parser.output.metadata.aicc.AiccMetadata;
import dev.jcputney.elearning.parser.parsers.AiccParser;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

/**
 * Comprehensive tests for the AICC parser and related classes. This test suite focuses on testing
 * the parsing of AICC manifest files and the creation of AICC metadata objects.
 */
public class AiccParserComprehensiveTest {


  private static final String BASE_MODULE_PATH = "src/test/resources/modules/aicc";

  /**
   * Tests parsing a standard AICC course from the example package.
   */
  @Test
  void testParseStandardAiccPackage() throws ModuleParsingException {
    String modulePath = BASE_MODULE_PATH + "/package";
    AiccParser parser = new AiccParser(new LocalFileAccess(modulePath));
    AiccMetadata metadata = parser.parse();

    // Verify metadata
    assertNotNull(metadata);
    assertEquals(ModuleType.AICC, metadata.getModuleType());

    // Verify manifest
    AiccManifest manifest = metadata.getManifest();
    assertNotNull(manifest);
    assertEquals("UniversitySite AICC Testing Tool", manifest.getTitle());
    assertEquals("Descriptive Text", manifest.getDescription());
    assertEquals("default.htm", manifest.getLaunchUrl());

    // Verify course
    assertNotNull(manifest.getCourse());
    assertEquals("Profiscience Partners", manifest.getCourse().getCourse().getCourseCreator());
    assertEquals("HTML", manifest.getCourse().getCourse().getCourseSystem());
    assertEquals("1", manifest.getCourse().getCourse().getTotalAus());

    // Verify course behavior
    assertNotNull(manifest.getCourse().getCourseBehavior());
    assertEquals("1", manifest.getCourse().getCourseBehavior().getMaxNormal());

    // Verify assignable units
    assertNotNull(manifest.getAssignableUnits());
    assertEquals(1, manifest.getAssignableUnits().size());
    assertEquals("A1", manifest.getAssignableUnits().get(0).getSystemId());
    assertEquals("C,N", manifest.getAssignableUnits().get(0).getTimeLimitAction());

    // Verify descriptors
    assertNotNull(manifest.getDescriptors());
    assertEquals(1, manifest.getDescriptors().size());
    assertEquals("A1", manifest.getDescriptors().get(0).getSystemId());
    assertEquals("Title", manifest.getDescriptors().get(0).getTitle());
    assertEquals("Description", manifest.getDescriptors().get(0).getDescription());

    // Verify course structure
    assertNotNull(manifest.getCourseStructures());
    assertEquals(1, manifest.getCourseStructures().size());
    assertEquals("ROOT", manifest.getCourseStructures().get(0).getBlock());
    assertEquals("A1", manifest.getCourseStructures().get(0).getMember());
  }

  /**
   * Tests parsing a complete AICC package with all required files.
   */
  @Test
  void testParseCompleteAiccPackage(@TempDir Path tempDir)
      throws IOException, ModuleParsingException {
    // Create a complete AICC package
    createCompleteAiccPackage(tempDir);

    // Create a parser and parse the package
    AiccParser parser = new AiccParser(new LocalFileAccess(tempDir.toString()));
    AiccMetadata metadata = parser.parse();

    // Verify metadata
    assertNotNull(metadata);
    assertEquals(ModuleType.AICC, metadata.getModuleType());

    // Verify manifest
    AiccManifest manifest = metadata.getManifest();
    assertNotNull(manifest);
    assertEquals("Test Course Title", manifest.getTitle());
    assertEquals("Test Description", manifest.getDescription());
    assertEquals("lesson1.html", manifest.getLaunchUrl());
    assertEquals("TEST-001", manifest.getIdentifier());
    assertEquals("1.0", manifest.getVersion());

    // Verify course
    assertNotNull(manifest.getCourse());
    assertEquals("Test Creator", manifest.getCourse().getCourse().getCourseCreator());
    assertEquals("Test System", manifest.getCourse().getCourse().getCourseSystem());
    assertEquals("2", manifest.getCourse().getCourse().getTotalAus());

    // Verify course behavior
    assertNotNull(manifest.getCourse().getCourseBehavior());
    assertEquals("2", manifest.getCourse().getCourseBehavior().getMaxNormal());

    // Verify assignable units
    assertNotNull(manifest.getAssignableUnits());
    assertEquals(2, manifest.getAssignableUnits().size());
    assertEquals("A1", manifest.getAssignableUnits().get(0).getSystemId());
    assertEquals("lesson1.html", manifest.getAssignableUnits().get(0).getFileName());
    assertEquals("A2", manifest.getAssignableUnits().get(1).getSystemId());
    assertEquals("assessment.html", manifest.getAssignableUnits().get(1).getFileName());

    // Verify descriptors
    assertNotNull(manifest.getDescriptors());
    assertEquals(2, manifest.getDescriptors().size());
    assertEquals("A1", manifest.getDescriptors().get(0).getSystemId());
    assertEquals("Lesson 1", manifest.getDescriptors().get(0).getTitle());
    assertEquals("A2", manifest.getDescriptors().get(1).getSystemId());
    assertEquals("Assessment", manifest.getDescriptors().get(1).getTitle());

    // Verify course structures
    assertNotNull(manifest.getCourseStructures());
    assertEquals(3, manifest.getCourseStructures().size());
    assertEquals("ROOT", manifest.getCourseStructures().get(0).getBlock());
    assertEquals("A1", manifest.getCourseStructures().get(0).getMember());
  }

  /**
   * Tests parsing an AICC package with a complex course structure.
   */
  @Test
  void testParseAiccPackageWithComplexStructure(@TempDir Path tempDir)
      throws IOException, ModuleParsingException {
    // Create a course structure file (.cst) with a complex structure
    Path cstPath = tempDir.resolve("course.cst");
    Files.writeString(cstPath,
        """
            "block","member"
            "ROOT","A1"
            "ROOT","BLOCK1"
            "BLOCK1","A2"
            "BLOCK1","BLOCK2"
            "BLOCK2","A3"
            """);

    // Create course file (.crs)
    Path crsPath = tempDir.resolve("course.crs");
    Files.writeString(crsPath,
        """
            [Course]
            Course_Creator=Test Creator
            Course_ID=TEST-001
            Course_System=Test System
            Course_Title=Complex Structure Course
            Level=1
            Max_Fields_CST=2
            Total_AUs=3
            Total_Blocks=2
            Version=1.0
            [Course_Behavior]
            Max_Normal=3
            [Course_Description]
            Complex Structure Course
            """);

    // Create assignable unit file (.au)
    Path auPath = tempDir.resolve("course.au");
    Files.writeString(auPath,
        """
            "system_id","file_name"
            "A1","lesson1.html"
            "A2","lesson2.html"
            "A3","assessment.html"
            """);

    // Create descriptor file (.des)
    Path desPath = tempDir.resolve("course.des");
    Files.writeString(desPath,
        """
            "system_id","title","description"
            "A1","Lesson 1","First lesson"
            "A2","Lesson 2","Second lesson"
            "A3","Assessment","Final assessment"
            """);

    // Create a parser and parse the package
    AiccParser parser = new AiccParser(new LocalFileAccess(tempDir.toString()));
    AiccMetadata metadata = parser.parse();

    // Verify metadata
    assertNotNull(metadata);
    assertEquals(ModuleType.AICC, metadata.getModuleType());

    // Verify manifest
    AiccManifest manifest = metadata.getManifest();
    assertNotNull(manifest);
    assertEquals("Complex Structure Course", manifest.getTitle());

    // Verify assignable units
    assertNotNull(manifest.getAssignableUnits());
    assertEquals(3, manifest.getAssignableUnits().size());

    // Verify course structures
    assertNotNull(manifest.getCourseStructures());
    assertEquals(5, manifest.getCourseStructures().size());

    // Verify the structure hierarchy
    boolean foundRootA1 = false;
    boolean foundRootBlock1 = false;
    boolean foundBlock1A2 = false;
    boolean foundBlock1Block2 = false;
    boolean foundBlock2A3 = false;

    for (CourseStructure cs : manifest.getCourseStructures()) {
      if (cs.getBlock().equals("ROOT") && cs.getMember().equals("A1")) {
        foundRootA1 = true;
      } else if (cs.getBlock().equals("ROOT") && cs.getMember().equals("BLOCK1")) {
        foundRootBlock1 = true;
      } else if (cs.getBlock().equals("BLOCK1") && cs.getMember().equals("A2")) {
        foundBlock1A2 = true;
      } else if (cs.getBlock().equals("BLOCK1") && cs.getMember().equals("BLOCK2")) {
        foundBlock1Block2 = true;
      } else if (cs.getBlock().equals("BLOCK2") && cs.getMember().equals("A3")) {
        foundBlock2A3 = true;
      }
    }

    assertTrue(foundRootA1, "ROOT->A1 structure not found");
    assertTrue(foundRootBlock1, "ROOT->BLOCK1 structure not found");
    assertTrue(foundBlock1A2, "BLOCK1->A2 structure not found");
    assertTrue(foundBlock1Block2, "BLOCK1->BLOCK2 structure not found");
    assertTrue(foundBlock2A3, "BLOCK2->A3 structure not found");
  }

  /**
   * Tests parsing an AICC package with special characters in the content.
   */
  @Test
  void testParseAiccPackageWithSpecialCharacters(@TempDir Path tempDir)
      throws IOException, ModuleParsingException {
    // Create a course file (.crs) with special characters
    Path crsPath = tempDir.resolve("course.crs");
    Files.writeString(crsPath,
        """
            [Course]
            Course_Creator=Test & Creator
            Course_ID=TEST-001
            Course_System=Test System
            Course_Title=Special "Characters" Course
            Level=1
            Max_Fields_CST=2
            Total_AUs=1
            Total_Blocks=0
            Version=1.0
            [Course_Behavior]
            Max_Normal=1
            [Course_Description]
            Course with special characters: & < > " '
            """);

    // Create assignable unit file (.au)
    Path auPath = tempDir.resolve("course.au");
    Files.writeString(auPath,
        """
            "system_id","file_name"
            "A1","lesson & test.html"
            """);

    // Create descriptor file (.des)
    Path desPath = tempDir.resolve("course.des");
    Files.writeString(desPath,
        """
            "system_id","title","description"
            "A1","Lesson & Test","Description with <special> characters"
            """);

    // Create course structure file (.cst)
    Path cstPath = tempDir.resolve("course.cst");
    Files.writeString(cstPath,
        """
            "block","member"
            "ROOT","A1"
            """);

    // Create a parser and parse the package
    AiccParser parser = new AiccParser(new LocalFileAccess(tempDir.toString()));
    AiccMetadata metadata = parser.parse();

    // Verify metadata
    assertNotNull(metadata);
    assertEquals(ModuleType.AICC, metadata.getModuleType());

    // Verify manifest
    AiccManifest manifest = metadata.getManifest();
    assertNotNull(manifest);
    assertEquals("Special \"Characters\" Course", manifest.getTitle());
    assertEquals("Course with special characters", manifest.getDescription());
    assertEquals("lesson & test.html", manifest.getLaunchUrl());

    // Verify course
    assertNotNull(manifest.getCourse());
    assertEquals("Test & Creator", manifest.getCourse().getCourse().getCourseCreator());

    // Verify descriptors
    assertNotNull(manifest.getDescriptors());
    assertEquals(1, manifest.getDescriptors().size());
    assertEquals("Lesson & Test", manifest.getDescriptors().get(0).getTitle());
    assertEquals("Description with <special> characters",
        manifest.getDescriptors().get(0).getDescription());
  }

  /**
   * Tests parsing an AICC package with missing optional fields.
   */
  @Test
  void testParseAiccPackageWithMissingOptionalFields(@TempDir Path tempDir)
      throws IOException, ModuleParsingException {
    // Create a course file (.crs) with minimal required fields
    Path crsPath = tempDir.resolve("course.crs");
    Files.writeString(crsPath,
        """
            [Course]
            Course_Creator=Test Creator
            Course_ID=TEST-001
            Course_System=Test System
            Course_Title=Minimal Course
            Level=1
            Total_AUs=1
            Total_Blocks=0
            Version=1.0
            [Course_Behavior]
            Max_Normal=1
            [Course_Description]
            Minimal Course Description
            """);

    // Create assignable unit file (.au) with minimal required fields
    Path auPath = tempDir.resolve("course.au");
    Files.writeString(auPath,
        """
            "system_id","file_name"
            "A1","lesson.html"
            """);

    // Create descriptor file (.des) with minimal required fields
    Path desPath = tempDir.resolve("course.des");
    Files.writeString(desPath,
        """
            "system_id","title"
            "A1","Minimal Lesson"
            """);

    // Create course structure file (.cst)
    Path cstPath = tempDir.resolve("course.cst");
    Files.writeString(cstPath,
        """
            "block","member"
            "ROOT","A1"
            """);

    // Create a parser and parse the package
    AiccParser parser = new AiccParser(new LocalFileAccess(tempDir.toString()));
    AiccMetadata metadata = parser.parse();

    // Verify metadata
    assertNotNull(metadata);
    assertEquals(ModuleType.AICC, metadata.getModuleType());

    // Verify manifest
    AiccManifest manifest = metadata.getManifest();
    assertNotNull(manifest);
    assertEquals("Minimal Course", manifest.getTitle());
    assertEquals("Minimal Course Description", manifest.getDescription());
    assertEquals("lesson.html", manifest.getLaunchUrl());

    // Verify course
    assertNotNull(manifest.getCourse());
    assertEquals("Test Creator", manifest.getCourse().getCourse().getCourseCreator());
    assertEquals("TEST-001", manifest.getCourse().getCourse().getCourseId());

    // Verify assignable units
    assertNotNull(manifest.getAssignableUnits());
    assertEquals(1, manifest.getAssignableUnits().size());
    assertEquals("A1", manifest.getAssignableUnits().get(0).getSystemId());
    assertEquals("lesson.html", manifest.getAssignableUnits().get(0).getFileName());

    // Verify descriptors
    assertNotNull(manifest.getDescriptors());
    assertEquals(1, manifest.getDescriptors().size());
    assertEquals("A1", manifest.getDescriptors().get(0).getSystemId());
    assertEquals("Minimal Lesson", manifest.getDescriptors().get(0).getTitle());
  }

  /**
   * Tests error handling when parsing an invalid AICC package with malformed files.
   */
  @Test
  void testParseInvalidAiccPackage(@TempDir Path tempDir) throws IOException {
    // Create an invalid course file (.crs)
    Path crsPath = tempDir.resolve("course.crs");
    Files.writeString(crsPath, "This is not a valid INI file");

    // Create a minimal course structure file (.cst)
    Path cstPath = tempDir.resolve("course.cst");
    Files.writeString(cstPath,
        """
            "block","member"
            "ROOT","A1"
            """);

    // Create a minimal assignable unit file (.au)
    Path auPath = tempDir.resolve("course.au");
    Files.writeString(auPath,
        """
            "system_id","file_name"
            "A1","lesson.html"
            """);

    // Create a minimal descriptor file (.des)
    Path desPath = tempDir.resolve("course.des");
    Files.writeString(desPath,
        """
            "system_id","title"
            "A1","Lesson"
            """);

    // Create a parser and try to parse the package with invalid course file
    AiccParser parser = new AiccParser(new LocalFileAccess(tempDir.toString()));
    assertThrows(ModuleParsingException.class, parser::parse);

    // Create a valid course file but invalid CSV file
    Files.writeString(crsPath,
        """
            [Course]
            Course_Creator=Test Creator
            Course_ID=TEST-001
            Course_System=Test System
            Course_Title=Test Course
            Level=1
            Total_AUs=1
            Total_Blocks=0
            Version=1.0
            [Course_Behavior]
            Max_Normal=1
            [Course_Description]
            Test Description
            """);

    Files.writeString(auPath, "This is not a valid CSV file");

    // Create a parser and try to parse the package with invalid CSV file
    parser = new AiccParser(new LocalFileAccess(tempDir.toString()));
    assertThrows(ModuleParsingException.class, parser::parse);
  }

  /**
   * Tests parsing an AICC package with missing required files.
   */
  @Test
  void testParseMissingRequiredFiles(@TempDir Path tempDir) throws IOException {
    // Create only a course file (.crs) without other required files
    Path crsPath = tempDir.resolve("course.crs");
    Files.writeString(crsPath,
        """
            [Course]
            Course_Creator=Test Creator
            Course_ID=TEST-001
            Course_System=Test System
            Course_Title=Test Course Title
            [Course_Behavior]
            Max_Normal=1
            """);

    // Create a parser and try to parse the incomplete package
    AiccParser parser = new AiccParser(new LocalFileAccess(tempDir.toString()));
    assertThrows(ModuleParsingException.class, parser::parse);
  }

  /**
   * Tests parsing an AICC package with empty title or launch URL.
   */
  @Test
  void testParseEmptyTitleOrLaunchUrl(@TempDir Path tempDir) throws IOException {
    // Create a minimal AICC package with empty title
    createMinimalAiccPackage(tempDir, "", "test.html");

    // Create a parser and try to parse the package with empty title
    AiccParser parser = new AiccParser(new LocalFileAccess(tempDir.toString()));
    assertThrows(ModuleParsingException.class, parser::parse);

    // Create a minimal AICC package with empty launch URL
    createMinimalAiccPackage(tempDir, "Test Title", "");

    // Create a parser and try to parse the package with empty launch URL
    parser = new AiccParser(new LocalFileAccess(tempDir.toString()));
    assertThrows(ModuleParsingException.class, parser::parse);
  }

  /**
   * Helper method to create a minimal AICC package with the specified title and launch URL.
   */
  private void createMinimalAiccPackage(Path tempDir, String title, String launchUrl)
      throws IOException {
    // Create course structure file (.cst)
    Path csPath = tempDir.resolve("course.cst");
    Files.writeString(csPath,
        """
            "block","member"
            "ROOT","A1"
            """);

    // Create course file (.crs)
    Path crsPath = tempDir.resolve("course.crs");
    Files.writeString(crsPath,
        """
            [Course]
            Course_Creator=Test Creator
            Course_ID=TEST-001
            Course_System=Test System
            Course_Title=%s
            Level=1
            Total_AUs=1
            [Course_Behavior]
            Max_Normal=1
            [Course_Description]
            Test Description
            """.formatted(title));

    // Create assignable unit file (.au)
    Path auPath = tempDir.resolve("course.au");
    Files.writeString(auPath,
        """
            "system_id","file_name"
            "A1","%s"
            """.formatted(launchUrl));

    // Create descriptor file (.des)
    Path desPath = tempDir.resolve("course.des");
    Files.writeString(desPath,
        """
            "system_id","title","description"
            "A1","Test AU","Test Description"
            """);
  }

  /**
   * Helper method to create a complete AICC package with all required files.
   */
  private void createCompleteAiccPackage(Path tempDir) throws IOException {
    // Create course structure file (.cst)
    Path cstPath = tempDir.resolve("course.cst");
    Files.writeString(cstPath,
        """
            "block","member"
            "ROOT","A1"
            "ROOT","BLOCK1"
            "BLOCK1","A2"
            """);

    // Create course file (.crs)
    Path crsPath = tempDir.resolve("course.crs");
    Files.writeString(crsPath,
        """
            [Course]
            Course_Creator=Test Creator
            Course_ID=TEST-001
            Course_System=Test System
            Course_Title=Test Course Title
            Level=1
            Max_Fields_CST=2
            Total_AUs=2
            Total_Blocks=1
            Version=1.0
            [Course_Behavior]
            Max_Normal=2
            [Course_Description]
            Test Description
            """);

    // Create assignable unit file (.au)
    Path auPath = tempDir.resolve("course.au");
    Files.writeString(auPath,
        """
            "system_id","type","command_line","Max_Time_Allowed","time_limit_action","file_name","max_score","mastery_score","system_vendor","core_vendor","web_launch","au_password"
            "A1","Lesson","","00:30:00","C,N","lesson1.html",100,80,"Test Vendor","Core Vendor","",""
            "A2","Assessment","","00:15:00","C,E","assessment.html",50,40,"Test Vendor","Core Vendor","",""
            """);

    // Create descriptor file (.des)
    Path desPath = tempDir.resolve("course.des");
    Files.writeString(desPath,
        """
            "system_id","developer_id","title","description"
            "A1","DEV-001","Lesson 1","Introduction to the course"
            "A2","DEV-002","Assessment","Final assessment for the course"
            """);
  }
}
