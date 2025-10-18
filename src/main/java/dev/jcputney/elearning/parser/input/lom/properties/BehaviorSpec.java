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
import java.math.BigDecimal;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * <p>Represents the <strong>behaviorSpec</strong> complex type.</p>
 *
 * <p>The following schema snippet specifies its contents:</p>
 * <pre>{@code
 * <xs:complexType name="behaviorSpec">
 *   <xs:all>
 *     <xs:element name="launch" type="launchSpec" minOccurs="0" maxOccurs="1" />
 *     <xs:element name="exitActions" type="exitActionsSpec" minOccurs="0" maxOccurs="1" />
 *     <xs:element name="communications" type="communicationsSpec" minOccurs="0" maxOccurs="1" />
 *     <xs:element name="debug" type="debugSpec" minOccurs="0" maxOccurs="1" />
 *     <xs:element name="history" type="captureHistorySpec" minOccurs="0" maxOccurs="1" />
 *     <xs:element name="disableRightClick" type="yesNoType" minOccurs="0" maxOccurs="1" />
 *     <xs:element name="preventWindowResize" type="yesNoType" minOccurs="0" maxOccurs="1" />
 *     <xs:element name="scoreRollupMode" type="scoreRollupType" minOccurs="0" maxOccurs="1" />
 *     <xs:element name="numberOfScoringObjects" type="xs:int" minOccurs="0" maxOccurs="1" />
 *     <xs:element name="statusRollupMode" type="statusRollupType" minOccurs="0" maxOccurs="1" />
 *     <xs:element name="thresholdScoreForCompletion" type="xs:decimal" minOccurs="0" maxOccurs="1" />
 *     <xs:element name="applyRollupStatusToSuccess" type="yesNoType" minOccurs="0" maxOccurs="1" />
 *     <xs:element name="firstScoIsPretest" type="yesNoType" minOccurs="0" maxOccurs="1" />
 *     <xs:element name="finishCausesImmediateCommit" type="yesNoType" minOccurs="0" maxOccurs="1" />
 *     <xs:element name="invalidMenuItemAction" type="invalidMenuItemActionType" minOccurs="0" maxOccurs="1" />
 *     <xs:element name="alwaysFlowToFirstSco" type="yesNoType" minOccurs="0" maxOccurs="1" />
 *     <xs:element name="logoutCausesPlayerExit" type="yesNoType" minOccurs="0" maxOccurs="1" />
 *     <xs:element name="resetRtTiming" type="resetRunTimeDataTimingType" minOccurs="0" maxOccurs="1" />
 *     <xs:element name="lookaheadSequencerMode" type="lookaheadSequencerModeType" minOccurs="0" maxOccurs="1" />
 *     <xs:element name="scoreOverridesStatus" type="yesNoType" minOccurs="0" maxOccurs="1" />
 *     <xs:element name="allowCompleteStatusChange" type="yesNoType" minOccurs="0" maxOccurs="1" />
 *     <xs:element name="scaleRawScore" type="yesNoType" minOccurs="0" maxOccurs="1" />
 *     <xs:element name="useQuickLookaheadSequencer" type="yesNoType" minOccurs="0" maxOccurs="1" />
 *     <xs:element name="rollupRuntimeAtScoUnload" type="yesNoType" minOccurs="0" maxOccurs="1" />
 *     <xs:element name="forceObjectiveCompletionSetByContent" type="yesNoType" minOccurs="0" maxOccurs="1" />
 *     <xs:element name="invokeRollupAtSuspendAll" type="yesNoType" minOccurs="0" maxOccurs="1" />
 *     <xs:element name="completionStatOfFailedSuccessStat" type="completionStatusType" minOccurs="0" maxOccurs="1" />
 *     <xs:element name="satisfiedCausesCompletion" type="yesNoType" minOccurs="0" maxOccurs="1" />
 *     <xs:element name="makeStudentPrefsGlobalToCourse" type="yesNoType" minOccurs="0" maxOccurs="1" />
 *     <xs:element name="returnToLmsAction" type="returnToLmsActionType" minOccurs="0" maxOccurs="1" />
 *     <xs:element name="rollupEmptySetToUnknown" type="yesNoType" minOccurs="0" maxOccurs="1" />
 *     <xs:element name="validateInteractionTypes" type="yesNoType" minOccurs="0" maxOccurs="1" />
 *     <xs:element name="suspendDataMaxLength" type="xs:int" minOccurs="0" maxOccurs="1" />
 *     <xs:element name="timeLimit" type="xs:int" minOccurs="0" maxOccurs="1" />
 *   </xs:all>
 * </xs:complexType>
 * }</pre>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonFormat(with = Feature.ACCEPT_CASE_INSENSITIVE_PROPERTIES)
