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
package dev.jcputney.elearning.parser.input.scorm2004.ims.ss.types;

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Feature;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Enum representing the possible ways to combine multiple conditions within a rollup or sequencing
 * rule.
 * <p>
 * This attribute specifies whether all specified conditions must be met ("all") or if meeting any
 * single condition is sufficient ("any").
 * </p>
 * The following schema snippet defines the possible values:
 * <pre>{@code
 *  <xs:simpleType name = "conditionCombinationType">
 * 		<xs:restriction base = "xs:token">
 * 			<xs:enumeration value = "all"/>
 * 			<xs:enumeration value = "any"/>
 * 		</xs:restriction>
 * 	</xs:simpleType>
 * }</pre>
 */
@JsonFormat(with = Feature.ACCEPT_CASE_INSENSITIVE_VALUES)
public enum ConditionCombinationType {
  /**
   * The "all" value specifies that all conditions must be met for the rule to be satisfied.
   */
  @JsonProperty("all")
  ALL,

  /**
   * The "any" value specifies that any condition may be met for the rule to be satisfied.
   */
  @JsonProperty("any")
  ANY,

  /**
   * The "unknown" value is used when the condition combination type is not specified or is not
   * recognized.
   */
  @JsonEnumDefaultValue
  UNKNOWN
}