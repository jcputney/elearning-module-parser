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

  /**
   * Represents the result of validation performed during the parsing of a module. This field
   * contains an immutable {@link ValidationResult} instance, encapsulating all validation issues,
   * such as errors and warnings, encountered during the parsing process.
   * <p>
   * The validation result provides various methods to retrieve detailed information about the
   * issues, such as severity, error messages, and suggestions for fixes.
   * <p>
   * This field is used in the context of exceptions to describe validation failures and provides a
   * mechanism to access the full list of issues for further processing or reporting.
   */
  private final ValidationResult validationResult;

  /**
   * Constructs a new ModuleParsingException with validation result. This constructor should only be
   * called from ValidationResult.toException().
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
