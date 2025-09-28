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

import static dev.jcputney.elearning.parser.input.scorm2004.IMSSS.NAMESPACE_URI;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import java.io.Serializable;
import java.util.List;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * Represents a collection of sequencing elements within the SCORM IMS Simple Sequencing schema. A
 * sequencing collection groups multiple {@link Sequencing} definitions, each of which can specify
 * rules, objectives, and rollup behaviors for a learning activity.
 */
@JsonFormat(with = JsonFormat.Feature.ACCEPT_CASE_INSENSITIVE_PROPERTIES)
public class SequencingCollection implements Serializable {

  /**
   * A list of {@link Sequencing} elements within the sequencing collection. Each sequencing element
   * defines navigation, rollup, and tracking settings for a specific set of learning activities.
   *
   * <p>These sequencing definitions are used by the LMS to control the flow of activities,
   * based on rules defined for completion, satisfaction, and objectives.</p>
   */
  @JacksonXmlElementWrapper(localName = "sequencing", useWrapping = false)
  @JacksonXmlProperty(localName = "sequencing", namespace = NAMESPACE_URI)
  private List<Sequencing> sequencingList;

  public SequencingCollection() {
    // no-op
  }

  public List<Sequencing> getSequencingList() {
    return this.sequencingList;
  }

  public void setSequencingList(List<Sequencing> sequencingList) {
    this.sequencingList = sequencingList;
  }

  /**
   * Resolves and retrieves the {@link DeliveryControls} associated with the given sequencing
   * identifier. This method searches through the list of {@link Sequencing} elements within the
   * sequencing collection and returns the delivery controls of the matching sequence, if found.
   *
   * @param sequenceId the identifier of the sequencing element for which the delivery controls need
   * to be resolved. Must not be null or blank.
   * @return the {@link DeliveryControls} associated with the specified sequence identifier, or null
   * if no matching sequencing element or delivery controls are found.
   */
  public DeliveryControls resolveDeliveryControlsById(String sequenceId) {
    if (sequenceId == null || sequenceId.isBlank()) {
      return null;
    }
    if (sequencingList == null || sequencingList.isEmpty()) {
      return null;
    }
    for (Sequencing sequencing : sequencingList) {
      if (sequencing != null && sequenceId.equals(sequencing.getId())
          && sequencing.getDeliveryControls() != null) {
        return sequencing.getDeliveryControls();
      }
    }
    return null;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (!(o instanceof SequencingCollection that)) {
      return false;
    }

    return new EqualsBuilder()
        .append(getSequencingList(), that.getSequencingList())
        .isEquals();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder(17, 37)
        .append(getSequencingList())
        .toHashCode();
  }
}
