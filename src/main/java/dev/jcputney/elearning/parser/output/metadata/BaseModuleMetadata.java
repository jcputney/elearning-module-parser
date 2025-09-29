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

import dev.jcputney.elearning.parser.enums.ModuleType;
import dev.jcputney.elearning.parser.input.PackageManifest;
import dev.jcputney.elearning.parser.output.ModuleMetadata;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * Base class for module metadata.
 *
 * @param <M> The type of package manifest.
 */
public abstract class BaseModuleMetadata<M extends PackageManifest> extends ModuleMetadata<M> {

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
  }

  protected BaseModuleMetadata() {
    // no-op
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (!(o instanceof BaseModuleMetadata<?>)) {
      return false;
    }

    return new EqualsBuilder()
        .appendSuper(super.equals(o))
        .isEquals();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder(17, 37)
        .appendSuper(super.hashCode())
        .toHashCode();
  }
}
