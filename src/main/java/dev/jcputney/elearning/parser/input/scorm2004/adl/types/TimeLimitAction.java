package dev.jcputney.elearning.parser.input.scorm2004.adl.types;

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Enum for timeLimitAction values. The following schema snippet defines the possible values:
 * <pre>{@code
 *   <xs:simpleType name = "timeLimitActionType">
 *      <xs:restriction base = "xs:string">
 *         <xs:enumeration value = "exit,message"/>
 *         <xs:enumeration value = "exit,no message"/>
 *         <xs:enumeration value = "continue,message"/>
 *         <xs:enumeration value = "continue,no message"/>
 *      </xs:restriction>
 *   </xs:simpleType>
 * }</pre>
 */
public enum TimeLimitAction {
  @JsonProperty("exit,message")
  EXIT_MESSAGE,

  @JsonProperty("exit,no message")
  EXIT_NO_MESSAGE,

  @JsonProperty("continue,message")
  CONTINUE_MESSAGE,

  @JsonProperty("continue,no message")
  CONTINUE_NO_MESSAGE,

  @JsonEnumDefaultValue
  UNKNOWN
}
