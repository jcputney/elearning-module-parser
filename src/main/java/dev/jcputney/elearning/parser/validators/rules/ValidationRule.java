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
package dev.jcputney.elearning.parser.validators.rules;

import dev.jcputney.elearning.parser.validation.ValidationResult;

/**
 * Interface for individual validation rules that can be composed together. Each rule encapsulates a
 * single validation concern with spec traceability.
 *
 * <p>Rules are designed to be:</p>
 * <ul>
 *   <li>Testable - each rule can be tested independently</li>
 *   <li>Reusable - common rules can be shared across validators</li>
 *   <li>Traceable - each rule references the specification it enforces</li>
 *   <li>Composable - rules are combined in validators via merge()</li>
 * </ul>
 *
 * @param <T> The manifest type this rule validates
 */
public interface ValidationRule<T> {

  /**
   * Validates the manifest according to this rule.
   *
   * @param manifest The manifest to validate (must not be null)
   * @return ValidationResult with any issues found (never null)
   * @throws IllegalArgumentException if manifest is null
   */
  ValidationResult validate(T manifest);

  /**
   * Human-readable name of this rule for logging and debugging.
   *
   * @return Rule name (e.g., "Resource Reference Validation")
   */
  String getRuleName();

  /**
   * Specification reference for traceability and documentation. Used to trace validation back to
   * spec requirements.
   *
   * @return Spec reference (e.g., "SCORM 1.2 CAM Section 2.3.4")
   */
  String getSpecReference();
}
