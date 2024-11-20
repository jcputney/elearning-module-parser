package dev.jcputney.elearning.parser.input.cmi5;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import dev.jcputney.elearning.parser.input.cmi5.types.Objective;
import java.util.List;
import lombok.Data;

/**
 * Represents the objectives section of a CMI5 course structure, containing a list of defined
 * "objectives".
 *
 * <p>Defined in the schema as:</p>
 *
 * <pre>{@code
 * <xs:complexType name="objectivesType">
 *   <xs:sequence>
 *     <xs:element name="objective" minOccurs="1" maxOccurs="unbounded">
 *       <xs:complexType>
 *         <xs:all>
 *           <xs:element name="title" type="textType"/>
 *           <xs:element name="description" type="textType"/>
 *         </xs:all>
 *         <xs:attribute name="id" type="xs:anyURI" use="required"/>
 *       </xs:complexType>
 *     </xs:element>
 *     <xs:group ref="anyElement"/>
 *   </xs:sequence>
 *   <xs:attributeGroup ref="anyAttribute"/>
 * </xs:complexType>
 * }</pre>
 */
@Data
public class Objectives {

  /**
   * A list of defined "objectives", each represented by an {@link Objective}.
   *
   * <pre>{@code
   * <xs:element name="objective" minOccurs="1" maxOccurs="unbounded">
   *   <xs:complexType>
   *     <xs:all>
   *       <xs:element name="title" type="textType"/>
   *       <xs:element name="description" type="textType"/>
   *     </xs:all>
   *     <xs:attribute name="id" type="xs:anyURI" use="required"/>
   *   </xs:complexType>
   * }</pre>
   */
  @JacksonXmlElementWrapper(useWrapping = false)
  @JacksonXmlProperty(localName = "objective")
  private List<Objective> objectives;

}
