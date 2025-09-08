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
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
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
    assertTrue(exception
        .getMetadata()
        .isEmpty());
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
    assertTrue(exception
        .getMetadata()
        .isEmpty());
  }

  @Test
  void testConstructor_WithMessageCauseAndMetadata_Success() {
    // Arrange
    Throwable cause = new RuntimeException("Underlying cause");
    Map<String, Object> metadata = new HashMap<>();
    metadata.put("moduleType", "SCORM");
    metadata.put("lineNumber", 42);

    // Act
    ModuleParsingException exception = new ModuleParsingException("Parse error", cause, metadata);

    // Assert
    assertEquals("Parse error", exception.getMessage());
    assertSame(cause, exception.getCause());
    assertEquals(2, exception
        .getMetadata()
        .size());
    assertEquals("SCORM", exception.getMetadata("moduleType"));
    assertEquals(42, exception.getMetadata("lineNumber"));
  }

  @Test
  void testConstructor_WithNullMessage_Success() {
    // Act
    ModuleParsingException exception = new ModuleParsingException(null);

    // Assert
    assertNull(exception.getMessage());
    assertNull(exception.getCause());
    assertTrue(exception
        .getMetadata()
        .isEmpty());
  }

  @Test
  void testConstructor_WithNullCause_Success() {
    // Act
    ModuleParsingException exception = new ModuleParsingException("Parse error", null);

    // Assert
    assertEquals("Parse error", exception.getMessage());
    assertNull(exception.getCause());
    assertTrue(exception
        .getMetadata()
        .isEmpty());
  }

  @Test
  void testConstructor_WithNullMetadata_Success() {
    // Arrange
    Throwable cause = new RuntimeException("Underlying cause");

    // Act
    ModuleParsingException exception = new ModuleParsingException("Parse error", cause, null);

    // Assert
    assertEquals("Parse error", exception.getMessage());
    assertSame(cause, exception.getCause());
    assertTrue(exception
        .getMetadata()
        .isEmpty());
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
  void testInheritedMetadataFunctionality_AddMetadata_Success() {
    // Arrange
    ModuleParsingException exception = new ModuleParsingException("Parse error");

    // Act
    exception
        .addMetadata("fileName", "manifest.xml")
        .addMetadata("errorCode", "INVALID_XML");

    // Assert
    assertEquals("manifest.xml", exception.getMetadata("fileName"));
    assertEquals("INVALID_XML", exception.getMetadata("errorCode"));
    assertEquals(2, exception
        .getMetadata()
        .size());
  }

  @Test
  void testInheritedMetadataFunctionality_GetMetadata_Success() {
    // Arrange
    Map<String, Object> metadata = new HashMap<>();
    metadata.put("parser", "SCORM12Parser");
    metadata.put("severity", "HIGH");
    ModuleParsingException exception = new ModuleParsingException("Parse error", null, metadata);

    // Act & Assert
    assertEquals("SCORM12Parser", exception.getMetadata("parser"));
    assertEquals("HIGH", exception.getMetadata("severity"));
    assertNull(exception.getMetadata("nonexistent"));
  }

  @Test
  void testInheritedToString_WithMetadata_Success() {
    // Arrange
    ModuleParsingException exception = new ModuleParsingException("Parse error");
    exception
        .addMetadata("file", "imsmanifest.xml")
        .addMetadata("line", 25);

    // Act
    String result = exception.toString();

    // Assert
    assertTrue(result.contains("ModuleParsingException"));
    assertTrue(result.contains("Parse error"));
    assertTrue(result.contains("Metadata"));
    assertTrue(result.contains("file"));
    assertTrue(result.contains("imsmanifest.xml"));
    assertTrue(result.contains("line"));
    assertTrue(result.contains("25"));
  }

  @Test
  void testRealisticScenario_ManifestParsingError_Success() {
    // Arrange
    Throwable xmlException = new javax.xml.stream.XMLStreamException("Invalid XML at line 15");
    Map<String, Object> context = new HashMap<>();
    context.put("fileName", "imsmanifest.xml");
    context.put("moduleType", "SCORM 1.2");
    context.put("rootPath", "/modules/course123");

    // Act
    ModuleParsingException exception = new ModuleParsingException(
        "Failed to parse SCORM manifest", xmlException, context);

    // Assert
    assertEquals("Failed to parse SCORM manifest", exception.getMessage());
    assertInstanceOf(XMLStreamException.class, exception.getCause());
    assertEquals("Invalid XML at line 15", exception
        .getCause()
        .getMessage());
    assertEquals("imsmanifest.xml", exception.getMetadata("fileName"));
    assertEquals("SCORM 1.2", exception.getMetadata("moduleType"));
    assertEquals("/modules/course123", exception.getMetadata("rootPath"));
  }

  @Test
  void testRealisticScenario_MissingRequiredElement_Success() {
    // Arrange
    ModuleParsingException exception = new ModuleParsingException(
        "Missing required element: <title>");
    exception
        .addMetadata("element", "title")
        .addMetadata("parentElement", "metadata")
        .addMetadata("required", true)
        .addMetadata("specification", "SCORM 2004");

    // Act & Assert
    assertEquals("Missing required element: <title>", exception.getMessage());
    assertEquals("title", exception.getMetadata("element"));
    assertEquals("metadata", exception.getMetadata("parentElement"));
    assertEquals(true, exception.getMetadata("required"));
    assertEquals("SCORM 2004", exception.getMetadata("specification"));
  }

  @Test
  void testRealisticScenario_InvalidModuleStructure_Success() {
    // Arrange
    java.io.IOException ioException = new java.io.IOException("File not found: launch.html");
    ModuleParsingException exception = new ModuleParsingException(
        "Invalid module structure: missing launch file", ioException);

    exception
        .addMetadata("missingFile", "launch.html")
        .addMetadata("expectedLocation", "content/")
        .addMetadata("moduleId", "course-123")
        .addMetadata("validationStage", "structure-check");

    // Act & Assert
    assertEquals("Invalid module structure: missing launch file", exception.getMessage());
    assertInstanceOf(IOException.class, exception.getCause());
    assertEquals("launch.html", exception.getMetadata("missingFile"));
    assertEquals("content/", exception.getMetadata("expectedLocation"));
    assertEquals("course-123", exception.getMetadata("moduleId"));
    assertEquals("structure-check", exception.getMetadata("validationStage"));
  }

  @Test
  void testChainedExceptions_MultipleWrappingLevels_Success() {
    // Arrange
    RuntimeException rootCause = new RuntimeException("Database connection failed");
    java.io.IOException ioException = new java.io.IOException("Failed to read metadata", rootCause);
    ModuleParsingException exception = new ModuleParsingException("Module parsing failed",
        ioException);

    // Act & Assert
    assertEquals("Module parsing failed", exception.getMessage());
    assertSame(ioException, exception.getCause());
    assertSame(rootCause, exception
        .getCause()
        .getCause());
  }

  @Test
  void testEmptyMessage_Success() {
    // Act
    ModuleParsingException exception = new ModuleParsingException("");

    // Assert
    assertEquals("", exception.getMessage());
    assertNull(exception.getCause());
    assertTrue(exception
        .getMetadata()
        .isEmpty());
  }

  @Test
  void testComplexMetadata_NestedObjects_Success() {
    // Arrange
    Map<String, Object> nestedData = new HashMap<>();
    nestedData.put("validationRules", java.util.Arrays.asList("required-title", "valid-duration"));
    nestedData.put("errorCount", 3);

    ModuleParsingException exception = new ModuleParsingException("Validation failed");
    exception
        .addMetadata("validationResults", nestedData)
        .addMetadata("timestamp", java.time.Instant.now())
        .addMetadata("validator", "DefaultValidator");

    // Act & Assert
    assertEquals(nestedData, exception.getMetadata("validationResults"));
    assertNotNull(exception.getMetadata("timestamp"));
    assertEquals("DefaultValidator", exception.getMetadata("validator"));
  }
}