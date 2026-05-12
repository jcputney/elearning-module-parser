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
   * Validates an AICC manifest for structural integrity. Uses rule-based validation for better
   * testability and maintainability.
   *
   * @param manifest The AICC manifest to validate
   * @return ValidationResult containing any issues found
   */
  public ValidationResult validate(AiccManifest manifest) {
    return rules
        .stream()
        .map(rule -> rule.validate(manifest))
        .reduce(ValidationResult.valid(), ValidationResult::merge);
  }
}
