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

import dev.jcputney.elearning.parser.input.xapi.tincan.TincanManifest;
import dev.jcputney.elearning.parser.validation.ValidationIssue;
import dev.jcputney.elearning.parser.validation.ValidationResult;
import dev.jcputney.elearning.parser.validators.rules.ValidationRule;

/**
 * Validates that an xAPI package has a launch URL.
 *
 * <p>According to xAPI specification, at least one activity must have a launch attribute
 * that serves as the entry point for the learning experience.</p>
 *
 * <p>This rule defers validation when the activities list is null or empty, as that is
 * handled by {@link ActivitiesRequiredRule}.</p>
 *
 * @see <a href="https://github.com/adlnet/xAPI-Spec/blob/master/xAPI-About.md">xAPI Specification</a>
 */
public class LaunchUrlRequiredRule implements ValidationRule<TincanManifest> {

  /**
   * Validates that the package has a launch URL.
   *
   * @param manifest The xAPI manifest to validate (must not be null)
   * @return ValidationResult containing any issues found
   * @throws IllegalArgumentException if manifest is null
   */
  @Override
  public ValidationResult validate(TincanManifest manifest) {
    if (manifest == null) {
      throw new IllegalArgumentException("manifest must not be null");
    }

    // Defer to ActivitiesRequiredRule for null/empty activities
    if (manifest.getActivities() == null || manifest.getActivities().isEmpty()) {
      return ValidationResult.valid();
    }

    String launchUrl = manifest.getLaunchUrl();
    if (launchUrl == null || launchUrl.trim().isEmpty()) {
      return ValidationResult.of(
          ValidationIssue.error(
              "XAPI_MISSING_LAUNCH_URL",
              "xAPI package must have a launch URL",
              "tincan.xml/activities/activity",
              "Ensure at least one activity has a launch attribute"
          )
      );
    }

    return ValidationResult.valid();
  }

  @Override
  public String getRuleName() {
    return "LaunchUrlRequired";
  }

  @Override
  public String getSpecReference() {
    return "xAPI Specification - Activity Launch URL";
  }
}
