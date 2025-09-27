package dev.jcputney.elearning.parser.input.scorm2004.ims.ss.types;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.jcputney.elearning.parser.input.scorm2004.adl.cp.CompletionThreshold;
import dev.jcputney.elearning.parser.input.scorm2004.ims.ss.sequencing.RuleCondition;
import java.math.BigDecimal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MeasureTypeDeserializerTest {

  private ObjectMapper objectMapper;

  @BeforeEach
  void setUp() {
    objectMapper = new ObjectMapper();
    objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
  }

  @Test
  void deserialize_invalidValue_returnsNullForRuleCondition() throws Exception {
    String json = """
        {"measureThreshold":"{"}
        """;

    RuleCondition condition = objectMapper.readValue(json, RuleCondition.class);

    assertNull(condition.getMeasureThreshold());
  }

  @Test
  void deserialize_invalidValue_keepsCompletionThresholdDefault() throws Exception {
    String json = """
        {"minProgressMeasure":"{"}
        """;

    CompletionThreshold threshold = objectMapper.readValue(json, CompletionThreshold.class);

    assertEquals(new BigDecimal("1.0000"), threshold
        .getMinProgressMeasure()
        .getValue());
  }

  @Test
  void deserialize_blankValue_returnsNull() throws Exception {
    String json = """
        {"measureThreshold":"   "}
        """;

    RuleCondition condition = objectMapper.readValue(json, RuleCondition.class);

    assertNull(condition.getMeasureThreshold());
  }
}
