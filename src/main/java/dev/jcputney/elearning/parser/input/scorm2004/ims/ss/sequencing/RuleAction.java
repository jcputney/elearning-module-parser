package dev.jcputney.elearning.parser.input.scorm2004.ims.ss.sequencing;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;

/**
 * Represents the action to perform if the conditions specified in a sequencing rule are met. Rule
 * actions determine the sequencing behavior for the activity, such as enabling, disabling, hiding,
 * or skipping an activity.
 *
 * <p>Actions are only executed if the conditions in the rule's {@link RuleConditions} are
 * met.</p>
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class RuleAction {

  /**
   * The action to be taken if the conditions are met. Possible actions include:
   * <ul>
   *   <li><strong>skip:</strong> Skip the activity and proceed to the next available activity.</li>
   *   <li><strong>disabled:</strong> Disable the activity, making it unavailable to the learner.</li>
   *   <li><strong>hiddenFromChoice:</strong> Hide the activity from the learner's choice menu.</li>
   *   <li><strong>stopForwardTraversal:</strong> Prevent the learner from progressing forward beyond the activity.</li>
   * </ul>
   */
  @JacksonXmlProperty(isAttribute = true)
  private String action;
}
