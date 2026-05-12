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
 * <p>Enum representing the lookaheadSequencerModeType simple type.</p>
 *
 * <p>The following schema snippet defines the possible values:</p>
 * <pre>{@code
 * <xs:simpleType name="lookaheadSequencerModeType">
 *   <xs:restriction base="xs:string">
 *     <xs:enumeration value="enabled" />
 *     <xs:enumeration value="disabled" />
 *   </xs:restriction>
 * </xs:simpleType>
 * }</pre>
 */
@JsonFormat(with = Feature.ACCEPT_CASE_INSENSITIVE_VALUES)
public enum LookaheadSequencerModeType {

  /**
   * The lookahead sequencer mode is enabled.
   */
  @JsonProperty("enabled")
  ENABLED,

  /**
   * The lookahead sequencer mode is disabled.
   */
  @JsonProperty("disabled")
  DISABLED
}