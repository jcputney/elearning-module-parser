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
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import dev.jcputney.elearning.parser.input.scorm2004.IMSSS;
import java.io.Serializable;
import java.util.List;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * Represents a set of rollup rules within the SCORM IMS Simple Sequencing (IMSSS) schema. Rollup
 * rules define how the completion status, progress, and satisfaction of learning objectives are
 * aggregated or "rolled up" from child activities to their parent activities in the content
 * hierarchy.
 *
 * <p>Rollup rules are used to determine whether a parent activity (such as a module or course)
 * is considered complete or satisfied based on the statuses of its child activities. By configuring
 * these rules, instructional designers can define custom logic for calculating the overall
 * completion and success of a sequence based on individual activities within that sequence.</p>
 *
 * <p>Common configurations include:</p>
 * <ul>
 *   <li>Requiring all child activities to be completed before the parent activity is considered complete.</li>
 *   <li>Setting a minimum number of child activities to be completed or a minimum percentage of completion.</li>
 *   <li>Specifying objective weights to control the impact of each activity on the overall progress.</li>
 * </ul>
 *
 * <p>The IMSSS namespace is specified by {@link IMSSS#NAMESPACE_URI}, following the SCORM 2004 standards
 * for sequencing and navigation.</p>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonFormat(with = JsonFormat.Feature.ACCEPT_CASE_INSENSITIVE_PROPERTIES)
public class RollupRules implements Serializable {

  /**
   * A list of individual rollup rules that define specific conditions and actions for aggregating
   * completion and satisfaction statuses from child activities. Each rollup rule specifies
   * conditions under which the rule applies and an action to perform when the conditions are met.
   */
  @JacksonXmlElementWrapper(useWrapping = false)
  @JacksonXmlProperty(localName = "rollupRule", namespace = IMSSS.NAMESPACE_URI)
  private List<RollupRule> rollupRuleList;

  /**
   * Indicates whether the objective satisfaction status of the parent activity should be based on
   * the rollup of its child activities' statuses. When set to <code>true</code>, the parent
   * activity’s objective satisfaction depends on the rollup logic defined in the child activities.
   *
   * <p>Defaults to <code>true</code>.</p>
   */
  @JacksonXmlProperty(isAttribute = true)
  @JsonProperty("rollupObjectiveSatisfied")
  private boolean rollupObjectiveSatisfied = true;

  /**
   * Indicates whether the progress completion status of the parent activity should be based on the
   * rollup of its child activities' completion statuses. When set to <code>true</code>, the parent
   * activity’s progress completion depends on the rollup logic defined in the child activities.
   *
   * <p>Defaults to <code>true</code>.</p>
   */
  @JacksonXmlProperty(isAttribute = true)
  @JsonProperty("rollupProgressCompletion")
  private boolean rollupProgressCompletion = true;

  /**
   * Specifies the weighting applied to the measure of each child activity’s objective when
   * determining the rollup satisfaction of the parent activity. This value allows each child
   * activity to contribute to the parent’s satisfaction based on a weighted score, rather than a
   * simple average or completion count.
   *
   * <p>Defaults to <code>1.0</code> and is represented as a decimal between 0 and 1.</p>
   */
  @JacksonXmlProperty(isAttribute = true)
  @JsonProperty("objectiveMeasureWeight")
  private double objectiveMeasureWeight = 1.0;

  public RollupRules(List<RollupRule> rollupRuleList, boolean rollupObjectiveSatisfied,
      boolean rollupProgressCompletion, double objectiveMeasureWeight) {
    this.rollupRuleList = rollupRuleList;
    this.rollupObjectiveSatisfied = rollupObjectiveSatisfied;
    this.rollupProgressCompletion = rollupProgressCompletion;
    this.objectiveMeasureWeight = objectiveMeasureWeight;
  }

  public RollupRules() {
    // no-op
  }

  /**
   * Retrieves the list of rollup rules associated with this instance.
   *
   * @return a list of RollupRule objects representing the rollup rules.
   */
  public List<RollupRule> getRollupRuleList() {
    return this.rollupRuleList;
  }

  /**
   * Sets the list of rollup rules for this instance.
   *
   * @param rollupRuleList a list of RollupRule objects to be associated with this instance
   */
  public void setRollupRuleList(
      List<RollupRule> rollupRuleList) {
    this.rollupRuleList = rollupRuleList;
  }

  /**
   * Determines whether the rollup objective is satisfied for this instance.
   *
   * @return true if the rollup objective is satisfied, false otherwise.
   */
  public boolean isRollupObjectiveSatisfied() {
    return this.rollupObjectiveSatisfied;
  }

  /**
   * Sets the status of whether the rollup objective is satisfied for this instance.
   *
   * @param rollupObjectiveSatisfied a boolean value indicating whether the rollup objective is
   * satisfied
   */
  public void setRollupObjectiveSatisfied(boolean rollupObjectiveSatisfied) {
    this.rollupObjectiveSatisfied = rollupObjectiveSatisfied;
  }

  /**
   * Determines whether the rollup progress completion condition is met for this instance.
   *
   * @return true if the rollup progress completion is satisfied, false otherwise.
   */
  public boolean isRollupProgressCompletion() {
    return this.rollupProgressCompletion;
  }

  /**
   * Sets the status of whether the rollup progress completion condition is met for this instance.
   *
   * @param rollupProgressCompletion a boolean value indicating whether the rollup progress
   * completion condition is satisfied
   */
  public void setRollupProgressCompletion(boolean rollupProgressCompletion) {
    this.rollupProgressCompletion = rollupProgressCompletion;
  }

  /**
   * Retrieves the weight of the objective measure associated with this instance.
   *
   * @return a double value representing the weight of the objective measure.
   */
  public double getObjectiveMeasureWeight() {
    return this.objectiveMeasureWeight;
  }

  /**
   * Sets the weight of the objective measure for this instance.
   *
   * @param objectiveMeasureWeight a double value representing the weight of the objective measure
   */
  public void setObjectiveMeasureWeight(double objectiveMeasureWeight) {
    this.objectiveMeasureWeight = objectiveMeasureWeight;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (!(o instanceof RollupRules that)) {
      return false;
    }

    return new EqualsBuilder()
        .append(isRollupObjectiveSatisfied(),
            that.isRollupObjectiveSatisfied())
        .append(isRollupProgressCompletion(), that.isRollupProgressCompletion())
        .append(getObjectiveMeasureWeight(), that.getObjectiveMeasureWeight())
        .append(getRollupRuleList(), that.getRollupRuleList())
        .isEquals();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder(17, 37)
        .append(getRollupRuleList())
        .append(isRollupObjectiveSatisfied())
        .append(isRollupProgressCompletion())
        .append(getObjectiveMeasureWeight())
        .toHashCode();
  }
}
