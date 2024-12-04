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
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Feature;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * The kind of relation between the learning object and the related resource. The following schema
 * snippet shows the kind element:
 * <pre>{@code
 *   <xs:simpleType name="kindValues">
 *      <xs:restriction base="xs:token">
 *         <xs:enumeration value="ispartof"/>
 *         <xs:enumeration value="haspart"/>
 *         <xs:enumeration value="isversionof"/>
 *         <xs:enumeration value="hasversion"/>
 *         <xs:enumeration value="isformatof"/>
 *         <xs:enumeration value="hasformat"/>
 *         <xs:enumeration value="references"/>
 *         <xs:enumeration value="isreferencedby"/>
 *         <xs:enumeration value="isbasedon"/>
 *         <xs:enumeration value="isbasisfor"/>
 *         <xs:enumeration value="requires"/>
 *         <xs:enumeration value="isrequiredby"/>
 *      </xs:restriction>
 *   </xs:simpleType>
 * }</pre>
 */
@JsonFormat(with = Feature.ACCEPT_CASE_INSENSITIVE_VALUES)
public enum Kind {
  @JsonProperty("ispartof")
  IS_PART_OF,

  @JsonProperty("haspart")
  HAS_PART,

  @JsonProperty("isversionof")
  IS_VERSION_OF,

  @JsonProperty("hasversion")
  HAS_VERSION,

  @JsonProperty("isformatof")
  IS_FORMAT_OF,

  @JsonProperty("hasformat")
  HAS_FORMAT,

  @JsonProperty("references")
  REFERENCES,

  @JsonProperty("isreferencedby")
  IS_REFERENCED_BY,

  @JsonProperty("isbasedon")
  IS_BASED_ON,

  @JsonProperty("isbasisfor")
  IS_BASIS_FOR,

  @JsonProperty("requires")
  REQUIRES,

  @JsonProperty("isrequiredby")
  IS_REQUIRED_BY,

  @JsonEnumDefaultValue
  UNKNOWN
}
