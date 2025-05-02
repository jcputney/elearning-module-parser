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

package dev.jcputney.elearning.parser.input.scorm2004.ims.ss.sequencing;

import static lombok.AccessLevel.PRIVATE;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import dev.jcputney.elearning.parser.input.scorm2004.ADLSeq;
import dev.jcputney.elearning.parser.input.scorm2004.IMSSS;
import dev.jcputney.elearning.parser.input.scorm2004.adl.sequencing.ConstrainChoiceConsiderations;
import dev.jcputney.elearning.parser.input.scorm2004.adl.sequencing.RollupConsiderations;
import dev.jcputney.elearning.parser.input.scorm2004.ims.ss.objective.Scorm2004Objectives;
import dev.jcputney.elearning.parser.input.scorm2004.ims.ss.random.RandomizationControls;
import dev.jcputney.elearning.parser.input.scorm2004.ims.ss.rollup.RollupRules;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * Represents a top-level sequencing configuration, containing elements that define the sequencing
 * rules and objectives for SCORM content.
 */
@Builder
@Getter
@Jacksonized
@AllArgsConstructor(access = PRIVATE)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonFormat(with = JsonFormat.Feature.ACCEPT_CASE_INSENSITIVE_PROPERTIES)
public class Sequencing {

  /**
   * Control modes that specify the navigation options available to the learner.
   */
  @JacksonXmlProperty(localName = "controlMode", namespace = IMSSS.NAMESPACE_URI)
  private ControlMode controlMode;
  /**
   * Sequencing rules that apply conditions for enabling, disabling, hiding, or skipping content
   * based on learner progress.
   */
  @JacksonXmlProperty(localName = "sequencingRules", namespace = IMSSS.NAMESPACE_URI)
  private SequencingRules sequencingRules;
  /**
   * Defines limitations on the number of attempts, time limits, and duration constraints.
   */
  @JacksonXmlProperty(localName = "limitConditions", namespace = IMSSS.NAMESPACE_URI)
  private LimitConditions limitConditions;
  /**
   * Auxiliary resources that provide external resources associated with the learning objects.
   */
  @JacksonXmlProperty(localName = "auxiliaryResources", namespace = IMSSS.NAMESPACE_URI)
  private AuxiliaryResources auxiliaryResources;
  /**
   * Rules governing the rollup of objectives and completion status for this activity.
   */
  @JacksonXmlProperty(localName = "rollupRules", namespace = IMSSS.NAMESPACE_URI)
  private RollupRules rollupRules;
  /**
   * Defines learning objectives for this activity, including primary objectives that contribute to
   * rollup and additional objectives.
   */
  @JacksonXmlProperty(localName = "objectives", namespace = IMSSS.NAMESPACE_URI)
  private Scorm2004Objectives objectives;
  /**
   * The ADL objectives for this sequencing element.
   */
  @JacksonXmlProperty(localName = "adlseq:objectives", namespace = ADLSeq.NAMESPACE_URI)
  private Scorm2004Objectives adlObjectives;
  /**
   * Controls the randomization of child activities within a sequence.
   */
  @JacksonXmlProperty(localName = "randomizationControls", namespace = IMSSS.NAMESPACE_URI)
  private RandomizationControls randomizationControls;
  /**
   * Delivery controls that determine how the content's completion and objectives are set based on
   * the learner's progress.
   */
  @JacksonXmlProperty(localName = "deliveryControls", namespace = IMSSS.NAMESPACE_URI)
  private DeliveryControls deliveryControls;
  /**
   * The constrained choice considerations element, which contains attributes to define choice and
   * activation restrictions.
   */
  @JacksonXmlProperty(localName = "rollupConsiderations", namespace = ADLSeq.NAMESPACE_URI)
  private RollupConsiderations rollupConsiderations;
  /**
   * The constrained choice considerations element, which contains attributes to define choice and
   * activation restrictions.
   */
  @JacksonXmlProperty(localName = "constrainedChoiceConsiderations", namespace = ADLSeq.NAMESPACE_URI)
  private ConstrainChoiceConsiderations constrainChoiceConsiderations;

  /**
   * Default constructor for the Sequencing class.
   */
  @SuppressWarnings("unused")
  public Sequencing() {
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

    Sequencing that = (Sequencing) o;

    return new EqualsBuilder()
        .append(controlMode, that.controlMode)
        .append(sequencingRules, that.sequencingRules)
        .append(limitConditions, that.limitConditions)
        .append(auxiliaryResources, that.auxiliaryResources)
        .append(rollupRules, that.rollupRules)
        .append(objectives, that.objectives)
        .append(adlObjectives, that.adlObjectives)
        .append(randomizationControls, that.randomizationControls)
        .append(deliveryControls, that.deliveryControls)
        .append(rollupConsiderations, that.rollupConsiderations)
        .append(constrainChoiceConsiderations, that.constrainChoiceConsiderations)
        .isEquals();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder(17, 37)
        .append(controlMode)
        .append(sequencingRules)
        .append(limitConditions)
        .append(auxiliaryResources)
        .append(rollupRules)
        .append(objectives)
        .append(adlObjectives)
        .append(randomizationControls)
        .append(deliveryControls)
        .append(rollupConsiderations)
        .append(constrainChoiceConsiderations)
        .toHashCode();
  }
}
