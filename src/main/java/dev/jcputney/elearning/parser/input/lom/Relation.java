/*
 * Copyright (c) 2024-2025. Jonathan Putney
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

import static lombok.AccessLevel.PRIVATE;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import dev.jcputney.elearning.parser.input.lom.types.Kind;
import dev.jcputney.elearning.parser.input.lom.types.Resource;
import dev.jcputney.elearning.parser.input.lom.types.SourceValuePair;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;

/**
 * Represents the relationship information about a learning object in a Learning Object Metadata
 * (LOM) document. Relations describe connections between the current learning object and other
 * resources.
 *
 * <p>Schema snippet:
 * <pre>{@code
 * <complexType name="relation">
 *   <choice minOccurs="0" maxOccurs="unbounded">
 *     <group ref="kind"/>
 *     <group ref="resource"/>
 *     <group ref="ex:customElements"/>
 *   </choice>
 *   <attributeGroup ref="ag:relation"/>
 * </complexType>
 * }</pre>
 */
@Builder
@Getter
@Jacksonized
@AllArgsConstructor(access = PRIVATE)
@JsonFormat(with = JsonFormat.Feature.ACCEPT_CASE_INSENSITIVE_PROPERTIES)
public class Relation {

  /**
   * The kind of relationship, represented as a source-value pair, specifying the type of connection
   * between the current learning object and another resource.
   *
   * <p>Schema snippet:
   * <pre>{@code
   * <complexType name="kind">
   *   <complexContent>
   *     <extension base="kindVocab">
   *       <attributeGroup ref="ag:kind"/>
   *     </extension>
   *   </complexContent>
   * </complexType>
   * }</pre>
   */
  @JacksonXmlProperty(localName = "kind")
  private SourceValuePair<Kind> kind;
  /**
   * The resource information that describes the target of the relationship. A resource can include
   * identifiers and descriptions for the related learning object.
   *
   * <p>Schema snippet:
   * <pre>{@code
   * <complexType name="resource">
   *   <choice minOccurs="0" maxOccurs="unbounded">
   *     <group ref="identifier"/>
   *     <group ref="description"/>
   *     <group ref="ex:customElements"/>
   *   </choice>
   *   <attributeGroup ref="ag:resource"/>
   * </complexType>
   * }</pre>
   */
  @JacksonXmlElementWrapper(localName = "resource", useWrapping = false)
  @JacksonXmlProperty(localName = "resource")
  private List<Resource> resource;

  /**
   * Default constructor for the Relation class.
   */
  @SuppressWarnings("unused")
  public Relation() {
    // Default constructor
  }
}