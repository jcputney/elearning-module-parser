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
 * Enum representing the possible values for <code>interactivityLevel</code> in a LOM element,
 * specifying the interactivity level of the learning resource. The following schema snippet defines
 * the possible values:
 * <pre>{@code
 *  <xs:simpleType name = "interactivityLevelType">
 * 		<xs:restriction base = "xs:string">
 * 			<xs:enumeration value = "very low"/>
 * 			<xs:enumeration value = "low"/>
 * 			<xs:enumeration value = "medium"/>
 * 			<xs:enumeration value = "high"/>
 * 			<xs:enumeration value = "very high"/>
 * 		</xs:restriction>
 * 	</xs:simpleType>
 *  }</pre>
 */
@JsonFormat(with = Feature.ACCEPT_CASE_INSENSITIVE_VALUES)
public enum InteractivityLevel {
  /**
   * The interactivity level is very low.
   */
  @JsonProperty("very low")
  VERY_LOW,

  /**
   * The interactivity level is low.
   */
  @JsonProperty("low")
  LOW,

  /**
   * The interactivity level is medium.
   */
  @JsonProperty("medium")
  MEDIUM,

  /**
   * The interactivity level is high.
   */
  @JsonProperty("high")
  HIGH,

  /**
   * The interactivity level is very high.
   */
  @JsonProperty("very high")
  VERY_HIGH,

  /**
   * The interactivity level is unknown.
   */
  @JsonEnumDefaultValue
  UNKNOWN
}
