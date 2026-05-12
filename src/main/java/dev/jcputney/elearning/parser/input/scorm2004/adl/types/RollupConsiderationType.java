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
package dev.jcputney.elearning.parser.input.scorm2004.adl.types;

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Feature;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Enum for rollup consideration types, with allowed values as per the schema. The following schema
 * snippet defines the possible values:
 * <pre>{@code
 *   <xs:simpleType name = "rollupConsiderationType">
 *      <xs:restriction base = "xs:token">
 *         <xs:enumeration value = "always"/>
 *         <xs:enumeration value = "ifAttempted"/>
 *         <xs:enumeration value = "ifNotSkipped"/>
 *         <xs:enumeration value = "ifNotSuspended"/>
 *      </xs:restriction>
 *   </xs:simpleType>
 * }</pre>
 */
@JsonFormat(with = Feature.ACCEPT_CASE_INSENSITIVE_VALUES)
public enum RollupConsiderationType {

  /**
   * Always consider the rollup.
   */
  @JsonProperty("always")
  ALWAYS,

  /**
   * Consider the rollup if it's been attempted.
   */
  @JsonProperty("ifAttempted")
  IF_ATTEMPTED,

  /**
   * Consider the rollup if it hasn't been skipped.
   */
  @JsonProperty("ifNotSkipped")
  IF_NOT_SKIPPED,

  /**
   * Consider the rollup if it hasn't been suspended.
   */
  @JsonProperty("ifNotSuspended")
  IF_NOT_SUSPENDED,

  /**
   * Unknown value, used for deserialization when the value isn't recognized.
   */
  @JsonEnumDefaultValue
  UNKNOWN
}
