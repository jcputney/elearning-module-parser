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

import static lombok.AccessLevel.PRIVATE;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.jackson.Jacksonized;

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
@Builder
@Getter
@Jacksonized
@NoArgsConstructor
@EqualsAndHashCode(doNotUseGetters = true)
@AllArgsConstructor(access = PRIVATE)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonFormat(with = JsonFormat.Feature.ACCEPT_CASE_INSENSITIVE_PROPERTIES)
public class Identifier implements Serializable {

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