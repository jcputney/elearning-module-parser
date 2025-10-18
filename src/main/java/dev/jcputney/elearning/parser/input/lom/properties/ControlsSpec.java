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
public final class ControlsSpec implements Serializable {

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
   * Default constructor for the ControlsSpec class. This constructor initializes a new instance of
   * the ControlsSpec class. No specific initialization is performed in this constructor.
   */
  public ControlsSpec() {
    // no-op
  }

  /**
   * Retrieves the value of the showFinishButton property, which determines whether the "Finish"
   * button should be displayed or not.
   *
   * @return the current value of the showFinishButton property, either as YES, NO, TRUE, or FALSE.
   */
  public YesNoType getShowFinishButton() {
    return this.showFinishButton;
  }

  /**
   * Sets the value of the showFinishButton property, which determines whether the "Finish" button
   * should be displayed or not.
   *
   * @param showFinishButton the value to set for the showFinishButton property. Accepts values of
   * either YES, NO, TRUE, or FALSE, as defined by the YesNoType enum.
   */
  public void setShowFinishButton(YesNoType showFinishButton) {
    this.showFinishButton = showFinishButton;
  }

  /**
   * Retrieves the value of the showCloseItem property, which indicates whether the "Close" item
   * should be displayed or hidden.
   *
   * @return the current value of the showCloseItem property, represented as one of the values
   * defined in the YesNoType enumeration (YES, NO, TRUE, or FALSE).
   */
  public YesNoType getShowCloseItem() {
    return this.showCloseItem;
  }

  /**
   * Sets the value of the showCloseItem property, which determines whether the "Close" item should
   * be displayed or hidden.
   *
   * @param showCloseItem the value to set for the showCloseItem property. Accepts values of either
   * YES, NO, TRUE, or FALSE, as defined by the YesNoType enum.
   */
  public void setShowCloseItem(YesNoType showCloseItem) {
    this.showCloseItem = showCloseItem;
  }

  /**
   * Retrieves the value of the showHelp property, which defines whether the help section should be
   * displayed or not.
   *
   * @return the current value of the showHelp property, represented as one of the values in the
   * YesNoType enumeration (YES, NO, TRUE, or FALSE).
   */
  public YesNoType getShowHelp() {
    return this.showHelp;
  }

  /**
   * Sets the value of the showHelp property, determining whether the help section should be
   * displayed or hidden.
   *
   * @param showHelp the value to set for the showHelp property. Accepts values of either YES, NO,
   * TRUE, or FALSE, as defined by the YesNoType enum.
   */
  public void setShowHelp(YesNoType showHelp) {
    this.showHelp = showHelp;
  }

  /**
   * Retrieves the value of the showProgressBar property, which determines whether the progress bar
   * should be displayed or not.
   *
   * @return the current value of the showProgressBar property, represented as one of the values
   * defined in the YesNoType enumeration (YES, NO, TRUE, or FALSE).
   */
  public YesNoType getShowProgressBar() {
    return this.showProgressBar;
  }

  /**
   * Sets the value of the showProgressBar property, which determines whether the progress bar
   * should be displayed or hidden.
   *
   * @param showProgressBar the value to set for the showProgressBar property. Accepts values of
   * either YES, NO, TRUE, or FALSE, as defined by the YesNoType enum.
   */
  public void setShowProgressBar(YesNoType showProgressBar) {
    this.showProgressBar = showProgressBar;
  }

  /**
   * Retrieves the value of the useMeasureProgressBar property, which determines whether the measure
   * progress bar functionality is enabled or disabled.
   *
   * @return the current value of the useMeasureProgressBar property, represented as one of the
   * values defined in the YesNoType enumeration (YES, NO, TRUE, or FALSE).
   */
  public YesNoType getUseMeasureProgressBar() {
    return this.useMeasureProgressBar;
  }

  /**
   * Sets the value of the useMeasureProgressBar property, which determines whether the measure
   * progress bar functionality is enabled or disabled.
   *
   * @param useMeasureProgressBar the value to set for the useMeasureProgressBar property. Accepts
   * values of either YES, NO, TRUE, or FALSE, as defined by the YesNoType enum.
   */
  public void setUseMeasureProgressBar(YesNoType useMeasureProgressBar) {
    this.useMeasureProgressBar = useMeasureProgressBar;
  }

  /**
   * Retrieves the value of the showCourseStructure property, which determines whether the course
   * structure should be displayed or hidden.
   *
   * @return the current value of the showCourseStructure property, represented as one of the values
   * defined in the YesNoType enumeration (YES, NO, TRUE, or FALSE).
   */
  public YesNoType getShowCourseStructure() {
    return this.showCourseStructure;
  }

