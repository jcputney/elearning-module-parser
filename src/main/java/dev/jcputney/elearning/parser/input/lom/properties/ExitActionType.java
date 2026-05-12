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
 * <p>Enum representing the exitActionType simple type.</p>
 *
 * <p>The following schema snippet defines the possible values:</p>
 * <pre>{@code
 * <xs:simpleType name="exitActionType">
 *   <xs:restriction base="xs:string">
 *     <xs:enumeration value="exit,no confirmation" />
 *     <xs:enumeration value="exit,confirmation" />
 *     <xs:enumeration value="continue" />
 *     <xs:enumeration value="message page" />
 *     <xs:enumeration value="do nothing" />
 *   </xs:restriction>
 * </xs:simpleType>
 * }</pre>
 */
@JsonFormat(with = Feature.ACCEPT_CASE_INSENSITIVE_VALUES)
public enum ExitActionType {

  /**
   * The exit action is to exit without confirmation.
   */
  @JsonProperty("exit,no confirmation")
  EXIT_NO_CONFIRMATION,

  /**
   * The exit action is to exit with confirmation.
   */
  @JsonProperty("exit,confirmation")
  EXIT_CONFIRMATION,

  /**
   * The exit action is to continue.
   */
  @JsonProperty("continue")
  CONTINUE,

  /**
   * The exit action is to display a message page.
   */
  @JsonProperty("message page")
  MESSAGE_PAGE,

  /**
   * The exit action is to do nothing.
   */
  @JsonProperty("do nothing")
  DO_NOTHING
}