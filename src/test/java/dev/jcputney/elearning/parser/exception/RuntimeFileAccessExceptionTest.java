/* Copyright (c) 2024-2025. Jonathan Putney
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