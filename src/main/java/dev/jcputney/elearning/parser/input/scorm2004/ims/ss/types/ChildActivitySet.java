package dev.jcputney.elearning.parser.input.scorm2004.ims.ss.types;

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

/**
 * Enum representing the possible values for <code>childActivitySet</code>, specifying the set of
 * child activities to consider when evaluating a rollup rule. The following schema snippet defines
 * the possible values for this type:
 * <pre>{@code
 *  <xs:simpleType name = "childActivityType">
 * 		<xs:restriction base = "xs:token">
 * 			<xs:enumeration value = "all"/>
 * 			<xs:enumeration value = "any"/>
 * 			<xs:enumeration value = "none"/>
 * 			<xs:enumeration value = "atLeastCount"/>
 * 			<xs:enumeration value = "atLeastPercent"/>
 * 		</xs:restriction>
 * 	</xs:simpleType>
 * }</pre>
 */
@Getter
public enum ChildActivitySet {
  /**
   * The "all" value specifies that all child activities must be considered when evaluating the
   * rollup rule.
   */
  @JsonProperty("all")
  ALL,

  /**
   * The "any" value specifies that any child activity may be considered when evaluating the rollup
   * rule.
   */
  @JsonProperty("any")
  ANY,

  /**
   * The "none" value specifies that no child activities should be considered when evaluating the
   * rollup rule.
   */
  @JsonProperty("none")
  NONE,

  /**
   * The "atLeastCount" value specifies that at least a specified number of child activities must be
   * considered when evaluating the rollup rule.
   */
  @JsonProperty("atLeastCount")
  AT_LEAST_COUNT,

  /**
   * The "atLeastPercent" value specifies that at least a specified percentage of child activities
   * must be considered when evaluating the rollup rule.
   */
  @JsonProperty("atLeastPercent")
  AT_LEAST_PERCENT,

  @JsonEnumDefaultValue
  UNKNOWN
}
