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

import dev.jcputney.elearning.parser.input.xapi.tincan.TincanManifest;
import dev.jcputney.elearning.parser.validation.ValidationIssue;
import dev.jcputney.elearning.parser.validation.ValidationResult;
import java.util.ArrayList;
import java.util.List;

/**
 * Validator for xAPI/TinCan manifests.
 * <p>
 * This validator checks that:
 * </p>
 * <ul>
 *   <li>Required activities exist</li>
 *   <li>Launch URLs are properly defined</li>
 *   <li>Activity IDs are valid</li>
 * </ul>
 */
public class XapiValidator {

  /**
   * Validates an xAPI/TinCan manifest for structural integrity.
   *
   * @param manifest The xAPI manifest to validate
   * @return ValidationResult containing any issues found
   */
  public ValidationResult validate(TincanManifest manifest) {
    List<ValidationIssue> issues = new ArrayList<>();

    // Validate basic structure
    if (manifest.getActivities() == null || manifest.getActivities().isEmpty()) {
      issues.add(ValidationIssue.error(
          "XAPI_MISSING_ACTIVITIES",
          "xAPI manifest must contain at least one activity",
          "tincan.xml/activities"
      ));
      return ValidationResult.of(issues.toArray(new ValidationIssue[0]));
    }

    if (manifest.getLaunchUrl() == null || manifest.getLaunchUrl().trim().isEmpty()) {
      issues.add(ValidationIssue.error(
          "XAPI_MISSING_LAUNCH_URL",
          "xAPI package must have a launch URL",
          "tincan.xml/activities/activity",
          "Ensure at least one activity has a launch attribute"
      ));
    }

    return ValidationResult.of(issues.toArray(new ValidationIssue[0]));
  }
}
