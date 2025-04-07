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

package dev.jcputney.elearning.parser.input.scorm2004.adl.types;

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Feature;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Enum for timeLimitAction values. The following schema snippet defines the possible values:
 * <pre>{@code
 *   <xs:simpleType name = "timeLimitActionType">
 *      <xs:restriction base = "xs:string">
 *         <xs:enumeration value = "exit,message"/>
 *         <xs:enumeration value = "exit,no message"/>
 *         <xs:enumeration value = "continue,message"/>
 *         <xs:enumeration value = "continue,no message"/>
 *      </xs:restriction>
 *   </xs:simpleType>
 * }</pre>
 */
@JsonFormat(with = Feature.ACCEPT_CASE_INSENSITIVE_VALUES)
public enum TimeLimitAction {

  /**
   * Exit the activity and display a message.
   */
  @JsonProperty("exit,message")
  EXIT_MESSAGE,

  /**
   * Exit the activity without displaying a message.
   */
  @JsonProperty("exit,no message")
  EXIT_NO_MESSAGE,

  /**
   * Continue the activity and display a message.
   */
  @JsonProperty("continue,message")
  CONTINUE_MESSAGE,

  /**
   * Continue the activity without displaying a message.
   */
  @JsonProperty("continue,no message")
  CONTINUE_NO_MESSAGE,

  /**
   * Unknown value, used for error handling or default cases.
   */
  @JsonEnumDefaultValue
  UNKNOWN
}