  /**
   * Sets the value of the showCourseStructure property, which determines whether the course
   * structure should be displayed or hidden.
   *
   * @param showCourseStructure the value to set for the showCourseStructure property. Accepts
   * values of either YES, NO, TRUE, or FALSE, as defined by the YesNoType enum.
   */
  public void setShowCourseStructure(YesNoType showCourseStructure) {
    this.showCourseStructure = showCourseStructure;
  }

  /**
   * Retrieves the value of the courseStructureStartsOpen property, which determines whether the
   * course structure starts in an open state or not.
   *
   * @return the current value of the courseStructureStartsOpen property, represented as one of the
   * values in the YesNoType enumeration (YES, NO, TRUE, or FALSE).
   */
  public YesNoType getCourseStructureStartsOpen() {
    return this.courseStructureStartsOpen;
  }

  /**
   * Sets the value of the courseStructureStartsOpen property, which determines whether the course
   * structure should start in an open state or not.
   *
   * @param courseStructureStartsOpen the value to set for the courseStructureStartsOpen property.
   * Accepts values of either YES, NO, TRUE, or FALSE, as defined by the YesNoType enum.
   */
  public void setCourseStructureStartsOpen(
      YesNoType courseStructureStartsOpen) {
    this.courseStructureStartsOpen = courseStructureStartsOpen;
  }

  /**
   * Retrieves the value of the showNavBar property, which determines whether the navigation bar
   * should be displayed or not.
   *
   * @return the current value of the showNavBar property, represented as one of the values in the
   * YesNoType enumeration (YES, NO, TRUE, or FALSE).
   */
  public YesNoType getShowNavBar() {
    return this.showNavBar;
  }

  /**
   * Sets the value of the showNavBar property, which determines whether the navigation bar should
   * be displayed or hidden.
   *
   * @param showNavBar the value to set for the showNavBar property. Accepts values of either YES,
   * NO, TRUE, or FALSE, as defined by the YesNoType enum.
   */
  public void setShowNavBar(YesNoType showNavBar) {
    this.showNavBar = showNavBar;
  }

  /**
   * Retrieves the value of the showTitleBar property, which determines whether the title bar should
   * be displayed or not.
   *
   * @return the current value of the showTitleBar property, represented as one of the values in the
   * YesNoType enumeration (YES, NO, TRUE, or FALSE).
   */
  public YesNoType getShowTitleBar() {
    return this.showTitleBar;
  }

  /**
   * Sets whether the title bar should be displayed.
   *
   * @param showTitleBar a YesNoType value indicating if the title bar should be visible (Yes) or
   * hidden (No)
   */
  public void setShowTitleBar(YesNoType showTitleBar) {
    this.showTitleBar = showTitleBar;
  }

  /**
   * Retrieves the value of the enableFlowNav property, which indicates whether the flow navigation
   * functionality is enabled or disabled.
   *
   * @return the current value of the enableFlowNav property, represented as one of the values in
   * the YesNoType enumeration (YES, NO, TRUE, or FALSE).
   */
  public YesNoType getEnableFlowNav() {
    return this.enableFlowNav;
  }

  /**
   * Sets the flow navigation enablement status.
   *
   * @param enableFlowNav Determines whether flow navigation is enabled or disabled.
   */
  public void setEnableFlowNav(YesNoType enableFlowNav) {
    this.enableFlowNav = enableFlowNav;
  }

  /**
   * Retrieves the current state of the enableChoiceNav property.
   *
   * @return a YesNoType value representing whether the choice navigation is enabled or not.
   */
  public YesNoType getEnableChoiceNav() {
    return this.enableChoiceNav;
  }

  /**
   * Sets the enableChoiceNav property to define whether choice navigation is enabled or not.
   *
   * @param enableChoiceNav the YesNoType value indicating if choice navigation should be enabled
   */
  public void setEnableChoiceNav(YesNoType enableChoiceNav) {
    this.enableChoiceNav = enableChoiceNav;
  }

  /**
   * Retrieves the current status display type.
   *
   * @return the current status display of type StatusDisplayType
   */
  public StatusDisplayType getStatusDisplay() {
    return this.statusDisplay;
  }

  /**
   * Updates the status display to the specified type.
   *
   * @param statusDisplay the new status display type to set
   */
  public void setStatusDisplay(StatusDisplayType statusDisplay) {
    this.statusDisplay = statusDisplay;
  }

  /**
   * Retrieves the current value of the forceDisableRootChoice setting.
   *
   * @return the value of forceDisableRootChoice of type YesNoType
   */
  public YesNoType getForceDisableRootChoice() {
    return this.forceDisableRootChoice;
  }

  /**
   * Sets the value indicating whether the root choice should be forcibly disabled.
   *
   * @param forceDisableRootChoice a value of type YesNoType that determines whether the root choice
   * is forcibly disabled.
   */
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
