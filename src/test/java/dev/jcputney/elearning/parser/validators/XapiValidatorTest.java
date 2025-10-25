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

import dev.jcputney.elearning.parser.input.xapi.tincan.TincanActivity;
import dev.jcputney.elearning.parser.input.xapi.tincan.TincanManifest;
import dev.jcputney.elearning.parser.input.xapi.types.SimpleLangString;
import dev.jcputney.elearning.parser.validation.ValidationResult;
import java.util.Collections;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests for the xAPI validator.
 */
class XapiValidatorTest {

  private XapiValidator validator;

  @BeforeEach
  void setUp() {
    validator = new XapiValidator();
  }

  @Test
  void validate_validManifest_noIssues() {
    // Create a valid manifest
    TincanManifest manifest = createValidManifest();

    ValidationResult result = validator.validate(manifest);

    assertThat(result.isValid()).isTrue();
    assertThat(result.hasErrors()).isFalse();
    assertThat(result.getAllIssues()).isEmpty();
  }

  @Test
  void validate_missingActivities_hasError() {
    // Create manifest without activities
    TincanManifest manifest = new TincanManifest();
    manifest.setActivities(null);

    ValidationResult result = validator.validate(manifest);

    assertThat(result.isValid()).isFalse();
    assertThat(result.hasErrors()).isTrue();
    assertThat(result.getErrors()).hasSize(1);
    assertThat(result.getErrors().get(0).code()).isEqualTo("XAPI_MISSING_ACTIVITIES");
    assertThat(result.getErrors().get(0).message()).contains("at least one activity");
  }

  @Test
  void validate_emptyActivities_hasError() {
    // Create manifest with empty activities list
    TincanManifest manifest = new TincanManifest();
    manifest.setActivities(Collections.emptyList());

    ValidationResult result = validator.validate(manifest);

    assertThat(result.isValid()).isFalse();
    assertThat(result.hasErrors()).isTrue();
    assertThat(result.getErrors()).hasSize(1);
    assertThat(result.getErrors().get(0).code()).isEqualTo("XAPI_MISSING_ACTIVITIES");
  }

  @Test
  void validate_missingLaunchUrl_hasError() {
    // Create manifest without launch URL - activity has no launches
    TincanManifest manifest = new TincanManifest();
    TincanActivity activity = new TincanActivity();
    activity.setId("http://example.com/activity/1");
    // Don't set launches, so getLaunch() returns null
    manifest.setActivities(Collections.singletonList(activity));

    ValidationResult result = validator.validate(manifest);

    assertThat(result.isValid()).isFalse();
    assertThat(result.hasErrors()).isTrue();
    assertThat(result.getErrors().stream()
        .anyMatch(e -> e.code().equals("XAPI_MISSING_LAUNCH_URL"))).isTrue();
  }

  @Test
  void validate_emptyLaunchUrl_hasError() {
    // Create manifest with empty launch URL - activity has empty/whitespace launch
    TincanManifest manifest = new TincanManifest();
    TincanActivity activity = new TincanActivity();
    activity.setId("http://example.com/activity/1");
    SimpleLangString emptyLaunch = new SimpleLangString();
    emptyLaunch.setValue("   "); // Empty/whitespace
    activity.setLaunches(Collections.singletonList(emptyLaunch));
    manifest.setActivities(Collections.singletonList(activity));

    ValidationResult result = validator.validate(manifest);

    assertThat(result.isValid()).isFalse();
    assertThat(result.hasErrors()).isTrue();
    assertThat(result.getErrors().stream()
        .anyMatch(e -> e.code().equals("XAPI_MISSING_LAUNCH_URL"))).isTrue();
  }

  @Test
  void validate_usesRuleBasedArchitecture() {
    // This test verifies the validator uses the rule-based architecture
    // by checking that multiple rules are applied
    TincanManifest manifest = new TincanManifest();
    TincanActivity activity = new TincanActivity();
    activity.setId("http://example.com/activity/1");
    // Activity exists but has no launch URL
    manifest.setActivities(Collections.singletonList(activity));

    ValidationResult result = validator.validate(manifest);

    assertThat(result.isValid()).isFalse();
    assertThat(result.hasErrors()).isTrue();
    // Should have 1 error (launch URL) - activities exist but no launch URL
    assertThat(result.getErrors()).hasSize(1);
    assertThat(result.getErrors().get(0).code()).isEqualTo("XAPI_MISSING_LAUNCH_URL");
  }

  /**
   * Creates a valid xAPI manifest for testing.
   */
  private TincanManifest createValidManifest() {
    TincanManifest manifest = new TincanManifest();

    TincanActivity activity = new TincanActivity();
    activity.setId("http://example.com/activity/1");
    SimpleLangString launch = new SimpleLangString();
    launch.setValue("course.html");
    activity.setLaunches(Collections.singletonList(launch));
    manifest.setActivities(Collections.singletonList(activity));

    return manifest;
  }
}
