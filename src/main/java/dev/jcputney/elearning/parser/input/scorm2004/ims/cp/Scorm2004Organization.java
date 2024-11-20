package dev.jcputney.elearning.parser.input.scorm2004.ims.cp;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import dev.jcputney.elearning.parser.input.scorm2004.ADLCP;
import dev.jcputney.elearning.parser.input.scorm2004.ADLSeq;
import dev.jcputney.elearning.parser.input.scorm2004.IMSSS;
import dev.jcputney.elearning.parser.input.scorm2004.Scorm2004Manifest;
import dev.jcputney.elearning.parser.input.scorm2004.ims.ss.sequencing.Sequencing;
import java.util.List;
import lombok.Data;

/**
 * Represents a single organization within the content package. Each organization may contain
 * multiple items structured hierarchically.
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Scorm2004Organization {

  /**
   * The unique identifier for this organization, used to distinguish it from other organizations
   * within the same manifest.
   */
  @JacksonXmlProperty(isAttribute = true)
  private String identifier;

  /**
   * The structure of this organization, defining the hierarchical arrangement of items within the
   * organization. The structure is typically a tree-like arrangement of items, but is not typically
   * used in any meaningful way.
   */
  @JacksonXmlProperty(isAttribute = true)
  private String structure = "hierarchical";

  /**
   * The title of this organization, providing a descriptive label for the learning structure it
   * represents.
   */
  @JacksonXmlProperty(localName = "title", namespace = Scorm2004Manifest.NAMESPACE_URI)
  private String title;

  /**
   * A list of items within this organization, representing a hierarchical structure of learning
   * objects.
   */
  @JacksonXmlElementWrapper(useWrapping = false)
  @JacksonXmlProperty(localName = "item", namespace = Scorm2004Manifest.NAMESPACE_URI)
  private List<Scorm2004Item> items;

  /**
   * The default for the adlcp:objectivesGlobalToSystem attribute for items in this organization. If
   * true, "objectives" defined in this organization are considered global to the system. If false,
   * "objectives" are considered local to the organization.
   */
  @JacksonXmlProperty(isAttribute = true, localName = "objectivesGlobalToSystem", namespace = ADLSeq.NAMESPACE_URI)
  private boolean objectivesGlobalToSystem = false;

  /**
   * The default for the adlcp:sharedDataGlobalToSystem attribute for items in this organization. If
   * true, "sharedData" defined in this organization are considered global to the system. If false,
   * "sharedData" is considered local to the organization.
   */
  @JacksonXmlProperty(isAttribute = true, localName = "sharedDataGlobalToSystem", namespace = ADLCP.NAMESPACE_URI)
  private boolean sharedDataGlobalToSystem = false;

  /**
   * Metadata associated with this organization, providing details such as creation date, and other
   * descriptive information relevant to the organization.
   */
  @JacksonXmlProperty(localName = "metadata", namespace = Scorm2004Manifest.NAMESPACE_URI)
  private Scorm2004SubMetadata metadata;

  /**
   * The sequencing element for this organization, defining control modes, delivery controls, and
   * sequencing rules.
   */
  @JacksonXmlProperty(localName = "sequencing", namespace = IMSSS.NAMESPACE_URI)
  private Sequencing sequencing;
}
