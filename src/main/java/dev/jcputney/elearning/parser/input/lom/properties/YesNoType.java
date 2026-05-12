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
 * <p>The following schema snippet defines the possible values:</p>
 * <pre>{@code
 * <xs:simpleType name="yesNoType">
 * 		<xs:restriction base="xs:string">
 * 			<xs:enumeration value="yes" />
 * 			<xs:enumeration value="no" />
 * 			<xs:enumeration value="true" />
 * 			<xs:enumeration value="false" />
 * 		</xs:restriction>
 * 	</xs:simpleType>
 * }</pre>
 */
@JsonFormat(with = Feature.ACCEPT_CASE_INSENSITIVE_VALUES)
public enum YesNoType {
  /**
   * The value is yes.
   */
  @JsonProperty("yes")
  YES,

  /**
   * The value is no.
   */
  @JsonProperty("no")
  NO,

  /**
   * The value is true.
   */
  @JsonProperty("true")
  TRUE,

  /**
   * The value is false.
   */
  @JsonProperty("false")
  FALSE
}
