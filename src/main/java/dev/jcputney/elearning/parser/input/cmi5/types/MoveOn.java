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
package dev.jcputney.elearning.parser.input.cmi5.types;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Feature;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Enum representing the possible values for <code>moveOn</code> in a CMI5 element, specifying the
 * conditions that must be met to move on to the next activity. The following schema snippet defines
 * the possible values:
 * <pre>{@code
 *   <xs:simpleType name="moveOnValues">
 *      <xs:restriction base="xs:token">
 *         <xs:enumeration value="NotApplicable"/>
 *         <xs:enumeration value="Completed"/>
 *         <xs:enumeration value="CompletedAndPassed"/>
 *         <xs:enumeration value="CompletedOrPassed"/>
 *         <xs:enumeration value="Passed"/>
 *      </xs:restriction>
 *   </xs:simpleType>
 * }</pre>
 */
@JsonFormat(with = Feature.ACCEPT_CASE_INSENSITIVE_VALUES)
public enum MoveOn {

  /**
   * The move on condition is not applicable.
   */
  @JsonProperty("NotApplicable")
  NOT_APPLICABLE,

  /**
   * The move on condition is completed.
   */
  @JsonProperty("Completed")
  COMPLETED,

  /**
   * The move on condition is completed and passed.
   */
  @JsonProperty("CompletedAndPassed")
  COMPLETED_AND_PASSED,

  /**
   * The move on condition is completed or passed.
   */
  @JsonProperty("CompletedOrPassed")
  COMPLETED_OR_PASSED,

  /**
   * The move on condition is passed.
   */
  @JsonProperty("Passed")
  PASSED
}
