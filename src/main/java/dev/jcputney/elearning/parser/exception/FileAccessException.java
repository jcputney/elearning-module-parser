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
 * Exception thrown when there's an error accessing files within a module.
 *
 * <p>This exception is typically thrown by {@link dev.jcputney.elearning.parser.api.FileAccess}
 * implementations when they encounter errors while accessing files, such as:
 * <ul>
 *   <li>File not found</li>
 *   <li>Permission denied</li>
 *   <li>I/O errors</li>
 *   <li>Network errors (for remote file access)</li>
 * </ul>
 */
public final class FileAccessException extends ModuleException {

  /**
   * Constructs a new FileAccessException with the specified detail message.
   *
   * @param message the detail message (which is saved for later retrieval by the
   * {@link #getMessage()} method)
   */
  public FileAccessException(String message) {
    super(message);
  }

  /**
   * Constructs a new FileAccessException with the specified detail message and cause.
   *
   * <p>Note that the detail message associated with {@code cause} is <i>not</i> automatically
   * incorporated into this exception's detail message.
   *
   * @param message the detail message (which is saved for later retrieval by the
   * {@link #getMessage()} method)
   * @param cause the cause (which is saved for later retrieval by the {@link #getCause()} method).
   * A null value is permitted and indicates that the cause is nonexistent or unknown.
   */
  public FileAccessException(String message, Throwable cause) {
    super(message, cause);
  }
}