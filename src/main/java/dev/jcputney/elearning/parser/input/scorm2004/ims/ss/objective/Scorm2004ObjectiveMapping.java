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

package dev.jcputney.elearning.parser.input.scorm2004.ims.ss.objective;

import static lombok.AccessLevel.PRIVATE;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;

/**
 * Represents a mapping from a local objective to a global objective within the SCORM IMS Simple
 * Sequencing schema.
 * <p>Objective mappings allow local objectives to share satisfaction and progress
 * information with global objectives, providing consistency in tracking and reporting across
 * different activities.</p>
 */
@Builder
@Getter
@Jacksonized
@AllArgsConstructor(access = PRIVATE)
@JsonFormat(with = JsonFormat.Feature.ACCEPT_CASE_INSENSITIVE_PROPERTIES)
public class Scorm2004ObjectiveMapping {

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
  @Default
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
  @Default
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
  @Default
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
  @Default
  private boolean writeNormalizedMeasure = false;

  /**
   * Default constructor for the Scorm2004ObjectiveMapping class.
   */
  @SuppressWarnings("unused")
  public Scorm2004ObjectiveMapping() {
    // Default constructor
  }
}
