/*
 * Copyright (c) 2024-2026 Jonathan Putney
 *
 * Licensed under the Apache License, Version 2.0 (the "License").
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at the project root LICENSE file
 * or at http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0
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
   * Validates an xAPI/TinCan manifest for structural integrity. Uses rule-based validation for
   * better testability and maintainability.
   *
   * @param manifest The xAPI manifest to validate
   * @return ValidationResult containing any issues found
   */
  public ValidationResult validate(TincanManifest manifest) {
    return rules
        .stream()
        .map(rule -> rule.validate(manifest))
        .reduce(ValidationResult.valid(), ValidationResult::merge);
  }
}
