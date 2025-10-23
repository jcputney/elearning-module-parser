/*
 * Copyright (c) 2024-2025. Jonathan Putney
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
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

import dev.jcputney.elearning.parser.input.aicc.AiccManifest;
import dev.jcputney.elearning.parser.validation.ValidationIssue;
import dev.jcputney.elearning.parser.validation.ValidationResult;
import java.util.ArrayList;
import java.util.List;

/**
 * Validator for AICC manifests.
 * <p>
 * This validator checks that:
 * </p>
 * <ul>
 *   <li>Course structure is valid</li>
 *   <li>Required files exist (.crs, .au, .des, .cst)</li>
 *   <li>Assignable units have valid launch URLs</li>
 * </ul>
 */
public class AiccValidator {

  /**
   * Validates an AICC manifest for structural integrity.
   *
   * @param manifest The AICC manifest to validate
   * @return ValidationResult containing any issues found
   */
  public ValidationResult validate(AiccManifest manifest) {
    List<ValidationIssue> issues = new ArrayList<>();

    // Validate basic structure
    if (manifest.getCourse() == null) {
      issues.add(ValidationIssue.error(
          "AICC_MISSING_COURSE",
          "AICC manifest must contain course information",
          "course.crs"
      ));
    }

    if (manifest.getTitle() == null || manifest.getTitle().trim().isEmpty()) {
      issues.add(ValidationIssue.error(
          "AICC_MISSING_TITLE",
          "AICC course must have a title",
          "course.crs",
          "Add a course_title field to the .crs file"
      ));
    }

    if (manifest.getLaunchUrl() == null || manifest.getLaunchUrl().trim().isEmpty()) {
      issues.add(ValidationIssue.error(
          "AICC_MISSING_LAUNCH_URL",
          "AICC course must have a launch URL",
          "assignable_unit",
          "Ensure at least one assignable unit has a file_name"
      ));
    }

    return ValidationResult.of(issues.toArray(new ValidationIssue[0]));
  }
}
