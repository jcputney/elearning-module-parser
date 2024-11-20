package dev.jcputney.elearning.parser.input.scorm2004.ims.ss.types;

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

/**
 * Enum representing the possible operators for evaluating a rollup condition. These operators
 * define how the rollup condition is applied during evaluation. The following schema snippet
 * defines the possible values:
 * <pre>{@code
 *  <xs:simpleType name = "conditionOperatorType">
 * 		<xs:restriction base = "xs:token">
 * 			<xs:enumeration value = "not"/>
 * 			<xs:enumeration value = "noOp"/>
 * 		</xs:restriction>
 * 	</xs:simpleType>
 * }</pre>
 */
@Getter
public enum ConditionOperatorType {
  /**
   * The "not" operator negates the result of the condition.
   */
  @JsonProperty("not")
  NOT,

  /**
   * The "noOp" operator does not modify the result of the condition.
   */
  @JsonProperty("noOp")
  NO_OP,

  @JsonEnumDefaultValue
  UNKNOWN
}
