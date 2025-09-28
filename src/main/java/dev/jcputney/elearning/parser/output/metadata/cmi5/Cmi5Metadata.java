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

import dev.jcputney.elearning.parser.enums.ModuleEditionType;
import dev.jcputney.elearning.parser.enums.ModuleType;
import dev.jcputney.elearning.parser.input.cmi5.AU;
import dev.jcputney.elearning.parser.input.cmi5.Block;
import dev.jcputney.elearning.parser.input.cmi5.Cmi5Manifest;
import dev.jcputney.elearning.parser.input.cmi5.ObjectivesList;
import dev.jcputney.elearning.parser.input.cmi5.types.Objective;
import dev.jcputney.elearning.parser.output.metadata.BaseModuleMetadata;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * Represents metadata for a cmi5 module, including cmi5-specific fields such as prerequisites,
 * dependencies, assignable units, custom data, and xAPI support.
 * <p>
 * This class extends the base {@link BaseModuleMetadata} class to provide cmi5-specific information
 * for LMS tracking and reporting, including a list of Assignable Units (AUs) and custom data
 * fields.
 * </p>
 */
public class Cmi5Metadata extends BaseModuleMetadata<Cmi5Manifest> {

  private final List<String> assignableUnitIds = new ArrayList<>();
  private final List<String> assignableUnitUrls = new ArrayList<>();
  private final Map<String, Map<String, Object>> auDetails = new HashMap<>();
  private final Map<String, Double> masteryScores = new HashMap<>();
  private final Map<String, String> moveOnCriteria = new HashMap<>();
  private final Map<String, String> launchMethods = new HashMap<>();
  private final Map<String, String> activityTypes = new HashMap<>();
  private final Map<String, String> launchParameters = new HashMap<>();
  private final List<String> blockIds = new ArrayList<>();
  private final List<String> objectiveIds = new ArrayList<>();

  /**
   * Default constructor for the Cmi5Metadata class. This constructor initializes a new instance of
   * Cmi5Metadata. Designed to be used internally or by derived classes.
   */
  protected Cmi5Metadata() {
  }

  /**
   * Creates a new Cmi5Metadata instance with standard cmi5 metadata components.
   *
   * @param manifest The cmi5 manifest.
   * @param xapiEnabled Whether xAPI is enabled.
   * @return A new Cmi5Metadata instance.
   */
  public static Cmi5Metadata create(Cmi5Manifest manifest, boolean xapiEnabled) {
    Cmi5Metadata metadata = new Cmi5Metadata();
    metadata.manifest = manifest;
    metadata.moduleType = ModuleType.CMI5;
    metadata.moduleEditionType = ModuleEditionType.CMI5;
    metadata.xapiEnabled = xapiEnabled;

    // Extract all AUs (from root level and blocks)
    List<AU> allAssignableUnits = getAllAssignableUnits(manifest);
    if (!allAssignableUnits.isEmpty()) {
      for (AU au : allAssignableUnits) {
        addAssignableUnitMetadata(metadata, au);
      }
      List<String> auIds = allAssignableUnits
          .stream()
          .map(AU::getId)
          .toList();
      List<String> auUrls = allAssignableUnits
          .stream()
          .map(AU::getUrl)
          .toList();
      metadata.assignableUnitIds.addAll(auIds);
      metadata.assignableUnitUrls.addAll(auUrls);
    }

    // Add blocks if available
    List<Block> blocks = manifest.getBlocks();
    if (blocks != null && !blocks.isEmpty()) {
      List<String> blockIds = blocks
          .stream()
          .map(Block::getId)
          .toList();
      metadata.blockIds.addAll(blockIds);
    }

    // Add objectives if available
    Optional
        .ofNullable(manifest.getObjectives())
        .map(ObjectivesList::getObjectives)
        .filter(objectives -> !objectives.isEmpty())
        .ifPresent(objectives ->
            metadata.objectiveIds.addAll(
                objectives
                    .stream()
                    .map(Objective::getId)
                    .toList()
            )
        );

    return metadata;
  }

  /**
   * Extract or consolidate all metadata for a single AU into the provided metadata instance.
   */
  private static void addAssignableUnitMetadata(Cmi5Metadata metadata, AU au) {
    String auId = au.getId();
    metadata.auDetails.put(auId, getActivityUnitMetadata(au));

    if (au.getMasteryScore() != null) {
      metadata.masteryScores.put(auId, au
          .getMasteryScore()
          .value()
          .doubleValue());
    }
    if (au.getMoveOn() != null) {
      metadata.moveOnCriteria.put(auId, au
          .getMoveOn()
          .name());
    }
    if (au.getLaunchMethod() != null) {
      metadata.launchMethods.put(auId, au
          .getLaunchMethod()
          .name());
    }
    if (au.getActivityType() != null) {
      metadata.activityTypes.put(auId, au.getActivityType());
    }
    if (au.getLaunchParameters() != null) {
      metadata.launchParameters.put(auId, au.getLaunchParameters());
    }
  }

