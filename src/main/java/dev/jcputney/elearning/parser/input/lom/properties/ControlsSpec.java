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

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Feature;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;

/**
 * <p>Represents the <strong>controlsSpec</strong> complex type.</p>
 *
 * <p>The following schema snippet specifies its contents:</p>
 * <pre>{@code
 * <xs:complexType name="controlsSpec">
 *   <xs:all>
 *     <xs:element name="showFinishButton" type="yesNoType" minOccurs="0" maxOccurs="1" />
 *     <xs:element name="showCloseItem" type="yesNoType" minOccurs="0" maxOccurs="1" />
 *     <xs:element name="showHelp" type="yesNoType" minOccurs="0" maxOccurs="1" />
 *     <xs:element name="showProgressBar" type="yesNoType" minOccurs="0" maxOccurs="1" />
 *     <xs:element name="useMeasureProgressBar" type="yesNoType" minOccurs="0" maxOccurs="1" />
 *     <xs:element name="showCourseStructure" type="yesNoType" minOccurs="0" maxOccurs="1" />
 *     <xs:element name="courseStructureStartsOpen" type="yesNoType" minOccurs="0" maxOccurs="1" />
 *     <xs:element name="showNavBar" type="yesNoType" minOccurs="0" maxOccurs="1" />
 *     <xs:element name="showTitleBar" type="yesNoType" minOccurs="0" maxOccurs="1" />
 *     <xs:element name="enableFlowNav" type="yesNoType" minOccurs="0" maxOccurs="1" />
 *     <xs:element name="enableChoiceNav" type="yesNoType" minOccurs="0" maxOccurs="1" />
 *     <xs:element name="statusDisplay" type="statusDisplayType" minOccurs="0" maxOccurs="1" />
 *     <xs:element name="forceDisableRootChoice" type="yesNoType" minOccurs="0" maxOccurs="1" />
 *   </xs:all>
 * </xs:complexType>
 * }</pre>
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonFormat(with = Feature.ACCEPT_CASE_INSENSITIVE_PROPERTIES)
public class ControlsSpec {

  @JacksonXmlProperty(localName = "showFinishButton")
  private YesNoType showFinishButton;

  @JacksonXmlProperty(localName = "showCloseItem")
  private YesNoType showCloseItem;

  @JacksonXmlProperty(localName = "showHelp")
  private YesNoType showHelp;

  @JacksonXmlProperty(localName = "showProgressBar")
  private YesNoType showProgressBar;

  @JacksonXmlProperty(localName = "useMeasureProgressBar")
  private YesNoType useMeasureProgressBar;

  @JacksonXmlProperty(localName = "showCourseStructure")
  private YesNoType showCourseStructure;

  @JacksonXmlProperty(localName = "courseStructureStartsOpen")
  private YesNoType courseStructureStartsOpen;

  @JacksonXmlProperty(localName = "showNavBar")
  private YesNoType showNavBar;

  @JacksonXmlProperty(localName = "showTitleBar")
  private YesNoType showTitleBar;

  @JacksonXmlProperty(localName = "enableFlowNav")
  private YesNoType enableFlowNav;

  @JacksonXmlProperty(localName = "enableChoiceNav")
  private YesNoType enableChoiceNav;

  @JacksonXmlProperty(localName = "statusDisplay")
  private StatusDisplayType statusDisplay;

  @JacksonXmlProperty(localName = "forceDisableRootChoice")
  private YesNoType forceDisableRootChoice;
}