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

package dev.jcputney.elearning.parser.output.metadata.scorm2004;

import com.fasterxml.jackson.annotation.JsonIgnore;
import dev.jcputney.elearning.parser.enums.ModuleType;
import dev.jcputney.elearning.parser.input.scorm2004.Scorm2004Manifest;
import dev.jcputney.elearning.parser.input.scorm2004.ims.ss.objective.Scorm2004Objective;
import dev.jcputney.elearning.parser.input.scorm2004.ims.ss.objective.Scorm2004ObjectiveMapping;
import dev.jcputney.elearning.parser.output.metadata.BaseModuleMetadata;
import dev.jcputney.elearning.parser.output.metadata.SimpleMetadata;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.experimental.SuperBuilder;

/**
 * Represents metadata for SCORM 2004 eLearning modules, including SCORM 2004-specific fields such
 * as sequencing information, mastery score, custom data, prerequisites, and additional metadata
 * from external manifests.
 * <p>
 * This class extends the base {@link BaseModuleMetadata} class to provide metadata that describes
 * the structure and rules that are specific to SCORM 2004 modules.
 * </p>
 */
@SuperBuilder
public class Scorm2004Metadata extends BaseModuleMetadata<Scorm2004Manifest> {

  /**
   * Default constructor for the Scorm2004Metadata class.
   */
  @SuppressWarnings("unused")
  protected Scorm2004Metadata() {
    // Default constructor
  }

  /**
   * Creates a new Scorm2004Metadata instance with standard SCORM 2004 metadata components.
   *
   * @param manifest    The SCORM 2004 manifest.
   * @param xapiEnabled Whether xAPI is enabled.
   * @return A new Scorm2004Metadata instance.
   */
  public static Scorm2004Metadata create(Scorm2004Manifest manifest, boolean xapiEnabled) {
    Scorm2004Metadata metadata =
        Scorm2004Metadata.builder().manifest(manifest).moduleType(ModuleType.SCORM_2004).xapiEnabled(xapiEnabled)
            .build();

    // Add SCORM 2004 specific metadata
    SimpleMetadata scorm2004Metadata = metadata.getSimpleMetadata(manifest);

    // Add global objective IDs
    Set<String> globalObjectiveIds = getGlobalObjectiveIds(manifest);
    if (!globalObjectiveIds.isEmpty()) {
      scorm2004Metadata.addMetadata("globalObjectiveIds", globalObjectiveIds);
    }

    // Add the SCORM 2004 metadata component to the composite
    metadata.addMetadataComponent(scorm2004Metadata);

    return metadata;
  }

  /**
   * Retrieves the set of global objective IDs from the manifest. A global objective ID is defined
   * by the presence of a targetObjectiveID in a mapInfo element.
   *
   * @param manifest The SCORM 2004 manifest.
   * @return A set of global objective IDs.
   */
  @JsonIgnore
  private static Set<String> getGlobalObjectiveIds(Scorm2004Manifest manifest) {
    return manifest.getOrganizations().getOrganizationList().stream()
        .flatMap(org -> safeStream(org.getItems())) // Null-safe stream for items
        .flatMap(item -> safeStream(getObjectives(item))) // Null-safe stream for objectives
        .flatMap(obj -> safeStream(obj.getMapInfo())) // Null-safe stream for mapInfo
        .map(Scorm2004ObjectiveMapping::getTargetObjectiveID)
        .filter(id -> id != null && !id.isEmpty()) // Filter non-null and non-empty IDs
        .collect(Collectors.toSet());
  }

  /**
   * Retrieves the list of objectives from a given item in a null-safe manner.
   *
   * @param item The SCORM item to retrieve objectives from.
   * @return A list of objectives, or an empty list if null.
   */
  private static List<Scorm2004Objective> getObjectives(
      dev.jcputney.elearning.parser.input.scorm2004.ims.cp.Scorm2004Item item) {
    if (item.getSequencing() != null && item.getSequencing().getObjectives() != null) {
      return item.getSequencing().getObjectives().getObjectiveList();
    }
    return List.of(); // Return an empty list if objectives are null
  }

  /**
   * Wraps a potentially null collection in a stream.
   *
   * @param collection The collection to wrap.
   * @param <T>        The type of elements in the collection.
   * @return A stream of elements, or an empty stream if the collection is null.
   */
  private static <T> Stream<T> safeStream(Collection<T> collection) {
    return collection != null ? collection.stream() : Stream.empty();
  }
}