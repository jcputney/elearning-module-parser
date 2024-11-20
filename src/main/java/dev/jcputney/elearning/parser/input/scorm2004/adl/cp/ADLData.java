package dev.jcputney.elearning.parser.input.scorm2004.adl.cp;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import dev.jcputney.elearning.parser.input.scorm2004.ADLCP;
import java.util.List;
import lombok.Data;

/**
 * Represents the data element, which contains multiple map elements.
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ADLData {

  /**
   * List of map elements, where each map specifies a target and shared data access.
   */
  @JacksonXmlElementWrapper(useWrapping = false)
  @JacksonXmlProperty(localName = "map", namespace = ADLCP.NAMESPACE_URI)
  private List<DataMap> mapList;
}
