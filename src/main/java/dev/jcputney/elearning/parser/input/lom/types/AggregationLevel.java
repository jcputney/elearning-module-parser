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

package dev.jcputney.elearning.parser.input.lom.types;

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Feature;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Enum representing the possible values for <code>aggregationLevel</code> in a LOM element,
 * specifying the aggregation level of the learning object. The following schema snippet defines the
 * possible values:
 * <pre>{@code
 *   <xs:simpleType name="aggregationLevelValues">
 *      <xs:restriction base="xs:token">
 *         <xs:enumeration value="1"/>
 *         <xs:enumeration value="2"/>
 *         <xs:enumeration value="3"/>
 *         <xs:enumeration value="4"/>
 *      </xs:restriction>
 *   </xs:simpleType>
 * }</pre>
 */
@JsonFormat(with = Feature.ACCEPT_CASE_INSENSITIVE_VALUES)
public enum AggregationLevel {
  /**
   * The aggregation level is 1.
   */
  @JsonProperty("1")
  LEVEL_1,

  /**
   * The aggregation level is 2.
   */
  @JsonProperty("2")
  LEVEL_2,

  /**
   * The aggregation level is 3.
   */
  @JsonProperty("3")
  LEVEL_3,

  /**
   * The aggregation level is 4.
   */
  @JsonProperty("4")
  LEVEL_4,

  /**
   * The aggregation level is unknown.
   */
  @JsonEnumDefaultValue
  UNKNOWN

}
