package dev.jcputney.elearning.parser.validators.rules.cmi5;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import dev.jcputney.elearning.parser.input.cmi5.AU;
import dev.jcputney.elearning.parser.input.cmi5.Cmi5Manifest;
import dev.jcputney.elearning.parser.input.cmi5.Course;
import dev.jcputney.elearning.parser.validation.ValidationResult;
import java.util.Collections;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class LaunchUrlRequiredRuleTest {

  private LaunchUrlRequiredRule rule;

  @BeforeEach
  void setUp() {
    rule = new LaunchUrlRequiredRule();
  }

  @Test
  void validate_withValidLaunchUrl_returnsValid() {
    Cmi5Manifest manifest = new Cmi5Manifest();
    Course course = new Course();
    manifest.setCourse(course);

    AU au = new AU();
    au.setUrl("course.html");
    manifest.setAssignableUnits(Collections.singletonList(au));

    ValidationResult result = rule.validate(manifest);

    assertThat(result.isValid()).isTrue();
    assertThat(result.hasErrors()).isFalse();
  }

  @Test
  void validate_withNullLaunchUrl_returnsError() {
    Cmi5Manifest manifest = new Cmi5Manifest();
    Course course = new Course();
    manifest.setCourse(course);
    // No AUs means getLaunchUrl() returns null

    ValidationResult result = rule.validate(manifest);

    assertThat(result.isValid()).isFalse();
    assertThat(result.hasErrors()).isTrue();
    assertThat(result.getErrors()).hasSize(1);
    assertThat(result.getErrors().get(0).code()).isEqualTo("CMI5_MISSING_LAUNCH_URL");
    assertThat(result.getErrors().get(0).message())
        .isEqualTo("cmi5 course must have at least one AU with a launch URL");
  }

  @Test
  void validate_withEmptyLaunchUrl_returnsError() {
    Cmi5Manifest manifest = new Cmi5Manifest();
    Course course = new Course();
    manifest.setCourse(course);

    AU au = new AU();
    au.setUrl("   "); // Empty/whitespace
    manifest.setAssignableUnits(Collections.singletonList(au));

    ValidationResult result = rule.validate(manifest);

    assertThat(result.isValid()).isFalse();
    assertThat(result.hasErrors()).isTrue();
    assertThat(result.getErrors().get(0).code()).isEqualTo("CMI5_MISSING_LAUNCH_URL");
  }

  @Test
  void validate_withNullManifest_throwsException() {
    assertThatThrownBy(() -> rule.validate(null))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("manifest must not be null");
  }
}
