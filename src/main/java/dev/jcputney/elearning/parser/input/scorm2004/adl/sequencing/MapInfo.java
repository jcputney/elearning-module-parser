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

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;

/**
 * Represents a mapping to a global "objective" or another objective within the LMS. Maps allow
 * tracking of "objectives" that are shared across different content items. The following schema
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
@Data
public class MapInfo {

  /**
   * The unique identifier for the target "objective" in the LMS that this "objective" is mapped to.
   * This enables global tracking of the objective’s completion and satisfaction status.
   */
  @JacksonXmlProperty(isAttribute = true, localName = "targetObjectiveID")
  private String targetObjectiveID;

  /**
   * Indicates whether the raw score for this "objective" should be read from the mapped global
   * "objective".
   */
  @JacksonXmlProperty(isAttribute = true, localName = "readRawScore")
  private boolean readRawScore = true;

  /**
   * Indicates whether the minimum score for this "objective" should be read from the mapped global
   * "objective".
   */
  @JacksonXmlProperty(isAttribute = true, localName = "readMinScore")
  private boolean readMinScore = true;

  /**
   * Indicates whether the maximum score for this "objective" should be read from the mapped global
   * "objective".
   */
  @JacksonXmlProperty(isAttribute = true, localName = "readMaxScore")
  private boolean readMaxScore = true;

  /**
   * Indicates whether the completion status for this "objective" should be read from the mapped
   * global "objective".
   */
  @JacksonXmlProperty(isAttribute = true, localName = "readCompletionStatus")
  private boolean readCompletionStatus = true;

  /**
   * Indicates whether the progress measure for this "objective" should be read from the mapped
   * global "objective".
   */
  @JacksonXmlProperty(isAttribute = true, localName = "readProgressMeasure")
  private boolean readProgressMeasure = true;

  /**
   * Indicates whether the raw score for this "objective" should be written to the mapped global
   * "objective".
   */
  @JacksonXmlProperty(isAttribute = true, localName = "writeRawScore")
  private boolean writeRawScore = false;

  /**
   * Indicates whether the minimum score for this "objective" should be written to the mapped global
   * "objective".
   */
  @JacksonXmlProperty(isAttribute = true, localName = "writeMinScore")
  private boolean writeMinScore = false;

  /**
   * Indicates whether the maximum score for this "objective" should be written to the mapped global
   * "objective".
   */
  @JacksonXmlProperty(isAttribute = true, localName = "writeMaxScore")
  private boolean writeMaxScore = false;

  /**
   * Indicates whether the completion status for this "objective" should be written to the mapped
   * global "objective".
   */
  @JacksonXmlProperty(isAttribute = true, localName = "writeCompletionStatus")
  private boolean writeCompletionStatus = false;

  /**
   * Indicates whether the progress measure for this "objective" should be written to the mapped
   * global "objective".
   */
  @JacksonXmlProperty(isAttribute = true, localName = "writeProgressMeasure")
  private boolean writeProgressMeasure = false;
}
