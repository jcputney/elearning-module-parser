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

import static lombok.AccessLevel.PRIVATE;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import dev.jcputney.elearning.parser.input.scorm2004.ims.ss.types.ConditionOperatorType;
import dev.jcputney.elearning.parser.input.scorm2004.ims.ss.types.MeasureType;
import dev.jcputney.elearning.parser.input.scorm2004.ims.ss.types.MeasureTypeDeserializer;
import dev.jcputney.elearning.parser.input.scorm2004.ims.ss.types.SequencingRuleConditionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;

/**
 * Represents an individual condition within a set of rule conditions. Each condition specifies a
 * specific criterion that must be met, such as an objective being completed, a minimum measure
 * threshold, or a defined attempt status.
 */
@Builder
@Getter
@Jacksonized
@AllArgsConstructor(access = PRIVATE)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonFormat(with = JsonFormat.Feature.ACCEPT_CASE_INSENSITIVE_PROPERTIES)
public class RuleCondition {

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
  @Default
  private ConditionOperatorType operator = ConditionOperatorType.NO_OP;
  /**
   * Specifies the condition evaluated for this rule. The condition can include criteria such as
   * "satisfied", "completed", "attempted", or other learning activity states.
   */
  @JacksonXmlProperty(isAttribute = true)
  @JsonProperty("condition")
  private SequencingRuleConditionType condition;

  /**
   * Default constructor for the RuleCondition class.
   */
  @SuppressWarnings("unused")
  public RuleCondition() {
    // Default constructor
  }
}
