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
@JsonFormat(with = Feature.ACCEPT_CASE_INSENSITIVE_VALUES)
public enum Cost {
  /**
   * The learning object has a cost associated with it.
   */
  @JsonProperty("yes")
  YES,

  /**
   * The learning object does not have a cost associated with it.
   */
  @JsonProperty("no")
  NO,

  /**
   * The cost of the learning object is unknown.
   */
  @JsonEnumDefaultValue
  UNKNOWN
}
