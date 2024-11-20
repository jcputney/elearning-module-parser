package dev.jcputney.elearning.parser.input.lom.types;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlText;
import lombok.Data;

/**
 * Represents a LangString in LOM metadata, which is a collection of strings with language
 * attributes. This type is used for fields such as title, description, and keyword.
 *
 * <pre>{@code
 * <xs:complexType name="LangString">
 *   <xs:choice minOccurs="0" maxOccurs="unbounded">
 *     <xs:element name="string" type="langString"/>
 *     <xs:group ref="ex:customElements"/>
 *   </xs:choice>
 * </xs:complexType>
 *
 * <xs:complexType name="langString">
 *   <xs:simpleContent>
 *     <xs:extension base="CharacterString">
 *       <xs:attribute name="language" type="LanguageId"/>
 *     </xs:extension>
 *   </xs:simpleContent>
 * </xs:complexType>
 * }</pre>
 */
@Data
public class LangString {

  /**
   * The language of the string.
   */
  @JacksonXmlProperty(isAttribute = true)
  private String language;

  /**
   * The actual string value.
   */
  @JacksonXmlText
  private String value;
}
