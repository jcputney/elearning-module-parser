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

package dev.jcputney.elearning.parser.output.cmi5;

import dev.jcputney.elearning.parser.enums.ModuleType;
import dev.jcputney.elearning.parser.input.cmi5.Cmi5Manifest;
import dev.jcputney.elearning.parser.output.ModuleMetadata;
import lombok.NoArgsConstructor;

/**
 * Represents metadata for a cmi5 module, including cmi5-specific fields such as prerequisites,
 * dependencies, assignable units, custom data, and xAPI support.
 * <p>
 * This class extends the base {@link ModuleMetadata} class to provide cmi5-specific information for
 * LMS tracking and reporting, including a list of Assignable Units (AUs) and custom data fields.
 * </p>
 */
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class Cmi5Metadata extends ModuleMetadata<Cmi5Manifest> {

  public Cmi5Metadata(Cmi5Manifest manifest, ModuleType moduleType,
      boolean xapiEnabled) {
    super(manifest, moduleType, xapiEnabled);
  }
}