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

import java.io.IOException;
import org.junit.jupiter.api.Test;

/**
 * Test class for ModuleDetectionException.
 */
class ModuleDetectionExceptionTest {

  @Test
  void testConstructor_WithMessage_Success() {
    // Act
    ModuleDetectionException exception = new ModuleDetectionException("Detection failed");

    // Assert
    assertEquals("Detection failed", exception.getMessage());
    assertNull(exception.getCause());
  }

  @Test
  void testConstructor_WithMessageAndCause_Success() {
    // Arrange
    Throwable cause = new IOException("File access error");

    // Act
    ModuleDetectionException exception = new ModuleDetectionException("Detection failed", cause);

    // Assert
    assertEquals("Detection failed", exception.getMessage());
    assertSame(cause, exception.getCause());
  }

  @Test
  void testConstructor_WithNullMessage_Success() {
    // Act
    ModuleDetectionException exception = new ModuleDetectionException(null);

    // Assert
    assertNull(exception.getMessage());
    assertNull(exception.getCause());
  }

  @Test
  void testConstructor_WithNullCause_Success() {
    // Act
    ModuleDetectionException exception = new ModuleDetectionException("Detection failed", null);

    // Assert
    assertEquals("Detection failed", exception.getMessage());
    assertNull(exception.getCause());
  }

  @Test
  void testInheritance_ExtendsModuleException() {
    // Arrange
    ModuleDetectionException exception = new ModuleDetectionException("Detection failed");

    // Assert
    assertInstanceOf(ModuleException.class, exception);
    assertInstanceOf(Exception.class, exception);
    assertInstanceOf(Throwable.class, exception);
  }

  @Test
  void testRealisticScenario_UnknownModuleType_Success() {
    // Act
    ModuleDetectionException exception = new ModuleDetectionException(
        "Unable to detect module type: no valid manifest files found");

    // Assert
    assertEquals("Unable to detect module type: no valid manifest files found",
        exception.getMessage());
  }

  @Test
  void testRealisticScenario_IOError_Success() {
    // Arrange
    IOException ioException = new IOException("Permission denied: /modules/course123");

    // Act
    ModuleDetectionException exception = new ModuleDetectionException(
        "Failed to access module directory", ioException);

    // Assert
    assertEquals("Failed to access module directory", exception.getMessage());
    assertInstanceOf(IOException.class, exception.getCause());
  }

  @Test
  void testEmptyMessage_Success() {
    // Act
    ModuleDetectionException exception = new ModuleDetectionException("");

    // Assert
    assertEquals("", exception.getMessage());
    assertNull(exception.getCause());
  }
}
