/*
 * Copyright (c) 2024-2026 Jonathan Putney
 *
 * Licensed under the Apache License, Version 2.0 (the "License").
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at the project root LICENSE file
 * or at http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0
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

  /**
   * The "unknown" value specifies that the structure of the learning object is unknown.
   */
  @JsonEnumDefaultValue
  UNKNOWN
}
