package dev.jcputney.elearning.parser.input.scorm2004.ims.ss.types;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import java.io.IOException;
import java.math.BigDecimal;

/**
 * Custom deserializer for PercentType, ensuring the value is within the range of 0 to 1.
 */
public class PercentTypeDeserializer extends JsonDeserializer<PercentType> {

  @Override
  public PercentType deserialize(JsonParser p, DeserializationContext context)
      throws IOException {
    String value = p.getText();
    try {
      return new PercentType(new BigDecimal(value));
    } catch (IllegalArgumentException e) {
      throw new IOException("Invalid PercentType value: " + value, e);
    }
  }
}