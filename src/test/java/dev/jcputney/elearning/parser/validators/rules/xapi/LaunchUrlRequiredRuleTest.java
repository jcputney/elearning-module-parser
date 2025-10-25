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

package dev.jcputney.elearning.parser.validators.rules.xapi;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import dev.jcputney.elearning.parser.input.xapi.tincan.TincanActivity;
import dev.jcputney.elearning.parser.input.xapi.tincan.TincanManifest;
import dev.jcputney.elearning.parser.input.xapi.types.SimpleLangString;
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
    TincanManifest manifest = new TincanManifest();
    TincanActivity activity = new TincanActivity();
    activity.setId("http://example.com/activity/1");
    SimpleLangString launch = new SimpleLangString();
    launch.setValue("course.html");
    activity.setLaunches(Collections.singletonList(launch));
    manifest.setActivities(Collections.singletonList(activity));

    ValidationResult result = rule.validate(manifest);

    assertThat(result.isValid()).isTrue();
    assertThat(result.hasErrors()).isFalse();
  }

  @Test
  void validate_withNullLaunchUrl_returnsError() {
    TincanManifest manifest = new TincanManifest();
    TincanActivity activity = new TincanActivity();
    activity.setId("http://example.com/activity/1");
    // No launches set, so getLaunchUrl() returns null
    manifest.setActivities(Collections.singletonList(activity));

    ValidationResult result = rule.validate(manifest);

    assertThat(result.isValid()).isFalse();
    assertThat(result.hasErrors()).isTrue();
    assertThat(result.getErrors()).hasSize(1);
    assertThat(result.getErrors().get(0).code()).isEqualTo("XAPI_MISSING_LAUNCH_URL");
    assertThat(result.getErrors().get(0).message())
        .isEqualTo("xAPI package must have a launch URL");
  }

  @Test
  void validate_withEmptyLaunchUrl_returnsError() {
    TincanManifest manifest = new TincanManifest();
    TincanActivity activity = new TincanActivity();
    activity.setId("http://example.com/activity/1");
    SimpleLangString emptyLaunch = new SimpleLangString();
    emptyLaunch.setValue("   "); // Empty/whitespace
    activity.setLaunches(Collections.singletonList(emptyLaunch));
    manifest.setActivities(Collections.singletonList(activity));

    ValidationResult result = rule.validate(manifest);

    assertThat(result.isValid()).isFalse();
    assertThat(result.hasErrors()).isTrue();
    assertThat(result.getErrors().get(0).code()).isEqualTo("XAPI_MISSING_LAUNCH_URL");
  }

  @Test
  void validate_withNullActivities_returnsValid() {
    // Defer to ActivitiesRequiredRule for null/empty activities validation
    TincanManifest manifest = new TincanManifest();
    manifest.setActivities(null);

    ValidationResult result = rule.validate(manifest);

    assertThat(result.isValid()).isTrue();
  }

  @Test
  void validate_withEmptyActivities_returnsValid() {
    // Defer to ActivitiesRequiredRule for empty activities validation
    TincanManifest manifest = new TincanManifest();
    manifest.setActivities(Collections.emptyList());

    ValidationResult result = rule.validate(manifest);

    assertThat(result.isValid()).isTrue();
  }

  @Test
  void validate_withNullManifest_throwsException() {
    assertThatThrownBy(() -> rule.validate(null))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("manifest must not be null");
  }
}
