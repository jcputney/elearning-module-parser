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
 * Enum representing the possible values for <code>randomizationTiming</code> and
 * <code>selectionTiming</code>, specifying when randomization and selection of child activities
 * should occur. The following schema snippet defines the possible values for this type:
 * <pre>{@code
 * <xs:simpleType name = "randomTimingType">
 *   <xs:restriction base = "xs:token">
 *     <xs:enumeration value = "never"/>
 *     <xs:enumeration value = "once"/>
 *     <xs:enumeration value = "onEachNewAttempt"/>
 *   </xs:restriction>
 * </xs:simpleType>
 * }</pre>
 */
@JsonFormat(with = Feature.ACCEPT_CASE_INSENSITIVE_VALUES)
public enum RandomizationTiming {
  /**
   * The "never" value specifies that randomization or selection should never occur.
   */
  @JsonProperty("never")
  NEVER,

  /**
   * The "once" value specifies that randomization or selection should occur once.
   */
  @JsonProperty("once")
  ONCE,

  /**
   * The "onEachNewAttempt" value specifies that randomization or selection should occur on each new
   * attempt.
   */
  @JsonProperty("onEachNewAttempt")
  ON_EACH_NEW_ATTEMPT,

  /**
   * The "unknown" value is used when the randomization timing type is not specified or is not
   * recognized.
   */
  @JsonEnumDefaultValue
  UNKNOWN
}
