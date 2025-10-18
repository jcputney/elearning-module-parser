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
import com.fasterxml.jackson.annotation.JsonProperty;
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
public final class Sequencing implements Serializable {

  /**
   * Identifier for this sequencing definition, required when stored in a sequencing collection.
   */
  @JacksonXmlProperty(isAttribute = true, localName = "ID")
  private String id;

  /**
   * Reference to a shared sequencing definition defined elsewhere in the document.
   */
  @JacksonXmlProperty(isAttribute = true, localName = "IDRef")
  private String idRef;

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
  @JacksonXmlProperty(localName = "objectives", namespace = ADLSeq.NAMESPACE_URI)
  @JsonProperty("adlObjectives")
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
   *
   * Initializes an instance of the Sequencing object with default values. This constructor performs
   * no specific initialization logic (no-op).
   */
  public Sequencing() {
    // no-op
  }

  /**
   * Retrieves the identifier associated with the sequencing object.
   *
   * @return the identifier as a {@code String}
   */
  public String getId() {
    return this.id;
  }

  /**
   * Sets the identifier for the sequencing object.
   *
   * @param id the identifier to set as a String
   */
  public void setId(String id) {
    this.id = id;
  }

  /**
   * Retrieves the reference identifier associated with the sequencing object.
   *
   * @return the reference identifier as a {@code String}
   */
  public String getIdRef() {
    return this.idRef;
  }

  /**
   * Sets the reference identifier associated with the sequencing object.
   *
   * @param idRef the reference identifier to set as a String
   */
  public void setIdRef(String idRef) {
    this.idRef = idRef;
  }

  /**
   * Retrieves the control mode associated with the sequencing object.
   *
   * @return the control mode as a {@code ControlMode} object
   */
  public ControlMode getControlMode() {
    return this.controlMode;
  }

  /**
   * Sets the control mode for the sequencing object.
   *
   * @param controlMode the control mode to set as a {@code ControlMode} object
   */
  public void setControlMode(ControlMode controlMode) {
    this.controlMode = controlMode;
  }

  /**
   * Retrieves the sequencing rules associated with this sequencing object.
   *
   * @return the sequencing rules as a {@code SequencingRules} object
   */
  public SequencingRules getSequencingRules() {
    return this.sequencingRules;
  }

  /**
   * Sets the sequencing rules for the sequencing object.
   *
   * @param sequencingRules the {@code SequencingRules} object containing the set of rules that
   * define the behavior and flow of activities, including pre-condition, exit-condition, and
   * post-condition rules.
   */
  public void setSequencingRules(SequencingRules sequencingRules) {
    this.sequencingRules = sequencingRules;
  }

  /**
   * Retrieves the limit conditions associated with the sequencing object.
   *
   * @return the limit conditions as a {@code LimitConditions} object
   */
  public LimitConditions getLimitConditions() {
    return this.limitConditions;
  }

  /**
   * Sets the limit conditions for the sequencing object.
   *
   * @param limitConditions the {@code LimitConditions} object that defines the constraints on
   * activity attempts, including maximum limits and other restrictions.
   */
  public void setLimitConditions(LimitConditions limitConditions) {
    this.limitConditions = limitConditions;
  }

  /**
   * Retrieves the auxiliary resources associated with this sequencing object.
   *
   * @return the {@code AuxiliaryResources} object representing a collection of supplementary
   * resources that support and enhance the primary learning activity.
   */
  public AuxiliaryResources getAuxiliaryResources() {
    return this.auxiliaryResources;
  }

  /**
   * Sets the auxiliary resources for the sequencing object.
   *
   * @param auxiliaryResources the {@code AuxiliaryResources} object representing a collection of
   * supplementary resources that provide additional support and enhance the primary learning
   * activity
   */
  public void setAuxiliaryResources(AuxiliaryResources auxiliaryResources) {
    this.auxiliaryResources = auxiliaryResources;
  }

  /**
   * Retrieves the rollup rules associated with this sequencing object.
   *
   * @return the rollup rules as a {@code RollupRules} object
   */
  public RollupRules getRollupRules() {
    return this.rollupRules;
  }

