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
 * <p>Enum representing the invalidMenuItemActionType simple type.</p>
 *
 * <p>The following schema snippet defines the possible values:</p>
 * <pre>{@code
 * <xs:simpleType name="invalidMenuItemActionType">
 *   <xs:restriction base="xs:string">
 *     <xs:enumeration value="show" />
 *     <xs:enumeration value="hide" />
 *     <xs:enumeration value="disable" />
 *   </xs:restriction>
 * </xs:simpleType>
 * }</pre>
 */
@JsonFormat(with = Feature.ACCEPT_CASE_INSENSITIVE_VALUES)
public enum InvalidMenuItemActionType {

  /**
   * The invalid menu item action type is "show".
   */
  @JsonProperty("show")
  SHOW,

  /**
   * The invalid menu item action type is "hide".
   */
  @JsonProperty("hide")
  HIDE,

  /**
   * The invalid menu item action type is "disable".
   */
  @JsonProperty("disable")
  DISABLE
}