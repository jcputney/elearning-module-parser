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

package dev.jcputney.elearning.parser.input.lom.properties;

import static lombok.AccessLevel.PRIVATE;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Feature;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;

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
@Builder
@Getter
@Jacksonized
@AllArgsConstructor(access = PRIVATE)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonFormat(with = Feature.ACCEPT_CASE_INSENSITIVE_PROPERTIES)
public class ControlsSpec {

  /**
   * The flag indicating whether to show the finish button.
   */
  @JacksonXmlProperty(localName = "showFinishButton")
  private YesNoType showFinishButton;
  /**
   * The flag indicating whether to show the close item.
   */
  @JacksonXmlProperty(localName = "showCloseItem")
  private YesNoType showCloseItem;
  /**
   * The flag indicating whether to show help.
   */
  @JacksonXmlProperty(localName = "showHelp")
  private YesNoType showHelp;
  /**
   * The flag indicating whether to show the progress bar.
   */
  @JacksonXmlProperty(localName = "showProgressBar")
  private YesNoType showProgressBar;
  /**
   * The flag indicating whether to use the measure progress bar.
   */
  @JacksonXmlProperty(localName = "useMeasureProgressBar")
  private YesNoType useMeasureProgressBar;
  /**
   * The flag indicating whether to show the course structure.
   */
  @JacksonXmlProperty(localName = "showCourseStructure")
  private YesNoType showCourseStructure;
  /**
   * The flag indicating whether the course structure starts open.
   */
  @JacksonXmlProperty(localName = "courseStructureStartsOpen")
  private YesNoType courseStructureStartsOpen;
  /**
   * The flag indicating whether to show the navigation bar.
   */
  @JacksonXmlProperty(localName = "showNavBar")
  private YesNoType showNavBar;
  /**
   * The flag indicating whether to show the title bar.
   */
  @JacksonXmlProperty(localName = "showTitleBar")
  private YesNoType showTitleBar;
  /**
   * The flag indicating whether to enable flow navigation.
   */
  @JacksonXmlProperty(localName = "enableFlowNav")
  private YesNoType enableFlowNav;
  /**
   * The flag indicating whether to enable choice navigation.
   */
  @JacksonXmlProperty(localName = "enableChoiceNav")
  private YesNoType enableChoiceNav;
  /**
   * The status display type.
   */
  @JacksonXmlProperty(localName = "statusDisplay")
  private StatusDisplayType statusDisplay;
  /**
   * The flag indicating whether to force disable the root choice.
   */
  @JacksonXmlProperty(localName = "forceDisableRootChoice")
  private YesNoType forceDisableRootChoice;

  /**
   * Default constructor for the ControlsSpec class.
   */
  @SuppressWarnings("unused")
  public ControlsSpec() {
    // Default constructor
  }
}
