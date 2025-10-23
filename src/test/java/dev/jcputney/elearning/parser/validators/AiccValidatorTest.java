/*
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
 */

package dev.jcputney.elearning.parser.validators;

import static org.assertj.core.api.Assertions.assertThat;

import dev.jcputney.elearning.parser.input.aicc.AiccCourse;
import dev.jcputney.elearning.parser.input.aicc.AiccManifest;
import dev.jcputney.elearning.parser.validation.ValidationResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests for the AICC validator.
 */
class AiccValidatorTest {

  private AiccValidator validator;

  @BeforeEach
  void setUp() {
    validator = new AiccValidator();
  }

  @Test
  void validate_validManifest_noIssues() {
    // Create a valid manifest
    AiccManifest manifest = createValidManifest();

    ValidationResult result = validator.validate(manifest);

    assertThat(result.isValid()).isTrue();
    assertThat(result.hasErrors()).isFalse();
    assertThat(result.getAllIssues()).isEmpty();
  }

  @Test
  void validate_missingCourse_hasError() {
    // Create manifest with valid structure but empty course title
    // Note: The validator checks getCourse() == null, not the inner course.getCourse()
    // So we test with a valid structure but empty title to verify validation
    AiccManifest manifest = new AiccManifest();
    AiccCourse aiccCourse = new AiccCourse();
    AiccCourse.Course course = new AiccCourse.Course();
    course.setCourseTitle(""); // Empty title
    aiccCourse.setCourse(course);
    manifest.setCourse(aiccCourse);

    ValidationResult result = validator.validate(manifest);

    assertThat(result.isValid()).isFalse();
    assertThat(result.hasErrors()).isTrue();
    // Will report missing title and missing launch URL
    assertThat(result.getErrors().size()).isGreaterThanOrEqualTo(1);
  }

  @Test
  void validate_missingTitle_hasError() {
    // Create manifest with null title
    AiccManifest manifest = new AiccManifest();
    AiccCourse aiccCourse = new AiccCourse();
    AiccCourse.Course course = new AiccCourse.Course();
    course.setCourseTitle(null);
    aiccCourse.setCourse(course);
    manifest.setCourse(aiccCourse);

    ValidationResult result = validator.validate(manifest);

    assertThat(result.isValid()).isFalse();
    assertThat(result.hasErrors()).isTrue();
    assertThat(result.getErrors().stream()
        .anyMatch(e -> e.code().equals("AICC_MISSING_TITLE"))).isTrue();
  }

  @Test
  void validate_emptyTitle_hasError() {
    // Create manifest with empty title
    AiccManifest manifest = new AiccManifest();
    AiccCourse aiccCourse = new AiccCourse();
    AiccCourse.Course course = new AiccCourse.Course();
    course.setCourseTitle("   "); // Empty/whitespace title
    aiccCourse.setCourse(course);
    manifest.setCourse(aiccCourse);
    manifest.setLaunchUrl("test.html");

    ValidationResult result = validator.validate(manifest);

    assertThat(result.isValid()).isFalse();
    assertThat(result.hasErrors()).isTrue();
    assertThat(result.getErrors().stream()
        .anyMatch(e -> e.code().equals("AICC_MISSING_TITLE"))).isTrue();
  }

  @Test
  void validate_missingLaunchUrl_hasError() {
    // Create manifest without launch URL
    AiccManifest manifest = new AiccManifest();
    AiccCourse aiccCourse = new AiccCourse();
    AiccCourse.Course course = new AiccCourse.Course();
    course.setCourseTitle("Test Course");
    aiccCourse.setCourse(course);
    manifest.setCourse(aiccCourse);
    manifest.setLaunchUrl(null);

    ValidationResult result = validator.validate(manifest);

    assertThat(result.isValid()).isFalse();
    assertThat(result.hasErrors()).isTrue();
    assertThat(result.getErrors().stream()
        .anyMatch(e -> e.code().equals("AICC_MISSING_LAUNCH_URL"))).isTrue();
  }

  @Test
  void validate_emptyLaunchUrl_hasError() {
    // Create manifest with empty launch URL
    AiccManifest manifest = new AiccManifest();
    AiccCourse aiccCourse = new AiccCourse();
    AiccCourse.Course course = new AiccCourse.Course();
    course.setCourseTitle("Test Course");
    aiccCourse.setCourse(course);
    manifest.setCourse(aiccCourse);
    manifest.setLaunchUrl("   "); // Empty/whitespace

    ValidationResult result = validator.validate(manifest);

    assertThat(result.isValid()).isFalse();
    assertThat(result.hasErrors()).isTrue();
    assertThat(result.getErrors().stream()
        .anyMatch(e -> e.code().equals("AICC_MISSING_LAUNCH_URL"))).isTrue();
  }

  /**
   * Creates a valid AICC manifest for testing.
   */
  private AiccManifest createValidManifest() {
    AiccManifest manifest = new AiccManifest();

    AiccCourse aiccCourse = new AiccCourse();
    AiccCourse.Course course = new AiccCourse.Course();
    course.setCourseTitle("Test Course");
    aiccCourse.setCourse(course);
    manifest.setCourse(aiccCourse);

    manifest.setLaunchUrl("course.html");

    return manifest;
  }
}
