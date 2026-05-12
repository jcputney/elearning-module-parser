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
 * Enum for timeLimitAction values. The following schema snippet defines the possible values:
 * <pre>{@code
 *   <xs:simpleType name = "timeLimitActionType">
 *      <xs:restriction base = "xs:string">
 *         <xs:enumeration value = "exit,message"/>
 *         <xs:enumeration value = "exit,no message"/>
 *         <xs:enumeration value = "continue,message"/>
 *         <xs:enumeration value = "continue,no message"/>
 *      </xs:restriction>
 *   </xs:simpleType>
 * }</pre>
 */
@JsonFormat(with = Feature.ACCEPT_CASE_INSENSITIVE_VALUES)
public enum TimeLimitAction {

  /**
   * Exit the activity and display a message.
   */
  @JsonProperty("exit,message")
  EXIT_MESSAGE,

  /**
   * Exit the activity without displaying a message.
   */
  @JsonProperty("exit,no message")
  EXIT_NO_MESSAGE,

  /**
   * Continue the activity and display a message.
   */
  @JsonProperty("continue,message")
  CONTINUE_MESSAGE,

  /**
   * Continue the activity without displaying a message.
   */
  @JsonProperty("continue,no message")
  CONTINUE_NO_MESSAGE,

  /**
   * Unknown value, used for error handling or default cases.
   */
  @JsonEnumDefaultValue
  UNKNOWN
}
