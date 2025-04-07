/*
 * qlty-ignore: +qlty:similar-code
 *
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
 *
 * qlty-ignore: -qlty:similar-code
 */

package dev.jcputney.elearning.parser.output.metadata.aicc;

import dev.jcputney.elearning.parser.enums.ModuleType;
import dev.jcputney.elearning.parser.input.aicc.AiccManifest;
import dev.jcputney.elearning.parser.input.aicc.AssignableUnit;
import dev.jcputney.elearning.parser.output.metadata.BaseModuleMetadata;
import dev.jcputney.elearning.parser.output.metadata.SimpleMetadata;
import java.util.List;
import lombok.experimental.SuperBuilder;

/**
 * Represents metadata for an AICC eLearning module, including AICC-specific fields such as course
 * structure, assignable units, prerequisites, objectives, credit type, time limit actions, and
 * objective relationships.
 * <p>
 * This class extends the base {@link BaseModuleMetadata} class to provide metadata specific to AICC
 * modules, enabling structured storage of AICC format details.
 * </p>
 */
@SuperBuilder
public class AiccMetadata extends BaseModuleMetadata<AiccManifest> {

  /**
   * Default constructor for the AiccMetadata class.
   */
  @SuppressWarnings("unused")
  protected AiccMetadata() {
    // Default constructor
  }

  /**
   * Creates a new AiccMetadata instance with standard AICC metadata components.
   *
   * @param manifest The AICC manifest.
   * @param xapiEnabled Whether xAPI is enabled.
   * @return A new AiccMetadata instance.
   */
  public static AiccMetadata create(AiccManifest manifest, boolean xapiEnabled) {
    AiccMetadata metadata = AiccMetadata.builder()
        .manifest(manifest)
        .moduleType(ModuleType.AICC)
        .xapiEnabled(xapiEnabled)
        .build();

    // Add AICC specific metadata
    SimpleMetadata aiccMetadata = metadata.getSimpleMetadata(manifest);

    // Add AICC-specific fields
    List<AssignableUnit> assignableUnits = manifest.getAssignableUnits();
    if (assignableUnits != null && !assignableUnits.isEmpty()) {
      // Add assignable unit IDs
      List<String> assignableUnitIds = assignableUnits.stream()
          .map(AssignableUnit::getSystemId)
          .toList();
      aiccMetadata.addMetadata("assignableUnitIds", assignableUnitIds);

      // Add assignable unit names
      List<String> assignableUnitNames = assignableUnits.stream()
          .map(AssignableUnit::getFileName)
          .toList();
      aiccMetadata.addMetadata("assignableUnitNames", assignableUnitNames);
    }

    // Add the AICC metadata component to the composite
    metadata.addMetadataComponent(aiccMetadata);

    return metadata;
  }
}