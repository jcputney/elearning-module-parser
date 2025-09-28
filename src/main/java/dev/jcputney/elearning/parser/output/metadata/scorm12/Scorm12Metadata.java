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

import dev.jcputney.elearning.parser.enums.ModuleEditionType;
import dev.jcputney.elearning.parser.enums.ModuleType;
import dev.jcputney.elearning.parser.input.scorm12.Scorm12Manifest;
import dev.jcputney.elearning.parser.input.scorm12.ims.cp.Scorm12Item;
import dev.jcputney.elearning.parser.output.metadata.BaseModuleMetadata;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * Represents metadata for SCORM 1.2 eLearning modules, including SCORM 1.2-specific fields such as
 * mastery score, prerequisites, and custom data.
 * <p>
 * This class extends the base {@link BaseModuleMetadata} class with additional fields specific to
 * SCORM 1.2, providing metadata that describes the learning module content and requirements.
 * </p>
 */
public class Scorm12Metadata extends BaseModuleMetadata<Scorm12Manifest> {

  private final Map<String, String> prerequisites = new LinkedHashMap<>();
  private final Map<String, Double> masteryScores = new LinkedHashMap<>();
  private final Map<String, String> customData = new LinkedHashMap<>();

  /**
   * Protected constructor for the Scorm12Metadata class. Initializes a new instance while
   * restricting direct instantiation outside the class or its subclasses.
   */
  protected Scorm12Metadata() {
  }

  /**
   * Creates a new Scorm12Metadata instance with standard SCORM 1.2 metadata components.
   *
   * @param manifest The SCORM 1.2 manifest.
   * @param xapiEnabled Whether xAPI is enabled.
   * @return A new Scorm12Metadata instance.
   */
  public static Scorm12Metadata create(Scorm12Manifest manifest, boolean xapiEnabled) {
    Scorm12Metadata metadata = new Scorm12Metadata();
    metadata.manifest = manifest;
    metadata.moduleType = ModuleType.SCORM_12;
    metadata.moduleEditionType = ModuleEditionType.SCORM_12;
    metadata.xapiEnabled = xapiEnabled;

    // Add SCORM 1.2 specific fields from items
    metadata.extractScorm12SpecificMetadata(manifest);

    return metadata;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (!(o instanceof Scorm12Metadata that)) {
      return false;
    }

    return new EqualsBuilder()
        .appendSuper(super.equals(o))
        .append(getPrerequisites(), that.getPrerequisites())
        .append(getMasteryScores(), that.getMasteryScores())
        .append(getCustomData(), that.getCustomData())
        .isEquals();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder(17, 37)
        .appendSuper(super.hashCode())
        .append(getPrerequisites())
        .append(getMasteryScores())
        .append(getCustomData())
        .toHashCode();
  }

  public Map<String, String> getPrerequisites() {
    return Map.copyOf(prerequisites);
  }

  public Map<String, Double> getMasteryScores() {
    return Map.copyOf(masteryScores);
  }

  public Map<String, String> getCustomData() {
    return Map.copyOf(customData);
  }

  /**
   * Extracts SCORM 1.2 specific metadata from the manifest, including prerequisites, mastery
   * scores, and other item-level information.
   *
   * @param manifest The SCORM 1.2 manifest.
   */
  private void extractScorm12SpecificMetadata(Scorm12Manifest manifest) {
    if (manifest.getOrganizations() != null && manifest
        .getOrganizations()
        .getDefault() != null && manifest
        .getOrganizations()
        .getDefault()
        .getItems() != null) {

      List<Scorm12Item> items = manifest
          .getOrganizations()
          .getDefault()
          .getItems();

      prerequisites.clear();
      masteryScores.clear();
      customData.clear();

      extractItemData(items, prerequisites, masteryScores, customData);
    }
  }

  /**
   * Recursively extracts item-level data from a list of SCORM 1.2 items.
   *
   * @param items The list of items to process.
   * @param prerequisites The map to store prerequisites.
   * @param masteryScores The map to store mastery scores.
   * @param customData The map to store custom data.
   */
  private void extractItemData(List<Scorm12Item> items, Map<String, String> prerequisites,
      Map<String, Double> masteryScores, Map<String, String> customData) {
    for (Scorm12Item item : items) {
      String itemId = item.getIdentifier();

      // Extract prerequisites
      if (item.getPrerequisites() != null && item
          .getPrerequisites()
          .getValue() != null) {
        prerequisites.put(itemId, item
            .getPrerequisites()
            .getValue());
      }

      // Extract mastery score
      if (item.getMasteryScore() != null) {
        masteryScores.put(itemId, item.getMasteryScore());
      }

      // Extract custom data (dataFromLMS)
      if (item.getDataFromLMS() != null) {
        customData.put(itemId, item.getDataFromLMS());
      }

      // Recursively process child items
      if (item.getItems() != null && !item
          .getItems()
          .isEmpty()) {
        extractItemData(item.getItems(), prerequisites, masteryScores, customData);
      }
    }
  }
}
