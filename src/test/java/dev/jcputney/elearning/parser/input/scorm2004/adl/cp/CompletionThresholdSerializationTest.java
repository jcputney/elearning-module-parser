package dev.jcputney.elearning.parser.input.scorm2004.adl.cp;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.jcputney.elearning.parser.input.common.PercentType;
import java.math.BigDecimal;
import org.junit.jupiter.api.Test;

/**
 * Test to demonstrate the serialization/deserialization bug with PercentType
 * in CompletionThreshold.
 */
class CompletionThresholdSerializationTest {

  @Test
  void testSerializationDeserialization() throws Exception {
    ObjectMapper mapper = new ObjectMapper();

    // Create a CompletionThreshold with progressWeight
    CompletionThreshold threshold = new CompletionThreshold();
    threshold.setProgressWeight(new PercentType(new BigDecimal("0.5")));

    // Serialize to JSON
    String json = mapper.writeValueAsString(threshold);
    System.out.println("Serialized CompletionThreshold: " + json);

    // This should fail because progressWeight is missing @JsonSerialize annotation
    CompletionThreshold deserialized = mapper.readValue(json, CompletionThreshold.class);

    assertNotNull(deserialized.getProgressWeight());
    assertEquals(new BigDecimal("0.5"), deserialized.getProgressWeight().value());
  }
}
