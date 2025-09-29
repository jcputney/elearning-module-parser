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

package dev.jcputney.elearning.parser.input.scorm2004.adl.sequencing;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import java.io.Serializable;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * Represents a mapping to a global objective or another objective within the LMS. Maps allow
 * tracking of objectives that are shared across different content items. The following schema shows
 * the structure of the mapInfoType element:
 * <pre>{@code
 *   <xs:complexType name="mapInfoType">
 *      <xs:attribute name="targetObjectiveID" use="required" type="xs:anyURI" />
 *      <xs:attribute name="readRawScore" default="true" type="xs:boolean" />
 *      <xs:attribute name="readMinScore" default="true" type="xs:boolean" />
 *      <xs:attribute name="readMaxScore" default="true" type="xs:boolean" />
 *      <xs:attribute name="readCompletionStatus" default="true" type="xs:boolean" />
 *      <xs:attribute name="readProgressMeasure" default="true" type="xs:boolean" />
 *      <xs:attribute name="writeRawScore" default="false" type="xs:boolean" />
 *      <xs:attribute name="writeMinScore" default="false" type="xs:boolean" />
 *      <xs:attribute name="writeMaxScore" default="false" type="xs:boolean" />
 *      <xs:attribute name="writeCompletionStatus" default="false" type="xs:boolean" />
 *      <xs:attribute name="writeProgressMeasure" default="false" type="xs:boolean" />
 *   </xs:complexType>
 * }</pre>
 */
@JsonFormat(with = JsonFormat.Feature.ACCEPT_CASE_INSENSITIVE_PROPERTIES)
public class MapInfo implements Serializable {

  /**
   * The unique identifier for the target objective in the LMS that this objective is mapped to.
   * This enables global tracking of the objective’s completion and satisfaction status.
   */
  @JacksonXmlProperty(isAttribute = true, localName = "targetObjectiveID")
  @JsonProperty("targetObjectiveID")
  private String targetObjectiveID;

  /**
   * Indicates whether the raw score for this objective should be read from the mapped global
   * objective.
   */
  @JacksonXmlProperty(isAttribute = true, localName = "readRawScore")
  @JsonProperty("readRawScore")
  private boolean readRawScore = true;

  /**
   * Indicates whether the minimum score for this objective should be read from the mapped global
   * objective.
   */
  @JacksonXmlProperty(isAttribute = true, localName = "readMinScore")
  @JsonProperty("readMinScore")
  private boolean readMinScore = true;

  /**
   * Indicates whether the maximum score for this objective should be read from the mapped global
   * objective.
   */
  @JacksonXmlProperty(isAttribute = true, localName = "readMaxScore")
  @JsonProperty("readMaxScore")
  private boolean readMaxScore = true;

  /**
   * Indicates whether the completion status for this objective should be read from the mapped
   * global objective.
   */
  @JacksonXmlProperty(isAttribute = true, localName = "readCompletionStatus")
  @JsonProperty("readCompletionStatus")
  private boolean readCompletionStatus = true;

  /**
   * Indicates whether the progress measure for this objective should be read from the mapped global
   * objective.
   */
  @JacksonXmlProperty(isAttribute = true, localName = "readProgressMeasure")
  @JsonProperty("readProgressMeasure")
  private boolean readProgressMeasure = true;

  /**
   * Indicates whether the raw score for this objective should be written to the mapped global
   * objective.
   */
  @JacksonXmlProperty(isAttribute = true, localName = "writeRawScore")
  @JsonProperty("writeRawScore")
  private boolean writeRawScore = false;

  /**
   * Indicates whether the minimum score for this objective should be written to the mapped global
   * objective.
   */
  @JacksonXmlProperty(isAttribute = true, localName = "writeMinScore")
  @JsonProperty("writeMinScore")
  private boolean writeMinScore = false;

  /**
   * Indicates whether the maximum score for this objective should be written to the mapped global
   * objective.
   */
  @JacksonXmlProperty(isAttribute = true, localName = "writeMaxScore")
  @JsonProperty("writeMaxScore")
  private boolean writeMaxScore = false;

