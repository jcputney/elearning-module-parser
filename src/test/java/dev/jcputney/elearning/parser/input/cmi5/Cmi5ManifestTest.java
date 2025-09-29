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

package dev.jcputney.elearning.parser.input.cmi5;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import dev.jcputney.elearning.parser.input.cmi5.types.LangString;
import dev.jcputney.elearning.parser.input.cmi5.types.TextType;
import java.time.Duration;
import java.util.Collections;
import org.junit.jupiter.api.Test;

/**
 * Tests for the {@link Cmi5Manifest} class.
 */
class Cmi5ManifestTest {

  private static final String TEST_TITLE = "Test Title";
  private static final String TEST_DESCRIPTION = "Test Description";
  private static final String TEST_ID = "test-id";
  private static final String TEST_URL = "http://example.com/test";

  /**
   * Tests that getTitle() returns the title from the course.
   */
  @Test
  void getTitleReturnsTitleFromCourse() {
    // Arrange
    Cmi5Manifest manifest = new Cmi5Manifest();
    manifest.setCourse(createTestCourse());

    // Act
    String title = manifest.getTitle();

    // Assert
    assertEquals(TEST_TITLE, title);
  }

  /**
   * Tests that getTitle() returns null when course is null.
   */
  @Test
  void getTitleReturnsNullWhenCourseIsNull() {
    // Arrange
    Cmi5Manifest manifest = new Cmi5Manifest();

    // Act
    String title = manifest.getTitle();

    // Assert
    assertNull(title);
  }

  /**
   * Tests that getTitle() returns null when title is null.
   */
  @Test
  void getTitleReturnsNullWhenTitleIsNull() {
    // Arrange
    Course course = new Course();
    course.setId(TEST_ID);
    Cmi5Manifest manifest = new Cmi5Manifest();
    manifest.setCourse(course);

    // Act
    String title = manifest.getTitle();

    // Assert
    assertNull(title);
  }

  /**
   * Tests that getDescription() returns the description from the course.
   */
  @Test
  void getDescriptionReturnsDescriptionFromCourse() {
    // Arrange
    Cmi5Manifest manifest = new Cmi5Manifest();
    manifest.setCourse(createTestCourse());

    // Act
    String description = manifest.getDescription();

    // Assert
    assertEquals(TEST_DESCRIPTION, description);
  }

  /**
   * Tests that getDescription() returns null when course is null.
   */
  @Test
  void getDescriptionReturnsNullWhenCourseIsNull() {
    // Arrange
    Cmi5Manifest manifest = new Cmi5Manifest();

    // Act
    String description = manifest.getDescription();

    // Assert
    assertNull(description);
  }

  /**
   * Tests that getDescription() returns null when description is null.
   */
  @Test
  void getDescriptionReturnsNullWhenDescriptionIsNull() {
    // Arrange
    Course course = new Course();
    course.setId(TEST_ID);
    course.setTitle(createTextType(TEST_TITLE));
    Cmi5Manifest manifest = new Cmi5Manifest();
    manifest.setCourse(course);

    // Act
    String description = manifest.getDescription();

    // Assert
    assertNull(description);
  }

  /**
   * Tests that getLaunchUrl() returns the URL from the first AU at the root level.
   */
  @Test
  void getLaunchUrlReturnsUrlFromFirstRootLevelAU() {
    // Arrange
    Cmi5Manifest manifest = new Cmi5Manifest();
    manifest.setCourse(createTestCourse());
    manifest.setAssignableUnits(Collections.singletonList(createTestAU()));

    // Act
    String launchUrl = manifest.getLaunchUrl();

    // Assert
    assertEquals(TEST_URL, launchUrl);
  }

  /**
   * Tests that getLaunchUrl() returns the URL from the first AU in the first block when there are
   * no root-level AUs.
   */
  @Test
  void getLaunchUrlReturnsUrlFromFirstAUInFirstBlockWhenNoRootLevelAUs() {
    // Arrange
    Block block = new Block();
    block.setTitle(createTextType("Test Block"));
    block.setDescription(createTextType("Test Block Description"));
    block.setId("test-block-id");
    block.setAssignableUnits(Collections.singletonList(createTestAU()));

    Cmi5Manifest manifest = new Cmi5Manifest();
    manifest.setCourse(createTestCourse());
    manifest.setBlocks(Collections.singletonList(block));

    // Act
    String launchUrl = manifest.getLaunchUrl();

    // Assert
    assertEquals(TEST_URL, launchUrl);
  }

