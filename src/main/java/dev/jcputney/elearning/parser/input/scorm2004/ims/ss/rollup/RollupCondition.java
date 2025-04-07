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

import static lombok.AccessLevel.PRIVATE;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import dev.jcputney.elearning.parser.input.scorm2004.ims.ss.types.ConditionOperatorType;
import dev.jcputney.elearning.parser.input.scorm2004.ims.ss.types.RollupRuleConditionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;

/**
 * Represents an individual rollup condition within a set of rollup conditions. Each condition
 * specifies a specific criterion, such as completion or satisfaction status, that affects whether
 * the associated rollup rule is applied.
 */
@Builder
@Getter
@Jacksonized
@AllArgsConstructor(access = PRIVATE)
@JsonFormat(with = JsonFormat.Feature.ACCEPT_CASE_INSENSITIVE_PROPERTIES)
public class RollupCondition {

  /**
   * Specifies the operator to use when evaluating the rollup condition. Possible values include:
   * <ul>
   *   <li><strong>NOT:</strong> Inverts the evaluation result of the condition.</li>
   *   <li><strong>NO_OP:</strong> Applies the condition as-is, without modification.</li>
   * </ul>
   */
  @JacksonXmlProperty(isAttribute = true)
  @JsonProperty("operator")
  @Default
  private ConditionOperatorType operator = ConditionOperatorType.NO_OP;
  /**
   * Specifies the specific condition being evaluated in this rollup condition. The condition may
   * include criteria related to the learner's progress or completion status of activities.
   */
  @JacksonXmlProperty(isAttribute = true)
  @JsonProperty("condition")
  private RollupRuleConditionType condition;

  /**
   * Default constructor for the RollupCondition class.
   */
  @SuppressWarnings("unused")
  public RollupCondition() {
    // Default constructor
  }
}
