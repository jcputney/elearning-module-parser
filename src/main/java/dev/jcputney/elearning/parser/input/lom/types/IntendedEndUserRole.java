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
 * The role of the end user in the context to which the learning object is intended to be delivered.
 * The following schema snippet shows the intended end user role element:
 * <pre>{@code
 *   <xs:simpleType name="intendedEndUserRoleValues">
 *      <xs:restriction base="xs:token">
 *         <xs:enumeration value="teacher"/>
 *         <xs:enumeration value="author"/>
 *         <xs:enumeration value="learner"/>
 *         <xs:enumeration value="manager"/>
 *      </xs:restriction>
 *   </xs:simpleType>
 * }</pre>
 */
@JsonFormat(with = Feature.ACCEPT_CASE_INSENSITIVE_VALUES)
public enum IntendedEndUserRole {
  /**
   * The intended end user role is a teacher.
   */
  @JsonProperty("teacher")
  TEACHER,

  /**
   * The intended end user role is an author.
   */
  @JsonProperty("author")
  AUTHOR,

  /**
   * The intended end user role is a learner.
   */
  @JsonProperty("learner")
  LEARNER,

  /**
   * The intended end user role is a manager.
   */
  @JsonProperty("manager")
  MANAGER,

  /**
   * The intended end user role is unknown.
   */
  @JsonEnumDefaultValue
  UNKNOWN
}
