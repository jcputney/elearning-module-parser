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

package dev.jcputney.elearning.parser.output.metadata;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Test class for SimpleMetadata.
 */
class SimpleMetadataTest {

  private SimpleMetadata metadata;

  @BeforeEach
  void setUp() {
    metadata = new SimpleMetadata();
  }

  @Test
  void testDefaultConstructor_CreatesEmptyMetadata() {
    assertNotNull(metadata);
    assertFalse(metadata.hasMetadata("anyKey"));
  }

  @Test
  void testAddMetadata_StringValue_Success() {
    // Act
    SimpleMetadata result = metadata.addMetadata("title", "Test Module");

    // Assert
    assertSame(metadata, result); // Method chaining
    assertTrue(metadata.hasMetadata("title"));
    assertEquals(Optional.of("Test Module"), metadata.getMetadata("title"));
  }

  @Test
  void testAddMetadata_IntegerValue_Success() {
    // Act
    metadata.addMetadata("version", 1);

    // Assert
    assertTrue(metadata.hasMetadata("version"));
    assertEquals(Optional.of(1), metadata.getMetadata("version"));
  }

  @Test
  void testAddMetadata_ListValue_Success() {
    // Arrange
    List<String> authors = Arrays.asList("Author1", "Author2");

    // Act
    metadata.addMetadata("authors", authors);

    // Assert
    assertTrue(metadata.hasMetadata("authors"));
    assertEquals(Optional.of(authors), metadata.getMetadata("authors"));
  }

  @Test
  void testAddMetadata_NullValue_Success() {
    // Act
    metadata.addMetadata("nullable", null);

    // Assert
    assertTrue(metadata.hasMetadata("nullable"));
    assertEquals(Optional.empty(), metadata.getMetadata("nullable"));
  }

  @Test
  void testAddMetadata_NullKey_ThrowsException() {
    // Act & Assert
    assertThrows(NullPointerException.class, () -> {
      metadata.addMetadata(null, "value");
    });
  }

  @Test
  void testAddMetadata_OverwriteExistingKey_Success() {
    // Arrange
    metadata.addMetadata("key", "original");

    // Act
    metadata.addMetadata("key", "updated");

    // Assert
    assertEquals(Optional.of("updated"), metadata.getMetadata("key"));
  }

  @Test
  void testGetMetadata_ExistingKey_ReturnsValue() {
    // Arrange
    metadata.addMetadata("description", "Test description");

    // Act
    Optional<Object> result = metadata.getMetadata("description");

    // Assert
    assertTrue(result.isPresent());
    assertEquals("Test description", result.get());
  }

  @Test
  void testGetMetadata_NonExistingKey_ReturnsEmpty() {
    // Act
    Optional<Object> result = metadata.getMetadata("nonexistent");

    // Assert
    assertFalse(result.isPresent());
  }

  @Test
  void testGetMetadata_NullKey_ReturnsEmpty() {
    // Act
    Optional<Object> result = metadata.getMetadata(null);

    // Assert
    assertFalse(result.isPresent());
  }

  @Test
  void testGetMetadataWithType_CorrectType_ReturnsTypedValue() {
    // Arrange
    metadata.addMetadata("count", 42);

    // Act
    Optional<Integer> result = metadata.getMetadata("count", Integer.class);

    // Assert
    assertTrue(result.isPresent());
    assertEquals(Integer.valueOf(42), result.get());
  }

  @Test
  void testGetMetadataWithType_IncorrectType_ReturnsEmpty() {
    // Arrange
    metadata.addMetadata("count", 42);

    // Act
    Optional<String> result = metadata.getMetadata("count", String.class);

    // Assert
    assertFalse(result.isPresent());
  }

  @Test
  void testGetMetadataWithType_NonExistingKey_ReturnsEmpty() {
    // Act
    Optional<String> result = metadata.getMetadata("nonexistent", String.class);

    // Assert
    assertFalse(result.isPresent());
  }

  @Test
  void testGetMetadataWithType_NullValue_ReturnsEmpty() {
    // Arrange
    metadata.addMetadata("nullable", null);

    // Act
    Optional<String> result = metadata.getMetadata("nullable", String.class);

    // Assert
    assertFalse(result.isPresent());
  }

