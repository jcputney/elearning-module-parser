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

  /**
   * A list of IDs representing assignable units within the Cmi5 metadata. Each assignable unit
   * corresponds to a specific learning activity or module that can be assigned and tracked within a
   * Cmi5-compliant system. This variable is immutable and is initialized to an empty list by
   * default.
   */
  private final List<String> assignableUnitIds = new ArrayList<>();

  /**
   * A list of URLs associated with assignable units in the CMI5 metadata. This field is used to
   * store the URLs of activity units (AUs) extracted from the CMI5 manifest or provided metadata.
   * It is populated with the URLs of assignable units during metadata extraction or processing.
   * <p>
   * The URLs correspond to the locations where the assignable units are hosted or can be accessed.
   * These URLs are typically used to identify and reference the specific AUs within the context of
   * the metadata.
   * <p>
   * This list is defined as final to ensure that the reference to the list does not change, but the
   * contents of the list may still be modified during the metadata extraction process.
   */
  private final List<String> assignableUnitUrls = new ArrayList<>();

  /**
   * A map holding the details of assignable units (AUs) in the metadata. Each entry in the map is a
   * key-value pair, where the key is a unique identifier for the assignable unit, and the value is
   * another map containing detailed metadata for that assignable unit. The metadata may include
   * details such as URLs, titles, descriptions, and other relevant attributes.
   */
  private final Map<String, Map<String, Object>> auDetails = new HashMap<>();

  /**
   * A mapping of assignable unit identifiers to their corresponding mastery scores. Each key in the
   * map represents the unique identifier of an assignable unit, and its associated value represents
   * the mastery score as a {@code double}.
   * <p>
   * This data structure is used to store the mastery thresholds required for successful completion
   * of the corresponding assignable units. It allows for quick retrieval of proficiency levels
   * required for each unit.
   */
  private final Map<String, Double> masteryScores = new HashMap<>();

  /**
   * Represents the mapping of move-on criteria for assignable units within the Cmi5 metadata. Each
   * entry in this map associates the unique identifier of an assignable unit with its corresponding
   * move-on criterion. The move-on criterion defines the conditions required for the learner to
   * advance within a course or module.
   * <p>
   * This field is immutable, ensuring the mapping remains constant after initialization.
   */
  private final Map<String, String> moveOnCriteria = new HashMap<>();

  /**
   * A map that holds the launch methods associated with the metadata. Each entry in the map
   * represents a key-value pair, where the key is a unique identifier and the value is the
   * corresponding launch method information.
   * <p>
   * This field is used to store and retrieve the launch method details tied to specific
   * identifiable elements within the metadata. Launch methods indicate the mechanisms or formats
   * through which the associated activity or resource can be initiated.
   * <p>
   * This map is immutable and should be accessed using the appropriate getter method.
   */
  private final Map<String, String> launchMethods = new HashMap<>();

  /**
   * A map representing the activity types associated with the metadata. Each activity type is
   * defined as a key-value pair, where the key is a unique identifier for the activity type, and
   * the value is a human-readable description of the activity type.
   * <p>
   * This field is intended to store metadata that categorizes and identifies specific types of
   * activities within the system, providing a means to distinguish between different activity types
   * and their purpose.
   * <p>
   * The map is initialized as an empty HashMap and can be populated during the metadata extraction
   * or creation process.
   */
  private final Map<String, String> activityTypes = new HashMap<>();

  /**
   * A map that stores launch parameters associated with the metadata. Each entry in the map
   * represents a key-value pair, where the key is a unique identifier for the launch parameter, and
   * the value is the corresponding information or value for that parameter.
   * <p>
   * This map is used to configure launch-related settings or attributes for assignable units or
   * activities within the context of the CMI5 metadata. It is initialized as an empty map and
   * populated as required during metadata processing or instantiation.
   * <p>
   * The keys in this map represent parameter names, while the values provide specific details or
   * configurations related to those parameters.
   */
  private final Map<String, String> launchParameters = new HashMap<>();

  /**
   * Holds a list of unique identifiers for blocks associated with the metadata. Block IDs are used
   * to identify and organize blocks defined within the metadata structure. This list is immutable
   * once initialized.
   */
  private final List<String> blockIds = new ArrayList<>();

  /**
   * A list of objective IDs associated with the metadata. Each objective ID represents a unique
   * identifier for an objective defined within the metadata.
   */
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
   * Adds metadata for a specific activity unit (AU) to the provided Cmi5Metadata object. This
   * includes details such as activity unit metadata, mastery score, move-on criteria, launch
   * method, activity type, and launch parameters, if available.
   *
   * @param metadata The Cmi5Metadata object where the activity unit metadata should be added.
   * @param au The activity unit (AU) containing metadata to be extracted and stored in the
   * Cmi5Metadata object.
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

  /**
   * Retrieves a list of assignable unit IDs associated with the metadata.
   *
   * @return A list of strings representing the IDs of the assignable units.
   */
  public List<String> getAssignableUnitIds() {
    return List.copyOf(assignableUnitIds);
  }

  /**
   * Retrieves a list of URLs associated with the assignable units in the metadata.
   *
   * @return An unmodifiable list of strings representing the URLs of the assignable units.
   */
  public List<String> getAssignableUnitUrls() {
    return List.copyOf(assignableUnitUrls);
  }

  /**
   * Retrieves the details of assignable units (AUs) as a map. Each assignable unit is represented
   * as a key-value pair, where the key is a unique identifier for the AU, and the value is another
   * map containing detailed metadata for that AU. The metadata may include information such as
   * URLs, titles, descriptions, and other relevant attributes.
   *
   * @return An unmodifiable map with assignable unit identifiers as keys and their corresponding
   * metadata as values. Each value is a map containing key-value pairs of the AU's metadata.
   */
  public Map<String, Map<String, Object>> getAuDetails() {
    return Map.copyOf(auDetails);
  }

  /**
   * Retrieves the mastery scores associated with the metadata. Each mastery score corresponds to an
   * assignable unit and is represented as a key-value pair, where the key is the unique identifier
   * of the assignable unit and the value is the mastery score as a double.
   *
   * @return An unmodifiable map containing the mastery scores for the assignable units. The keys
   * are the assignable unit identifiers, and the values are the respective mastery scores.
   */
  public Map<String, Double> getMasteryScores() {
    return Map.copyOf(masteryScores);
  }

  /**
   * Retrieves the move-on criteria associated with the metadata. Each move-on criterion corresponds
   * to an assignable unit and is represented as a key-value pair, where the key is the unique
   * identifier of the assignable unit and the value is the move-on criterion as a string.
   *
   * @return An unmodifiable map containing the move-on criteria for the assignable units. The keys
   * are the assignable unit identifiers, and the values are the respective move-on criteria.
   */
  public Map<String, String> getMoveOnCriteria() {
    return Map.copyOf(moveOnCriteria);
  }

  /**
   * Retrieves the launch methods associated with the metadata. Each launch method is represented as
   * a key-value pair, where the key is a unique identifier, and the value is the corresponding
   * launch method information.
   *
   * @return An unmodifiable map containing the launch methods. The keys are unique identifiers, and
   * the values are the corresponding launch method details.
   */
  public Map<String, String> getLaunchMethods() {
    return Map.copyOf(launchMethods);
  }

  /**
   * Retrieves the activity types associated with the metadata. Each activity type is represented as
   * a key-value pair, where the key is a unique identifier and the value is the corresponding
   * activity type description.
   *
   * @return An unmodifiable map containing the activity types. The keys are unique identifiers, and
   * the values are the respective activity type descriptions.
   */
  public Map<String, String> getActivityTypes() {
    return Map.copyOf(activityTypes);
  }

  /**
   * Retrieves the launch parameters associated with the metadata. Each launch parameter is
   * represented as a key-value pair, where the key is a unique identifier and the value is the
   * corresponding launch parameter information.
   *
   * @return An unmodifiable map containing the launch parameters. The keys are unique identifiers,
   * and the values are their corresponding launch parameter details.
   */
  public Map<String, String> getLaunchParameters() {
    return Map.copyOf(launchParameters);
  }

  /**
   * Retrieves the block IDs associated with the metadata. Block IDs represent unique identifiers
   * for the blocks defined within the metadata.
   *
   * @return An unmodifiable list of strings representing the block IDs.
   */
  public List<String> getBlockIds() {
    return List.copyOf(blockIds);
  }

  /**
   * Retrieves the objective IDs associated with the metadata. Objective IDs represent unique
   * identifiers for objectives defined within the metadata.
   *
   * @return An unmodifiable list of strings representing the objective IDs.
   */
  public List<String> getObjectiveIds() {
    return List.copyOf(objectiveIds);
  }
}
