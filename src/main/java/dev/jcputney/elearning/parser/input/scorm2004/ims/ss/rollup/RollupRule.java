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

package dev.jcputney.elearning.parser.input.scorm2004.ims.ss.rollup;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import dev.jcputney.elearning.parser.input.common.PercentType;
import dev.jcputney.elearning.parser.input.common.PercentTypeDeserializer;
import dev.jcputney.elearning.parser.input.common.PercentTypeSerializer;
import dev.jcputney.elearning.parser.input.scorm2004.IMSSS;
import dev.jcputney.elearning.parser.input.scorm2004.ims.ss.types.ChildActivitySet;
import java.io.Serializable;
import java.math.BigDecimal;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * Represents an individual rollup rule within a set of rollup rules. Each rule defines conditions
 * and an action that dictate how child activities’ statuses affect the parent activity’s rollup.
 */
@JsonFormat(with = JsonFormat.Feature.ACCEPT_CASE_INSENSITIVE_PROPERTIES)
public final class RollupRule implements Serializable {

  /**
   * The conditions that must be met for the rollup rule to apply. These conditions specify criteria
   * such as completion status, satisfaction status, or other properties of the child activities
   * that control whether the rollup action should occur.
   */
  @JacksonXmlProperty(localName = "rollupConditions", namespace = IMSSS.NAMESPACE_URI)
  private RollupConditions rollupConditions;

  /**
   * The action to perform if the rule’s conditions are met. The rollup action determines how the
   * child activities' statuses impact the parent activity’s rollup status, such as marking the
   * parent as completed or satisfied.
   */
  @JacksonXmlProperty(localName = "rollupAction", namespace = IMSSS.NAMESPACE_URI)
  private RollupAction rollupAction;

  /**
   * Specifies the set of child activities to consider when evaluating this rollup rule. This
   * attribute controls whether the rollup rule should apply to all child activities, any child
   * activity, none, or a specific count or percentage.
   */
  @JacksonXmlProperty(isAttribute = true)
  @JsonProperty("childActivitySet")
  private ChildActivitySet childActivitySet = ChildActivitySet.ALL;

  /**
   * Specifies the minimum number of child activities that must meet the rollup conditions for this
   * rule to apply. This attribute is only relevant when <code>childActivitySet</code> is set to
   * <code>atLeastCount</code>.
   *
   * <p>Defaults to <code>0</code>.</p>
   */
  @JacksonXmlProperty(isAttribute = true)
  @JsonProperty("minimumCount")
  private int minimumCount = 0;

  /**
   * Specifies the minimum percentage of child activities that must meet the rollup conditions for
   * this rule to apply. This attribute is only relevant when <code>childActivitySet</code> is set
   * to <code>atLeastPercent</code>.
   *
   * <p>Defaults to <code>0.0</code> and is represented as a decimal between 0 and 1.</p>
   */
  @JacksonXmlProperty(isAttribute = true)
  @JsonDeserialize(using = PercentTypeDeserializer.class)
  @JsonSerialize(using = PercentTypeSerializer.class)
  @JsonProperty("minimumPercent")
  private PercentType minimumPercent = new PercentType(BigDecimal.ZERO);

  public RollupRule() {
    // no-op
  }

  /**
   * Retrieves the rollup conditions associated with this instance. Rollup conditions define
   * criteria that determine if specific rollup logic should be applied based on the status of child
   * activities.
   *
   * @return the {@link RollupConditions} object representing the criteria for applying the rollup
   * rule.
   */
  public RollupConditions getRollupConditions() {
    return this.rollupConditions;
  }

  /**
   * Sets the rollup conditions for this instance. Rollup conditions define the specific criteria
   * that determine if rollup logic should be applied based on the status of child activities.
   *
   * @param rollupConditions the {@link RollupConditions} object containing the criteria for rollup
   * processing
   */
  public void setRollupConditions(RollupConditions rollupConditions) {
    this.rollupConditions = rollupConditions;
  }

  /**
   * Retrieves the rollup action associated with this instance. The rollup action specifies the
   * operation to perform on the parent activity's status when the conditions defined in the rollup
   * rule are satisfied.
   *
   * @return the {@link RollupAction} object representing the action to be performed if the rollup
   * conditions are met.
   */
  public RollupAction getRollupAction() {
    return this.rollupAction;
  }

  /**
   * Sets the rollup action to be performed when the conditions of the rollup rule are met. The
   * rollup action specifies the operation to apply to the parent activity's status based on the
   * statuses of its child activities and the associated rollup criteria.
   *
   * @param rollupAction the {@link RollupAction} object defining the specific action to be taken
   * when the rollup conditions are satisfied
   */
  public void setRollupAction(RollupAction rollupAction) {
    this.rollupAction = rollupAction;
  }

  /**
   * Retrieves the child activity set associated with this instance. The child activity set
   * specifies the subset of child activities to consider while evaluating the rollup rule.
   *
   * @return the {@link ChildActivitySet} representing the set of child activities to be used in
   * rollup rule evaluation.
   */
  public ChildActivitySet getChildActivitySet() {
    return this.childActivitySet;
  }

  /**
   * Sets the child activity set for this instance. The child activity set specifies the subset of
   * child activities to consider during the evaluation of the rollup rule.
   *
   * @param childActivitySet the {@link ChildActivitySet} representing the set of child activities
   * to be used in rollup rule evaluation
   */
  public void setChildActivitySet(ChildActivitySet childActivitySet) {
    this.childActivitySet = childActivitySet;
  }

  /**
   * Retrieves the minimum count associated with this instance. The minimum count specifies the
   * threshold value used in evaluating whether the rollup rule criteria are met.
   *
   * @return the minimum count as an integer.
   */
  public int getMinimumCount() {
    return this.minimumCount;
  }

  /**
   * Sets the minimum count for this instance. The minimum count represents the threshold value used
   * in evaluating whether the rollup rule criteria are met.
   *
   * @param minimumCount the minimum count as an integer.
   */
  public void setMinimumCount(int minimumCount) {
    this.minimumCount = minimumCount;
  }

  /**
   * Retrieves the minimum percentage associated with this instance. The minimum percentage is
   * represented as a {@link PercentType}, which holds a value constrained between 0 and 1,
   * inclusive.
   *
   * @return the {@link PercentType} object representing the minimum percentage.
   */
  public PercentType getMinimumPercent() {
    return this.minimumPercent;
  }

  /**
   * Sets the minimum percentage for this instance. The minimum percentage is represented as a
   * {@link PercentType}, which holds a value constrained between 0 and 1, inclusive. This
   * percentage defines the threshold to be used in rollup rule evaluations.
   *
   * @param minimumPercent the {@link PercentType} object representing the minimum percentage value
   * to set.
   */
  public void setMinimumPercent(PercentType minimumPercent) {
    this.minimumPercent = minimumPercent;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (!(o instanceof RollupRule that)) {
      return false;
    }

    return new EqualsBuilder()
        .append(getMinimumCount(), that.getMinimumCount())
        .append(getRollupConditions(), that.getRollupConditions())
        .append(getRollupAction(), that.getRollupAction())
        .append(getChildActivitySet(), that.getChildActivitySet())
        .append(getMinimumPercent(), that.getMinimumPercent())
        .isEquals();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder(17, 37)
        .append(getRollupConditions())
        .append(getRollupAction())
        .append(getChildActivitySet())
        .append(getMinimumCount())
        .append(getMinimumPercent())
        .toHashCode();
  }
}
