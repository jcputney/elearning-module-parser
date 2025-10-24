/*
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
 */

package dev.jcputney.elearning.parser.input.aicc;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Custom deserializer for AICC course descriptions that provides backward compatibility with
 * legacy JSON data.
 *
 * <p>In earlier versions (pre-867d2d1), course descriptions were stored as {@code Map<String,
 * String>} to handle multi-line descriptions from AICC .crs files. Current versions use {@code
 * String} directly since the parser extracts raw text via {@link
 * dev.jcputney.elearning.parser.parsers.AiccParser#extractCourseDescription}.</p>
 *
 * <p>This deserializer handles both formats:</p>
 * <ul>
 *   <li><b>Current format (String):</b> Returns the string value directly</li>
 *   <li><b>Legacy format (Map/Object):</b> Reconstructs multi-line text by joining map entries,
 *       treating empty values as plain text lines and non-empty values as "key: value" pairs</li>
 * </ul>
 *
 * @see AiccCourse#courseDescription
 */
public class CourseDescriptionDeserializer extends JsonDeserializer<String> {

  /**
   * Default constructor for the deserializer.
   */
  public CourseDescriptionDeserializer() {
    // Default constructor
  }

  /**
   * Deserializes a JSON value into a course description string.
   *
   * <p>Supports two formats:</p>
   * <ul>
   *   <li><b>String:</b> {@code "Course_Description": "Simple text"} → returns the string</li>
   *   <li><b>Object/Map:</b> {@code "Course_Description": {"line1": "", "line2": "value"}} →
   *       reconstructs as "line1\nline2: value"</li>
   * </ul>
   *
   * @param parser the JsonParser to read the value from
   * @param context context for the deserialization process
   * @return the course description as a String, or null if the field is null
   * @throws IOException if the value cannot be parsed
   */
  @Override
  public String deserialize(JsonParser parser, DeserializationContext context) throws IOException {
    if (parser == null) {
      throw new IllegalArgumentException("JsonParser cannot be null");
    }

    JsonToken token = parser.currentToken();

    // Handle current format: plain String
    if (token == JsonToken.VALUE_STRING) {
      return parser.getText();
    }

    // Handle null value
    if (token == JsonToken.VALUE_NULL) {
      return null;
    }

    // Handle legacy format: Map/Object
    if (token == JsonToken.START_OBJECT) {
      return deserializeLegacyMapFormat(parser);
    }

    // Unexpected token type
    throw new IOException(
        "Cannot deserialize Course_Description: expected String or Object, got " + token);
  }

  /**
   * Deserializes the legacy Map format and reconstructs the multi-line description.
   *
   * <p>The legacy format stored course descriptions as {@code Map<String, String>} where:</p>
   * <ul>
   *   <li>Empty/null values indicate plain text lines (key only)</li>
   *   <li>Non-empty values indicate "key: value" pairs</li>
   * </ul>
   *
   * @param parser the JsonParser positioned at START_OBJECT
   * @return reconstructed multi-line description, or null if the map is empty
   * @throws IOException if parsing fails
   */
  private String deserializeLegacyMapFormat(JsonParser parser) throws IOException {
    Map<String, String> descriptionMap = new LinkedHashMap<>();

    while (parser.nextToken() != JsonToken.END_OBJECT) {
      String key = parser.currentName();
      parser.nextToken(); // Move to value

      String value = parser.getText();
      descriptionMap.put(key, value);
    }

    if (descriptionMap.isEmpty()) {
      return null;
    }

    // Reconstruct multi-line description from map (same logic as pre-867d2d1)
    return descriptionMap
        .entrySet()
        .stream()
        .map(entry -> {
          String key = entry.getKey();
          String value = entry.getValue();
          if (value == null || value.trim().isEmpty()) {
            // Plain text line (no key=value separator found by parser)
            return key;
          } else {
            // Key-value pair, reconstruct as "key: value"
            return key + ": " + value;
          }
        })
        .collect(Collectors.joining("\n"));
  }
}
