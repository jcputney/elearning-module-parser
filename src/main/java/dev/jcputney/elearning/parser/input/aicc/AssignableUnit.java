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

import static lombok.AccessLevel.PRIVATE;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.jackson.Jacksonized;
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
@Builder
@Getter
@Jacksonized
@AllArgsConstructor(access = PRIVATE)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonFormat(with = JsonFormat.Feature.ACCEPT_CASE_INSENSITIVE_PROPERTIES)
public class AssignableUnit {

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
  @Setter
  private Descriptor descriptor;

  /**
   * Default constructor for the AssignableUnit class.
   */
  @SuppressWarnings("unused")
  public AssignableUnit() {
    // Default constructor
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    AssignableUnit that = (AssignableUnit) o;

    return new EqualsBuilder()
        .append(systemId, that.systemId)
        .append(commandLine, that.commandLine)
        .append(fileName, that.fileName)
        .append(coreVendor, that.coreVendor)
        .append(type, that.type)
        .append(maxScore, that.maxScore)
        .append(masteryScore, that.masteryScore)
        .append(maxTimeAllowed, that.maxTimeAllowed)
        .append(timeLimitAction, that.timeLimitAction)
        .append(systemVendor, that.systemVendor)
        .append(webLaunch, that.webLaunch)
        .append(auPassword, that.auPassword)
        .append(descriptor, that.descriptor)
        .isEquals();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder(17, 37)
        .append(systemId)
        .append(commandLine)
        .append(fileName)
        .append(coreVendor)
        .append(type)
        .append(maxScore)
        .append(masteryScore)
        .append(maxTimeAllowed)
        .append(timeLimitAction)
        .append(systemVendor)
        .append(webLaunch)
        .append(auPassword)
        .append(descriptor)
        .toHashCode();
  }
}

