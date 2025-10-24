package dev.jcputney.elearning.parser.validators.rules.aicc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import dev.jcputney.elearning.parser.input.aicc.AiccCourse;
import dev.jcputney.elearning.parser.input.aicc.AiccManifest;
import dev.jcputney.elearning.parser.validation.ValidationResult;
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
    AiccManifest manifest = new AiccManifest();
    manifest.setCourse(new AiccCourse());
    manifest.setLaunchUrl("course.html");

    ValidationResult result = rule.validate(manifest);

    assertThat(result.isValid()).isTrue();
    assertThat(result.hasErrors()).isFalse();
  }

  @Test
  void validate_withNullLaunchUrl_returnsError() {
    AiccManifest manifest = new AiccManifest();
    manifest.setCourse(new AiccCourse());
    manifest.setLaunchUrl(null);

    ValidationResult result = rule.validate(manifest);

    assertThat(result.isValid()).isFalse();
    assertThat(result.hasErrors()).isTrue();
    assertThat(result.getErrors()).hasSize(1);
    assertThat(result.getErrors().get(0).code()).isEqualTo("AICC_MISSING_LAUNCH_URL");
    assertThat(result.getErrors().get(0).message())
        .isEqualTo("AICC course must have a launch URL");
  }

  @Test
  void validate_withEmptyLaunchUrl_returnsError() {
    AiccManifest manifest = new AiccManifest();
    manifest.setCourse(new AiccCourse());
    manifest.setLaunchUrl("   ");

    ValidationResult result = rule.validate(manifest);

    assertThat(result.isValid()).isFalse();
    assertThat(result.hasErrors()).isTrue();
    assertThat(result.getErrors().get(0).code()).isEqualTo("AICC_MISSING_LAUNCH_URL");
  }

  @Test
  void validate_withNullManifest_throwsException() {
    assertThatThrownBy(() -> rule.validate(null))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("manifest must not be null");
  }
}
