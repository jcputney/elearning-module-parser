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
 * <p>Enum representing the statusRollupType simple type.</p>
 *
 * <p>The following schema snippet defines the possible values:</p>
 * <pre>{@code
 * <xs:simpleType name="statusRollupType">
 *   <xs:restriction base="xs:string">
 *     <xs:enumeration value="status provided by course" />
 *     <xs:enumeration value="complete when all units complete" />
 *     <xs:enumeration value="complete when all units satisfactorily complete" />
 *     <xs:enumeration value="complete when threshold score is met" />
 *   </xs:restriction>
 * </xs:simpleType>
 * }</pre>
 */
@JsonFormat(with = Feature.ACCEPT_CASE_INSENSITIVE_VALUES)
public enum StatusRollupType {

  /**
   * The course provides the status rollup type.
   */
  @JsonProperty("status provided by course")
  STATUS_PROVIDED_BY_COURSE,

  /**
   * The status rollup type is complete when all units complete.
   */
  @JsonProperty("complete when all units complete")
  COMPLETE_WHEN_ALL_UNITS_COMPLETE,

  /**
   * The status rollup type is complete when all units satisfactorily complete.
   */
  @JsonProperty("complete when all units satisfactorily complete")
  COMPLETE_WHEN_ALL_UNITS_SATISFACTORILY_COMPLETE,

  /**
   * The status rollup type is complete when the threshold score is met.
   */
  @JsonProperty("complete when threshold score is met")
  COMPLETE_WHEN_THRESHOLD_SCORE_IS_MET
}