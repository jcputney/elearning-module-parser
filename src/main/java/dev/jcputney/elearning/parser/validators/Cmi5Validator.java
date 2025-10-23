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

import dev.jcputney.elearning.parser.input.cmi5.Cmi5Manifest;
import dev.jcputney.elearning.parser.validation.ValidationIssue;
import dev.jcputney.elearning.parser.validation.ValidationResult;
import java.util.ArrayList;
import java.util.List;

/**
 * Validator for cmi5 manifests.
 * <p>
 * This validator checks that:
 * </p>
 * <ul>
 *   <li>Required course structure exists</li>
 *   <li>AUs have valid launch URLs</li>
 *   <li>Metadata is properly defined</li>
 * </ul>
 */
public class Cmi5Validator {

  /**
   * Validates a cmi5 manifest for structural integrity.
   *
   * @param manifest The cmi5 manifest to validate
   * @return ValidationResult containing any issues found
   */
  public ValidationResult validate(Cmi5Manifest manifest) {
    List<ValidationIssue> issues = new ArrayList<>();

    // Validate basic structure
    if (manifest.getCourse() == null) {
      issues.add(ValidationIssue.error(
          "CMI5_MISSING_COURSE",
          "cmi5 manifest must contain course element",
          "cmi5.xml/course"
      ));
    }

    if (manifest.getTitle() == null || manifest.getTitle().isEmpty()) {
      issues.add(ValidationIssue.error(
          "CMI5_MISSING_TITLE",
          "cmi5 course must have a title",
          "cmi5.xml/course/title",
          "Add a <title> element to the course"
      ));
    }

    if (manifest.getLaunchUrl() == null || manifest.getLaunchUrl().trim().isEmpty()) {
      issues.add(ValidationIssue.error(
          "CMI5_MISSING_LAUNCH_URL",
          "cmi5 course must have at least one AU with a launch URL",
          "cmi5.xml/course/au",
          "Ensure at least one AU has a url attribute"
      ));
    }

    return ValidationResult.of(issues.toArray(new ValidationIssue[0]));
  }
}
