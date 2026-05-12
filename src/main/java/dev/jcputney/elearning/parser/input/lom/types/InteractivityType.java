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
 * Enum representing the possible values for <code>interactivityType</code> in a lom element,
 * specifying the degree of interactivity of the learning resource. The following schema snippet
 * defines the possible values:
 * <pre>{@code
 *   <xs:simpleType name="interactivityTypeValues">
 *      <xs:restriction base="xs:token">
 *         <xs:enumeration value="active"/>
 *         <xs:enumeration value="expositive"/>
 *         <xs:enumeration value="mixed"/>
 *      </xs:restriction>
 *   </xs:simpleType>
 * }</pre>
 */
@JsonFormat(with = Feature.ACCEPT_CASE_INSENSITIVE_VALUES)
public enum InteractivityType {
  /**
   * The learning resource is interactive and requires user input.
   */
  @JsonProperty("active")
  ACTIVE,

  /**
   * The learning resource is expositive and doesn't require user input.
   */
  @JsonProperty("expositive")
  EXPOSITIVE,

  /**
   * The learning resource is a mix of both active and expositive types.
   */
  @JsonProperty("mixed")
  MIXED,

  /**
   * The interactivity type of the learning resource is unknown.
   */
  @JsonEnumDefaultValue
  UNKNOWN
}
