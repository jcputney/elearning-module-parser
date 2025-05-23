/*
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
 */

package dev.jcputney.elearning.parser.input.lom.properties;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Feature;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * <p>Enum representing the statusDisplayType simple type.</p>
 *
 * <p>The following schema snippet defines the possible values:</p>
 * <pre>{@code
 * <xs:simpleType name="statusDisplayType">
 *   <xs:restriction base="xs:string">
 *     <xs:enumeration value="success only" />
 *     <xs:enumeration value="completion only" />
 *     <xs:enumeration value="separate" />
 *     <xs:enumeration value="combined" />
 *     <xs:enumeration value="none" />
 *   </xs:restriction>
 * </xs:simpleType>
 * }</pre>
 */
@JsonFormat(with = Feature.ACCEPT_CASE_INSENSITIVE_VALUES)
public enum StatusDisplayType {

  /**
   * The status display type is success only.
   */
  @JsonProperty("success only")
  SUCCESS_ONLY,

  /**
   * The status display type is completion only.
   */
  @JsonProperty("completion only")
  COMPLETION_ONLY,

  /**
   * The status display type is separate.
   */
  @JsonProperty("separate")
  SEPARATE,

  /**
   * The status display type is combined.
   */
  @JsonProperty("combined")
  COMBINED,

  /**
   * The status display type is none.
   */
  @JsonProperty("none")
  NONE
}