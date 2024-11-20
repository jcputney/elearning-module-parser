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

  @Override
  public Instant deserialize(JsonParser parser, DeserializationContext context) throws IOException {
    String dateTimeString = parser.getText();
    try {
      return Instant.parse(dateTimeString);
    } catch (DateTimeParseException e) {
      throw new IOException("Invalid ISO 8601 date-time format for Instant: " + dateTimeString, e);
    }
  }
}