/*
 * qlty-ignore: +qlty:similar-code
 *
 * Copyright (c) 2025. Jonathan Putney
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

package dev.jcputney.elearning.parser.input.lom.properties;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Feature;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * <p>Enum representing the resetRunTimeDataTimingType simple type.</p>
 *
 * <p>The following schema snippet defines the possible values:</p>
 * <pre>{@code
 * <xs:simpleType name="resetRunTimeDataTimingType">
 *   <xs:restriction base="xs:string">
 *     <xs:enumeration value="never" />
 *     <xs:enumeration value="when exit is not suspend" />
 *     <xs:enumeration value="on each new sequencing attempt" />
 *   </xs:restriction>
 * </xs:simpleType>
 * }</pre>
 */
@JsonFormat(with = Feature.ACCEPT_CASE_INSENSITIVE_VALUES)
public enum ResetRunTimeDataTimingType {

  /**
   * The reset run time data timing type is "never".
   */
  @JsonProperty("never")
  NEVER,

  /**
   * The reset run time data timing type is "when exit is not suspend".
   */
  @JsonProperty("when exit is not suspend")
  WHEN_EXIT_IS_NOT_SUSPEND,

  /**
   * The reset run time data timing type is "on each new sequencing attempt".
   */
  @JsonProperty("on each new sequencing attempt")
  ON_EACH_NEW_SEQUENCING_ATTEMPT
}