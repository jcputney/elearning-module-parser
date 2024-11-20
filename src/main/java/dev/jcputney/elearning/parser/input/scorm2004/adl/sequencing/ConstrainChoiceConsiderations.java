package dev.jcputney.elearning.parser.input.scorm2004.adl.sequencing;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;

/**
 * Represents the constrainedChoiceConsiderationsType complex type, defining choice and activation
 * restrictions. The following schema snippet shows the structure of the
 * constrainedChoiceConsiderationsType element:
 * <pre>{@code
 *   <xs:complexType name = "constrainChoiceConsiderationsType">
 *      <xs:attribute name = "preventActivation" default = "false" type = "xs:boolean"/>
 *      <xs:attribute name = "constrainChoice" default = "false" type = "xs:boolean"/>
 *   </xs:complexType>
 * }</pre>
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ConstrainChoiceConsiderations {

  /**
   * Prevents activation when true. Default is false.
   */
  @JacksonXmlProperty(localName = "preventActivation", isAttribute = true)
  private boolean preventActivation = false;

  /**
   * Constrains choice when true. Default is false.
   */
  @JacksonXmlProperty(localName = "constrainChoice", isAttribute = true)
  private boolean constrainChoice = false;
}
