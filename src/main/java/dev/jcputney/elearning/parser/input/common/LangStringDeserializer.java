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
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.jcputney.elearning.parser.input.lom.types.LangString;
import java.io.IOException;

/**
 * Custom deserializer for LangString objects to ensure that they are properly deserialized from
 * XML. This deserializer is used to prevent ClassCastException issues when deserializing LangString
 * objects from XML.
 */
public class LangStringDeserializer extends JsonDeserializer<LangString> {

  /**
   * Default constructor for the LangStringDeserializer class.
   */
  public LangStringDeserializer() {
    // no-op
  }

  /**
   * Deserializes a JSON or XML representation of a LangString object. The method extracts the
   * language and value fields from the provided input and constructs a new LangString instance.
   *
   * @param p the JSON parser used to read the input data
   * @param context the deserialization context providing additional functionality
   * @return a LangString object constructed from the deserialized data
   * @throws IOException if an error occurs during the deserialization process
   */
  @Override
  public LangString deserialize(JsonParser p, DeserializationContext context) throws IOException {
    ObjectMapper mapper = (ObjectMapper) p.getCodec();
    JsonNode node = mapper.readTree(p);

    String language = null;
    String value = null;

    if (node.has("language")) {
      language = node
          .get("language")
          .asText();
    } else if (node.has("@language")) {
      language = node
          .get("@language")
          .asText();
    }

    if (node.has("value")) {
      value = node
          .get("value")
          .asText();
    } else if (node.has("#text")) {
      value = node
          .get("#text")
          .asText();
    } else if (node.has("")) {
      // Jackson XML sometimes uses empty string as key for text content
      value = node
          .get("")
          .asText();
    } else if (node.isTextual()) {
      value = node.asText();
    }

    // Create a new LangString instance using the builder pattern
    return new LangString(language, value);
  }
}
