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
 * Enum representing the possible values for <code>interactivityLevel</code> in a LOM element,
 * specifying the interactivity level of the learning resource. The following schema snippet defines
 * the possible values:
 * <pre>{@code
 *  <xs:simpleType name = "interactivityLevelType">
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
public enum InteractivityLevel {
  /**
   * The interactivity level is very low.
   */
  @JsonProperty("very low")
  VERY_LOW,

  /**
   * The interactivity level is low.
   */
  @JsonProperty("low")
  LOW,

  /**
   * The interactivity level is medium.
   */
  @JsonProperty("medium")
  MEDIUM,

  /**
   * The interactivity level is high.
   */
  @JsonProperty("high")
  HIGH,

  /**
   * The interactivity level is very high.
   */
  @JsonProperty("very high")
  VERY_HIGH,

  /**
   * The interactivity level is unknown.
   */
  @JsonEnumDefaultValue
  UNKNOWN
}
