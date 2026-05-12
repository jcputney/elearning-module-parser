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
 * Enum representing the possible values for <code>semanticDensity</code> in a LOM element,
 * specifying the semantic density of the learning resource. The following schema snippet defines
 * the possible values:
 * <pre>{@code
 *  <xs:simpleType name = "semanticDensityType">
 * 		<xs:restriction base = "xs:string">
 * 			<xs:enumeration value = "very low"/>
 * 			<xs:enumeration value = "low"/>
 * 			<xs:enumeration value = "medium"/>
 * 			<xs:enumeration value = "high"/>
 * 			<xs:enumeration value = "very high"/>
 * 		</xs:restriction>
 * 	</xs:simpleType>
 *  }</pre>
 */
@JsonFormat(with = Feature.ACCEPT_CASE_INSENSITIVE_VALUES)
public enum SemanticDensity {
  /**
   * The semantic density is very low.
   */
  @JsonProperty("very low")
  VERY_LOW,

  /**
   * The semantic density is low.
   */
  @JsonProperty("low")
  LOW,

  /**
   * The semantic density is medium.
   */
  @JsonProperty("medium")
  MEDIUM,

  /**
   * The semantic density is high.
   */
  @JsonProperty("high")
  HIGH,

  /**
   * The semantic density is very high.
   */
  @JsonProperty("very high")
  VERY_HIGH,

  /**
   * The semantic density is unknown.
   */
  @JsonEnumDefaultValue
  UNKNOWN
}
