/*
 * Copyright (c) 2024. Jonathan Putney
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

package dev.jcputney.elearning.parser.output;

import com.fasterxml.jackson.annotation.JsonIgnore;
import dev.jcputney.elearning.parser.enums.ModuleType;
import dev.jcputney.elearning.parser.input.PackageManifest;
import java.time.Duration;
import lombok.Builder.Default;
import lombok.Getter;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;

/**
 * Represents the core metadata for an eLearning module, providing common fields shared across
 * various module types, such as SCORM, AICC, or cmi5.
 * <p>
 * Each module type can extend this class to include additional fields specific to that type.
 * </p>
 *
 * @param <M> the type of the package manifest
 */
@Getter
@SuperBuilder
public abstract class ModuleMetadata<M extends PackageManifest> implements PackageManifest {

  /**
   * The package manifest for the module.
   */
  @NonNull
  protected M manifest;
  /**
   * The type of the module (for example, SCORM, AICC, cmi5).
   */
  @NonNull
  protected ModuleType moduleType;
  /**
   * Indicates whether xAPI is enabled for the module.
   */
  @Default
  protected boolean xapiEnabled = false;

  /**
   * Default constructor for the ModuleMetadata class.
   */
  @SuppressWarnings("unused")
  protected ModuleMetadata() {
    // Default constructor
  }

  /**
   * Constructor for ModuleMetadata.
   *
   * @param manifest the package manifest
   * @param moduleType the module type
   * @param xapiEnabled whether xAPI is enabled
   */
  protected ModuleMetadata(M manifest, ModuleType moduleType, boolean xapiEnabled) {
    this.manifest = manifest;
    this.moduleType = moduleType;
    this.xapiEnabled = xapiEnabled;
  }

  @Override
  @JsonIgnore
  public String getTitle() {
    return manifest.getTitle();
  }

  @Override
  @JsonIgnore
  public String getDescription() {
    return manifest.getDescription();
  }

  @Override
  @JsonIgnore
  public String getLaunchUrl() {
    return manifest.getLaunchUrl();
  }

  @Override
  @JsonIgnore
  public String getIdentifier() {
    return manifest.getIdentifier();
  }

  @Override
  @JsonIgnore
  public String getVersion() {
    return manifest.getVersion();
  }

  @Override
  @JsonIgnore
  public Duration getDuration() {
    return manifest.getDuration();
  }
}
