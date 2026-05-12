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
 * The context to which the learning object applies. The following schema snippet shows the context
 * element:
 * <pre>{@code
 *   <xs:simpleType name="contextValues">
 *      <xs:restriction base="xs:token">
 *         <xs:enumeration value="school"/>
 *         <xs:enumeration value="higherEducation"/>
 *         <xs:enumeration value="training"/>
 *         <xs:enumeration value="other"/>
 *      </xs:restriction>
 *   </xs:simpleType>
 * }</pre>
 */
@JsonFormat(with = Feature.ACCEPT_CASE_INSENSITIVE_VALUES)
public enum Context {
  /**
   * The learning object is intended for use in a school context.
   */
  @JsonProperty("school")
  SCHOOL,

  /**
   * The learning object is intended for use in a higher education context.
   */
  @JsonProperty("higherEducation")
  HIGHER_EDUCATION,

  /**
   * The learning object is intended for use in a training context.
   */
  @JsonProperty("training")
  TRAINING,

  /**
   * The learning object is intended for use in another context.
   */
  @JsonProperty("other")
  OTHER,

  /**
   * The context of the learning object is unknown.
   */
  @JsonEnumDefaultValue
  UNKNOWN
}
