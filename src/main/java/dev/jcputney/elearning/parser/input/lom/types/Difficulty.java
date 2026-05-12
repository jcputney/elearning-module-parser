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
 * The difficulty of the learning object. The following schema snippet shows the difficulty
 * element:
 * <pre>{@code
 *   <xs:simpleType name="difficultyValues">
 *      <xs:restriction base="xs:token">
 *         <xs:enumeration value="very easy"/>
 *         <xs:enumeration value="easy"/>
 *         <xs:enumeration value="medium"/>
 *         <xs:enumeration value="difficult"/>
 *         <xs:enumeration value="very difficult"/>
 *      </xs:restriction>
 *   </xs:simpleType>
 * }</pre>
 */
@JsonFormat(with = Feature.ACCEPT_CASE_INSENSITIVE_VALUES)
public enum Difficulty {
  /**
   * The learning object is very easy.
   */
  @JsonProperty("very easy")
  VERY_EASY,

  /**
   * The learning object is "easy".
   */
  @JsonProperty("easy")
  EASY,

  /**
   * The learning object is of medium difficulty.
   */
  @JsonProperty("medium")
  MEDIUM,

  /**
   * The learning object is "difficult".
   */
  @JsonProperty("difficult")
  DIFFICULT,

  /**
   * The learning object is very difficult.
   */
  @JsonProperty("very difficult")
  VERY_DIFFICULT,

  /**
   * The difficulty of the learning object is unknown.
   */
  @JsonEnumDefaultValue
  UNKNOWN
}
