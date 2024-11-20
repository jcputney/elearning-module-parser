package dev.jcputney.elearning.parser.input.scorm2004.adl.sequencing;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import dev.jcputney.elearning.parser.input.scorm2004.ADLSeq;
import java.util.List;
import lombok.Data;

/**
 * Represents an objective within the SCORM sequencing model. "Objectives" define specific learning
 * goals and their status and measure. The following schema snippet shows the structure of an
 * "objective" element:
 * <pre>{@code
 *   <xs:complexType name="objectiveType">
 *     <xs:sequence>
 *        <xs:element ref = "mapInfo" minOccurs = "1" maxOccurs = "unbounded"/>
 *     </xs:sequence>
 *     <xs:attribute name = "objectiveID" use = "required" type = "xs:anyURI"/>
 *   </xs:complexType>
 * }</pre>
 */
@Data
public class ADLObjective {

  /**
   * The unique identifier for this "objective". This is used to map the "objective" within the LMS
   * to track the learner’s progress and completion status.
   */
  @JacksonXmlProperty(isAttribute = true, localName = "objectiveID")
  private String objectiveID;

  /**
   * List of mappings for this "objective", defining connections to global "objectives" or other
   * objectives within the LMS.
   */
  @JacksonXmlElementWrapper(useWrapping = false)
  @JacksonXmlProperty(localName = "mapInfo", namespace = ADLSeq.NAMESPACE_URI)
  private List<MapInfo> mapInfoList;
}