  /**
   * Indicates whether the completion status for this objective should be written to the mapped
   * global objective.
   */
  @JacksonXmlProperty(isAttribute = true, localName = "writeCompletionStatus")
  @JsonProperty("writeCompletionStatus")
  private boolean writeCompletionStatus = false;

  /**
   * Indicates whether the progress measure for this objective should be written to the mapped
   * global objective.
   */
  @JacksonXmlProperty(isAttribute = true, localName = "writeProgressMeasure")
  @JsonProperty("writeProgressMeasure")
  private boolean writeProgressMeasure = false;

  public MapInfo() {
    // no-op
  }

  /**
   * Retrieves the target objective ID associated with this instance.
   *
   * @return the target objective ID, represented as a string.
   */
  public String getTargetObjectiveID() {
    return this.targetObjectiveID;
  }

  /**
   * Sets the target objective ID for this instance.
   *
   * @param targetObjectiveID the target objective ID to be set, represented as a string
   */
  public void setTargetObjectiveID(String targetObjectiveID) {
    this.targetObjectiveID = targetObjectiveID;
  }

  /**
   * Determines whether the raw score data is set to be read.
   *
   * @return true if the raw score data is configured to be read, false otherwise.
   */
  public boolean isReadRawScore() {
    return this.readRawScore;
  }

  /**
   * Sets whether the raw score data is to be read.
   *
   * @param readRawScore a boolean value indicating whether the raw score data should be read. If
   * true, the raw score data will be read; otherwise, it will not be read.
   */
  public void setReadRawScore(boolean readRawScore) {
    this.readRawScore = readRawScore;
  }

  /**
   * Determines whether the minimum score data is set to be read.
   *
   * @return true if the minimum score data is configured to be read, false otherwise.
   */
  public boolean isReadMinScore() {
    return this.readMinScore;
  }

  /**
   * Sets whether the minimum score data is to be read.
   *
   * @param readMinScore a boolean value indicating whether the minimum score data should be read.
   * If true, the minimum score data will be read; otherwise, it will not be read.
   */
  public void setReadMinScore(boolean readMinScore) {
    this.readMinScore = readMinScore;
  }

  /**
   * Determines whether the maximum score data is set to be read.
   *
   * @return true if the maximum score data is configured to be read, false otherwise.
   */
  public boolean isReadMaxScore() {
    return this.readMaxScore;
  }

  /**
   * Sets whether the maximum score data is to be read.
   *
   * @param readMaxScore a boolean value indicating whether the maximum score data should be read.
   * If true, the maximum score data will be read; otherwise, it will not be read.
   */
  public void setReadMaxScore(boolean readMaxScore) {
    this.readMaxScore = readMaxScore;
  }

  /**
   * Determines whether the completion status data is set to be read.
   *
   * @return true if the completion status data is configured to be read, false otherwise.
   */
  public boolean isReadCompletionStatus() {
    return this.readCompletionStatus;
  }

  /**
   * Sets whether the completion status data is to be read.
   *
   * @param readCompletionStatus a boolean value indicating whether the completion status data
   * should be read. If true, the completion status data will be read; otherwise, it will not be
   * read.
   */
  public void setReadCompletionStatus(boolean readCompletionStatus) {
    this.readCompletionStatus = readCompletionStatus;
  }

  /**
   * Determines whether the progress measure data is set to be read.
   *
   * @return true if the progress measure data is configured to be read, false otherwise.
   */
  public boolean isReadProgressMeasure() {
    return this.readProgressMeasure;
  }

  /**
   * Sets whether the progress measure data is to be read.
   *
   * @param readProgressMeasure a boolean value indicating whether the progress measure data should
   * be read. If true, the progress measure data will be read; otherwise, it will not be read.
   */
  public void setReadProgressMeasure(boolean readProgressMeasure) {
    this.readProgressMeasure = readProgressMeasure;
  }

  /**
   * Determines whether the raw score data is set to be written.
   *
   * @return true if the raw score data is configured to be written, false otherwise.
   */
  public boolean isWriteRawScore() {
    return this.writeRawScore;
  }

