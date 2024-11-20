package dev.jcputney.elearning.parser.input.lom.types;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import dev.jcputney.elearning.parser.util.DurationIso8601Deserializer;
import java.time.Duration;
import lombok.Data;

/**
 * Represents the duration metadata of a resource, including the duration value in ISO 8601 format
 * and an optional description.
 * <p>
 * The following schema snippet illustrates the expected XML structure for duration:
 * <pre>{@code
 * <xs:complexType name="duration">
 *   <xs:complexContent>
 *     <xs:extension base="Duration">
 *       <xs:attributeGroup ref="ag:duration"/>
 *     </xs:extension>
 *   </xs:complexContent>
 * </xs:complexType>
 * }</pre>
 * <p>
 * A duration value typically conforms to the ISO 8601 format (e.g., "PT10M" for 10 minutes). The
 * description element, if present, allows for a natural language explanation of the duration.
 * <p>
 * Example XML:
 * <pre>{@code
 * <duration>
 *   <duration>PT10M</duration>
 *   <description>
 *     <string language="en-us">This video is 10 minutes long.</string>
 *   </description>
 * </duration>
 * }</pre>
 */
@Data
public class LomDuration {

  /**
   * The duration of the resource in ISO 8601 format (e.g., "PT10M").
   */
  @JacksonXmlProperty(localName = "duration")
  @JsonDeserialize(using = DurationIso8601Deserializer.class)
  private Duration duration;

  /**
   * A natural language description of the duration, represented as a {@link LangString}.
   * <p>
   * Example:
   * <pre>{@code
   * <description>
   *   <string language="en-us">This video is 10 minutes long.</string>
   * </description>
   * }</pre>
   */
  @JacksonXmlProperty(localName = "description")
  private SingleLangString description;
}
