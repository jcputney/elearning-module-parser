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
import dev.jcputney.elearning.parser.input.aicc.prereq.AiccPrerequisiteExpression;
import java.io.Serializable;
import java.time.Duration;
import java.util.List;
import java.util.Objects;
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
  @JsonProperty(value = "Descriptor")
  private Descriptor descriptor;

  /**
   * Raw prerequisites expression sourced from the course structure file.
   */
  @JsonProperty(value = "Prerequisites")
  private String prerequisitesExpression;
  /**
   * Parsed representation of the prerequisites expression.
   */
  private AiccPrerequisiteExpression prerequisiteModel;

  /**
   * Completion criteria (CA/CL/CR) defined for this assignable unit.
   */
  private AiccCompletionCriteria completionCriteria;

  /**
   * Normalized mastery score (0-1 range) resolved for this AU.
   */
  private Double masteryScoreNormalized;

  /**
   * Normalized maximum time allowed as a duration.
   */
  private Duration maxTimeAllowedNormalized;

  /**
   * Normalized time limit actions split into individual tokens.
   */
  private List<String> timeLimitActionNormalized = List.of();

  /**
   * Explicit prerequisite mandatory flag when provided by the source.
   */
  private Boolean prerequisitesMandatoryOverride;

  /**
   * Retrieves the system identifier associated with the assignable unit.
   *
   * @return the system identifier as a {@code String}
   */
  public String getSystemId() {
    return this.systemId;
  }

  /**
   * Sets the system identifier for the assignable unit.
   *
   * @param systemId the system identifier to set as a String
   */
  public void setSystemId(String systemId) {
    this.systemId = systemId;
  }

  /**
   * Retrieves the command line associated with this assignable unit.
   *
   * @return the command line as a String
   */
  public String getCommandLine() {
    return this.commandLine;
  }

  /**
   * Sets the command line associated with this assignable unit.
   *
   * @param commandLine the command line to set as a String
   */
  public void setCommandLine(String commandLine) {
    this.commandLine = commandLine;
  }

  /**
   * Retrieves the file name associated with this assignable unit.
   *
   * @return the file name as a String
   */
  public String getFileName() {
    return this.fileName;
  }

  /**
   * Sets the file name for the assignable unit.
   *
   * @param fileName the file name to set as a String
   */
  public void setFileName(String fileName) {
    this.fileName = fileName;
  }

  /**
   * Retrieves the core vendor associated with this assignable unit.
   *
   * @return the core vendor as a String
   */
  public String getCoreVendor() {
    return this.coreVendor;
  }

  /**
   * Sets the core vendor associated with this assignable unit.
   *
   * @param coreVendor the core vendor to set as a String
   */
  public void setCoreVendor(String coreVendor) {
    this.coreVendor = coreVendor;
  }

  /**
   * Retrieves the type associated with this assignable unit.
   *
   * @return the type as a String
   */
  public String getType() {
    return this.type;
  }

  /**
   * Sets the type associated with this assignable unit.
   *
   * @param type the type to set as a String
   */
  public void setType(String type) {
    this.type = type;
  }

  /**
   * Retrieves the maximum score associated with this assignable unit.
   *
   * @return the maximum score as a String
   */
  public String getMaxScore() {
    return this.maxScore;
  }

  /**
   * Sets the maximum score for the assignable unit.
   *
   * @param maxScore the maximum score to set as a String
   */
  public void setMaxScore(String maxScore) {
    this.maxScore = maxScore;
  }

  /**
   * Retrieves the mastery score associated with this assignable unit.
   *
   * @return the mastery score as a String
   */
  public String getMasteryScore() {
    return this.masteryScore;
  }

  /**
   * Sets the mastery score for the assignable unit.
   *
   * @param masteryScore the mastery score to set as a String
   */
  public void setMasteryScore(String masteryScore) {
    this.masteryScore = masteryScore;
  }

  /**
   * Retrieves the maximum time allowed for this assignable unit.
   *
   * @return the maximum time allowed as a String
   */
  public String getMaxTimeAllowed() {
    return this.maxTimeAllowed;
  }

  /**
   * Sets the maximum time allowed for this assignable unit.
   *
   * @param maxTimeAllowed the maximum time allowed to set as a String
   */
  public void setMaxTimeAllowed(String maxTimeAllowed) {
    this.maxTimeAllowed = maxTimeAllowed;
  }

  /**
   * Retrieves the time limit action associated with this assignable unit.
   *
   * @return the time limit action as a String
   */
  public String getTimeLimitAction() {
    return this.timeLimitAction;
  }

  /**
   * Sets the time limit action for the assignable unit.
   *
   * @param timeLimitAction the time limit action to set as a String
   */
  public void setTimeLimitAction(String timeLimitAction) {
    this.timeLimitAction = timeLimitAction;
  }

  /**
   * Retrieves the system vendor associated with this assignable unit.
   *
   * @return the system vendor as a String
   */
  public String getSystemVendor() {
    return this.systemVendor;
  }

  /**
   * Sets the system vendor for the assignable unit.
   *
   * @param systemVendor the system vendor to set as a String
   */
  public void setSystemVendor(String systemVendor) {
    this.systemVendor = systemVendor;
  }

  /**
   * Retrieves the web launch configuration associated with the assignable unit.
   *
   * @return the web launch configuration as a String
   */
  public String getWebLaunch() {
    return this.webLaunch;
  }

  /**
   * Sets the web launch configuration associated with the assignable unit.
   *
   * @param webLaunch the web launch configuration to set as a String
   */
  public void setWebLaunch(String webLaunch) {
    this.webLaunch = webLaunch;
  }

  /**
   * Retrieves the AU password associated with this assignable unit.
   *
   * @return the AU password as a String
   */
  public String getAuPassword() {
    return this.auPassword;
  }

  /**
   * Sets the AU (Assignable Unit) password for this object.
   *
   * @param auPassword the AU password to set as a String
   */
  public void setAuPassword(String auPassword) {
    this.auPassword = auPassword;
  }

  /**
   * Retrieves the descriptor associated with the assignable unit.
   *
   * @return the descriptor as a Descriptor object
   */
  public Descriptor getDescriptor() {
    return this.descriptor;
  }

  /**
   * Sets the descriptor associated with this assignable unit.
   *
   * @param descriptor the descriptor to set as a {@code Descriptor} object
   */
  public void setDescriptor(Descriptor descriptor) {
    this.descriptor = descriptor;
  }

  /**
   * Retrieves the prerequisites expression associated with this assignable unit.
   *
   * @return the prerequisites expression as a String
   */
  public String getPrerequisitesExpression() {
    return this.prerequisitesExpression;
  }

  /**
   * Sets the prerequisites expression for the assignable unit.
   *
   * @param prerequisitesExpression the prerequisites expression to set as a String
   */
  public void setPrerequisitesExpression(String prerequisitesExpression) {
    this.prerequisitesExpression = prerequisitesExpression;
  }

  /**
   * Retrieves the prerequisite model associated with this assignable unit.
   *
   * @return the prerequisite model as an {@code AiccPrerequisiteExpression}
   */
  public AiccPrerequisiteExpression getPrerequisiteModel() {
    return this.prerequisiteModel;
  }

  /**
   * Sets the prerequisite model for the current object with the provided AiccPrerequisiteExpression
   * instance.
   *
   * @param prerequisiteModel an instance of AiccPrerequisiteExpression that defines the
   * prerequisite model to be set.
   */
  public void setPrerequisiteModel(AiccPrerequisiteExpression prerequisiteModel) {
    this.prerequisiteModel = prerequisiteModel;
  }

  /**
   * Retrieves the AICC completion criteria for the current context or object.
   *
   * @return the completion criteria of type AiccCompletionCriteria associated with this instance
   */
  public AiccCompletionCriteria getCompletionCriteria() {
    return this.completionCriteria;
  }

  /**
   * Sets the completion criteria for the current object.
   *
   * @param completionCriteria the AICC completion criteria to be set
   */
  public void setCompletionCriteria(AiccCompletionCriteria completionCriteria) {
    this.completionCriteria = completionCriteria;
  }

  /**
   * Retrieves the normalized mastery score.
   *
   * @return the normalized mastery score as a Double, or null if it is not set.
   */
  public Double getMasteryScoreNormalized() {
    return this.masteryScoreNormalized;
  }

  /**
   * Sets the normalized mastery score for a learning object or module. The normalized score is
   * typically a value between 0 and 1 that indicates the mastery threshold.
   *
   * @param masteryScoreNormalized the normalized mastery score to set. Should be a Double
   * representing a value between 0.0 and 1.0.
   */
  public void setMasteryScoreNormalized(Double masteryScoreNormalized) {
    this.masteryScoreNormalized = masteryScoreNormalized;
  }

  /**
   * Retrieves the maximum time allowed normalized as a Duration object.
   *
   * @return the maximum normalized time allowed
   */
  public Duration getMaxTimeAllowedNormalized() {
    return this.maxTimeAllowedNormalized;
  }

  /**
   * Sets the maximum allowed time in a normalized format.
   *
   * @param maxTimeAllowedNormalized the maximum duration allowed, represented as a
   * {@link Duration}
   */
  public void setMaxTimeAllowedNormalized(Duration maxTimeAllowedNormalized) {
    this.maxTimeAllowedNormalized = maxTimeAllowedNormalized;
  }

  /**
   * Retrieves the list of time limit actions in a normalized format.
   *
   * @return a list of strings representing the normalized time limit actions
   */
  public List<String> getTimeLimitActionNormalized() {
    return this.timeLimitActionNormalized;
  }

  /**
   * Sets the normalized time limit actions for the assignable unit. If the input list is null, it
   * is replaced with an immutable empty list. Otherwise, the provided list is copied to ensure
   * immutability.
   *
   * @param timeLimitActionNormalized a list of normalized time limit actions; if null, it defaults
   * to an empty list
   */
  public void setTimeLimitActionNormalized(List<String> timeLimitActionNormalized) {
    this.timeLimitActionNormalized = timeLimitActionNormalized == null
        ? List.of()
        : List.copyOf(timeLimitActionNormalized);
  }

  /**
   * Retrieves the value indicating whether the prerequisites mandatory override is enabled.
   *
   * @return a Boolean indicating if the prerequisites mandatory override is set. Returns true if
   * enabled, false if disabled, or null if not set.
   */
  public Boolean getPrerequisitesMandatoryOverride() {
    return this.prerequisitesMandatoryOverride;
  }

  /**
   * Sets the prerequisites mandatory override flag.
   *
   * @param prerequisitesMandatoryOverride the value to set for indicating whether prerequisites are
   * mandatory to override
   */
  public void setPrerequisitesMandatoryOverride(Boolean prerequisitesMandatoryOverride) {
    this.prerequisitesMandatoryOverride = prerequisitesMandatoryOverride;
  }

  /**
   * Determines if prerequisites are mandatory for the Assignable Unit (AU). The determination is
   * based on a combination of the `prerequisitesMandatoryOverride` value and the
   * `prerequisiteModel` configuration. If `prerequisitesMandatoryOverride` is set, it takes
   * precedence; otherwise, the mandatory status of the `prerequisiteModel` is used.
   *
   * @return true if prerequisites are mandatory, false otherwise
   */
  public boolean isPrerequisitesMandatory() {
    return Objects.requireNonNullElseGet(this.prerequisitesMandatoryOverride,
        () -> this.prerequisiteModel != null && this.prerequisiteModel.isMandatory());
  }

  /**
   * Retrieves a list of optional Assignable Unit (AU) IDs from the prerequisite model. If the
   * prerequisite model is not defined, an empty list is returned.
   *
   * @return a list of optional AU IDs, or an empty list if the prerequisite model is not set
   */
  public List<String> getPrerequisiteOptionalAuIds() {
    if (this.prerequisiteModel == null) {
      return List.of();
    }
    return this.prerequisiteModel.getOptionalAuIds();
  }

  /**
   * Retrieves a list of referenced Assignable Unit (AU) IDs from the prerequisite model. If the
   * prerequisite model is not defined, an empty list is returned.
   *
   * @return a list of referenced AU IDs, or an empty list if the prerequisite model is not set
   */
  public List<String> getPrerequisiteReferencedAuIds() {
    if (this.prerequisiteModel == null) {
      return List.of();
    }
    return this.prerequisiteModel.getReferencedAuIds();
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
        .append(getPrerequisitesExpression(), that.getPrerequisitesExpression())
        .append(getPrerequisiteModel(), that.getPrerequisiteModel())
        .append(getCompletionCriteria(), that.getCompletionCriteria())
        .append(getMasteryScoreNormalized(), that.getMasteryScoreNormalized())
        .append(getMaxTimeAllowedNormalized(), that.getMaxTimeAllowedNormalized())
        .append(getTimeLimitActionNormalized(), that.getTimeLimitActionNormalized())
        .append(getPrerequisitesMandatoryOverride(), that.getPrerequisitesMandatoryOverride())
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
        .append(getPrerequisitesExpression())
        .append(getPrerequisiteModel())
        .append(getCompletionCriteria())
        .append(getMasteryScoreNormalized())
        .append(getMaxTimeAllowedNormalized())
        .append(getTimeLimitActionNormalized())
        .append(getPrerequisitesMandatoryOverride())
        .toHashCode();
  }
}
