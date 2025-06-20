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

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for CompositeMetadata.
 */
class CompositeMetadataTest {

  @Mock
  private MetadataComponent mockComponent1;

  @Mock
  private MetadataComponent mockComponent2;

  @Mock
  private MetadataComponent mockComponent3;

  private CompositeMetadata compositeMetadata;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    compositeMetadata = new CompositeMetadata();
  }

  @Test
  void testDefaultConstructor_CreatesEmptyComposite() {
    assertNotNull(compositeMetadata);
    assertTrue(compositeMetadata.getComponents().isEmpty());
    assertFalse(compositeMetadata.hasMetadata("anyKey"));
  }

  @Test
  void testBuilderConstructor_CreatesEmptyComposite() {
    CompositeMetadata metadata = CompositeMetadata.builder().build();
    assertNotNull(metadata);
    assertTrue(metadata.getComponents().isEmpty());
  }

  @Test
  void testAddComponent_SingleComponent_Success() {
    // Act
    CompositeMetadata result = compositeMetadata.addComponent(mockComponent1);

    // Assert
    assertSame(compositeMetadata, result); // Method chaining
    assertEquals(1, compositeMetadata.getComponents().size());
    assertTrue(compositeMetadata.getComponents().contains(mockComponent1));
  }

  @Test
  void testAddComponent_NullComponent_ThrowsException() {
    // Act & Assert
    assertThrows(NullPointerException.class, () -> {
      compositeMetadata.addComponent(null);
    });
  }

  @Test
  void testAddComponent_MultipleComponents_Success() {
    // Act
    compositeMetadata.addComponent(mockComponent1)
                    .addComponent(mockComponent2)
                    .addComponent(mockComponent3);

    // Assert
    List<MetadataComponent> components = compositeMetadata.getComponents();
    assertEquals(3, components.size());
    assertEquals(mockComponent1, components.get(0));
    assertEquals(mockComponent2, components.get(1));
    assertEquals(mockComponent3, components.get(2));
  }

  @Test
  void testAddComponents_ListOfComponents_Success() {
    // Arrange
    List<MetadataComponent> components = Arrays.asList(mockComponent1, mockComponent2);

    // Act
    CompositeMetadata result = compositeMetadata.addComponents(components);

    // Assert
    assertSame(compositeMetadata, result); // Method chaining
    assertEquals(2, compositeMetadata.getComponents().size());
    assertTrue(compositeMetadata.getComponents().contains(mockComponent1));
    assertTrue(compositeMetadata.getComponents().contains(mockComponent2));
  }

  @Test
  void testAddComponents_NullList_ThrowsException() {
    // Act & Assert
    assertThrows(NullPointerException.class, () -> {
      compositeMetadata.addComponents(null);
    });
  }

  @Test
  void testAddComponents_EmptyList_Success() {
    // Act
    compositeMetadata.addComponents(Arrays.asList());

    // Assert
    assertTrue(compositeMetadata.getComponents().isEmpty());
  }

  @Test
  void testGetMetadata_NoComponents_ReturnsEmpty() {
    // Act
    Optional<Object> result = compositeMetadata.getMetadata("anyKey");

    // Assert
    assertFalse(result.isPresent());
  }

  @Test
  void testGetMetadata_FirstComponentHasValue_ReturnsFirstValue() {
    // Arrange
    compositeMetadata.addComponent(mockComponent1).addComponent(mockComponent2);
    when(mockComponent1.getMetadata("key")).thenReturn(Optional.of("value1"));
    when(mockComponent2.getMetadata("key")).thenReturn(Optional.of("value2"));

    // Act
    Optional<Object> result = compositeMetadata.getMetadata("key");

    // Assert
    assertTrue(result.isPresent());
    assertEquals("value1", result.get());
    verify(mockComponent1).getMetadata("key");
    verify(mockComponent2, never()).getMetadata("key"); // Should not check second component
  }

  @Test
  void testGetMetadata_FirstComponentEmpty_ChecksSecondComponent() {
    // Arrange
    compositeMetadata.addComponent(mockComponent1).addComponent(mockComponent2);
    when(mockComponent1.getMetadata("key")).thenReturn(Optional.empty());
    when(mockComponent2.getMetadata("key")).thenReturn(Optional.of("value2"));

    // Act
    Optional<Object> result = compositeMetadata.getMetadata("key");

    // Assert
    assertTrue(result.isPresent());
    assertEquals("value2", result.get());
    verify(mockComponent1).getMetadata("key");
    verify(mockComponent2).getMetadata("key");
  }

  @Test
  void testGetMetadata_AllComponentsEmpty_ReturnsEmpty() {
    // Arrange
    compositeMetadata.addComponent(mockComponent1).addComponent(mockComponent2);
    when(mockComponent1.getMetadata("key")).thenReturn(Optional.empty());
    when(mockComponent2.getMetadata("key")).thenReturn(Optional.empty());

    // Act
    Optional<Object> result = compositeMetadata.getMetadata("key");

    // Assert
    assertFalse(result.isPresent());
    verify(mockComponent1).getMetadata("key");
    verify(mockComponent2).getMetadata("key");
  }

  @Test
  void testGetMetadata_NullKey_SearchesAllComponents() {
    // Arrange
    compositeMetadata.addComponent(mockComponent1).addComponent(mockComponent2);
    when(mockComponent1.getMetadata(null)).thenReturn(Optional.empty());
    when(mockComponent2.getMetadata(null)).thenReturn(Optional.of("nullKeyValue"));

    // Act
    Optional<Object> result = compositeMetadata.getMetadata(null);

    // Assert
    assertTrue(result.isPresent());
    assertEquals("nullKeyValue", result.get());
    verify(mockComponent1).getMetadata(null);
    verify(mockComponent2).getMetadata(null);
  }

  @Test
  void testGetMetadataWithType_CorrectType_ReturnsTypedValue() {
    // Arrange
    compositeMetadata.addComponent(mockComponent1);
    when(mockComponent1.getMetadata("count", Integer.class)).thenReturn(Optional.of(42));

    // Act
    Optional<Integer> result = compositeMetadata.getMetadata("count", Integer.class);

    // Assert
    assertTrue(result.isPresent());
    assertEquals(Integer.valueOf(42), result.get());
    verify(mockComponent1).getMetadata("count", Integer.class);
  }

  @Test
  void testGetMetadataWithType_FirstComponentWrongType_ChecksSecondComponent() {
    // Arrange
    compositeMetadata.addComponent(mockComponent1).addComponent(mockComponent2);
    when(mockComponent1.getMetadata("key", String.class)).thenReturn(Optional.empty());
    when(mockComponent2.getMetadata("key", String.class)).thenReturn(Optional.of("stringValue"));

    // Act
    Optional<String> result = compositeMetadata.getMetadata("key", String.class);

    // Assert
    assertTrue(result.isPresent());
    assertEquals("stringValue", result.get());
    verify(mockComponent1).getMetadata("key", String.class);
    verify(mockComponent2).getMetadata("key", String.class);
  }

  @Test
  void testGetMetadataWithType_AllComponentsWrongType_ReturnsEmpty() {
    // Arrange
    compositeMetadata.addComponent(mockComponent1).addComponent(mockComponent2);
    when(mockComponent1.getMetadata("key", Integer.class)).thenReturn(Optional.empty());
    when(mockComponent2.getMetadata("key", Integer.class)).thenReturn(Optional.empty());

    // Act
    Optional<Integer> result = compositeMetadata.getMetadata("key", Integer.class);

    // Assert
    assertFalse(result.isPresent());
    verify(mockComponent1).getMetadata("key", Integer.class);
    verify(mockComponent2).getMetadata("key", Integer.class);
  }

  @Test
  void testHasMetadata_NoComponents_ReturnsFalse() {
    // Act
    boolean result = compositeMetadata.hasMetadata("anyKey");

    // Assert
    assertFalse(result);
  }

  @Test
  void testHasMetadata_FirstComponentHasKey_ReturnsTrue() {
    // Arrange
    compositeMetadata.addComponent(mockComponent1).addComponent(mockComponent2);
    when(mockComponent1.hasMetadata("key")).thenReturn(true);

    // Act
    boolean result = compositeMetadata.hasMetadata("key");

    // Assert
    assertTrue(result);
    verify(mockComponent1).hasMetadata("key");
    verify(mockComponent2, never()).hasMetadata("key"); // Should not check second component
  }

  @Test
  void testHasMetadata_FirstComponentDoesNotHaveKey_ChecksSecondComponent() {
    // Arrange
    compositeMetadata.addComponent(mockComponent1).addComponent(mockComponent2);
    when(mockComponent1.hasMetadata("key")).thenReturn(false);
    when(mockComponent2.hasMetadata("key")).thenReturn(true);

    // Act
    boolean result = compositeMetadata.hasMetadata("key");

    // Assert
    assertTrue(result);
    verify(mockComponent1).hasMetadata("key");
    verify(mockComponent2).hasMetadata("key");
  }

  @Test
  void testHasMetadata_NoComponentsHaveKey_ReturnsFalse() {
    // Arrange
    compositeMetadata.addComponent(mockComponent1).addComponent(mockComponent2);
    when(mockComponent1.hasMetadata("key")).thenReturn(false);
    when(mockComponent2.hasMetadata("key")).thenReturn(false);

    // Act
    boolean result = compositeMetadata.hasMetadata("key");

    // Assert
    assertFalse(result);
    verify(mockComponent1).hasMetadata("key");
    verify(mockComponent2).hasMetadata("key");
  }

  @Test
  void testHasMetadata_NullKey_ChecksAllComponents() {
    // Arrange
    compositeMetadata.addComponent(mockComponent1).addComponent(mockComponent2);
    when(mockComponent1.hasMetadata(null)).thenReturn(false);
    when(mockComponent2.hasMetadata(null)).thenReturn(true);

    // Act
    boolean result = compositeMetadata.hasMetadata(null);

    // Assert
    assertTrue(result);
    verify(mockComponent1).hasMetadata(null);
    verify(mockComponent2).hasMetadata(null);
  }

  @Test
  void testGetComponents_ReturnsDefensiveCopy() {
    // Arrange
    compositeMetadata.addComponent(mockComponent1).addComponent(mockComponent2);

    // Act
    List<MetadataComponent> components = compositeMetadata.getComponents();
    components.clear(); // Modify the returned list

    // Assert
    assertEquals(2, compositeMetadata.getComponents().size()); // Original should be unchanged
    assertTrue(compositeMetadata.getComponents().contains(mockComponent1));
    assertTrue(compositeMetadata.getComponents().contains(mockComponent2));
  }

  @Test
  void testEquals_SameComponents_ReturnsTrue() {
    // Arrange
    CompositeMetadata other = new CompositeMetadata();
    compositeMetadata.addComponent(mockComponent1).addComponent(mockComponent2);
    other.addComponent(mockComponent1).addComponent(mockComponent2);

    // Act & Assert
    assertEquals(compositeMetadata, other);
  }

  @Test
  void testEquals_DifferentComponents_ReturnsFalse() {
    // Arrange
    CompositeMetadata other = new CompositeMetadata();
    compositeMetadata.addComponent(mockComponent1);
    other.addComponent(mockComponent2);

    // Act & Assert
    assertNotEquals(compositeMetadata, other);
  }

  @Test
  void testEquals_EmptyComposites_ReturnsTrue() {
    // Arrange
    CompositeMetadata other = new CompositeMetadata();

    // Act & Assert
    assertEquals(compositeMetadata, other);
  }

  @Test
  void testHashCode_SameComponents_SameHashCode() {
    // Arrange
    CompositeMetadata other = new CompositeMetadata();
    compositeMetadata.addComponent(mockComponent1).addComponent(mockComponent2);
    other.addComponent(mockComponent1).addComponent(mockComponent2);

    // Act & Assert
    assertEquals(compositeMetadata.hashCode(), other.hashCode());
  }

  @Test
  void testSearchOrder_ComponentsSearchedInOrder() {
    // Arrange
    compositeMetadata.addComponent(mockComponent1)
                    .addComponent(mockComponent2)
                    .addComponent(mockComponent3);

    // First component returns empty, second returns value, third should not be checked
    when(mockComponent1.getMetadata("key")).thenReturn(Optional.empty());
    when(mockComponent2.getMetadata("key")).thenReturn(Optional.of("found"));
    when(mockComponent3.getMetadata("key")).thenReturn(Optional.of("should not reach here"));

    // Act
    Optional<Object> result = compositeMetadata.getMetadata("key");

    // Assert
    assertTrue(result.isPresent());
    assertEquals("found", result.get());
    verify(mockComponent1).getMetadata("key");
    verify(mockComponent2).getMetadata("key");
    verify(mockComponent3, never()).getMetadata("key"); // Should not reach third component
  }

  @Test
  void testIntegrationWithRealComponents_Success() {
    // Arrange
    SimpleMetadata simpleMetadata1 = new SimpleMetadata();
    SimpleMetadata simpleMetadata2 = new SimpleMetadata();
    
    simpleMetadata1.addMetadata("title", "Test Title");
    simpleMetadata1.addMetadata("version", 1);
    
    simpleMetadata2.addMetadata("description", "Test Description");
    simpleMetadata2.addMetadata("version", 2); // Should not override first version

    compositeMetadata.addComponent(simpleMetadata1).addComponent(simpleMetadata2);

    // Act & Assert
    assertEquals(Optional.of("Test Title"), compositeMetadata.getMetadata("title"));
    assertEquals(Optional.of("Test Description"), compositeMetadata.getMetadata("description"));
    assertEquals(Optional.of(1), compositeMetadata.getMetadata("version")); // First component wins
    assertEquals(Optional.empty(), compositeMetadata.getMetadata("nonexistent"));
    
    assertTrue(compositeMetadata.hasMetadata("title"));
    assertTrue(compositeMetadata.hasMetadata("description"));
    assertTrue(compositeMetadata.hasMetadata("version"));
    assertFalse(compositeMetadata.hasMetadata("nonexistent"));
    
    // Test typed access
    assertEquals(Optional.of("Test Title"), compositeMetadata.getMetadata("title", String.class));
    assertEquals(Optional.of(1), compositeMetadata.getMetadata("version", Integer.class));
    assertEquals(Optional.empty(), compositeMetadata.getMetadata("version", String.class)); // Wrong type
  }
}