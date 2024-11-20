package dev.jcputney.elearning.parser.input.lom.types;

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * The role of the end user in the context to which the learning object is intended to be delivered.
 * The following schema snippet shows the intended end user role element:
 * <pre>{@code
 *   <xs:simpleType name="intendedEndUserRoleValues">
 *      <xs:restriction base="xs:token">
 *         <xs:enumeration value="teacher"/>
 *         <xs:enumeration value="author"/>
 *         <xs:enumeration value="learner"/>
 *         <xs:enumeration value="manager"/>
 *      </xs:restriction>
 *   </xs:simpleType>
 * }</pre>
 */
public enum IntendedEndUserRole {
  @JsonProperty("teacher")
  TEACHER,

  @JsonProperty("author")
  AUTHOR,

  @JsonProperty("learner")
  LEARNER,

  @JsonProperty("manager")
  MANAGER,

  @JsonEnumDefaultValue
  UNKNOWN
}
