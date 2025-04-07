/* Copyright (c) 2024-2025. Jonathan Putney
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

package dev.jcputney.elearning.parser.input.lom.properties;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

/**
 * Tests for the {@link EnabledDisabledType} enum.
 */
class EnabledDisabledTypeTest {

  private final ObjectMapper objectMapper = new ObjectMapper()
      .configure(com.fasterxml.jackson.databind.DeserializationFeature.READ_ENUMS_USING_TO_STRING,
          false)
      .configure(
          com.fasterxml.jackson.databind.DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_AS_NULL,
          false)
      .configure(com.fasterxml.jackson.databind.MapperFeature.ACCEPT_CASE_INSENSITIVE_ENUMS, true);

  @Test
  void deserialize_enabled_returnsEnabled() throws Exception {
    EnabledDisabledType result = objectMapper.readValue("\"enabled\"", EnabledDisabledType.class);
    assertEquals(EnabledDisabledType.ENABLED, result);
  }

  @Test
  void deserialize_disabled_returnsDisabled() throws Exception {
    EnabledDisabledType result = objectMapper.readValue("\"disabled\"", EnabledDisabledType.class);
    assertEquals(EnabledDisabledType.DISABLED, result);
  }

  @Test
  void deserialize_caseInsensitive_returnsCorrectValue() throws Exception {
    EnabledDisabledType result1 = objectMapper.readValue("\"ENABLED\"", EnabledDisabledType.class);
    assertEquals(EnabledDisabledType.ENABLED, result1);

    EnabledDisabledType result2 = objectMapper.readValue("\"DiSaBlEd\"", EnabledDisabledType.class);
    assertEquals(EnabledDisabledType.DISABLED, result2);
  }

  @Test
  void serialize_enabled_returnsEnabledString() throws Exception {
    String result = objectMapper.writeValueAsString(EnabledDisabledType.ENABLED);
    assertEquals("\"enabled\"", result);
  }

  @Test
  void serialize_disabled_returnsDisabledString() throws Exception {
    String result = objectMapper.writeValueAsString(EnabledDisabledType.DISABLED);
    assertEquals("\"disabled\"", result);
  }
}
