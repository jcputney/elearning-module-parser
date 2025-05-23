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
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import dev.jcputney.elearning.parser.input.scorm2004.IMSSS;
import dev.jcputney.elearning.parser.input.scorm2004.ims.ss.types.ConditionCombinationType;
import java.io.Serializable;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.jackson.Jacksonized;

/**
 * Represents a set of conditions that must be met for a sequencing rule to apply. Rule conditions
 * define specific criteria, such as completion status or objective measure, that determine whether
 * a rule's action should be executed.
 *
 * <p>The rule conditions are combined based on the specified combination logic,
 * such as "all" (all conditions must be met) or "any" (at least one condition must be met).</p>
 */
@Builder
@Getter
@Jacksonized
@NoArgsConstructor
@EqualsAndHashCode(doNotUseGetters = true)
@AllArgsConstructor(access = PRIVATE)
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
  @Default
  private ConditionCombinationType conditionCombination = ConditionCombinationType.ANY;
}
