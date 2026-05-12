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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.module.SimpleModule;
import java.math.BigDecimal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link PercentTypeSerializer} to ensure proper serialization of PercentType values.
 */
class PercentTypeSerializerTest {

  private ObjectMapper objectMapper;

  @BeforeEach
  void setUp() {
    objectMapper = new ObjectMapper();
    SimpleModule module = new SimpleModule();
    module.addSerializer(PercentType.class, new PercentTypeSerializer());
    objectMapper.registerModule(module);
  }

  @Test
  void serialize_withNonNullValue_shouldSerializeAsString() throws JsonProcessingException {
    // Arrange
    PercentType percentType = new PercentType(new BigDecimal("0.75"));
    TestClass testObject = new TestClass(percentType);

    // Act
    String json = objectMapper.writeValueAsString(testObject);

    // Assert
    assertEquals("{\"percentValue\":\"0.75\"}", json);
  }

  @Test
  void serialize_withZeroValue_shouldSerializeAsString() throws JsonProcessingException {
    // Arrange
    PercentType percentType = new PercentType(BigDecimal.ZERO);
    TestClass testObject = new TestClass(percentType);

    // Act
    String json = objectMapper.writeValueAsString(testObject);

    // Assert
    assertEquals("{\"percentValue\":\"0\"}", json);
  }

  @Test
  void serialize_withOneValue_shouldSerializeAsString() throws JsonProcessingException {
    // Arrange
    PercentType percentType = new PercentType(BigDecimal.ONE);
    TestClass testObject = new TestClass(percentType);

    // Act
    String json = objectMapper.writeValueAsString(testObject);

    // Assert
    assertEquals("{\"percentValue\":\"1\"}", json);
  }

  @Test
  void serialize_withNullValue_shouldSerializeAsNull() throws JsonProcessingException {
    // Arrange
    TestClass testObject = new TestClass(null);

    // Act
    String json = objectMapper.writeValueAsString(testObject);

    // Assert
    assertEquals("{\"percentValue\":null}", json);
  }

  @Test
  void serialize_directPercentTypeObject_shouldSerializeAsString() throws JsonProcessingException {
    // Arrange
    PercentType percentType = new PercentType(new BigDecimal("0.5"));

    // Act
    String json = objectMapper.writeValueAsString(percentType);

    // Assert
    assertEquals("\"0.5\"", json);
  }

  /**
   * Test class with a PercentType field that uses the PercentTypeSerializer.
   */
  private record TestClass(
      @JsonSerialize(using = PercentTypeSerializer.class) PercentType percentValue) {

    private TestClass(PercentType percentValue) {
      this.percentValue = percentValue;
    }
  }
}