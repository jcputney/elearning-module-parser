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

package dev.jcputney.elearning.parser.output.metadata.cmi5;

import dev.jcputney.elearning.parser.enums.ModuleType;
import dev.jcputney.elearning.parser.input.cmi5.AU;
import dev.jcputney.elearning.parser.input.cmi5.Block;
import dev.jcputney.elearning.parser.input.cmi5.Cmi5Manifest;
import dev.jcputney.elearning.parser.input.cmi5.Objectives;
import dev.jcputney.elearning.parser.input.cmi5.types.Objective;
import dev.jcputney.elearning.parser.output.metadata.BaseModuleMetadata;
import dev.jcputney.elearning.parser.output.metadata.SimpleMetadata;
import java.util.List;
import java.util.Optional;
import lombok.experimental.SuperBuilder;

/**
 * Represents metadata for a cmi5 module, including cmi5-specific fields such as prerequisites,
 * dependencies, assignable units, custom data, and xAPI support.
 * <p>
 * This class extends the base {@link BaseModuleMetadata} class to provide cmi5-specific information
 * for LMS tracking and reporting, including a list of Assignable Units (AUs) and custom data
 * fields.
 * </p>
 */
@SuperBuilder
public class Cmi5Metadata extends BaseModuleMetadata<Cmi5Manifest> {

  /**
   * Default constructor for the Cmi5Metadata class.
   */
  @SuppressWarnings("unused")
  protected Cmi5Metadata() {
    // Default constructor
  }

  /**
   * Creates a new Cmi5Metadata instance with standard cmi5 metadata components.
   *
   * @param manifest The cmi5 manifest.
   * @param xapiEnabled Whether xAPI is enabled.
   * @return A new Cmi5Metadata instance.
   */
  public static Cmi5Metadata create(Cmi5Manifest manifest, boolean xapiEnabled) {
    Cmi5Metadata metadata = Cmi5Metadata.builder()
        .manifest(manifest)
        .moduleType(ModuleType.CMI5)
        .xapiEnabled(xapiEnabled)
        .build();

    // Add cmi5 specific metadata
    SimpleMetadata cmi5Metadata = metadata.getSimpleMetadata(manifest);

    // Add cmi5-specific fields

    // Add assignable units if available
    List<AU> assignableUnits = manifest.getAssignableUnits();
    if (assignableUnits != null && !assignableUnits.isEmpty()) {
      // Add assignable unit IDs
      List<String> assignableUnitIds = assignableUnits.stream()
          .map(AU::getId)
          .toList();
      cmi5Metadata.addMetadata("assignableUnitIds", assignableUnitIds);

      // Add assignable unit URLs
      List<String> assignableUnitUrls = assignableUnits.stream()
          .map(AU::getUrl)
          .toList();
      cmi5Metadata.addMetadata("assignableUnitUrls", assignableUnitUrls);
    }

    // Add blocks if available
    List<Block> blocks = manifest.getBlocks();
    if (blocks != null && !blocks.isEmpty()) {
      // Add block IDs
      List<String> blockIds = blocks.stream()
          .map(Block::getId)
          .toList();
      cmi5Metadata.addMetadata("blockIds", blockIds);
    }

    // Add objectives if available
    Optional.ofNullable(manifest.getObjectives())
        .map(Objectives::getObjectives)
        .filter(objectiveList -> !objectiveList.isEmpty())
        .ifPresent(objectiveList -> {
          // Add objective IDs
          List<String> objectiveIds = objectiveList.stream()
              .map(Objective::getId)
              .toList();
          cmi5Metadata.addMetadata("objectiveIds", objectiveIds);
        });

    // Add the cmi5 metadata component to the composite
    metadata.addMetadataComponent(cmi5Metadata);

    return metadata;
  }
}
