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

package dev.jcputney.elearning.parser.input.cmi5;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import dev.jcputney.elearning.parser.input.cmi5.types.ReferencesObjectives;
import dev.jcputney.elearning.parser.input.cmi5.types.TextType;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

/**
 * Represents a block within a CMI5 course structure. Blocks can contain nested blocks or AUs
 * (Assignable Units) and have associated objectives.
 *
 * <p>Defined in the schema as:</p>
 *
 * <pre>{@code
 * <xs:complexType name="blockType">
 *   <xs:sequence>
 *     <xs:element name="title" type="textType"/>
 *     <xs:element name="description" type="textType"/>
 *     <xs:element name="objectives" type="referencesObjectivesType" minOccurs="0"/>
 *     <xs:choice minOccurs="1" maxOccurs="unbounded">
 *       <xs:element name="au" type="auType"/>
 *       <xs:element name="block" type="blockType"/>
 *     </xs:choice>
 *     <xs:group ref="anyElement"/>
 *   </xs:sequence>
 *   <xs:attributeGroup ref="anyAttribute"/>
 *   <xs:attribute name="id" type="xs:anyURI" use="required"/>
 * </xs:complexType>
 * }</pre>
 */
@Builder
@Getter
@JsonFormat(with = JsonFormat.Feature.ACCEPT_CASE_INSENSITIVE_PROPERTIES)
public class Block {

  /**
   * The title of the block, represented as a localized text type.
   *
   * <pre>{@code
   * <xs:element name="title" type="textType"/>
   * }</pre>
   */
  @JacksonXmlProperty(localName = "title")
  private TextType title;

  /**
   * The description of the block, represented as a localized text type.
   *
   * <pre>{@code
   * <xs:element name="description" type="textType"/>
   * }</pre>
   */
  @JacksonXmlProperty(localName = "description")
  private TextType description;

  /**
   * The objectives for the block, if specified. This references existing objectives defined in the course.
   *
   * <pre>{@code
   * <xs:element name="objectives" type="referencesObjectivesType" minOccurs="0"/>
   * }</pre>
   */
  @JacksonXmlProperty(localName = "objectives")
  private ReferencesObjectives objectives;

  /**
   * Nested assignable units (AUs) within the block.
   *
   * <pre>{@code
   * <xs:element name="au" type="auType"/>
   * }</pre>
   */
  @JacksonXmlElementWrapper(useWrapping = false)
  @JacksonXmlProperty(localName = "au")
  private List<AU> assignableUnits;

  /**
   * Nested blocks within this block, allowing for hierarchical structure.
   *
   * <pre>{@code
   * <xs:element name="block" type="blockType"/>
   * }</pre>
   */
  @JacksonXmlElementWrapper(useWrapping = false)
  @JacksonXmlProperty(localName = "block")
  private List<Block> nestedBlocks;

  /**
   * The unique identifier for the block, represented as an anyURI.
   *
   * <pre>{@code
   * <xs:attribute name="id" type="xs:anyURI" use="required"/>
   * }</pre>
   */
  @JacksonXmlProperty(isAttribute = true)
  @JsonProperty("id")
  private String id;
}
