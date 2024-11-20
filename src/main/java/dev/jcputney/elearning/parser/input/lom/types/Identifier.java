package dev.jcputney.elearning.parser.input.lom.types;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;

/**
 * Represents an identifier for a related resource.
 * <pre>{@code
 * <xs:complexType name="identifier">
 *   <xs:choice minOccurs="0" maxOccurs="unbounded">
 *     <xs:element name="catalog" type="catalog" minOccurs="0"/>
 *     <xs:element name="entry" type="entry" minOccurs="0"/>
 *   </xs:choice>
 *   <xs:attributeGroup ref="ag:identifier"/>
 * </xs:complexType>
 * }</pre>
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Identifier {

  /**
   * The catalog for the identifier.
   */
  @JacksonXmlProperty(localName = "catalog")
  private String catalog;

  /**
   * The entry for the identifier.
   */
  @JacksonXmlProperty(localName = "entry")
  private String entry;
}