  /**
   * Extracts metadata for a given activity unit (AU). The metadata includes details such as the
   * activity unit's URL, title, and description if they are available.
   *
   * @param au The activity unit (AU) to extract metadata from. It contains the details of the
   * activity unit that need to be processed and retrieved.
   * @return A map containing the metadata of the provided activity unit. The map could include
   * key-value pairs for "url", "title", and "description" if these attributes exist in the activity
   * unit.
   */
  private static Map<String, Object> getActivityUnitMetadata(AU au) {
    Map<String, Object> auInfo = new HashMap<>();
    auInfo.put("url", au.getUrl());
    if (au.getTitle() != null && au
        .getTitle()
        .getStrings() != null && !au
        .getTitle()
        .getStrings()
        .isEmpty()) {
      auInfo.put("title", au
          .getTitle()
          .getStrings()
          .get(0)
          .getValue());
    }
    if (au.getDescription() != null && au
        .getDescription()
        .getStrings() != null && !au
        .getDescription()
        .getStrings()
        .isEmpty()) {
      auInfo.put("description", au
          .getDescription()
          .getStrings()
          .get(0)
          .getValue());
    }
    return auInfo;
  }

  /**
   * Recursively extracts all assignable units from the manifest, including those nested in blocks.
   *
   * @param manifest The CMI5 manifest
   * @return A list of all assignable units
   */
  private static List<AU> getAllAssignableUnits(Cmi5Manifest manifest) {
    List<AU> allAUs = new ArrayList<>();

    // Add root-level AUs
    if (manifest.getAssignableUnits() != null) {
      allAUs.addAll(manifest.getAssignableUnits());
    }

    // Add AUs from blocks
    if (manifest.getBlocks() != null) {
      for (Block block : manifest.getBlocks()) {
        allAUs.addAll(getAUsFromBlock(block));
      }
    }

    return allAUs;
  }

  /**
   * Recursively extracts assignable units from a block and its nested blocks.
   *
   * @param block The block to extract AUs from
   * @return A list of assignable units from the block
   */
  private static List<AU> getAUsFromBlock(Block block) {
    List<AU> aus = new ArrayList<>();

    // Add AUs from this block
    if (block.getAssignableUnits() != null) {
      aus.addAll(block.getAssignableUnits());
    }

    // Recursively add AUs from nested blocks
    if (block.getNestedBlocks() != null) {
      for (Block nestedBlock : block.getNestedBlocks()) {
        aus.addAll(getAUsFromBlock(nestedBlock));
      }
    }

    return aus;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (!(o instanceof Cmi5Metadata that)) {
      return false;
    }

    return new EqualsBuilder()
        .appendSuper(super.equals(o))
        .append(getAssignableUnitIds(), that.getAssignableUnitIds())
        .append(getAssignableUnitUrls(), that.getAssignableUnitUrls())
        .append(getAuDetails(), that.getAuDetails())
        .append(getMasteryScores(), that.getMasteryScores())
        .append(getMoveOnCriteria(), that.getMoveOnCriteria())
        .append(getLaunchMethods(), that.getLaunchMethods())
        .append(getActivityTypes(), that.getActivityTypes())
        .append(getLaunchParameters(), that.getLaunchParameters())
        .append(getBlockIds(), that.getBlockIds())
        .append(getObjectiveIds(), that.getObjectiveIds())
        .isEquals();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder(17, 37)
        .appendSuper(super.hashCode())
        .append(getAssignableUnitIds())
        .append(getAssignableUnitUrls())
        .append(getAuDetails())
        .append(getMasteryScores())
        .append(getMoveOnCriteria())
        .append(getLaunchMethods())
        .append(getActivityTypes())
        .append(getLaunchParameters())
        .append(getBlockIds())
        .append(getObjectiveIds())
        .toHashCode();
  }

  public List<String> getAssignableUnitIds() {
    return List.copyOf(assignableUnitIds);
  }

  public List<String> getAssignableUnitUrls() {
    return List.copyOf(assignableUnitUrls);
  }

  public Map<String, Map<String, Object>> getAuDetails() {
    return Map.copyOf(auDetails);
  }

  public Map<String, Double> getMasteryScores() {
    return Map.copyOf(masteryScores);
  }

  public Map<String, String> getMoveOnCriteria() {
    return Map.copyOf(moveOnCriteria);
  }

  public Map<String, String> getLaunchMethods() {
    return Map.copyOf(launchMethods);
  }

  public Map<String, String> getActivityTypes() {
    return Map.copyOf(activityTypes);
  }

  public Map<String, String> getLaunchParameters() {
    return Map.copyOf(launchParameters);
  }

  public List<String> getBlockIds() {
    return List.copyOf(blockIds);
  }

  public List<String> getObjectiveIds() {
    return List.copyOf(objectiveIds);
  }
}
