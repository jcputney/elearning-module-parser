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
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import dev.jcputney.elearning.parser.input.scorm2004.IMSSS;
import dev.jcputney.elearning.parser.input.scorm2004.ims.ss.types.ConditionCombinationType;
import java.io.Serializable;
import java.util.List;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * Represents a set of conditions that must be met for a sequencing rule to apply. Rule conditions
 * define specific criteria, such as completion status or objective measure, that determine whether
 * a rule's action should be executed.
 *
 * <p>The rule conditions are combined based on the specified combination logic,
 * such as "all" (all conditions must be met) or "any" (at least one condition must be met).</p>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonFormat(with = JsonFormat.Feature.ACCEPT_CASE_INSENSITIVE_PROPERTIES)
public class RuleConditions implements Serializable {

  /**
   * A list of individual rule conditions. Each condition specifies a criterion, such as an
   * objective being completed, that must be met for the rule's action to apply.
   */
  @JacksonXmlElementWrapper(useWrapping = false)
  @JacksonXmlProperty(localName = "ruleCondition", namespace = IMSSS.NAMESPACE_URI)
  private List<RuleCondition> ruleConditionList;
  /**
   * Specifies the combination type for conditions in this rollup rule. Determines whether all
   * conditions must be met ("all") or if any single condition is sufficient ("any").
   */
  @JacksonXmlProperty(isAttribute = true)
  @JsonProperty("conditionCombination")
  private ConditionCombinationType conditionCombination = ConditionCombinationType.ANY;

  public RuleConditions() {
    // no-op
  }

  /**
   * Retrieves the list of rule conditions associated with this instance. Each rule condition
   * defines a specific criterion that must be evaluated or met within the context of a sequencing
   * rule.
   *
   * @return a list of {@code RuleCondition} objects representing the individual conditions for this
   * set of rules, or an empty list if no conditions are defined
   */
  public List<RuleCondition> getRuleConditionList() {
    return this.ruleConditionList;
  }

  /**
   * Sets the list of rule conditions associated with this instance. Each rule condition specifies a
   * criterion that must be evaluated or met as part of the sequencing rule.
   *
   * @param ruleConditionList a list of {@code RuleCondition} objects representing the individual
   * conditions for the sequencing rule. If set to {@code null}, this clears the current list of
   * rule conditions.
   */
  public void setRuleConditionList(List<RuleCondition> ruleConditionList) {
    this.ruleConditionList = ruleConditionList;
  }

  /**
   * Retrieves the condition combination type associated with this set of rule conditions. The
   * condition combination determines whether all conditions must be satisfied, any one condition
   * needs to be satisfied, or if the type is unknown.
   *
   * @return the condition combination type, represented as a {@code ConditionCombinationType} enum,
   * which could be {@code ALL}, {@code ANY}, or {@code UNKNOWN}.
   */
  public ConditionCombinationType getConditionCombination() {
    return this.conditionCombination;
  }

  /**
   * Sets the condition combination type for the current instance. The condition combination
   * determines how multiple conditions in a sequencing rule are evaluated, such as requiring all
   * conditions to be met or allowing any one condition to be sufficient.
   *
   * @param conditionCombination the condition combination type to set, represented as a
   * {@code ConditionCombinationType} enum. Possible values include {@code ALL}, {@code ANY}, and
   * {@code UNKNOWN}.
   */
  public void setConditionCombination(ConditionCombinationType conditionCombination) {
    this.conditionCombination = conditionCombination;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (!(o instanceof RuleConditions that)) {
      return false;
    }

    return new EqualsBuilder()
        .append(getRuleConditionList(), that.getRuleConditionList())
        .append(getConditionCombination(), that.getConditionCombination())
        .isEquals();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder(17, 37)
        .append(getRuleConditionList())
        .append(getConditionCombination())
        .toHashCode();
  }
}
