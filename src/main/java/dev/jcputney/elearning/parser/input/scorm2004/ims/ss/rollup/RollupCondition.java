package dev.jcputney.elearning.parser.input.scorm2004.ims.ss.rollup;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import dev.jcputney.elearning.parser.input.scorm2004.ims.ss.types.ConditionOperatorType;
import dev.jcputney.elearning.parser.input.scorm2004.ims.ss.types.RollupRuleConditionType;
import lombok.Data;

/**
 * Represents an individual rollup condition within a set of rollup conditions. Each condition
 * specifies a specific criterion, such as completion or satisfaction status, that affects whether
 * the associated rollup rule is applied.
 */
@Data
public class RollupCondition {

  /**
   * Specifies the operator to use when evaluating the rollup condition. Possible values include:
   * <ul>
   *   <li><strong>NOT:</strong> Inverts the evaluation result of the condition.</li>
   *   <li><strong>NO_OP:</strong> Applies the condition as-is, without modification.</li>
   * </ul>
   */
  @JacksonXmlProperty(isAttribute = true)
  private ConditionOperatorType operator = ConditionOperatorType.NO_OP;

  /**
   * Specifies the specific condition being evaluated in this rollup condition. The condition may
   * include criteria related to the learner's progress or completion status of activities.
   */
  @JacksonXmlProperty(isAttribute = true)
  private RollupRuleConditionType condition;
}
