package dev.jcputney.elearning.parser.input.lom.types;

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * The difficulty of the learning object. The following schema snippet shows the difficulty
 * element:
 * <pre>{@code
 *   <xs:simpleType name="difficultyValues">
 *      <xs:restriction base="xs:token">
 *         <xs:enumeration value="very easy"/>
 *         <xs:enumeration value="easy"/>
 *         <xs:enumeration value="medium"/>
 *         <xs:enumeration value="difficult"/>
 *         <xs:enumeration value="very difficult"/>
 *      </xs:restriction>
 *   </xs:simpleType>
 * }</pre>
 */
public enum Difficulty {
  @JsonProperty("very easy")
  VERY_EASY,

  @JsonProperty("easy")
  EASY,

  @JsonProperty("medium")
  MEDIUM,

  @JsonProperty("difficult")
  DIFFICULT,

  @JsonProperty("very difficult")
  VERY_DIFFICULT,

  @JsonEnumDefaultValue
  UNKNOWN
}
