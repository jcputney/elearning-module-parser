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
 * The difficulty of the learning object. The following schema snippet shows the difficulty
 * element:
 * <pre>{@code
 *   <xs:simpleType name="difficultyValues">
 *      <xs:restriction base="xs:token">
 *         <xs:enumeration value="very easy"/>
 *         <xs:enumeration value="easy"/>
 *         <xs:enumeration value="medium"/>
 *         <xs:enumeration value="difficult"/>
 *         <xs:enumeration value="very difficult"/>
 *      </xs:restriction>
 *   </xs:simpleType>
 * }</pre>
 */
@JsonFormat(with = Feature.ACCEPT_CASE_INSENSITIVE_VALUES)
public enum Difficulty {
  /**
   * The learning object is very easy.
   */
  @JsonProperty("very easy")
  VERY_EASY,

  /**
   * The learning object is easy.
   */
  @JsonProperty("easy")
  EASY,

  /**
   * The learning object is of medium difficulty.
   */
  @JsonProperty("medium")
  MEDIUM,

  /**
   * The learning object is difficult.
   */
  @JsonProperty("difficult")
  DIFFICULT,

  /**
   * The learning object is very difficult.
   */
  @JsonProperty("very difficult")
  VERY_DIFFICULT,

  /**
   * The difficulty of the learning object is unknown.
   */
  @JsonEnumDefaultValue
  UNKNOWN
}
