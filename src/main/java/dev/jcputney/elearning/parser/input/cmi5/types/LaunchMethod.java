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

package dev.jcputney.elearning.parser.input.cmi5.types;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Feature;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Enum representing the possible values for <code>launchMethod</code> in a CMI5 element, specifying
 * the method used to launch the activity.
 *
 * <p>Schema snippet:
 * <pre>{@code
 * <xs:simpleType name="launchMethodValues">
 *    <xs:restriction base="xs:token">
 *       <xs:enumeration value="AnyWindow"/>
 *       <xs:enumeration value="NewWindow"/>
 *    </xs:restriction>
 * </xs:simpleType>
 * }</pre>
 */
@JsonFormat(with = Feature.ACCEPT_CASE_INSENSITIVE_VALUES)
public enum LaunchMethod {

  /**
   * The activity can be launched in any window.
   */
  @JsonProperty("AnyWindow")
  ANY_WINDOW,

  /**
   * The activity must be launched in a new window.
   */
  @JsonProperty("OwnWindow")
  OWN_WINDOW
}
