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
import static org.junit.jupiter.api.Assertions.assertTrue;

import dev.jcputney.elearning.parser.input.cmi5.types.LangString;
import dev.jcputney.elearning.parser.input.cmi5.types.TextType;
import java.time.Duration;
import java.util.Collections;
import java.util.List;
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
   * Creates a TextType with a single LangString.
   *
   * @param value the value of the LangString
   * @return a TextType with a single LangString
   */
  private TextType createTextType(String value) {
    return TextType.builder()
        .strings(Collections.singletonList(
            LangString.builder()
                .value(value)
                .lang("en-US")
                .build()
        ))
        .build();
  }

  /**
   * Creates a test Course.
   *
   * @return a test Course
   */
  private Course createTestCourse() {
    return Course.builder()
        .title(createTextType(TEST_TITLE))
        .description(createTextType(TEST_DESCRIPTION))
        .id(TEST_ID)
        .build();
  }

  /**
   * Creates a test AU.
   *
   * @return a test AU
   */
  private AU createTestAU() {
    return AU.builder()
        .title(createTextType("Test AU"))
        .description(createTextType("Test AU Description"))
        .id("test-au-id")
        .url(TEST_URL)
        .build();
  }

  /**
   * Tests that getTitle() returns the title from the course.
   */
  @Test
  void getTitleReturnsTitleFromCourse() {
    // Arrange
    Cmi5Manifest manifest = Cmi5Manifest.builder()
        .course(createTestCourse())
        .build();

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
    Cmi5Manifest manifest = Cmi5Manifest.builder().build();

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
    Course course = Course.builder()
        .id(TEST_ID)
        .build();
    Cmi5Manifest manifest = Cmi5Manifest.builder()
        .course(course)
        .build();

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
    Cmi5Manifest manifest = Cmi5Manifest.builder()
        .course(createTestCourse())
        .build();

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
    Cmi5Manifest manifest = Cmi5Manifest.builder().build();

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
    Course course = Course.builder()
        .title(createTextType(TEST_TITLE))
        .id(TEST_ID)
        .build();
    Cmi5Manifest manifest = Cmi5Manifest.builder()
        .course(course)
        .build();

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
    Cmi5Manifest manifest = Cmi5Manifest.builder()
        .course(createTestCourse())
        .assignableUnits(Collections.singletonList(createTestAU()))
        .build();

    // Act
    String launchUrl = manifest.getLaunchUrl();

    // Assert
    assertEquals(TEST_URL, launchUrl);
  }

  /**
   * Tests that getLaunchUrl() returns the URL from the first AU in the first block when there are no root-level AUs.
   */
  @Test
  void getLaunchUrlReturnsUrlFromFirstAUInFirstBlockWhenNoRootLevelAUs() {
    // Arrange
    Block block = Block.builder()
        .title(createTextType("Test Block"))
        .description(createTextType("Test Block Description"))
        .id("test-block-id")
        .assignableUnits(Collections.singletonList(createTestAU()))
        .build();
    Cmi5Manifest manifest = Cmi5Manifest.builder()
        .course(createTestCourse())
        .blocks(Collections.singletonList(block))
        .build();

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
    Cmi5Manifest manifest = Cmi5Manifest.builder()
        .course(createTestCourse())
        .build();

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
    Cmi5Manifest manifest = Cmi5Manifest.builder()
        .course(createTestCourse())
        .build();

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
    Cmi5Manifest manifest = Cmi5Manifest.builder().build();

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
    Cmi5Manifest manifest = Cmi5Manifest.builder()
        .course(createTestCourse())
        .build();

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
    Cmi5Manifest manifest = Cmi5Manifest.builder()
        .course(createTestCourse())
        .build();

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
    Cmi5Manifest manifest1 = Cmi5Manifest.builder()
        .course(createTestCourse())
        .assignableUnits(Collections.singletonList(createTestAU()))
        .build();
    Cmi5Manifest manifest2 = Cmi5Manifest.builder()
        .course(createTestCourse())
        .assignableUnits(Collections.singletonList(createTestAU()))
        .build();

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
    Cmi5Manifest manifest1 = Cmi5Manifest.builder()
        .course(createTestCourse())
        .assignableUnits(Collections.singletonList(createTestAU()))
        .build();
    Cmi5Manifest manifest2 = Cmi5Manifest.builder()
        .course(Course.builder()
            .title(createTextType("Different Title"))
            .description(createTextType(TEST_DESCRIPTION))
            .id(TEST_ID)
            .build())
        .assignableUnits(Collections.singletonList(createTestAU()))
        .build();

    // Act & Assert
    assertNotEquals(manifest1, manifest2);
    assertNotEquals(manifest1.hashCode(), manifest2.hashCode());
  }
}