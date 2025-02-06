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
import java.time.Period;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import org.apache.commons.lang3.StringUtils;
import org.threeten.extra.PeriodDuration;

/**
 * Custom deserializer for {@link Duration} objects, allowing ISO 8601 duration strings to be parsed
 * into {@link java.time.Duration} instances.
 *
 * <p>This deserializer supports durations in the format PnDTnHnMnS, where n represents
 * days, hours, minutes, or seconds. It does not support months or years, as these are not
 * compatible with {@link java.time.Duration}.</p>
 */
public class DurationIso8601Deserializer extends JsonDeserializer<Duration> {

  @Override
  public Duration deserialize(JsonParser parser, DeserializationContext context)
      throws IOException {
    String durationString = parser.getText();
    if (StringUtils.isEmpty(durationString)) {
      return Duration.ZERO;
    }
    try {
      if (!durationString.startsWith("P")) {
        return DurationHHMMSSDeserializer.parseDuration(durationString);
      }
      return PeriodDuration.parse(durationString).getDuration();
    } catch (NumberFormatException | DateTimeParseException e) {
      throw new IOException(
          "Invalid ISO 8601 duration format for java.time.Duration: " + durationString, e);
    }
  }
}
