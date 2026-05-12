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

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import java.io.IOException;
import java.math.BigDecimal;

/**
 * Custom deserializer for PercentType, ensuring the value is within the range of 0 to 1.
 */
public class PercentTypeDeserializer extends JsonDeserializer<PercentType> {

  /**
   * Default constructor for the PercentTypeDeserializer class.
   */
  public PercentTypeDeserializer() {
    // no-op
  }

  /**
   * Deserializes a JSON string into a PercentType object.
   *
   * @param p the JsonParser
   * @param context the DeserializationContext
   * @return the deserialized PercentType object
   * @throws IOException if an error occurs during deserialization
   */
  @Override
  public PercentType deserialize(JsonParser p, DeserializationContext context)
      throws IOException {
    String value = p.getText();
    try {
      return new PercentType(new BigDecimal(value));
    } catch (IllegalArgumentException e) {
      throw new IOException("Invalid PercentType value: " + value, e);
    }
  }
}
