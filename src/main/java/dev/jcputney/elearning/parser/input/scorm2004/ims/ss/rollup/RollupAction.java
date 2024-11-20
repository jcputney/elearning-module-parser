package dev.jcputney.elearning.parser.input.scorm2004.ims.ss.rollup;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import dev.jcputney.elearning.parser.input.scorm2004.ims.ss.types.RollupActionType;
import lombok.Data;

/**
 * Represents the action to perform if the conditions specified in a rollup rule are met. The rollup
 * action determines how the statuses of child activities affect the parent activity’s rollup
 * status, allowing for complex aggregation logic.
 *
 * <p>Common actions include marking the parent activity as satisfied, not satisfied,
 * completed, or incomplete based on the rollup rule conditions.</p>
 */
@Data
public class RollupAction {

  /**
   * Specifies the action to be taken for this rollup rule when the conditions are met. Possible
   * values include:
   * <ul>
   *   <li><strong>SATISFIED:</strong> Marks the activity as satisfied.</li>
   *   <li><strong>NOT_SATISFIED:</strong> Marks the activity as not satisfied.</li>
   *   <li><strong>COMPLETED:</strong> Marks the activity as completed.</li>
   *   <li><strong>INCOMPLETE:</strong> Marks the activity as incomplete.</li>
   * </ul>
   */
  @JacksonXmlProperty(isAttribute = true)
  private RollupActionType action;
}
