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
 * Exception thrown when there's a runtime error related to file access. This exception is a wrapper
 * for other exceptions that may occur during file access operations.
 */
public final class RuntimeFileAccessException extends RuntimeException {

  /**
   * Constructs a new RuntimeFileAccessException with the specified detail message.
   *
   * @param cause the cause of the exception
   */
  public RuntimeFileAccessException(Exception cause) {
    super(cause);
  }
}
