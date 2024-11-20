package dev.jcputney.elearning.parser.input.cmi5.types;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Enum representing the possible values for <code>launchMethod</code> in a CMI5 element, specifying
 * the method used to launch the activity.
 *
 * <p>Schema snippet:
 * <pre>{@code
 * <xs:simpleType name="launchMethodValues">
 *    <xs:restriction base="xs:token">
 *       <xs:enumeration value="AnyWindow"/>
 *       <xs:enumeration value="NewWindow"/>
 *    </xs:restriction>
 * </xs:simpleType>
 * }</pre>
 */
public enum LaunchMethod {
  @JsonProperty("AnyWindow")
  ANY_WINDOW,

  @JsonProperty("NewWindow")
  OWN_WINDOW
}
