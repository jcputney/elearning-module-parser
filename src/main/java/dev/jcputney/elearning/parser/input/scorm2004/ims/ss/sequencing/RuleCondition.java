package dev.jcputney.elearning.parser.input.scorm2004.ims.ss.sequencing;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import dev.jcputney.elearning.parser.input.scorm2004.ims.ss.types.ConditionOperatorType;
import dev.jcputney.elearning.parser.input.scorm2004.ims.ss.types.MeasureType;
import dev.jcputney.elearning.parser.input.scorm2004.ims.ss.types.MeasureTypeDeserializer;
import dev.jcputney.elearning.parser.input.scorm2004.ims.ss.types.SequencingRuleConditionType;
import lombok.Data;

/**
 * Represents an individual condition within a set of rule conditions. Each condition specifies a
 * specific criterion that must be met, such as an "objective" being completed, a minimum measure
 * threshold, or a defined attempt status.
 *
 * <p>Conditions are combined based on the logic defined in
 * {@link RuleConditions#conditionCombination}.</p>
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class RuleCondition {

  /**
   * The identifier of a specific "objective" to which this condition applies. This attribute
   * references an "objective" that must meet the specified condition.
   */
  @JacksonXmlProperty(isAttribute = true)
  private String referencedObjective;

  /**
   * The minimum measure threshold for the "objective". This value indicates the level of
   * achievement required for the condition to be met, typically represented as a decimal.
   */
  @JacksonXmlProperty(isAttribute = true)
  @JsonDeserialize(using = MeasureTypeDeserializer.class)
  private MeasureType measureThreshold;

  /**
   * Specifies the operator to use when evaluating the rule condition. Possible values are:
   * <ul>
   *   <li><strong>NOT:</strong> Inverts the result of the condition.</li>
   *   <li><strong>NO_OP:</strong> Applies the condition as-is.</li>
   * </ul>
   */
  @JacksonXmlProperty(isAttribute = true)
  private ConditionOperatorType operator = ConditionOperatorType.NO_OP;

  /**
   * Specifies the condition evaluated for this rule. The condition can include criteria such as
   * "satisfied", "completed", "attempted", or other learning activity states.
   */
  @JacksonXmlProperty(isAttribute = true)
  private SequencingRuleConditionType condition;
}
