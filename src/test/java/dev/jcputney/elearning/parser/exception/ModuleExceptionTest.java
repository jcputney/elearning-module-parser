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