public final class BehaviorSpec implements Serializable {

  /**
   * The launch specification.
   */
  @JacksonXmlProperty(localName = "launch")
  private LaunchSpec launch;

  /**
   * The exit actions specification.
   */
  @JacksonXmlProperty(localName = "exitActions")
  private ExitActionsSpec exitActions;

  /**
   * The communications specification.
   */
  @JacksonXmlProperty(localName = "communications")
  private CommunicationsSpec communications;

  /**
   * The debug specification.
   */
  @JacksonXmlProperty(localName = "debug")
  private DebugSpec debug;

  /**
   * The capture history specification.
   */
  @JacksonXmlProperty(localName = "history")
  private CaptureHistorySpec history;

  /**
   * The flag indicating whether to disable right-click.
   */
  @JacksonXmlProperty(localName = "disableRightClick")
  private YesNoType disableRightClick;

  /**
   * The flag indicating whether to prevent the window resizing.
   */
  @JacksonXmlProperty(localName = "preventWindowResize")
  private YesNoType preventWindowResize;

  /**
   * The score rollup mode.
   */
  @JacksonXmlProperty(localName = "scoreRollupMode")
  private ScoreRollupType scoreRollupMode;

  /**
   * The number of scoring objects.
   */
  @JacksonXmlProperty(localName = "numberOfScoringObjects")
  private Integer numberOfScoringObjects;

  /**
   * The status rollup mode.
   */
  @JacksonXmlProperty(localName = "statusRollupMode")
  private StatusRollupType statusRollupMode;

  /**
   * The threshold score for completion.
   */
  @JacksonXmlProperty(localName = "thresholdScoreForCompletion")
  private BigDecimal thresholdScoreForCompletion;

  /**
   * The flag indicating whether to apply rollup status to success.
   */
  @JacksonXmlProperty(localName = "applyRollupStatusToSuccess")
  private YesNoType applyRollupStatusToSuccess;

  /**
   * The flag indicating whether the first SCO is a pretest.
   */
  @JacksonXmlProperty(localName = "firstScoIsPretest")
  private YesNoType firstScoIsPretest;

  /**
   * The flag indicating whether to finish causes an immediate commit.
   */
  @JacksonXmlProperty(localName = "finishCausesImmediateCommit")
  private YesNoType finishCausesImmediateCommit;

  /**
   * The invalid menu item action type.
   */
  @JacksonXmlProperty(localName = "invalidMenuItemAction")
  private InvalidMenuItemActionType invalidMenuItemAction;

  /**
   * The flag indicating whether to always flow to the first SCO.
   */
  @JacksonXmlProperty(localName = "alwaysFlowToFirstSco")
  private YesNoType alwaysFlowToFirstSco;

  /**
   * The flag indicating whether logout causes player exit.
   */
  @JacksonXmlProperty(localName = "logoutCausesPlayerExit")
  private YesNoType logoutCausesPlayerExit;

  /**
   * The reset run time data timing type.
   */
  @JacksonXmlProperty(localName = "resetRtTiming")
  private ResetRunTimeDataTimingType resetRtTiming;

  /**
   * The lookahead sequencer mode type.
   */
  @JacksonXmlProperty(localName = "lookaheadSequencerMode")
  private LookaheadSequencerModeType lookaheadSequencerMode;

  /**
   * The flag indicating whether the score overrides' status.
   */
  @JacksonXmlProperty(localName = "scoreOverridesStatus")
  private YesNoType scoreOverridesStatus;

  /**
   * The flag indicating whether to allow complete status change.
   */
  @JacksonXmlProperty(localName = "allowCompleteStatusChange")
  private YesNoType allowCompleteStatusChange;

  /**
   * The flag indicating whether to scale a raw score.
   */
  @JacksonXmlProperty(localName = "scaleRawScore")
  private YesNoType scaleRawScore;

  /**
   * The flag indicating whether to use a quick lookahead sequencer.
   */
  @JacksonXmlProperty(localName = "useQuickLookaheadSequencer")
  private YesNoType useQuickLookaheadSequencer;

  /**
   * The flag indicating whether to roll up runtime at SCO unloaded.
   */
  @JacksonXmlProperty(localName = "rollupRuntimeAtScoUnload")
  private YesNoType rollupRuntimeAtScoUnload;

