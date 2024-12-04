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

package dev.jcputney.elearning.parser.input.scorm2004.ims.ss.types;

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Feature;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

/**
 * Enum representing the possible ways to combine multiple conditions within a rollup or sequencing
 * rule.
 * <p>
 * This attribute specifies whether all specified conditions must be met ("all") or if meeting any
 * single condition is sufficient ("any").
 * </p>
 * The following schema snippet defines the possible values:
 * <pre>{@code
 *  <xs:simpleType name = "conditionCombinationType">
 * 		<xs:restriction base = "xs:token">
 * 			<xs:enumeration value = "all"/>
 * 			<xs:enumeration value = "any"/>
 * 		</xs:restriction>
 * 	</xs:simpleType>
 * }</pre>
 */
@JsonFormat(with = Feature.ACCEPT_CASE_INSENSITIVE_VALUES)
public enum ConditionCombinationType {
  /**
   * The "all" value specifies that all conditions must be met for the rule to be satisfied.
   */
  @JsonProperty("all")
  ALL,

  /**
   * The "any" value specifies that any condition may be met for the rule to be satisfied.
   */
  @JsonProperty("any")
  ANY,

  @JsonEnumDefaultValue
  UNKNOWN
}