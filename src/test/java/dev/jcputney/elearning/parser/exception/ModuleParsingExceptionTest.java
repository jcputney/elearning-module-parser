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

import java.io.IOException;
import javax.xml.stream.XMLStreamException;
import org.junit.jupiter.api.Test;

/**
 * Test class for ModuleParsingException.
 */
class ModuleParsingExceptionTest {

  @Test
  void testConstructor_WithMessage_Success() {
    // Act
    ModuleParsingException exception = new ModuleParsingException("Parse error");

    // Assert
    assertEquals("Parse error", exception.getMessage());
    assertNull(exception.getCause());
  }

  @Test
  void testConstructor_WithMessageAndCause_Success() {
    // Arrange
    Throwable cause = new RuntimeException("Underlying cause");

    // Act
    ModuleParsingException exception = new ModuleParsingException("Parse error", cause);

    // Assert
    assertEquals("Parse error", exception.getMessage());
    assertSame(cause, exception.getCause());
  }

  @Test
  void testConstructor_WithNullMessage_Success() {
    // Act
    ModuleParsingException exception = new ModuleParsingException(null);

    // Assert
    assertNull(exception.getMessage());
    assertNull(exception.getCause());
  }

  @Test
  void testConstructor_WithNullCause_Success() {
    // Act
    ModuleParsingException exception = new ModuleParsingException("Parse error", null);

    // Assert
    assertEquals("Parse error", exception.getMessage());
    assertNull(exception.getCause());
  }

  @Test
  void testInheritance_ExtendsModuleException() {
    // Arrange
    ModuleParsingException exception = new ModuleParsingException("Parse error");

    // Assert
    assertInstanceOf(ModuleException.class, exception);
    assertInstanceOf(Exception.class, exception);
    assertInstanceOf(Throwable.class, exception);
  }

  @Test
  void testRealisticScenario_ManifestParsingError_Success() {
    // Arrange
    Throwable xmlException = new XMLStreamException("Invalid XML at line 15");

    // Act
    ModuleParsingException exception = new ModuleParsingException(
        "Failed to parse SCORM manifest", xmlException);

    // Assert
    assertEquals("Failed to parse SCORM manifest", exception.getMessage());
    assertInstanceOf(XMLStreamException.class, exception.getCause());
    assertEquals("Invalid XML at line 15", exception.getCause().getMessage());
  }

  @Test
  void testRealisticScenario_MissingRequiredElement_Success() {
    // Arrange & Act
    ModuleParsingException exception = new ModuleParsingException(
        "Missing required element: <title>");

    // Assert
    assertEquals("Missing required element: <title>", exception.getMessage());
  }

  @Test
  void testRealisticScenario_InvalidModuleStructure_Success() {
    // Arrange
    IOException ioException = new IOException("File not found: launch.html");

    // Act
    ModuleParsingException exception = new ModuleParsingException(
        "Invalid module structure: missing launch file", ioException);

    // Assert
    assertEquals("Invalid module structure: missing launch file", exception.getMessage());
    assertInstanceOf(IOException.class, exception.getCause());
  }

  @Test
  void testChainedExceptions_MultipleWrappingLevels_Success() {
    // Arrange
    RuntimeException rootCause = new RuntimeException("Database connection failed");
    IOException ioException = new IOException("Failed to read metadata", rootCause);

    // Act
    ModuleParsingException exception = new ModuleParsingException("Module parsing failed",
        ioException);

    // Assert
    assertEquals("Module parsing failed", exception.getMessage());
    assertSame(ioException, exception.getCause());
    assertSame(rootCause, exception.getCause().getCause());
  }

  @Test
  void testEmptyMessage_Success() {
    // Act
    ModuleParsingException exception = new ModuleParsingException("");

    // Assert
    assertEquals("", exception.getMessage());
    assertNull(exception.getCause());
  }
}