  /**
   * The flag indicating whether to force objective completion set by content.
   */
  @JacksonXmlProperty(localName = "forceObjectiveCompletionSetByContent")
  private YesNoType forceObjectiveCompletionSetByContent;

  /**
   * The flag indicating whether to invoke rollup at suspend all.
   */
  @JacksonXmlProperty(localName = "invokeRollupAtSuspendAll")
  private YesNoType invokeRollupAtSuspendAll;

  /**
   * The completion status type for failed success status.
   */
  @JacksonXmlProperty(localName = "completionStatOfFailedSuccessStat")
  private CompletionStatusType completionStatOfFailedSuccessStat;

  /**
   * The flag indicating whether satisfied causes completion.
   */
  @JacksonXmlProperty(localName = "satisfiedCausesCompletion")
  private YesNoType satisfiedCausesCompletion;

  /**
   * The flag indicating whether to make student preferences global to the course.
   */
  @JacksonXmlProperty(localName = "makeStudentPrefsGlobalToCourse")
  private YesNoType makeStudentPrefsGlobalToCourse;

  /**
   * The return to LMS action type.
   */
  @JacksonXmlProperty(localName = "returnToLmsAction")
  private ReturnToLmsActionType returnToLmsAction;

  /**
   * The flag indicating whether to roll up empty set to unknown.
   */
  @JacksonXmlProperty(localName = "rollupEmptySetToUnknown")
  private YesNoType rollupEmptySetToUnknown;

  /**
   * The flag indicating whether to validate interaction types.
   */
  @JacksonXmlProperty(localName = "validateInteractionTypes")
  private YesNoType validateInteractionTypes;

  /**
   * The maximum length of suspend data.
   */
  @JacksonXmlProperty(localName = "suspendDataMaxLength")
  private Integer suspendDataMaxLength;

  /**
   * The time limit for the course.
   */
  @JacksonXmlProperty(localName = "timeLimit")
  private Integer timeLimit;

  /**
   * Constructs a new instance of BehaviorSpec.
   * <p>
   * This constructor initializes the BehaviorSpec object with its default state. It does not
   * perform any operations or take parameters.
   */
  public BehaviorSpec() {
    // no-op
  }

  /**
   * Retrieves the launch specification.
   *
   * @return the LaunchSpec instance associated with this behavior.
   */
  public LaunchSpec getLaunch() {
    return this.launch;
  }

  /**
   * Sets the launch specification for the behavior.
   *
   * @param launch the LaunchSpec instance to associate with this behavior
   */
  public void setLaunch(LaunchSpec launch) {
    this.launch = launch;
  }

  /**
   * Retrieves the exit actions specification for the behavior.
   *
   * @return the ExitActionsSpec instance associated with this behavior.
   */
  public ExitActionsSpec getExitActions() {
    return this.exitActions;
  }

  /**
   * Sets the exit actions specification for the behavior.
   *
   * @param exitActions the ExitActionsSpec instance to associate with this behavior
   */
  public void setExitActions(ExitActionsSpec exitActions) {
    this.exitActions = exitActions;
  }

  /**
   * Retrieves the communications specification for the behavior.
   *
   * @return the CommunicationsSpec instance associated with this behavior.
   */
  public CommunicationsSpec getCommunications() {
    return this.communications;
  }

  /**
   * Sets the communications specification for the behavior.
   *
   * @param communications the CommunicationsSpec instance to associate with this behavior
   */
  public void setCommunications(CommunicationsSpec communications) {
    this.communications = communications;
  }

  /**
   * Retrieves the debug specification for the behavior.
   *
   * @return the DebugSpec instance associated with this behavior.
   */
  public DebugSpec getDebug() {
    return this.debug;
  }

  /**
   * Sets the debug specification for the behavior.
   *
   * @param debug the DebugSpec instance to associate with this behavior
   */
  public void setDebug(DebugSpec debug) {
    this.debug = debug;
  }

  /**
   * Retrieves the capture history specification for the behavior.
   *
   * @return the CaptureHistorySpec instance associated with this behavior.
   */
  public CaptureHistorySpec getHistory() {
    return this.history;
  }

  /**
   * Sets the capture history specification for the behavior.
   *
   * @param history the CaptureHistorySpec instance to associate with this behavior
   */
  public void setHistory(CaptureHistorySpec history) {
    this.history = history;
  }

  /**
   * Retrieves the disable-right-click configuration for the behavior.
   *
   * @return the YesNoType value indicating whether right-click is disabled.
   */
  public YesNoType getDisableRightClick() {
    return this.disableRightClick;
  }

