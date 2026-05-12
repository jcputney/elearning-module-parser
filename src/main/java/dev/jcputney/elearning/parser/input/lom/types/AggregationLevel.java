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
 * Enum representing the possible values for <code>aggregationLevel</code> in a LOM element,
 * specifying the aggregation level of the learning object. The following schema snippet defines the
 * possible values:
 * <pre>{@code
 *   <xs:simpleType name="aggregationLevelValues">
 *      <xs:restriction base="xs:token">
 *         <xs:enumeration value="1"/>
 *         <xs:enumeration value="2"/>
 *         <xs:enumeration value="3"/>
 *         <xs:enumeration value="4"/>
 *      </xs:restriction>
 *   </xs:simpleType>
 * }</pre>
 */
@JsonFormat(with = Feature.ACCEPT_CASE_INSENSITIVE_VALUES)
public enum AggregationLevel {
  /**
   * The aggregation level is 1.
   */
  @JsonProperty("1")
  LEVEL_1,

  /**
   * The aggregation level is 2.
   */
  @JsonProperty("2")
  LEVEL_2,

  /**
   * The aggregation level is 3.
   */
  @JsonProperty("3")
  LEVEL_3,

  /**
   * The aggregation level is 4.
   */
  @JsonProperty("4")
  LEVEL_4,

  /**
   * The aggregation level is unknown.
   */
  @JsonEnumDefaultValue
  UNKNOWN

}
