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
package dev.jcputney.elearning.parser.input.scorm2004.ims.ss.types;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import java.io.IOException;
import java.math.BigDecimal;

/**
 * Custom deserializer for MeasureType, ensuring the value is within the range of -1 to 1 and has at
 * least four decimal digits.
 */
public final class MeasureTypeDeserializer extends JsonDeserializer<MeasureType> {

  /**
   * Default constructor for the MeasureTypeDeserializer class.
   */
  public MeasureTypeDeserializer() {
    // Default constructor
  }

  /**
   * Deserialize a JSON string into a MeasureType object.
   *
   * @param p the JsonParser
   * @param context the DeserializationContext
   * @return a MeasureType object
   * @throws IOException if the value is not valid
   */
  @Override
  public MeasureType deserialize(JsonParser p, DeserializationContext context) throws IOException {
    String rawValue = p.getValueAsString();
    if (rawValue == null) {
      return null;
    }

    String value = rawValue.trim();
    if (value.isEmpty()) {
      return null;
    }

    try {
      return new MeasureType(new BigDecimal(value));
    } catch (IllegalArgumentException e) {
      return null;
    }
  }
}
