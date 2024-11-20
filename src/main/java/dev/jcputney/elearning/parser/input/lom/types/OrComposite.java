package dev.jcputney.elearning.parser.input.lom.types;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;

/**
 * Represents an <code>orComposite</code> element in the LOM schema, defining a set of conditions
 * related to platform or software requirements. Each <code>orComposite</code> provides specific
 * information about the type, name, and version constraints for the required environment.
 * <p>
 * Schema snippet:
 * <pre>{@code
 * <xs:complexType name="orComposite">
 *   <xs:choice minOccurs="0" maxOccurs="unbounded">
 *     <xs:group ref="type"/>
 *     <xs:group ref="name"/>
 *     <xs:group ref="minimumVersion"/>
 *     <xs:group ref="maximumVersion"/>
 *     <xs:group ref="ex:customElements"/>
 *   </xs:choice>
 *   <xs:attributeGroup ref="ag:orComposite"/>
 * </xs:complexType>
 * }</pre>
 */
@Data
public class OrComposite {

  /**
   * The type of platform or software requirement, represented as a controlled vocabulary.
   * <p>
   * Schema snippet:
   * <pre>{@code
   * <xs:complexType name="type">
   *   <xs:complexContent>
   *     <xs:extension base="typeVocab">
   *       <xs:attributeGroup ref="ag:type"/>
   *     </xs:extension>
   *   </xs:complexContent>
   * </xs:complexType>
   * }</pre>
   */
  @JacksonXmlElementWrapper(localName = "type", useWrapping = false)
  @JacksonXmlProperty(localName = "type")
  private SourceValuePair<Type> type;

  /**
   * The name of the platform or software, represented as a controlled vocabulary.
   * <p>
   * Schema snippet:
   * <pre>{@code
   * <xs:complexType name="name">
   *   <xs:complexContent>
   *     <xs:extension base="nameVocab">
   *       <xs:attributeGroup ref="ag:name"/>
   *     </xs:extension>
   *   </xs:complexContent>
   * </xs:complexType>
   * }</pre>
   */
  @JacksonXmlElementWrapper(localName = "name", useWrapping = false)
  @JacksonXmlProperty(localName = "name")
  private SourceValuePair<Name> name;

  /**
   * The minimum version of the platform or software required.
   * <p>
   * Schema snippet:
   * <pre>{@code
   * <xs:complexType name="minimumVersion">
   *   <xs:simpleContent>
   *     <xs:extension base="CharacterString">
   *       <xs:attributeGroup ref="ag:minimumVersion"/>
   *     </xs:extension>
   *   </xs:simpleContent>
   * </xs:complexType>
   * }</pre>
   */
  @JacksonXmlProperty(localName = "minimumVersion")
  private String minimumVersion;

  /**
   * The maximum version of the platform or software required.
   * <p>
   * Schema snippet:
   * <pre>{@code
   * <xs:complexType name="maximumVersion">
   *   <xs:simpleContent>
   *     <xs:extension base="CharacterString">
   *       <xs:attributeGroup ref="ag:maximumVersion"/>
   *     </xs:extension>
   *   </xs:simpleContent>
   * </xs:complexType>
   * }</pre>
   */
  @JacksonXmlProperty(localName = "maximumVersion")
  private String maximumVersion;
}