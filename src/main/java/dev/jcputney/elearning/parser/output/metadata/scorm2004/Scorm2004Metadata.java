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

package dev.jcputney.elearning.parser.output.metadata.scorm2004;

import com.fasterxml.jackson.annotation.JsonIgnore;
import dev.jcputney.elearning.parser.enums.ModuleEditionType;
import dev.jcputney.elearning.parser.enums.ModuleType;
import dev.jcputney.elearning.parser.input.scorm2004.Scorm2004Manifest;
import dev.jcputney.elearning.parser.input.scorm2004.ims.cp.Scorm2004Item;
import dev.jcputney.elearning.parser.input.scorm2004.ims.ss.objective.Scorm2004Objective;
import dev.jcputney.elearning.parser.input.scorm2004.ims.ss.objective.Scorm2004ObjectiveMapping;
import dev.jcputney.elearning.parser.output.metadata.BaseModuleMetadata;
import dev.jcputney.elearning.parser.output.metadata.SimpleMetadata;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * Represents metadata for SCORM 2004 eLearning modules, including SCORM 2004-specific fields such
 * as sequencing information, mastery score, custom data, prerequisites, and additional metadata
 * from external manifests.
 * <p>
 * This class extends the base {@link BaseModuleMetadata} class to provide metadata that describes
 * the structure and rules that are specific to SCORM 2004 modules.
 * </p>
 */
public class Scorm2004Metadata extends BaseModuleMetadata<Scorm2004Manifest> {

  private boolean hasSequencing;

  protected Scorm2004Metadata() {
  }

  /**
   * Creates a new Scorm2004Metadata instance with standard SCORM 2004 metadata components.
   *
   * @param manifest The SCORM 2004 manifest.
   * @param xapiEnabled Whether xAPI is enabled.
   * @return A new Scorm2004Metadata instance.
   */
  public static Scorm2004Metadata create(Scorm2004Manifest manifest, boolean xapiEnabled) {
    // Detect the SCORM 2004 edition from the manifest metadata
    String schemaVersion = null;
    if (manifest.getMetadata() != null) {
      schemaVersion = manifest
          .getMetadata()
          .getSchemaVersion();
    }

    // Determine the specific edition type
    ModuleEditionType editionType = ModuleEditionType.fromModuleType(ModuleType.SCORM_2004,
        schemaVersion);

    Scorm2004Metadata metadata = new Scorm2004Metadata();
    metadata.manifest = manifest;
    metadata.moduleType = ModuleType.SCORM_2004;
    metadata.moduleEditionType = editionType;
    metadata.xapiEnabled = xapiEnabled;
    metadata.hasSequencing = hasSequencing(manifest);

    // Add SCORM 2004 specific metadata
    SimpleMetadata scorm2004Metadata = metadata.getSimpleMetadata(manifest);

    // Add global objective IDs
    Set<String> globalObjectiveIds = metadata.getGlobalObjectiveIds();
    if (!globalObjectiveIds.isEmpty()) {
      scorm2004Metadata.addMetadata("globalObjectiveIds", globalObjectiveIds);
    }

    // Add sequencing flag
    scorm2004Metadata.addMetadata("hasSequencing", metadata.hasSequencing);

    // Add the SCORM 2004 metadata component to the composite
    metadata.addMetadataComponent(scorm2004Metadata);

    return metadata;
  }

  /**
   * Retrieves the list of objectives from a given item in a null-safe manner.
   *
   * @param item The SCORM item to retrieve objectives from.
   * @return A list of objectives, or an empty list if null.
   */
  private static List<Scorm2004Objective> getObjectives(Scorm2004Item item) {
    if (item.getSequencing() != null && item
        .getSequencing()
        .getObjectives() != null) {
      return item
          .getSequencing()
          .getObjectives()
          .getObjectiveList();
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
  private static <T> Stream<T> safeStream(Collection<T> collection) {
    return collection != null ? collection.stream() : Stream.empty();
  }

  /**
   * Determines if a SCORM 2004 manifest has sequencing information.
   * <p>
   * This method checks if any item in any organization has sequencing rules defined. Sequencing is
   * considered present if any item has a non-null sequencing element.
   * </p>
   *
   * @param manifest The SCORM 2004 manifest to check.
   * @return true if the manifest contains sequencing information, false otherwise.
   */
  public static boolean hasSequencing(Scorm2004Manifest manifest) {
    if (manifest == null || manifest.getOrganizations() == null) {
      return false;
    }

    return manifest
        .getOrganizations()
        .getOrganizationList()
        .stream()
        .flatMap(org -> safeStream(org.getItems()))
        .anyMatch(Scorm2004Metadata::hasSequencingInItem);
  }

  /**
   * Recursively checks if an item or any of its subitems has sequencing.
   *
   * @param item The item to check.
   * @return true if the item or any subitem has sequencing, false otherwise.
   */
  private static boolean hasSequencingInItem(Scorm2004Item item) {
    if (item == null) {
      return false;
    }

    // Check if this item has sequencing
    if (item.getSequencing() != null) {
      return true;
    }

    // Check subitems recursively
    if (item.getItems() != null) {
      return item
          .getItems()
          .stream()
          .anyMatch(Scorm2004Metadata::hasSequencingInItem);
    }

    return false;
  }

  /**
   * Retrieves the set of global objective IDs from the manifest. A global objective ID is defined
   * by the presence of a targetObjectiveID in a mapInfo element.
   *
   * @return A set of global objective IDs.
   */
  @JsonIgnore
  public Set<String> getGlobalObjectiveIds() {
    return manifest
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

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (!(o instanceof Scorm2004Metadata that)) {
      return false;
    }

    return new EqualsBuilder()
        .appendSuper(super.equals(o))
        .append(isHasSequencing(), that.isHasSequencing())
        .isEquals();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder(17, 37)
        .appendSuper(super.hashCode())
        .append(isHasSequencing())
        .toHashCode();
  }

  public boolean isHasSequencing() {
    return this.hasSequencing;
  }
}