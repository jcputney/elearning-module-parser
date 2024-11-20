package dev.jcputney.elearning.parser.input.scorm2004.ims.ss.rollup;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import dev.jcputney.elearning.parser.input.scorm2004.IMSSS;
import dev.jcputney.elearning.parser.input.scorm2004.ims.ss.types.ChildActivitySet;
import dev.jcputney.elearning.parser.input.scorm2004.ims.ss.types.PercentType;
import dev.jcputney.elearning.parser.input.scorm2004.ims.ss.types.PercentTypeDeserializer;
import java.math.BigDecimal;
import lombok.Data;

/**
 * Represents an individual rollup rule within a set of rollup rules. Each rule defines conditions
 * and an action that dictate how child activities’ statuses affect the parent activity’s rollup.
 */
@Data
public class RollupRule {

  /**
   * The conditions that must be met for the rollup rule to apply. These conditions specify criteria
   * such as completion status, satisfaction status, or other properties of the child activities
   * that control whether the rollup action should occur.
   */
  @JacksonXmlProperty(localName = "rollupConditions", namespace = IMSSS.NAMESPACE_URI)
  private RollupConditions rollupConditions;

  /**
   * The action to perform if the rule’s conditions are met. The rollup action determines how the
   * child activities' statuses impact the parent activity’s rollup status, such as marking the
   * parent as completed or satisfied.
   */
  @JacksonXmlProperty(localName = "rollupAction", namespace = IMSSS.NAMESPACE_URI)
  private RollupAction rollupAction;

  /**
   * Specifies the set of child activities to consider when evaluating this rollup rule. This
   * attribute controls whether the rollup rule should apply to all child activities, any child
   * activity, none, or a specific count or percentage.
   */
  @JacksonXmlProperty(isAttribute = true)
  private ChildActivitySet childActivitySet = ChildActivitySet.ALL;

  /**
   * Specifies the minimum number of child activities that must meet the rollup conditions for this
   * rule to apply. This attribute is only relevant when <code>childActivitySet</code> is set to
   * <code>atLeastCount</code>.
   *
   * <p>Defaults to <code>0</code>.</p>
   */
  @JacksonXmlProperty(isAttribute = true)
  private int minimumCount = 0;

  /**
   * Specifies the minimum percentage of child activities that must meet the rollup conditions for
   * this rule to apply. This attribute is only relevant when <code>childActivitySet</code> is set
   * to <code>atLeastPercent</code>.
   *
   * <p>Defaults to <code>0.0</code> and is represented as a decimal between 0 and 1.</p>
   */
  @JacksonXmlProperty(isAttribute = true)
  @JsonDeserialize(using = PercentTypeDeserializer.class)
  private PercentType minimumPercent = new PercentType(BigDecimal.ZERO);
}
