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

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import dev.jcputney.elearning.parser.input.scorm2004.adl.types.RollupConsiderationType;
import lombok.Data;

/**
 * Represents the rollupConsiderationsType complex type, defining attributes for rollup conditions.
 * The following schema snippet shows the structure of the rollupConsiderationsType element:
 * <pre>{@code
 *   <xs:complexType name = "rollupConsiderationsType">
 *      <xs:attribute name = "requiredForSatisfied" default = "always" type = "rollupConsiderationType"/>
 *      <xs:attribute name = "requiredForNotSatisfied" default = "always" type = "rollupConsiderationType"/>
 *      <xs:attribute name = "requiredForCompleted" default = "always" type = "rollupConsiderationType"/>
 *      <xs:attribute name = "requiredForIncomplete" default = "always" type = "rollupConsiderationType"/>
 *      <xs:attribute name = "measureSatisfactionIfActive" default = "true" type = "xs:boolean"/>
 *   </xs:complexType>
 * }</pre>
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class RollupConsiderations {

  /**
   * Specifies when satisfaction is required for rollup. Default is "always".
   */
  @JacksonXmlProperty(localName = "requiredForSatisfied", isAttribute = true)
  private RollupConsiderationType requiredForSatisfied = RollupConsiderationType.ALWAYS;

  /**
   * Specifies when not satisfaction is required for rollup. Default is "always".
   */
  @JacksonXmlProperty(localName = "requiredForNotSatisfied", isAttribute = true)
  private RollupConsiderationType requiredForNotSatisfied = RollupConsiderationType.ALWAYS;

  /**
   * Specifies when completion is required for rollup. Default is "always".
   */
  @JacksonXmlProperty(localName = "requiredForCompleted", isAttribute = true)
  private RollupConsiderationType requiredForCompleted = RollupConsiderationType.ALWAYS;

  /**
   * Specifies when incompletion is required for rollup. Default is "always".
   */
  @JacksonXmlProperty(localName = "requiredForIncomplete", isAttribute = true)
  private RollupConsiderationType requiredForIncomplete = RollupConsiderationType.ALWAYS;

  /**
   * Indicates if satisfaction is measured only when active. Default is true.
   */
  @JacksonXmlProperty(localName = "measureSatisfactionIfActive", isAttribute = true)
  private boolean measureSatisfactionIfActive = true;
}
