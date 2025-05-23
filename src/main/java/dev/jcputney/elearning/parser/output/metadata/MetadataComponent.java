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

package dev.jcputney.elearning.parser.output.metadata;

import java.io.Serializable;
import java.util.Optional;

/**
 * Interface for all metadata components in the composite pattern. This interface defines the common
 * methods that all metadata components must implement.
 * <p>
 * The composite pattern allows for creating a tree structure of metadata components, where each
 * component can be either a leaf (simple metadata) or a composite (containing other components).
 * </p>
 */
public interface MetadataComponent extends Serializable {

  /**
   * Gets a metadata value by key.
   *
   * @param key The key to look up.
   * @return An Optional containing the value if found, or empty if not found.
   */
  Optional<Object> getMetadata(String key);

  /**
   * Gets a metadata value by key, with type conversion.
   *
   * @param key The key to look up.
   * @param type The class to convert the value to.
   * @param <T> The type to convert the value to.
   * @return An Optional containing the converted value if found, or empty if not found.
   */
  <T> Optional<T> getMetadata(String key, Class<T> type);

  /**
   * Checks if this component contains metadata for the given key.
   *
   * @param key The key to check.
   * @return true if this component contains metadata for the key, false otherwise.
   */
  boolean hasMetadata(String key);
}