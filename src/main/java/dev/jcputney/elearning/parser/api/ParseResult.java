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

package dev.jcputney.elearning.parser.api;

import dev.jcputney.elearning.parser.input.PackageManifest;
import dev.jcputney.elearning.parser.output.ModuleMetadata;
import dev.jcputney.elearning.parser.validation.ValidationResult;

/**
 * Result of parsing and validating an eLearning module.
 * Contains both the validation result and the extracted metadata.
 * <p>
 * This record is returned by {@link ModuleParser#parseAndValidate()} to provide
 * both validation feedback and parsed content in a single operation, eliminating
 * the need to parse the manifest twice.
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
   * Checks if the module passed validation without errors.
   * Warnings are not considered failures.
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
