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
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import java.io.IOException;
import java.math.BigDecimal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link PercentTypeDeserializer} to ensure proper deserialization of PercentType
 * values.
 */
class PercentTypeDeserializerTest {

  private ObjectMapper objectMapper;

  @BeforeEach
  void setUp() {
    objectMapper = new ObjectMapper();
    SimpleModule module = new SimpleModule();
    module.addDeserializer(PercentType.class, new PercentTypeDeserializer());
    objectMapper.registerModule(module);
  }

  @Test
  void deserialize_simpleStringValue_returnsPercentType() throws IOException {
    // Arrange
    String json = "\"0.5\"";

    // Act
    PercentType result = objectMapper.readValue(json, PercentType.class);

    // Assert
    assertEquals(new BigDecimal("0.5"), result.value());
  }

  @Test
  void deserialize_invalidSimpleValue_throwsIOException() {
    // Arrange
    String json = "\"invalid\"";

    // Act & Assert
    assertThrows(IOException.class, () -> objectMapper.readValue(json, PercentType.class));
  }

  @Test
  void deserialize_invalidObjectValue_throwsIOException() {
    // Arrange
    String json = "{\"value\":\"invalid\"}";

    // Act & Assert
    assertThrows(IOException.class, () -> objectMapper.readValue(json, PercentType.class));
  }

  @Test
  void deserialize_outOfRangeValue_throwsIOException() {
    // Arrange
    String json = "\"1.5\"";

    // Act & Assert
    assertThrows(IOException.class, () -> objectMapper.readValue(json, PercentType.class));
  }
}