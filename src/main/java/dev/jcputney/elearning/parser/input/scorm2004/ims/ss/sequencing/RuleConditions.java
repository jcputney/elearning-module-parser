package dev.jcputney.elearning.parser.input.scorm2004.ims.ss.sequencing;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import dev.jcputney.elearning.parser.input.scorm2004.IMSSS;
import dev.jcputney.elearning.parser.input.scorm2004.ims.ss.types.ConditionCombinationType;
import java.util.List;
import lombok.Data;

/**
 * Represents a set of conditions that must be met for a sequencing rule to apply. Rule conditions
 * define specific criteria, such as completion status or objective measure, that determine whether
 * a rule's action should be executed.
 *
 * <p>The rule conditions are combined based on the specified combination logic,
 * such as "all" (all conditions must be met) or "any" (at least one condition must be met).</p>
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class RuleConditions {

  /**
   * A list of individual rule conditions. Each condition specifies a criterion, such as an
   * "objective" being completed, that must be met for the rule's action to apply.
   */
  @JacksonXmlElementWrapper(useWrapping = false)
  @JacksonXmlProperty(localName = "ruleCondition", namespace = IMSSS.NAMESPACE_URI)
  private List<RuleCondition> ruleConditionList;

  /**
   * Specifies the combination type for conditions in this rollup rule. Determines whether all
   * conditions must be met ("all") or if any single condition is sufficient ("any").
   */
  @JacksonXmlProperty(isAttribute = true)
  private ConditionCombinationType conditionCombination = ConditionCombinationType.ANY;
}
