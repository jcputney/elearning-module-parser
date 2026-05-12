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
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

/**
 * Test class for ModuleException.
 */
class ModuleExceptionTest {

  @Test
  void testConstructor_WithMessage_Success() {
    // Act
    ModuleException exception = new ModuleException("Test message");

    // Assert
    assertEquals("Test message", exception.getMessage());
    assertNull(exception.getCause());
  }

  @Test
  void testConstructor_WithMessageAndCause_Success() {
    // Arrange
    Throwable cause = new RuntimeException("Cause message");

    // Act
    ModuleException exception = new ModuleException("Test message", cause);

    // Assert
    assertEquals("Test message", exception.getMessage());
    assertSame(cause, exception.getCause());
  }

  @Test
  void testToString_MessageOnly_Success() {
    // Arrange
    ModuleException exception = new ModuleException("Test message");

    // Act
    String result = exception.toString();

    // Assert
    assertTrue(result.contains("ModuleException"));
    assertTrue(result.contains("Test message"));
  }

  @Test
  void testToString_NullMessage_Success() {
    // Arrange
    ModuleException exception = new ModuleException(null);

    // Act
    String result = exception.toString();

    // Assert
    assertTrue(result.contains("ModuleException"));
    assertEquals("dev.jcputney.elearning.parser.exception.ModuleException", result);
  }

  @Test
  void testToString_EmptyMessage_Success() {
    // Arrange
    ModuleException exception = new ModuleException("");

    // Act
    String result = exception.toString();

    // Assert
    assertTrue(result.contains("ModuleException"));
    assertTrue(result.contains(": "));
  }

  @Test
  void testToString_WithCause_Success() {
    // Arrange
    Throwable cause = new RuntimeException("Cause message");
    ModuleException exception = new ModuleException("Test message", cause);

    // Act
    String result = exception.toString();

    // Assert
    assertTrue(result.contains("ModuleException"));
    assertTrue(result.contains("Test message"));
  }

  @Test
  void testInheritance_ExtendsException() {
    // Arrange
    ModuleException exception = new ModuleException("Test message");

    // Assert
    assertInstanceOf(Exception.class, exception);
    assertInstanceOf(Throwable.class, exception);
  }
}
