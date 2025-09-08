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