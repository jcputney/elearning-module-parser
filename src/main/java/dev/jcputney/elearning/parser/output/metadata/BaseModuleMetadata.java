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

import com.fasterxml.jackson.annotation.JsonIgnore;
import dev.jcputney.elearning.parser.enums.ModuleType;
import dev.jcputney.elearning.parser.input.PackageManifest;
import dev.jcputney.elearning.parser.output.ModuleMetadata;
import java.util.Optional;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;

/**
 * Base class for module metadata that implements the MetadataComponent interface.
 * <p>
 * This class serves as a bridge between the old metadata model and the new composite metadata
 * model. It provides the core capability of the old ModuleMetadata class while implementing the new
 * MetadataComponent interface.
 * </p>
 *
 * @param <M> The type of package manifest.
 */
@Getter
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class BaseModuleMetadata<M extends PackageManifest> extends
    ModuleMetadata<M> implements MetadataComponent {

  /**
   * The title field of the module metadata.
   */
  public static final String TITLE = "title";

  /**
   * The description field of the module metadata.
   */
  public static final String DESCRIPTION = "description";

  /**
   * The launch URL field of the module metadata.
   */
  public static final String LAUNCH_URL = "launchUrl";

  /**
   * The identifier field of the module metadata.
   */
  public static final String IDENTIFIER = "identifier";

  /**
   * The version field of the module metadata.
   */
  public static final String VERSION = "version";

  /**
   * The duration field of the module metadata.
   */
  public static final String DURATION = "duration";

  /**
   * The module type field of the module metadata.
   */
  public static final String MODULE_TYPE = "moduleType";

  /**
   * The xAPI enabled field of the module metadata.
   */
  public static final String XAPI_ENABLED = "xapiEnabled";

  /**
   * The composite metadata component that contains additional metadata.
   */
  @JsonIgnore
  protected CompositeMetadata compositeMetadata;

  /**
   * Constructor for BaseModuleMetadata.
   *
   * @param manifest The package manifest.
   * @param moduleType The module type.
   * @param xapiEnabled Whether xAPI is enabled.
   */
  @SuppressWarnings("unused")
  protected BaseModuleMetadata(M manifest, ModuleType moduleType, boolean xapiEnabled) {
    this.manifest = manifest;
    this.moduleType = moduleType;
    this.xapiEnabled = xapiEnabled;
    this.compositeMetadata = new CompositeMetadata();
  }

  /**
   * Adds a metadata component to this module's composite metadata.
   *
   * @param component The component to add.
   * @return This BaseModuleMetadata instance for method chaining.
   */
  public BaseModuleMetadata<M> addMetadataComponent(@NonNull MetadataComponent component) {
    if (compositeMetadata == null) {
      compositeMetadata = new CompositeMetadata();
    }
    compositeMetadata.addComponent(component);
    return this;
  }

  @Override
  public Optional<Object> getMetadata(String key) {
    // First check standard metadata fields
    return switch (key) {
      case TITLE -> Optional.ofNullable(getTitle());
      case DESCRIPTION -> Optional.ofNullable(getDescription());
      case LAUNCH_URL -> Optional.ofNullable(getLaunchUrl());
      case IDENTIFIER -> Optional.ofNullable(getIdentifier());
      case VERSION -> Optional.ofNullable(getVersion());
      case DURATION -> Optional.ofNullable(getDuration());
      case MODULE_TYPE -> Optional.of(getModuleType());
      case XAPI_ENABLED -> Optional.of(isXapiEnabled());
      default ->
        // Then check composite metadata
          compositeMetadata != null ? compositeMetadata.getMetadata(key) : Optional.empty();
    };
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
    // Check standard metadata fields
    return switch (key) {
      case TITLE, DESCRIPTION, LAUNCH_URL, IDENTIFIER, VERSION, DURATION, MODULE_TYPE,
           XAPI_ENABLED -> true;
      default ->
        // Then check composite metadata
          compositeMetadata != null && compositeMetadata.hasMetadata(key);
    };
  }

  /**
   * Creates a SimpleMetadata object from the provided PackageManifest.
   *
   * @param manifest The package manifest to extract metadata from.
   * @return A SimpleMetadata object containing the extracted metadata.
   */
  protected SimpleMetadata getSimpleMetadata(PackageManifest manifest) {
    return new SimpleMetadata()
        .addMetadata(TITLE, manifest.getTitle())
        .addMetadata(DESCRIPTION, manifest.getDescription())
        .addMetadata(LAUNCH_URL, manifest.getLaunchUrl())
        .addMetadata(IDENTIFIER, manifest.getIdentifier())
        .addMetadata(VERSION, manifest.getVersion())
        .addMetadata(DURATION, manifest.getDuration());
  }
}
