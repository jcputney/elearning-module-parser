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
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashMap;
import java.util.Map;
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
    assertTrue(exception
        .getMetadata()
        .isEmpty());
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
    assertTrue(exception
        .getMetadata()
        .isEmpty());
  }

  @Test
  void testConstructor_WithMessageCauseAndMetadata_Success() {
    // Arrange
    Throwable cause = new RuntimeException("Cause message");
    Map<String, Object> metadata = new HashMap<>();
    metadata.put("key1", "value1");
    metadata.put("key2", 42);

    // Act
    ModuleException exception = new ModuleException("Test message", cause, metadata);

    // Assert
    assertEquals("Test message", exception.getMessage());
    assertSame(cause, exception.getCause());
    assertEquals(2, exception
        .getMetadata()
        .size());
    assertEquals("value1", exception.getMetadata("key1"));
    assertEquals(42, exception.getMetadata("key2"));
  }

  @Test
  void testConstructor_WithNullMetadata_Success() {
    // Arrange
    Throwable cause = new RuntimeException("Cause message");

    // Act
    ModuleException exception = new ModuleException("Test message", cause, null);

    // Assert
    assertEquals("Test message", exception.getMessage());
    assertSame(cause, exception.getCause());
    assertTrue(exception
        .getMetadata()
        .isEmpty());
  }

  @Test
  void testConstructor_WithEmptyMetadata_Success() {
    // Arrange
    Throwable cause = new RuntimeException("Cause message");
    Map<String, Object> metadata = new HashMap<>();

    // Act
    ModuleException exception = new ModuleException("Test message", cause, metadata);

    // Assert
    assertEquals("Test message", exception.getMessage());
    assertSame(cause, exception.getCause());
    assertTrue(exception
        .getMetadata()
        .isEmpty());
  }

  @Test
  void testAddMetadata_ValidKeyValue_Success() {
    // Arrange
    ModuleException exception = new ModuleException("Test message");

    // Act
    ModuleException result = exception.addMetadata("testKey", "testValue");

    // Assert
    assertSame(exception, result); // Method chaining
    assertEquals("testValue", exception.getMetadata("testKey"));
    assertEquals(1, exception
        .getMetadata()
        .size());
  }

  @Test
  void testAddMetadata_NullKey_DoesNotAddMetadata() {
    // Arrange
    ModuleException exception = new ModuleException("Test message");

    // Act
    ModuleException result = exception.addMetadata(null, "testValue");

    // Assert
    assertSame(exception, result); // Method chaining still works
    assertTrue(exception
        .getMetadata()
        .isEmpty());
  }

  @Test
  void testAddMetadata_NullValue_AddsNullValue() {
    // Arrange
    ModuleException exception = new ModuleException("Test message");

    // Act
    exception.addMetadata("testKey", null);

    // Assert
    assertTrue(exception
        .getMetadata()
        .containsKey("testKey"));
    assertNull(exception.getMetadata("testKey"));
    assertEquals(1, exception
        .getMetadata()
        .size());
  }

  @Test
  void testAddMetadata_MultipleValues_Success() {
    // Arrange
    ModuleException exception = new ModuleException("Test message");

    // Act
    exception
        .addMetadata("key1", "value1")
        .addMetadata("key2", 42)
        .addMetadata("key3", true);

    // Assert
    assertEquals(3, exception
        .getMetadata()
        .size());
    assertEquals("value1", exception.getMetadata("key1"));
    assertEquals(42, exception.getMetadata("key2"));
    assertEquals(true, exception.getMetadata("key3"));
  }

  @Test
  void testAddMetadata_OverwriteExistingKey_Success() {
    // Arrange
    ModuleException exception = new ModuleException("Test message");
    exception.addMetadata("key", "original");

    // Act
    exception.addMetadata("key", "updated");

    // Assert
    assertEquals("updated", exception.getMetadata("key"));
    assertEquals(1, exception
        .getMetadata()
        .size());
  }

  @Test
  void testGetMetadata_ExistingKey_ReturnsValue() {
    // Arrange
    ModuleException exception = new ModuleException("Test message");
    exception.addMetadata("testKey", "testValue");

    // Act
    Object result = exception.getMetadata("testKey");

    // Assert
    assertEquals("testValue", result);
  }

  @Test
  void testGetMetadata_NonExistingKey_ReturnsNull() {
    // Arrange
    ModuleException exception = new ModuleException("Test message");

    // Act
    Object result = exception.getMetadata("nonExistentKey");

    // Assert
    assertNull(result);
  }

  @Test
  void testGetMetadata_NullKey_ReturnsNull() {
    // Arrange
    ModuleException exception = new ModuleException("Test message");

    // Act
    Object result = exception.getMetadata(null);

    // Assert
    assertNull(result);
  }

  @Test
  void testGetMetadata_AllMetadata_ReturnsUnmodifiableMap() {
    // Arrange
    ModuleException exception = new ModuleException("Test message");
    exception
        .addMetadata("key1", "value1")
        .addMetadata("key2", "value2");

    // Act
    Map<String, Object> metadata = exception.getMetadata();

    // Assert
    assertEquals(2, metadata.size());
    assertEquals("value1", metadata.get("key1"));
    assertEquals("value2", metadata.get("key2"));

    // Verify map is unmodifiable
    assertThrows(UnsupportedOperationException.class, () -> {
      metadata.put("key3", "value3");
    });
  }

  @Test
  void testGetMetadata_EmptyMetadata_ReturnsEmptyMap() {
    // Arrange
    ModuleException exception = new ModuleException("Test message");

    // Act
    Map<String, Object> metadata = exception.getMetadata();

    // Assert
    assertTrue(metadata.isEmpty());
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
    assertFalse(result.contains("Metadata"));
  }

  @Test
  void testToString_MessageWithMetadata_Success() {
    // Arrange
    ModuleException exception = new ModuleException("Test message");
    exception
        .addMetadata("key1", "value1")
        .addMetadata("key2", 42);

    // Act
    String result = exception.toString();

    // Assert
    assertTrue(result.contains("ModuleException"));
    assertTrue(result.contains("Test message"));
    assertTrue(result.contains("Metadata"));
    assertTrue(result.contains("key1"));
    assertTrue(result.contains("value1"));
    assertTrue(result.contains("key2"));
    assertTrue(result.contains("42"));
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
    // Note: toString() doesn't include cause information in the basic implementation
  }

  @Test
  void testInheritance_ExtendsException() {
    // Arrange
    ModuleException exception = new ModuleException("Test message");

    // Assert
    assertInstanceOf(Exception.class, exception);
    assertInstanceOf(Throwable.class, exception);
  }

  @Test
  void testMetadataDataTypes_VariousTypes_Success() {
    // Arrange
    ModuleException exception = new ModuleException("Test message");

    // Act
    exception
        .addMetadata("string", "text")
        .addMetadata("integer", 123)
        .addMetadata("boolean", true)
        .addMetadata("double", 45.67)
        .addMetadata("object", new Object())
        .addMetadata("null", null);

    // Assert
    assertEquals("text", exception.getMetadata("string"));
    assertEquals(123, exception.getMetadata("integer"));
    assertEquals(true, exception.getMetadata("boolean"));
    assertEquals(45.67, exception.getMetadata("double"));
    assertNotNull(exception.getMetadata("object"));
    assertNull(exception.getMetadata("null"));
    assertEquals(6, exception
        .getMetadata()
        .size());
  }

  @Test
  void testConstructorMetadata_DoesNotModifyOriginal() {
    // Arrange
    Map<String, Object> originalMetadata = new HashMap<>();
    originalMetadata.put("key", "value");
    ModuleException exception = new ModuleException("Test message", null, originalMetadata);

    // Act - Modify original metadata after construction
    originalMetadata.put("newKey", "newValue");

    // Assert - Exception's metadata should not be affected
    assertEquals(1, exception
        .getMetadata()
        .size());
    assertEquals("value", exception.getMetadata("key"));
    assertNull(exception.getMetadata("newKey"));
  }
}