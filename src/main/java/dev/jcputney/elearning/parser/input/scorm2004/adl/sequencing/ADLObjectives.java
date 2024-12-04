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
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import dev.jcputney.elearning.parser.input.scorm2004.ADLSeq;
import java.util.List;
import lombok.Data;

/**
 * Represents the objectivesType complex type, containing a list of objective elements. The
 * following schema shows the structure of the objectivesType element:
 * <pre>{@code
 *   <xs:complexType name="objectivesType">
 *      <xs:sequence>
 *         <xs:element ref = "objective" minOccurs = "1" maxOccurs = "unbounded"/>
 *      </xs:sequence>
 *   </xs:complexType>
 * }</pre>
 */
@Data
@JsonFormat(with = JsonFormat.Feature.ACCEPT_CASE_INSENSITIVE_PROPERTIES)
public class ADLObjectives {

  /**
   * List of objectives. Each objective defines specific attributes for rollup.
   */
  @JacksonXmlElementWrapper(useWrapping = false)
  @JacksonXmlProperty(localName = "objective", namespace = ADLSeq.NAMESPACE_URI)
  private List<ADLObjective> objectiveList;
}
