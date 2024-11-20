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

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;

/**
 * Represents a single language string in LOM metadata. This type is used for fields that only
 * require a single string value with a language attribute.
 *
 * <pre>{@code
 * <xs:complexType name="singleLangString">
 *   <xs:sequence>
 *     <xs:element name="string" type="langString"/>
 *   </xs:sequence>
 * </xs:complexType>
 * }</pre>
 */
@Data
public class SingleLangString {
  /**
   * The string value for a given language.
   */
  @JacksonXmlProperty(localName = "string")
  private LangString langString;
}
