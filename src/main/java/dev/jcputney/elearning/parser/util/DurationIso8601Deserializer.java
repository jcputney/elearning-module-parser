package dev.jcputney.elearning.parser.util;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import java.io.IOException;
import java.time.Duration;
import java.time.format.DateTimeParseException;

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
    try {
      return Duration.parse(durationString);
    } catch (DateTimeParseException e) {
      throw new IOException(
          "Invalid ISO 8601 duration format for java.time.Duration: " + durationString, e);
    }
  }
}
