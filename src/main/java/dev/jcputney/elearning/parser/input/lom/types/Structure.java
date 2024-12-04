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
 * Enum representing the possible values for <code>structure</code> in a LOM element, specifying the
 * structure of the learning object. The following schema snippet defines the possible values:
 * <pre>{@code
 *   <xs:simpleType name="structureValues">
 *      <xs:restriction base="xs:token">
 *         <xs:enumeration value="atomic"/>
 *         <xs:enumeration value="collection"/>
 *         <xs:enumeration value="networked"/>
 *         <xs:enumeration value="hierarchical"/>
 *         <xs:enumeration value="linear"/>
 *      </xs:restriction>
 *   </xs:simpleType>
 * }</pre>
 */
@JsonFormat(with = Feature.ACCEPT_CASE_INSENSITIVE_VALUES)
public enum Structure {
  /**
   * The "atomic" value specifies that the learning object is an atomic unit, not divisible into
   * smaller parts.
   */
  @JsonProperty("atomic")
  ATOMIC,

  /**
   * The "collection" value specifies that the learning object is a collection of smaller units,
   * each with its own metadata.
   */
  @JsonProperty("collection")
  COLLECTION,

  /**
   * The "networked" value specifies that the learning object is a networked structure, with
   * multiple nodes and connections between them.
   */
  @JsonProperty("networked")
  NETWORKED,

  /**
   * The "hierarchical" value specifies that the learning object is a hierarchical structure, with
   * parent-child relationships between nodes.
   */
  @JsonProperty("hierarchical")
  HIERARCHICAL,

  /**
   * The "linear" value specifies that the learning object is a linear structure, with a single path
   * through the content.
   */
  @JsonProperty("linear")
  LINEAR,

  @JsonEnumDefaultValue
  UNKNOWN
}
