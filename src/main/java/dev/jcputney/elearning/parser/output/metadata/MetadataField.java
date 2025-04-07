/*
 * qlty-ignore: +qlty:similar-code
 *
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
 *
 * qlty-ignore: -qlty:similar-code
 */

package dev.jcputney.elearning.parser.output.metadata;

/**
 * Interface for all metadata fields in the composite pattern.
 * <p>
 * This interface defines the common behavior for all metadata fields, whether they are simple
 * fields (like strings or numbers) or composite fields (containing other fields).
 * </p>
 */
public interface MetadataField {

  /**
   * Gets the name of this metadata field.
   *
   * @return the name of the field
   */
  String getName();

  /**
   * Gets the value of this metadata field as an Object.
   * <p>
   * The actual type of the returned object depends on the implementation. For example, a
   * StringMetadataField will return a String, while a DurationMetadataField will return a
   * Duration.
   * </p>
   *
   * @return the value of the field
   */
  Object getValue();

  /**
   * Gets the value of this metadata field as a String.
   * <p>
   * This method provides a standard way to get a string representation of the field's value,
   * regardless of its actual type.
   * </p>
   *
   * @return the string representation of the field's value
   */
  String getValueAsString();
}