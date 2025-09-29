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

package dev.jcputney.elearning.parser.output;

import com.fasterxml.jackson.annotation.JsonIgnore;
import dev.jcputney.elearning.parser.enums.ModuleEditionType;
import dev.jcputney.elearning.parser.enums.ModuleType;
import dev.jcputney.elearning.parser.input.PackageManifest;
import java.time.Duration;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * Represents the core metadata for an eLearning module, providing common fields shared across
 * various module types, such as SCORM, AICC, or cmi5.
 * <p>
 * Each module type can extend this class to include additional fields specific to that type.
 * </p>
 *
 * @param <M> the type of the package manifest
 */
public abstract class ModuleMetadata<M extends PackageManifest> implements PackageManifest {

  /**
   * The package manifest for the module.
   */
  protected M manifest;

  /**
   * The type of the module (for example, SCORM, AICC, cmi5).
   */
  protected ModuleType moduleType;

  /**
   * The specific edition type of the module, including SCORM 2004 edition information.
   */
  protected ModuleEditionType moduleEditionType;

  /**
   * Indicates whether xAPI is enabled for the module.
   */
  protected boolean xapiEnabled = false;

  /**
   * The total size of all files in the module in bytes. A value of -1 indicates the size is not
   * available.
   */
  protected long sizeOnDisk = -1;

  /**
   * Constructor for ModuleMetadata.
   *
   * @param manifest the package manifest
   * @param moduleType the module type
   * @param moduleEditionType the module edition type
   * @param xapiEnabled whether xAPI is enabled
   */
  protected ModuleMetadata(M manifest, ModuleType moduleType, ModuleEditionType moduleEditionType,
      boolean xapiEnabled) {
    this.manifest = manifest;
    this.moduleType = moduleType;
    this.moduleEditionType = moduleEditionType;
    this.xapiEnabled = xapiEnabled;
  }

  protected ModuleMetadata() {
    // no-op
  }

  /**
   * Retrieves the title of the module. This method delegates to the underlying manifest to obtain
   * the human-readable name of the package.
   *
   * @return the title of the module as a String
   */
  @Override
  @JsonIgnore
  public String getTitle() {
    return manifest.getTitle();
  }

  /**
   * Retrieves the description of the module. This method delegates to the underlying manifest to
   * obtain additional information about the module in the form of a description.
   *
   * @return the description of the module as a String
   */
  @Override
  @JsonIgnore
  public String getDescription() {
    return manifest.getDescription();
  }

  /**
   * Retrieves the launch URL for the module. This method delegates to the underlying manifest to
   * obtain the URL used to start or launch the module.
   *
   * @return the launch URL of the module as a String
   */
  @Override
  @JsonIgnore
  public String getLaunchUrl() {
    return manifest.getLaunchUrl();
  }

  /**
   * Retrieves the unique identifier of the module. This method delegates to the underlying manifest
   * to obtain the identifier.
   *
   * @return the unique identifier of the module as a String
   */
  @Override
  @JsonIgnore
  public String getIdentifier() {
    return manifest.getIdentifier();
  }

  /**
   * Retrieves the version of the module. This method delegates to the underlying manifest to obtain
   * the version of the package.
   *
   * @return the version of the module as a String
   */
  @Override
  @JsonIgnore
  public String getVersion() {
    return manifest.getVersion();
  }

  /**
   * Retrieves the duration of the module. This method delegates to the underlying manifest to
   * obtain the duration, indicating the expected time to complete the module.
   *
   * @return the duration of the module as a {@link Duration}
   */
  @Override
  @JsonIgnore
  public Duration getDuration() {
    return manifest.getDuration();
  }

  /**
   * Gets the total size of all files in the module on disk.
   *
   * @return Total size of all files in bytes, or -1 if not available
   */
  public long getSizeOnDisk() {
    return sizeOnDisk;
  }

  /**
   * Sets the total size of all files in the module on disk.
   *
   * @param sizeOnDisk The total size of the module's files in bytes.
   */
  public void setSizeOnDisk(long sizeOnDisk) {
    this.sizeOnDisk = sizeOnDisk;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    ModuleMetadata<?> that = (ModuleMetadata<?>) o;

    return new EqualsBuilder()
        .append(xapiEnabled, that.xapiEnabled)
        .append(sizeOnDisk, that.sizeOnDisk)
        .append(manifest, that.manifest)
        .append(moduleType, that.moduleType)
        .append(moduleEditionType, that.moduleEditionType)
        .isEquals();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder(17, 37)
        .append(manifest)
        .append(moduleType)
        .append(moduleEditionType)
        .append(xapiEnabled)
        .append(sizeOnDisk)
        .toHashCode();
  }

  /**
   * Retrieves the manifest associated with this module metadata.
   *
   * @return the manifest of the module as an instance of type M
   */
  public M getManifest() {
    return this.manifest;
  }

  /**
   * Retrieves the type of the module.
   *
   * @return the module type as an instance of {@link ModuleType}
   */
  public ModuleType getModuleType() {
    return this.moduleType;
  }

  /**
   * Retrieves the edition type of the module.
   *
   * @return the module edition type as an instance of {@link ModuleEditionType}
   */
  public ModuleEditionType getModuleEditionType() {
    return this.moduleEditionType;
  }

  /**
   * Checks whether xAPI is enabled for the module.
   *
   * @return true if xAPI is enabled, false otherwise
   */
  public boolean isXapiEnabled() {
    return this.xapiEnabled;
  }
}
