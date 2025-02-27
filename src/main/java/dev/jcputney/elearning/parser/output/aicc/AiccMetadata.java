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

package dev.jcputney.elearning.parser.output.aicc;

import dev.jcputney.elearning.parser.enums.ModuleType;
import dev.jcputney.elearning.parser.input.aicc.AiccManifest;
import dev.jcputney.elearning.parser.output.ModuleMetadata;
import lombok.NoArgsConstructor;

/**
 * Represents metadata for an AICC eLearning module, including AICC-specific fields such as course
 * structure, assignable units, prerequisites, objectives, credit type, time limit actions, and
 * objective relationships.
 * <p>
 * This class extends the base {@link ModuleMetadata} class to provide metadata specific to AICC
 * modules, enabling structured storage of AICC format details.
 * </p>
 */
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class AiccMetadata extends ModuleMetadata<AiccManifest> {

  public AiccMetadata(AiccManifest manifest, ModuleType moduleType,
      boolean xapiEnabled) {
    super(manifest, moduleType, xapiEnabled);
  }
}