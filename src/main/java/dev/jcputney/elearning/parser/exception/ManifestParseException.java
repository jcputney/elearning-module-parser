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
 * Exception thrown when there's an error parsing a module manifest file.
 *
 * <p>This exception is typically thrown by parser implementations when they encounter
 * errors while parsing manifest files (e.g., imsmanifest.xml for SCORM, .crs/.des/.au/.cst for
 * AICC, cmi5.xml for cmi5).
 *
 * <p>Common scenarios that might cause this exception include:
 * <ul>
 *   <li>Malformed XML or INI files</li>
 *   <li>Missing required elements in the manifest</li>
 *   <li>Invalid format or structure in the manifest</li>
 * </ul>
 */
public final class ManifestParseException extends ModuleException {

  /**
   * Constructs a new ManifestParseException with the specified detail message.
   *
   * @param message the detail message (which is saved for later retrieval by the
   * {@link #getMessage()} method)
   */
  public ManifestParseException(String message) {
    super(message);
  }

  /**
   * Constructs a new ManifestParseException with the specified detail message and cause.
   *
   * <p>Note that the detail message associated with {@code cause} is <i>not</i> automatically
   * incorporated into this exception's detail message.
   *
   * @param message the detail message (which is saved for later retrieval by the
   * {@link #getMessage()} method)
   * @param cause the cause (which is saved for later retrieval by the {@link #getCause()} method).
   * A null value is permitted and indicates that the cause is nonexistent or unknown.
   */
  public ManifestParseException(String message, Throwable cause) {
    super(message, cause);
  }
}
