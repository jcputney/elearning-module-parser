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
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import java.io.Serializable;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * Represents a mapping from a local objective to a global objective within the SCORM IMS Simple
 * Sequencing schema.
 * <p>Objective mappings allow local objectives to share satisfaction and progress
 * information with global objectives, providing consistency in tracking and reporting across
 * different activities.</p>
 */
@JsonFormat(with = JsonFormat.Feature.ACCEPT_CASE_INSENSITIVE_PROPERTIES)
public class Scorm2004ObjectiveMapping implements Serializable {

  /**
   * The unique identifier for the target global objective that this local objective is mapped to.
   * This identifier is provided as a URI, linking the local objective to a broader learning goal.
   *
   * <p>The <code>targetObjectiveID</code> must be specified as a URI to ensure it is unique
   * and identifiable across the SCORM package.</p>
   */
  @JacksonXmlProperty(isAttribute = true)
  @JsonProperty("targetObjectiveID")
  private String targetObjectiveID;
  /**
   * Specifies whether this local objective should read the satisfaction status of the target global
   * objective.
   * <p>When set to <code>true</code>, this objective reads the satisfaction status from the
   * target global objective, allowing the local objective to reflect the global status.</p>
   *
   * <p>Defaults to <code>true</code>.</p>
   */
  @JacksonXmlProperty(isAttribute = true)
  @JsonProperty("readSatisfiedStatus")
  private boolean readSatisfiedStatus = true;
  /**
   * Specifies whether this local objective should read the normalized measure (performance level)
   * of the target global objective.
   * <p>When set to <code>true</code>, this objective reads the performance measure from the target
   * global objective.</p>
   *
   * <p>Defaults to <code>true</code>.</p>
   */
  @JacksonXmlProperty(isAttribute = true)
  @JsonProperty("readNormalizedMeasure")
  private boolean readNormalizedMeasure = true;
  /**
   * Specifies whether this local objective should write its satisfaction status to the target
   * global objective.
   * <p>When set to <code>true</code>, the satisfaction status of this objective is
   * shared with the target global objective, allowing the global objective to reflect this local
   * objective’s satisfaction.</p>
   *
   * <p>Defaults to <code>false</code>.</p>
   */
  @JacksonXmlProperty(isAttribute = true)
  @JsonProperty("writeSatisfiedStatus")
  private boolean writeSatisfiedStatus = false;
  /**
   * Specifies whether this local objective should write its normalized measure (performance level)
   * to the target global objective.
   * <p>When set to <code>true</code>, the performance measure of this
   * objective is shared with the target global objective.</p>
   *
   * <p>Defaults to <code>false</code>.</p>
   */
  @JacksonXmlProperty(isAttribute = true)
  @JsonProperty("writeNormalizedMeasure")
  private boolean writeNormalizedMeasure = false;

  /**
   * Specifies whether this local objective should read the completion status of the target global
   * objective.
   * <p>When set to <code>true</code>, this objective reads the completion status from the
   * target global objective, allowing the local objective to reflect the global completion
   * status.</p>
   *
   * <p>This field was introduced in SCORM 2004 4th Edition.</p>
   *
   * <p>Defaults to <code>true</code>.</p>
   */
  @JacksonXmlProperty(isAttribute = true)
  @JsonProperty("readCompletionStatus")
  private boolean readCompletionStatus = true;

  /**
   * Specifies whether this local objective should write its completion status to the target global
   * objective.
   * <p>When set to <code>true</code>, the completion status of this objective is
   * shared with the target global objective, allowing the global objective to reflect this local
   * objective's completion status.</p>
   *
   * <p>This field was introduced in SCORM 2004 4th Edition.</p>
   *
   * <p>Defaults to <code>false</code>.</p>
   */
  @JacksonXmlProperty(isAttribute = true)
  @JsonProperty("writeCompletionStatus")
  private boolean writeCompletionStatus = false;

  public Scorm2004ObjectiveMapping(String targetObjectiveID, boolean readSatisfiedStatus,
      boolean readNormalizedMeasure, boolean writeSatisfiedStatus, boolean writeNormalizedMeasure,
      boolean readCompletionStatus, boolean writeCompletionStatus) {
    this.targetObjectiveID = targetObjectiveID;
    this.readSatisfiedStatus = readSatisfiedStatus;
    this.readNormalizedMeasure = readNormalizedMeasure;
    this.writeSatisfiedStatus = writeSatisfiedStatus;
    this.writeNormalizedMeasure = writeNormalizedMeasure;
    this.readCompletionStatus = readCompletionStatus;
    this.writeCompletionStatus = writeCompletionStatus;
  }

  public Scorm2004ObjectiveMapping() {
  }

  public String getTargetObjectiveID() {
    return this.targetObjectiveID;
  }

  public void setTargetObjectiveID(String targetObjectiveID) {
    this.targetObjectiveID = targetObjectiveID;
  }

  public boolean isReadSatisfiedStatus() {
    return this.readSatisfiedStatus;
  }

  public void setReadSatisfiedStatus(boolean readSatisfiedStatus) {
    this.readSatisfiedStatus = readSatisfiedStatus;
  }

  public boolean isReadNormalizedMeasure() {
    return this.readNormalizedMeasure;
  }

  public void setReadNormalizedMeasure(boolean readNormalizedMeasure) {
    this.readNormalizedMeasure = readNormalizedMeasure;
  }

  public boolean isWriteSatisfiedStatus() {
    return this.writeSatisfiedStatus;
  }

  public void setWriteSatisfiedStatus(boolean writeSatisfiedStatus) {
    this.writeSatisfiedStatus = writeSatisfiedStatus;
  }

  public boolean isWriteNormalizedMeasure() {
    return this.writeNormalizedMeasure;
  }

  public void setWriteNormalizedMeasure(boolean writeNormalizedMeasure) {
    this.writeNormalizedMeasure = writeNormalizedMeasure;
  }

  public boolean isReadCompletionStatus() {
    return this.readCompletionStatus;
  }

  public void setReadCompletionStatus(boolean readCompletionStatus) {
    this.readCompletionStatus = readCompletionStatus;
  }

  public boolean isWriteCompletionStatus() {
    return this.writeCompletionStatus;
  }

  public void setWriteCompletionStatus(boolean writeCompletionStatus) {
    this.writeCompletionStatus = writeCompletionStatus;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (!(o instanceof Scorm2004ObjectiveMapping that)) {
      return false;
    }

    return new EqualsBuilder()
        .append(isReadSatisfiedStatus(),
            that.isReadSatisfiedStatus())
        .append(isReadNormalizedMeasure(), that.isReadNormalizedMeasure())
        .append(isWriteSatisfiedStatus(), that.isWriteSatisfiedStatus())
        .append(isWriteNormalizedMeasure(), that.isWriteNormalizedMeasure())
        .append(isReadCompletionStatus(), that.isReadCompletionStatus())
        .append(isWriteCompletionStatus(), that.isWriteCompletionStatus())
        .append(getTargetObjectiveID(), that.getTargetObjectiveID())
        .isEquals();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder(17, 37)
        .append(getTargetObjectiveID())
        .append(isReadSatisfiedStatus())
        .append(isReadNormalizedMeasure())
        .append(isWriteSatisfiedStatus())
        .append(isWriteNormalizedMeasure())
        .append(isReadCompletionStatus())
        .append(isWriteCompletionStatus())
        .toHashCode();
  }
}
