package dev.jcputney.elearning.parser.input.scorm12.adl;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;

/**
 * Represents the {@code <adlcp:prerequisites>} element in SCORM 1.2. This element includes a string value
 * and an optional type attribute.
 */
@Data
public class Scorm12Prerequisites {

  /**
   * The string content of the prerequisites element.
   */
  @JacksonXmlProperty(localName = "value")
  private String value;

  /**
   * The type attribute of the prerequisites element, which is optional. Example value:
   * "aicc_script".
   */
  @JacksonXmlProperty(isAttribute = true, localName = "type")
  private String type;
}
