package dev.jcputney.elearning.parser.input.lom.types;

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Enum representing the possible values for <code>interactivityType</code> in a lom element,
 * specifying the degree of interactivity of the learning resource. The following schema snippet
 * defines the possible values:
 * <pre>{@code
 *   <xs:simpleType name="interactivityTypeValues">
 *      <xs:restriction base="xs:token">
 *         <xs:enumeration value="active"/>
 *         <xs:enumeration value="expositive"/>
 *         <xs:enumeration value="mixed"/>
 *      </xs:restriction>
 *   </xs:simpleType>
 * }</pre>
 */
public enum InteractivityType {
  @JsonProperty("active")
  ACTIVE,

  @JsonProperty("expositive")
  EXPOSITIVE,

  @JsonProperty("mixed")
  MIXED,

  @JsonEnumDefaultValue
  UNKNOWN
}
