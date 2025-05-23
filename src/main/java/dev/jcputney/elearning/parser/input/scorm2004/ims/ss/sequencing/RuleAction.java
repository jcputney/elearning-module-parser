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
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * Represents the action to perform if the conditions specified in a sequencing rule are met. Rule
 * actions determine the sequencing behavior for the activity, such as enabling, disabling, hiding,
 * or skipping an activity.
 *
 * <p>Actions are only executed if the conditions in the rule's {@link RuleConditions} are
 * met.</p>
 */
@Builder
@Getter
@Jacksonized
@AllArgsConstructor(access = PRIVATE)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonFormat(with = JsonFormat.Feature.ACCEPT_CASE_INSENSITIVE_PROPERTIES)
public class RuleAction implements Serializable {

  /**
   * The action to be taken if the conditions are met. Possible actions include:
   * <ul>
   *   <li><strong>skip:</strong> Skip the activity and proceed to the next available activity.</li>
   *   <li><strong>disabled:</strong> Disable the activity, making it unavailable to the learner.</li>
   *   <li><strong>hiddenFromChoice:</strong> Hide the activity from the learner's choice menu.</li>
   *   <li><strong>stopForwardTraversal:</strong> Prevent the learner from progressing forward beyond the activity.</li>
   * </ul>
   */
  @JacksonXmlProperty(isAttribute = true)
  @JsonProperty("action")
  private String action;

  /**
   * Default constructor for the RuleAction class.
   */
  @SuppressWarnings("unused")
  public RuleAction() {
    // Default constructor
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    RuleAction that = (RuleAction) o;

    return new EqualsBuilder()
        .append(action, that.action)
        .isEquals();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder(17, 37)
        .append(action)
        .toHashCode();
  }
}
