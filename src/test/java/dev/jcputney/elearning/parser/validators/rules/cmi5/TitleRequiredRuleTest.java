package dev.jcputney.elearning.parser.validators.rules.cmi5;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import dev.jcputney.elearning.parser.input.cmi5.Cmi5Manifest;
import dev.jcputney.elearning.parser.input.cmi5.Course;
import dev.jcputney.elearning.parser.input.xapi.types.LangString;
import dev.jcputney.elearning.parser.input.xapi.types.TextType;
import dev.jcputney.elearning.parser.validation.ValidationResult;
import java.util.Collections;
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
    Cmi5Manifest manifest = createManifestWithTitle("Test Course");

    ValidationResult result = rule.validate(manifest);

    assertThat(result.isValid()).isTrue();
    assertThat(result.hasErrors()).isFalse();
  }

  @Test
  void validate_withNullTitle_returnsError() {
    Cmi5Manifest manifest = createManifestWithTitle(null);

    ValidationResult result = rule.validate(manifest);

    assertThat(result.isValid()).isFalse();
    assertThat(result.hasErrors()).isTrue();
    assertThat(result.getErrors()).hasSize(1);
    assertThat(result.getErrors().get(0).code()).isEqualTo("CMI5_MISSING_TITLE");
    assertThat(result.getErrors().get(0).message())
        .isEqualTo("cmi5 course must have a title");
  }

  @Test
  void validate_withEmptyTitle_returnsError() {
    Cmi5Manifest manifest = createManifestWithTitle("");

    ValidationResult result = rule.validate(manifest);

    assertThat(result.isValid()).isFalse();
    assertThat(result.hasErrors()).isTrue();
    assertThat(result.getErrors().get(0).code()).isEqualTo("CMI5_MISSING_TITLE");
  }

  @Test
  void validate_withNullCourse_returnsValid() {
    // Defer to CourseRequiredRule for null course validation
    Cmi5Manifest manifest = new Cmi5Manifest();
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

  private Cmi5Manifest createManifestWithTitle(String titleValue) {
    Cmi5Manifest manifest = new Cmi5Manifest();
    Course course = new Course();
    course.setId("course1");

    if (titleValue != null) {
      LangString title = new LangString();
      title.setValue(titleValue);
      TextType titleType = new TextType();
      titleType.setStrings(Collections.singletonList(title));
      course.setTitle(titleType);
    }

    manifest.setCourse(course);
    return manifest;
  }
}
