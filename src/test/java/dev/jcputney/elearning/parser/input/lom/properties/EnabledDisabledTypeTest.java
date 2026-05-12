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
package dev.jcputney.elearning.parser.input.lom.properties;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

/**
 * Tests for the {@link EnabledDisabledType} enum.
 */
class EnabledDisabledTypeTest {

  private final ObjectMapper objectMapper = new ObjectMapper()
      .configure(DeserializationFeature.READ_ENUMS_USING_TO_STRING,
          false)
      .configure(
          DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_AS_NULL,
          false)
      .configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_ENUMS, true);

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
