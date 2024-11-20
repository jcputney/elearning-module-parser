package dev.jcputney.elearning.parser.input.scorm2004.ims.ss.random;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import dev.jcputney.elearning.parser.input.scorm2004.IMSSS;
import dev.jcputney.elearning.parser.input.scorm2004.ims.ss.types.RandomizationTiming;
import lombok.Data;

/**
 * Represents the randomization controls for a learning activity within the SCORM IMS Simple
 * Sequencing (IMSSS) schema. Randomization controls define how and when child activities within a
 * sequence are randomized for the learner.
 *
 * <p>Randomization controls can help to increase the variability of the learning experience by
 * allowing activities to be reordered or selectively presented. For example, they can be used to
 * shuffle assessment questions or to ensure that the learner sees different content each time they
 * access the activity.</p>
 *
 * <p>The randomization controls include settings for:
 * <ul>
 *   <li>Randomization timing – determining when randomization occurs (e.g., on each new attempt).</li>
 *   <li>Selection timing – controlling when activities are selected for presentation.</li>
 *   <li>Child reordering – allowing the order of child activities to be changed.</li>
 *   <li>Selection count – specifying how many child activities are selected from the available pool.</li>
 * </ul>
 * </p>
 *
 * <p>The IMSSS namespace is specified by {@link IMSSS#NAMESPACE_URI}, following the SCORM 2004 standards
 * for sequencing and navigation.</p>
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class RandomizationControls {

  /**
   * Specifies when randomization of child activities should occur. Possible values include:
   * <ul>
   *   <li><strong>NEVER:</strong> No randomization is applied.</li>
   *   <li><strong>ONCE:</strong> Randomization occurs only once, typically on the first attempt.</li>
   *   <li><strong>ON_EACH_NEW_ATTEMPT:</strong> Randomization occurs each time the learner makes a new attempt.</li>
   * </ul>
   */
  @JacksonXmlProperty(isAttribute = true)
  private RandomizationTiming randomizationTiming = RandomizationTiming.NEVER;

  /**
   * Specifies the timing of selection for child activities within the sequence.
   */
  @JacksonXmlProperty(isAttribute = true)
  private RandomizationTiming selectionTiming = RandomizationTiming.NEVER;

  /**
   * Indicates whether the order of child activities within the sequence can be changed. When set
   * to
   * <code>true</code>, the child activities are allowed to be reordered, typically as part of a
   * randomization or shuffling process.
   *
   * <p>This attribute enables instructional designers to create a more varied learning
   * experience by rearranging the sequence of activities, especially useful in assessment scenarios
   * where question order needs to be shuffled.</p>
   *
   * <p>Defaults to <code>false</code>.</p>
   */
  @JacksonXmlProperty(isAttribute = true)
  private boolean reorderChildren = false;

  /**
   * Specifies the number of child activities to be selected from the available set. This attribute
   * enables partial selection from a larger pool, allowing only a subset of activities to be
   * presented to the learner.
   *
   * <p>If <code>selectCount</code> is not specified, all available child activities are presented.
   * When specified, the system selects the given number of child activities at random based on the
   * other randomization settings.</p>
   */
  @JacksonXmlProperty(isAttribute = true)
  private Integer selectCount;
}
