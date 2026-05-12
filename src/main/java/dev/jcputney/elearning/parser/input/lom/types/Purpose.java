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
 * The purpose of the learning object. The following schema snippet defines the Purpose type:
 * <pre>{@code
 *   <xs:simpleType name="purposeValues">
 *      <xs:restriction base="xs:token">
 *         <xs:enumeration value="discipline"/>
 *         <xs:enumeration value="idea"/>
 *         <xs:enumeration value="prerequisite"/>
 *         <xs:enumeration value="educational objective"/>
 *         <xs:enumeration value="accessibility restrictions"/>
 *         <xs:enumeration value="educational level"/>
 *         <xs:enumeration value="skill level"/>
 *         <xs:enumeration value="security level"/>
 *         <xs:enumeration value="competency"/>
 *      </xs:restriction>
 *   </xs:simpleType>
 * }</pre>
 */
@JsonFormat(with = Feature.ACCEPT_CASE_INSENSITIVE_VALUES)
public enum Purpose {
  /**
   * The purpose of the learning object is to provide a discipline.
   */
  @JsonProperty("discipline")
  DISCIPLINE,

  /**
   * The purpose of the learning object is to provide an idea.
   */
  @JsonProperty("idea")
  IDEA,

  /**
   * The purpose of the learning object is to provide a prerequisite.
   */
  @JsonProperty("prerequisite")
  PREREQUISITE,

  /**
   * The purpose of the learning object is to provide an educational objective.
   */
  @JsonProperty("educational objective")
  EDUCATIONAL_OBJECTIVE,

  /**
   * The purpose of the learning object is to provide accessibility restrictions.
   */
  @JsonProperty("accessibility restrictions")
  ACCESSIBILITY_RESTRICTIONS,

  /**
   * The purpose of the learning object is to provide an educational level.
   */
  @JsonProperty("educational level")
  EDUCATIONAL_LEVEL,

  /**
   * The purpose of the learning object is to provide a skill level.
   */
  @JsonProperty("skill level")
  SKILL_LEVEL,

  /**
   * The purpose of the learning object is to provide a security level.
   */
  @JsonProperty("security level")
  SECURITY_LEVEL,

  /**
   * The purpose of the learning object is to provide a competency.
   */
  @JsonProperty("competency")
  COMPETENCY,

  /**
   * The purpose of the learning object is unknown.
   */
  @JsonEnumDefaultValue
  UNKNOWN
}
