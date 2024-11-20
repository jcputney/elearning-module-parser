package dev.jcputney.elearning.parser.input.scorm2004.ims.ss.types;

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

/**
 * Enum representing the possible ways to combine multiple conditions within a rollup or sequencing
 * rule.
 * <p>
 * This attribute specifies whether all specified conditions must be met ("all") or if meeting any
 * single condition is sufficient ("any").
 * </p>
 * The following schema snippet defines the possible values:
 * <pre>{@code
 *  <xs:simpleType name = "conditionCombinationType">
 * 		<xs:restriction base = "xs:token">
 * 			<xs:enumeration value = "all"/>
 * 			<xs:enumeration value = "any"/>
 * 		</xs:restriction>
 * 	</xs:simpleType>
 * }</pre>
 */
@Getter
public enum ConditionCombinationType {
  /**
   * The "all" value specifies that all conditions must be met for the rule to be satisfied.
   */
  @JsonProperty("all")
  ALL,

  /**
   * The "any" value specifies that any condition may be met for the rule to be satisfied.
   */
  @JsonProperty("any")
  ANY,

  @JsonEnumDefaultValue
  UNKNOWN
}