  @Test
  void testGetMetadataWithType_SubclassType_ReturnsValue() {
    // Arrange
    List<String> stringList = Arrays.asList("item1", "item2");
    metadata.addMetadata("items", stringList);

    // Act
    Optional<List> result = metadata.getMetadata("items", List.class);

    // Assert
    assertTrue(result.isPresent());
    assertEquals(stringList, result.get());
  }

  @Test
  void testHasMetadata_ExistingKey_ReturnsTrue() {
    // Arrange
    metadata.addMetadata("test", "value");

    // Act & Assert
    assertTrue(metadata.hasMetadata("test"));
  }

  @Test
  void testHasMetadata_NonExistingKey_ReturnsFalse() {
    // Act & Assert
    assertFalse(metadata.hasMetadata("nonexistent"));
  }

  @Test
  void testHasMetadata_NullKey_ReturnsFalse() {
    // Act & Assert
    assertFalse(metadata.hasMetadata(null));
  }

  @Test
  void testHasMetadata_KeyWithNullValue_ReturnsTrue() {
    // Arrange
    metadata.addMetadata("nullable", null);

    // Act & Assert
    assertTrue(metadata.hasMetadata("nullable"));
  }

  @Test
  void testMethodChaining_MultipleAdditions_Success() {
    // Act
    SimpleMetadata result = metadata
        .addMetadata("title", "Test")
        .addMetadata("version", 1)
        .addMetadata("active", true);

    // Assert
    assertSame(metadata, result);
    assertTrue(metadata.hasMetadata("title"));
    assertTrue(metadata.hasMetadata("version"));
    assertTrue(metadata.hasMetadata("active"));
    assertEquals("Test", metadata
        .getMetadata("title")
        .get());
    assertEquals(1, metadata
        .getMetadata("version")
        .get());
    assertEquals(true, metadata
        .getMetadata("active")
        .get());
  }

  @Test
  void testEquals_SameContent_ReturnsTrue() {
    // Arrange
    SimpleMetadata other = new SimpleMetadata();
    metadata
        .addMetadata("key1", "value1")
        .addMetadata("key2", 42);
    other
        .addMetadata("key1", "value1")
        .addMetadata("key2", 42);

    // Act & Assert
    assertEquals(metadata, other);
  }

  @Test
  void testEquals_DifferentContent_ReturnsFalse() {
    // Arrange
    SimpleMetadata other = new SimpleMetadata();
    metadata.addMetadata("key1", "value1");
    other.addMetadata("key1", "value2");

    // Act & Assert
    assertNotEquals(metadata, other);
  }

  @Test
  void testEquals_EmptyMetadata_ReturnsTrue() {
    // Arrange
    SimpleMetadata other = new SimpleMetadata();

    // Act & Assert
    assertEquals(metadata, other);
  }

  @Test
  void testHashCode_SameContent_SameHashCode() {
    // Arrange
    SimpleMetadata other = new SimpleMetadata();
    metadata.addMetadata("key", "value");
    other.addMetadata("key", "value");

    // Act & Assert
    assertEquals(metadata.hashCode(), other.hashCode());
  }

  @Test
  void testHashCode_DifferentContent_DifferentHashCode() {
    // Arrange
    SimpleMetadata other = new SimpleMetadata();
    metadata.addMetadata("key", "value1");
    other.addMetadata("key", "value2");

    // Act & Assert
    assertNotEquals(metadata.hashCode(), other.hashCode());
  }

  @Test
  void testComplexDataTypes_Map_Success() {
    // Arrange
    java.util.Map<String, String> complexData = new java.util.HashMap<>();
    complexData.put("nested", "value");

    // Act
    metadata.addMetadata("complex", complexData);

    // Assert
    Optional<java.util.Map> result = metadata.getMetadata("complex", java.util.Map.class);
    assertTrue(result.isPresent());
    assertEquals(complexData, result.get());
  }

  @Test
  void testComplexDataTypes_CustomObject_Success() {
    // Arrange
    TestObject testObj = new TestObject("test", 123);

    // Act
    metadata.addMetadata("object", testObj);

    // Assert
    Optional<TestObject> result = metadata.getMetadata("object", TestObject.class);
    assertTrue(result.isPresent());
    assertEquals(testObj, result.get());
  }

  /**
   * Test object for complex data type testing.
   */
  private record TestObject(String name, int value) {

  }
}