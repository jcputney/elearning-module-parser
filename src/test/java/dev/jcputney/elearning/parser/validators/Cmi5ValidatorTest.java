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

import dev.jcputney.elearning.parser.input.cmi5.AU;
import dev.jcputney.elearning.parser.input.cmi5.Course;
import dev.jcputney.elearning.parser.input.cmi5.Cmi5Manifest;
import dev.jcputney.elearning.parser.input.xapi.types.LangString;
import dev.jcputney.elearning.parser.input.xapi.types.TextType;
import dev.jcputney.elearning.parser.validation.ValidationResult;
import java.util.Collections;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests for the cmi5 validator.
 */
class Cmi5ValidatorTest {

  private Cmi5Validator validator;

  @BeforeEach
  void setUp() {
    validator = new Cmi5Validator();
  }

  @Test
  void validate_validManifest_noIssues() {
    // Create a valid manifest
    Cmi5Manifest manifest = createValidManifest();

    ValidationResult result = validator.validate(manifest);

    assertThat(result.isValid()).isTrue();
    assertThat(result.hasErrors()).isFalse();
    assertThat(result.getAllIssues()).isEmpty();
  }

  @Test
  void validate_missingCourse_hasError() {
    // Create manifest without course
    // Note: validator checks all conditions even if course is null,
    // so we get multiple errors
    Cmi5Manifest manifest = new Cmi5Manifest();
    manifest.setCourse(null);

    ValidationResult result = validator.validate(manifest);

    assertThat(result.isValid()).isFalse();
    assertThat(result.hasErrors()).isTrue();
    // Validator reports multiple errors when course is null:
    // missing course, missing title, and missing launch URL
    assertThat(result.getErrors().size()).isGreaterThanOrEqualTo(1);
    assertThat(result.getErrors().stream()
        .anyMatch(e -> e.code().equals("CMI5_MISSING_COURSE"))).isTrue();
  }

  @Test
  void validate_missingTitle_hasError() {
    // Create manifest with null title
    Cmi5Manifest manifest = new Cmi5Manifest();
    Course course = new Course();
    course.setId("course1");
    manifest.setCourse(course);

    ValidationResult result = validator.validate(manifest);

    assertThat(result.isValid()).isFalse();
    assertThat(result.hasErrors()).isTrue();
    assertThat(result.getErrors().stream()
        .anyMatch(e -> e.code().equals("CMI5_MISSING_TITLE"))).isTrue();
  }

  @Test
  void validate_emptyTitle_hasError() {
    // Create manifest with empty title
    Cmi5Manifest manifest = new Cmi5Manifest();
    Course course = new Course();
    course.setId("course1");
    LangString emptyTitle = new LangString();
    emptyTitle.setValue("");
    TextType titleType = new TextType();
    titleType.setStrings(Collections.singletonList(emptyTitle));
    course.setTitle(titleType);
    manifest.setCourse(course);

    // Add AU with URL so launch URL validation passes
    AU au = new AU();
    au.setUrl("test.html");
    manifest.setAssignableUnits(Collections.singletonList(au));

    ValidationResult result = validator.validate(manifest);

    assertThat(result.isValid()).isFalse();
    assertThat(result.hasErrors()).isTrue();
    assertThat(result.getErrors().stream()
        .anyMatch(e -> e.code().equals("CMI5_MISSING_TITLE"))).isTrue();
  }

  @Test
  void validate_missingLaunchUrl_hasError() {
    // Create manifest without launch URL - no AUs means getLaunchUrl() returns null
    Cmi5Manifest manifest = new Cmi5Manifest();
    Course course = new Course();
    course.setId("course1");
    LangString title = new LangString();
    title.setValue("Test Course");
    TextType titleType = new TextType();
    titleType.setStrings(Collections.singletonList(title));
    course.setTitle(titleType);
    manifest.setCourse(course);
    // Don't set assignableUnits or blocks, so getLaunchUrl() returns null

    ValidationResult result = validator.validate(manifest);

    assertThat(result.isValid()).isFalse();
    assertThat(result.hasErrors()).isTrue();
    assertThat(result.getErrors().stream()
        .anyMatch(e -> e.code().equals("CMI5_MISSING_LAUNCH_URL"))).isTrue();
  }

  @Test
  void validate_emptyLaunchUrl_hasError() {
    // Create manifest with empty launch URL - AU with empty/whitespace URL
    Cmi5Manifest manifest = new Cmi5Manifest();
    Course course = new Course();
    course.setId("course1");
    LangString title = new LangString();
    title.setValue("Test Course");
    TextType titleType = new TextType();
    titleType.setStrings(Collections.singletonList(title));
    course.setTitle(titleType);
    manifest.setCourse(course);

    // Add AU with empty/whitespace URL
    AU au = new AU();
    au.setUrl("   "); // Empty/whitespace
    manifest.setAssignableUnits(Collections.singletonList(au));

    ValidationResult result = validator.validate(manifest);

    assertThat(result.isValid()).isFalse();
    assertThat(result.hasErrors()).isTrue();
    assertThat(result.getErrors().stream()
        .anyMatch(e -> e.code().equals("CMI5_MISSING_LAUNCH_URL"))).isTrue();
  }

  /**
   * Creates a valid cmi5 manifest for testing.
   */
  private Cmi5Manifest createValidManifest() {
    Cmi5Manifest manifest = new Cmi5Manifest();

    Course course = new Course();
    course.setId("course1");
    LangString title = new LangString();
    title.setValue("Test Course");
    TextType titleType = new TextType();
    titleType.setStrings(Collections.singletonList(title));
    course.setTitle(titleType);
    manifest.setCourse(course);

    // Add AU with valid URL so getLaunchUrl() returns a value
    AU au = new AU();
    au.setUrl("course.html");
    manifest.setAssignableUnits(Collections.singletonList(au));

    return manifest;
  }
}
