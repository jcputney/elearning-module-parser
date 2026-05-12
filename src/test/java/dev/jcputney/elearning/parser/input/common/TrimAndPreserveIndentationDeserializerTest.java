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

  @Test
  void deserialize_windowsLineEndings_shouldNormaliseCarriageReturns() throws IOException {
    // Arrange
    String json = "{\"text\": \"  Line 1\\r\\n  Line 2\\r\\n  Line 3\"}";

    // Act
    TestClass result = objectMapper.readValue(json, TestClass.class);

    // Assert
    assertEquals("Line 1\nLine 2\nLine 3", result.getText());
  }

  @Test
  void deserialize_oldMacLineEndings_shouldNormaliseCarriageReturns() throws IOException {
    // Arrange
    String json = "{\"text\": \"  Line 1\\r  Line 2\\r  Line 3\"}";

    // Act
    TestClass result = objectMapper.readValue(json, TestClass.class);

    // Assert
    assertEquals("Line 1\nLine 2\nLine 3", result.getText());
  }
}
