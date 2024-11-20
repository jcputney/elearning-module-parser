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
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

/**
 * Enum representing the possible operators for evaluating a rollup condition. These operators
 * define how the rollup condition is applied during evaluation. The following schema snippet
 * defines the possible values:
 * <pre>{@code
 *  <xs:simpleType name = "conditionOperatorType">
 * 		<xs:restriction base = "xs:token">
 * 			<xs:enumeration value = "not"/>
 * 			<xs:enumeration value = "noOp"/>
 * 		</xs:restriction>
 * 	</xs:simpleType>
 * }</pre>
 */
@Getter
public enum ConditionOperatorType {
  /**
   * The "not" operator negates the result of the condition.
   */
  @JsonProperty("not")
  NOT,

  /**
   * The "noOp" operator does not modify the result of the condition.
   */
  @JsonProperty("noOp")
  NO_OP,

  @JsonEnumDefaultValue
  UNKNOWN
}
