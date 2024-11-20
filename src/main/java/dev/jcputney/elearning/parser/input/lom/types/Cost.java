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

package dev.jcputney.elearning.parser.input.lom.types;

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * The cost of using the learning object. The following schema snippet shows the cost element:
 * <pre>{@code
 *   <xs:simpleType name="costValues">
 *      <xs:restriction base="xs:token">
 *         <xs:enumeration value="yes"/>
 *         <xs:enumeration value="no"/>
 *      </xs:restriction>
 *   </xs:simpleType>
 * }</pre>
 */
public enum Cost {
  @JsonProperty("yes")
  YES,

  @JsonProperty("no")
  NO,

  @JsonEnumDefaultValue
  UNKNOWN
}
