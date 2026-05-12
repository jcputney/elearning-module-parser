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
package dev.jcputney.elearning.parser.input.scorm2004.adl.navigation;

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Feature;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Enum representing elements of the LMS user interface that can be hidden. This enables or disables
 * specific LMS features in line with SCORM navigation requirements. The following schema snippet
 * defines the possible values for this type:
 * <pre>{@code
 *   <xs:simpleType name = "hideLMSUIType">
 *      <xs:restriction base = "xs:token">
 *         <xs:enumeration value = "abandon"/>
 *         <xs:enumeration value = "continue"/>
 *         <xs:enumeration value = "exit"/>
 *         <xs:enumeration value = "previous"/>
 *         <xs:enumeration value = "suspendAll"/>
 *         <xs:enumeration value = "exitAll"/>
 *         <xs:enumeration value = "abandonAll"/>
 *      </xs:restriction>
 *   </xs:simpleType>
 * }</pre>
 */
@JsonFormat(with = Feature.ACCEPT_CASE_INSENSITIVE_VALUES)
public enum HideLMSUI {

  /**
   * The "abandon" option hides the LMS's "abandon" button or action.
   */
  @JsonProperty("abandon")
  ABANDON,

  /**
   * The "continue" option hides the LMS's "continue" button or action.
   */
  @JsonProperty("continue")
  CONTINUE,

  /**
   * The "exit" option hides the LMS's "exit" button or action.
   */
  @JsonProperty("exit")
  EXIT,

  /**
   * The "previous" option hides the LMS's "previous" button or action.
   */
  @JsonProperty("previous")
  PREVIOUS,

  /**
   * The "suspendAll" option hides the LMS's "suspend all" feature.
   */
  @JsonProperty("suspendAll")
  SUSPEND_ALL,

  /**
   * The "exitAll" option hides the LMS's "exit all" feature.
   */
  @JsonProperty("exitAll")
  EXIT_ALL,

  /**
   * The "abandonAll" option hides the LMS's "abandon all" feature.
   */
  @JsonProperty("abandonAll")
  ABANDON_ALL,

  /**
   * The "unknown" option indicates that the LMS UI element is not specified or recognized.
   */
  @JsonEnumDefaultValue
  UNKNOWN
}
