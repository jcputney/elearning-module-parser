package dev.jcputney.elearning.parser.input.scorm2004.ims.cp;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;

/**
 * Represents a dependency element within a resource, specifying a relationship to another resource
 * that this resource relies upon.
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Scorm2004Dependency {

  /**
   * The identifier reference for the dependency. This points to another resource in the content
   * package that this resource depends on.
   */
  @JacksonXmlProperty(isAttribute = true, localName = "identifierref")
  private String identifierRef;
}
