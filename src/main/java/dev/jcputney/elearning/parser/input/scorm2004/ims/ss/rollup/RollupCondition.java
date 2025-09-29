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
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import dev.jcputney.elearning.parser.input.scorm2004.ims.ss.types.ConditionOperatorType;
import dev.jcputney.elearning.parser.input.scorm2004.ims.ss.types.RollupRuleConditionType;
import java.io.Serializable;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * Represents an individual rollup condition within a set of rollup conditions. Each condition
 * specifies a specific criterion, such as completion or satisfaction status, that affects whether
 * the associated rollup rule is applied.
 */
@JsonFormat(with = JsonFormat.Feature.ACCEPT_CASE_INSENSITIVE_PROPERTIES)
public class RollupCondition implements Serializable {

  /**
   * Specifies the operator to use when evaluating the rollup condition. Possible values include:
   * <ul>
   *   <li><strong>NOT:</strong> Inverts the evaluation result of the condition.</li>
   *   <li><strong>NO_OP:</strong> Applies the condition as-is, without modification.</li>
   * </ul>
   */
  @JacksonXmlProperty(isAttribute = true)
  @JsonProperty("operator")
  private ConditionOperatorType operator = ConditionOperatorType.NO_OP;

  /**
   * Specifies the specific condition being evaluated in this rollup condition. The condition may
   * include criteria related to the learner's progress or completion status of activities.
   */
  @JacksonXmlProperty(isAttribute = true)
  @JsonProperty("condition")
  private RollupRuleConditionType condition;

  public RollupCondition() {
    // no-op
  }

  /**
   * Retrieves the operator used to evaluate the rollup condition.
   *
   * @return the {@link ConditionOperatorType} representing the operator for this rollup condition.
   */
  public ConditionOperatorType getOperator() {
    return this.operator;
  }

  /**
   * Sets the operator for this rollup condition. The operator determines how the condition is
   * evaluated during the rollup process.
   *
   * @param operator the {@link ConditionOperatorType} to set as the operator for this rollup
   * condition, which specifies whether to invert the condition (NOT) or apply it as-is (NO_OP).
   */
  public void setOperator(ConditionOperatorType operator) {
    this.operator = operator;
  }

  /**
   * Retrieves the condition specified for this rollup condition. The condition determines the
   * criteria being evaluated, such as completion status, progress, or other activity-related
   * metrics, which influence the associated rollup rule.
   *
   * @return the {@link RollupRuleConditionType} representing the specific condition of this rollup
   * condition.
   */
  public RollupRuleConditionType getCondition() {
    return this.condition;
  }

  /**
   * Sets the specific condition to evaluate within this rollup condition. The provided condition
   * determines the criteria related to the learner's progress, completion, or activity status that
   * affect the rollup rule's application.
   *
   * @param condition the {@link RollupRuleConditionType} representing the criterion to set for this
   * rollup condition, such as completion, satisfaction, or other activity-related metrics.
   */
  public void setCondition(RollupRuleConditionType condition) {
    this.condition = condition;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (!(o instanceof RollupCondition that)) {
      return false;
    }

    return new EqualsBuilder()
        .append(getOperator(), that.getOperator())
        .append(getCondition(), that.getCondition())
        .isEquals();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder(17, 37)
        .append(getOperator())
        .append(getCondition())
        .toHashCode();
  }
}
