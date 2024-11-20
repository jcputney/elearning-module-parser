package dev.jcputney.elearning.parser.input.scorm2004.adl.navigation;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import dev.jcputney.elearning.parser.input.scorm2004.ADLNav;
import lombok.Data;

/**
 * Represents the presentation settings for the navigation controls. This class contains additional
 * settings that may define whether the navigation interface should be shown or hidden under certain
 * conditions.
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Presentation {

  /**
   * Contains settings for the navigation interface within the LMS. This allows the LMS to control
   * which navigation components are displayed to the learner.
   */
  @JacksonXmlProperty(localName = "navigationInterface", namespace = ADLNav.NAMESPACE_URI)
  private NavigationInterface navigationInterface;
}
