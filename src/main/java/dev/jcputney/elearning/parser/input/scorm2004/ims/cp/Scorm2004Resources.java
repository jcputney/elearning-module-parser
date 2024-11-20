package dev.jcputney.elearning.parser.input.scorm2004.ims.cp;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import dev.jcputney.elearning.parser.input.scorm2004.Scorm2004Manifest;
import java.util.List;
import lombok.Data;

/**
 * Represents the collection of resources within the content package. Each resource defines a
 * learning object or asset.
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Scorm2004Resources {

  /**
   * The base URL for all resources in the content package. This URL is used to resolve relative
   * paths for resources.
   */
  @JacksonXmlProperty(isAttribute = true, localName = "base", namespace = "http://www.w3.org/XML/1998/namespace")
  private String base;

  /**
   * A list of resources in the content package, each representing a specific learning object or
   * asset.
   */
  @JacksonXmlElementWrapper(useWrapping = false)
  @JacksonXmlProperty(localName = "resource", namespace = Scorm2004Manifest.NAMESPACE_URI)
  private List<Scorm2004Resource> resourceList;
}
