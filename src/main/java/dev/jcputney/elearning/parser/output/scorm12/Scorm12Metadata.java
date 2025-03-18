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

package dev.jcputney.elearning.parser.output.scorm12;

import dev.jcputney.elearning.parser.enums.ModuleType;
import dev.jcputney.elearning.parser.input.scorm12.Scorm12Manifest;
import dev.jcputney.elearning.parser.output.ModuleMetadata;
import lombok.NoArgsConstructor;

/**
 * Represents metadata for SCORM 1.2 eLearning modules, including SCORM 1.2-specific fields such as
 * mastery score, prerequisites, and custom data.
 * <p>
 * This class extends the base {@link ModuleMetadata} class with additional fields specific to SCORM
 * 1.2, providing metadata that describes the learning module content and requirements.
 * </p>
 */
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class Scorm12Metadata extends ModuleMetadata<Scorm12Manifest> {

  public Scorm12Metadata(Scorm12Manifest manifest, ModuleType moduleType, boolean xapiEnabled) {
    super(manifest, moduleType, xapiEnabled);
  }
}