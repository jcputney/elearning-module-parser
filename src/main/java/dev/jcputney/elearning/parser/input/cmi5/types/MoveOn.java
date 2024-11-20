package dev.jcputney.elearning.parser.input.cmi5.types;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Enum representing the possible values for <code>moveOn</code> in a CMI5 element, specifying the
 * conditions that must be met to move on to the next activity. The following schema snippet defines
 * the possible values:
 * <pre>{@code
 *   <xs:simpleType name="moveOnValues">
 *      <xs:restriction base="xs:token">
 *         <xs:enumeration value="NotApplicable"/>
 *         <xs:enumeration value="Completed"/>
 *         <xs:enumeration value="CompletedAndPassed"/>
 *         <xs:enumeration value="CompletedOrPassed"/>
 *         <xs:enumeration value="Passed"/>
 *      </xs:restriction>
 *   </xs:simpleType>
 * }</pre>
 */
public enum MoveOn {
  @JsonProperty("NotApplicable")
  NOT_APPLICABLE,

  @JsonProperty("Completed")
  COMPLETED,

  @JsonProperty("CompletedAndPassed")
  COMPLETED_AND_PASSED,

  @JsonProperty("CompletedOrPassed")
  COMPLETED_OR_PASSED,

  @JsonProperty("Passed")
  PASSED
}