  /**
   * Configures whether right-click functionality is disabled for this behavior.
   *
   * @param disableRightClick the YesNoType value indicating whether right-click functionality
   * should be disabled.
   */
  public void setDisableRightClick(YesNoType disableRightClick) {
    this.disableRightClick = disableRightClick;
  }

  /**
   * Retrieves the configuration indicating whether window resize prevention is enabled.
   *
   * @return the YesNoType value indicating whether window resizing is prevented.
   */
  public YesNoType getPreventWindowResize() {
    return this.preventWindowResize;
  }

  /**
   * Configures whether window resizing should be prevented for this behavior.
   *
   * @param preventWindowResize the YesNoType value indicating whether window resizing should be
   * disabled (YES/NO/TRUE/FALSE).
   */
  public void setPreventWindowResize(YesNoType preventWindowResize) {
    this.preventWindowResize = preventWindowResize;
  }

  /**
   * Retrieves the score rollup mode setting for this behavior.
   *
   * @return the ScoreRollupType value indicating the current score rollup mode configuration.
   */
  public ScoreRollupType getScoreRollupMode() {
    return this.scoreRollupMode;
  }

  /**
   * Sets the score rollup mode for the behavior.
   *
   * @param scoreRollupMode the ScoreRollupType value to define how score rollup should be
   * configured
   */
  public void setScoreRollupMode(ScoreRollupType scoreRollupMode) {
    this.scoreRollupMode = scoreRollupMode;
  }

  /**
   * Retrieves the number of scoring objects associated with the behavior.
   *
   * @return the number of scoring objects as an Integer.
   */
  public Integer getNumberOfScoringObjects() {
    return this.numberOfScoringObjects;
  }

  /**
   * Sets the number of scoring objects associated with the behavior.
   *
   * @param numberOfScoringObjects the Integer value representing the number of scoring objects to
   * be set
   */
  public void setNumberOfScoringObjects(Integer numberOfScoringObjects) {
    this.numberOfScoringObjects = numberOfScoringObjects;
  }

  /**
   * Retrieves the current status rollup mode.
   *
   * @return the status rollup mode of type {@code StatusRollupType}
   */
  public StatusRollupType getStatusRollupMode() {
    return this.statusRollupMode;
  }

  /**
   * Sets the status rollup mode for the current instance.
   *
   * @param statusRollupMode the status rollup mode to be applied. This determines how statuses are
   * aggregated or rolled up.
   */
  public void setStatusRollupMode(StatusRollupType statusRollupMode) {
    this.statusRollupMode = statusRollupMode;
  }

  /**
   * Retrieves the threshold score required for completion.
   *
   * @return the threshold score as a BigDecimal value.
   */
  public BigDecimal getThresholdScoreForCompletion() {
    return this.thresholdScoreForCompletion;
  }

  /**
   * Sets the threshold score required for completion.
   *
   * @param thresholdScoreForCompletion the score that represents the completion threshold
   */
  public void setThresholdScoreForCompletion(BigDecimal thresholdScoreForCompletion) {
    this.thresholdScoreForCompletion = thresholdScoreForCompletion;
  }

  /**
   * Retrieves the current rollup status indicating whether it should be applied to success.
   *
   * @return a YesNoType value representing whether rollup status is applied to success
   */
  public YesNoType getApplyRollupStatusToSuccess() {
    return this.applyRollupStatusToSuccess;
  }

  /**
   * Sets the apply rollup status to success.
   *
   * @param applyRollupStatusToSuccess The value indicating whether the rollup status should be
   * applied as a success.
   */
  public void setApplyRollupStatusToSuccess(YesNoType applyRollupStatusToSuccess) {
    this.applyRollupStatusToSuccess = applyRollupStatusToSuccess;
  }

  /**
   * Retrieves the value indicating whether the first SCO (Shareable Content Object) is a pretest.
   *
   * @return a YesNoType value representing whether the first SCO is a pretest.
   */
  public YesNoType getFirstScoIsPretest() {
    return this.firstScoIsPretest;
  }

  /**
   * Sets the value indicating whether the first SCO (Shareable Content Object) is a pretest.
   *
   * @param firstScoIsPretest an instance of YesNoType that specifies if the first SCO is a pretest
   */
  public void setFirstScoIsPretest(YesNoType firstScoIsPretest) {
    this.firstScoIsPretest = firstScoIsPretest;
  }

