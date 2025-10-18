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
import dev.jcputney.elearning.parser.input.scorm2004.ims.ss.types.ConditionOperatorType;
import dev.jcputney.elearning.parser.input.scorm2004.ims.ss.types.MeasureType;
import dev.jcputney.elearning.parser.input.scorm2004.ims.ss.types.MeasureTypeDeserializer;
import dev.jcputney.elearning.parser.input.scorm2004.ims.ss.types.SequencingRuleConditionType;
import java.io.Serializable;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * Represents an individual condition within a set of rule conditions. Each condition specifies a
 * specific criterion that must be met, such as an objective being completed, a minimum measure
 * threshold, or a defined attempt status.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonFormat(with = JsonFormat.Feature.ACCEPT_CASE_INSENSITIVE_PROPERTIES)
public final class RuleCondition implements Serializable {

  /**
   * The identifier of a specific objective to which this condition applies. This attribute
   * references an objective that must meet the specified condition.
   */
  @JacksonXmlProperty(isAttribute = true)
  @JsonProperty("referencedObjective")
  private String referencedObjective;

  /**
   * The minimum measure threshold for the objective. This value indicates the level of achievement
   * required for the condition to be met, typically represented as a decimal.
   */
  @JacksonXmlProperty(isAttribute = true)
  @JsonDeserialize(using = MeasureTypeDeserializer.class)
  @JsonProperty("measureThreshold")
  private MeasureType measureThreshold;

  /**
   * Specifies the operator to use when evaluating the rule condition. Possible values are:
   * <ul>
   *   <li><strong>NOT:</strong> Inverts the result of the condition.</li>
   *   <li><strong>NO_OP:</strong> Applies the condition as-is.</li>
   * </ul>
   */
  @JacksonXmlProperty(isAttribute = true)
  @JsonProperty("operator")
  private ConditionOperatorType operator = ConditionOperatorType.NO_OP;

  /**
   * Specifies the condition evaluated for this rule. The condition can include criteria such as
   * "satisfied", "completed", "attempted", or other learning activity states.
   */
  @JacksonXmlProperty(isAttribute = true)
  @JsonProperty("condition")
  private SequencingRuleConditionType condition;

  /**
   * Constructs a new instance of the RuleCondition class, which represents a condition used in
   * sequencing and evaluation rules.
   *
   * @param referencedObjective the identifier of the referenced objective as a {@code String}. This
   * specifies the learning objective associated with the rule condition.
   * @param measureThreshold the threshold value of type {@code MeasureType} that is evaluated
   * against the condition. Values must be between -1 and 1.
   * @param operator the operator of type {@code ConditionOperatorType} that defines how the
   * condition is applied, such as "not" to negate the condition or "noOp" for no modification.
   * @param condition the type of condition represented by {@code SequencingRuleConditionType},
   * which specifies criteria for sequencing rules, such as activity completion status or objective
   * status.
   */
  public RuleCondition(String referencedObjective, MeasureType measureThreshold,
      ConditionOperatorType operator,
      SequencingRuleConditionType condition) {
    this.referencedObjective = referencedObjective;
    this.measureThreshold = measureThreshold;
    this.operator = operator;
    this.condition = condition;
  }

  /**
   * Default constructor for the RuleCondition class.
   * <p>
   * This constructor initializes a new instance of the RuleCondition class with default values for
   * its attributes. It is primarily used to create an instance without specifying any initial
   * parameters.
   */
  public RuleCondition() {
    // no-op
  }

  /**
   * Retrieves the referenced objective associated with this rule condition.
   *
   * @return the identifier of the referenced objective as a string
   */
  public String getReferencedObjective() {
    return this.referencedObjective;
  }

  /**
   * Sets the referenced objective associated with this rule condition.
   *
   * @param referencedObjective the identifier of the referenced objective to set, represented as a
   * string
   */
  public void setReferencedObjective(String referencedObjective) {
    this.referencedObjective = referencedObjective;
  }

  /**
   * Retrieves the measure threshold associated with this rule condition.
   *
   * @return the measure threshold of type MeasureType
   */
  public MeasureType getMeasureThreshold() {
    return this.measureThreshold;
  }

  /**
   * Sets the measure threshold associated with this rule condition.
   *
   * @param measureThreshold the measure threshold of type MeasureType to set
   */
  public void setMeasureThreshold(MeasureType measureThreshold) {
    this.measureThreshold = measureThreshold;
  }

  /**
   * Retrieves the condition operator associated with this rule condition.
   *
   * @return the operator of type ConditionOperatorType, which defines how the condition is
   * evaluated
   */
  public ConditionOperatorType getOperator() {
    return this.operator;
  }

  /**
   * Sets the condition operator for this rule condition.
   *
   * @param operator the operator of type {@code ConditionOperatorType} to set. This defines how the
   * condition is evaluated, such as "not" to negate the condition or "noOp" to leave it
   * unmodified.
   */
  public void setOperator(ConditionOperatorType operator) {
    this.operator = operator;
  }

  /**
   * Retrieves the sequencing rule condition associated with this instance.
   *
   * @return the condition of type SequencingRuleConditionType
   */
  public SequencingRuleConditionType getCondition() {
    return this.condition;
  }

  /**
   * Sets the sequencing rule condition associated with this instance.
   *
   * @param condition the condition of type {@code SequencingRuleConditionType} to set. This defines
   * the criteria that must be met for the rule to apply, such as activity completion status,
   * attempt count, or objective status.
   */
  public void setCondition(SequencingRuleConditionType condition) {
    this.condition = condition;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (!(o instanceof RuleCondition that)) {
      return false;
    }

    return new EqualsBuilder()
        .append(getReferencedObjective(),
            that.getReferencedObjective())
        .append(getMeasureThreshold(), that.getMeasureThreshold())
        .append(getOperator(), that.getOperator())
        .append(getCondition(), that.getCondition())
        .isEquals();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder(17, 37)
        .append(getReferencedObjective())
        .append(getMeasureThreshold())
        .append(getOperator())
        .append(getCondition())
        .toHashCode();
  }
}
