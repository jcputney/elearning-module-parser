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

/**
 * Custom deserializer for trimming leading whitespace and preserving indentation in JSON strings
 * and XML text blocks.
 */
public class TrimAndPreserveIndentationDeserializer extends JsonDeserializer<String> {

  /**
   * Default constructor for the TrimAndPreserveIndentationDeserializer class.
   */
  public TrimAndPreserveIndentationDeserializer() {
    // no-op
  }

  /**
   * Deserializes a JSON string into a trimmed and normalized string.
   *
   * @param p the JsonParser
   * @param context the DeserializationContext
   * @return the deserialized and normalized string
   * @throws IOException if an error occurs during deserialization
   */
  @Override
  public String deserialize(JsonParser p, DeserializationContext context) throws IOException {
    String rawText = p.getText();
    if (rawText == null || rawText.isBlank()) {
      return "";
    }

    // Normalize Windows/legacy Mac line endings before processing
    String[] lines = rawText
        .replace("\r\n", "\n")
        .replace('\r', '\n')
        .split("\n");
    if (lines.length == 0) {
      return "";
    }

    // Detect the leading whitespace of the first non-empty line
    int minIndentation = Integer.MAX_VALUE;
    for (String line : lines) {
      String trimmedLine = line.trim();
      if (!trimmedLine.isEmpty()) {
        int leadingSpaces = line.indexOf(trimmedLine);
        minIndentation = Math.min(minIndentation, leadingSpaces);
      }
    }

    // Normalize all lines by removing leading whitespace
    StringBuilder result = new StringBuilder();
    for (String line : lines) {
      if (!line
          .trim()
          .isEmpty()) {
        result
            .append(line
                .substring(minIndentation)
                .trim())
            .append("\n");
      }
    }

    return result
        .toString()
        .trim();
  }
}
