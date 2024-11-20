package dev.jcputney.elearning.parser.input.scorm2004.adl.types;

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Enum for rollup consideration types, with allowed values as per the schema. The following schema
 * snippet defines the possible values:
 * <pre>{@code
 *   <xs:simpleType name = "rollupConsiderationType">
 *      <xs:restriction base = "xs:token">
 *         <xs:enumeration value = "always"/>
 *         <xs:enumeration value = "ifAttempted"/>
 *         <xs:enumeration value = "ifNotSkipped"/>
 *         <xs:enumeration value = "ifNotSuspended"/>
 *      </xs:restriction>
 *   </xs:simpleType>
 * }</pre>
 */
public enum RollupConsiderationType {
  @JsonProperty("always")
  ALWAYS,
  @JsonProperty("ifAttempted")
  IF_ATTEMPTED,
  @JsonProperty("ifNotSkipped")
  IF_NOT_SKIPPED,
  @JsonProperty("ifNotSuspended")
  IF_NOT_SUSPENDED,

  @JsonEnumDefaultValue
  UNKNOWN
}
