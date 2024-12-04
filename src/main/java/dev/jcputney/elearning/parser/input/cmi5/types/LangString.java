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

package dev.jcputney.elearning.parser.input.cmi5.types;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlText;
import dev.jcputney.elearning.parser.input.common.TrimAndPreserveIndentationDeserializer;
import lombok.Data;

/**
 * Represents a single localized string with an optional language attribute.
 *
 * <p>Defined in the schema as:</p>
 *
 * <pre>{@code
 * <xs:element name="langstring" maxOccurs="unbounded" minOccurs="1">
 *   <xs:complexType>
 *     <xs:simpleContent>
 *       <xs:extension base="xs:string">
 *         <xs:attribute name="lang" type="xs:language"/>
 *         <xs:attributeGroup ref="anyAttribute"/>
 *       </xs:extension>
 *     </xs:simpleContent>
 *   </xs:complexType>
 * }</pre>
 */
@Data
@JsonFormat(with = JsonFormat.Feature.ACCEPT_CASE_INSENSITIVE_PROPERTIES)
public class LangString {

  /**
   * The actual text content of the localized string.
   *
   * <pre>{@code
   * <xs:extension base="xs:string">
   *   <xs:attribute name="lang" type="xs:language"/>
   *   <xs:attributeGroup ref="anyAttribute"/>
   * </xs:extension>
   * }</pre>
   */
  @JacksonXmlText
  @JsonDeserialize(using = TrimAndPreserveIndentationDeserializer.class)
  private String value;

  /**
   * The language of the string, represented as an optional attribute.
   *
   * <pre>{@code
   * <xs:attribute name="lang" type="xs:language"/>
   * }</pre>
   */
  @JacksonXmlProperty(isAttribute = true)
  @JsonProperty("lang")
  private String lang;
}
