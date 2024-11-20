package dev.jcputney.elearning.parser.input.lom.types;

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * The cost of using the learning object. The following schema snippet shows the cost element:
 * <pre>{@code
 *   <xs:simpleType name="costValues">
 *      <xs:restriction base="xs:token">
 *         <xs:enumeration value="yes"/>
 *         <xs:enumeration value="no"/>
 *      </xs:restriction>
 *   </xs:simpleType>
 * }</pre>
 */
public enum Cost {
  @JsonProperty("yes")
  YES,

  @JsonProperty("no")
  NO,

  @JsonEnumDefaultValue
  UNKNOWN
}
