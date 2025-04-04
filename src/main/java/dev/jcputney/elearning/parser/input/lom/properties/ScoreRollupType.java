/*
 * Copyright (c) 2025. Jonathan Putney
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
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
   * The score rollup type is provided by the course.
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