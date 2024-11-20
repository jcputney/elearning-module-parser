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
 * Enum representing the possible values for <code>randomizationTiming</code> and
 * <code>selectionTiming</code>, specifying when randomization and selection of child activities
 * should occur. The following schema snippet defines the possible values for this type:
 * <pre>{@code
 * <xs:simpleType name = "randomTimingType">
 *   <xs:restriction base = "xs:token">
 *     <xs:enumeration value = "never"/>
 *     <xs:enumeration value = "once"/>
 *     <xs:enumeration value = "onEachNewAttempt"/>
 *   </xs:restriction>
 * </xs:simpleType>
 * }</pre>
 */
@Getter
public enum RandomizationTiming {
  /**
   * The "never" value specifies that randomization or selection should never occur.
   */
  @JsonProperty("never")
  NEVER,

  /**
   * The "once" value specifies that randomization or selection should occur once.
   */
  @JsonProperty("once")
  ONCE,

  /**
   * The "onEachNewAttempt" value specifies that randomization or selection should occur on each new
   * attempt.
   */
  @JsonProperty("onEachNewAttempt")
  ON_EACH_NEW_ATTEMPT,

  @JsonEnumDefaultValue
  UNKNOWN
}
