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
 * The purpose of the learning object. The following schema snippet defines the Purpose type:
 * <pre>{@code
 *   <xs:simpleType name="purposeValues">
 *      <xs:restriction base="xs:token">
 *         <xs:enumeration value="discipline"/>
 *         <xs:enumeration value="idea"/>
 *         <xs:enumeration value="prerequisite"/>
 *         <xs:enumeration value="educational objective"/>
 *         <xs:enumeration value="accessibility restrictions"/>
 *         <xs:enumeration value="educational level"/>
 *         <xs:enumeration value="skill level"/>
 *         <xs:enumeration value="security level"/>
 *         <xs:enumeration value="competency"/>
 *      </xs:restriction>
 *   </xs:simpleType>
 * }</pre>
 */
public enum Purpose {
  @JsonProperty("discipline")
  DISCIPLINE,

  @JsonProperty("idea")
  IDEA,

  @JsonProperty("prerequisite")
  PREREQUISITE,

  @JsonProperty("educational objective")
  EDUCATIONAL_OBJECTIVE,

  @JsonProperty("accessibility restrictions")
  ACCESSIBILITY_RESTRICTIONS,

  @JsonProperty("educational level")
  EDUCATIONAL_LEVEL,

  @JsonProperty("skill level")
  SKILL_LEVEL,

  @JsonProperty("security level")
  SECURITY_LEVEL,

  @JsonProperty("competency")
  COMPETENCY,

  @JsonEnumDefaultValue
  UNKNOWN
}
