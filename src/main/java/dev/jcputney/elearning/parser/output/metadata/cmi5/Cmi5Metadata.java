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
import dev.jcputney.elearning.parser.output.metadata.SimpleMetadata;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
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
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
@EqualsAndHashCode(doNotUseGetters = true, callSuper = true)
public class Cmi5Metadata extends BaseModuleMetadata<Cmi5Manifest> {

  /**
   * Creates a new Cmi5Metadata instance with standard cmi5 metadata components.
   *
   * @param manifest The cmi5 manifest.
   * @param xapiEnabled Whether xAPI is enabled.
   * @return A new Cmi5Metadata instance.
   */
  public static Cmi5Metadata create(Cmi5Manifest manifest, boolean xapiEnabled) {
    Cmi5Metadata metadata = Cmi5Metadata
        .builder()
        .manifest(manifest)
        .moduleType(ModuleType.CMI5)
        .moduleEditionType(ModuleEditionType.CMI5)
        .xapiEnabled(xapiEnabled)
        .build();

    // Add cmi5 specific metadata
    SimpleMetadata cmi5Metadata = metadata.getSimpleMetadata(manifest);

    // Extract all AUs (from root level and blocks)
    List<AU> allAUs = getAllAssignableUnits(manifest);

    if (!allAUs.isEmpty()) {
      // Add comprehensive AU metadata
      Map<String, Map<String, Object>> auDetails = new HashMap<>();
      Map<String, Double> masteryScores = new HashMap<>();
      Map<String, String> moveOnCriteria = new HashMap<>();
      Map<String, String> launchMethods = new HashMap<>();
      Map<String, String> activityTypes = new HashMap<>();
      Map<String, String> launchParameters = new HashMap<>();

      for (AU au : allAUs) {
        String auId = au.getId();

        // Create detailed AU info
        auDetails.put(auId, getActivityUnitMetadata(au));

        // Extract mastery score
        if (au.getMasteryScore() != null) {
          masteryScores.put(auId, au
              .getMasteryScore()
              .getValue()
              .doubleValue());
        }

        // Extract moveOn criteria
        if (au.getMoveOn() != null) {
          moveOnCriteria.put(auId, au
              .getMoveOn()
              .name());
        }

        // Extract launch method
        if (au.getLaunchMethod() != null) {
          launchMethods.put(auId, au
              .getLaunchMethod()
              .name());
        }

        // Extract activity type
        if (au.getActivityType() != null) {
          activityTypes.put(auId, au.getActivityType());
        }

        // Extract launch parameters
        if (au.getLaunchParameters() != null) {
          launchParameters.put(auId, au.getLaunchParameters());
        }
      }

      // Add all extracted data to metadata
      cmi5Metadata.addMetadata("cmi5.auDetails", auDetails);
      if (!masteryScores.isEmpty()) {
        cmi5Metadata.addMetadata("cmi5.masteryScores", masteryScores);
      }
      if (!moveOnCriteria.isEmpty()) {
        cmi5Metadata.addMetadata("cmi5.moveOnCriteria", moveOnCriteria);
      }
      if (!launchMethods.isEmpty()) {
        cmi5Metadata.addMetadata("cmi5.launchMethods", launchMethods);
      }
      if (!activityTypes.isEmpty()) {
        cmi5Metadata.addMetadata("cmi5.activityTypes", activityTypes);
      }
      if (!launchParameters.isEmpty()) {
        cmi5Metadata.addMetadata("cmi5.launchParameters", launchParameters);
      }

      // Keep legacy fields for backward compatibility
      List<String> assignableUnitIds = allAUs
          .stream()
          .map(AU::getId)
          .toList();
      List<String> assignableUnitUrls = allAUs
          .stream()
          .map(AU::getUrl)
          .toList();
      cmi5Metadata.addMetadata("assignableUnitIds", assignableUnitIds);
      cmi5Metadata.addMetadata("assignableUnitUrls", assignableUnitUrls);
    }

    // Add blocks if available
    List<Block> blocks = manifest.getBlocks();
    if (blocks != null && !blocks.isEmpty()) {
      // Add block IDs
      List<String> blockIds = blocks
          .stream()
          .map(Block::getId)
          .toList();
      cmi5Metadata.addMetadata("blockIds", blockIds);
    }

    // Add objectives if available
    Optional
        .ofNullable(manifest.getObjectives())
        .map(ObjectivesList::getObjectives)
        .filter(objectiveList -> !objectiveList.isEmpty())
        .ifPresent(objectiveList -> {
          // Add objective IDs
          List<String> objectiveIds = objectiveList
              .stream()
              .map(Objective::getId)
              .toList();
          cmi5Metadata.addMetadata("objectiveIds", objectiveIds);
        });

    // Add the cmi5 metadata component to the composite
    metadata.addMetadataComponent(cmi5Metadata);

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
}
