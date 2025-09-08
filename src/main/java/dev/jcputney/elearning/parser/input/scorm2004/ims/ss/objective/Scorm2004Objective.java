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
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import dev.jcputney.elearning.parser.input.scorm2004.IMSSS;
import java.io.Serializable;
import java.util.List;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * Represents an individual learning objective within the SCORM IMS Simple Sequencing schema.
 * Objectives specify criteria such as minimum required performance, satisfaction requirements, and
 * mappings to global objectives for tracking learner progress.
 */
@JsonFormat(with = JsonFormat.Feature.ACCEPT_CASE_INSENSITIVE_PROPERTIES)
public class Scorm2004Objective implements Serializable {

  /**
   * A unique identifier for the objective, provided as a URI. This identifier distinguishes the
   * objective and allows it to be referenced in sequencing and navigation rules.
   */
  @JacksonXmlProperty(isAttribute = true)
  @JsonProperty("objectiveID")
  private String objectiveID;
  /**
   * satisfiedByMeasure is a boolean value that indicates whether the objective is satisfied based
   * on the measure value.
   * <p>If `true`, the objective is satisfied when the measure value is greater than or equal to
   * the minNormalizedMeasure.</p>
   * <p>If `false`, the objective is satisfied when the learner has completed the objective,
   * regardless of the measure value.</p>
   */
  @JacksonXmlProperty(isAttribute = true)
  @JsonProperty("satisfiedByMeasure")
  private Boolean satisfiedByMeasure = false;
  /**
   * The minimum normalized measure required to consider this objective as satisfied. This value
   * represents the minimum level of achievement, typically as a decimal between 0 and 1, where 1.0
   * represents full satisfaction.
   *
   * <p>If not specified, the objective may be satisfied based on other criteria or defaults.</p>
   */
  @JacksonXmlProperty(localName = "minNormalizedMeasure", namespace = IMSSS.NAMESPACE_URI)
  private Double minNormalizedMeasure;
  /**
   * A list of mappings to global objectives. These mappings associate this local objective with
   * broader goals or learning objectives across multiple activities or sequences.
   *
   * <p>Each mapping is represented by an instance of {@link Scorm2004ObjectiveMapping}, allowing
   * control over whether specific aspects, such as satisfaction and measure, are shared between the
   * local and global objectives.</p>
   */
  @JacksonXmlElementWrapper(useWrapping = false)
  @JacksonXmlProperty(localName = "mapInfo", namespace = IMSSS.NAMESPACE_URI)
  private List<Scorm2004ObjectiveMapping> mapInfo;

  public Scorm2004Objective(String objectiveID, Boolean satisfiedByMeasure,
      Double minNormalizedMeasure, List<Scorm2004ObjectiveMapping> mapInfo) {
    this.objectiveID = objectiveID;
    this.satisfiedByMeasure = satisfiedByMeasure;
    this.minNormalizedMeasure = minNormalizedMeasure;
    this.mapInfo = mapInfo;
  }

  public Scorm2004Objective() {
  }

  public String getObjectiveID() {
    return this.objectiveID;
  }

  public void setObjectiveID(String objectiveID) {
    this.objectiveID = objectiveID;
  }

  public Boolean getSatisfiedByMeasure() {
    return this.satisfiedByMeasure;
  }

  public void setSatisfiedByMeasure(Boolean satisfiedByMeasure) {
    this.satisfiedByMeasure = satisfiedByMeasure;
  }

  public Double getMinNormalizedMeasure() {
    return this.minNormalizedMeasure;
  }

  public void setMinNormalizedMeasure(Double minNormalizedMeasure) {
    this.minNormalizedMeasure = minNormalizedMeasure;
  }

  public List<Scorm2004ObjectiveMapping> getMapInfo() {
    return this.mapInfo;
  }

  public void setMapInfo(
      List<Scorm2004ObjectiveMapping> mapInfo) {
    this.mapInfo = mapInfo;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (!(o instanceof Scorm2004Objective that)) {
      return false;
    }

    return new EqualsBuilder()
        .append(getObjectiveID(), that.getObjectiveID())
        .append(getSatisfiedByMeasure(), that.getSatisfiedByMeasure())
        .append(getMinNormalizedMeasure(), that.getMinNormalizedMeasure())
        .append(getMapInfo(), that.getMapInfo())
        .isEquals();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder(17, 37)
        .append(getObjectiveID())
        .append(getSatisfiedByMeasure())
        .append(getMinNormalizedMeasure())
        .append(getMapInfo())
        .toHashCode();
  }
}
