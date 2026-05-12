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
package dev.jcputney.elearning.parser.input.lom.properties;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Feature;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * <p>Enum representing the resetRunTimeDataTimingType simple type.</p>
 *
 * <p>The following schema snippet defines the possible values:</p>
 * <pre>{@code
 * <xs:simpleType name="resetRunTimeDataTimingType">
 *   <xs:restriction base="xs:string">
 *     <xs:enumeration value="never" />
 *     <xs:enumeration value="when exit is not suspend" />
 *     <xs:enumeration value="on each new sequencing attempt" />
 *   </xs:restriction>
 * </xs:simpleType>
 * }</pre>
 */
@JsonFormat(with = Feature.ACCEPT_CASE_INSENSITIVE_VALUES)
public enum ResetRunTimeDataTimingType {

  /**
   * The reset run time data timing type is "never".
   */
  @JsonProperty("never")
  NEVER,

  /**
   * The reset run time data timing type is "when exit is not suspend".
   */
  @JsonProperty("when exit is not suspend")
  WHEN_EXIT_IS_NOT_SUSPEND,

  /**
   * The reset run time data timing type is "on each new sequencing attempt".
   */
  @JsonProperty("on each new sequencing attempt")
  ON_EACH_NEW_SEQUENCING_ATTEMPT
}