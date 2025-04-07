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

package dev.jcputney.elearning.parser.input.scorm2004.adl.types;

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Feature;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Enum for rollup consideration types, with allowed values as per the schema. The following schema
 * snippet defines the possible values:
 * <pre>{@code
 *   <xs:simpleType name = "rollupConsiderationType">
 *      <xs:restriction base = "xs:token">
 *         <xs:enumeration value = "always"/>
 *         <xs:enumeration value = "ifAttempted"/>
 *         <xs:enumeration value = "ifNotSkipped"/>
 *         <xs:enumeration value = "ifNotSuspended"/>
 *      </xs:restriction>
 *   </xs:simpleType>
 * }</pre>
 */
@JsonFormat(with = Feature.ACCEPT_CASE_INSENSITIVE_VALUES)
public enum RollupConsiderationType {

  /**
   * Always consider the rollup.
   */
  @JsonProperty("always")
  ALWAYS,

  /**
   * Consider the rollup if it's been attempted.
   */
  @JsonProperty("ifAttempted")
  IF_ATTEMPTED,

  /**
   * Consider the rollup if it hasn't been skipped.
   */
  @JsonProperty("ifNotSkipped")
  IF_NOT_SKIPPED,

  /**
   * Consider the rollup if it hasn't been suspended.
   */
  @JsonProperty("ifNotSuspended")
  IF_NOT_SUSPENDED,

  /**
   * Unknown value, used for deserialization when the value isn't recognized.
   */
  @JsonEnumDefaultValue
  UNKNOWN
}
