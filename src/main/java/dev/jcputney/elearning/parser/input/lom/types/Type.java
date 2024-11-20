package dev.jcputney.elearning.parser.input.lom.types;

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Enum representing the possible values for <code>type</code> in a <code>technical</code> element,
 * specifying the type of the location. The following schema snippet defines the possible values:
 * <pre>{@code
 *  <xs:simpleType name = "typeType">
 * 		<xs:restriction base = "xs:token">
 * 			<xs:enumeration value = "operating system"/>
 * 			<xs:enumeration value = "browser"/>
 * 		</xs:restriction>
 * 	</xs:simpleType>
 *  }</pre>
 */
public enum Type {
  /**
   * The "operating system" value specifies that the location is an operating system.
   */
  @JsonProperty("operating system")
  OPERATING_SYSTEM,

  /**
   * The "browser" value specifies that the location is a browser.
   */
  @JsonProperty("browser")
  BROWSER,

  @JsonEnumDefaultValue
  UNKNOWN
}
