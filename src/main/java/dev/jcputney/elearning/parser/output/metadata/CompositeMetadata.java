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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.NonNull;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * A composite implementation of the MetadataComponent interface that can contain other metadata
 * components.
 * <p>
 * This class represents a composite node in the composite pattern, containing other metadata
 * components. When queried for metadata, it searches through its child components in order until it
 * finds a match.
 * </p>
 */
public class CompositeMetadata implements MetadataComponent {

  /**
   * The list of metadata components contained in this composite.
   */
  private final List<MetadataComponent> components = new ArrayList<>();

  /**
   * Default constructor for CompositeMetadata.
   */
  @SuppressWarnings("unused")
  public CompositeMetadata() {
    // Default constructor
  }

  /**
   * Adds a metadata component to this composite.
   *
   * @param component The component to add.
   * @return This CompositeMetadata instance for method chaining.
   */
  public CompositeMetadata addComponent(@NonNull MetadataComponent component) {
    components.add(component);
    return this;
  }

  /**
   * Adds multiple metadata components to this composite.
   *
   * @param components The components to add.
   * @return This CompositeMetadata instance for method chaining.
   */
  public CompositeMetadata addComponents(@NonNull List<MetadataComponent> components) {
    this.components.addAll(components);
    return this;
  }

  @Override
  public Optional<Object> getMetadata(String key) {
    for (MetadataComponent component : components) {
      Optional<Object> value = component.getMetadata(key);
      if (value.isPresent()) {
        return value;
      }
    }
    return Optional.empty();
  }

  @Override
  public <T> Optional<T> getMetadata(String key, Class<T> type) {
    for (MetadataComponent component : components) {
      Optional<T> value = component.getMetadata(key, type);
      if (value.isPresent()) {
        return value;
      }
    }
    return Optional.empty();
  }

  @Override
  public boolean hasMetadata(String key) {
    for (MetadataComponent component : components) {
      if (component.hasMetadata(key)) {
        return true;
      }
    }
    return false;
  }

  /**
   * Gets all components in this composite.
   *
   * @return A list of all components.
   */
  public List<MetadataComponent> getComponents() {
    return new ArrayList<>(components);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    CompositeMetadata that = (CompositeMetadata) o;

    return new EqualsBuilder()
        .append(components, that.components)
        .isEquals();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder(17, 37)
        .append(components)
        .toHashCode();
  }
}