package dev.jcputney.elearning.parser.input.scorm2004.ims.ss.sequencing;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import dev.jcputney.elearning.parser.input.scorm2004.IMSSS;
import lombok.Data;

/**
 * Represents a single sequencing rule within a set of pre-condition, exit-condition, or
 * post-condition rules. Each rule consists of a set of conditions and an action.
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class SequencingRule {

  /**
   * The conditions that must be met for the rule to apply. These conditions specify criteria such
   * as activity completion status, attempt count, or objective status that control whether the
   * rule's action should be executed.
   */
  @JacksonXmlProperty(localName = "ruleConditions", namespace = IMSSS.NAMESPACE_URI)
  private RuleConditions ruleConditions;

  /**
   * The action to perform if the rule's conditions are met. Actions control the sequencing
   * behavior, such as enabling, disabling, or hiding an activity, or advancing to the next
   * activity.
   */
  @JacksonXmlProperty(localName = "ruleAction", namespace = IMSSS.NAMESPACE_URI)
  private RuleAction ruleAction;
}
