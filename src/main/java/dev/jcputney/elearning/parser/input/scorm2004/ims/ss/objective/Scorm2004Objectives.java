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

package dev.jcputney.elearning.parser.input.scorm2004.ims.ss.objective;

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
 * Represents the learning objectives for an activity within the SCORM IMS Simple Sequencing (IMSSS)
 * schema. Objectives define specific learning goals or criteria that are associated with a learning
 * activity and may contribute to the activity’s completion and satisfaction status.
 *
 * <p>The objectives for an activity are classified into a primary objective, which contributes
 * to rollup and tracking of learner progress, and additional objectives that may serve as
 * supporting or secondary goals.</p>
 *
 * <p>Objectives typically include settings such as the minimum measure required for success,
 * satisfaction criteria, and mappings to global objectives. These settings allow for detailed
 * control over how progress is assessed and reported in the LMS.</p>
 *
 * <p>The IMSSS namespace is specified by {@link IMSSS#NAMESPACE_URI}, following the SCORM 2004
 * standards for sequencing and navigation.</p>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonFormat(with = JsonFormat.Feature.ACCEPT_CASE_INSENSITIVE_PROPERTIES)
public final class Scorm2004Objectives implements Serializable {

  /**
   * The primary objective of the activity. This objective typically contributes to the rollup of
   * progress and satisfaction for the activity, affecting the learner’s overall progress in the
   * sequence.
   *
   * <p>The primary objective is usually a required learning goal that must be achieved
   * to consider the activity successfully completed.</p>
   */
  @JacksonXmlProperty(localName = "primaryObjective", namespace = IMSSS.NAMESPACE_URI)
  private Scorm2004Objective primaryObjective;

  /**
   * A list of additional objectives for the activity. These objectives may serve as supplementary
   * goals that are tracked but do not necessarily affect the rollup of progress or satisfaction.
   *
   * <p>Each objective in this list is represented by an instance of {@link Scorm2004Objective},
   * allowing for fine-grained tracking and evaluation of learner performance.</p>
   */
  @JacksonXmlElementWrapper(localName = "objective", useWrapping = false)
  @JacksonXmlProperty(localName = "objective", namespace = IMSSS.NAMESPACE_URI)
  private List<Scorm2004Objective> objectiveList;

  /**
   * Constructs a `Scorm2004Objectives` object with a primary objective and a list of additional
   * objectives.
   *
   * @param primaryObjective the primary learning objective for this instance, represented as a
   * Scorm2004Objective object.
   * @param objectiveList a list of additional learning objectives associated with this instance,
   * provided as a List of Scorm2004Objective objects.
   */
  public Scorm2004Objectives(Scorm2004Objective primaryObjective,
      List<Scorm2004Objective> objectiveList) {
    this.primaryObjective = primaryObjective;
    this.objectiveList = objectiveList;
  }

  /**
   * Default constructor for the Scorm2004Objectives class.
   * <p>
   * This constructor initializes an instance of Scorm2004Objectives with no primary objective or
   * additional objectives. Typically used when initial values will be set manually after
   * instantiation.
   */
  public Scorm2004Objectives() {
    // no-op
  }

  /**
   * Retrieves the primary objective associated with the instance.
   *
   * @return The primary objective as an instance of Scorm2004Objective. Returns null if no primary
   * objective is defined.
   */
  public Scorm2004Objective getPrimaryObjective() {
    return this.primaryObjective;
  }

  /**
   * Sets the primary objective for the `Scorm2004Objectives` instance.
   *
   * @param primaryObjective the primary learning objective to be associated with this instance,
   * provided as an instance of `Scorm2004Objective`
   */
  public void setPrimaryObjective(
      Scorm2004Objective primaryObjective) {
    this.primaryObjective = primaryObjective;
  }

  /**
   * Retrieves the list of SCORM 2004 objectives associated with this instance.
   *
   * @return A List of Scorm2004Objective objects representing the objectives. Returns an empty list
   * if no objectives are defined.
   */
  public List<Scorm2004Objective> getObjectiveList() {
    return this.objectiveList;
  }

  /**
   * Sets the list of SCORM 2004 objectives associated with this instance.
   *
   * @param objectiveList a List of Scorm2004Objective objects to be assigned as the objectives for
   * this instance.
   */
  public void setObjectiveList(List<Scorm2004Objective> objectiveList) {
    this.objectiveList = objectiveList;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (!(o instanceof Scorm2004Objectives that)) {
      return false;
    }

    return new EqualsBuilder()
        .append(getPrimaryObjective(), that.getPrimaryObjective())
        .append(getObjectiveList(), that.getObjectiveList())
        .isEquals();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder(17, 37)
        .append(getPrimaryObjective())
        .append(getObjectiveList())
        .toHashCode();
  }
}
