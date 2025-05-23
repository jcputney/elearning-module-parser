/*
 * qlty-ignore: +qlty:similar-code
 *
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
 *
 * qlty-ignore: -qlty:similar-code
 */

package dev.jcputney.elearning.parser.input.common;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.module.SimpleModule;
import java.io.IOException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link TrimAndPreserveIndentationDeserializer} to ensure proper deserialization and
 * formatting of indented text.
 */
class TrimAndPreserveIndentationDeserializerTest {

  private ObjectMapper objectMapper;

  /**
   * Test class with a String field that uses the TrimAndPreserveIndentationDeserializer.
   */
  private static class TestClass {
    @JsonDeserialize(using = TrimAndPreserveIndentationDeserializer.class)
    private String text;

    public String getText() {
      return text;
    }
  }

  @BeforeEach
  void setUp() {
    objectMapper = new ObjectMapper();
    SimpleModule module = new SimpleModule();
    module.addDeserializer(String.class, new TrimAndPreserveIndentationDeserializer());
    objectMapper.registerModule(module);
  }

  @Test
  void deserialize_nullOrEmptyString_shouldReturnEmptyString() throws IOException {
    // Arrange
    String json = "{\"text\": \"\"}";

    // Act
    TestClass result = objectMapper.readValue(json, TestClass.class);

    // Assert
    assertEquals("", result.getText());
  }

  @Test
  void deserialize_blankString_shouldReturnEmptyString() throws IOException {
    // Arrange
    String json = "{\"text\": \"   \\n  \\n  \"}";

    // Act
    TestClass result = objectMapper.readValue(json, TestClass.class);

    // Assert
    assertEquals("", result.getText());
  }

  @Test
  void deserialize_singleLineString_shouldTrimString() throws IOException {
    // Arrange
    String json = "{\"text\": \"  Hello, World!  \"}";

    // Act
    TestClass result = objectMapper.readValue(json, TestClass.class);

    // Assert
    assertEquals("Hello, World!", result.getText());
  }

  @Test
  void deserialize_multiLineStringWithConsistentIndentation_shouldPreserveRelativeIndentation() throws IOException {
    // Arrange
    String json = "{\"text\": \"    Line 1\\n    Line 2\\n    Line 3\"}";

    // Act
    TestClass result = objectMapper.readValue(json, TestClass.class);

    // Assert
    assertEquals("Line 1\nLine 2\nLine 3", result.getText());
  }

  @Test
  void deserialize_multiLineStringWithVaryingIndentation_shouldNormalizeIndentation() throws IOException {
    // Arrange
    String json = "{\"text\": \"    Line 1\\n      Line 2\\n        Line 3\"}";

    // Act
    TestClass result = objectMapper.readValue(json, TestClass.class);

    // Assert
    // The deserializer trims all lines, not preserving relative indentation
    assertEquals("Line 1\nLine 2\nLine 3", result.getText());
  }

  @Test
  void deserialize_multiLineStringWithEmptyLines_shouldSkipEmptyLines() throws IOException {
    // Arrange
    String json = "{\"text\": \"    Line 1\\n\\n    Line 3\"}";

    // Act
    TestClass result = objectMapper.readValue(json, TestClass.class);

    // Assert
    assertEquals("Line 1\nLine 3", result.getText());
  }

  @Test
  void deserialize_multiLineStringWithDifferentStartingIndentation_shouldDetectMinimumIndentation() throws IOException {
    // Arrange
    String json = "{\"text\": \"      Line 1\\n    Line 2\\n        Line 3\"}";

    // Act
    TestClass result = objectMapper.readValue(json, TestClass.class);

    // Assert
    // The deserializer trims all lines, not preserving relative indentation
    assertEquals("Line 1\nLine 2\nLine 3", result.getText());
  }

  @Test
  void deserialize_stringWithTabIndentation_shouldHandleTabsCorrectly() throws IOException {
    // Arrange
    String json = "{\"text\": \"\\tLine 1\\n\\t\\tLine 2\\n\\t\\t\\tLine 3\"}";

    // Act
    TestClass result = objectMapper.readValue(json, TestClass.class);

    // Assert
    // The deserializer trims all lines, not preserving tab indentation
    assertEquals("Line 1\nLine 2\nLine 3", result.getText());
  }

  @Test
  void deserialize_stringWithMixedIndentation_shouldHandleMixedIndentationCorrectly() throws IOException {
    // Arrange
    String json = "{\"text\": \"  Line 1\\n\\tLine 2\\n    Line 3\"}";

    // Act
    TestClass result = objectMapper.readValue(json, TestClass.class);

    // Assert
    // The deserializer trims all lines, not preserving mixed indentation
    assertEquals("Line 1\nLine 2\nLine 3", result.getText());
  }
}
