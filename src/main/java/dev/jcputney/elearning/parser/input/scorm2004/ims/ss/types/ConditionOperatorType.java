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
 * Enum representing the possible operators for evaluating a rollup condition. These operators
 * define how the rollup condition is applied during evaluation. The following schema snippet
 * defines the possible values:
 * <pre>{@code
 *  <xs:simpleType name = "conditionOperatorType">
 * 		<xs:restriction base = "xs:token">
 * 			<xs:enumeration value = "not"/>
 * 			<xs:enumeration value = "noOp"/>
 * 		</xs:restriction>
 * 	</xs:simpleType>
 * }</pre>
 */
@JsonFormat(with = Feature.ACCEPT_CASE_INSENSITIVE_VALUES)
public enum ConditionOperatorType {
  /**
   * The "not" operator negates the result of the condition.
   */
  @JsonProperty("not")
  NOT,

  /**
   * The "noOp" operator does not modify the result of the condition.
   */
  @JsonProperty("noOp")
  NO_OP,

  /**
   * The "unknown" operator is used when the condition operator type is not specified or is not
   * recognized.
   */
  @JsonEnumDefaultValue
  UNKNOWN
}
