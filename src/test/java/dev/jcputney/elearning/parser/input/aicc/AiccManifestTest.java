/*
 * qlty-ignore: +qlty:similar-code
 *
 * Copyright (c) 2024-2025. Jonathan Putney
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

import dev.jcputney.elearning.parser.exception.ModuleParsingException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;

/**
 * Comprehensive tests for the AiccManifest class.
 */
public class AiccManifestTest {

  /**
   * Tests the builder and getters for the AiccManifest class.
   */
  @Test
  void testAiccManifestBuilderAndGetters() {
    // Create a course
    AiccCourse.Course course = new AiccCourse.Course();
    course.setCourseCreator("Test Creator");
    course.setCourseId("TEST-001");
    course.setCourseTitle("Test Course");
    course.setCourseSystem("HTML");
    course.setLevel("1");
    course.setMaxFieldsCst("100");
    course.setMaxFieldsOrt("50");
    course.setTotalAus("1");
    course.setTotalBlocks("0");
    course.setVersion("1.0");

    // Create a course behavior
    AiccCourse.CourseBehavior courseBehavior = new AiccCourse.CourseBehavior();
    courseBehavior.setMaxNormal("1");

    // Create a course description
    Map<String, String> courseDescription = new HashMap<>();
    courseDescription.put("Test Description", "This is a test course");

    // Create an AiccCourse
    AiccCourse aiccCourse = new AiccCourse();
    aiccCourse.setCourse(course);
    aiccCourse.setCourseBehavior(courseBehavior);
    aiccCourse.setCourseDescription(courseDescription);

    // Create an assignable unit
    AssignableUnit assignableUnit = new AssignableUnit();
    assignableUnit.setSystemId("A1");
    assignableUnit.setCommandLine("test.html");
    assignableUnit.setFileName("test.html");
    assignableUnit.setCoreVendor("Test Vendor");

    // Create a descriptor
    Descriptor descriptor = new Descriptor();
    descriptor.setSystemId("A1");
    descriptor.setDeveloperId("DEV-001");
    descriptor.setTitle("Test AU");
    descriptor.setDescription("Test Assignable Unit");

    // Associate descriptor with assignable unit (normally done in AiccManifest constructor)
    assignableUnit.setDescriptor(descriptor);

    // Create a course structure
    CourseStructure courseStructure = new CourseStructure("ROOT", "A1");

    // Create lists
    List<AssignableUnit> assignableUnits = new ArrayList<>();
    assignableUnits.add(assignableUnit);

    List<Descriptor> descriptors = new ArrayList<>();
    descriptors.add(descriptor);

    List<CourseStructure> courseStructures = new ArrayList<>();
    courseStructures.add(courseStructure);

    // Create an AiccManifest using the builder
    AiccManifest manifest = new AiccManifest();
    manifest.setCourse(aiccCourse);
    manifest.setAssignableUnits(assignableUnits);
    manifest.setDescriptors(descriptors);
    manifest.setCourseStructures(courseStructures);
    manifest.setLaunchUrl("test.html");

    // Verify the getters
    assertEquals(aiccCourse, manifest.getCourse());
    assertEquals(assignableUnits, manifest.getAssignableUnits());
    assertEquals(descriptors, manifest.getDescriptors());
    assertEquals(courseStructures, manifest.getCourseStructures());

    // Verify the PackageManifest interface methods
    assertEquals("Test Course", manifest.getTitle());
    // Description now comes from the descriptor (.des file), not the course description (.crs file)
    assertEquals("Test Assignable Unit", manifest.getDescription());
    assertEquals("test.html", manifest.getLaunchUrl());
    assertEquals("TEST-001", manifest.getIdentifier());
    assertEquals("1.0", manifest.getVersion());
    assertEquals(Duration.ZERO, manifest.getDuration());
  }

