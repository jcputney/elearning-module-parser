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
import java.io.Serializable;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * Represents a top-level sequencing configuration, containing elements that define the sequencing
 * rules and objectives for SCORM content.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonFormat(with = JsonFormat.Feature.ACCEPT_CASE_INSENSITIVE_PROPERTIES)
public class Sequencing implements Serializable {

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

  public Sequencing() {
  }

  public ControlMode getControlMode() {
    return this.controlMode;
  }

  public void setControlMode(ControlMode controlMode) {
    this.controlMode = controlMode;
  }

  public SequencingRules getSequencingRules() {
    return this.sequencingRules;
  }

  public void setSequencingRules(SequencingRules sequencingRules) {
    this.sequencingRules = sequencingRules;
  }

  public LimitConditions getLimitConditions() {
    return this.limitConditions;
  }

  public void setLimitConditions(LimitConditions limitConditions) {
    this.limitConditions = limitConditions;
  }

  public AuxiliaryResources getAuxiliaryResources() {
    return this.auxiliaryResources;
  }

  public void setAuxiliaryResources(AuxiliaryResources auxiliaryResources) {
    this.auxiliaryResources = auxiliaryResources;
  }

  public RollupRules getRollupRules() {
    return this.rollupRules;
  }

  public void setRollupRules(RollupRules rollupRules) {
    this.rollupRules = rollupRules;
  }

  public Scorm2004Objectives getObjectives() {
    return this.objectives;
  }

  public void setObjectives(Scorm2004Objectives objectives) {
    this.objectives = objectives;
  }

  public Scorm2004Objectives getAdlObjectives() {
    return this.adlObjectives;
  }

  public void setAdlObjectives(Scorm2004Objectives adlObjectives) {
    this.adlObjectives = adlObjectives;
  }

  public RandomizationControls getRandomizationControls() {
    return this.randomizationControls;
  }

  public void setRandomizationControls(RandomizationControls randomizationControls) {
    this.randomizationControls = randomizationControls;
  }

  public DeliveryControls getDeliveryControls() {
    return this.deliveryControls;
  }

  public void setDeliveryControls(DeliveryControls deliveryControls) {
    this.deliveryControls = deliveryControls;
  }

  public RollupConsiderations getRollupConsiderations() {
    return this.rollupConsiderations;
  }

  public void setRollupConsiderations(RollupConsiderations rollupConsiderations) {
    this.rollupConsiderations = rollupConsiderations;
  }

  public ConstrainChoiceConsiderations getConstrainChoiceConsiderations() {
    return this.constrainChoiceConsiderations;
  }

  public void setConstrainChoiceConsiderations(
      ConstrainChoiceConsiderations constrainChoiceConsiderations) {
    this.constrainChoiceConsiderations = constrainChoiceConsiderations;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (!(o instanceof Sequencing that)) {
      return false;
    }

    return new EqualsBuilder()
        .append(getControlMode(), that.getControlMode())
        .append(getSequencingRules(), that.getSequencingRules())
        .append(getLimitConditions(), that.getLimitConditions())
        .append(getAuxiliaryResources(), that.getAuxiliaryResources())
        .append(getRollupRules(), that.getRollupRules())
        .append(getObjectives(), that.getObjectives())
        .append(getAdlObjectives(), that.getAdlObjectives())
        .append(getRandomizationControls(), that.getRandomizationControls())
        .append(getDeliveryControls(), that.getDeliveryControls())
        .append(getRollupConsiderations(), that.getRollupConsiderations())
        .append(getConstrainChoiceConsiderations(), that.getConstrainChoiceConsiderations())
        .isEquals();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder(17, 37)
        .append(getControlMode())
        .append(getSequencingRules())
        .append(getLimitConditions())
        .append(getAuxiliaryResources())
        .append(getRollupRules())
        .append(getObjectives())
        .append(getAdlObjectives())
        .append(getRandomizationControls())
        .append(getDeliveryControls())
        .append(getRollupConsiderations())
        .append(getConstrainChoiceConsiderations())
        .toHashCode();
  }
}
