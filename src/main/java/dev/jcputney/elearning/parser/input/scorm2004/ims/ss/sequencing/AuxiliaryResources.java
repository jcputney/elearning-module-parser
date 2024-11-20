package dev.jcputney.elearning.parser.input.scorm2004.ims.ss.sequencing;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import dev.jcputney.elearning.parser.input.scorm2004.IMSSS;
import java.util.List;
import lombok.Data;

/**
 * Represents a collection of auxiliary resources associated with a learning activity within the
 * SCORM IMS Simple Sequencing (IMSSS) schema. Auxiliary resources are additional resources that
 * support the main content of the activity, such as supplementary documents, tools, or media
 * files.
 *
 * <p>Auxiliary resources are intended to enhance the learning experience by providing extra
 * materials that are accessible alongside the primary content. They do not directly contribute to
 * the activity's objectives or completion status but serve as helpful references or optional tools
 * for the learner.</p>
 *
 * <p>This class holds a list of individual {@link AuxiliaryResource} objects, each of which
 * describes a specific auxiliary resource associated with the learning activity.</p>
 *
 * <p>The IMSSS namespace is specified by {@link IMSSS#NAMESPACE_URI}, following the SCORM 2004
 * standards for auxiliary resource management in sequencing and navigation.</p>
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class AuxiliaryResources {

  /**
   * A list of auxiliary resources associated with the learning activity. Each resource provides
   * additional, optional content for the learner and is represented by an instance of
   * {@link AuxiliaryResource}.
   *
   * <p>If no auxiliary resources are defined, this list may be empty.</p>
   */
  @JacksonXmlElementWrapper(useWrapping = false)
  @JacksonXmlProperty(localName = "auxiliaryResource", namespace = IMSSS.NAMESPACE_URI)
  private List<AuxiliaryResource> auxiliaryResourceList;
}
