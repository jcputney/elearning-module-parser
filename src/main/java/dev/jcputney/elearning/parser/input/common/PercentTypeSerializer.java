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

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import java.io.IOException;

/**
 * A custom serializer for the {@link PercentType} class, enabling its conversion to a JSON string
 * representation during serialization.
 * <p>
 * This serializer ensures that the {@code PercentType} object is serialized as the string
 * representation of its value, while handling null values gracefully.
 * <p>
 * The intended use is to handle serialization of percentage values represented as decimals between
 * 0 and 1 within a JSON structure.
 */
public class PercentTypeSerializer extends JsonSerializer<PercentType> {

  /**
   * Serializes a {@link PercentType} object into its JSON string representation. Writes the value
   * as a string if it is non-null; otherwise, writes a JSON null.
   *
   * @param value the {@code PercentType} object to serialize; may be null
   * @param gen the {@code JsonGenerator} used to generate the JSON output
   * @param serializers the {@code SerializerProvider} that can provide serializers for serializing
   * objects
   * @throws IOException if an error occurs during JSON serialization
   */
  @Override
  public void serialize(PercentType value, JsonGenerator gen, SerializerProvider serializers)
      throws IOException {
    if (value != null) {
      gen.writeString(value
          .value()
          .toString());
    } else {
      gen.writeNull();
    }
  }
}
