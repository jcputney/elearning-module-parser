package dev.jcputney.elearning.parser.validators.rules.aicc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import dev.jcputney.elearning.parser.input.aicc.AiccCourse;
import dev.jcputney.elearning.parser.input.aicc.AiccManifest;
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
    AiccManifest manifest = new AiccManifest();
    AiccCourse course = new AiccCourse();
    manifest.setCourse(course);

    ValidationResult result = rule.validate(manifest);

    assertThat(result.isValid()).isTrue();
    assertThat(result.hasErrors()).isFalse();
  }

  @Test
  void validate_withNullCourse_returnsError() {
    AiccManifest manifest = new AiccManifest();
    manifest.setCourse(null);

    ValidationResult result = rule.validate(manifest);

    assertThat(result.isValid()).isFalse();
    assertThat(result.hasErrors()).isTrue();
    assertThat(result.getErrors()).hasSize(1);
    assertThat(result.getErrors().get(0).code()).isEqualTo("AICC_MISSING_COURSE");
    assertThat(result.getErrors().get(0).message())
        .isEqualTo("AICC manifest must contain course information");
  }

  @Test
  void validate_withNullManifest_throwsException() {
    assertThatThrownBy(() -> rule.validate(null))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("manifest must not be null");
  }
}
