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
import java.util.stream.Collectors;

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
    List<AU> allAUs = getAllAssignableUnits(manifest);

    if (!allAUs.isEmpty()) {
      // Add comprehensive AU metadata
      for (AU au : allAUs) {
        String auId = au.getId();

        // Create detailed AU info
        metadata.auDetails.put(auId, getActivityUnitMetadata(au));

        // Extract mastery score
        if (au.getMasteryScore() != null) {
          metadata.masteryScores.put(auId, au
              .getMasteryScore()
              .value()
              .doubleValue());
        }

        // Extract moveOn criteria
        if (au.getMoveOn() != null) {
          metadata.moveOnCriteria.put(auId, au
              .getMoveOn()
              .name());
        }

        // Extract launch method
        if (au.getLaunchMethod() != null) {
          metadata.launchMethods.put(auId, au
              .getLaunchMethod()
              .name());
        }

        // Extract activity type
        if (au.getActivityType() != null) {
          metadata.activityTypes.put(auId, au.getActivityType());
        }

        // Extract launch parameters
        if (au.getLaunchParameters() != null) {
          metadata.launchParameters.put(auId, au.getLaunchParameters());
        }
      }

      metadata.assignableUnitIds.addAll(allAUs
          .stream()
          .map(AU::getId)
          .collect(Collectors.toCollection(ArrayList::new)));
      metadata.assignableUnitUrls.addAll(allAUs
          .stream()
          .map(AU::getUrl)
          .collect(Collectors.toCollection(ArrayList::new)));
    }

    // Add blocks if available
    List<Block> blocks = manifest.getBlocks();
    if (blocks != null && !blocks.isEmpty()) {
      // Add block IDs
      metadata.blockIds.addAll(blocks
          .stream()
          .map(Block::getId)
          .collect(Collectors.toCollection(ArrayList::new)));
    }

    // Add objectives if available
    Optional
        .ofNullable(manifest.getObjectives())
        .map(ObjectivesList::getObjectives)
        .filter(objectiveList -> !objectiveList.isEmpty())
        .ifPresent(objectiveList -> {
          // Add objective IDs
          metadata.objectiveIds.addAll(objectiveList
              .stream()
              .map(Objective::getId)
              .collect(Collectors.toCollection(ArrayList::new)));
        });

    return metadata;
  }

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
