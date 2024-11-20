package dev.jcputney.elearning.parser.input.lom.types;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;

/**
 * Represents a date element in LOM metadata, including the date value and an optional description.
 * <pre>{@code
 * <xs:complexType name="date">
 *   <xs:complexContent>
 *     <xs:extension base="DateTime">
 *       <xs:attributeGroup ref="ag:date"/>
 *     </xs:extension>
 *   </xs:complexContent>
 * </xs:complexType>
 * }</pre>
 */
@Data
public class Date {

  /**
   * The actual date-time value.
   */
  @JacksonXmlProperty(localName = "dateTime")
  private String dateTime;

  /**
   * A description of the date, typically a LangString.
   */
  @JacksonXmlProperty(localName = "description")
  private UnboundLangString description;
}