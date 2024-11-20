package dev.jcputney.elearning.parser.input.lom.types;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;

/**
 * Represents a single language string in LOM metadata. This type is used for fields that only
 * require a single string value with a language attribute.
 *
 * <pre>{@code
 * <xs:complexType name="singleLangString">
 *   <xs:sequence>
 *     <xs:element name="string" type="langString"/>
 *   </xs:sequence>
 * </xs:complexType>
 * }</pre>
 */
@Data
public class SingleLangString {
  /**
   * The string value for a given language.
   */
  @JacksonXmlProperty(localName = "string")
  private LangString langString;
}
