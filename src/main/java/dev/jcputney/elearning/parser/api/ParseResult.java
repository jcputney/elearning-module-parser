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
package dev.jcputney.elearning.parser.api;

import dev.jcputney.elearning.parser.input.PackageManifest;
import dev.jcputney.elearning.parser.output.ModuleMetadata;
import dev.jcputney.elearning.parser.validation.ValidationResult;

/**
 * Result of parsing and validating an eLearning module. Contains both the validation result and the
 * extracted metadata.
 * <p>
 * This record is returned by {@link ModuleParser#parseAndValidate()} to provide both validation
 * feedback and parsed content in a single operation, eliminating the need to parse the manifest
 * twice.
 * </p>
 *
 * @param <M> The type of package manifest associated with the module
 * @param validation The validation result containing any errors or warnings found
 * @param metadata The extracted module metadata (always present, even if validation failed)
 */
public record ParseResult<M extends PackageManifest>(
    ValidationResult validation,
    ModuleMetadata<M> metadata
) {

  /**
   * Checks if the module passed validation without errors. Warnings are not considered failures.
   *
   * @return true if validation passed (no errors), false otherwise
   */
  public boolean isValid() {
    return validation.isValid();
  }

  /**
   * Checks if the module has any validation errors.
   *
   * @return true if there are validation errors, false otherwise
   */
  public boolean hasErrors() {
    return validation.hasErrors();
  }

  /**
   * Checks if the module has any validation warnings.
   *
   * @return true if there are validation warnings, false otherwise
   */
  public boolean hasWarnings() {
    return validation.hasWarnings();
  }
}
