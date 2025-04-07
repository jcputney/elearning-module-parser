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
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
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
@AllArgsConstructor(access = PRIVATE)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonFormat(with = Feature.ACCEPT_CASE_INSENSITIVE_PROPERTIES)
public class BehaviorSpec {

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
   * The flag indicating whether to prevent window resizing.
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
   * The flag indicating whether to finish causes immediate commit.
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
   * The flag indicating whether score overrides status.
   */
  @JacksonXmlProperty(localName = "scoreOverridesStatus")
  private YesNoType scoreOverridesStatus;
  /**
   * The flag indicating whether to allow complete status change.
   */
  @JacksonXmlProperty(localName = "allowCompleteStatusChange")
  private YesNoType allowCompleteStatusChange;
  /**
   * The flag indicating whether to scale raw score.
   */
  @JacksonXmlProperty(localName = "scaleRawScore")
  private YesNoType scaleRawScore;
  /**
   * The flag indicating whether to use quick lookahead sequencer.
   */
  @JacksonXmlProperty(localName = "useQuickLookaheadSequencer")
  private YesNoType useQuickLookaheadSequencer;
  /**
   * The flag indicating whether to roll up runtime at SCO unload.
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
   * Default constructor for the BehaviorSpec class.
   */
  @SuppressWarnings("unused")
  public BehaviorSpec() {
    // Default constructor
  }
}
