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

package dev.jcputney.elearning.parser.input.lom;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import dev.jcputney.elearning.parser.input.lom.types.CatalogEntry;
import dev.jcputney.elearning.parser.input.lom.types.ContributeMeta;
import dev.jcputney.elearning.parser.input.lom.types.Identifier;
import java.util.List;
import lombok.Data;

/**
 * Represents the meta-metadata information about a learning object in a Learning Object Metadata
 * (LOM) document. Meta-metadata provides information about the metadata itself, including its
 * origin, purpose, and maintenance.
 *
 * <p>Schema snippet:
 * <pre>{@code
 * <complexType name="metaMetadata">
 *   <choice minOccurs="0" maxOccurs="unbounded">
 *     <group ref="identifier"/>
 *     <group ref="contributeMeta"/>
 *     <group ref="metadataSchema"/>
 *     <group ref="language"/>
 *     <group ref="ex:customElements"/>
 *   </choice>
 *   <attributeGroup ref="ag:metaMetadata"/>
 * </complexType>
 * }</pre>
 */
@Data
@JsonFormat(with = JsonFormat.Feature.ACCEPT_CASE_INSENSITIVE_PROPERTIES)
public class MetaMetadata {

  /**
   * The list of unique identifiers for the metadata. Each identifier typically includes a catalog
   * and an entry.
   *
   * <p>Schema snippet:
   * <pre>{@code
   * <complexType name="identifier">
   *   <choice minOccurs="0" maxOccurs="unbounded">
   *     <group ref="catalog"/>
   *     <group ref="entry"/>
   *     <group ref="ex:customElements"/>
   *   </choice>
   *   <attributeGroup ref="ag:identifier"/>
   * </complexType>
   * }</pre>
   */
  @JacksonXmlElementWrapper(localName = "identifier", useWrapping = false)
  @JacksonXmlProperty(localName = "identifier")
  private List<Identifier> identifier;

  /**
   * A list of catalog entries for the metadata.
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
   * The list of contributors to the metadata, including their roles, entities, and contribution
   * dates.
   *
   * <p>Schema snippet:
   * <pre>{@code
   * <complexType name="contributeMeta">
   *   <choice minOccurs="0" maxOccurs="unbounded">
   *     <group ref="roleMeta"/>
   *     <group ref="entityUnbounded"/>
   *     <group ref="date"/>
   *     <group ref="ex:customElements"/>
   *   </choice>
   *   <attributeGroup ref="ag:contribute"/>
   * </complexType>
   * }</pre>
   */
  @JacksonXmlElementWrapper(localName = "contribute", useWrapping = false)
  @JacksonXmlProperty(localName = "contribute")
  private List<ContributeMeta> contribute;

  /**
   * The metadata schema or standard being used. Typically represented as a list of strings.
   *
   * <p>Schema snippet:
   * <pre>{@code
   * <complexType name="metadataSchema">
   *   <simpleContent>
   *     <extension base="CharacterString">
   *       <attributeGroup ref="ag:metadataSchema"/>
   *     </extension>
   *   </simpleContent>
   * </complexType>
   * }</pre>
   */
  @JacksonXmlElementWrapper(localName = "metadataSchema", useWrapping = false)
  @JacksonXmlProperty(localName = "metadataSchema")
  @JsonAlias("metadatascheme")
  private List<String> metadataSchema;

  /**
   * The language used for the metadata content.
   *
   * <p>Schema snippet:
   * <pre>{@code
   * <complexType name="language">
   *   <simpleContent>
   *     <extension base="LanguageId">
   *       <attributeGroup ref="ag:language"/>
   *     </extension>
   *   </simpleContent>
   * </complexType>
   * }</pre>
   */
  @JacksonXmlProperty(localName = "language")
  private String language;

  /**
   * A placeholder for custom elements that extend the meta-metadata information. This allows for
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