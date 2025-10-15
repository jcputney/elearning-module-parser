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
import java.io.Serializable;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * Represents the control mode settings for a learning activity within the SCORM IMS Simple
 * Sequencing (IMSSS) schema. The control mode determines the navigation options available to the
 * learner, such as whether they are allowed to navigate freely, use flow-based navigation, and
 * revisit previously accessed activities.
 *
 * <p>Each control mode attribute specifies a particular navigation control option, allowing
 * authors to restrict or enable navigation behaviors according to the instructional design of the
 * SCORM package.</p>
 *
 * <p>The available control modes include:</p>
 * <ul>
 *   <li><strong>Choice:</strong> Allows the learner to freely navigate between available activities.</li>
 *   <li><strong>Choice Exit:</strong> Allows the learner to exit from an activity and return to a previously accessed one.</li>
 *   <li><strong>Flow:</strong> Enables linear or sequential navigation through activities.</li>
 *   <li><strong>Forward Only:</strong> Restricts the learner to moving forward through the activities without going back.</li>
 *   <li><strong>Use Current Attempt Objective Info:</strong> Indicates whether the current attempt's objective information is used in sequencing.</li>
 *   <li><strong>Use Current Attempt Progress Info:</strong> Indicates whether the current attempt's progress information is used in sequencing.</li>
 * </ul>
 *
 * <p>These attributes control navigation at the activity level, allowing for flexibility in sequencing logic
 * based on the learner's progress and interactions with the SCORM content.</p>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonFormat(with = JsonFormat.Feature.ACCEPT_CASE_INSENSITIVE_PROPERTIES)
public final class ControlMode implements Serializable {

  /**
   * Indicates whether the learner is allowed to freely navigate between available activities in
   * this sequence. When set to <code>true</code>, the learner can select any activity in the
   * content structure; otherwise, navigation is restricted.
   *
   * <p>Defaults to <code>true</code>.</p>
   */
  @JacksonXmlProperty(isAttribute = true)
  @JsonProperty("choice")
  private boolean choice = true;

  /**
   * Indicates whether the learner is allowed to exit from an activity and return to a previously
   * accessed one. When set to <code>true</code>, the learner can exit and reselect previously
   * visited activities.
   *
   * <p>Defaults to <code>true</code>.</p>
   */
  @JacksonXmlProperty(isAttribute = true)
  @JsonProperty("choiceExit")
  private boolean choiceExit = true;

  /**
   * Enables linear or sequential navigation through activities in the sequence. When set to
   * <code>true</code>, the learner is required to progress sequentially through the activities,
   * typically by selecting "next" options. If set to <code>false</code>, the learner is not
   * restricted to sequential navigation.
   *
   * <p>Defaults to <code>false</code>.</p>
   */
  @JacksonXmlProperty(isAttribute = true)
  @JsonProperty("flow")
  private boolean flow = false;

  /**
   * Restricts the learner to only moving forward through the activities without being allowed to go
   * back. When set to <code>true</code>, the learner cannot revisit previously accessed activities
   * within the same attempt. If set to <code>false</code>, the learner may navigate backward if
   * choice or flow allows.
   *
   * <p>Defaults to <code>false</code>.</p>
   */
  @JacksonXmlProperty(isAttribute = true)
  @JsonProperty("forwardOnly")
  private boolean forwardOnly = false;

  /**
   * Indicates whether the current attempt's objective information is used in sequencing decisions.
   * When set to <code>true</code>, the system uses information about objectives completed within
   * the current attempt to control sequencing behavior.
   *
   * <p>Defaults to <code>true</code>.</p>
   */
  @JacksonXmlProperty(isAttribute = true)
  @JsonProperty("useCurrentAttemptObjectiveInfo")
  private boolean useCurrentAttemptObjectiveInfo = true;

  /**
   * Indicates whether the current attempt's progress information is used in sequencing decisions.
   * When set to <code>true</code>, the system uses information about progress made within the
   * current attempt to control sequencing behavior.
   *
   * <p>Defaults to <code>true</code>.</p>
   */
  @JacksonXmlProperty(isAttribute = true)
  @JsonProperty("useCurrentAttemptProgressInfo")
  private boolean useCurrentAttemptProgressInfo = true;

  public ControlMode(boolean choice, boolean choiceExit, boolean flow, boolean forwardOnly,
      boolean useCurrentAttemptObjectiveInfo, boolean useCurrentAttemptProgressInfo) {
    this.choice = choice;
    this.choiceExit = choiceExit;
    this.flow = flow;
    this.forwardOnly = forwardOnly;
    this.useCurrentAttemptObjectiveInfo = useCurrentAttemptObjectiveInfo;
    this.useCurrentAttemptProgressInfo = useCurrentAttemptProgressInfo;
  }

  public ControlMode() {
    // no-op
  }

