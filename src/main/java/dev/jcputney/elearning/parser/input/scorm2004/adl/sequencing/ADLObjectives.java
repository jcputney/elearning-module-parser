package dev.jcputney.elearning.parser.input.scorm2004.adl.sequencing;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import dev.jcputney.elearning.parser.input.scorm2004.ADLSeq;
import java.util.List;
import lombok.Data;

/**
 * Represents the objectivesType complex type, containing a list of objective elements. The
 * following schema shows the structure of the objectivesType element:
 * <pre>{@code
 *   <xs:complexType name="objectivesType">
 *      <xs:sequence>
 *         <xs:element ref = "objective" minOccurs = "1" maxOccurs = "unbounded"/>
 *      </xs:sequence>
 *   </xs:complexType>
 * }</pre>
 */
@Data
public class ADLObjectives {

  /**
   * List of objectives. Each "objective" defines specific attributes for rollup.
   */
  @JacksonXmlElementWrapper(useWrapping = false)
  @JacksonXmlProperty(localName = "objective", namespace = ADLSeq.NAMESPACE_URI)
  private List<ADLObjective> objectiveList;
}
