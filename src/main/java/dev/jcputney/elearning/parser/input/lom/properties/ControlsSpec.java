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

package dev.jcputney.elearning.parser.input.lom.properties;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Feature;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import java.io.Serializable;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

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
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonFormat(with = Feature.ACCEPT_CASE_INSENSITIVE_PROPERTIES)
public class ControlsSpec implements Serializable {

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

  public ControlsSpec() {
    // no-op
  }

  public YesNoType getShowFinishButton() {
    return this.showFinishButton;
  }

  public void setShowFinishButton(YesNoType showFinishButton) {
    this.showFinishButton = showFinishButton;
  }

  public YesNoType getShowCloseItem() {
    return this.showCloseItem;
  }

  public void setShowCloseItem(YesNoType showCloseItem) {
    this.showCloseItem = showCloseItem;
  }

  public YesNoType getShowHelp() {
    return this.showHelp;
  }

  public void setShowHelp(YesNoType showHelp) {
    this.showHelp = showHelp;
  }

  public YesNoType getShowProgressBar() {
    return this.showProgressBar;
  }

  public void setShowProgressBar(YesNoType showProgressBar) {
    this.showProgressBar = showProgressBar;
  }

  public YesNoType getUseMeasureProgressBar() {
    return this.useMeasureProgressBar;
  }

  public void setUseMeasureProgressBar(YesNoType useMeasureProgressBar) {
    this.useMeasureProgressBar = useMeasureProgressBar;
  }

  public YesNoType getShowCourseStructure() {
    return this.showCourseStructure;
  }

  public void setShowCourseStructure(YesNoType showCourseStructure) {
    this.showCourseStructure = showCourseStructure;
  }

  public YesNoType getCourseStructureStartsOpen() {
    return this.courseStructureStartsOpen;
  }

  public void setCourseStructureStartsOpen(
      YesNoType courseStructureStartsOpen) {
    this.courseStructureStartsOpen = courseStructureStartsOpen;
  }

  public YesNoType getShowNavBar() {
    return this.showNavBar;
  }

  public void setShowNavBar(YesNoType showNavBar) {
    this.showNavBar = showNavBar;
  }

  public YesNoType getShowTitleBar() {
    return this.showTitleBar;
  }

  public void setShowTitleBar(YesNoType showTitleBar) {
    this.showTitleBar = showTitleBar;
  }

  public YesNoType getEnableFlowNav() {
    return this.enableFlowNav;
  }

  public void setEnableFlowNav(YesNoType enableFlowNav) {
    this.enableFlowNav = enableFlowNav;
  }

  public YesNoType getEnableChoiceNav() {
    return this.enableChoiceNav;
  }

  public void setEnableChoiceNav(YesNoType enableChoiceNav) {
    this.enableChoiceNav = enableChoiceNav;
  }

  public StatusDisplayType getStatusDisplay() {
    return this.statusDisplay;
  }

  public void setStatusDisplay(StatusDisplayType statusDisplay) {
    this.statusDisplay = statusDisplay;
  }

  public YesNoType getForceDisableRootChoice() {
    return this.forceDisableRootChoice;
  }

  public void setForceDisableRootChoice(YesNoType forceDisableRootChoice) {
    this.forceDisableRootChoice = forceDisableRootChoice;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (!(o instanceof ControlsSpec that)) {
      return false;
    }

    return new EqualsBuilder()
        .append(getShowFinishButton(), that.getShowFinishButton())
        .append(getShowCloseItem(), that.getShowCloseItem())
        .append(getShowHelp(), that.getShowHelp())
        .append(getShowProgressBar(), that.getShowProgressBar())
        .append(getUseMeasureProgressBar(), that.getUseMeasureProgressBar())
        .append(getShowCourseStructure(), that.getShowCourseStructure())
        .append(getCourseStructureStartsOpen(), that.getCourseStructureStartsOpen())
        .append(getShowNavBar(), that.getShowNavBar())
        .append(getShowTitleBar(), that.getShowTitleBar())
        .append(getEnableFlowNav(), that.getEnableFlowNav())
        .append(getEnableChoiceNav(), that.getEnableChoiceNav())
        .append(getStatusDisplay(), that.getStatusDisplay())
        .append(getForceDisableRootChoice(), that.getForceDisableRootChoice())
        .isEquals();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder(17, 37)
        .append(getShowFinishButton())
        .append(getShowCloseItem())
        .append(getShowHelp())
        .append(getShowProgressBar())
        .append(getUseMeasureProgressBar())
        .append(getShowCourseStructure())
        .append(getCourseStructureStartsOpen())
        .append(getShowNavBar())
        .append(getShowTitleBar())
        .append(getEnableFlowNav())
        .append(getEnableChoiceNav())
        .append(getStatusDisplay())
        .append(getForceDisableRootChoice())
        .toHashCode();
  }
}
