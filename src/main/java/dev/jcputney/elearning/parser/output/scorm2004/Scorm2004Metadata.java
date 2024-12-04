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

package dev.jcputney.elearning.parser.output.scorm2004;

import dev.jcputney.elearning.parser.enums.ModuleType;
import dev.jcputney.elearning.parser.input.scorm2004.Scorm2004Manifest;
import dev.jcputney.elearning.parser.input.scorm2004.ims.cp.Scorm2004Item;
import dev.jcputney.elearning.parser.input.scorm2004.ims.ss.objective.Scorm2004Objective;
import dev.jcputney.elearning.parser.input.scorm2004.ims.ss.objective.Scorm2004ObjectiveMapping;
import dev.jcputney.elearning.parser.output.ModuleMetadata;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Represents metadata for SCORM 2004 eLearning modules, including SCORM 2004-specific fields such
 * as sequencing information, mastery score, custom data, prerequisites, and additional metadata
 * from external manifests.
 * <p>
 * This class extends the base {@link ModuleMetadata} class to provide metadata that describes the
 * structure and rules that are specific to SCORM 2004 modules.
 * </p>
 */
public class Scorm2004Metadata extends ModuleMetadata<Scorm2004Manifest> {

  public Scorm2004Metadata(Scorm2004Manifest manifest, ModuleType moduleType,
      boolean xapiEnabled) {
    super(manifest, moduleType, xapiEnabled);
  }

  /**
   * Retrieves the set of global objective IDs from the manifest.
   * A global objective ID is defined by the presence of a targetObjectiveID in a mapInfo element.
   *
   * @return A set of global objective IDs.
   */
  public Set<String> getGlobalObjectiveIds() {
    return getManifest()
        .getOrganizations()
        .getOrganizationList()
        .stream()
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
  private List<Scorm2004Objective> getObjectives(Scorm2004Item item) {
    if (item.getSequencing() != null && item.getSequencing().getObjectives() != null) {
      return item.getSequencing().getObjectives().getObjectiveList();
    }
    return List.of(); // Return an empty list if objectives are null
  }

  /**
   * Wraps a potentially null collection in a stream.
   *
   * @param collection The collection to wrap.
   * @param <T> The type of elements in the collection.
   * @return A stream of elements, or an empty stream if the collection is null.
   */
  private <T> Stream<T> safeStream(Collection<T> collection) {
    return collection != null ? collection.stream() : Stream.empty();
  }
}