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

/**
 * Base exception class for all module-related exceptions in the elearning-module-parser library.
 *
 * <p>This class provides a foundation for a more extensible error handling framework by:
 * <ul>
 *   <li>Establishing a common base class for all module-related exceptions</li>
 *   <li>Providing consistent constructors and methods across all exception types</li>
 * </ul>
 *
 * <p>All specific exception types in the library should extend this class or one of its subclasses.
 */
public sealed class ModuleException extends Exception permits FileAccessException,
    ManifestParseException, ModuleDetectionException, ModuleParsingException {

  /**
   * Constructs a new ModuleException with the specified detail message.
   *
   * @param message the detail message (which is saved for later retrieval by the
   * {@link #getMessage()} method)
   */
  public ModuleException(String message) {
    super(message);
  }

  /**
   * Constructs a new ModuleException with the specified detail message and cause.
   *
   * @param message the detail message (which is saved for later retrieval by the
   * {@link #getMessage()} method)
   * @param cause the cause (which is saved for later retrieval by the {@link #getCause()} method).
   * A null value is permitted and indicates that the cause is nonexistent or unknown.
   */
  public ModuleException(String message, Throwable cause) {
    super(message, cause);
  }
}