package dev.jcputney.elearning.parser.input.scorm2004.adl.cp;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import dev.jcputney.elearning.parser.input.scorm2004.ims.ss.types.MeasureType;
import dev.jcputney.elearning.parser.input.scorm2004.ims.ss.types.MeasureTypeDeserializer;
import dev.jcputney.elearning.parser.input.scorm2004.ims.ss.types.PercentType;
import dev.jcputney.elearning.parser.input.scorm2004.ims.ss.types.PercentTypeDeserializer;
import java.math.BigDecimal;
import lombok.Data;

/**
 * Represents the completion threshold element, which specifies the minimum progress required to
 * mark the content as complete. Includes attributes for progress weight and a boolean indicating if
 * completion is by measure.
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CompletionThreshold {

  /**
   * Defines the minimum progress measure for the content to be marked complete. The value should be
   * between -1.0 and 1.0, representing the fraction of content that must be completed.
   */
  @JacksonXmlProperty(isAttribute = true, localName = "minProgressMeasure")
  @JsonDeserialize(using = MeasureTypeDeserializer.class)
  private MeasureType minProgressMeasure = new MeasureType(BigDecimal.ONE);

  /**
   * Specifies the relative weight of this content item towards overall progress. The value is
   * between 0.0 and 1.0, where 1.0 indicates the full weight.
   */
  @JacksonXmlProperty(isAttribute = true, localName = "progressWeight")
  @JsonDeserialize(using = PercentTypeDeserializer.class)
  private PercentType progressWeight = new PercentType(BigDecimal.ONE);

  /**
   * Indicates whether completion is determined by measure. If true, completion is based on
   * achieving the minimum progress measure defined.
   */
  @JacksonXmlProperty(isAttribute = true, localName = "completedByMeasure")
  private Boolean completedByMeasure = false;
}
