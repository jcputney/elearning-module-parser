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

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import dev.jcputney.elearning.parser.input.scorm2004.IMSSS;
import java.io.Serializable;
import java.util.List;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * Represents a collection of auxiliary resources associated with a learning activity within the
 * SCORM IMS Simple Sequencing (IMSSS) schema. Auxiliary resources are additional resources that
 * support the main content of the activity, such as supplementary documents, tools, or media
 * files.
 *
 * <p>Auxiliary resources are intended to enhance the learning experience by providing extra
 * materials that are accessible alongside the primary content. They do not directly contribute to
 * the activity's objectives or completion status but serve as helpful references or optional tools
 * for the learner.</p>
 *
 * <p>This class holds a list of individual {@link AuxiliaryResource} objects, each of which
 * describes a specific auxiliary resource associated with the learning activity.</p>
 *
 * <p>The IMSSS namespace is specified by {@link IMSSS#NAMESPACE_URI}, following the SCORM 2004
 * standards for auxiliary resource management in sequencing and navigation.</p>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonFormat(with = JsonFormat.Feature.ACCEPT_CASE_INSENSITIVE_PROPERTIES)
public final class AuxiliaryResources implements Serializable {

  /**
   * A list of auxiliary resources associated with the learning activity. Each resource provides
   * additional, optional content for the learner and is represented by an instance of
   * {@link AuxiliaryResource}.
   *
   * <p>If no auxiliary resources are defined, this list may be empty.</p>
   */
  @JacksonXmlElementWrapper(useWrapping = false)
  @JacksonXmlProperty(localName = "auxiliaryResource", namespace = IMSSS.NAMESPACE_URI)
  private List<AuxiliaryResource> auxiliaryResourceList;

  /**
   * Default constructor for the AuxiliaryResources class. This constructor initializes a new
   * instance of AuxiliaryResources without setting any fields or performing additional operations.
   */
  public AuxiliaryResources() {
    // no-op
  }

  /**
   * Retrieves the list of auxiliary resources associated with the learning activity. Each auxiliary
   * resource provides additional, optional content for the learner and is represented by an
   * instance of {@link AuxiliaryResource}.
   *
   * @return a list of {@link AuxiliaryResource} objects representing the auxiliary resources
   * associated with the learning activity, or an empty list if no auxiliary resources are defined
   */
  public List<AuxiliaryResource> getAuxiliaryResourceList() {
    return this.auxiliaryResourceList;
  }

  /**
   * Sets the list of auxiliary resources associated with the learning activity. Each auxiliary
   * resource provides additional, optional content for the learner and is represented by an
   * instance of {@link AuxiliaryResource}.
   *
   * @param auxiliaryResourceList a list of {@link AuxiliaryResource} objects representing the
   * auxiliary resources to associate with the learning activity
   */
  public void setAuxiliaryResourceList(
      List<AuxiliaryResource> auxiliaryResourceList) {
    this.auxiliaryResourceList = auxiliaryResourceList;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (!(o instanceof AuxiliaryResources that)) {
      return false;
    }

    return new EqualsBuilder()
        .append(getAuxiliaryResourceList(),
            that.getAuxiliaryResourceList())
        .isEquals();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder(17, 37)
        .append(getAuxiliaryResourceList())
        .toHashCode();
  }
}