  /**
   * Tests the constructor that sets up relationships between descriptors and assignable units.
   */
  @Test
  void testAiccManifestConstructor() throws ModuleParsingException {
    // Create a course
    AiccCourse.Course course = new AiccCourse.Course();
    course.setCourseCreator("Test Creator");
    course.setCourseId("TEST-001");
    course.setCourseTitle("Test Course");
    course.setCourseSystem("HTML");
    course.setLevel("1");
    course.setMaxFieldsCst("100");
    course.setMaxFieldsOrt("50");
    course.setTotalAus("1");
    course.setTotalBlocks("0");
    course.setVersion("1.0");

    // Create a course behavior
    AiccCourse.CourseBehavior courseBehavior = new AiccCourse.CourseBehavior();
    courseBehavior.setMaxNormal("1");

    // Create a course description
    Map<String, String> courseDescription = new HashMap<>();
    courseDescription.put("Test Description", "This is a test course");

    // Create an AiccCourse
    AiccCourse aiccCourse = new AiccCourse();
    aiccCourse.setCourse(course);
    aiccCourse.setCourseBehavior(courseBehavior);
    aiccCourse.setCourseDescription(courseDescription);

    // Create an assignable unit
    AssignableUnit assignableUnit = new AssignableUnit();
    assignableUnit.setSystemId("A1");
    assignableUnit.setCommandLine("test.html");
    assignableUnit.setFileName("test.html");
    assignableUnit.setCoreVendor("Test Vendor");

    // Create a descriptor
    Descriptor descriptor = new Descriptor();
    descriptor.setSystemId("A1");
    descriptor.setDeveloperId("DEV-001");
    descriptor.setTitle("Test AU");
    descriptor.setDescription("Test Assignable Unit");

    // Create a course structure
    CourseStructure courseStructure = new CourseStructure("ROOT", "A1");

    // Create lists
    List<AssignableUnit> assignableUnits = new ArrayList<>();
    assignableUnits.add(assignableUnit);

    List<Descriptor> descriptors = new ArrayList<>();
    descriptors.add(descriptor);

    List<CourseStructure> courseStructures = new ArrayList<>();
    courseStructures.add(courseStructure);

    // Create an AiccManifest using the constructor
    AiccManifest manifest = new AiccManifest(aiccCourse, assignableUnits, descriptors,
        courseStructures);

    // Verify the descriptor was set on the assignable unit
    assertNotNull(assignableUnit.getDescriptor());
    assertEquals(descriptor, assignableUnit.getDescriptor());

    // Verify the launch URL was set correctly
    assertEquals("test.html", manifest.getLaunchUrl());
  }

  /**
   * Tests the constructor with a non-ROOT course structure.
   */
  @Test
  void testAiccManifestConstructor_withNonRootCourseStructure() throws ModuleParsingException {
    // Create a course
    AiccCourse.Course course = new AiccCourse.Course();
    course.setCourseCreator("Test Creator");
    course.setCourseId("TEST-001");
    course.setCourseTitle("Test Course");
    course.setCourseSystem("HTML");
    course.setLevel("1");
    course.setMaxFieldsCst("100");
    course.setMaxFieldsOrt("50");
    course.setTotalAus("1");
    course.setTotalBlocks("0");
    course.setVersion("1.0");

    // Create a course behavior
    AiccCourse.CourseBehavior courseBehavior = new AiccCourse.CourseBehavior();
    courseBehavior.setMaxNormal("1");

    // Create an AiccCourse
    AiccCourse aiccCourse = new AiccCourse();
    aiccCourse.setCourse(course);
    aiccCourse.setCourseBehavior(courseBehavior);

    // Create an assignable unit
    AssignableUnit assignableUnit = new AssignableUnit();
    assignableUnit.setSystemId("A1");
    assignableUnit.setCommandLine("test.html");
    assignableUnit.setFileName("test.html");
    assignableUnit.setCoreVendor("Test Vendor");

    // Create a descriptor
    Descriptor descriptor = new Descriptor();
    descriptor.setSystemId("A1");
    descriptor.setDeveloperId("DEV-001");
    descriptor.setTitle("Test AU");
    descriptor.setDescription("Test Assignable Unit");

    // Create a course structure (not ROOT)
    CourseStructure courseStructure = new CourseStructure("BLOCK1", "A1");

    // Create lists
    List<AssignableUnit> assignableUnits = new ArrayList<>();
    assignableUnits.add(assignableUnit);

    List<Descriptor> descriptors = new ArrayList<>();
    descriptors.add(descriptor);

    List<CourseStructure> courseStructures = new ArrayList<>();
    courseStructures.add(courseStructure);

    // Create an AiccManifest using the constructor
    AiccManifest manifest = new AiccManifest(aiccCourse, assignableUnits, descriptors,
        courseStructures);

    // Verify the launch URL was set correctly (should use the first course structure)
    assertEquals("test.html", manifest.getLaunchUrl());
  }

