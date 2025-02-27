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

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import dev.jcputney.elearning.parser.input.scorm2004.ims.ss.types.RollupActionType;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;

/**
 * Represents the action to perform if the conditions specified in a rollup rule are met. The rollup
 * action determines how the statuses of child activities affect the parent activity’s rollup
 * status, allowing for complex aggregation logic.
 *
 * <p>Common actions include marking the parent activity as satisfied, not satisfied,
 * completed, or incomplete based on the rollup rule conditions.</p>
 */
@Builder
@Getter
@Jacksonized
@JsonFormat(with = JsonFormat.Feature.ACCEPT_CASE_INSENSITIVE_PROPERTIES)
public class RollupAction {

  /**
   * Specifies the action to be taken for this rollup rule when the conditions are met. Possible
   * values include:
   * <ul>
   *   <li><strong>SATISFIED:</strong> Marks the activity as satisfied.</li>
   *   <li><strong>NOT_SATISFIED:</strong> Marks the activity as not satisfied.</li>
   *   <li><strong>COMPLETED:</strong> Marks the activity as completed.</li>
   *   <li><strong>INCOMPLETE:</strong> Marks the activity as incomplete.</li>
   * </ul>
   */
  @JacksonXmlProperty(isAttribute = true)
  @JsonProperty("action")
  private RollupActionType action;
}
