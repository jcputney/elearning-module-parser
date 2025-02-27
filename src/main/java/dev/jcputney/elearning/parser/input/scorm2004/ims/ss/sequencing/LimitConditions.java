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

package dev.jcputney.elearning.parser.input.scorm2004.ims.ss.sequencing;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import dev.jcputney.elearning.parser.input.scorm2004.IMSSS;
import dev.jcputney.elearning.parser.util.DurationIso8601Deserializer;
import dev.jcputney.elearning.parser.util.InstantDeserializer;
import java.time.Duration;
import java.time.Instant;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;

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
@Builder
@Getter
@Jacksonized
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonFormat(with = JsonFormat.Feature.ACCEPT_CASE_INSENSITIVE_PROPERTIES)
public class LimitConditions {

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
}