  /**
   * Retrieves the value indicating whether finishing causes an immediate commit.
   *
   * @return the value of finishCausesImmediateCommit, represented as YesNoType.
   */
  public YesNoType getFinishCausesImmediateCommit() {
    return this.finishCausesImmediateCommit;
  }

  /**
   * Sets whether finishing causes an immediate commit.
   *
   * @param finishCausesImmediateCommit A {@code YesNoType} value indicating whether the finish
   * action should cause an immediate commit.
   */
  public void setFinishCausesImmediateCommit(YesNoType finishCausesImmediateCommit) {
    this.finishCausesImmediateCommit = finishCausesImmediateCommit;
  }

  /**
   * Retrieves the action type associated with invalid menu items.
   *
   * @return the {@code InvalidMenuItemActionType} representing the action to be taken for invalid
   * menu items.
   */
  public InvalidMenuItemActionType getInvalidMenuItemAction() {
    return this.invalidMenuItemAction;
  }

  /**
   * Sets the action to be taken when an invalid menu item is encountered.
   *
   * @param invalidMenuItemAction the action to set, represented by an InvalidMenuItemActionType
   * enumeration
   */
  public void setInvalidMenuItemAction(InvalidMenuItemActionType invalidMenuItemAction) {
    this.invalidMenuItemAction = invalidMenuItemAction;
  }

  /**
   * Retrieves the value indicating whether the flow should always go to the first SCO (Sharable
   * Content Object).
   *
   * @return a YesNoType value that specifies whether the flow always goes to the first SCO.
   */
  public YesNoType getAlwaysFlowToFirstSco() {
    return this.alwaysFlowToFirstSco;
  }

  /**
   * Sets whether the flow should always proceed to the first SCO (Sharable Content Object).
   *
   * @param alwaysFlowToFirstSco an instance of YesNoType indicating whether the flow should always
   * start at the first SCO.
   */
  public void setAlwaysFlowToFirstSco(YesNoType alwaysFlowToFirstSco) {
    this.alwaysFlowToFirstSco = alwaysFlowToFirstSco;
  }

  /**
   * Retrieves whether the player's logout causes their exit.
   *
   * @return A YesNoType value indicating if logout causes the player to exit.
   */
  public YesNoType getLogoutCausesPlayerExit() {
    return this.logoutCausesPlayerExit;
  }

  /**
   * Sets whether logging out causes the player to exit.
   *
   * @param logoutCausesPlayerExit specifies if the player exit should occur on logout.
   */
  public void setLogoutCausesPlayerExit(YesNoType logoutCausesPlayerExit) {
    this.logoutCausesPlayerExit = logoutCausesPlayerExit;
  }

  /**
   * Retrieves the timing configuration for resetting runtime data.
   *
   * @return the reset runtime data timing configuration as a ResetRunTimeDataTimingType object.
   */
  public ResetRunTimeDataTimingType getResetRtTiming() {
    return this.resetRtTiming;
  }

  /**
   * Sets the reset runtime data timing.
   *
   * @param resetRtTiming the ResetRunTimeDataTimingType object representing the reset runtime data
   * timing to be set
   */
  public void setResetRtTiming(ResetRunTimeDataTimingType resetRtTiming) {
    this.resetRtTiming = resetRtTiming;
  }

  /**
   * Retrieves the current mode of the lookahead sequencer.
   *
   * @return the {@code LookaheadSequencerModeType} representing the current mode of the lookahead
   * sequencer.
   */
  public LookaheadSequencerModeType getLookaheadSequencerMode() {
    return this.lookaheadSequencerMode;
  }

  /**
   * Sets the lookahead sequencer mode for the current instance.
   *
   * @param lookaheadSequencerMode the lookahead sequencer mode to be set, represented as a
   * {@code LookaheadSequencerModeType}.
   */
  public void setLookaheadSequencerMode(LookaheadSequencerModeType lookaheadSequencerMode) {
    this.lookaheadSequencerMode = lookaheadSequencerMode;
  }

  /**
   * Retrieves the status of score overrides.
   *
   * @return an instance of YesNoType representing the score overrides status
   */
  public YesNoType getScoreOverridesStatus() {
    return this.scoreOverridesStatus;
  }

  /**
   * Sets the status for score overrides.
   *
   * @param scoreOverridesStatus the status indicating whether score overrides are enabled or not,
   * represented as a YesNoType value
   */
  public void setScoreOverridesStatus(YesNoType scoreOverridesStatus) {
    this.scoreOverridesStatus = scoreOverridesStatus;
  }

