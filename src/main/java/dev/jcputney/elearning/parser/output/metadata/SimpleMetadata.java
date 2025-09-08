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
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * A simple implementation of the MetadataComponent interface that stores metadata in a map.
 * <p>
 * This class represents a leaf node in the composite pattern containing basic metadata values.
 * </p>
 */
public class SimpleMetadata implements MetadataComponent {

  /**
   * The map that stores metadata key-value pairs.
   */
  private final Map<String, Object> metadata = new HashMap<>();

  public SimpleMetadata() {
  }

  /**
   * Adds a metadata value with the given key.
   *
   * @param key The key for the metadata.
   * @param value The value for the metadata.
   * @return This SimpleMetadata instance for method chaining.
   */
  public SimpleMetadata addMetadata(String key, Object value) {
    if (key == null) {
      throw new NullPointerException("Metadata key cannot be null");
    }

    metadata.put(key, value);
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

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (!(o instanceof SimpleMetadata that)) {
      return false;
    }

    return new EqualsBuilder()
        .append(metadata, that.metadata)
        .isEquals();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder(17, 37)
        .append(metadata)
        .toHashCode();
  }
}