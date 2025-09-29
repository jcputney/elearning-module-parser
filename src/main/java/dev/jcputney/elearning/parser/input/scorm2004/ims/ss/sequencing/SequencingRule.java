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
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import dev.jcputney.elearning.parser.input.scorm2004.IMSSS;
import java.io.Serializable;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * Represents a single sequencing rule within a set of pre-condition, exit-condition, or
 * post-condition rules. Each rule consists of a set of conditions and an action.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonFormat(with = JsonFormat.Feature.ACCEPT_CASE_INSENSITIVE_PROPERTIES)
public class SequencingRule implements Serializable {

  /**
   * The conditions that must be met for the rule to apply. These conditions specify criteria such
   * as activity completion status, attempt count, or objective status that control whether the
   * rule's action should be executed.
   */
  @JacksonXmlProperty(localName = "ruleConditions", namespace = IMSSS.NAMESPACE_URI)
  private RuleConditions ruleConditions;
  /**
   * The action to perform if the rule's conditions are met. Actions control the sequencing
   * behavior, such as enabling, disabling, or hiding an activity, or advancing to the next
   * activity.
   */
  @JacksonXmlProperty(localName = "ruleAction", namespace = IMSSS.NAMESPACE_URI)
  private RuleAction ruleAction;

  public SequencingRule() {
    // no-op
  }

  /**
   * Retrieves the rule conditions associated with this sequencing rule. Rule conditions define the
   * criteria that must be met for the rule to apply.
   *
   * @return the {@code RuleConditions} object representing the conditions that control the
   * applicability of the rule, or {@code null} if no conditions are set
   */
  public RuleConditions getRuleConditions() {
    return this.ruleConditions;
  }

  /**
   * Sets the conditions that define the criteria for the sequencing rule to apply. These conditions
   * specify the logic and requirements that must be fulfilled for the associated action to be
   * executed.
   *
   * @param ruleConditions the {@code RuleConditions} object containing the set of conditions that
   * determine the applicability of the rule. Passing {@code null} will clear the current
   * conditions.
   */
  public void setRuleConditions(RuleConditions ruleConditions) {
    this.ruleConditions = ruleConditions;
  }

  /**
   * Retrieves the action to be performed if the conditions specified in the sequencing rule are
   * met. The action determines the sequencing behavior for the associated activity, such as
   * enabling, disabling, hiding, skipping, or preventing forward traversal.
   *
   * @return the {@code RuleAction} object representing the action specified in the rule, or
   * {@code null} if no action is set
   */
  public RuleAction getRuleAction() {
    return this.ruleAction;
  }

  /**
   * Sets the action to be executed when the conditions specified in the sequencing rule are met.
   * This method updates the {@code RuleAction} associated with the rule, determining the behavior
   * that will take place if the rule's conditions are satisfied.
   *
   * @param ruleAction the {@code RuleAction} object representing the action to be performed. This
   * may include actions such as skipping an activity, disabling it, hiding it from choice, or
   * preventing forward traversal. Passing {@code null} will clear the current action.
   */
  public void setRuleAction(RuleAction ruleAction) {
    this.ruleAction = ruleAction;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (!(o instanceof SequencingRule that)) {
      return false;
    }

    return new EqualsBuilder()
        .append(getRuleConditions(), that.getRuleConditions())
        .append(getRuleAction(), that.getRuleAction())
        .isEquals();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder(17, 37)
        .append(getRuleConditions())
        .append(getRuleAction())
        .toHashCode();
  }
}
