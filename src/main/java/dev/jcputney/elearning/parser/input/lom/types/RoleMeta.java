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
 * Enum representing the possible values for <code>role</code> in a <code>contribute</code> element,
 * specifying the role of the entity contributing to the resource. The following schema snippet
 * defines the possible values:
 * <pre>{@code
 *  <xs:simpleType name = "roleMeta">
 * 		<xs:restriction base = "xs:token">
 * 			<xs:enumeration value = "creator"/>
 * 			<xs:enumeration value = "validator"/>
 * 		</xs:restriction>
 * 	</xs:simpleType>
 *  }</pre>
 */
@JsonFormat(with = Feature.ACCEPT_CASE_INSENSITIVE_VALUES)
public enum RoleMeta {
  /**
   * The "creator" value specifies that the entity is the creator of the resource.
   */
  @JsonProperty("creator")
  CREATOR,

  /**
   * The "validator" value specifies that the entity is the validator of the resource.
   */
  @JsonProperty("validator")
  VALIDATOR,

  /**
   * The "unknown" value specifies that the role of the entity is unknown.
   */
  @JsonEnumDefaultValue
  UNKNOWN
}
