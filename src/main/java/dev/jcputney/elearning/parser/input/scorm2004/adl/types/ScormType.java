/*
 * Copyright (c) 2024. Jonathan Putney
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package dev.jcputney.elearning.parser.input.scorm2004.adl.types;

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Feature;
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
@JsonFormat(with = Feature.ACCEPT_CASE_INSENSITIVE_VALUES)
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
