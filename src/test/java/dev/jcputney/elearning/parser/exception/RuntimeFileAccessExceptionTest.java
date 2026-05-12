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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

import org.junit.jupiter.api.Test;

/**
 * Tests for {@link RuntimeFileAccessException} to ensure proper exception handling.
 */
class RuntimeFileAccessExceptionTest {

  /**
   * Tests that the exception can be created with a cause and that the cause is properly stored.
   */
  @Test
  void constructor_withCause_storesCause() {
    // Arrange
    Exception cause = new Exception("Test cause");

    // Act
    RuntimeFileAccessException exception = new RuntimeFileAccessException(cause);

    // Assert
    assertSame(cause, exception.getCause());
  }

  /**
   * Tests that the exception message includes the cause message.
   */
  @Test
  void getMessage_withCause_includesCauseMessage() {
    // Arrange
    Exception cause = new Exception("Test cause");

    // Act
    RuntimeFileAccessException exception = new RuntimeFileAccessException(cause);

    // Assert
    assertEquals(cause.toString(), exception.getMessage());
  }
}