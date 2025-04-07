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
 * <p>The following schema snippet defines the possible values:</p>
 * <pre>{@code
 * <xs:simpleType name="yesNoType">
 * 		<xs:restriction base="xs:string">
 * 			<xs:enumeration value="yes" />
 * 			<xs:enumeration value="no" />
 * 			<xs:enumeration value="true" />
 * 			<xs:enumeration value="false" />
 * 		</xs:restriction>
 * 	</xs:simpleType>
 * }</pre>
 */
@JsonFormat(with = Feature.ACCEPT_CASE_INSENSITIVE_VALUES)
public enum YesNoType {
  /**
   * The value is yes.
   */
  @JsonProperty("yes")
  YES,

  /**
   * The value is no.
   */
  @JsonProperty("no")
  NO,

  /**
   * The value is true.
   */
  @JsonProperty("true")
  TRUE,

  /**
   * The value is false.
   */
  @JsonProperty("false")
  FALSE
}
