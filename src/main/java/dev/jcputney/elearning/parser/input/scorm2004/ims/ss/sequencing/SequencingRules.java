package dev.jcputney.elearning.parser.input.scorm2004.ims.ss.sequencing;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import dev.jcputney.elearning.parser.input.scorm2004.IMSSS;
import java.util.List;
import lombok.Data;

/**
 * Represents a set of sequencing rules within the SCORM IMS Simple Sequencing (IMSSS) schema.
 * Sequencing rules define specific conditions and actions that control the availability and
 * behavior of learning activities based on the learner's progress, completion status, and
 * interactions with the content.
 *
 * <p>Sequencing rules are used to enforce a specific flow of learning activities,
 * applying conditions that enable, disable, hide, or skip activities. These rules can be
 * categorized into three main types:</p>
 *
 * <ul>
 *   <li><strong>Pre-Condition Rules:</strong> Applied before an activity is attempted,
 *   to determine if it should be enabled, disabled, or hidden from the learner.</li>
 *   <li><strong>Exit-Condition Rules:</strong> Applied when the learner exits an activity,
 *   to define the behavior upon exit, such as retrying the activity or exiting the current sequence.</li>
 *   <li><strong>Post-Condition Rules:</strong> Applied after an activity has been attempted,
 *   to control subsequent sequencing actions, such as skipping an activity or marking it as complete.</li>
 * </ul>
 *
 * <p>Each sequencing rule contains conditions (defined in {@link RuleConditions}) that
 * must be met for the associated action (defined in {@link RuleAction}) to be applied.</p>
 *
 * <p>The IMSSS namespace is specified by {@link IMSSS#NAMESPACE_URI}, and this class
 * aligns with the SCORM 2004 standards for sequencing and navigation.</p>
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class SequencingRules {

  /**
   * A list of pre-condition rules that are evaluated before an activity is attempted. These rules
   * can be used to enable, disable, or hide an activity based on specific conditions, such as the
   * completion status of previous activities.
   */
  @JacksonXmlElementWrapper(useWrapping = false)
  @JacksonXmlProperty(localName = "preConditionRule", namespace = IMSSS.NAMESPACE_URI)
  private List<SequencingRule> preConditionRules;

  /**
   * A list of exit-condition rules that are evaluated when the learner exits an activity. These
   * rules define actions that control sequencing upon exit, such as retrying the current activity,
   * proceeding to the next activity, or exiting the sequence entirely.
   */
  @JacksonXmlElementWrapper(useWrapping = false)
  @JacksonXmlProperty(localName = "exitConditionRule", namespace = IMSSS.NAMESPACE_URI)
  private List<SequencingRule> exitConditionRules;

  /**
   * A list of post-condition rules that are evaluated after an activity has been attempted. These
   * rules can specify actions like skipping certain activities, marking an activity as complete, or
   * determining if subsequent activities should be enabled.
   */
  @JacksonXmlElementWrapper(useWrapping = false)
  @JacksonXmlProperty(localName = "postConditionRule", namespace = IMSSS.NAMESPACE_URI)
  private List<SequencingRule> postConditionRules;
}