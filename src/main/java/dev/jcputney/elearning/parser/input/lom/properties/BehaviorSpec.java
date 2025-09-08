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
public class BehaviorSpec implements Serializable {

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

  public BehaviorSpec() {
  }

  public LaunchSpec getLaunch() {
    return this.launch;
  }

  public void setLaunch(LaunchSpec launch) {
    this.launch = launch;
  }

  public ExitActionsSpec getExitActions() {
    return this.exitActions;
  }

  public void setExitActions(ExitActionsSpec exitActions) {
    this.exitActions = exitActions;
  }

  public CommunicationsSpec getCommunications() {
    return this.communications;
  }

  public void setCommunications(CommunicationsSpec communications) {
    this.communications = communications;
  }

  public DebugSpec getDebug() {
    return this.debug;
  }

  public void setDebug(DebugSpec debug) {
    this.debug = debug;
  }

  public CaptureHistorySpec getHistory() {
    return this.history;
  }

  public void setHistory(CaptureHistorySpec history) {
    this.history = history;
  }

  public YesNoType getDisableRightClick() {
    return this.disableRightClick;
  }

  public void setDisableRightClick(YesNoType disableRightClick) {
    this.disableRightClick = disableRightClick;
  }

  public YesNoType getPreventWindowResize() {
    return this.preventWindowResize;
  }

  public void setPreventWindowResize(YesNoType preventWindowResize) {
    this.preventWindowResize = preventWindowResize;
  }

  public ScoreRollupType getScoreRollupMode() {
    return this.scoreRollupMode;
  }

  public void setScoreRollupMode(ScoreRollupType scoreRollupMode) {
    this.scoreRollupMode = scoreRollupMode;
  }

  public Integer getNumberOfScoringObjects() {
    return this.numberOfScoringObjects;
  }

  public void setNumberOfScoringObjects(Integer numberOfScoringObjects) {
    this.numberOfScoringObjects = numberOfScoringObjects;
  }

  public StatusRollupType getStatusRollupMode() {
    return this.statusRollupMode;
  }

  public void setStatusRollupMode(StatusRollupType statusRollupMode) {
    this.statusRollupMode = statusRollupMode;
  }

  public BigDecimal getThresholdScoreForCompletion() {
    return this.thresholdScoreForCompletion;
  }

  public void setThresholdScoreForCompletion(BigDecimal thresholdScoreForCompletion) {
    this.thresholdScoreForCompletion = thresholdScoreForCompletion;
  }

  public YesNoType getApplyRollupStatusToSuccess() {
    return this.applyRollupStatusToSuccess;
  }

  public void setApplyRollupStatusToSuccess(YesNoType applyRollupStatusToSuccess) {
    this.applyRollupStatusToSuccess = applyRollupStatusToSuccess;
  }

  public YesNoType getFirstScoIsPretest() {
    return this.firstScoIsPretest;
  }

  public void setFirstScoIsPretest(YesNoType firstScoIsPretest) {
    this.firstScoIsPretest = firstScoIsPretest;
  }

  public YesNoType getFinishCausesImmediateCommit() {
    return this.finishCausesImmediateCommit;
  }

  public void setFinishCausesImmediateCommit(YesNoType finishCausesImmediateCommit) {
    this.finishCausesImmediateCommit = finishCausesImmediateCommit;
  }

  public InvalidMenuItemActionType getInvalidMenuItemAction() {
    return this.invalidMenuItemAction;
  }

  public void setInvalidMenuItemAction(InvalidMenuItemActionType invalidMenuItemAction) {
    this.invalidMenuItemAction = invalidMenuItemAction;
  }

  public YesNoType getAlwaysFlowToFirstSco() {
    return this.alwaysFlowToFirstSco;
  }

  public void setAlwaysFlowToFirstSco(YesNoType alwaysFlowToFirstSco) {
    this.alwaysFlowToFirstSco = alwaysFlowToFirstSco;
  }

  public YesNoType getLogoutCausesPlayerExit() {
    return this.logoutCausesPlayerExit;
  }

  public void setLogoutCausesPlayerExit(YesNoType logoutCausesPlayerExit) {
    this.logoutCausesPlayerExit = logoutCausesPlayerExit;
  }

  public ResetRunTimeDataTimingType getResetRtTiming() {
    return this.resetRtTiming;
  }

  public void setResetRtTiming(ResetRunTimeDataTimingType resetRtTiming) {
    this.resetRtTiming = resetRtTiming;
  }

