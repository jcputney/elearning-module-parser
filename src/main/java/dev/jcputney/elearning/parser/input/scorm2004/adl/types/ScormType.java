/*
 * Copyright (c) 2024-2026 Jonathan Putney
 *
 * Licensed under the Apache License, Version 2.0 (the "License").
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at the project root LICENSE file
 * or at http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0
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
   * Indicates an asset, typically static content, that doesn't support interactivity or tracking.
   */
  @JsonProperty("asset")
  ASSET,

  /**
   * Indicates an unknown type. This value is used when the type can't be determined or isn't
   * specified in the manifest.
   */
  @JsonEnumDefaultValue
  UNKNOWN
}
