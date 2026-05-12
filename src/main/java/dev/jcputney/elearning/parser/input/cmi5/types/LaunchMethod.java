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
package dev.jcputney.elearning.parser.input.cmi5.types;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Feature;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Enum representing the possible values for <code>launchMethod</code> in a CMI5 element, specifying
 * the method used to launch the activity.
 *
 * <p>Schema snippet:
 * <pre>{@code
 * <xs:simpleType name="launchMethodValues">
 *    <xs:restriction base="xs:token">
 *       <xs:enumeration value="AnyWindow"/>
 *       <xs:enumeration value="NewWindow"/>
 *    </xs:restriction>
 * </xs:simpleType>
 * }</pre>
 */
@JsonFormat(with = Feature.ACCEPT_CASE_INSENSITIVE_VALUES)
public enum LaunchMethod {

  /**
   * The activity can be launched in any window.
   */
  @JsonProperty("AnyWindow")
  ANY_WINDOW,

  /**
   * The activity must be launched in a new window.
   */
  @JsonProperty("OwnWindow")
  OWN_WINDOW
}
