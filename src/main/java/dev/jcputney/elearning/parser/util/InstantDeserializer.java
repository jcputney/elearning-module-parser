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

package dev.jcputney.elearning.parser.util;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import java.io.IOException;
import java.time.Instant;
import java.time.format.DateTimeParseException;

/**
 * Custom deserializer for {@link Instant} objects, allowing ISO 8601 date-time strings to be parsed
 * into {@link java.time.Instant} instances.
 *
 * <p>This deserializer supports date-time formats with timezone information (e.g.,
 * "2023-05-01T10:15:30Z").</p>
 */
public class InstantDeserializer extends JsonDeserializer<Instant> {

  /**
   * Default constructor for the deserializer.
   */
  public InstantDeserializer() {
    // Default constructor
  }

  /**
   * Deserializes a JSON value into an {@link Instant} object.
   *
   * <p>This method parses a string representation of a date-time in ISO 8601 format
   * (e.g., "2023-05-01T10:15:30Z") into an {@link Instant} object.
   *
   * @param parser the JsonParser to read the value from
   * @param context context for the deserialization process
   * @return the deserialized {@link Instant} object
   * @throws IOException if the value cannot be parsed as an ISO 8601 date-time or if there's an
   * issue with the parser
   * @throws IllegalArgumentException if the parser is null
   */
  @Override
  public Instant deserialize(JsonParser parser, DeserializationContext context) throws IOException {
    if (parser == null) {
      throw new IllegalArgumentException("JsonParser cannot be null");
    }

    String dateTimeString = parser.getText();
    if (dateTimeString == null || dateTimeString.isEmpty()) {
      return Instant.EPOCH;
    }

    try {
      return Instant.parse(dateTimeString);
    } catch (DateTimeParseException e) {
      throw new IOException("Invalid ISO 8601 date-time format for Instant: " + dateTimeString, e);
    }
  }
}
