/*
 * qlty-ignore: +qlty:similar-code
 *
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
 *
 * qlty-ignore: -qlty:similar-code
 */

package dev.jcputney.elearning.parser.input.scorm2004.adl.sequencing;

import static lombok.AccessLevel.PRIVATE;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import dev.jcputney.elearning.parser.input.scorm2004.ADLSeq;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;

/**
 * Represents an objective within the SCORM sequencing model. Objectives define specific learning
 * goals and their status and measure. The following schema snippet shows the structure of an
 * objective element:
 * <pre>{@code
 *   <xs:complexType name="objectiveType">
 *     <xs:sequence>
 *        <xs:element ref = "mapInfo" minOccurs = "1" maxOccurs = "unbounded"/>
 *     </xs:sequence>
 *     <xs:attribute name = "objectiveID" use = "required" type = "xs:anyURI"/>
 *   </xs:complexType>
 * }</pre>
 */
@Builder
@Getter
@Jacksonized
@AllArgsConstructor(access = PRIVATE)
@JsonFormat(with = JsonFormat.Feature.ACCEPT_CASE_INSENSITIVE_PROPERTIES)
public class ADLObjective {

  /**
   * The unique identifier for this objective. This is used to map the objective within the LMS to
   * track the learner’s progress and completion status.
   */
  @JacksonXmlProperty(isAttribute = true, localName = "objectiveID")
  @JsonProperty("objectiveID")
  private String objectiveID;
  /**
   * List of mappings for this objective, defining connections to global objectives or other
   * objectives within the LMS.
   */
  @JacksonXmlElementWrapper(useWrapping = false)
  @JacksonXmlProperty(localName = "mapInfo", namespace = ADLSeq.NAMESPACE_URI)
  private List<MapInfo> mapInfoList;

  /**
   * Default constructor for the ADLObjective class.
   */
  @SuppressWarnings("unused")
  public ADLObjective() {
    // Default constructor
  }
}
