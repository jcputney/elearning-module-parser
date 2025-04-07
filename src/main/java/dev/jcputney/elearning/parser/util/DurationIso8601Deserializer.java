/*
 * qlty-ignore: +qlty:similar-code
 *
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
 *
 * qlty-ignore: -qlty:similar-code
 */

package dev.jcputney.elearning.parser.util;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import java.io.IOException;
import java.time.Duration;
import java.time.format.DateTimeParseException;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.threeten.extra.PeriodDuration;

/**
 * Custom deserializer for {@link Duration} objects, allowing ISO 8601 duration strings to be parsed
 * into {@link java.time.Duration} instances.
 *
 * <p>This deserializer supports durations in the format PnDTnHnMnS, where n represents
 * days, hours, minutes, or seconds. It doesn't support months or years, as these aren't compatible
 * with {@link java.time.Duration}.</p>
 */
public class DurationIso8601Deserializer extends JsonDeserializer<Duration> {

  /**
   * Default constructor for the deserializer.
   */
  public DurationIso8601Deserializer() {
    // Default constructor
  }

  /**
   * Deserializes a JSON value into a {@link Duration} object.
   *
   * <p>This method supports multiple duration formats:
   * <ul>
   *   <li>Empty string or null, returns <code>Duration.ZERO</code></li>
   *   <li>Numeric values, interpreted as seconds</li>
   *   <li>ISO 8601 duration format, for example, "PT1H30M45S"</li>
   *   <li>HH:MM:SS format, delegated to {@link DurationHHMMSSDeserializer#parseDuration}</li>
   * </ul>
   *
   * @param parser the JsonParser to read the value from
   * @param context context for the deserialization process
   * @return the deserialized {@link Duration} object
   * @throws IOException if the value can't be parsed as a duration, or if there's an issue with the
   * parser
   * @throws IllegalArgumentException if the parser is null
   */
  @Override
  public Duration deserialize(JsonParser parser, DeserializationContext context)
      throws IOException {
    if (parser == null) {
      throw new IllegalArgumentException("JsonParser cannot be null");
    }

    String durationString = parser.getText();
    if (StringUtils.isEmpty(durationString)) {
      return Duration.ZERO;
    }

    if (NumberUtils.isParsable(durationString)) {
      return Duration.ofSeconds((long) Double.parseDouble(durationString));
    }

    try {
      if (!durationString.startsWith("P")) {
        return DurationHHMMSSDeserializer.parseDuration(durationString);
      }
      return PeriodDuration.parse(durationString).getDuration();
    } catch (NumberFormatException | DateTimeParseException e) {
      throw new IOException(
          "Invalid ISO 8601 duration format for java.time.Duration: " + durationString +
              ". Expected format: ISO 8601 (e.g., PT1H30M45S) or HH:MM:SS.", e);
    }
  }
}
