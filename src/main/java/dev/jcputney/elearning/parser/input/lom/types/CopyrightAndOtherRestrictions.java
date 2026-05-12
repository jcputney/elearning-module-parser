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
package dev.jcputney.elearning.parser.input.lom.types;

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Feature;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Enum representing the copyright and other restrictions of a learning object.
 *
 * <p>This enum is used to indicate whether a learning object has copyright and other restrictions,
 * doesn't have such restrictions, or if the status is unknown.
 *
 * <p>It is annotated with {@link JsonFormat} to allow for case-insensitive deserialization of
 * values.
 */
@JsonFormat(with = Feature.ACCEPT_CASE_INSENSITIVE_VALUES)
public enum CopyrightAndOtherRestrictions {
  /**
   * The learning object has copyright and other restrictions.
   */
  @JsonProperty("yes")
  YES,

  /**
   * The learning object does not have copyright and other restrictions.
   */
  @JsonProperty("no")
  NO,

  /**
   * The copyright and other restrictions of the learning object are unknown.
   */
  @JsonEnumDefaultValue
  UNKNOWN
}
