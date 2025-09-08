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
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Test;

/**
 * Comprehensive tests for the AiccCourse class.
 */
public class AiccCourseTest {

  /**
   * Tests the builder and getters for the AiccCourse class.
   */
  @Test
  void testAiccCourseBuilderAndGetters() {
    // Create a course using the builder
    AiccCourse.Course course = new AiccCourse.Course();
    course.setCourseCreator("Test Creator");
    course.setCourseId("TEST-001");
    course.setCourseTitle("Test Course");
    course.setCourseSystem("HTML");
    course.setLevel("1");
    course.setMaxFieldsCst("100");
    course.setMaxFieldsOrt("50");
    course.setTotalAus("5");
    course.setTotalBlocks("3");
    course.setVersion("1.0");
    course.setTotalComplexObj("0");
    course.setTotalObjectives("10");

    // Create a course behavior using the builder
    AiccCourse.CourseBehavior courseBehavior = new AiccCourse.CourseBehavior();
    courseBehavior.setMaxNormal("5");

    // Create a course description map
    Map<String, String> courseDescription = new HashMap<>();
    courseDescription.put("Test Description", "This is a test course");

    // Create an AiccCourse using the builder
    AiccCourse aiccCourse = new AiccCourse();
    aiccCourse.setCourse(course);
    aiccCourse.setCourseBehavior(courseBehavior);
    aiccCourse.setCourseDescription(courseDescription);

    // Verify the getters
    assertEquals(course, aiccCourse.getCourse());
    assertEquals(courseBehavior, aiccCourse.getCourseBehavior());

    // Verify the course getters
    assertEquals("Test Creator", aiccCourse
        .getCourse()
        .getCourseCreator());
    assertEquals("TEST-001", aiccCourse
        .getCourse()
        .getCourseId());
    assertEquals("Test Course", aiccCourse
        .getCourse()
        .getCourseTitle());
    assertEquals("HTML", aiccCourse
        .getCourse()
        .getCourseSystem());
    assertEquals("1", aiccCourse
        .getCourse()
        .getLevel());
    assertEquals("100", aiccCourse
        .getCourse()
        .getMaxFieldsCst());
    assertEquals("50", aiccCourse
        .getCourse()
        .getMaxFieldsOrt());
    assertEquals("5", aiccCourse
        .getCourse()
        .getTotalAus());
    assertEquals("3", aiccCourse
        .getCourse()
        .getTotalBlocks());
    assertEquals("1.0", aiccCourse
        .getCourse()
        .getVersion());
    assertEquals("0", aiccCourse
        .getCourse()
        .getTotalComplexObj());
    assertEquals("10", aiccCourse
        .getCourse()
        .getTotalObjectives());

    // Verify the course behavior getters
    assertEquals("5", aiccCourse
        .getCourseBehavior()
        .getMaxNormal());

    // Verify the getCourseDescription method
    assertEquals("Test Description", aiccCourse.getCourseDescription());
  }

  /**
   * Tests the getCourseDescription method with an empty description map.
   */
  @Test
  void testGetCourseDescription_withEmptyMap_returnsNull() {
    // Create a course using the builder
    AiccCourse.Course course = new AiccCourse.Course();
    course.setCourseCreator("Test Creator");
    course.setCourseId("TEST-001");
    course.setCourseTitle("Test Course");
    course.setCourseSystem("HTML");
    course.setLevel("1");
    course.setMaxFieldsCst("100");
    course.setMaxFieldsOrt("50");
    course.setTotalAus("5");
    course.setTotalBlocks("3");
    course.setVersion("1.0");

    // Create a course behavior using the builder
    AiccCourse.CourseBehavior courseBehavior = new AiccCourse.CourseBehavior();
    courseBehavior.setMaxNormal("5");

    // Create an empty course description map
    Map<String, String> courseDescription = new HashMap<>();

    // Create an AiccCourse using the builder
    AiccCourse aiccCourse = new AiccCourse();
    aiccCourse.setCourse(course);
    aiccCourse.setCourseBehavior(courseBehavior);
    aiccCourse.setCourseDescription(courseDescription);

    // Verify the getCourseDescription method returns null for an empty map
    assertNull(aiccCourse.getCourseDescription());
  }

  /**
   * Tests the getCourseDescription method with a null description map.
   */
  @Test
  void testGetCourseDescription_withNullMap_returnsNull() {
    // Create a course using the builder
    AiccCourse.Course course = new AiccCourse.Course();
    course.setCourseCreator("Test Creator");
    course.setCourseId("TEST-001");
    course.setCourseTitle("Test Course");
    course.setCourseSystem("HTML");
    course.setLevel("1");
    course.setMaxFieldsCst("100");
    course.setMaxFieldsOrt("50");
    course.setTotalAus("5");
    course.setTotalBlocks("3");
    course.setVersion("1.0");

    // Create a course behavior using the builder
    AiccCourse.CourseBehavior courseBehavior = new AiccCourse.CourseBehavior();
    courseBehavior.setMaxNormal("5");

    // Create an AiccCourse using the builder with a null course description
    AiccCourse aiccCourse = new AiccCourse();
    aiccCourse.setCourse(course);
    aiccCourse.setCourseBehavior(courseBehavior);
    aiccCourse.setCourseDescription(null);

    // Verify the getCourseDescription method returns null for a null map
    assertNull(aiccCourse.getCourseDescription());
  }

  /**
   * Tests the default constructor for the AiccCourse class.
   */
  @Test
  void testAiccCourseDefaultConstructor() {
    // Create an AiccCourse using the default constructor
    AiccCourse aiccCourse = new AiccCourse();

    // Verify that the properties are null
    assertNull(aiccCourse.getCourse());
    assertNull(aiccCourse.getCourseBehavior());
    assertNull(aiccCourse.getCourseDescription());
  }

  /**
   * Tests the default constructor for the Course inner class.
   */
  @Test
  void testCourseDefaultConstructor() {
    // Create a Course using the default constructor
    AiccCourse.Course course = new AiccCourse.Course();

    // Verify that the properties are null
    assertNull(course.getCourseCreator());
    assertNull(course.getCourseId());
    assertNull(course.getCourseTitle());
    assertNull(course.getCourseSystem());
    assertNull(course.getLevel());
    assertNull(course.getMaxFieldsCst());
    assertNull(course.getMaxFieldsOrt());
    assertNull(course.getTotalAus());
    assertNull(course.getTotalBlocks());
    assertNull(course.getVersion());
    assertNull(course.getTotalComplexObj());
    assertNull(course.getTotalObjectives());
  }

  /**
   * Tests the default constructor for the CourseBehavior inner class.
   */
  @Test
  void testCourseBehaviorDefaultConstructor() {
    // Create a CourseBehavior using the default constructor
    AiccCourse.CourseBehavior courseBehavior = new AiccCourse.CourseBehavior();

    // Verify that the properties are null
    assertNull(courseBehavior.getMaxNormal());
  }
}