  /**
   * Retrieves the current status indicating whether a complete status change is allowed.
   *
   * @return the allowCompleteStatusChange value as a YesNoType.
   */
  public YesNoType getAllowCompleteStatusChange() {
    return this.allowCompleteStatusChange;
  }

  /**
   * Sets whether the status change to 'complete' is allowed or not.
   *
   * @param allowCompleteStatusChange a YesNoType value indicating if the status change to
   * 'complete' is permitted
   */
  public void setAllowCompleteStatusChange(YesNoType allowCompleteStatusChange) {
    this.allowCompleteStatusChange = allowCompleteStatusChange;
  }

  /**
   * Retrieves the raw score associated with the scale.
   *
   * @return the scale raw score of type YesNoType
   */
  public YesNoType getScaleRawScore() {
    return this.scaleRawScore;
  }

  /**
   * Sets the value of the scaleRawScore field.
   *
   * @param scaleRawScore the YesNoType value to set for scaleRawScore
   */
  public void setScaleRawScore(YesNoType scaleRawScore) {
    this.scaleRawScore = scaleRawScore;
  }

  /**
   * Retrieves the current setting for the quick lookahead sequencer usage.
   *
   * @return a YesNoType value indicating whether the quick lookahead sequencer is enabled or
   * disabled.
   */
  public YesNoType getUseQuickLookaheadSequencer() {
    return this.useQuickLookaheadSequencer;
  }

  /**
   * Sets the flag to determine whether to use the quick lookahead sequencer.
   *
   * @param useQuickLookaheadSequencer the value indicating whether to enable or disable the quick
   * lookahead sequencer
   */
  public void setUseQuickLookaheadSequencer(YesNoType useQuickLookaheadSequencer) {
    this.useQuickLookaheadSequencer = useQuickLookaheadSequencer;
  }

  /**
   * Retrieves the rollup runtime state at the time of SCO (Shareable Content Object) unload.
   *
   * @return a YesNoType value representing whether rollup runtime calculations occur at SCO unload.
   */
  public YesNoType getRollupRuntimeAtScoUnload() {
    return this.rollupRuntimeAtScoUnload;
  }

  /**
   * Sets whether the rollup runtime should be triggered during SCO unload.
   *
   * @param rollupRuntimeAtScoUnload a YesNoType value indicating whether to enable or disable the
   * rollup runtime at SCO unload
   */
  public void setRollupRuntimeAtScoUnload(YesNoType rollupRuntimeAtScoUnload) {
    this.rollupRuntimeAtScoUnload = rollupRuntimeAtScoUnload;
  }

  /**
   * Retrieves the value indicating whether the objective completion is forcibly set by content.
   *
   * @return YesNoType value representing whether the objective completion is forcibly set by
   * content.
   */
  public YesNoType getForceObjectiveCompletionSetByContent() {
    return this.forceObjectiveCompletionSetByContent;
  }

  /**
   * Sets the force objective completion state based on the provided content.
   *
   * @param forceObjectiveCompletionSetByContent the value indicating whether the objective
   * completion should be forced by the content
   */
  public void setForceObjectiveCompletionSetByContent(
      YesNoType forceObjectiveCompletionSetByContent) {
    this.forceObjectiveCompletionSetByContent = forceObjectiveCompletionSetByContent;
  }

  /**
   * Retrieves the current value of the invokeRollupAtSuspendAll property.
   *
   * @return the value of invokeRollupAtSuspendAll of type YesNoType
   */
  public YesNoType getInvokeRollupAtSuspendAll() {
    return this.invokeRollupAtSuspendAll;
  }

  /**
   * Sets the invokeRollupAtSuspendAll flag to the specified value.
   *
   * @param invokeRollupAtSuspendAll the value indicating whether to invoke rollup logic at
   * suspend-all operation
   */
  public void setInvokeRollupAtSuspendAll(YesNoType invokeRollupAtSuspendAll) {
    this.invokeRollupAtSuspendAll = invokeRollupAtSuspendAll;
  }

  /**
   * Retrieves the completion status corresponding to the failed success status.
   *
   * @return the completion status type associated with the failed success status
   */
  public CompletionStatusType getCompletionStatOfFailedSuccessStat() {
    return this.completionStatOfFailedSuccessStat;
  }

  /**
   * Sets the completion status for a failed success status.
   *
   * @param completionStatOfFailedSuccessStat the completion status to be set for the failed success
   * status
   */
  public void setCompletionStatOfFailedSuccessStat(
      CompletionStatusType completionStatOfFailedSuccessStat) {
    this.completionStatOfFailedSuccessStat = completionStatOfFailedSuccessStat;
  }

