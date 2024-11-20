package dev.jcputney.elearning.parser.input.lom.types;

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * The kind of relation between the learning object and the related resource. The following schema
 * snippet shows the kind element:
 * <pre>{@code
 *   <xs:simpleType name="kindValues">
 *      <xs:restriction base="xs:token">
 *         <xs:enumeration value="ispartof"/>
 *         <xs:enumeration value="haspart"/>
 *         <xs:enumeration value="isversionof"/>
 *         <xs:enumeration value="hasversion"/>
 *         <xs:enumeration value="isformatof"/>
 *         <xs:enumeration value="hasformat"/>
 *         <xs:enumeration value="references"/>
 *         <xs:enumeration value="isreferencedby"/>
 *         <xs:enumeration value="isbasedon"/>
 *         <xs:enumeration value="isbasisfor"/>
 *         <xs:enumeration value="requires"/>
 *         <xs:enumeration value="isrequiredby"/>
 *      </xs:restriction>
 *   </xs:simpleType>
 * }</pre>
 */
public enum Kind {
  @JsonProperty("ispartof")
  IS_PART_OF,

  @JsonProperty("haspart")
  HAS_PART,

  @JsonProperty("isversionof")
  IS_VERSION_OF,

  @JsonProperty("hasversion")
  HAS_VERSION,

  @JsonProperty("isformatof")
  IS_FORMAT_OF,

  @JsonProperty("hasformat")
  HAS_FORMAT,

  @JsonProperty("references")
  REFERENCES,

  @JsonProperty("isreferencedby")
  IS_REFERENCED_BY,

  @JsonProperty("isbasedon")
  IS_BASED_ON,

  @JsonProperty("isbasisfor")
  IS_BASIS_FOR,

  @JsonProperty("requires")
  REQUIRES,

  @JsonProperty("isrequiredby")
  IS_REQUIRED_BY,

  @JsonEnumDefaultValue
  UNKNOWN
}
