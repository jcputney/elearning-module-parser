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
 * <p>Enum representing the statusDisplayType simple type.</p>
 *
 * <p>The following schema snippet defines the possible values:</p>
 * <pre>{@code
 * <xs:simpleType name="statusDisplayType">
 *   <xs:restriction base="xs:string">
 *     <xs:enumeration value="success only" />
 *     <xs:enumeration value="completion only" />
 *     <xs:enumeration value="separate" />
 *     <xs:enumeration value="combined" />
 *     <xs:enumeration value="none" />
 *   </xs:restriction>
 * </xs:simpleType>
 * }</pre>
 */
@JsonFormat(with = Feature.ACCEPT_CASE_INSENSITIVE_VALUES)
public enum StatusDisplayType {

  /**
   * The status display type is success only.
   */
  @JsonProperty("success only")
  SUCCESS_ONLY,

  /**
   * The status display type is completion only.
   */
  @JsonProperty("completion only")
  COMPLETION_ONLY,

  /**
   * The status display type is separate.
   */
  @JsonProperty("separate")
  SEPARATE,

  /**
   * The status display type is combined.
   */
  @JsonProperty("combined")
  COMBINED,

  /**
   * The status display type is none.
   */
  @JsonProperty("none")
  NONE
}