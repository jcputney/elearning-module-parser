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
import dev.jcputney.elearning.parser.input.scorm2004.SequencingUsageDetector;
import dev.jcputney.elearning.parser.input.scorm2004.SequencingUsageDetector.SequencingLevel;
import dev.jcputney.elearning.parser.input.scorm2004.ims.cp.Scorm2004Item;
import dev.jcputney.elearning.parser.input.scorm2004.ims.ss.objective.Scorm2004Objective;
import dev.jcputney.elearning.parser.input.scorm2004.ims.ss.objective.Scorm2004ObjectiveMapping;
import dev.jcputney.elearning.parser.input.scorm2004.ims.ss.sequencing.DeliveryControls;
import dev.jcputney.elearning.parser.input.scorm2004.ims.ss.sequencing.Sequencing;
import dev.jcputney.elearning.parser.output.metadata.BaseModuleMetadata;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
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

  /**
   * A mapping of activity identifiers to their associated delivery controls. This map uses a
   * LinkedHashMap to preserve the order of insertion, allowing for predictable iteration order.
   * <p>
   * The keys in this map represent unique identifiers for specific activities, while the values
   * correspond to {@code DeliveryControls} objects that define the delivery-related configurations
   * for those activities.
   */
  private final Map<String, DeliveryControls> activityDeliveryControls = new LinkedHashMap<>();

  /**
   * A set containing delivery control override keys. This collection is used to manage specific
   * delivery control configurations that need to deviate from standard settings. It ensures
   * insertion order is preserved and prevents duplicate entries.
   */
  private final Set<String> deliveryControlOverrides = new LinkedHashSet<>();

  /**
   * A collection that holds unique identifiers for global objectives. The identifiers are stored in
   * a LinkedHashSet to maintain insertion order while ensuring uniqueness.
   */
  private final Set<String> globalObjectiveIds = new LinkedHashSet<>();

  /**
   * A list that holds sequencing indicators represented as strings. This list is initialized as an
   * empty ArrayList and is intended to store indicators used for sequencing purposes within the
   * application. The contents of this list are immutable as it is declared final, which ensures
   * that the reference to the list cannot be reassigned after initialization.
   */
  private final List<String> sequencingIndicators = new ArrayList<>();

  /**
   * A map that stores completion thresholds. The outer map uses a string as a key to categorize
   * thresholds, while the inner map maps string keys to their respective threshold values, which
   * are represented as generic objects.
   * <p>
   * This structure allows for organizing and managing multiple thresholds under different
   * categories or contexts, providing flexibility in defining and retrieving these thresholds as
   * needed.
   */
  private final Map<String, Map<String, Object>> completionThresholds = new LinkedHashMap<>();

  /**
   * A map that associates specific action names (keys) with their corresponding time limit
   * descriptions or identifiers (values). The LinkedHashMap ensures that the order of insertion is
   * maintained when processing or iterating through the entries of the map. This is helpful when
   * the sequence of actions and their timing constraints need to be preserved for business logic or
   * execution policies.
   */
  private final Map<String, String> timeLimitActions = new LinkedHashMap<>();

  /**
   * A map that holds data retrieved from the LMS (Learning Management System). The map uses a
   * {@code String} as the key and a {@code String} as the value. Keys and values are ordered as
   * they are inserted, maintaining a predictable iteration order. This map is immutable once
   * initialized due to being declared as {@code final}.
   */
  private final Map<String, String> dataFromLms = new LinkedHashMap<>();

  /**
   * A mapping that determines the LMS (Learning Management System) UI elements to be hidden for
   * specified criteria. The key represents a criteria string, and the value is a list of
   * identifiers for UI elements that should be hidden under that criteria. The order of the
   * mappings is preserved as it uses a LinkedHashMap.
   */
  private final Map<String, List<String>> hideLmsUi = new LinkedHashMap<>();

  /**
   * A nested map structure that represents control modes with a specific hierarchy. The outer map
   * uses a string as the key to represent a top-level category or mode group. Each top-level key is
   * associated with another map (inner map), where: - The key of the inner map is a string
   * representing a specific sub-mode or sub-category. - The value of the inner map is a boolean
   * indicating the state or activation of that specific sub-mode.
   * <p>
   * This structure allows organizing control modes in a hierarchical way, enabling quick access and
   * evaluation of mode states.
   */
  private final Map<String, Map<String, Boolean>> controlModes = new LinkedHashMap<>();

  /**
   * Indicates whether sequencing is enabled or present. This variable is used as a flag to
   * determine if sequencing-related features or functionalities should be considered.
   */
  private boolean hasSequencing;

  /**
   * Represents the current sequencing level for a specific operation or process. The sequencing
   * level determines the degree or mode of sequencing applied. Default value is set to
   * SequencingLevel.NONE, indicating no sequencing.
   */
  private SequencingLevel sequencingLevel = SequencingLevel.NONE;

  /**
   * Constructs an instance of the Scorm2004Metadata class. This constructor initializes the
   * Scorm2004Metadata object by invoking the superclass's constructor and preparing it for further
   * operations.
   * <p>
   * This class is likely associated with handling metadata specific to the SCORM 2004 standard, a
   * set of technical standards for eLearning.
   */
  protected Scorm2004Metadata() {
    super();
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

    // Determine the specific edition type. If schemaversion is absent or ambiguous, fall back to
    // namespace-based detection (adlcp_v1p2 => 2nd Edition; adlcp_v1p3 => 3rd/4th, unknown which).
    ModuleEditionType editionType = ModuleEditionType.fromModuleType(ModuleType.SCORM_2004,
        schemaVersion);
    if (editionType == ModuleEditionType.SCORM_2004) {
      String adlcpNs = manifest.getAdlcpNamespaceUri();
      if (adlcpNs != null && adlcpNs
          .toLowerCase()
          .contains("adlcp_v1p2")) {
        editionType = ModuleEditionType.SCORM_2004_2ND_EDITION;
      } else {
        String schemaLoc = manifest.getSchemaLocation();
        if (schemaLoc != null && schemaLoc
            .toLowerCase()
            .contains("adlcp_v1p2")) {
          editionType = ModuleEditionType.SCORM_2004_2ND_EDITION;
        }
      }
    }

    var sequencingResult = SequencingUsageDetector.detect(manifest);

    Scorm2004Metadata metadata = new Scorm2004Metadata();
    metadata.manifest = manifest;
    metadata.moduleType = ModuleType.SCORM_2004;
    metadata.moduleEditionType = editionType;
    metadata.xapiEnabled = xapiEnabled;
    metadata.hasSequencing = sequencingResult.hasSequencing();
    metadata.sequencingLevel = sequencingResult.getLevel();

    // Extract SCORM 2004 item-level attributes (ADLCP/ADLNav/IMSSS highlights)
    metadata.extractScorm2004SpecificMetadata(manifest);

    // Add global objective IDs
    metadata.globalObjectiveIds.clear();
    metadata.globalObjectiveIds.addAll(metadata.collectGlobalObjectiveIds());

    // Add sequencing flag and indicators
    metadata.sequencingIndicators.clear();
    if (!sequencingResult
        .getIndicators()
        .isEmpty()) {
      metadata.sequencingIndicators.addAll(sequencingResult
          .getIndicators()
          .stream()
          .map(Enum::name)
          .sorted()
          .toList());
    }

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
    if (manifest == null) {
      return false;
    }

    return SequencingUsageDetector
        .detect(manifest)
        .hasSequencing();
  }

  /**
   * Retrieves the set of delivery control overrides. These overrides specify the activities or
   * conditions for which delivery control defaults are not applicable and are customized or
   * explicitly defined.
   *
   * @return A set of delivery control overrides.
   */
  public Set<String> getDeliveryControlOverrides() {
    return deliveryControlOverrides;
  }

  /**
   * Retrieves the set of global objective IDs from the manifest. A global objective ID is defined
   * by the presence of a targetObjectiveID in a mapInfo element.
   *
   * @return A set of global objective IDs.
   */
  @JsonIgnore
  public Set<String> getGlobalObjectiveIds() {
    return Collections.unmodifiableSet(globalObjectiveIds);
  }

  /**
   * Retrieves a list of sequencing indicators associated with the SCORM 2004 metadata. Sequencing
   * indicators are used to specify the sequencing behaviors or rules applied to activities.
   *
   * @return An unmodifiable list of strings representing the sequencing indicators.
   */
  public List<String> getSequencingIndicators() {
    return List.copyOf(sequencingIndicators);
  }

  /**
   * Retrieves the completion thresholds associated with the SCORM 2004 metadata. Completion
   * thresholds define the required conditions for an activity to reach a completed state, often
   * including minimum progress measures and progress weights.
   *
   * @return An unmodifiable map where keys represent activity identifiers, and values are maps
   * containing threshold details, such as minimum progress measures or other completion-related
   * attributes.
   */
  public Map<String, Map<String, Object>> getCompletionThresholds() {
    return Map.copyOf(completionThresholds);
  }

  /**
   * Retrieves the time limit actions associated with the SCORM 2004 metadata. Time limit actions
   * define specific behaviors or instructions to follow when time constraints are applied to
   * activities.
   *
   * @return An unmodifiable map where the keys represent activity identifiers, and the values
   * represent corresponding time limit actions.
   */
  public Map<String, String> getTimeLimitActions() {
    return Map.copyOf(timeLimitActions);
  }

  /**
   * Retrieves SCORM-based metadata in the form of data from LMS (Learning Management System). This
   * data is represented as a mapping, where the keys are string identifiers, and the values are
   * corresponding string entries provided by the LMS.
   *
   * @return An unmodifiable map containing key-value pairs of data from the LMS.
   */
  public Map<String, String> getDataFromLms() {
    return Map.copyOf(dataFromLms);
  }

  /**
   * Retrieves a mapping of LMS (Learning Management System) UI elements that are hidden. The map
   * keys represent element identifiers, and the values are lists of specific settings or related
   * identifiers associated with each UI element.
   *
   * @return An unmodifiable map where keys are string identifiers of LMS UI elements, and values
   * are lists of related settings or parameters associated with those elements.
   */
  public Map<String, List<String>> getHideLmsUi() {
    return Map.copyOf(hideLmsUi);
  }

  /**
   * Retrieves the control modes associated with the SCORM 2004 metadata. Control modes define the
   * allowed behaviors or constraints for activities within the SCORM 2004 content package based on
   * specific configurations.
   *
   * @return An unmodifiable map where the keys represent the identifiers of control mode
   * configurations, and the values are maps of settings represented as key-value pairs, where keys
   * are their attributes and values are booleans indicating their state.
   */
  public Map<String, Map<String, Boolean>> getControlModes() {
    return Map.copyOf(controlModes);
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
        .append(getSequencingLevel(), that.getSequencingLevel())
        .isEquals();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder(17, 37)
        .appendSuper(super.hashCode())
        .append(isHasSequencing())
        .append(getSequencingLevel())
        .toHashCode();
  }

  /**
   * Checks if sequencing is enabled.
   *
   * @return true if sequencing is enabled, false otherwise
   */
  public boolean isHasSequencing() {
    return this.hasSequencing;
  }

  /**
   * Retrieves the current sequencing level.
   *
   * @return the sequencing level of type SequencingLevel
   */
  public SequencingLevel getSequencingLevel() {
    return sequencingLevel;
  }

  /**
   * Retrieves an unmodifiable map containing the delivery controls for activities.
   *
   * @return an unmodifiable map where the keys are activity identifiers and the values are the
   * corresponding delivery controls
   */
  public Map<String, DeliveryControls> getActivityDeliveryControls() {
    return Collections.unmodifiableMap(activityDeliveryControls);
  }

  /**
   * Retrieves the set of activities that override the default delivery control settings.
   *
   * @return an unmodifiable Set of activity identifiers representing activities that have
   * customized delivery control defaults.
   */
  @JsonIgnore
  public Set<String> getActivitiesOverridingDeliveryControlDefaults() {
    return Collections.unmodifiableSet(deliveryControlOverrides);
  }

  /**
   * Determines if the delivery control defaults are overridden for a given activity.
   *
   * @param activityId The identifier of the activity to check.
   * @return true if the delivery control defaults are overridden for the specified activity, false
   * otherwise.
   */
  @JsonIgnore
  public boolean overridesDeliveryControlDefaults(String activityId) {
    return deliveryControlOverrides.contains(activityId);
  }

  /**
   * Extracts SCORM 2004 item-level attributes and surfaces them in simple metadata: -
   * completionThresholds (minProgressMeasure, progressWeight, completedByMeasure) -
   * timeLimitActions - dataFromLMS - hideLMSUI (from ADLNav presentation/navigationInterface) -
   * controlModes (IMSSS controlMode flags)
   */
  private void extractScorm2004SpecificMetadata(Scorm2004Manifest manifest) {
    activityDeliveryControls.clear();
    deliveryControlOverrides.clear();
    completionThresholds.clear();
    timeLimitActions.clear();
    dataFromLms.clear();
    hideLmsUi.clear();
    controlModes.clear();
    if (manifest.getOrganizations() == null || manifest
        .getOrganizations()
        .getDefault() == null || manifest
        .getOrganizations()
        .getDefault()
        .getItems() == null) {
      return;
    }

    var items = manifest
        .getOrganizations()
        .getDefault()
        .getItems();

    ItemMetadataAccumulator accumulator = new ItemMetadataAccumulator(this);
    accumulator.collect(items);
    accumulator.publish();
  }

  /**
   * Records the delivery controls for a specific SCORM item based on its ID and sequencing
   * configuration. This method resolves effective delivery controls, applies them to the activity,
   * and tracks overrides if applicable.
   *
   * @param itemId The unique identifier for the SCORM item. Must not be null or empty.
   * @param sequencing The sequencing configuration associated with the SCORM item. Used to resolve
   * delivery controls.
   */
  private void recordDeliveryControls(String itemId, Sequencing sequencing) {
    if (itemId == null || itemId.isEmpty()) {
      return;
    }
    DeliveryControls resolved = findDeliveryControls(sequencing);
    DeliveryControls effective = buildEffectiveDeliveryControls(resolved);
    activityDeliveryControls.put(itemId, effective);
    if (resolved != null) {
      deliveryControlOverrides.add(itemId);
    }
  }

  /**
   * Constructs an effective set of delivery controls by merging the provided resolved delivery
   * controls with defaults. If the resolved controls are null, a new instance of DeliveryControls
   * is returned with default values.
   *
   * @param resolved The resolved DeliveryControls object containing pre-configured settings. If
   * null, defaults will be used.
   * @return A DeliveryControls object representing the effective configuration, either merged with
   * the resolved settings or defaults if resolved is null.
   */
  private DeliveryControls buildEffectiveDeliveryControls(DeliveryControls resolved) {
    DeliveryControls effective = new DeliveryControls();
    if (resolved != null) {
      effective.setTracked(resolved.isTracked());
      effective.setCompletionSetByContent(resolved.isCompletionSetByContent());
      effective.setObjectiveSetByContent(resolved.isObjectiveSetByContent());
    }
    return effective;
  }

  /**
   * Finds the delivery controls for the given sequencing instance. If delivery controls are not set
   * in the provided sequencing object, it attempts to resolve delivery controls using the
   * sequencing's ID reference and the associated manifest's sequencing collection.
   *
   * @param sequencing The sequencing object containing or referring to delivery controls. If null,
   * the method immediately returns null.
   * @return The resolved DeliveryControls object, or null if it cannot be found or resolved.
   */
  private DeliveryControls findDeliveryControls(Sequencing sequencing) {
    if (sequencing == null) {
      return null;
    }
    DeliveryControls deliveryControls = sequencing.getDeliveryControls();
    if (deliveryControls == null && sequencing.getIdRef() != null && manifest != null
        && manifest.getSequencingCollection() != null) {
      deliveryControls = manifest
          .getSequencingCollection()
          .resolveDeliveryControlsById(sequencing.getIdRef());
    }
    return deliveryControls;
  }

  /**
   * Collects and retrieves a set of global objective IDs from the SCORM 2004 manifest. A global
   * objective ID is derived from the targetObjectiveID values present in the mapInfo elements
   * associated with objectives for each item in the manifest.
   * <p>
   * The method processes the manifest by: 1. Retrieving all organizations and their item
   * structures. 2. Extracting objectives and their mapping information. 3. Filtering and collecting
   * non-null, non-empty targetObjectiveIDs.
   *
   * @return A LinkedHashSet of global objective IDs, ensuring uniqueness while maintaining order.
   */
  private Set<String> collectGlobalObjectiveIds() {
    return manifest
        .getOrganizations()
        .getOrganizationList()
        .stream()
        .flatMap(org -> safeStream(org.getItems()))
        .flatMap(item -> safeStream(getObjectives(item)))
        .flatMap(obj -> safeStream(obj.getMapInfo()))
        .map(Scorm2004ObjectiveMapping::getTargetObjectiveID)
        .filter(id -> id != null && !id.isEmpty())
        .collect(Collectors.toCollection(LinkedHashSet::new));
  }

  /**
   * An internal utility class for accumulating and managing metadata related to SCORM 2004 items.
   * This class gathers, processes, and publishes metadata for inclusion in a SCORM context, such as
   * completion thresholds, time limit actions, data from LMS, hidden UI elements, and control
   * modes.
   * <p>
   * The role of this class is to traverse a hierarchical structure of SCORM 2004 items, collect
   * relevant metadata, and provide methods to propagate the aggregated data to the owning
   * {@code Scorm2004Metadata} instance.
   */
  private static final class ItemMetadataAccumulator {

    /**
     * Represents the owner of the metadata collected by the {@code ItemMetadataAccumulator}.
     * <p>
     * This field is a reference to a {@code Scorm2004Metadata} instance associated with the
     * accumulator, serving as the recipient of aggregated metadata. The collected metadata,
     * including completion thresholds, time limit actions, data from LMS, hidden LMS UI settings,
     * and control modes, is published to this owner.
     * <p>
     * The {@code owner} is intended to be immutable and is initialized at the time of the
     * accumulator's creation.
     */
    private final Scorm2004Metadata owner;

    /**
     * A map used to store completion threshold configurations for SCORM 2004 items.
     * <p>
     * The outer map uses string keys to represent unique identifiers or names for specific items or
     * categories. Each entry in the outer map contains another map as its value, which is used to
     * store specific completion threshold settings for the corresponding item or category.
     * <p>
     * The inner map uses string keys to represent the names of individual completion threshold
     * settings. The associated values in the inner map can be of varying types, allowing for
     * flexibility in representing different types of completion threshold data.
     * <p>
     * This variable plays a key role in aggregating and managing completion threshold
     * configurations during the processing of SCORM 2004 items, facilitating their use in
     * publishing and applying metadata configurations.
     */
    private final Map<String, Map<String, Object>> completionThresholds = new LinkedHashMap<>();

    /**
     * A map that holds key-value pairs representing time limit actions associated with SCORM
     * metadata. The keys in the map are strings identifying specific time limit action types, and
     * the values are strings describing the corresponding actions or settings.
     * <p>
     * This map is used to store and manage time-related behavior configurations for SCORM items,
     * enabling actions or settings to be associated with specific time thresholds or limits.
     * <p>
     * The entries in this map are maintained in insertion order.
     */
    private final Map<String, String> timeLimitActions = new LinkedHashMap<>();

    /**
     * Stores a map of key-value pairs representing data retrieved from the LMS (Learning Management
     * System). This collection is used to persist metadata associated with individual SCORM 2004
     * items and is part of the aggregation process for managing SCORM metadata.
     * <p>
     * The keys in the map correspond to specific metadata identifiers or names, while the values
     * represent their respective data or configuration values provided by the LMS.
     * <p>
     * This map ensures the preservation of LMS-provided metadata for subsequent processing and
     * publishing within the lifecycle of the SCORM item metadata aggregation.
     */
    private final Map<String, String> dataFromLms = new LinkedHashMap<>();

    /**
     * A map that holds information about UI components of an LMS (Learning Management System) that
     * should be hidden. The keys in the map represent specific UI element identifiers, while the
     * values are lists of strings detailing the conditions or contexts under which these UI
     * elements should be hidden.
     * <p>
     * This data is typically aggregated and used during the metadata accumulation process to
     * provide configuration details regarding the visibility of LMS UI elements for specific SCORM
     * 2004 items.
     */
    private final Map<String, List<String>> hideLmsUi = new LinkedHashMap<>();

    /**
     * Represents a collection of control modes for SCORM 2004 items, organized as a nested mapping
     * structure. The outer map holds string keys representing item identifiers, and each inner map
     * contains string keys representing control mode settings and their corresponding boolean
     * values indicating whether specific control modes are enabled or disabled for the item.
     * <p>
     * This field is used to aggregate and store control mode information during the processing of
     * SCORM 2004 items, with the data eventually being published or merged into the owning entity's
     * metadata structure.
     * <p>
     * The order of entries in the map is maintained as insertion order due to the use of a
     * LinkedHashMap.
     */
    private final Map<String, Map<String, Boolean>> controlModes = new LinkedHashMap<>();

    private ItemMetadataAccumulator(Scorm2004Metadata owner) {
      this.owner = owner;
    }

    /**
     * Constructs a map of string keys and boolean values based on the control mode configuration of
     * the specified Scorm2004Item instance.
     *
     * @param item the Scorm2004Item instance from which the control mode values are extracted
     * @return a map containing boolean values for various control mode settings, where the keys are
     * the names of the settings and the values indicate their states
     */
    private static Map<String, Boolean> getStringBooleanMap(Scorm2004Item item) {
      var mode = item
          .getSequencing()
          .getControlMode();
      Map<String, Boolean> values = new LinkedHashMap<>();
      values.put("choice", mode.isChoice());
      values.put("choiceExit", mode.isChoiceExit());
      values.put("flow", mode.isFlow());
      values.put("forwardOnly", mode.isForwardOnly());
      values.put("useCurrentAttemptObjectiveInfo", mode.isUseCurrentAttemptObjectiveInfo());
      values.put("useCurrentAttemptProgressInfo", mode.isUseCurrentAttemptProgressInfo());
      return values;
    }

    /**
     * Constructs a map of string keys and object values based on the completion threshold
     * configuration of the specified Scorm2004Item instance.
     *
     * @param item the Scorm2004Item instance from which the completion threshold values are
     * extracted
     * @return a map containing object values for various completion threshold settings, where the
     * keys are the names of the settings and the values are their associated data
     */
    private static Map<String, Object> getStringObjectMap(Scorm2004Item item) {
      var threshold = item.getCompletionThreshold();
      Map<String, Object> values = new LinkedHashMap<>();
      if (threshold.getMinProgressMeasure() != null && threshold
          .getMinProgressMeasure()
          .getValue() != null) {
        values.put("minProgressMeasure", threshold
            .getMinProgressMeasure()
            .getValue());
      }
      if (threshold.getProgressWeight() != null && threshold
          .getProgressWeight()
          .value() != null) {
        values.put("progressWeight", threshold
            .getProgressWeight()
            .value());
      }
      if (threshold.getCompletedByMeasure() != null) {
        values.put("completedByMeasure", threshold.getCompletedByMeasure());
      }
      return values;
    }

    /**
     * Traverses a hierarchical structure of Scorm2004Item objects and processes each item.
     *
     * @param rootItems a list of root Scorm2004Item objects to initiate the traversal; if null or
     * empty, the method exits without performing any operations
     */
    void collect(List<Scorm2004Item> rootItems) {
      if (rootItems == null || rootItems.isEmpty()) {
        return;
      }
      ArrayDeque<Scorm2004Item> stack = new ArrayDeque<>(rootItems);
      while (!stack.isEmpty()) {
        Scorm2004Item item = stack.pop();
        processItem(item);
        if (item.getItems() != null && !item
            .getItems()
            .isEmpty()) {
          List<Scorm2004Item> children = item.getItems();
          for (int i = children.size() - 1; i >= 0; i--) {
            stack.push(children.get(i));
          }
        }
      }
    }

    /**
     * Publishes the collected metadata from the accumulator to the associated owner instance.
     * <p>
     * This method updates the owner's internal data structures with the values aggregated in the
     * accumulator, merging the following metadata collections: - Completion thresholds - Time limit
     * actions - Data from LMS - Hide LMS UI configurations - Control modes
     * <p>
     * The operation overwrites existing entries in the owner's maps that have the same keys as
     * those in the accumulator.
     */
    void publish() {
      owner.completionThresholds.putAll(completionThresholds);
      owner.timeLimitActions.putAll(timeLimitActions);
      owner.dataFromLms.putAll(dataFromLms);
      owner.hideLmsUi.putAll(hideLmsUi);
      owner.controlModes.putAll(controlModes);
    }

    /**
     * Processes a single SCORM 2004 item, extracting and recording metadata such as completion
     * thresholds, time limit actions, data from LMS, hidden LMS UI elements, and control modes into
     * their respective collections.
     *
     * @param item the Scorm2004Item to be processed; if null, the method exits without processing
     */
    private void processItem(Scorm2004Item item) {
      if (item == null) {
        return;
      }
      String itemId = item.getIdentifier();
      owner.recordDeliveryControls(itemId, item.getSequencing());
      if (itemId == null || itemId.isEmpty()) {
        return;
      }

      if (item.getCompletionThreshold() != null) {
        Map<String, Object> values = getStringObjectMap(item);
        if (!values.isEmpty()) {
          completionThresholds.put(itemId, values);
        }
      }

      if (item.getTimeLimitAction() != null) {
        timeLimitActions.put(itemId, item
            .getTimeLimitAction()
            .name());
      }

      if (item.getDataFromLMS() != null && !item
          .getDataFromLMS()
          .isEmpty()) {
        dataFromLms.put(itemId, item.getDataFromLMS());
      }

      if (item.getPresentation() != null && item
          .getPresentation()
          .getNavigationInterface() != null && item
          .getPresentation()
          .getNavigationInterface()
          .getHideLMSUI() != null && !item
          .getPresentation()
          .getNavigationInterface()
          .getHideLMSUI()
          .isEmpty()) {
        List<String> hidden = item
            .getPresentation()
            .getNavigationInterface()
            .getHideLMSUI()
            .stream()
            .map(Enum::name)
            .toList();
        hideLmsUi.put(itemId, hidden);
      }

      if (item.getSequencing() != null && item
          .getSequencing()
          .getControlMode() != null) {
        Map<String, Boolean> values = getStringBooleanMap(item);
        controlModes.put(itemId, values);
      }
    }
  }
}
