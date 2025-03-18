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
import java.math.BigDecimal;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;

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
@Builder
@Getter
@Jacksonized
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonFormat(with = Feature.ACCEPT_CASE_INSENSITIVE_PROPERTIES)
public class BehaviorSpec {

  @JacksonXmlProperty(localName = "launch")
  private LaunchSpec launch;

  @JacksonXmlProperty(localName = "exitActions")
  private ExitActionsSpec exitActions;

  @JacksonXmlProperty(localName = "communications")
  private CommunicationsSpec communications;

  @JacksonXmlProperty(localName = "debug")
  private DebugSpec debug;

  @JacksonXmlProperty(localName = "history")
  private CaptureHistorySpec history;

  @JacksonXmlProperty(localName = "disableRightClick")
  private YesNoType disableRightClick;

  @JacksonXmlProperty(localName = "preventWindowResize")
  private YesNoType preventWindowResize;

  @JacksonXmlProperty(localName = "scoreRollupMode")
  private ScoreRollupType scoreRollupMode;

  @JacksonXmlProperty(localName = "numberOfScoringObjects")
  private Integer numberOfScoringObjects;

  @JacksonXmlProperty(localName = "statusRollupMode")
  private StatusRollupType statusRollupMode;

  @JacksonXmlProperty(localName = "thresholdScoreForCompletion")
  private BigDecimal thresholdScoreForCompletion;

  @JacksonXmlProperty(localName = "applyRollupStatusToSuccess")
  private YesNoType applyRollupStatusToSuccess;

  @JacksonXmlProperty(localName = "firstScoIsPretest")
  private YesNoType firstScoIsPretest;

  @JacksonXmlProperty(localName = "finishCausesImmediateCommit")
  private YesNoType finishCausesImmediateCommit;

  @JacksonXmlProperty(localName = "invalidMenuItemAction")
  private InvalidMenuItemActionType invalidMenuItemAction;

  @JacksonXmlProperty(localName = "alwaysFlowToFirstSco")
  private YesNoType alwaysFlowToFirstSco;

  @JacksonXmlProperty(localName = "logoutCausesPlayerExit")
  private YesNoType logoutCausesPlayerExit;

  @JacksonXmlProperty(localName = "resetRtTiming")
  private ResetRunTimeDataTimingType resetRtTiming;

  @JacksonXmlProperty(localName = "lookaheadSequencerMode")
  private LookaheadSequencerModeType lookaheadSequencerMode;

  @JacksonXmlProperty(localName = "scoreOverridesStatus")
  private YesNoType scoreOverridesStatus;

  @JacksonXmlProperty(localName = "allowCompleteStatusChange")
  private YesNoType allowCompleteStatusChange;

  @JacksonXmlProperty(localName = "scaleRawScore")
  private YesNoType scaleRawScore;

  @JacksonXmlProperty(localName = "useQuickLookaheadSequencer")
  private YesNoType useQuickLookaheadSequencer;

  @JacksonXmlProperty(localName = "rollupRuntimeAtScoUnload")
  private YesNoType rollupRuntimeAtScoUnload;

  @JacksonXmlProperty(localName = "forceObjectiveCompletionSetByContent")
  private YesNoType forceObjectiveCompletionSetByContent;

  @JacksonXmlProperty(localName = "invokeRollupAtSuspendAll")
  private YesNoType invokeRollupAtSuspendAll;

  @JacksonXmlProperty(localName = "completionStatOfFailedSuccessStat")
  private CompletionStatusType completionStatOfFailedSuccessStat;

  @JacksonXmlProperty(localName = "satisfiedCausesCompletion")
  private YesNoType satisfiedCausesCompletion;

  @JacksonXmlProperty(localName = "makeStudentPrefsGlobalToCourse")
  private YesNoType makeStudentPrefsGlobalToCourse;

  @JacksonXmlProperty(localName = "returnToLmsAction")
  private ReturnToLmsActionType returnToLmsAction;

  @JacksonXmlProperty(localName = "rollupEmptySetToUnknown")
  private YesNoType rollupEmptySetToUnknown;

  @JacksonXmlProperty(localName = "validateInteractionTypes")
  private YesNoType validateInteractionTypes;

  @JacksonXmlProperty(localName = "suspendDataMaxLength")
  private Integer suspendDataMaxLength;

  @JacksonXmlProperty(localName = "timeLimit")
  private Integer timeLimit;
}