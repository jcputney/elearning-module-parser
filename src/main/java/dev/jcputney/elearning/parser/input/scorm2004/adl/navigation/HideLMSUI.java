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

package dev.jcputney.elearning.parser.input.scorm2004.adl.navigation;

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Feature;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Enum representing elements of the LMS user interface that can be hidden. This enables or disables
 * specific LMS features in line with SCORM navigation requirements. The following schema snippet
 * defines the possible values for this type:
 * <pre>{@code
 *   <xs:simpleType name = "hideLMSUIType">
 *      <xs:restriction base = "xs:token">
 *         <xs:enumeration value = "abandon"/>
 *         <xs:enumeration value = "continue"/>
 *         <xs:enumeration value = "exit"/>
 *         <xs:enumeration value = "previous"/>
 *         <xs:enumeration value = "suspendAll"/>
 *         <xs:enumeration value = "exitAll"/>
 *         <xs:enumeration value = "abandonAll"/>
 *      </xs:restriction>
 *   </xs:simpleType>
 * }</pre>
 */
@JsonFormat(with = Feature.ACCEPT_CASE_INSENSITIVE_VALUES)
public enum HideLMSUI {

  /**
   * The "abandon" option hides the LMS's "abandon" button or action.
   */
  @JsonProperty("abandon")
  ABANDON,

  /**
   * The "continue" option hides the LMS's "continue" button or action.
   */
  @JsonProperty("continue")
  CONTINUE,

  /**
   * The "exit" option hides the LMS's "exit" button or action.
   */
  @JsonProperty("exit")
  EXIT,

  /**
   * The "previous" option hides the LMS's "previous" button or action.
   */
  @JsonProperty("previous")
  PREVIOUS,

  /**
   * The "suspendAll" option hides the LMS's "suspend all" feature.
   */
  @JsonProperty("suspendAll")
  SUSPEND_ALL,

  /**
   * The "exitAll" option hides the LMS's "exit all" feature.
   */
  @JsonProperty("exitAll")
  EXIT_ALL,

  /**
   * The "abandonAll" option hides the LMS's "abandon all" feature.
   */
  @JsonProperty("abandonAll")
  ABANDON_ALL,

  /**
   * The "unknown" option indicates that the LMS UI element is not specified or recognized.
   */
  @JsonEnumDefaultValue
  UNKNOWN
}
