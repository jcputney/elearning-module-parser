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

package dev.jcputney.elearning.parser.util;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import java.io.IOException;
import java.time.Duration;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.lang3.StringUtils;

/**
 * Custom deserializer for {@link Duration} objects, allowing HH:MM:SS duration strings to be parsed
 * into {@link java.time.Duration} instances.
 *
 * <p>This deserializer supports durations in the format HH:MM:SS, where HH represents
 * hours, MM represents minutes, and SS represents seconds. It does not support days, months, or
 * years, as these are not compatible with {@link java.time.Duration}.</p>
 */
public class DurationHHMMSSDeserializer extends JsonDeserializer<Duration> {

  // Matches up to three groups of digits separated by optional colons.
  private static final Pattern HMS_REGEX = Pattern.compile(
      "^"                // start of string
          + "(\\d*)?"        // hours (group 1) - zero or more digits, optional
          + "(?::(\\d*))?"   // optional colon, then minutes (group 2) - zero or more digits
          + "(?::(\\d*))?"   // optional colon, then seconds (group 3) - zero or more digits
          + "$"              // end of string
  );

  public static Duration parseDuration(String durationString) throws NumberFormatException {
    if (StringUtils.isEmpty(durationString)) {
      return Duration.ZERO;
    }

    Matcher matcher = HMS_REGEX.matcher(durationString);
    if (!matcher.matches()) {
      throw new IllegalArgumentException("Invalid format: " + durationString);
    }

    String hoursPart = matcher.group(1); // may be null or empty
    String minutesPart = matcher.group(2); // may be null or empty
    String secondsPart = matcher.group(3); // may be null or empty

    int hours = parseOrZero(hoursPart);
    int minutes = parseOrZero(minutesPart);
    int seconds = parseOrZero(secondsPart);

    return Duration.ofHours(hours)
        .plusMinutes(minutes)
        .plusSeconds(seconds);
  }

  private static int parseOrZero(String s) {
    // If the group is null or empty, treat it as zero
    return (s == null || s.isEmpty()) ? 0 : Integer.parseInt(s);
  }

  @Override
  public Duration deserialize(JsonParser parser, DeserializationContext context)
      throws IOException {
    String durationString = parser.getText();
    try {
      return parseDuration(durationString);
    } catch (NumberFormatException e) {
      throw new IOException(
          "Invalid HH:MM:SS duration format for java.time.Duration: " + durationString, e);
    }
  }
}
