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

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Feature;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Enum representing the possible values for <code>type</code> in a <code>technical</code> element,
 * specifying the type of the location. The following schema snippet defines the possible values:
 * <pre>{@code
 *  <xs:simpleType name = "typeType">
 * 		<xs:restriction base = "xs:token">
 * 			<xs:enumeration value = "operating system"/>
 * 			<xs:enumeration value = "browser"/>
 * 		</xs:restriction>
 * 	</xs:simpleType>
 *  }</pre>
 */
@JsonFormat(with = Feature.ACCEPT_CASE_INSENSITIVE_VALUES)
public enum Type {
  /**
   * The "operating system" value specifies that the location is an operating system.
   */
  @JsonProperty("operating system")
  OPERATING_SYSTEM,

  /**
   * The "browser" value specifies that the location is a browser.
   */
  @JsonProperty("browser")
  BROWSER,

  @JsonEnumDefaultValue
  UNKNOWN
}
