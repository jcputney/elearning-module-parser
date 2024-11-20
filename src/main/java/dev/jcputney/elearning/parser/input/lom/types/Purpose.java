package dev.jcputney.elearning.parser.input.lom.types;

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * The purpose of the learning object. The following schema snippet defines the Purpose type:
 * <pre>{@code
 *   <xs:simpleType name="purposeValues">
 *      <xs:restriction base="xs:token">
 *         <xs:enumeration value="discipline"/>
 *         <xs:enumeration value="idea"/>
 *         <xs:enumeration value="prerequisite"/>
 *         <xs:enumeration value="educational objective"/>
 *         <xs:enumeration value="accessibility restrictions"/>
 *         <xs:enumeration value="educational level"/>
 *         <xs:enumeration value="skill level"/>
 *         <xs:enumeration value="security level"/>
 *         <xs:enumeration value="competency"/>
 *      </xs:restriction>
 *   </xs:simpleType>
 * }</pre>
 */
public enum Purpose {
  @JsonProperty("discipline")
  DISCIPLINE,

  @JsonProperty("idea")
  IDEA,

  @JsonProperty("prerequisite")
  PREREQUISITE,

  @JsonProperty("educational objective")
  EDUCATIONAL_OBJECTIVE,

  @JsonProperty("accessibility restrictions")
  ACCESSIBILITY_RESTRICTIONS,

  @JsonProperty("educational level")
  EDUCATIONAL_LEVEL,

  @JsonProperty("skill level")
  SKILL_LEVEL,

  @JsonProperty("security level")
  SECURITY_LEVEL,

  @JsonProperty("competency")
  COMPETENCY,

  @JsonEnumDefaultValue
  UNKNOWN
}
