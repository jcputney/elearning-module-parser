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

  private final Map<String, DeliveryControls> activityDeliveryControls = new LinkedHashMap<>();
  private final Set<String> deliveryControlOverrides = new LinkedHashSet<>();
  private final Set<String> globalObjectiveIds = new LinkedHashSet<>();
  private final List<String> sequencingIndicators = new ArrayList<>();
  private final Map<String, Map<String, Object>> completionThresholds = new LinkedHashMap<>();
  private final Map<String, String> timeLimitActions = new LinkedHashMap<>();
  private final Map<String, String> dataFromLms = new LinkedHashMap<>();
  private final Map<String, List<String>> hideLmsUi = new LinkedHashMap<>();
  private final Map<String, Map<String, Boolean>> controlModes = new LinkedHashMap<>();
  private boolean hasSequencing;
  private SequencingLevel sequencingLevel = SequencingLevel.NONE;

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

  public List<String> getSequencingIndicators() {
    return List.copyOf(sequencingIndicators);
  }

  public Map<String, Map<String, Object>> getCompletionThresholds() {
    return Map.copyOf(completionThresholds);
  }

  public Map<String, String> getTimeLimitActions() {
    return Map.copyOf(timeLimitActions);
  }

  public Map<String, String> getDataFromLms() {
    return Map.copyOf(dataFromLms);
  }

  public Map<String, List<String>> getHideLmsUi() {
    return Map.copyOf(hideLmsUi);
  }

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

  public boolean isHasSequencing() {
    return this.hasSequencing;
  }

  public SequencingLevel getSequencingLevel() {
    return sequencingLevel;
  }

  public Map<String, DeliveryControls> getActivityDeliveryControls() {
    return Collections.unmodifiableMap(activityDeliveryControls);
  }

  @JsonIgnore
  public Set<String> getActivitiesOverridingDeliveryControlDefaults() {
    return Collections.unmodifiableSet(deliveryControlOverrides);
  }

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

  private DeliveryControls buildEffectiveDeliveryControls(DeliveryControls resolved) {
    DeliveryControls effective = new DeliveryControls();
    if (resolved != null) {
      effective.setTracked(resolved.isTracked());
      effective.setCompletionSetByContent(resolved.isCompletionSetByContent());
      effective.setObjectiveSetByContent(resolved.isObjectiveSetByContent());
    }
    return effective;
  }

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

  private static final class ItemMetadataAccumulator {

    private final Scorm2004Metadata owner;
    private final Map<String, Map<String, Object>> completionThresholds = new LinkedHashMap<>();
    private final Map<String, String> timeLimitActions = new LinkedHashMap<>();
    private final Map<String, String> dataFromLms = new LinkedHashMap<>();
    private final Map<String, List<String>> hideLmsUi = new LinkedHashMap<>();
    private final Map<String, Map<String, Boolean>> controlModes = new LinkedHashMap<>();

    private ItemMetadataAccumulator(Scorm2004Metadata owner) {
      this.owner = owner;
    }

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

    void publish() {
      owner.completionThresholds.putAll(completionThresholds);
      owner.timeLimitActions.putAll(timeLimitActions);
      owner.dataFromLms.putAll(dataFromLms);
      owner.hideLmsUi.putAll(hideLmsUi);
      owner.controlModes.putAll(controlModes);
    }

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
            .collect(Collectors.toList());
        hideLmsUi.put(itemId, hidden);
      }

      if (item.getSequencing() != null && item
          .getSequencing()
          .getControlMode() != null) {
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
        controlModes.put(itemId, values);
      }
    }
  }
}