  /**
   * Sets the rollup rules for the sequencing object.
   *
   * @param rollupRules the {@code RollupRules} object that defines the logical conditions and
   * behaviors used to evaluate the status and progress of activities within a sequencing context.
   */
  public void setRollupRules(RollupRules rollupRules) {
    this.rollupRules = rollupRules;
  }

  /**
   * Retrieves the SCORM 2004 objectives associated with this sequencing object.
   *
   * @return the objectives as a {@code Scorm2004Objectives} object
   */
  public Scorm2004Objectives getObjectives() {
    return this.objectives;
  }

  /**
   * Sets the SCORM 2004 objectives for the sequencing object.
   *
   * @param objectives the {@code Scorm2004Objectives} object representing the set of objectives
   * used to evaluate the progress and performance within the SCORM 2004 sequencing model.
   */
  public void setObjectives(Scorm2004Objectives objectives) {
    this.objectives = objectives;
  }

  /**
   * Retrieves the ADL-specific objectives associated with this sequencing object.
   *
   * @return the ADL objectives as a {@code Scorm2004Objectives} object
   */
  @JsonProperty("adlObjectives")
  public Scorm2004Objectives getAdlObjectives() {
    return this.adlObjectives;
  }

  /**
   * Sets the ADL-specific objectives for the sequencing object.
   *
   * @param adlObjectives the {@code Scorm2004Objectives} object representing the set of ADL
   * objectives used to assess and track progress and performance within the SCORM 2004 sequencing
   * model.
   */
  @JsonProperty("adlObjectives")
  public void setAdlObjectives(Scorm2004Objectives adlObjectives) {
    this.adlObjectives = adlObjectives;
  }

  /**
   * Retrieves the randomization controls associated with the sequencing object.
   *
   * @return the randomization controls as a {@code RandomizationControls} object
   */
  public RandomizationControls getRandomizationControls() {
    return this.randomizationControls;
  }

  /**
   * Sets the randomization controls for the sequencing object. Randomization controls define how
   * activities are shuffled or ordered during selection to introduce variability or randomness in
   * the delivery sequence.
   *
   * @param randomizationControls the {@code RandomizationControls} object that specifies the
   * configuration parameters for randomizing activities within the sequencing context.
   */
  public void setRandomizationControls(RandomizationControls randomizationControls) {
    this.randomizationControls = randomizationControls;
  }

  /**
   * Retrieves the delivery controls associated with the sequencing object.
   *
   * @return the delivery controls as a {@code DeliveryControls} object
   */
  public DeliveryControls getDeliveryControls() {
    return this.deliveryControls;
  }

  /**
   * Sets the delivery controls for the sequencing object. Delivery controls define how content is
   * delivered and accessed during the learning process in accordance with SCORM 2004 sequencing
   * standards.
   *
   * @param deliveryControls the {@code DeliveryControls} object specifying the configuration
   * parameters for controlling the delivery of the associated learning activities.
   */
  public void setDeliveryControls(DeliveryControls deliveryControls) {
    this.deliveryControls = deliveryControls;
  }

  /**
   * Retrieves the rollup considerations associated with this sequencing object.
   *
   * @return the rollup considerations as a {@code RollupConsiderations} object
   */
  public RollupConsiderations getRollupConsiderations() {
    return this.rollupConsiderations;
  }

  /**
   * Sets the rollup considerations for the sequencing object.
   *
   * @param rollupConsiderations the {@code RollupConsiderations} object that defines configuration
   * parameters for evaluating and reporting the progress or status of activities within the
   * sequencing context.
   */
  public void setRollupConsiderations(RollupConsiderations rollupConsiderations) {
    this.rollupConsiderations = rollupConsiderations;
  }

  /**
   * Retrieves the constrain choice considerations associated with the sequencing object.
   *
   * @return the constrain choice considerations as a {@code ConstrainChoiceConsiderations} object
   */
  public ConstrainChoiceConsiderations getConstrainChoiceConsiderations() {
    return this.constrainChoiceConsiderations;
  }

  /**
   * Sets the constraint choice considerations.
   *
   * @param constrainChoiceConsiderations the ConstrainChoiceConsiderations object to set, which
   * holds the constraints and considerations for choice-related logic.
   */
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
        .append(getId(), that.getId())
        .append(getIdRef(), that.getIdRef())
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
        .append(getId())
        .append(getIdRef())
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
