package dev.jcputney.elearning.parser.input.scorm2004.ims.ss.rollup;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import dev.jcputney.elearning.parser.input.scorm2004.IMSSS;
import java.util.List;
import lombok.Data;

/**
 * Represents a set of rollup rules within the SCORM IMS Simple Sequencing (IMSSS) schema. Rollup
 * rules define how the completion status, progress, and satisfaction of learning objectives are
 * aggregated or "rolled up" from child activities to their parent activities in the content
 * hierarchy.
 *
 * <p>Rollup rules are used to determine whether a parent activity (such as a module or course)
 * is considered complete or satisfied based on the statuses of its child activities. By configuring
 * these rules, instructional designers can define custom logic for calculating the overall
 * completion and success of a sequence based on individual activities within that sequence.</p>
 *
 * <p>Common configurations include:
 * <ul>
 *   <li>Requiring all child activities to be completed before the parent activity is considered complete.</li>
 *   <li>Setting a minimum number of child activities to be completed or a minimum percentage of completion.</li>
 *   <li>Specifying objective weights to control the impact of each activity on the overall progress.</li>
 * </ul>
 * </p>
 *
 * <p>The IMSSS namespace is specified by {@link IMSSS#NAMESPACE_URI}, following the SCORM 2004 standards
 * for sequencing and navigation.</p>
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class RollupRules {

  /**
   * A list of individual rollup rules that define specific conditions and actions for aggregating
   * completion and satisfaction statuses from child activities. Each rollup rule specifies
   * conditions under which the rule applies and an action to perform when the conditions are met.
   */
  @JacksonXmlElementWrapper(useWrapping = false)
  @JacksonXmlProperty(localName = "rollupRule", namespace = IMSSS.NAMESPACE_URI)
  private List<RollupRule> rollupRuleList;

  /**
   * Indicates whether the objective satisfaction status of the parent activity should be based on
   * the rollup of its child activities' statuses. When set to <code>true</code>, the parent
   * activity’s objective satisfaction depends on the rollup logic defined in the child activities.
   *
   * <p>Defaults to <code>true</code>.</p>
   */
  @JacksonXmlProperty(isAttribute = true)
  private boolean rollupObjectiveSatisfied = true;

  /**
   * Indicates whether the progress completion status of the parent activity should be based on the
   * rollup of its child activities' completion statuses. When set to <code>true</code>, the parent
   * activity’s progress completion depends on the rollup logic defined in the child activities.
   *
   * <p>Defaults to <code>true</code>.</p>
   */
  @JacksonXmlProperty(isAttribute = true)
  private boolean rollupProgressCompletion = true;

  /**
   * Specifies the weighting applied to the measure of each child activity’s "objective" when
   * determining the rollup satisfaction of the parent activity. This value allows each child
   * activity to contribute to the parent’s satisfaction based on a weighted score, rather than a
   * simple average or completion count.
   *
   * <p>Defaults to <code>1.0</code> and is represented as a decimal between 0 and 1.</p>
   */
  @JacksonXmlProperty(isAttribute = true)
  private double objectiveMeasureWeight = 1.0;
}
