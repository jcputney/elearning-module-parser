package dev.jcputney.elearning.parser.input.cmi5.types;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import java.util.List;
import lombok.Data;

/**
 * Represents a list of objectives referenced by an AU or block in a CMI5 course structure.
 *
 * <p>Defined in the schema as:</p>
 *
 * <pre>{@code
 * <xs:complexType name="referencesObjectivesType">
 *   <xs:sequence>
 *     <xs:element name="objective" maxOccurs="unbounded">
 *       <xs:complexType>
 *         <xs:attribute name="idref" type="xs:anyURI"/>
 *       </xs:complexType>
 *     </xs:element>
 *     <xs:group ref="anyElement"/>
 *   </xs:sequence>
 *   <xs:attributeGroup ref="anyAttribute"/>
 * </xs:complexType>
 * }</pre>
 */
@Data
public class ReferencesObjectives {

  /**
   * A list of referenced "objectives", each represented by an {@link ObjectiveReference}.
   *
   * <pre>{@code
   * <xs:element name="objective" maxOccurs="unbounded">
   *   <xs:complexType>
   *     <xs:attribute name="idref" type="xs:anyURI"/>
   *   </xs:complexType>
   * }</pre>
   */
  @JacksonXmlElementWrapper(useWrapping = false)
  @JacksonXmlProperty(localName = "objective")
  private List<ObjectiveReference> objectives;

}
