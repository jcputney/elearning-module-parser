package dev.jcputney.elearning.parser.input.cmi5.types;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlText;
import lombok.Data;

/**
 * Represents a single localized string with an optional language attribute.
 *
 * <p>Defined in the schema as:</p>
 *
 * <pre>{@code
 * <xs:element name="langstring" maxOccurs="unbounded" minOccurs="1">
 *   <xs:complexType>
 *     <xs:simpleContent>
 *       <xs:extension base="xs:string">
 *         <xs:attribute name="lang" type="xs:language"/>
 *         <xs:attributeGroup ref="anyAttribute"/>
 *       </xs:extension>
 *     </xs:simpleContent>
 *   </xs:complexType>
 * }</pre>
 */
@Data
public class LangString {

  /**
   * The actual text content of the localized string.
   *
   * <pre>
   * <xs:simpleContent>
   *   <xs:extension base="xs:string"/>
   * </pre>
   */
  @JacksonXmlText
  private String value;

  /**
   * The language of the string, represented as an optional attribute.
   *
   * <pre>
   * <xs:attribute name="lang" type="xs:language"/>
   * </pre>
   */
  @JacksonXmlProperty(isAttribute = true)
  private String lang;
}
