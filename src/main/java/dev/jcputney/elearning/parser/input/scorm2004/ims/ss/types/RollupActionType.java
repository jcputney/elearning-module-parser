/*
 * qlty-ignore: +qlty:similar-code
 *
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
 *
 * qlty-ignore: -qlty:similar-code
 */

package dev.jcputney.elearning.parser.input.scorm2004.ims.ss.types;

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Feature;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Enum representing the possible values for <code>action</code> in a rollup rule, specifying the
 * effect on the activity’s status when the rule’s conditions are met. The following schema snippet
 * defines the possible values:
 * <pre>{@code
 *  <xs:simpleType name = "rollupActionType">
 * 		<xs:restriction base = "xs:token">
 * 			<xs:enumeration value = "satisfied"/>
 * 			<xs:enumeration value = "notSatisfied"/>
 * 			<xs:enumeration value = "completed"/>
 * 			<xs:enumeration value = "incomplete"/>
 * 		</xs:restriction>
 * 	</xs:simpleType>
 *  }</pre>
 */
@JsonFormat(with = Feature.ACCEPT_CASE_INSENSITIVE_VALUES)
public enum RollupActionType {
  /**
   * The "satisfied" value specifies that the activity is considered satisfied when the rule's
   * conditions are met.
   */
  @JsonProperty("satisfied")
  SATISFIED,

  /**
   * The "notSatisfied" value specifies that the activity is considered not satisfied when the
   * rule's conditions are met.
   */
  @JsonProperty("notSatisfied")
  NOT_SATISFIED,

  /**
   * The "completed" value specifies that the activity is considered completed when the rule's
   * conditions are met.
   */
  @JsonProperty("completed")
  COMPLETED,

  /**
   * The "incomplete" value specifies that the activity is considered incomplete when the rule's
   * conditions are met.
   */
  @JsonProperty("incomplete")
  INCOMPLETE,

  /**
   * The "unknown" value is used when the rollup action type is not specified or is not recognized.
   */
  @JsonEnumDefaultValue
  UNKNOWN
}
