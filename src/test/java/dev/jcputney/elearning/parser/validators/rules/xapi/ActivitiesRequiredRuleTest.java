package dev.jcputney.elearning.parser.validators.rules.xapi;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import dev.jcputney.elearning.parser.input.xapi.tincan.TincanActivity;
import dev.jcputney.elearning.parser.input.xapi.tincan.TincanManifest;
import dev.jcputney.elearning.parser.validation.ValidationResult;
import java.util.Collections;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ActivitiesRequiredRuleTest {

  private ActivitiesRequiredRule rule;

  @BeforeEach
  void setUp() {
    rule = new ActivitiesRequiredRule();
  }

  @Test
  void validate_withValidActivities_returnsValid() {
    TincanManifest manifest = new TincanManifest();
    TincanActivity activity = new TincanActivity();
    activity.setId("http://example.com/activity/1");
    manifest.setActivities(Collections.singletonList(activity));

    ValidationResult result = rule.validate(manifest);

    assertThat(result.isValid()).isTrue();
    assertThat(result.hasErrors()).isFalse();
  }

  @Test
  void validate_withNullActivities_returnsError() {
    TincanManifest manifest = new TincanManifest();
    manifest.setActivities(null);

    ValidationResult result = rule.validate(manifest);

    assertThat(result.isValid()).isFalse();
    assertThat(result.hasErrors()).isTrue();
    assertThat(result.getErrors()).hasSize(1);
    assertThat(result.getErrors().get(0).code()).isEqualTo("XAPI_MISSING_ACTIVITIES");
    assertThat(result.getErrors().get(0).message())
        .isEqualTo("xAPI manifest must contain at least one activity");
  }

  @Test
  void validate_withEmptyActivities_returnsError() {
    TincanManifest manifest = new TincanManifest();
    manifest.setActivities(Collections.emptyList());

    ValidationResult result = rule.validate(manifest);

    assertThat(result.isValid()).isFalse();
    assertThat(result.hasErrors()).isTrue();
    assertThat(result.getErrors()).hasSize(1);
    assertThat(result.getErrors().get(0).code()).isEqualTo("XAPI_MISSING_ACTIVITIES");
  }

  @Test
  void validate_withNullManifest_throwsException() {
    assertThatThrownBy(() -> rule.validate(null))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("manifest must not be null");
  }
}
