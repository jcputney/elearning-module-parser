/*
 * Copyright (c) 2024-2025. Jonathan Putney
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

package dev.jcputney.elearning.parser.input.scorm2004.ims.ss.sequencing;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import dev.jcputney.elearning.parser.input.scorm2004.IMSSS;
import dev.jcputney.elearning.parser.util.DurationIso8601Deserializer;
import dev.jcputney.elearning.parser.util.InstantDeserializer;
import java.io.Serializable;
import java.time.Duration;
import java.time.Instant;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * Represents the limit conditions for a learning activity within the SCORM IMS Simple Sequencing
 * (IMSSS) schema. Limit conditions define constraints on the learner's interaction with an
 * activity, such as the maximum number of attempts, time limits for activity completion, and
 * specific duration constraints.
 *
 * <p>These limit conditions are used to control the availability and accessibility of a learning
 * activity based on the time spent, the number of attempts made, and specified start and end times
 * for access. They are a key component in defining how learners can progress through SCORM content,
 * especially for time-sensitive or attempt-limited activities.</p>
 *
 * <p>Common use cases include:</p>
 * <ul>
 *   <li>Setting an attempt limit to restrict the number of times the user can access an activity.</li>
 *   <li>Applying time limits to control the duration of an activity attempt.</li>
 *   <li>Defining start and end times to establish an available time window for an activity.</li>
 * </ul>
 *
 * <p>The IMSSS namespace is specified by {@link IMSSS#NAMESPACE_URI}, and this class aligns with
 * SCORM 2004 standards for sequencing and navigation.</p>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonFormat(with = JsonFormat.Feature.ACCEPT_CASE_INSENSITIVE_PROPERTIES)
public class LimitConditions implements Serializable {

  /**
   * The maximum number of attempts allowed for the learner on this activity. When the attempt limit
   * is reached, the learner will no longer be able to access the activity.
   *
   * <p>If not specified, no limit is applied, and the learner may attempt the activity
   * an unlimited number of times.</p>
   */
  @JacksonXmlProperty(isAttribute = true)
  @JsonProperty("attemptLimit")
  private Integer attemptLimit;

  /**
   * The absolute duration limit for an attempt, represented as an ISO 8601 duration (e.g., "PT30M"
   * for 30 minutes). This attribute defines the maximum allowable time for a single attempt on the
   * activity.
   *
   * <p>If exceeded, the attempt may be automatically completed or terminated, depending on the
   * LMS behavior.</p>
   *
   * <p>Represented as a {@link Duration} parsed from an ISO 8601 string.</p>
   */
  @JacksonXmlProperty(isAttribute = true)
  @JsonDeserialize(using = DurationIso8601Deserializer.class)
  @JsonProperty("attemptAbsoluteDurationLimit")
  private Duration attemptAbsoluteDurationLimit;

  /**
   * The experienced duration limit for an attempt, represented as an ISO 8601 duration. This
   * duration measures the time actively spent by the learner on the activity, excluding idle or
   * paused time.
   *
   * <p>If this limit is exceeded, the LMS may mark the attempt as complete or restrict further
   * interaction.</p>
   *
   * <p>Represented as a {@link Duration} parsed from an ISO 8601 string.</p>
   */
  @JacksonXmlProperty(isAttribute = true)
  @JsonDeserialize(using = DurationIso8601Deserializer.class)
  @JsonProperty("attemptExperiencedDurationLimit")
  private Duration attemptExperiencedDurationLimit;

  /**
   * The absolute duration limit for the entire activity, represented as an ISO 8601 duration. This
   * limit defines the maximum allowable time for the activity across all attempts.
   *
   * <p>Once this duration is reached, the activity may be marked as complete, regardless of the
   * attempt status.</p>
   *
   * <p>Represented as a {@link Duration} parsed from an ISO 8601 string.</p>
   */
  @JacksonXmlProperty(isAttribute = true)
  @JsonDeserialize(using = DurationIso8601Deserializer.class)
  @JsonProperty("activityAbsoluteDurationLimit")
  private Duration activityAbsoluteDurationLimit;

  /**
   * The experienced duration limit for the activity, represented as an ISO 8601 duration. This
   * duration measures the active time spent on the activity across all attempts, excluding idle
   * time.
   *
   * <p>If the learner's total active time on the activity exceeds this limit, the LMS may enforce
   * completion or restrict access.</p>
   *
   * <p>Represented as a {@link Duration} parsed from an ISO 8601 string.</p>
   */
  @JacksonXmlProperty(isAttribute = true)
  @JsonDeserialize(using = DurationIso8601Deserializer.class)
  @JsonProperty("activityExperiencedDurationLimit")
  private Duration activityExperiencedDurationLimit;

  /**
   * Specifies a start time for the activity as an ISO 8601 date-time (e.g.,
   * "2024-11-13T09:00:00Z"). This attribute defines the earliest time at which the learner can
   * begin interacting with the activity.
   *
   * <p>If the current time is before this start time, the activity is not accessible to the
   * learner.</p>
   *
   * <p>Represented as an {@link Instant} parsed from an ISO 8601 string.</p>
   */
  @JacksonXmlProperty(isAttribute = true)
  @JsonDeserialize(using = InstantDeserializer.class)
  @JsonProperty("beginTimeLimit")
  private Instant beginTimeLimit;

  /**
   * Specifies an end time for the activity as an ISO 8601 date-time. This attribute defines the
   * latest time at which the learner can interact with the activity.
   *
   * <p>Once the end time is reached, the activity becomes unavailable to the learner, regardless
   * of completion status.</p>
   *
   * <p>Represented as an {@link Instant} parsed from an ISO 8601 string.</p>
   */
  @JacksonXmlProperty(isAttribute = true)
  @JsonDeserialize(using = InstantDeserializer.class)
  @JsonProperty("endTimeLimit")
  private Instant endTimeLimit;

  public LimitConditions(Integer attemptLimit, Duration attemptAbsoluteDurationLimit,
      Duration attemptExperiencedDurationLimit, Duration activityAbsoluteDurationLimit,
      Duration activityExperiencedDurationLimit, Instant beginTimeLimit, Instant endTimeLimit) {
    this.attemptLimit = attemptLimit;
    this.attemptAbsoluteDurationLimit = attemptAbsoluteDurationLimit;
    this.attemptExperiencedDurationLimit = attemptExperiencedDurationLimit;
    this.activityAbsoluteDurationLimit = activityAbsoluteDurationLimit;
    this.activityExperiencedDurationLimit = activityExperiencedDurationLimit;
    this.beginTimeLimit = beginTimeLimit;
    this.endTimeLimit = endTimeLimit;
  }

  public LimitConditions() {
    // no-op
  }

  /**
   * Retrieves the maximum number of attempts allowed.
   *
   * @return the maximum number of attempts as an Integer, or null if not set.
   */
  public Integer getAttemptLimit() {
    return this.attemptLimit;
  }

  /**
   * Sets the maximum number of attempts allowed.
   *
   * @param attemptLimit the maximum number of attempts as an Integer, or null to indicate no
   * limit.
   */
  public void setAttemptLimit(Integer attemptLimit) {
    this.attemptLimit = attemptLimit;
  }

  /**
   * Retrieves the absolute duration limit for an attempt.
   *
   * @return the absolute duration limit for an attempt as a {@link Duration}, or null if not set.
   */
  public Duration getAttemptAbsoluteDurationLimit() {
    return this.attemptAbsoluteDurationLimit;
  }

  /**
   * Sets the absolute duration limit for a single attempt in the activity or process.
   *
   * @param attemptAbsoluteDurationLimit the absolute duration limit for an attempt as a
   * {@link Duration}, or null to indicate no limit is set.
   */
  public void setAttemptAbsoluteDurationLimit(Duration attemptAbsoluteDurationLimit) {
    this.attemptAbsoluteDurationLimit = attemptAbsoluteDurationLimit;
  }

  /**
   * Retrieves the experienced duration limit for an attempt. This represents the maximum time
   * allowed for an attempt that has been experienced or in progress.
   *
   * @return the experienced duration limit of an attempt as a {@link Duration}, or null if not set.
   */
  public Duration getAttemptExperiencedDurationLimit() {
    return this.attemptExperiencedDurationLimit;
  }

  /**
   * Sets the experienced duration limit for an attempt. This represents the maximum time the
   * attempt can actively be worked on, as perceived by the user, before being considered over the
   * limit.
   *
   * @param attemptExperiencedDurationLimit the experienced duration limit for an attempt as a
   * {@link Duration}, or null to indicate that no such limit is set.
   */
  public void setAttemptExperiencedDurationLimit(Duration attemptExperiencedDurationLimit) {
    this.attemptExperiencedDurationLimit = attemptExperiencedDurationLimit;
  }

  /**
   * Retrieves the absolute duration limit for the activity. This duration represents the maximum
   * amount of time that the activity can remain active before exceeding the defined limit.
   *
   * @return the absolute duration limit for the activity as a {@link Duration}, or null if no limit
   * is set.
   */
  public Duration getActivityAbsoluteDurationLimit() {
    return this.activityAbsoluteDurationLimit;
  }

  /**
   * Sets the absolute duration limit for the activity. This duration represents the maximum amount
   * of time that the activity can remain active before exceeding the defined limit.
   *
   * @param activityAbsoluteDurationLimit the absolute duration limit for the activity as a
   * {@link Duration}, or null to indicate no limit is set.
   */
  public void setActivityAbsoluteDurationLimit(Duration activityAbsoluteDurationLimit) {
    this.activityAbsoluteDurationLimit = activityAbsoluteDurationLimit;
  }

  /**
   * Retrieves the experienced duration limit for the activity. This duration represents the maximum
   * allowable active time for the activity, as perceived by the user, before it exceeds the defined
   * limit.
   *
   * @return the experienced duration limit for the activity as a {@link Duration}, or null if no
   * limit is set.
   */
  public Duration getActivityExperiencedDurationLimit() {
    return this.activityExperiencedDurationLimit;
  }

  /**
   * Sets the experienced duration limit for the activity. This represents the maximum allowable
   * active time for the activity, as perceived by the user, before it exceeds the defined limit.
   *
   * @param activityExperiencedDurationLimit the experienced duration limit for the activity as a
   * {@link Duration}, or null to indicate no such limit is set.
   */
  public void setActivityExperiencedDurationLimit(Duration activityExperiencedDurationLimit) {
    this.activityExperiencedDurationLimit = activityExperiencedDurationLimit;
  }

  /**
   * Retrieves the starting time limit for an activity or process.
   *
   * @return the begin time limit as an {@link Instant}, or null if no limit is set.
   */
  public Instant getBeginTimeLimit() {
    return this.beginTimeLimit;
  }

  /**
   * Sets the starting time limit for an activity or process. This specifies the earliest point in
   * time when the activity or process is allowed to begin.
   *
   * @param beginTimeLimit the starting time limit as an {@link Instant}, or null to indicate no
   * limit.
   */
  public void setBeginTimeLimit(Instant beginTimeLimit) {
    this.beginTimeLimit = beginTimeLimit;
  }

  /**
   * Retrieves the ending time limit for an activity or process.
   *
   * @return the end time limit as an {@link Instant}, or null if no limit is set.
   */
  public Instant getEndTimeLimit() {
    return this.endTimeLimit;
  }

  /**
   * Sets the ending time limit for an activity or process. This specifies the latest point in time
   * by which the activity or process must be completed.
   *
   * @param endTimeLimit the ending time limit as an {@link Instant}, or null to indicate no limit.
   */
  public void setEndTimeLimit(Instant endTimeLimit) {
    this.endTimeLimit = endTimeLimit;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (!(o instanceof LimitConditions that)) {
      return false;
    }

    return new EqualsBuilder()
        .append(getAttemptLimit(), that.getAttemptLimit())
        .append(getAttemptAbsoluteDurationLimit(), that.getAttemptAbsoluteDurationLimit())
        .append(getAttemptExperiencedDurationLimit(), that.getAttemptExperiencedDurationLimit())
        .append(getActivityAbsoluteDurationLimit(), that.getActivityAbsoluteDurationLimit())
        .append(getActivityExperiencedDurationLimit(), that.getActivityExperiencedDurationLimit())
        .append(getBeginTimeLimit(), that.getBeginTimeLimit())
        .append(getEndTimeLimit(), that.getEndTimeLimit())
        .isEquals();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder(17, 37)
        .append(getAttemptLimit())
        .append(getAttemptAbsoluteDurationLimit())
        .append(getAttemptExperiencedDurationLimit())
        .append(getActivityAbsoluteDurationLimit())
        .append(getActivityExperiencedDurationLimit())
        .append(getBeginTimeLimit())
        .append(getEndTimeLimit())
        .toHashCode();
  }
}
