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
@SuppressWarnings("NullableProblems")
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
    this.sizeOnDisk = -1; // Initialize default value explicitly
  }

  protected ModuleMetadata() {
  }

  protected ModuleMetadata(ModuleMetadataBuilder<M, ?, ?> b) {
    this.manifest = b.manifest;
    this.moduleType = b.moduleType;
    this.moduleEditionType = b.moduleEditionType;
    if (b.xapiEnabled$set) {
      this.xapiEnabled = b.xapiEnabled$value;
    } else {
      this.xapiEnabled = $default$xapiEnabled();
    }
    if (b.sizeOnDisk$set) {
      this.sizeOnDisk = b.sizeOnDisk$value;
    } else {
      this.sizeOnDisk = $default$sizeOnDisk();
    }
  }

  private static boolean $default$xapiEnabled() {
    return false;
  }

  private static long $default$sizeOnDisk() {
    return -1;
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

  /**
   * Gets the total size of all files in the module on disk.
   *
   * @return Total size of all files in bytes, or -1 if not available
   */
  public long getSizeOnDisk() {
    return sizeOnDisk;
  }

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

  public M getManifest() {
    return this.manifest;
  }

  public ModuleType getModuleType() {
    return this.moduleType;
  }

  public ModuleEditionType getModuleEditionType() {
    return this.moduleEditionType;
  }

  public boolean isXapiEnabled() {
    return this.xapiEnabled;
  }

  public static abstract class ModuleMetadataBuilder<M extends PackageManifest, C extends ModuleMetadata<M>, B extends ModuleMetadataBuilder<M, C, B>> {

    private M manifest;
    private ModuleType moduleType;
    private ModuleEditionType moduleEditionType;
    private boolean xapiEnabled$value;
    private boolean xapiEnabled$set;
    private long sizeOnDisk$value;
    private boolean sizeOnDisk$set;

    public B manifest(M manifest) {
      this.manifest = manifest;
      return self();
    }

    public B moduleType(ModuleType moduleType) {
      this.moduleType = moduleType;
      return self();
    }

    public B moduleEditionType(ModuleEditionType moduleEditionType) {
      this.moduleEditionType = moduleEditionType;
      return self();
    }

    public B xapiEnabled(boolean xapiEnabled) {
      this.xapiEnabled$value = xapiEnabled;
      this.xapiEnabled$set = true;
      return self();
    }

    public B sizeOnDisk(long sizeOnDisk) {
      this.sizeOnDisk$value = sizeOnDisk;
      this.sizeOnDisk$set = true;
      return self();
    }

    public abstract C build();

    public String toString() {
      return "ModuleMetadata.ModuleMetadataBuilder(manifest=" + this.manifest + ", moduleType="
          + this.moduleType
          + ", moduleEditionType=" + this.moduleEditionType + ", xapiEnabled$value="
          + this.xapiEnabled$value
          + ", sizeOnDisk$value=" + this.sizeOnDisk$value + ")";
    }

    protected abstract B self();
  }
}
