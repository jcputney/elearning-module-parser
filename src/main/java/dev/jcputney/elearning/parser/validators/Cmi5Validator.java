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
import dev.jcputney.elearning.parser.validation.ValidationResult;
import dev.jcputney.elearning.parser.validators.rules.ValidationRule;
import dev.jcputney.elearning.parser.validators.rules.cmi5.CourseRequiredRule;
import dev.jcputney.elearning.parser.validators.rules.cmi5.LaunchUrlRequiredRule;
import dev.jcputney.elearning.parser.validators.rules.cmi5.TitleRequiredRule;
import java.util.Arrays;
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

  private final List<ValidationRule<Cmi5Manifest>> rules;

  /**
   * Constructs a new Cmi5Validator with default validation rules.
   */
  public Cmi5Validator() {
    this.rules = Arrays.asList(
        new CourseRequiredRule(),
        new TitleRequiredRule(),
        new LaunchUrlRequiredRule()
    );
  }

  /**
   * Validates a cmi5 manifest for structural integrity.
   * Uses rule-based validation for better testability and maintainability.
   *
   * @param manifest The cmi5 manifest to validate
   * @return ValidationResult containing any issues found
   */
  public ValidationResult validate(Cmi5Manifest manifest) {
    return rules.stream()
        .map(rule -> rule.validate(manifest))
        .reduce(ValidationResult.valid(), ValidationResult::merge);
  }
}
