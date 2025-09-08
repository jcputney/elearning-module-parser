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

package dev.jcputney.elearning.parser.input.aicc;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * Represents an Assignable Unit (AU) in the context of AICC (Aviation Industry CBT Committee).
 * <p>
 * The Assignable Unit is a component of a course that can be assigned to learners.
 * </p>
 *
 * <pre>{@code
 * <complexType name="assignableUnit">
 *   <complexContent>
 *     <extension base="assignableUnitVocab">
 *       <attributeGroup ref="ag:assignableUnit"/>
 *     </extension>
 *   </complexContent>
 * </complexType>
 * }</pre>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonFormat(with = JsonFormat.Feature.ACCEPT_CASE_INSENSITIVE_PROPERTIES)
public class AssignableUnit implements Serializable {

  /**
   * The identifier of the assignable unit.
   */
  @JsonProperty(value = "System_ID", required = true)
  private String systemId;
  /**
   * The command line used to launch the assignable unit.
   */
  @JsonProperty(value = "Command_Line", required = true)
  private String commandLine;
  /**
   * The filename of the assignable unit.
   */
  @JsonProperty(value = "File_Name", required = true)
  private String fileName;
  /**
   * The core vendor of the assignable unit.
   */
  @JsonProperty(value = "Core_Vendor", required = true)
  private String coreVendor;
  /**
   * The type of the assignable unit.
   */
  @JsonProperty(value = "Type")
  private String type;
  /**
   * The maximum score for the assignable unit.
   */
  @JsonProperty(value = "Max_Score")
  private String maxScore;
  /**
   * The mastery score for the assignable unit.
   */
  @JsonProperty(value = "Mastery_Score")
  private String masteryScore;
  /**
   * The maximum time allowed for the assignable unit.
   */
  @JsonProperty(value = "Max_Time_Allowed")
  private String maxTimeAllowed;
  /**
   * The time limit action for the assignable unit.
   */
  @JsonProperty(value = "Time_Limit_Action")
  private String timeLimitAction;
  /**
   * The system vendor of the assignable unit.
   */
  @JsonProperty(value = "System_Vendor")
  private String systemVendor;
  /**
   * The web launch URL for the assignable unit.
   */
  @JsonProperty(value = "web_launch")
  private String webLaunch;
  /**
   * The AU password for the assignable unit.
   */
  @JsonProperty(value = "AU_Password")
  private String auPassword;
  /**
   * The AU descriptor for the assignable unit.
   */
  private Descriptor descriptor;

  public AssignableUnit() {
  }

  public String getSystemId() {
    return this.systemId;
  }

  public void setSystemId(String systemId) {
    this.systemId = systemId;
  }

  public String getCommandLine() {
    return this.commandLine;
  }

  public void setCommandLine(String commandLine) {
    this.commandLine = commandLine;
  }

  public String getFileName() {
    return this.fileName;
  }

  public void setFileName(String fileName) {
    this.fileName = fileName;
  }

  public String getCoreVendor() {
    return this.coreVendor;
  }

  public void setCoreVendor(String coreVendor) {
    this.coreVendor = coreVendor;
  }

  public String getType() {
    return this.type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getMaxScore() {
    return this.maxScore;
  }

  public void setMaxScore(String maxScore) {
    this.maxScore = maxScore;
  }

  public String getMasteryScore() {
    return this.masteryScore;
  }

  public void setMasteryScore(String masteryScore) {
    this.masteryScore = masteryScore;
  }

  public String getMaxTimeAllowed() {
    return this.maxTimeAllowed;
  }

  public void setMaxTimeAllowed(String maxTimeAllowed) {
    this.maxTimeAllowed = maxTimeAllowed;
  }

  public String getTimeLimitAction() {
    return this.timeLimitAction;
  }

  public void setTimeLimitAction(String timeLimitAction) {
    this.timeLimitAction = timeLimitAction;
  }

  public String getSystemVendor() {
    return this.systemVendor;
  }

  public void setSystemVendor(String systemVendor) {
    this.systemVendor = systemVendor;
  }

  public String getWebLaunch() {
    return this.webLaunch;
  }

  public void setWebLaunch(String webLaunch) {
    this.webLaunch = webLaunch;
  }

  public String getAuPassword() {
    return this.auPassword;
  }

  public void setAuPassword(String auPassword) {
    this.auPassword = auPassword;
  }

  public Descriptor getDescriptor() {
    return this.descriptor;
  }

  public void setDescriptor(Descriptor descriptor) {
    this.descriptor = descriptor;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (!(o instanceof AssignableUnit that)) {
      return false;
    }

    return new EqualsBuilder()
        .append(getSystemId(), that.getSystemId())
        .append(getCommandLine(), that.getCommandLine())
        .append(getFileName(), that.getFileName())
        .append(getCoreVendor(), that.getCoreVendor())
        .append(getType(), that.getType())
        .append(getMaxScore(), that.getMaxScore())
        .append(getMasteryScore(), that.getMasteryScore())
        .append(getMaxTimeAllowed(), that.getMaxTimeAllowed())
        .append(getTimeLimitAction(), that.getTimeLimitAction())
        .append(getSystemVendor(), that.getSystemVendor())
        .append(getWebLaunch(), that.getWebLaunch())
        .append(getAuPassword(), that.getAuPassword())
        .append(getDescriptor(), that.getDescriptor())
        .isEquals();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder(17, 37)
        .append(getSystemId())
        .append(getCommandLine())
        .append(getFileName())
        .append(getCoreVendor())
        .append(getType())
        .append(getMaxScore())
        .append(getMasteryScore())
        .append(getMaxTimeAllowed())
        .append(getTimeLimitAction())
        .append(getSystemVendor())
        .append(getWebLaunch())
        .append(getAuPassword())
        .append(getDescriptor())
        .toHashCode();
  }
}

