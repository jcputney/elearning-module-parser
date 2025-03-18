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

package dev.jcputney.elearning.parser.input.scorm2004.adl.sequencing;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;

/**
 * Represents a mapping to a global objective or another objective within the LMS. Maps allow
 * tracking of objectives that are shared across different content items. The following schema
 * shows the structure of the mapInfoType element:
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
@Builder
@Getter
@Jacksonized
@JsonFormat(with = JsonFormat.Feature.ACCEPT_CASE_INSENSITIVE_PROPERTIES)
public class MapInfo {

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
  @Default
  private boolean readRawScore = true;

  /**
   * Indicates whether the minimum score for this objective should be read from the mapped global
   * objective.
   */
  @JacksonXmlProperty(isAttribute = true, localName = "readMinScore")
  @JsonProperty("readMinScore")
  @Default
  private boolean readMinScore = true;

  /**
   * Indicates whether the maximum score for this objective should be read from the mapped global
   * objective.
   */
  @JacksonXmlProperty(isAttribute = true, localName = "readMaxScore")
  @JsonProperty("readMaxScore")
  @Default
  private boolean readMaxScore = true;

  /**
   * Indicates whether the completion status for this objective should be read from the mapped
   * global objective.
   */
  @JacksonXmlProperty(isAttribute = true, localName = "readCompletionStatus")
  @JsonProperty("readCompletionStatus")
  @Default
  private boolean readCompletionStatus = true;

  /**
   * Indicates whether the progress measure for this objective should be read from the mapped
   * global objective.
   */
  @JacksonXmlProperty(isAttribute = true, localName = "readProgressMeasure")
  @JsonProperty("readProgressMeasure")
  @Default
  private boolean readProgressMeasure = true;

  /**
   * Indicates whether the raw score for this objective should be written to the mapped global
   * objective.
   */
  @JacksonXmlProperty(isAttribute = true, localName = "writeRawScore")
  @JsonProperty("writeRawScore")
  @Default
  private boolean writeRawScore = false;

  /**
   * Indicates whether the minimum score for this objective should be written to the mapped global
   * objective.
   */
  @JacksonXmlProperty(isAttribute = true, localName = "writeMinScore")
  @JsonProperty("writeMinScore")
  @Default
  private boolean writeMinScore = false;

  /**
   * Indicates whether the maximum score for this objective should be written to the mapped global
   * objective.
   */
  @JacksonXmlProperty(isAttribute = true, localName = "writeMaxScore")
  @JsonProperty("writeMaxScore")
  @Default
  private boolean writeMaxScore = false;

  /**
   * Indicates whether the completion status for this objective should be written to the mapped
   * global objective.
   */
  @JacksonXmlProperty(isAttribute = true, localName = "writeCompletionStatus")
  @JsonProperty("writeCompletionStatus")
  @Default
  private boolean writeCompletionStatus = false;

  /**
   * Indicates whether the progress measure for this objective should be written to the mapped
   * global objective.
   */
  @JacksonXmlProperty(isAttribute = true, localName = "writeProgressMeasure")
  @JsonProperty("writeProgressMeasure")
  @Default
  private boolean writeProgressMeasure = false;
}
