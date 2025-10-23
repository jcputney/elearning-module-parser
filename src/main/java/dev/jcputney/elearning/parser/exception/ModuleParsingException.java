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

package dev.jcputney.elearning.parser.exception;

import dev.jcputney.elearning.parser.api.ModuleParser;
import dev.jcputney.elearning.parser.validation.ValidationResult;

/**
 * Exception thrown when there's an error parsing an eLearning module.
 *
 * <p>This exception is typically thrown by {@link ModuleParser}
 * implementations when they encounter errors while parsing module content. It is a higher-level
 * exception that may wrap more specific exceptions like {@link ManifestParseException}.
 *
 * <p>Common scenarios that might cause this exception include:
 * <ul>
 *   <li>Missing or invalid manifest files</li>
 *   <li>Missing required metadata elements</li>
 *   <li>Invalid module structure</li>
 *   <li>I/O errors when accessing module files</li>
 * </ul>
 *
 * <p>Applications should catch this exception and provide appropriate feedback to users about
 * the invalid or problematic module.
 */
public final class ModuleParsingException extends ModuleException {

  private final ValidationResult validationResult;

  /**
   * Constructs a new ModuleParsingException with validation result.
   * This constructor should only be called from ValidationResult.toException().
   *
   * @param contextMessage Context describing what was being parsed
   * @param result ValidationResult containing all validation issues
   */
  public ModuleParsingException(String contextMessage, ValidationResult result) {
    super(contextMessage + ":\n" + result.formatErrors());
    this.validationResult = result;
  }

  /**
   * Gets the validation result containing all issues found during parsing.
   *
   * @return ValidationResult with errors and warnings
   */
  public ValidationResult getValidationResult() {
    return validationResult;
  }
}
