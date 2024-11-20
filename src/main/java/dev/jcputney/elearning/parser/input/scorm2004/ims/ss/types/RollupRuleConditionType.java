package dev.jcputney.elearning.parser.input.scorm2004.ims.ss.types;

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

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
@Getter
public enum RollupRuleConditionType {
  /**
   * The condition is met if the activity or objective is marked as satisfied.
   */
  @JsonProperty("satisfied")
  SATISFIED,

  /**
   * The condition is met if the status of the "objective" is known.
   */
  @JsonProperty("objectiveStatusKnown")
  OBJECTIVE_STATUS_KNOWN,

  /**
   * The condition is met if the measure of the "objective" is known.
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

  @JsonEnumDefaultValue
  UNKNOWN
}
