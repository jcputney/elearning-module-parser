package dev.jcputney.elearning.parser.output.scorm2004;

import dev.jcputney.elearning.parser.output.ModuleMetadata;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Represents metadata for a SCORM 2004 eLearning module, including SCORM 2004-specific fields such
 * as sequencing information, mastery score, custom data, prerequisites, and additional metadata
 * from external manifests.
 * <p>
 * This class extends the base {@link ModuleMetadata} class to provide metadata that describes the
 * structure and rules specific to SCORM 2004 modules.
 * </p>
 */
public class Scorm2004Metadata extends ModuleMetadata {

  private final List<SequencingInfo> sequencingInfo;
  private final Double masteryScore;
  private final List<String> customData;
  private final String prerequisites;
  private final List<AdditionalMetadata> additionalMetadataList;

  private Scorm2004Metadata(Builder builder) {
    super(builder);
    this.sequencingInfo = builder.sequencingInfo;
    this.masteryScore = builder.masteryScore;
    this.customData = builder.customData;
    this.prerequisites = builder.prerequisites;
    this.additionalMetadataList = builder.additionalMetadataList;
  }

  /**
   * Gets the sequencing information associated with the SCORM 2004 module, if specified. Sequencing
   * rules define the navigation and completion behavior for content items.
   *
   * @return A list of {@link SequencingInfo} objects, or an empty list if none are present.
   */
  public List<SequencingInfo> getSequencingInfo() {
    return sequencingInfo;
  }

  /**
   * Gets the mastery score required to complete the module.
   *
   * @return An Optional containing the mastery score, or empty if not specified.
   */
  public Optional<Double> getMasteryScore() {
    return Optional.ofNullable(masteryScore);
  }

  /**
   * Gets the list of custom data entries associated with the module.
   *
   * @return A list of custom data strings, or an empty list if none are present.
   */
  public List<String> getCustomData() {
    return customData;
  }

  /**
   * Gets the prerequisites for the module, if specified.
   *
   * @return An Optional containing the prerequisites, or empty if not specified.
   */
  public Optional<String> getPrerequisites() {
    return Optional.ofNullable(prerequisites);
  }

  /**
   * Gets the additional metadata entries from external manifests.
   *
   * @return A list of {@link AdditionalMetadata} objects, or an empty list if none are present.
   */
  public List<AdditionalMetadata> getAdditionalMetadataList() {
    return additionalMetadataList;
  }

  /**
   * Gets a list of global objective IDs that are used by more than one SequencingInfo object.
   *
   * @return A list of global objective IDs.
   */
  public List<String> getGlobalObjectiveIds() {
    // global objective IDs are objective IDs that are used by more than one SequencingInfo object
    Map<String, Integer> objectiveIdCount = new HashMap<>();
    for (SequencingInfo info : sequencingInfo) {
      for (String objectiveId : info.getObjectiveIds()) {
        objectiveIdCount.put(objectiveId, objectiveIdCount.getOrDefault(objectiveId, 0) + 1);
      }
    }
    return objectiveIdCount.entrySet().stream()
        .filter(entry -> entry.getValue() > 1)
        .map(Map.Entry::getKey)
        .toList();
  }

  /**
   * Builder for constructing instances of {@link Scorm2004Metadata}.
   * <p>
   * This builder provides methods for setting SCORM 2004-specific fields, in addition to the core
   * fields inherited from {@link ModuleMetadata}.
   * </p>
   */
  public static class Builder extends ModuleMetadata.Builder<Builder> {

    private List<SequencingInfo> sequencingInfo = List.of(); // Default to an empty list
    private Double masteryScore;
    private List<String> customData = List.of(); // Default to an empty list
    private String prerequisites;
    private List<AdditionalMetadata> additionalMetadataList = List.of(); // Default to an empty list

    /**
     * Sets the sequencing information for the SCORM 2004 module.
     *
     * @param sequencingInfo A list of {@link SequencingInfo} objects defining navigation and
     * completion rules.
     * @return The builder instance.
     */
    public Builder sequencingInfo(List<SequencingInfo> sequencingInfo) {
      this.sequencingInfo = sequencingInfo != null ? sequencingInfo : List.of();
      return this;
    }

    /**
     * Sets the mastery score for the module.
     *
     * @param masteryScore The mastery score required to complete the module.
     * @return The builder instance.
     */
    public Builder masteryScore(Double masteryScore) {
      this.masteryScore = masteryScore;
      return this;
    }

    /**
     * Sets the list of custom data entries associated with the module.
     *
     * @param customData A list of custom data strings.
     * @return The builder instance.
     */
    public Builder customData(List<String> customData) {
      this.customData = customData != null ? customData : List.of();
      return this;
    }

    /**
     * Sets the prerequisites for the module.
     *
     * @param prerequisites The prerequisites for the module.
     * @return The builder instance.
     */
    public Builder prerequisites(String prerequisites) {
      this.prerequisites = prerequisites;
      return this;
    }

    /**
     * Sets the additional metadata entries from external manifests.
     *
     * @param additionalMetadataList A list of {@link AdditionalMetadata} objects.
     * @return The builder instance.
     */
    public Builder additionalMetadataList(List<AdditionalMetadata> additionalMetadataList) {
      this.additionalMetadataList =
          additionalMetadataList != null ? additionalMetadataList : List.of();
      return this;
    }

    /**
     * Builds and returns a {@link Scorm2004Metadata} instance with the specified properties.
     *
     * @return A new instance of Scorm2004Metadata.
     */
    @Override
    public Scorm2004Metadata build() {
      return new Scorm2004Metadata(this);
    }

    @Override
    protected Builder self() {
      return this;
    }
  }
}