  /**
   * Tests that getLaunchUrl() returns null when there are no AUs.
   */
  @Test
  void getLaunchUrlReturnsNullWhenNoAUs() {
    // Arrange
    Cmi5Manifest manifest = new Cmi5Manifest();
    manifest.setCourse(createTestCourse());

    // Act
    String launchUrl = manifest.getLaunchUrl();

    // Assert
    assertNull(launchUrl);
  }

  /**
   * Tests that getIdentifier() returns the ID from the course.
   */
  @Test
  void getIdentifierReturnsIdFromCourse() {
    // Arrange
    Cmi5Manifest manifest = new Cmi5Manifest();
    manifest.setCourse(createTestCourse());

    // Act
    String identifier = manifest.getIdentifier();

    // Assert
    assertEquals(TEST_ID, identifier);
  }

  /**
   * Tests that getIdentifier() returns null when course is null.
   */
  @Test
  void getIdentifierReturnsNullWhenCourseIsNull() {
    // Arrange
    Cmi5Manifest manifest = new Cmi5Manifest();

    // Act
    String identifier = manifest.getIdentifier();

    // Assert
    assertNull(identifier);
  }

  /**
   * Tests that getVersion() returns null.
   */
  @Test
  void getVersionReturnsNull() {
    // Arrange
    Cmi5Manifest manifest = new Cmi5Manifest();
    manifest.setCourse(createTestCourse());

    // Act
    String version = manifest.getVersion();

    // Assert
    assertNull(version);
  }

  /**
   * Tests that getDuration() returns Duration.ZERO.
   */
  @Test
  void getDurationReturnsDurationZero() {
    // Arrange
    Cmi5Manifest manifest = new Cmi5Manifest();
    manifest.setCourse(createTestCourse());

    // Act
    Duration duration = manifest.getDuration();

    // Assert
    assertEquals(Duration.ZERO, duration);
  }

  /**
   * Tests that equals() returns true for equal manifests.
   */
  @Test
  void equalsReturnsTrueForEqualManifests() {
    // Arrange
    Cmi5Manifest manifest1 = new Cmi5Manifest();
    manifest1.setCourse(createTestCourse());
    manifest1.setAssignableUnits(Collections.singletonList(createTestAU()));

    Cmi5Manifest manifest2 = new Cmi5Manifest();
    manifest2.setCourse(createTestCourse());
    manifest2.setAssignableUnits(Collections.singletonList(createTestAU()));

    // Act & Assert
    assertEquals(manifest1, manifest2);
    assertEquals(manifest1.hashCode(), manifest2.hashCode());
  }

  /**
   * Tests that equals() returns false for different manifests.
   */
  @Test
  void equalsReturnsFalseForDifferentManifests() {
    // Arrange
    Cmi5Manifest manifest1 = new Cmi5Manifest();
    manifest1.setCourse(createTestCourse());
    manifest1.setAssignableUnits(Collections.singletonList(createTestAU()));

    Course course = new Course();
    course.setId(TEST_ID);
    course.setTitle(createTextType("Different Title"));
    course.setDescription(createTextType(TEST_DESCRIPTION));

    Cmi5Manifest manifest2 = new Cmi5Manifest();
    manifest2.setCourse(course);
    manifest2.setAssignableUnits(Collections.singletonList(createTestAU()));

    // Act & Assert
    assertNotEquals(manifest1, manifest2);
    assertNotEquals(manifest1.hashCode(), manifest2.hashCode());
  }

  /**
   * Creates a TextType with a single LangString.
   *
   * @param value the value of the LangString
   * @return a TextType with a single LangString
   */
  private TextType createTextType(String value) {
    return new TextType(Collections.singletonList(
        new LangString(value, "en-US")
    ));
  }

  /**
   * Creates a test Course.
   *
   * @return a test Course
   */
  private Course createTestCourse() {
    Course course = new Course();
    course.setId(TEST_ID);
    course.setTitle(createTextType(TEST_TITLE));
    course.setDescription(createTextType(TEST_DESCRIPTION));
    return course;
  }

  /**
   * Creates a test AU.
   *
   * @return a test AU
   */
  private AU createTestAU() {
    AU au = new AU();
    au.setId("test-au-id");
    au.setUrl(TEST_URL);
    au.setTitle(createTextType("Test AU"));
    au.setDescription(createTextType("Test AU Description"));
    return au;
  }
}