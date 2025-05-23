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

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;

/**
 * A simple implementation of the MetadataComponent interface that stores metadata in a map.
 * <p>
 * This class represents a leaf node in the composite pattern, containing basic metadata values.
 * </p>
 */
@NoArgsConstructor
@EqualsAndHashCode(doNotUseGetters = true)
public class SimpleMetadata implements MetadataComponent {

  /**
   * The map that stores metadata key-value pairs.
   */
  private final Map<String, Object> metadata = new HashMap<>();

  /**
   * Adds a metadata value with the given key.
   *
   * @param key The key for the metadata.
   * @param value The value for the metadata.
   * @return This SimpleMetadata instance for method chaining.
   */
  public SimpleMetadata addMetadata(@NonNull String key, Object value) {
    metadata.put(key, value);
    return this;
  }

  /**
   * Adds all entries from the provided map to this metadata component.
   *
   * @param metadataMap The map containing metadata entries to add.
   * @return This SimpleMetadata instance for method chaining.
   */
  public SimpleMetadata addAllMetadata(@NonNull Map<String, Object> metadataMap) {
    metadata.putAll(metadataMap);
    return this;
  }

  @Override
  public Optional<Object> getMetadata(String key) {
    return Optional.ofNullable(metadata.get(key));
  }

  @Override
  @SuppressWarnings("unchecked")
  public <T> Optional<T> getMetadata(String key, Class<T> type) {
    return getMetadata(key)
        .filter(type::isInstance)
        .map(value -> (T) value);
  }

  @Override
  public boolean hasMetadata(String key) {
    return metadata.containsKey(key);
  }
}