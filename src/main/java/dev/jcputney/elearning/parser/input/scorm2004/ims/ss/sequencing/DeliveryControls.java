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
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import dev.jcputney.elearning.parser.input.scorm2004.IMSSS;
import java.io.Serializable;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * Represents the delivery controls for a learning activity within the SCORM IMS Simple Sequencing
 * (IMSSS) schema. Delivery controls define the settings that influence how an activity's completion
 * and objective statuses are managed by the content itself, providing options for controlling
 * tracking and assessment behaviors.
 *
 * <p>Delivery controls are primarily used to configure how the activity interacts with the
 * learner's progress and achievement, allowing the activity to automatically set completion and
 * objective statuses based on predefined rules or on data provided by the content package.</p>
 *
 * <p>Key configurations include:</p>
 * <ul>
 *   <li>Tracking the activity –- determines if learner progress should be monitored.</li>
 *   <li>Completion set by content –- allows the content to control whether the activity is marked as complete.</li>
 *   <li>Objective set by content –- enables the content to set whether objectives are satisfied.</li>
 * </ul>
 *
 * <p>The IMSSS namespace is specified by {@link IMSSS#NAMESPACE_URI}, and this class aligns with
 * SCORM 2004 standards for sequencing and navigation.</p>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonFormat(with = JsonFormat.Feature.ACCEPT_CASE_INSENSITIVE_PROPERTIES)
public class DeliveryControls implements Serializable {

  /**
   * Specifies whether the learner's progress in this activity should be tracked. When set to
   * <code>true</code>, tracking information is recorded, which may include data on completion,
   * time spent, and other progress metrics.
   *
   * <p>This attribute is typically used to enable or turn off progress tracking at
   * the activity level, depending on whether the activity contributes to the overall progress
   * rollup.</p>
   *
   * <p>Defaults to <code>true</code>.</p>
   */
  @JacksonXmlProperty(isAttribute = true)
  @JsonProperty("tracked")
  private boolean tracked = true;

  /**
   * Indicates whether the content should set the activity's completion status. When set to
   * <code>true</code>, the content is responsible for determining when the activity is marked as
   * complete, based on its internal logic or learner actions.
   *
   * <p>This attribute is useful for activities that require specific criteria to be met
   * for completion, such as finishing a quiz, reading all content, or reaching a target score. The
   * LMS relies on the content's status information to update the completion status.</p>
   *
   * <p>Defaults to <code>false</code>.</p>
   */
  @JacksonXmlProperty(isAttribute = true)
  @JsonProperty("completionSetByContent")
  private boolean completionSetByContent = false;

  /**
   * Specifies whether the content should set the objective satisfaction status for this activity.
   * When set to <code>true</code>, the content determines whether objectives are satisfied based on
   * learner performance or other criteria.
   *
   * <p>This attribute is typically used for activities where the content tracks and reports
   * objective achievements, such as mastery of a topic or passing a test. The LMS uses this data to
   * update the learner’s objective satisfaction status.</p>
   *
   * <p>Defaults to <code>false</code>.</p>
   */
  @JacksonXmlProperty(isAttribute = true)
  @JsonProperty("objectiveSetByContent")
  private boolean objectiveSetByContent = false;

  public DeliveryControls() {
    // no-op
  }

  public boolean isTracked() {
    return this.tracked;
  }

  public void setTracked(boolean tracked) {
    this.tracked = tracked;
  }

  public boolean isCompletionSetByContent() {
    return this.completionSetByContent;
  }

  public void setCompletionSetByContent(boolean completionSetByContent) {
    this.completionSetByContent = completionSetByContent;
  }

  public boolean isObjectiveSetByContent() {
    return this.objectiveSetByContent;
  }

  public void setObjectiveSetByContent(boolean objectiveSetByContent) {
    this.objectiveSetByContent = objectiveSetByContent;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (!(o instanceof DeliveryControls that)) {
      return false;
    }

    return new EqualsBuilder()
        .append(isTracked(), that.isTracked())
        .append(isCompletionSetByContent(), that.isCompletionSetByContent())
        .append(isObjectiveSetByContent(), that.isObjectiveSetByContent())
        .isEquals();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder(17, 37)
        .append(isTracked())
        .append(isCompletionSetByContent())
        .append(isObjectiveSetByContent())
        .toHashCode();
  }
}
