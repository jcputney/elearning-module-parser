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
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Enum representing the possible launch type for the PackageProperties.
 * <p>The following schema snippet defines the possible values:</p>
 * <pre>{@code
 *  <xs:simpleType name="launchType">
 * 		<xs:restriction base="xs:string">
 * 			<xs:enumeration value="frameset" />
 * 			<xs:enumeration value="new window" />
 * 			<xs:enumeration value="new window,after click" />
 * 			<xs:enumeration value="new window without browser toolbar" />
 * 		</xs:restriction>
 * 	</xs:simpleType>
 *  }</pre>
 */
@JsonFormat(with = JsonFormat.Feature.ACCEPT_CASE_INSENSITIVE_VALUES)
public enum LaunchType {
  /**
   * The learning object is launched in a frameset.
   */
  @JsonProperty("frameset")
  FRAMESET,

  /**
   * The learning object is launched in a new window.
   */
  @JsonProperty("new window")
  NEW_WINDOW,

  /**
   * The learning object is launched in a new window after a click.
   */
  @JsonProperty("new window,after click")
  NEW_WINDOW_AFTER_CLICK,

  /**
   * The learning object is launched in a new window without the browser toolbar.
   */
  @JsonProperty("new window without browser toolbar")
  NEW_WINDOW_WITHOUT_BROWSER_TOOLBAR
}