  /**
   * Indicates whether the "choice" option is enabled for the current instance of the control mode.
   *
   * @return true if the "choice" option is enabled, false otherwise.
   */
  public boolean isChoice() {
    return this.choice;
  }

  /**
   * Sets the "choice" option for the current instance of the control mode.
   *
   * @param choice a boolean value indicating whether the "choice" option is enabled (true) or
   * disabled (false)
   */
  public void setChoice(boolean choice) {
    this.choice = choice;
  }

  /**
   * Determines whether the "choiceExit" option is enabled for the current instance of the control
   * mode.
   *
   * @return true if the "choiceExit" option is enabled, false otherwise.
   */
  public boolean isChoiceExit() {
    return this.choiceExit;
  }

  /**
   * Sets the "choiceExit" option for the current instance of the control mode.
   *
   * @param choiceExit a boolean value indicating whether the "choiceExit" option is enabled (true)
   * or disabled (false)
   */
  public void setChoiceExit(boolean choiceExit) {
    this.choiceExit = choiceExit;
  }

  /**
   * Determines whether the "flow" option is enabled for the current instance of the control mode.
   *
   * @return true if the "flow" option is enabled, false otherwise.
   */
  public boolean isFlow() {
    return this.flow;
  }

  /**
   * Sets the "flow" option for the current instance of the control mode.
   *
   * @param flow a boolean value indicating whether the "flow" option is enabled (true) or disabled
   * (false)
   */
  public void setFlow(boolean flow) {
    this.flow = flow;
  }

  /**
   * Determines whether the "forwardOnly" option is enabled for the current instance of the control
   * mode.
   *
   * @return true if the "forwardOnly" option is enabled, false otherwise.
   */
  public boolean isForwardOnly() {
    return this.forwardOnly;
  }

  /**
   * Sets the "forwardOnly" option for the current instance, specifying whether navigation through
   * content is restricted to a forward direction only.
   *
   * @param forwardOnly a boolean value indicating whether the "forwardOnly" option is enabled
   * (true) or disabled (false)
   */
  public void setForwardOnly(boolean forwardOnly) {
    this.forwardOnly = forwardOnly;
  }

  /**
   * Determines whether the "useCurrentAttemptObjectiveInfo" option is enabled for the current
   * instance of the control mode. This option specifies if the objective information for the
   * current attempt should be considered.
   *
   * @return true if the "useCurrentAttemptObjectiveInfo" option is enabled, false otherwise.
   */
  public boolean isUseCurrentAttemptObjectiveInfo() {
    return this.useCurrentAttemptObjectiveInfo;
  }

  /**
   * Sets the "useCurrentAttemptObjectiveInfo" option for the current instance of the control mode.
   * This option determines whether the objective information for the current attempt should be
   * used.
   *
   * @param useCurrentAttemptObjectiveInfo a boolean value indicating whether the
   * "useCurrentAttemptObjectiveInfo" option should be enabled (true) or disabled (false)
   */
  public void setUseCurrentAttemptObjectiveInfo(boolean useCurrentAttemptObjectiveInfo) {
    this.useCurrentAttemptObjectiveInfo = useCurrentAttemptObjectiveInfo;
  }

  /**
   * Determines whether the "useCurrentAttemptProgressInfo" option is enabled for the current
   * instance of the control mode. This option specifies if the progress information for the current
   * attempt should be considered.
   *
   * @return true if the "useCurrentAttemptProgressInfo" option is enabled, false otherwise.
   */
  public boolean isUseCurrentAttemptProgressInfo() {
    return this.useCurrentAttemptProgressInfo;
  }

  /**
   * Sets the "useCurrentAttemptProgressInfo" option for the current instance of the control mode.
   * This option determines whether the progress information for the current attempt should be
   * used.
   *
   * @param useCurrentAttemptProgressInfo a boolean value indicating whether the
   * "useCurrentAttemptProgressInfo" option should be enabled (true) or disabled (false)
   */
  public void setUseCurrentAttemptProgressInfo(boolean useCurrentAttemptProgressInfo) {
    this.useCurrentAttemptProgressInfo = useCurrentAttemptProgressInfo;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (!(o instanceof ControlMode that)) {
      return false;
    }

    return new EqualsBuilder()
        .append(isChoice(), that.isChoice())
        .append(isChoiceExit(), that.isChoiceExit())
        .append(isFlow(), that.isFlow())
        .append(isForwardOnly(), that.isForwardOnly())
        .append(isUseCurrentAttemptObjectiveInfo(), that.isUseCurrentAttemptObjectiveInfo())
        .append(isUseCurrentAttemptProgressInfo(), that.isUseCurrentAttemptProgressInfo())
        .isEquals();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder(17, 37)
        .append(isChoice())
        .append(isChoiceExit())
        .append(isFlow())
        .append(isForwardOnly())
        .append(isUseCurrentAttemptObjectiveInfo())
        .append(isUseCurrentAttemptProgressInfo())
        .toHashCode();
  }
}
