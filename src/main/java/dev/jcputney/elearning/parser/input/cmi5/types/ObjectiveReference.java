package dev.jcputney.elearning.parser.input.cmi5.types;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;

/**
 * Represents a single objective reference.
 */
@Data
public class ObjectiveReference {

  /**
   * The ID reference to the objective, represented as an anyURI.
   *
   * <pre>{@code
   * <xs:attribute name="idref" type="xs:anyURI"/>
   * }</pre>
   */
  @JacksonXmlProperty(isAttribute = true)
  private String idref;
}
