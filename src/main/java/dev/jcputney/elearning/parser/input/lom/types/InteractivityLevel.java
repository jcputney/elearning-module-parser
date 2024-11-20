package dev.jcputney.elearning.parser.input.lom.types;

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Enum representing the possible values for <code>interactivityLevel</code> in a LOM element,
 * specifying the interactivity level of the learning resource. The following schema snippet defines
 * the possible values:
 * <pre>{@code
 *  <xs:simpleType name = "interactivityLevelType">
 * 		<xs:restriction base = "xs:string">
 * 			<xs:enumeration value = "very low"/>
 * 			<xs:enumeration value = "low"/>
 * 			<xs:enumeration value = "medium"/>
 * 			<xs:enumeration value = "high"/>
 * 			<xs:enumeration value = "very high"/>
 * 		</xs:restriction>
 * 	</xs:simpleType>
 *  }</pre>
 */
public enum InteractivityLevel {
  @JsonProperty("very low")
  VERY_LOW,

  @JsonProperty("low")
  LOW,

  @JsonProperty("medium")
  MEDIUM,

  @JsonProperty("high")
  HIGH,

  @JsonProperty("very high")
  VERY_HIGH,

  @JsonEnumDefaultValue
  UNKNOWN
}
