/*
 * Copyright (c) 2024. Jonathan Putney
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package dev.jcputney.elearning.parser.input.lom.types;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;

/**
 * Represents the <code>resource</code> element in the LOM schema, containing information about a
 * related resource, including identifiers and descriptions.
 * <p>
 * Schema snippet:
 * <pre>{@code
 * <xs:complexType name="resource">
 *   <xs:choice minOccurs="0" maxOccurs="unbounded">
 *     <xs:group ref="identifier"/>
 *     <xs:group ref="description"/>
 *     <xs:group ref="ex:customElements"/>
 *   </xs:choice>
 *   <xs:attributeGroup ref="ag:resource"/>
 * </xs:complexType>
 * }</pre>
 */
@Builder
@Getter
@Jacksonized
@JsonFormat(with = JsonFormat.Feature.ACCEPT_CASE_INSENSITIVE_PROPERTIES)
public class Resource {

  /**
   * The list of identifiers that uniquely reference the related resource.
   * <p>
   * Schema snippet:
   * <pre>{@code
   * <xs:complexType name="identifier">
   *   <xs:choice minOccurs="0" maxOccurs="unbounded">
   *     <xs:group ref="catalog"/>
   *     <xs:group ref="entry"/>
   *     <xs:group ref="ex:customElements"/>
   *   </xs:choice>
   *   <xs:attributeGroup ref="ag:identifier"/>
   * </xs:complexType>
   * }</pre>
   */
  @JacksonXmlElementWrapper(localName = "identifier", useWrapping = false)
  @JacksonXmlProperty(localName = "identifier")
  private List<Identifier> identifiers;

  /**
   * A list of descriptions providing language-specific information about the related resource.
   * <p>
   * Schema snippet:
   * <pre>{@code
   * <xs:complexType name="description">
   *   <xs:complexContent>
   *     <xs:extension base="LangString">
   *       <xs:attributeGroup ref="ag:description"/>
   *     </xs:extension>
   *   </xs:complexContent>
   * </xs:complexType>
   * }</pre>
   */
  @JacksonXmlProperty(localName = "description")
  private UnboundLangString descriptions;

  /**
   * A list of catalog entries for the resource.
   *
   * <p>Schema snippet:</p>
   * <pre>{@code
   * <xsd:element ref="catalogentry" minOccurs="0" maxOccurs="unbounded"/>
   * }</pre>
   */
  @JacksonXmlElementWrapper(localName = "catalogentry", useWrapping = false)
  @JacksonXmlProperty(localName = "catalogentry")
  private List<CatalogEntry> catalogEntries;

  /**
   * A placeholder for custom elements that extend the resource information. This allows for
   * additional metadata to be included that is not part of the standard schema.
   *
   * <p>Schema snippet:
   * <pre>{@code
   * <complexType name="customElements">
   *   <complexContent>
   *     <extension base="xs:anyType"/>
   *   </complexContent>
   * </complexType>
   * }</pre>
   */
  @JacksonXmlElementWrapper(localName = "customElements", useWrapping = false)
  @JacksonXmlProperty(localName = "customElements")
  private List<Object> customElements;
}
