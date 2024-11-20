package dev.jcputney.elearning.parser.input.scorm2004.adl.navigation;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import dev.jcputney.elearning.parser.input.scorm2004.ADLNav;
import java.util.List;
import lombok.Data;

/**
 * Represents the navigation interface controls, which specify options for interacting with the LMS
 * navigation UI. This includes defining the appearance and functionality of the navigation buttons
 * and controls.
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class NavigationInterface {

  /**
   * A list of elements defining which parts of the LMS UI should be hidden. Each item specifies an
   * individual UI component to be hidden. Common values are options like "exit" or "continue".
   */
  @JacksonXmlElementWrapper(useWrapping = false)
  @JacksonXmlProperty(localName = "hideLMSUI", namespace = ADLNav.NAMESPACE_URI)
  private List<HideLMSUI> hideLMSUI;
}
