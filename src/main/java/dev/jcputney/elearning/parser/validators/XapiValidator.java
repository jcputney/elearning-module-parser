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
import dev.jcputney.elearning.parser.validation.ValidationResult;
import dev.jcputney.elearning.parser.validators.rules.ValidationRule;
import dev.jcputney.elearning.parser.validators.rules.xapi.ActivitiesRequiredRule;
import dev.jcputney.elearning.parser.validators.rules.xapi.LaunchUrlRequiredRule;
import java.util.Arrays;
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

  private final List<ValidationRule<TincanManifest>> rules;

  /**
   * Constructs a new XapiValidator with default validation rules.
   */
  public XapiValidator() {
    this.rules = Arrays.asList(
        new ActivitiesRequiredRule(),
        new LaunchUrlRequiredRule()
    );
  }

  /**
   * Validates an xAPI/TinCan manifest for structural integrity.
   * Uses rule-based validation for better testability and maintainability.
   *
   * @param manifest The xAPI manifest to validate
   * @return ValidationResult containing any issues found
   */
  public ValidationResult validate(TincanManifest manifest) {
    return rules.stream()
        .map(rule -> rule.validate(manifest))
        .reduce(ValidationResult.valid(), ValidationResult::merge);
  }
}
