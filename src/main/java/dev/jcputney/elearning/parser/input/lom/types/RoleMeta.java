package dev.jcputney.elearning.parser.input.lom.types;

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Enum representing the possible values for <code>role</code> in a <code>contribute</code> element,
 * specifying the role of the entity contributing to the resource. The following schema snippet
 * defines the possible values:
 * <pre>{@code
 *  <xs:simpleType name = "roleMeta">
 * 		<xs:restriction base = "xs:token">
 * 			<xs:enumeration value = "creator"/>
 * 			<xs:enumeration value = "validator"/>
 * 		</xs:restriction>
 * 	</xs:simpleType>
 *  }</pre>
 */
public enum RoleMeta {
  /**
   * The "creator" value specifies that the entity is the creator of the resource.
   */
  @JsonProperty("creator")
  CREATOR,

  /**
   * The "validator" value specifies that the entity is the validator of the resource.
   */
  @JsonProperty("validator")
  VALIDATOR,

  @JsonEnumDefaultValue
  UNKNOWN
}
