package dev.jcputney.elearning.parser.input.scorm2004.adl.types;

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Enum representing the allowed SCORM types in the SCORM ADLCP schema. The following schema snippet
 * defines the possible values for this type:
 * <pre>{@code
 *   <xs:attribute name = "scormType">
 *       <xs:simpleType>
 *          <xs:restriction base = "xs:string">
 *             <xs:enumeration value = "sco"/>
 *             <xs:enumeration value = "asset"/>
 *          </xs:restriction>
 *       </xs:simpleType>
 *    </xs:attribute>
 * }</pre>
 */
public enum ScormType {

  /**
   * Indicates a Shareable Content Object (SCO), which includes interactive, trackable learning
   * content.
   */
  @JsonProperty("sco")
  SCO,

  /**
   * Indicates an asset, typically static content that does not support interactivity or tracking.
   */
  @JsonProperty("asset")
  ASSET,

  @JsonEnumDefaultValue
  UNKNOWN
}
