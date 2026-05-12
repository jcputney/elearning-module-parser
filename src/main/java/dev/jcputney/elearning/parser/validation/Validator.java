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
package dev.jcputney.elearning.parser.validation;

/**
 * Functional interface for validators that check module manifests for issues. Validators should be
 * stateless and return immutable ValidationResult objects.
 *
 * @param <T> The type of object being validated (e.g., ScormManifest, AiccCourse)
 */
@FunctionalInterface
public interface Validator<T> {

  /**
   * Validates the target object and returns any issues found.
   *
   * @param target The object to validate
   * @return ValidationResult containing any errors or warnings
   */
  ValidationResult validate(T target);
}
