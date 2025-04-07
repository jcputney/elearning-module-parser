/*
 * qlty-ignore: +qlty:similar-code
 *
 * Copyright (c) 2024-2025. Jonathan Putney
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

package dev.jcputney.elearning.parser.input.lom.types;

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Feature;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * The context to which the learning object applies. The following schema snippet shows the context
 * element:
 * <pre>{@code
 *   <xs:simpleType name="contextValues">
 *      <xs:restriction base="xs:token">
 *         <xs:enumeration value="school"/>
 *         <xs:enumeration value="higherEducation"/>
 *         <xs:enumeration value="training"/>
 *         <xs:enumeration value="other"/>
 *      </xs:restriction>
 *   </xs:simpleType>
 * }</pre>
 */
@JsonFormat(with = Feature.ACCEPT_CASE_INSENSITIVE_VALUES)
public enum Context {
  /**
   * The learning object is intended for use in a school context.
   */
  @JsonProperty("school")
  SCHOOL,

  /**
   * The learning object is intended for use in a higher education context.
   */
  @JsonProperty("higherEducation")
  HIGHER_EDUCATION,

  /**
   * The learning object is intended for use in a training context.
   */
  @JsonProperty("training")
  TRAINING,

  /**
   * The learning object is intended for use in another context.
   */
  @JsonProperty("other")
  OTHER,

  /**
   * The context of the learning object is unknown.
   */
  @JsonEnumDefaultValue
  UNKNOWN
}
