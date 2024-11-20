package dev.jcputney.elearning.parser.input.lom.types;

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Enum representing the possible values for <code>status</code> in a LOM element, specifying the
 * status of the learning object. The following schema snippet defines the possible values:
 * <pre>{@code
 *   <xs:simpleType name="statusValues">
 *      <xs:restriction base="xs:token">
 *         <xs:enumeration value="draft"/>
 *         <xs:enumeration value="final"/>
 *         <xs:enumeration value="revised"/>
 *         <xs:enumeration value="unavailable"/>
 *      </xs:restriction>
 *   </xs:simpleType>
 * }</pre>
 */
public enum Status {
  /**
   * The "draft" value specifies that the learning object is in draft status.
   */
  @JsonProperty("draft")
  DRAFT,

  /**
   * The "final" value specifies that the learning object is in final status.
   */
  @JsonProperty("final")
  FINAL,

  /**
   * The "revised" value specifies that the learning object is in revised status.
   */
  @JsonProperty("revised")
  REVISED,

  /**
   * The "unavailable" value specifies that the learning object is in unavailable status.
   */
  @JsonProperty("unavailable")
  UNAVAILABLE,

  @JsonEnumDefaultValue
  UNKNOWN
}
