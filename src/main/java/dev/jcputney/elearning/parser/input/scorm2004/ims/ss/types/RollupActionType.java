package dev.jcputney.elearning.parser.input.scorm2004.ims.ss.types;

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

/**
 * Enum representing the possible values for <code>action</code> in a rollup rule, specifying the
 * effect on the activity’s status when the rule’s conditions are met. The following schema snippet
 * defines the possible values:
 * <pre>{@code
 *  <xs:simpleType name = "rollupActionType">
 * 		<xs:restriction base = "xs:token">
 * 			<xs:enumeration value = "satisfied"/>
 * 			<xs:enumeration value = "notSatisfied"/>
 * 			<xs:enumeration value = "completed"/>
 * 			<xs:enumeration value = "incomplete"/>
 * 		</xs:restriction>
 * 	</xs:simpleType>
 *  }</pre>
 */
@Getter
public enum RollupActionType {
  /**
   * The "satisfied" value specifies that the activity is considered satisfied when the rule's
   * conditions are met.
   */
  @JsonProperty("satisfied")
  SATISFIED,

  /**
   * The "notSatisfied" value specifies that the activity is considered not satisfied when the
   * rule's conditions are met.
   */
  @JsonProperty("notSatisfied")
  NOT_SATISFIED,

  /**
   * The "completed" value specifies that the activity is considered completed when the rule's
   * conditions are met.
   */
  @JsonProperty("completed")
  COMPLETED,

  /**
   * The "incomplete" value specifies that the activity is considered incomplete when the rule's
   * conditions are met.
   */
  @JsonProperty("incomplete")
  INCOMPLETE,

  @JsonEnumDefaultValue
  UNKNOWN
}
