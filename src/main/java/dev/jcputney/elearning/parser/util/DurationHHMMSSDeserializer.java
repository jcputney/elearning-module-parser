package dev.jcputney.elearning.parser.util;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import java.io.IOException;
import java.time.Duration;

/**
 * Custom deserializer for {@link Duration} objects, allowing HH:MM:SS duration strings to be parsed
 * into {@link java.time.Duration} instances.
 *
 * <p>This deserializer supports durations in the format HH:MM:SS, where HH represents
 * hours, MM represents minutes, and SS represents seconds. It does not support days, months, or
 * years, as these are not compatible with {@link java.time.Duration}.</p>
 * </p>
 */
public class DurationHHMMSSDeserializer extends JsonDeserializer<Duration> {

  @Override
  public Duration deserialize(JsonParser parser, DeserializationContext context)
      throws IOException {
    String durationString = parser.getText();
    try {
      String[] parts = durationString.split(":");
      if (parts.length != 3) {
        throw new IOException(
            "Invalid HH:MM:SS duration format for java.time.Duration: " + durationString);
      }
      int hours = Integer.parseInt(parts[0]);
      int minutes = Integer.parseInt(parts[1]);
      int seconds = Integer.parseInt(parts[2]);
      return Duration.ofHours(hours).plusMinutes(minutes).plusSeconds(seconds);
    } catch (NumberFormatException e) {
      throw new IOException(
          "Invalid HH:MM:SS duration format for java.time.Duration: " + durationString, e);
    }
  }
}
