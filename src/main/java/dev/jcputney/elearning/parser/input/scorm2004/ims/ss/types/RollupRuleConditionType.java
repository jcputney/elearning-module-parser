/*
 * Copyright (c) 2024. Jonathan Putney
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

package dev.jcputney.elearning.parser.input.scorm2004.ims.ss.types;

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Feature;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Enum representing the possible conditions that can be evaluated within a rollup rule. Each
 * condition defines a specific criterion related to the learner's progress, completion, or status
 * of an activity, and can influence the rollup actions. The following schema snippet defines the
 * possible values for this type:
 * <pre>{@code
 *  <xs:simpleType name = "rollupRuleConditionType">
 * 		<xs:restriction base = "xs:token">
 * 			<xs:enumeration value = "satisfied"/>
 * 			<xs:enumeration value = "objectiveStatusKnown"/>
 * 			<xs:enumeration value = "objectiveMeasureKnown"/>
 * 			<xs:enumeration value = "completed"/>
 * 			<xs:enumeration value = "activityProgressKnown"/>
 * 			<xs:enumeration value = "attempted"/>
 * 			<xs:enumeration value = "attemptLimitExceeded"/>
 * 			<xs:enumeration value = "timeLimitExceeded"/>
 * 			<xs:enumeration value = "outsideAvailableTimeRange"/>
 * 		</xs:restriction>
 * 	</xs:simpleType>
 * }</pre>
 */
@JsonFormat(with = Feature.ACCEPT_CASE_INSENSITIVE_VALUES)
public enum RollupRuleConditionType {
  /**
   * The condition is met if the activity or objective is marked as satisfied.
   */
  @JsonProperty("satisfied")
  SATISFIED,

  /**
   * The condition is met if the status of the objective is known.
   */
  @JsonProperty("objectiveStatusKnown")
  OBJECTIVE_STATUS_KNOWN,

  /**
   * The condition is met if the measure of the objective is known.
   */
  @JsonProperty("objectiveMeasureKnown")
  OBJECTIVE_MEASURE_KNOWN,

  /**
   * The condition is met if the activity is marked as completed.
   */
  @JsonProperty("completed")
  COMPLETED,

  /**
   * The condition is met if the progress status of the activity is known.
   */
  @JsonProperty("activityProgressKnown")
  ACTIVITY_PROGRESS_KNOWN,

  /**
   * The condition is met if the activity has been attempted.
   */
  @JsonProperty("attempted")
  ATTEMPTED,

  /**
   * The condition is met if the activity's attempt limit has been exceeded.
   */
  @JsonProperty("attemptLimitExceeded")
  ATTEMPT_LIMIT_EXCEEDED,

  /**
   * The condition is met if the activity's time limit has been exceeded.
   */
  @JsonProperty("timeLimitExceeded")
  TIME_LIMIT_EXCEEDED,

  /**
   * The condition is met if the current time falls outside the available time range for the
   * activity.
   */
  @JsonProperty("outsideAvailableTimeRange")
  OUTSIDE_AVAILABLE_TIME_RANGE,

  /**
   * The condition is met if the activity's status is unknown or not specified.
   */
  @JsonEnumDefaultValue
  UNKNOWN
}
