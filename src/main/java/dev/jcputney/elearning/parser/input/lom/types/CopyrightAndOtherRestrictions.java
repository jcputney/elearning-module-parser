/*
 * Copyright (c) 2024-2025. Jonathan Putney
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
