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
    assertEquals(new BigDecimal("0.5"), result.getValue());
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