  /**
   * Retrieves the value of the satisfiedCausesCompletion property.
   *
   * @return the YesNoType value indicating whether the satisfaction causes completion.
   */
  public YesNoType getSatisfiedCausesCompletion() {
    return this.satisfiedCausesCompletion;
  }

  /**
   * Sets the value indicating whether the satisfaction causes completion.
   *
   * @param satisfiedCausesCompletion a YesNoType value representing whether the satisfaction of a
   * condition causes completion
   */
  public void setSatisfiedCausesCompletion(YesNoType satisfiedCausesCompletion) {
    this.satisfiedCausesCompletion = satisfiedCausesCompletion;
  }

  /**
   * Retrieves the setting that determines if student preferences are made global to the course.
   *
   * @return the YesNoType value indicating whether the student preferences are global to the course
   */
  public YesNoType getMakeStudentPrefsGlobalToCourse() {
    return this.makeStudentPrefsGlobalToCourse;
  }

  /**
   * Sets whether the student preferences should be made global to the course.
   *
   * @param makeStudentPrefsGlobalToCourse a YesNoType value indicating whether student preferences
   * are global to the course
   */
  public void setMakeStudentPrefsGlobalToCourse(YesNoType makeStudentPrefsGlobalToCourse) {
    this.makeStudentPrefsGlobalToCourse = makeStudentPrefsGlobalToCourse;
  }

  /**
   * Retrieves the action type associated with returning to the Learning Management System (LMS).
   *
   * @return the type of action that defines how to return to the LMS, represented as a
   * {@code ReturnToLmsActionType} object.
   */
  public ReturnToLmsActionType getReturnToLmsAction() {
    return this.returnToLmsAction;
  }

  /**
   * Sets the action to be taken when returning to the Learning Management System (LMS).
   *
   * @param returnToLmsAction the action type to be set for returning to the LMS
   */
  public void setReturnToLmsAction(ReturnToLmsActionType returnToLmsAction) {
    this.returnToLmsAction = returnToLmsAction;
  }

  /**
   * Retrieves the rollup behavior for handling an empty set, which determines whether the system
   * should interpret it as an unknown state.
   *
   * @return The current configuration setting for rollupEmptySetToUnknown, represented as a
   * YesNoType value.
   */
  public YesNoType getRollupEmptySetToUnknown() {
    return this.rollupEmptySetToUnknown;
  }

  /**
   * Sets the behavior for handling roll-ups when the input set is empty. Determines whether an
   * empty set should be treated as unknown in roll-up calculations.
   *
   * @param rollupEmptySetToUnknown the value that specifies how empty sets are processed in
   * roll-ups. A value of {@code Yes} indicates treating the empty set as unknown, while {@code No}
   * indicates not treating it as unknown.
   */
  public void setRollupEmptySetToUnknown(YesNoType rollupEmptySetToUnknown) {
    this.rollupEmptySetToUnknown = rollupEmptySetToUnknown;
  }

  /**
   * Retrieves the validate interaction types configured for this instance.
   *
   * @return the validate interaction types as a YesNoType value
   */
  public YesNoType getValidateInteractionTypes() {
    return this.validateInteractionTypes;
  }

  /**
   * Sets the option to validate interaction types.
   *
   * @param validateInteractionTypes a YesNoType indicating whether to enable or disable interaction
   * type validation
   */
  public void setValidateInteractionTypes(YesNoType validateInteractionTypes) {
    this.validateInteractionTypes = validateInteractionTypes;
  }

  /**
   * Retrieves the maximum length allowed for suspension data.
   *
   * @return the maximum length of the suspension data as an Integer
   */
  public Integer getSuspendDataMaxLength() {
    return this.suspendDataMaxLength;
  }

  /**
   * Sets the maximum length for suspend data.
   *
   * @param suspendDataMaxLength the maximum length of suspend data to be set
   */
  public void setSuspendDataMaxLength(Integer suspendDataMaxLength) {
    this.suspendDataMaxLength = suspendDataMaxLength;
  }

  /**
   * Retrieves the time limit value.
   *
   * @return the time limit as an Integer
   */
  public Integer getTimeLimit() {
    return this.timeLimit;
  }

