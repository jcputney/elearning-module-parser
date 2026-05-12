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
 * <p>Enum representing the scoreRollupType simple type.</p>
 *
 * <p>The following schema snippet defines the possible values:</p>
 * <pre>{@code
 * <xs:simpleType name="scoreRollupType">
 *   <xs:restriction base="xs:string">
 *     <xs:enumeration value="score provided by course" />
 *     <xs:enumeration value="average score of all units" />
 *     <xs:enumeration value="average score of all units with scores" />
 *     <xs:enumeration value="fixed average" />
 *   </xs:restriction>
 * </xs:simpleType>
 * }</pre>
 */
@JsonFormat(with = Feature.ACCEPT_CASE_INSENSITIVE_VALUES)
public enum ScoreRollupType {

  /**
   * The course provides the score rollup type.
   */
  @JsonProperty("score provided by course")
  SCORE_PROVIDED_BY_COURSE,

  /**
   * The score rollup type is the average score of all units.
   */
  @JsonProperty("average score of all units")
  AVERAGE_SCORE_OF_ALL_UNITS,

  /**
   * The score rollup type is the average score of all units with scores.
   */
  @JsonProperty("average score of all units with scores")
  AVERAGE_SCORE_OF_ALL_UNITS_WITH_SCORES,

  /**
   * The score rollup type is a fixed average.
   */
  @JsonProperty("fixed average")
  FIXED_AVERAGE
}