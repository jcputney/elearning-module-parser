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
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;

/**
 * Represents a source-value pair, commonly used in the LOM schema to describe a value and its
 * associated source. This structure is used for elements such as structure, aggregation level, and
 * other vocabulary-based fields.
 * <p>
 * Schema snippet:
 * <pre>{@code
 * <xs:complexType name="sourceValuePair">
 *   <xs:sequence>
 *     <xs:element name="source" type="CharacterString"/>
 *     <xs:element name="value" type="CharacterString"/>
 *   </xs:sequence>
 *   <xs:attributeGroup ref="ag:sourceValuePair"/>
 * </xs:complexType>
 * }</pre>
 *
 * @param <T> the type of the value, which can be an enumeration or a string
 */
@Builder
@Getter
@Jacksonized
@JsonFormat(with = JsonFormat.Feature.ACCEPT_CASE_INSENSITIVE_PROPERTIES)
public class SourceValuePair<T> {

  /**
   * The source of the value, typically a reference to a controlled vocabulary or schema.
   * <p>
   * Schema snippet:
   * <pre>{@code
   * <xs:element name="source" type="CharacterString"/>
   * }</pre>
   */
  @JsonProperty("source")
  private String source;

  /**
   * The value associated with the source, representing the specific term or definition.
   * <p>
   * Schema snippet:
   * <pre>{@code
   * <xs:element name="value" type="CharacterString"/>
   * }</pre>
   */
  @JsonProperty("value")
  private T value;
}