  /**
   * Sets whether the raw score data is to be written.
   *
   * @param writeRawScore a boolean value indicating whether the raw score data should be written.
   * If true, the raw score data will be written; otherwise, it will not be written.
   */
  public void setWriteRawScore(boolean writeRawScore) {
    this.writeRawScore = writeRawScore;
  }

  /**
   * Determines whether the minimum score data is set to be written.
   *
   * @return true if the minimum score data is configured to be written, false otherwise.
   */
  public boolean isWriteMinScore() {
    return this.writeMinScore;
  }

  /**
   * Sets whether the minimum score data is to be written.
   *
   * @param writeMinScore a boolean value indicating whether the minimum score data should be
   * written. If true, the minimum score data will be written; otherwise, it will not be written.
   */
  public void setWriteMinScore(boolean writeMinScore) {
    this.writeMinScore = writeMinScore;
  }

  /**
   * Determines whether the maximum score data is set to be written.
   *
   * @return true if the maximum score data is configured to be written, false otherwise.
   */
  public boolean isWriteMaxScore() {
    return this.writeMaxScore;
  }

  /**
   * Sets whether the maximum score data is to be written.
   *
   * @param writeMaxScore a boolean value indicating whether the maximum score data should be
   * written. If true, the maximum score data will be written; otherwise, it will not be written.
   */
  public void setWriteMaxScore(boolean writeMaxScore) {
    this.writeMaxScore = writeMaxScore;
  }

  /**
   * Determines whether the completion status data is set to be written.
   *
   * @return true if the completion status data is configured to be written, false otherwise.
   */
  public boolean isWriteCompletionStatus() {
    return this.writeCompletionStatus;
  }

  /**
   * Sets whether the completion status data is to be written.
   *
   * @param writeCompletionStatus a boolean value indicating whether the completion status data
   * should be written. If true, the completion status data will be written; otherwise, it will not
   * be written.
   */
  public void setWriteCompletionStatus(boolean writeCompletionStatus) {
    this.writeCompletionStatus = writeCompletionStatus;
  }

  /**
   * Determines whether the progress measure data is set to be written.
   *
   * @return true if the progress measure data is configured to be written, false otherwise.
   */
  public boolean isWriteProgressMeasure() {
    return this.writeProgressMeasure;
  }

  /**
   * Sets whether the progress measure data is to be written.
   *
   * @param writeProgressMeasure a boolean value indicating whether the progress measure data should
   * be written. If true, the progress measure data will be written; otherwise, it will not be
   * written.
   */
  public void setWriteProgressMeasure(boolean writeProgressMeasure) {
    this.writeProgressMeasure = writeProgressMeasure;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (!(o instanceof MapInfo mapInfo)) {
      return false;
    }

    return new EqualsBuilder()
        .append(isReadRawScore(), mapInfo.isReadRawScore())
        .append(isReadMinScore(), mapInfo.isReadMinScore())
        .append(isReadMaxScore(), mapInfo.isReadMaxScore())
        .append(isReadCompletionStatus(), mapInfo.isReadCompletionStatus())
        .append(isReadProgressMeasure(), mapInfo.isReadProgressMeasure())
        .append(isWriteRawScore(), mapInfo.isWriteRawScore())
        .append(isWriteMinScore(), mapInfo.isWriteMinScore())
        .append(isWriteMaxScore(), mapInfo.isWriteMaxScore())
        .append(isWriteCompletionStatus(), mapInfo.isWriteCompletionStatus())
        .append(isWriteProgressMeasure(), mapInfo.isWriteProgressMeasure())
        .append(getTargetObjectiveID(), mapInfo.getTargetObjectiveID())
        .isEquals();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder(17, 37)
        .append(getTargetObjectiveID())
        .append(isReadRawScore())
        .append(isReadMinScore())
        .append(isReadMaxScore())
        .append(isReadCompletionStatus())
        .append(isReadProgressMeasure())
        .append(isWriteRawScore())
        .append(isWriteMinScore())
        .append(isWriteMaxScore())
        .append(isWriteCompletionStatus())
        .append(isWriteProgressMeasure())
        .toHashCode();
  }
}