  /**
   * Tests the constructor with a missing root assignable unit.
   */
  @Test
  void testAiccManifestConstructor_withMissingRootAssignableUnit() {
    // Create a course
    AiccCourse.Course course = new AiccCourse.Course();
    course.setCourseCreator("Test Creator");
    course.setCourseId("TEST-001");
    course.setCourseTitle("Test Course");
    course.setCourseSystem("HTML");
    course.setLevel("1");
    course.setMaxFieldsCst("100");
    course.setMaxFieldsOrt("50");
    course.setTotalAus("1");
    course.setTotalBlocks("0");
    course.setVersion("1.0");

    // Create a course behavior
    AiccCourse.CourseBehavior courseBehavior = new AiccCourse.CourseBehavior();
    courseBehavior.setMaxNormal("1");

    // Create an AiccCourse
    AiccCourse aiccCourse = new AiccCourse();
    aiccCourse.setCourse(course);
    aiccCourse.setCourseBehavior(courseBehavior);

    // Create an assignable unit with a different ID
    AssignableUnit assignableUnit = new AssignableUnit();
    assignableUnit.setSystemId("A2");
    assignableUnit.setCommandLine("test.html");
    assignableUnit.setFileName("test.html");
    assignableUnit.setCoreVendor("Test Vendor");

    // Create a descriptor
    Descriptor descriptor = new Descriptor();
    descriptor.setSystemId("A2");
    descriptor.setDeveloperId("DEV-001");
    descriptor.setTitle("Test AU");
    descriptor.setDescription("Test Assignable Unit");

    // Create a course structure that references a non-existent assignable unit
    CourseStructure courseStructure = new CourseStructure("ROOT", "A1");

    // Create lists
    List<AssignableUnit> assignableUnits = new ArrayList<>();
    assignableUnits.add(assignableUnit);

    List<Descriptor> descriptors = new ArrayList<>();
    descriptors.add(descriptor);

    List<CourseStructure> courseStructures = new ArrayList<>();
    courseStructures.add(courseStructure);

    // Verify that the constructor throws an exception
    ModuleParsingException exception = assertThrows(ModuleParsingException.class,
        () -> new AiccManifest(aiccCourse, assignableUnits, descriptors, courseStructures));

    // Verify the exception message
    assertTrue(exception
        .getMessage()
        .contains("No assignable unit found with ID: A1"));
  }

