package dev.jcputney.elearning.parser.input.lom.types;

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * The context to which the learning object applies. The following schema snippet shows the context
 * element:
 * <pre>{@code
 *   <xs:simpleType name="contextValues">
 *      <xs:restriction base="xs:token">
 *         <xs:enumeration value="school"/>
 *         <xs:enumeration value="higherEducation"/>
 *         <xs:enumeration value="training"/>
 *         <xs:enumeration value="other"/>
 *      </xs:restriction>
 *   </xs:simpleType>
 * }</pre>
 */
public enum Context {
  @JsonProperty("school")
  SCHOOL,

  @JsonProperty("higherEducation")
  HIGHER_EDUCATION,

  @JsonProperty("training")
  TRAINING,

  @JsonProperty("other")
  OTHER,

  @JsonEnumDefaultValue
  UNKNOWN
}
