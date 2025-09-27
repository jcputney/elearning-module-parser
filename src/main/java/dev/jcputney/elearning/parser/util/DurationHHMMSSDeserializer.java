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
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Strings;
import org.apache.commons.lang3.math.NumberUtils;

/**
 * Custom deserializer for {@link Duration} objects, allowing HH:MM:SS duration strings to be parsed
 * into {@link java.time.Duration} instances.
 *
 * <p>This deserializer supports durations in the format HH:MM:SS, where HH represents
 * hours, MM represents minutes, and SS represents seconds. It doesn't support days, months, or
 * years, as these aren't compatible with {@link java.time.Duration}.</p>
 */
public class DurationHHMMSSDeserializer extends JsonDeserializer<Duration> {

  // Matches up to three groups of digits separated by optional colons.
  // Also handles the case where the string starts with a colon (e.g., ":45" for 45 minutes)
  private static final Pattern HMS_REGEX = Pattern.compile("^"                // start of string
      + "(\\d+)?"      // optional hours (group 1) - one or more digits
      + "(?::(\\d+))?" // optional colon, then minutes (group 2) - one or more digits
      + "(?::(\\d+(?:[.,]\\d+)?))?" // optional colon then seconds (group 3) with optional decimal
      + "$"                // end of string
  );

  private static final BigDecimal ONE_BILLION = new BigDecimal("1000000000");

  /**
   * Default constructor for the deserializer.
   */
  public DurationHHMMSSDeserializer() {
    // Default constructor
  }

  /**
   * Parses a string representation of a duration into a {@link Duration} object.
   *
   * <p>This method supports the following formats:
   * <ul>
   *   <li>Numeric values (interpreted as seconds)</li>
   *   <li>HH:MM:SS format (hours:minutes:seconds)</li>
   *   <li>MM:SS format (minutes:seconds)</li>
   *   <li>SS format (seconds only)</li>
   * </ul>
   *
   * <p>Examples:
   * <ul>
   *   <li>"3600" → 1 hour</li>
   *   <li>"01:30:45" → 1 hour, 30 minutes, 45 seconds</li>
   *   <li>"30:45" → 30 minutes, 45 seconds</li>
   *   <li>"45" → 45 seconds</li>
   * </ul>
   *
   * @param durationString the string to parse, can be empty or null (returns
   * <code>Duration.ZERO</code>)
   * @return the parsed {@link Duration}
   * @throws NumberFormatException if the string can't be parsed as a duration
   * @throws IllegalArgumentException if the string format is invalid
   */
  public static Duration parseDuration(String durationString) throws NumberFormatException {
    if (StringUtils.isEmpty(durationString) || Strings.CS.equalsAny("::", durationString)) {
      return Duration.ZERO;
    }

    String trimmedDuration = durationString.trim();

    if (NumberUtils.isParsable(trimmedDuration)) {
      return Duration.ofSeconds((long) Double.parseDouble(trimmedDuration));
    }

    Matcher matcher = HMS_REGEX.matcher(trimmedDuration);
    if (!matcher.matches()) {
      throw new IllegalArgumentException("Invalid format: " + durationString);
    }

    String hoursPart = matcher.group(1); // maybe null or empty
    String minutesPart = matcher.group(2); // maybe null or empty
    String secondsPart = matcher.group(3); // maybe null or empty

    int hours = parseOrZero(hoursPart);
    int minutes = parseOrZero(minutesPart);
    Duration duration = Duration
        .ofHours(hours)
        .plusMinutes(minutes);

    return appendSeconds(duration, secondsPart, durationString);
  }

  private static int parseOrZero(String s) {
    // If the group is null or empty, treat it as zero
    return (s == null || s.isEmpty()) ? 0 : Integer.parseInt(s);
  }

  private static Duration appendSeconds(Duration base, String secondsPart, String originalInput) {
    if (secondsPart == null || secondsPart.isEmpty()) {
      return base;
    }

    String normalizedSeconds = secondsPart.replace(',', '.');
    try {
      BigDecimal seconds = new BigDecimal(normalizedSeconds);
      long wholeSeconds = seconds.longValue();
      BigDecimal fractional = seconds.subtract(BigDecimal.valueOf(wholeSeconds));
      int nanos = fractional
          .multiply(ONE_BILLION)
          .setScale(0, RoundingMode.DOWN)
          .intValueExact();
      return base.plusSeconds(wholeSeconds).plusNanos(nanos);
    } catch (NumberFormatException | ArithmeticException e) {
      throw new IllegalArgumentException("Invalid format: " + originalInput, e);
    }
  }

  /**
   * Deserializes a JSON value into a {@link Duration} object.
   *
   * <p>This method reads the text value from the parser and delegates to
   * {@link #parseDuration(String)} for the actual parsing logic.</p>
   *
   * @param parser the JsonParser to read the value from
   * @param context context for the deserialization process
   * @return the deserialized {@link Duration} object
   * @throws IOException if the value can't be parsed as a duration, or if there's an issue with the
   * parser
   * @throws IllegalArgumentException if the parser is null
   * @see #parseDuration(String)
   */
  @Override
  public Duration deserialize(JsonParser parser, DeserializationContext context)
      throws IOException {
    if (parser == null) {
      throw new IllegalArgumentException("JsonParser cannot be null");
    }

    String durationString = parser.getText();
    try {
      return parseDuration(durationString);
    } catch (NumberFormatException e) {
      throw new IOException(
          "Invalid HH:MM:SS duration format for java.time.Duration: " + durationString
              + ". Expected format: HH:MM:SS, MM:SS, or SS.", e);
    }
  }
}