  /**
   * Sets the time limit for the operation.
   *
   * @param timeLimit the time limit to set, in milliseconds. Should be a non-negative integer.
   */
  public void setTimeLimit(Integer timeLimit) {
    this.timeLimit = timeLimit;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (!(o instanceof BehaviorSpec that)) {
      return false;
    }

    return new EqualsBuilder()
        .append(getLaunch(), that.getLaunch())
        .append(getExitActions(), that.getExitActions())
        .append(getCommunications(), that.getCommunications())
        .append(getDebug(), that.getDebug())
        .append(getHistory(), that.getHistory())
        .append(getDisableRightClick(), that.getDisableRightClick())
        .append(getPreventWindowResize(), that.getPreventWindowResize())
        .append(getScoreRollupMode(), that.getScoreRollupMode())
        .append(getNumberOfScoringObjects(), that.getNumberOfScoringObjects())
        .append(getStatusRollupMode(), that.getStatusRollupMode())
        .append(getThresholdScoreForCompletion(), that.getThresholdScoreForCompletion())
        .append(getApplyRollupStatusToSuccess(), that.getApplyRollupStatusToSuccess())
        .append(getFirstScoIsPretest(), that.getFirstScoIsPretest())
        .append(getFinishCausesImmediateCommit(), that.getFinishCausesImmediateCommit())
        .append(getInvalidMenuItemAction(), that.getInvalidMenuItemAction())
        .append(getAlwaysFlowToFirstSco(), that.getAlwaysFlowToFirstSco())
        .append(getLogoutCausesPlayerExit(), that.getLogoutCausesPlayerExit())
        .append(getResetRtTiming(), that.getResetRtTiming())
        .append(getLookaheadSequencerMode(), that.getLookaheadSequencerMode())
        .append(getScoreOverridesStatus(), that.getScoreOverridesStatus())
        .append(getAllowCompleteStatusChange(), that.getAllowCompleteStatusChange())
        .append(getScaleRawScore(), that.getScaleRawScore())
        .append(getUseQuickLookaheadSequencer(), that.getUseQuickLookaheadSequencer())
        .append(getRollupRuntimeAtScoUnload(), that.getRollupRuntimeAtScoUnload())
        .append(getForceObjectiveCompletionSetByContent(),
            that.getForceObjectiveCompletionSetByContent())
        .append(getInvokeRollupAtSuspendAll(), that.getInvokeRollupAtSuspendAll())
        .append(getCompletionStatOfFailedSuccessStat(), that.getCompletionStatOfFailedSuccessStat())
        .append(getSatisfiedCausesCompletion(), that.getSatisfiedCausesCompletion())
        .append(getMakeStudentPrefsGlobalToCourse(), that.getMakeStudentPrefsGlobalToCourse())
        .append(getReturnToLmsAction(), that.getReturnToLmsAction())
        .append(getRollupEmptySetToUnknown(), that.getRollupEmptySetToUnknown())
        .append(getValidateInteractionTypes(), that.getValidateInteractionTypes())
        .append(getSuspendDataMaxLength(), that.getSuspendDataMaxLength())
        .append(getTimeLimit(), that.getTimeLimit())
        .isEquals();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder(17, 37)
        .append(getLaunch())
        .append(getExitActions())
        .append(getCommunications())
        .append(getDebug())
        .append(getHistory())
        .append(getDisableRightClick())
        .append(getPreventWindowResize())
        .append(getScoreRollupMode())
        .append(getNumberOfScoringObjects())
        .append(getStatusRollupMode())
        .append(getThresholdScoreForCompletion())
        .append(getApplyRollupStatusToSuccess())
        .append(getFirstScoIsPretest())
        .append(getFinishCausesImmediateCommit())
        .append(getInvalidMenuItemAction())
        .append(getAlwaysFlowToFirstSco())
        .append(getLogoutCausesPlayerExit())
        .append(getResetRtTiming())
        .append(getLookaheadSequencerMode())
        .append(getScoreOverridesStatus())
        .append(getAllowCompleteStatusChange())
        .append(getScaleRawScore())
        .append(getUseQuickLookaheadSequencer())
        .append(getRollupRuntimeAtScoUnload())
        .append(getForceObjectiveCompletionSetByContent())
        .append(getInvokeRollupAtSuspendAll())
        .append(getCompletionStatOfFailedSuccessStat())
        .append(getSatisfiedCausesCompletion())
        .append(getMakeStudentPrefsGlobalToCourse())
        .append(getReturnToLmsAction())
        .append(getRollupEmptySetToUnknown())
        .append(getValidateInteractionTypes())
        .append(getSuspendDataMaxLength())
        .append(getTimeLimit())
        .toHashCode();
  }
}