  /**
   * Tests the constructor with an empty member in the course structure.
   */
  @Test
  void testAiccManifestConstructor_withEmptyMember() {
    // Create a course
    AiccCourse.Course course = new AiccCourse.Course();
    course.setCourseCreator("Test Creator");
    course.setCourseId("TEST-001");
    course.setCourseTitle("Test Course");
    course.setCourseSystem("HTML");
    course.setLevel("1");
    course.setMaxFieldsCst("100");
    course.setMaxFieldsOrt("50");
    course.setTotalAus("1");
    course.setTotalBlocks("0");
    course.setVersion("1.0");

    // Create a course behavior
    AiccCourse.CourseBehavior courseBehavior = new AiccCourse.CourseBehavior();
    courseBehavior.setMaxNormal("1");

    // Create an AiccCourse
    AiccCourse aiccCourse = new AiccCourse();
    aiccCourse.setCourse(course);
    aiccCourse.setCourseBehavior(courseBehavior);

    // Create an assignable unit
    AssignableUnit assignableUnit = new AssignableUnit();
    assignableUnit.setSystemId("A1");
    assignableUnit.setCommandLine("test.html");
    assignableUnit.setFileName("test.html");
    assignableUnit.setCoreVendor("Test Vendor");

    // Create a descriptor
    Descriptor descriptor = new Descriptor();
    descriptor.setSystemId("A1");
    descriptor.setDeveloperId("DEV-001");
    descriptor.setTitle("Test AU");
    descriptor.setDescription("Test Assignable Unit");

    // Create a course structure with an empty member
    CourseStructure courseStructure = new CourseStructure("ROOT", "");

    // Create lists
    List<AssignableUnit> assignableUnits = new ArrayList<>();
    assignableUnits.add(assignableUnit);

    List<Descriptor> descriptors = new ArrayList<>();
    descriptors.add(descriptor);

    List<CourseStructure> courseStructures = new ArrayList<>();
    courseStructures.add(courseStructure);

    // Verify that the constructor throws an exception
    ModuleParsingException exception = assertThrows(ModuleParsingException.class,
        () -> new AiccManifest(aiccCourse, assignableUnits, descriptors, courseStructures));

    // Verify the exception message
    assertEquals("No root assignable unit found.", exception.getMessage());
  }

  /**
   * Tests the constructor with a null member in the course structure.
   */
  @Test
  void testAiccManifestConstructor_withNullMember() {
    // Create a course
    AiccCourse.Course course = new AiccCourse.Course();
    course.setCourseCreator("Test Creator");
    course.setCourseId("TEST-001");
    course.setCourseTitle("Test Course");
    course.setCourseSystem("HTML");
    course.setLevel("1");
    course.setMaxFieldsCst("100");
    course.setMaxFieldsOrt("50");
    course.setTotalAus("1");
    course.setTotalBlocks("0");
    course.setVersion("1.0");

    // Create a course behavior
    AiccCourse.CourseBehavior courseBehavior = new AiccCourse.CourseBehavior();
    courseBehavior.setMaxNormal("1");

    // Create an AiccCourse
    AiccCourse aiccCourse = new AiccCourse();
    aiccCourse.setCourse(course);
    aiccCourse.setCourseBehavior(courseBehavior);

    // Create an assignable unit
    AssignableUnit assignableUnit = new AssignableUnit();
    assignableUnit.setSystemId("A1");
    assignableUnit.setCommandLine("test.html");
    assignableUnit.setFileName("test.html");
    assignableUnit.setCoreVendor("Test Vendor");

    // Create a descriptor
    Descriptor descriptor = new Descriptor();
    descriptor.setSystemId("A1");
    descriptor.setDeveloperId("DEV-001");
    descriptor.setTitle("Test AU");
    descriptor.setDescription("Test Assignable Unit");

    // Create a course structure with a null member
    CourseStructure courseStructure = new CourseStructure("ROOT", null);

    // Create lists
    List<AssignableUnit> assignableUnits = new ArrayList<>();
    assignableUnits.add(assignableUnit);

    List<Descriptor> descriptors = new ArrayList<>();
    descriptors.add(descriptor);

    List<CourseStructure> courseStructures = new ArrayList<>();
    courseStructures.add(courseStructure);

    // Verify that the constructor throws an exception
    ModuleParsingException exception = assertThrows(ModuleParsingException.class,
        () -> new AiccManifest(aiccCourse, assignableUnits, descriptors, courseStructures));

    // Verify the exception message
    assertEquals("No root assignable unit found.", exception.getMessage());
  }
}