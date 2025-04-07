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

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Feature;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Enum representing the possible values for <code>status</code> in a LOM element, specifying the
 * status of the learning object. The following schema snippet defines the possible values:
 * <pre>{@code
 *   <xs:simpleType name="statusValues">
 *      <xs:restriction base="xs:token">
 *         <xs:enumeration value="draft"/>
 *         <xs:enumeration value="final"/>
 *         <xs:enumeration value="revised"/>
 *         <xs:enumeration value="unavailable"/>
 *      </xs:restriction>
 *   </xs:simpleType>
 * }</pre>
 */
@JsonFormat(with = Feature.ACCEPT_CASE_INSENSITIVE_VALUES)
public enum Status {
  /**
   * The "draft" value specifies that the learning object is in draft status.
   */
  @JsonProperty("draft")
  DRAFT,

  /**
   * The "final" value specifies that the learning object is in final status.
   */
  @JsonProperty("final")
  FINAL,

  /**
   * The "revised" value specifies that the learning object is in revised status.
   */
  @JsonProperty("revised")
  REVISED,

  /**
   * The "unavailable" value specifies that the learning object is in unavailable status.
   */
  @JsonProperty("unavailable")
  UNAVAILABLE,

  /**
   * The "unknown" value specifies that the status of the learning object is unknown.
   */
  @JsonEnumDefaultValue
  UNKNOWN
}
