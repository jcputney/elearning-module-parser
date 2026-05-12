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
package dev.jcputney.elearning.parser.input.lom.properties;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Feature;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * <p>Enum representing the completionStatusType simple type.</p>
 *
 * <p>The following schema snippet defines the possible values:</p>
 * <pre>{@code
 * <xs:simpleType name="completionStatusType">
 *   <xs:restriction base="xs:string">
 *     <xs:enumeration value="completed" />
 *     <xs:enumeration value="incomplete" />
 *     <xs:enumeration value="browsed" />
 *     <xs:enumeration value="not attempted" />
 *     <xs:enumeration value="unknown" />
 *   </xs:restriction>
 * </xs:simpleType>
 * }</pre>
 */
@JsonFormat(with = Feature.ACCEPT_CASE_INSENSITIVE_VALUES)
public enum CompletionStatusType {

  /**
   * The "completed" value indicates that the learning object has been completed.
   */
  @JsonProperty("completed") COMPLETED,

  /**
   * The "incomplete" value indicates that the learning object has not been fully completed.
   */
  @JsonProperty("incomplete") INCOMPLETE,

  /**
   * The "browsed" value indicates that the learning object has been browsed but not necessarily
   * completed.
   */
  @JsonProperty("browsed") BROWSED,

  /**
   * The "not attempted" value indicates that the learning object has not been attempted.
   */
  @JsonProperty("not attempted") NOT_ATTEMPTED,

  /**
   * The "unknown" value indicates that the completion status is unknown.
   */
  @JsonProperty("unknown") UNKNOWN
}
