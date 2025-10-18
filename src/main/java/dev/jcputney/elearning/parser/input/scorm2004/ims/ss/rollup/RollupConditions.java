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
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import dev.jcputney.elearning.parser.input.scorm2004.IMSSS;
import java.io.Serializable;
import java.util.List;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * Represents a set of conditions that control when a rollup rule should apply. Rollup conditions
 * define specific criteria based on the statuses of child activities, such as completion,
 * satisfaction, and other properties that affect the rollup logic.
 *
 * <p>Conditions can be combined based on the logic defined in {@link #conditionCombination},
 * allowing multiple criteria to be evaluated together.</p>
 */
@JsonFormat(with = JsonFormat.Feature.ACCEPT_CASE_INSENSITIVE_PROPERTIES)
public final class RollupConditions implements Serializable {

  /**
   * A list of individual rollup conditions. Each condition specifies a criterion, such as
   * completion or satisfaction, that must be met for the rollup rule to apply.
   */
  @JacksonXmlElementWrapper(useWrapping = false)
  @JacksonXmlProperty(localName = "rollupCondition", namespace = IMSSS.NAMESPACE_URI)
  private List<RollupCondition> rollupConditionList;

  /**
   * The logic used to combine the conditions in {@code rollupConditionList}. Possible values are:
   * <ul>
   *   <li><strong>all:</strong> All conditions must be met for the rollup rule to apply.</li>
   *   <li><strong>any:</strong> At least one condition must be met for the rollup rule to apply.</li>
   * </ul>
   * <p>Defaults to <code>any</code> if not specified.</p>
   */
  @JacksonXmlProperty(isAttribute = true)
  @JsonProperty("conditionCombination")
  private String conditionCombination = "any";

  /**
   * Default constructor for the RollupConditions class.
   * <p>
   * This no-argument constructor initializes an instance of the RollupConditions class without
   * setting any fields or performing any specific actions. It can be used to create an empty
   * instance of the class for further configuration or use.
   */
  public RollupConditions() {
    // no-op
  }

  /**
   * Retrieves the list of rollup conditions associated with this instance. Each rollup condition
   * defines a specific criterion, such as completion or satisfaction status, that determines
   * whether a rollup rule should be applied.
   *
   * @return a list of {@link RollupCondition} objects representing the rollup conditions.
   */
  public List<RollupCondition> getRollupConditionList() {
    return this.rollupConditionList;
  }

  /**
   * Sets the list of rollup conditions for this instance. The provided list contains individual
   * conditions that define specific criteria, such as completion or satisfaction status, which
   * determine whether the rollup logic should be applied.
   *
   * @param rollupConditionList the list of {@link RollupCondition} objects to set, representing the
   * criteria for applying the rollup rule.
   */
  public void setRollupConditionList(List<RollupCondition> rollupConditionList) {
    this.rollupConditionList = rollupConditionList;
  }

  /**
   * Retrieves the logical combination of conditions associated with this instance. The combination
   * specifies how the individual rollup conditions within the list are evaluated together to
   * determine the overall rule's application.
   *
   * @return a string representing the logical combination of rollup conditions.
   */
  public String getConditionCombination() {
    return this.conditionCombination;
  }

  /**
   * Sets the logical combination of rollup conditions for this instance. The combination defines
   * how individual rollup conditions are evaluated together to determine the overall application's
   * rule logic.
   *
   * @param conditionCombination the logical combination of rollup conditions, specified as a string
   * representation (e.g., "ALL", "ANY", etc.).
   */
  public void setConditionCombination(String conditionCombination) {
    this.conditionCombination = conditionCombination;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (!(o instanceof RollupConditions that)) {
      return false;
    }

    return new EqualsBuilder()
        .append(getRollupConditionList(), that.getRollupConditionList())
        .append(getConditionCombination(), that.getConditionCombination())
        .isEquals();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder(17, 37)
        .append(getRollupConditionList())
        .append(getConditionCombination())
        .toHashCode();
  }
}
