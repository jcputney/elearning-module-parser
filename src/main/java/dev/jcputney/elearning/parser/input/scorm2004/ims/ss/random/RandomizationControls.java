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

package dev.jcputney.elearning.parser.input.scorm2004.ims.ss.random;

import static lombok.AccessLevel.PRIVATE;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import dev.jcputney.elearning.parser.input.scorm2004.IMSSS;
import dev.jcputney.elearning.parser.input.scorm2004.ims.ss.types.RandomizationTiming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * Represents the randomization controls for a learning activity within the SCORM IMS Simple
 * Sequencing (IMSSS) schema. Randomization controls define how and when child activities within a
 * sequence are randomized for the learner.
 *
 * <p>Randomization controls can help to increase the variability of the learning experience by
 * allowing activities to be reordered or selectively presented. For example, they can be used to
 * shuffle assessment questions or to ensure that the learner sees different content each time they
 * access the activity.</p>
 *
 * <p>The randomization controls include settings for:</p>
 * <ul>
 *   <li>Randomization timing – determining when randomization occurs (e.g., on each new attempt).</li>
 *   <li>Selection timing – controlling when activities are selected for presentation.</li>
 *   <li>Child reordering – allowing the order of child activities to be changed.</li>
 *   <li>Selection count – specifying how many child activities are selected from the available pool.</li>
 * </ul>
 *
 * <p>The IMSSS namespace is specified by {@link IMSSS#NAMESPACE_URI}, following the SCORM 2004 standards
 * for sequencing and navigation.</p>
 */
@Builder
@Getter
@Jacksonized
@AllArgsConstructor(access = PRIVATE)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonFormat(with = JsonFormat.Feature.ACCEPT_CASE_INSENSITIVE_PROPERTIES)
public class RandomizationControls {

  /**
   * Specifies when randomization of child activities should occur. Possible values include:
   * <ul>
   *   <li><strong>NEVER:</strong> No randomization is applied.</li>
   *   <li><strong>ONCE:</strong> Randomization occurs only once, typically on the first attempt.</li>
   *   <li><strong>ON_EACH_NEW_ATTEMPT:</strong> Randomization occurs each time the learner makes a new attempt.</li>
   * </ul>
   */
  @JacksonXmlProperty(isAttribute = true)
  @JsonProperty("randomizationTiming")
  @Default
  private RandomizationTiming randomizationTiming = RandomizationTiming.NEVER;
  /**
   * Specifies the timing of selection for child activities within the sequence.
   */
  @JacksonXmlProperty(isAttribute = true)
  @JsonProperty("selectionTiming")
  @Default
  private RandomizationTiming selectionTiming = RandomizationTiming.NEVER;
  /**
   * Indicates whether the order of child activities within the sequence can be changed. When set
   * to
   * <code>true</code>, the child activities are allowed to be reordered, typically as part of a
   * randomization or shuffling process.
   *
   * <p>This attribute enables instructional designers to create a more varied learning
   * experience by rearranging the sequence of activities, especially useful in assessment scenarios
   * where question order needs to be shuffled.</p>
   *
   * <p>Defaults to <code>false</code>.</p>
   */
  @JacksonXmlProperty(isAttribute = true)
  @JsonProperty("reorderChildren")
  @Default
  private boolean reorderChildren = false;
  /**
   * Specifies the number of child activities to be selected from the available set. This attribute
   * enables partial selection from a larger pool, allowing only a subset of activities to be
   * presented to the learner.
   *
   * <p>If <code>selectCount</code> is not specified, all available child activities are presented.
   * When specified, the system selects the given number of child activities at random based on the
   * other randomization settings.</p>
   */
  @JacksonXmlProperty(isAttribute = true)
  @JsonProperty("selectCount")
  private Integer selectCount;

  /**
   * Default constructor for the RandomizationControls class.
   */
  @SuppressWarnings("unused")
  public RandomizationControls() {
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

    RandomizationControls that = (RandomizationControls) o;

    return new EqualsBuilder()
        .append(reorderChildren, that.reorderChildren)
        .append(randomizationTiming, that.randomizationTiming)
        .append(selectionTiming, that.selectionTiming)
        .append(selectCount, that.selectCount)
        .isEquals();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder(17, 37)
        .append(randomizationTiming)
        .append(selectionTiming)
        .append(reorderChildren)
        .append(selectCount)
        .toHashCode();
  }
}
