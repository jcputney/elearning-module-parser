package dev.jcputney.elearning.parser.input.scorm2004.ims.ss.sequencing;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import dev.jcputney.elearning.parser.input.scorm2004.ADLSeq;
import dev.jcputney.elearning.parser.input.scorm2004.IMSSS;
import dev.jcputney.elearning.parser.input.scorm2004.adl.sequencing.ConstrainChoiceConsiderations;
import dev.jcputney.elearning.parser.input.scorm2004.adl.sequencing.RollupConsiderations;
import dev.jcputney.elearning.parser.input.scorm2004.ims.ss.objective.Scorm2004Objectives;
import dev.jcputney.elearning.parser.input.scorm2004.ims.ss.random.RandomizationControls;
import dev.jcputney.elearning.parser.input.scorm2004.ims.ss.rollup.RollupRules;
import lombok.Data;

/**
 * Represents a top-level sequencing configuration, containing elements that define the sequencing
 * rules and objectives for SCORM content.
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
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
   * Rules governing the rollup of "objectives" and completion status for this activity.
   */
  @JacksonXmlProperty(localName = "rollupRules", namespace = IMSSS.NAMESPACE_URI)
  private RollupRules rollupRules;

  /**
   * Defines learning objectives for this activity, including primary "objectives" that contribute
   * to rollup and additional objectives.
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
}