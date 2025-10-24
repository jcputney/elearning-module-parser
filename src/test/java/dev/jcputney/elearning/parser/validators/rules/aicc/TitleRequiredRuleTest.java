package dev.jcputney.elearning.parser.validators.rules.aicc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import dev.jcputney.elearning.parser.input.aicc.AiccCourse;
import dev.jcputney.elearning.parser.input.aicc.AiccManifest;
import dev.jcputney.elearning.parser.validation.ValidationResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TitleRequiredRuleTest {

  private TitleRequiredRule rule;

  @BeforeEach
  void setUp() {
    rule = new TitleRequiredRule();
  }

  @Test
  void validate_withValidTitle_returnsValid() {
    AiccManifest manifest = createManifestWithTitle("Test Course");

    ValidationResult result = rule.validate(manifest);

    assertThat(result.isValid()).isTrue();
    assertThat(result.hasErrors()).isFalse();
  }

  @Test
  void validate_withNullTitle_returnsError() {
    AiccManifest manifest = createManifestWithTitle(null);

    ValidationResult result = rule.validate(manifest);

    assertThat(result.isValid()).isFalse();
    assertThat(result.hasErrors()).isTrue();
    assertThat(result.getErrors()).hasSize(1);
    assertThat(result.getErrors().get(0).code()).isEqualTo("AICC_MISSING_TITLE");
    assertThat(result.getErrors().get(0).message())
        .isEqualTo("AICC course must have a title");
  }

  @Test
  void validate_withEmptyTitle_returnsError() {
    AiccManifest manifest = createManifestWithTitle("   ");

    ValidationResult result = rule.validate(manifest);

    assertThat(result.isValid()).isFalse();
    assertThat(result.hasErrors()).isTrue();
    assertThat(result.getErrors().get(0).code()).isEqualTo("AICC_MISSING_TITLE");
  }

  @Test
  void validate_withNullCourse_returnsValid() {
    // Defer to CourseRequiredRule for null course validation
    AiccManifest manifest = new AiccManifest();
    manifest.setCourse(null);

    ValidationResult result = rule.validate(manifest);

    assertThat(result.isValid()).isTrue();
  }

  @Test
  void validate_withNullManifest_throwsException() {
    assertThatThrownBy(() -> rule.validate(null))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("manifest must not be null");
  }

  private AiccManifest createManifestWithTitle(String title) {
    AiccManifest manifest = new AiccManifest();
    AiccCourse aiccCourse = new AiccCourse();
    AiccCourse.Course course = new AiccCourse.Course();
    course.setCourseTitle(title);
    aiccCourse.setCourse(course);
    manifest.setCourse(aiccCourse);
    return manifest;
  }
}
