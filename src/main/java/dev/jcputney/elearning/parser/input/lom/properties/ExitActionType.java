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
 * <p>Enum representing the exitActionType simple type.</p>
 *
 * <p>The following schema snippet defines the possible values:</p>
 * <pre>{@code
 * <xs:simpleType name="exitActionType">
 *   <xs:restriction base="xs:string">
 *     <xs:enumeration value="exit,no confirmation" />
 *     <xs:enumeration value="exit,confirmation" />
 *     <xs:enumeration value="continue" />
 *     <xs:enumeration value="message page" />
 *     <xs:enumeration value="do nothing" />
 *   </xs:restriction>
 * </xs:simpleType>
 * }</pre>
 */
@JsonFormat(with = Feature.ACCEPT_CASE_INSENSITIVE_VALUES)
public enum ExitActionType {

  @JsonProperty("exit,no confirmation")
  EXIT_NO_CONFIRMATION,

  @JsonProperty("exit,confirmation")
  EXIT_CONFIRMATION,

  @JsonProperty("continue")
  CONTINUE,

  @JsonProperty("message page")
  MESSAGE_PAGE,

  @JsonProperty("do nothing")
  DO_NOTHING
}