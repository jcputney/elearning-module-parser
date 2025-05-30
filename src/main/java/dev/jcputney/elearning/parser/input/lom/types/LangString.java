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

package dev.jcputney.elearning.parser.input.lom.types;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlText;
import dev.jcputney.elearning.parser.input.common.TrimAndPreserveIndentationDeserializer;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.extern.jackson.Jacksonized;

/**
 * Represents a LangString in LOM metadata, which is a collection of strings with language
 * attributes. This type is used for fields such as title, description, and keyword.
 *
 * <pre>{@code
 * <xs:complexType name="LangString">
 *   <xs:choice minOccurs="0" maxOccurs="unbounded">
 *     <xs:element name="string" type="langString"/>
 *     <xs:group ref="ex:customElements"/>
 *   </xs:choice>
 * </xs:complexType>
 *
 * <xs:complexType name="langString">
 *   <xs:simpleContent>
 *     <xs:extension base="CharacterString">
 *       <xs:attribute name="language" type="LanguageId"/>
 *     </xs:extension>
 *   </xs:simpleContent>
 * </xs:complexType>
 * }</pre>
 */
@Builder
@Jacksonized
@Data
@NoArgsConstructor
@EqualsAndHashCode(doNotUseGetters = true)
@AllArgsConstructor(access = lombok.AccessLevel.PRIVATE)
@JsonFormat(with = JsonFormat.Feature.ACCEPT_CASE_INSENSITIVE_PROPERTIES)
public class LangString implements Serializable {

  /**
   * The language of the string.
   */
  @JacksonXmlProperty(isAttribute = true)
  @JsonProperty("language")
  @JsonAlias("lang")
  private String language;
  /**
   * The actual string value.
   */
  @JacksonXmlText
  @JsonDeserialize(using = TrimAndPreserveIndentationDeserializer.class)
  private String value;
}
