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
   * The status rollup type is provided by the course.
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