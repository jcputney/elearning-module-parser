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
import dev.jcputney.elearning.parser.input.scorm2004.ims.ss.types.RollupActionType;
import java.io.Serializable;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * Represents the action to perform if the conditions specified in a rollup rule are met. The rollup
 * action determines how the statuses of child activities affect the parent activity’s rollup
 * status, allowing for complex aggregation logic.
 *
 * <p>Common actions include marking the parent activity as satisfied, not satisfied,
 * completed, or incomplete based on the rollup rule conditions.</p>
 */
@JsonFormat(with = JsonFormat.Feature.ACCEPT_CASE_INSENSITIVE_PROPERTIES)
public final class RollupAction implements Serializable {

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

  public RollupAction(RollupActionType action) {
    this.action = action;
  }

  public RollupAction() {
    // no-op
  }

  /**
   * Retrieves the current rollup action type associated with this instance.
   *
   * @return the {@link RollupActionType} representing the action to be performed if the rollup
   * rule's conditions are met.
   */
  public RollupActionType getAction() {
    return this.action;
  }

  /**
   * Sets the rollup action to be performed when the rollup rule conditions are met.
   *
   * @param action the {@link RollupActionType} defining the specific action to be taken, such as
   * marking the activity as satisfied, not satisfied, completed, or incomplete.
   */
  public void setAction(RollupActionType action) {
    this.action = action;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (!(o instanceof RollupAction that)) {
      return false;
    }

    return new EqualsBuilder()
        .append(getAction(), that.getAction())
        .isEquals();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder(17, 37)
        .append(getAction())
        .toHashCode();
  }
}
