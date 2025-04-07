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

package dev.jcputney.elearning.parser.input.lom.properties;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Feature;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * <p>Enum representing the completionStatusType simple type.</p>
 *
 * <p>The following schema snippet defines the possible values:</p>
 * <pre>{@code
 * <xs:simpleType name="completionStatusType">
 *   <xs:restriction base="xs:string">
 *     <xs:enumeration value="completed" />
 *     <xs:enumeration value="incomplete" />
 *     <xs:enumeration value="browsed" />
 *     <xs:enumeration value="not attempted" />
 *     <xs:enumeration value="unknown" />
 *   </xs:restriction>
 * </xs:simpleType>
 * }</pre>
 */
@JsonFormat(with = Feature.ACCEPT_CASE_INSENSITIVE_VALUES)
public enum CompletionStatusType {

  /**
   * The "completed" value indicates that the learning object has been completed.
   */
  @JsonProperty("completed")
  COMPLETED,

  /**
   * The "incomplete" value indicates that the learning object has not been fully completed.
   */
  @JsonProperty("incomplete")
  INCOMPLETE,

  /**
   * The "browsed" value indicates that the learning object has been browsed but not necessarily
   * completed.
   */
  @JsonProperty("browsed")
  BROWSED,

  /**
   * The "not attempted" value indicates that the learning object has not been attempted.
   */
  @JsonProperty("not attempted")
  NOT_ATTEMPTED,

  /**
   * The "unknown" value indicates that the completion status is unknown.
   */
  @JsonProperty("unknown")
  UNKNOWN
}
