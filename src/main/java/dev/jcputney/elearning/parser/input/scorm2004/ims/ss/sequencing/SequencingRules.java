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
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import dev.jcputney.elearning.parser.input.scorm2004.IMSSS;
import java.io.Serializable;
import java.util.List;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * Represents a set of sequencing rules within the SCORM IMS Simple Sequencing (IMSSS) schema.
 * Sequencing rules define specific conditions and actions that control the availability and
 * behavior of learning activities based on the learner's progress, completion status, and
 * interactions with the content.
 *
 * <p>Sequencing rules are used to enforce a specific flow of learning activities,
 * applying conditions that enable, disable, hide, or skip activities. These rules can be
 * categorized into three main types:</p>
 *
 * <ul>
 *   <li><strong>Pre-Condition Rules:</strong> Applied before an activity is attempted,
 *   to determine if it should be enabled, disabled, or hidden from the learner.</li>
 *   <li><strong>Exit-Condition Rules:</strong> Applied when the learner exits an activity,
 *   to define the behavior upon exit, such as retrying the activity or exiting the current sequence.</li>
 *   <li><strong>Post-Condition Rules:</strong> Applied after an activity has been attempted,
 *   to control subsequent sequencing actions, such as skipping an activity or marking it as complete.</li>
 * </ul>
 *
 * <p>Each sequencing rule contains conditions (defined in {@link RuleConditions}) that
 * must be met for the associated action (defined in {@link RuleAction}) to be applied.</p>
 *
 * <p>The IMSSS namespace is specified by {@link IMSSS#NAMESPACE_URI}, and this class
 * aligns with the SCORM 2004 standards for sequencing and navigation.</p>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonFormat(with = JsonFormat.Feature.ACCEPT_CASE_INSENSITIVE_PROPERTIES)
public final class SequencingRules implements Serializable {

  /**
   * A list of pre-condition rules that are evaluated before an activity is attempted. These rules
   * can be used to enable, disable, or hide an activity based on specific conditions, such as the
   * completion status of previous activities.
   */
  @JacksonXmlElementWrapper(useWrapping = false)
  @JacksonXmlProperty(localName = "preConditionRule", namespace = IMSSS.NAMESPACE_URI)
  private List<SequencingRule> preConditionRules;
  /**
   * A list of exit-condition rules that are evaluated when the learner exits an activity. These
   * rules define actions that control sequencing upon exit, such as retrying the current activity,
   * proceeding to the next activity, or exiting the sequence entirely.
   */
  @JacksonXmlElementWrapper(useWrapping = false)
  @JacksonXmlProperty(localName = "exitConditionRule", namespace = IMSSS.NAMESPACE_URI)
  private List<SequencingRule> exitConditionRules;
  /**
   * A list of post-condition rules that are evaluated after an activity has been attempted. These
   * rules can specify actions like skipping certain activities, marking an activity as complete, or
   * determining if subsequent activities should be enabled.
   */
  @JacksonXmlElementWrapper(useWrapping = false)
  @JacksonXmlProperty(localName = "postConditionRule", namespace = IMSSS.NAMESPACE_URI)
  private List<SequencingRule> postConditionRules;

  public SequencingRules() {
    // no-op
  }

  /**
   * Retrieves the list of pre-condition sequencing rules. Pre-condition rules define the criteria
   * that must be met before an activity is processed within the sequencing context.
   *
   * @return a list of {@code SequencingRule} objects representing the pre-condition rules for
   * sequencing, or an empty list if no such rules are defined
   */
  public List<SequencingRule> getPreConditionRules() {
    return this.preConditionRules;
  }

  /**
   * Sets the list of pre-condition sequencing rules for an activity. Pre-condition rules define the
   * criteria that must be met before an activity is processed within the sequencing context.
   *
   * @param preConditionRules a list of {@code SequencingRule} objects representing the
   * pre-condition rules for sequencing. Passing {@code null} will clear the current pre-condition
   * rules.
   */
  public void setPreConditionRules(List<SequencingRule> preConditionRules) {
    this.preConditionRules = preConditionRules;
  }

  /**
   * Retrieves the list of exit-condition sequencing rules. Exit-condition rules define the criteria
   * that cause an activity to exit its processing context during sequencing.
   *
   * @return a list of {@code SequencingRule} objects representing the exit-condition rules for
   * sequencing, or an empty list if no such rules are defined
   */
  public List<SequencingRule> getExitConditionRules() {
    return this.exitConditionRules;
  }

  /**
   * Sets the list of exit-condition sequencing rules for an activity. Exit-condition rules define
   * the criteria that cause an activity to exit its processing context during sequencing.
   *
   * @param exitConditionRules a list of {@code SequencingRule} objects representing the
   * exit-condition rules for sequencing. Passing {@code null} will clear the current exit-condition
   * rules.
   */
  public void setExitConditionRules(List<SequencingRule> exitConditionRules) {
    this.exitConditionRules = exitConditionRules;
  }

  /**
   * Retrieves the list of post-condition sequencing rules. Post-condition rules specify the
   * criteria and actions to be evaluated after an activity completes processing within the
   * sequencing context.
   *
   * @return a list of {@code SequencingRule} objects representing the post-condition rules for
   * sequencing, or an empty list if no such rules are defined
   */
  public List<SequencingRule> getPostConditionRules() {
    return this.postConditionRules;
  }

  /**
   * Sets the list of post-condition sequencing rules for an activity. Post-condition rules specify
   * the criteria and actions to be evaluated after an activity completes processing within the
   * sequencing context.
   *
   * @param postConditionRules a list of {@code SequencingRule} objects representing the
   * post-condition rules for sequencing. Passing {@code null} will clear the current post-condition
   * rules.
   */
  public void setPostConditionRules(List<SequencingRule> postConditionRules) {
    this.postConditionRules = postConditionRules;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (!(o instanceof SequencingRules that)) {
      return false;
    }

    return new EqualsBuilder()
        .append(getPreConditionRules(), that.getPreConditionRules())
        .append(getExitConditionRules(), that.getExitConditionRules())
        .append(getPostConditionRules(), that.getPostConditionRules())
        .isEquals();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder(17, 37)
        .append(getPreConditionRules())
        .append(getExitConditionRules())
        .append(getPostConditionRules())
        .toHashCode();
  }
}
