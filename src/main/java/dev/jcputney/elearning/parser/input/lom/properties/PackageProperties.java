/*
 * Copyright (c) 2025. Jonathan Putney
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

package dev.jcputney.elearning.parser.input.lom.properties;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;

/**
 * <p>Class representing the root element of the ScormEnginePackageProperties XML.</p>
 *
 * <p>The following schema snippet is the root declaration of the XSD:</p>
 * <pre>{@code
 * <xs:element name="ScormEnginePackageProperties">
 *   <xs:complexType>
 *     <xs:all>
 *       <xs:element name="controls" type="controlsSpec" minOccurs="0" maxOccurs="1" />
 *       <xs:element name="appearance" type="appearanceSpec" minOccurs="0" maxOccurs="1" />
 *       <xs:element name="appearence" type="appearanceSpec" minOccurs="0" maxOccurs="1" />
 *       <xs:element name="behavior" type="behaviorSpec" minOccurs="0" maxOccurs="1" />
 *       <xs:element name="rsop" type="rsopSpec" minOccurs="0" maxOccurs="1" />
 *       <xs:element name="heuristics" type="heuristicSpec" minOccurs="0" maxOccurs="1" />
 *     </xs:all>
 *   </xs:complexType>
 * </xs:element>
 * }</pre>
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonFormat(with = JsonFormat.Feature.ACCEPT_CASE_INSENSITIVE_PROPERTIES)
public class PackageProperties {

  public static final String NAMESPACE_URI = "http://www.scorm.com/xsd/ScormEnginePackageProperties";

  @JacksonXmlProperty(localName = "controls", namespace = NAMESPACE_URI)
  private ControlsSpec controls;

  /**
   * Single field handling both "appearance" and the misspelled "appearence".
   */
  @JacksonXmlProperty(localName = "appearance", namespace = NAMESPACE_URI)
  @JsonAlias("appearence")
  private AppearanceSpec appearance;

  @JacksonXmlProperty(localName = "behavior", namespace = NAMESPACE_URI)
  private BehaviorSpec behavior;

  @JacksonXmlProperty(localName = "rsop", namespace = NAMESPACE_URI)
  private RsopSpec rsop;

  @JacksonXmlProperty(localName = "heuristics", namespace = NAMESPACE_URI)
  private HeuristicSpec heuristics;
}