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
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Represents the core metadata for an eLearning module, providing common fields shared across
 * various module types, such as SCORM, AICC, or cmi5.
 * <p>
 * Each module type can extend this class to include additional fields specific to that type.
 * </p>
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public abstract class ModuleMetadata<M extends PackageManifest> implements PackageManifest {

  private M manifest;
  private ModuleType moduleType;
  private boolean xapiEnabled;

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