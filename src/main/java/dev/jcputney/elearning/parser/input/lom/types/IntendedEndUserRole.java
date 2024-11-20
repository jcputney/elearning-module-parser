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
 * The role of the end user in the context to which the learning object is intended to be delivered.
 * The following schema snippet shows the intended end user role element:
 * <pre>{@code
 *   <xs:simpleType name="intendedEndUserRoleValues">
 *      <xs:restriction base="xs:token">
 *         <xs:enumeration value="teacher"/>
 *         <xs:enumeration value="author"/>
 *         <xs:enumeration value="learner"/>
 *         <xs:enumeration value="manager"/>
 *      </xs:restriction>
 *   </xs:simpleType>
 * }</pre>
 */
public enum IntendedEndUserRole {
  @JsonProperty("teacher")
  TEACHER,

  @JsonProperty("author")
  AUTHOR,

  @JsonProperty("learner")
  LEARNER,

  @JsonProperty("manager")
  MANAGER,

  @JsonEnumDefaultValue
  UNKNOWN
}
