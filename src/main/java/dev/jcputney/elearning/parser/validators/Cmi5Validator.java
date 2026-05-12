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
   * Validates a cmi5 manifest for structural integrity. Uses rule-based validation for better
   * testability and maintainability.
   *
   * @param manifest The cmi5 manifest to validate
   * @return ValidationResult containing any issues found
   */
  public ValidationResult validate(Cmi5Manifest manifest) {
    return rules
        .stream()
        .map(rule -> rule.validate(manifest))
        .reduce(ValidationResult.valid(), ValidationResult::merge);
  }
}
