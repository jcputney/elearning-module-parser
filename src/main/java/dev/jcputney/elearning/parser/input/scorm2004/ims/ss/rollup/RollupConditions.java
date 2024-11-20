/*
 * Copyright (c) 2024. Jonathan Putney
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

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import dev.jcputney.elearning.parser.input.scorm2004.IMSSS;
import java.util.List;
import lombok.Data;

/**
 * Represents a set of conditions that control when a rollup rule should apply. Rollup conditions
 * define specific criteria based on the statuses of child activities, such as completion,
 * satisfaction, and other properties that affect the rollup logic.
 *
 * <p>Conditions can be combined based on the logic defined in {@link #conditionCombination},
 * allowing multiple criteria to be evaluated together.</p>
 */
@Data
public class RollupConditions {

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
  private String conditionCombination = "any";
}
