package dev.jcputney.elearning.parser.input.scorm2004;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

/**
 * Represents the SCORM IMS Simple Sequencing (IMSSS) schema, which defines the sequencing and
 * navigation rules for SCORM-compliant content. This schema is designed to control the progression
 * of learners through SCORM packages based on defined conditions and constraints.
 *
 * <p>The IMSSS schema includes elements for setting control modes, defining rollup and sequencing
 * rules, establishing limit conditions, and configuring objectives, delivery, and randomization
 * controls.</p>
 *
 * <p>The IMSSS namespace is specified by {@link #NAMESPACE_URI}, following the SCORM 2004
 * standards.</p>
 *
 * @see <a href="https://www.imsglobal.org">IMS Global Learning Consortium</a>
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class IMSSS {

  /**
   * The XML namespace URI for SCORM IMS Simple Sequencing (imsss).
   */
  public static final String NAMESPACE_URI = "http://www.imsglobal.org/xsd/imsss";

}
