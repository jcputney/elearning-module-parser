package dev.jcputney.elearning.parser.input.lom.types;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import java.util.List;
import lombok.Data;

/**
 * Represents a list of language strings in LOM metadata. This type is used for fields that require
 * multiple string values with language attributes.
 *
 * <pre>{@code
 * <xs:complexType name="unboundLangString">
 *   <xs:sequence>
 *     <xs:element name="string" type="langString" minOccurs="0" maxOccurs="unbounded"/>
 *   </xs:sequence>
 * </xs:complexType>
 * }</pre>
 */
@Data
public class UnboundLangString {
  /**
   * The string values for multiple languages.
   */
  @JacksonXmlElementWrapper(useWrapping = false)
  @JacksonXmlProperty(localName = "string")
  private List<LangString> langStrings;
}
