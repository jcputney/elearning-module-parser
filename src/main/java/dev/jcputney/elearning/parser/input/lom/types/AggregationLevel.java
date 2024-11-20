package dev.jcputney.elearning.parser.input.lom.types;

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Enum representing the possible values for <code>aggregationLevel</code> in a LOM element,
 * specifying the aggregation level of the learning object. The following schema snippet defines the
 * possible values:
 * <pre>{@code
 *   <xs:simpleType name="aggregationLevelValues">
 *      <xs:restriction base="xs:token">
 *         <xs:enumeration value="1"/>
 *         <xs:enumeration value="2"/>
 *         <xs:enumeration value="3"/>
 *         <xs:enumeration value="4"/>
 *      </xs:restriction>
 *   </xs:simpleType>
 * }</pre>
 */
public enum AggregationLevel {
  @JsonProperty("1")
  LEVEL_1,

  @JsonProperty("2")
  LEVEL_2,

  @JsonProperty("3")
  LEVEL_3,

  @JsonProperty("4")
  LEVEL_4,

  @JsonEnumDefaultValue
  UNKNOWN

}
