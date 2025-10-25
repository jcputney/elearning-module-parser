package dev.jcputney.elearning.parser.validators.rules.cmi5;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import dev.jcputney.elearning.parser.input.cmi5.Cmi5Manifest;
import dev.jcputney.elearning.parser.input.cmi5.Course;
import dev.jcputney.elearning.parser.validation.ValidationResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CourseRequiredRuleTest {

  private CourseRequiredRule rule;

  @BeforeEach
  void setUp() {
    rule = new CourseRequiredRule();
  }

  @Test
  void validate_withValidCourse_returnsValid() {
    Cmi5Manifest manifest = new Cmi5Manifest();
    Course course = new Course();
    course.setId("course1");
    manifest.setCourse(course);

    ValidationResult result = rule.validate(manifest);

    assertThat(result.isValid()).isTrue();
    assertThat(result.hasErrors()).isFalse();
  }

  @Test
  void validate_withNullCourse_returnsError() {
    Cmi5Manifest manifest = new Cmi5Manifest();
    manifest.setCourse(null);

    ValidationResult result = rule.validate(manifest);

    assertThat(result.isValid()).isFalse();
    assertThat(result.hasErrors()).isTrue();
    assertThat(result.getErrors()).hasSize(1);
    assertThat(result.getErrors().get(0).code()).isEqualTo("CMI5_MISSING_COURSE");
    assertThat(result.getErrors().get(0).message())
        .isEqualTo("cmi5 manifest must contain course element");
  }

  @Test
  void validate_withNullManifest_throwsException() {
    assertThatThrownBy(() -> rule.validate(null))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("manifest must not be null");
  }
}
