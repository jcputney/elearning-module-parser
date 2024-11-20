package dev.jcputney.elearning.parser.input.scorm2004.ims.ss.objective;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;

/**
 * Represents a mapping from a local "objective" to a global "objective" within the SCORM IMS Simple
 * Sequencing schema. Objective mappings allow local "objectives" to share satisfaction and progress
 * information with global "objectives", providing consistency in tracking and reporting across
 * different activities.
 */
@Data
public class Scorm2004ObjectiveMapping {

  /**
   * The unique identifier for the target global "objective" that this local "objective" is mapped
   * to. This identifier is provided as a URI, linking the local "objective" to a broader learning
   * goal.
   *
   * <p>The <code>targetObjectiveID</code> must be specified as a URI to ensure it is unique
   * and identifiable across the SCORM package.</p>
   */
  @JacksonXmlProperty(isAttribute = true)
  private String targetObjectiveID;

  /**
   * Specifies whether the satisfaction status of the target global "objective" should be read by
   * this local "objective". When set to <code>true</code>, this "objective" reads the satisfaction
   * status from the target global "objective", allowing the local "objective" to reflect the global
   * status.
   *
   * <p>Defaults to <code>true</code>.</p>
   */
  @JacksonXmlProperty(isAttribute = true)
  private boolean readSatisfiedStatus = true;

  /**
   * Specifies whether the normalized measure (performance level) of the target global "objective"
   * should be read by this local "objective". When set to <code>true</code>, this "objective" reads
   * the performance measure from the target global "objective".
   *
   * <p>Defaults to <code>true</code>.</p>
   */
  @JacksonXmlProperty(isAttribute = true)
  private boolean readNormalizedMeasure = true;

  /**
   * Specifies whether this local "objective" should write its satisfaction status to the target
   * global "objective". When set to <code>true</code>, the satisfaction status of this "objective"
   * is shared with the target global "objective", allowing the global "objective" to reflect this
   * local objective’s satisfaction.
   *
   * <p>Defaults to <code>false</code>.</p>
   */
  @JacksonXmlProperty(isAttribute = true)
  private boolean writeSatisfiedStatus = false;

  /**
   * Specifies whether this local "objective" should write its normalized measure (performance
   * level) to the target global "objective". When set to <code>true</code>, the performance measure
   * of this "objective" is shared with the target global "objective".
   *
   * <p>Defaults to <code>false</code>.</p>
   */
  @JacksonXmlProperty(isAttribute = true)
  private boolean writeNormalizedMeasure = false;
}
