/*
 * Copyright (c) 2024. Jonathan Putney
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
    // Default constructor
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

    // Split into lines
    String[] lines = rawText.split("\n");
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
      if (!line.trim().isEmpty()) {
        result.append(line.substring(minIndentation).trim()).append("\n");
      }
    }

    return result.toString().trim();
  }
}
