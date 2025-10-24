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
import dev.jcputney.elearning.parser.validation.ValidationResult;
import dev.jcputney.elearning.parser.validators.rules.ValidationRule;
import dev.jcputney.elearning.parser.validators.rules.aicc.CourseRequiredRule;
import dev.jcputney.elearning.parser.validators.rules.aicc.LaunchUrlRequiredRule;
import dev.jcputney.elearning.parser.validators.rules.aicc.TitleRequiredRule;
import java.util.Arrays;
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

  private final List<ValidationRule<AiccManifest>> rules;

  /**
   * Constructs a new AiccValidator with default validation rules.
   */
  public AiccValidator() {
    this.rules = Arrays.asList(
        new CourseRequiredRule(),
        new TitleRequiredRule(),
        new LaunchUrlRequiredRule()
    );
  }

  /**
   * Validates an AICC manifest for structural integrity.
   * Uses rule-based validation for better testability and maintainability.
   *
   * @param manifest The AICC manifest to validate
   * @return ValidationResult containing any issues found
   */
  public ValidationResult validate(AiccManifest manifest) {
    return rules.stream()
        .map(rule -> rule.validate(manifest))
        .reduce(ValidationResult.valid(), ValidationResult::merge);
  }
}