  public LookaheadSequencerModeType getLookaheadSequencerMode() {
    return this.lookaheadSequencerMode;
  }

  public void setLookaheadSequencerMode(LookaheadSequencerModeType lookaheadSequencerMode) {
    this.lookaheadSequencerMode = lookaheadSequencerMode;
  }

  public YesNoType getScoreOverridesStatus() {
    return this.scoreOverridesStatus;
  }

  public void setScoreOverridesStatus(YesNoType scoreOverridesStatus) {
    this.scoreOverridesStatus = scoreOverridesStatus;
  }

  public YesNoType getAllowCompleteStatusChange() {
    return this.allowCompleteStatusChange;
  }

  public void setAllowCompleteStatusChange(YesNoType allowCompleteStatusChange) {
    this.allowCompleteStatusChange = allowCompleteStatusChange;
  }

  public YesNoType getScaleRawScore() {
    return this.scaleRawScore;
  }

  public void setScaleRawScore(YesNoType scaleRawScore) {
    this.scaleRawScore = scaleRawScore;
  }

  public YesNoType getUseQuickLookaheadSequencer() {
    return this.useQuickLookaheadSequencer;
  }

  public void setUseQuickLookaheadSequencer(YesNoType useQuickLookaheadSequencer) {
    this.useQuickLookaheadSequencer = useQuickLookaheadSequencer;
  }

  public YesNoType getRollupRuntimeAtScoUnload() {
    return this.rollupRuntimeAtScoUnload;
  }

  public void setRollupRuntimeAtScoUnload(YesNoType rollupRuntimeAtScoUnload) {
    this.rollupRuntimeAtScoUnload = rollupRuntimeAtScoUnload;
  }

  public YesNoType getForceObjectiveCompletionSetByContent() {
    return this.forceObjectiveCompletionSetByContent;
  }

  public void setForceObjectiveCompletionSetByContent(
      YesNoType forceObjectiveCompletionSetByContent) {
    this.forceObjectiveCompletionSetByContent = forceObjectiveCompletionSetByContent;
  }

  public YesNoType getInvokeRollupAtSuspendAll() {
    return this.invokeRollupAtSuspendAll;
  }

  public void setInvokeRollupAtSuspendAll(YesNoType invokeRollupAtSuspendAll) {
    this.invokeRollupAtSuspendAll = invokeRollupAtSuspendAll;
  }

  public CompletionStatusType getCompletionStatOfFailedSuccessStat() {
    return this.completionStatOfFailedSuccessStat;
  }

  public void setCompletionStatOfFailedSuccessStat(
      CompletionStatusType completionStatOfFailedSuccessStat) {
    this.completionStatOfFailedSuccessStat = completionStatOfFailedSuccessStat;
  }

  public YesNoType getSatisfiedCausesCompletion() {
    return this.satisfiedCausesCompletion;
  }

  public void setSatisfiedCausesCompletion(YesNoType satisfiedCausesCompletion) {
    this.satisfiedCausesCompletion = satisfiedCausesCompletion;
  }

  public YesNoType getMakeStudentPrefsGlobalToCourse() {
    return this.makeStudentPrefsGlobalToCourse;
  }

  public void setMakeStudentPrefsGlobalToCourse(YesNoType makeStudentPrefsGlobalToCourse) {
    this.makeStudentPrefsGlobalToCourse = makeStudentPrefsGlobalToCourse;
  }

  public ReturnToLmsActionType getReturnToLmsAction() {
    return this.returnToLmsAction;
  }

  public void setReturnToLmsAction(ReturnToLmsActionType returnToLmsAction) {
    this.returnToLmsAction = returnToLmsAction;
  }

  public YesNoType getRollupEmptySetToUnknown() {
    return this.rollupEmptySetToUnknown;
  }

  public void setRollupEmptySetToUnknown(YesNoType rollupEmptySetToUnknown) {
    this.rollupEmptySetToUnknown = rollupEmptySetToUnknown;
  }

  public YesNoType getValidateInteractionTypes() {
    return this.validateInteractionTypes;
  }

  public void setValidateInteractionTypes(YesNoType validateInteractionTypes) {
    this.validateInteractionTypes = validateInteractionTypes;
  }

  public Integer getSuspendDataMaxLength() {
    return this.suspendDataMaxLength;
  }

  public void setSuspendDataMaxLength(Integer suspendDataMaxLength) {
    this.suspendDataMaxLength = suspendDataMaxLength;
  }

  public Integer getTimeLimit() {
    return this.timeLimit;
  }

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
