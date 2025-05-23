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

package dev.jcputney.elearning.parser.output.metadata.scorm12;

import dev.jcputney.elearning.parser.enums.ModuleType;
import dev.jcputney.elearning.parser.input.scorm12.Scorm12Manifest;
import dev.jcputney.elearning.parser.output.metadata.BaseModuleMetadata;
import dev.jcputney.elearning.parser.output.metadata.SimpleMetadata;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * Represents metadata for SCORM 1.2 eLearning modules, including SCORM 1.2-specific fields such as
 * mastery score, prerequisites, and custom data.
 * <p>
 * This class extends the base {@link BaseModuleMetadata} class with additional fields specific to
 * SCORM 1.2, providing metadata that describes the learning module content and requirements.
 * </p>
 */
@SuperBuilder
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
@EqualsAndHashCode(doNotUseGetters = true, callSuper = true)
public class Scorm12Metadata extends BaseModuleMetadata<Scorm12Manifest> {

  /**
   * Creates a new Scorm12Metadata instance with standard SCORM 1.2 metadata components.
   *
   * @param manifest The SCORM 1.2 manifest.
   * @param xapiEnabled Whether xAPI is enabled.
   * @return A new Scorm12Metadata instance.
   */
  public static Scorm12Metadata create(Scorm12Manifest manifest, boolean xapiEnabled) {
    Scorm12Metadata metadata =
        Scorm12Metadata
            .builder()
            .manifest(manifest)
            .moduleType(ModuleType.SCORM_12)
            .xapiEnabled(xapiEnabled)
            .build();

    // Add SCORM 1.2 specific metadata
    SimpleMetadata scorm12Metadata = metadata.getSimpleMetadata(manifest);

    // Add the SCORM 1.2 metadata component to the composite
    metadata.addMetadataComponent(scorm12Metadata);

    return metadata;
  }
}
