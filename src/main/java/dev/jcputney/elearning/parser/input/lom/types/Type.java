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
 * Enum representing the possible values for <code>type</code> in a <code>technical</code> element,
 * specifying the type of the location. The following schema snippet defines the possible values:
 * <pre>{@code
 *  <xs:simpleType name = "typeType">
 * 		<xs:restriction base = "xs:token">
 * 			<xs:enumeration value = "operating system"/>
 * 			<xs:enumeration value = "browser"/>
 * 		</xs:restriction>
 * 	</xs:simpleType>
 *  }</pre>
 */
@JsonFormat(with = Feature.ACCEPT_CASE_INSENSITIVE_VALUES)
public enum Type {
  /**
   * The "operating system" value specifies that the location is an operating system.
   */
  @JsonProperty("operating system")
  OPERATING_SYSTEM,

  /**
   * The "browser" value specifies that the location is a browser.
   */
  @JsonProperty("browser")
  BROWSER,

  /**
   * The "unknown" value specifies that the location type is unknown.
   */
  @JsonEnumDefaultValue
  UNKNOWN
}
