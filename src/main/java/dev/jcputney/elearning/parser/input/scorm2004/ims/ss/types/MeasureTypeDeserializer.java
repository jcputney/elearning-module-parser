package dev.jcputney.elearning.parser.input.scorm2004.ims.ss.types;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import java.io.IOException;
import java.math.BigDecimal;

/**
 * Custom deserializer for MeasureType, ensuring the value is within the range of -1 to 1 and has at
 * least four decimal digits.
 */
public class MeasureTypeDeserializer extends JsonDeserializer<MeasureType> {

  @Override
  public MeasureType deserialize(JsonParser p, DeserializationContext context)
      throws IOException {
    String value = p.getText();
    try {
      return new MeasureType(new BigDecimal(value));
    } catch (IllegalArgumentException e) {
      throw new IOException("Invalid MeasureType value: " + value, e);
    }
  }
}