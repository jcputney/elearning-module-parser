package dev.jcputney.elearning.parser.output.metadata.aicc;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Objective metadata derived from AICC .ort/.pre files, including association with assignable units
 * and measure requirements.
 */
@SuppressWarnings("ClassCanBeRecord")
public final class AiccObjectiveMetadata implements Serializable {

  @Serial
  private static final long serialVersionUID = 1L;

  /**
   * The unique identifier for the objective metadata.
   */
  private final String id;

  /**
   * A text description providing additional details about the objective metadata. This description
   * can be used to clarify or elaborate on the purpose or nature of the associated objective.
   */
  private final String description;

  /**
   * A list of assignable unit (AU) identifiers associated with the current objective metadata.
   * These identifiers represent distinct assignable units that are linked to the objective,
   * providing a way to establish relationships between the objective and specific components of a
   * learning or training module.
   */
  private final List<String> associatedAuIds;

  /**
   * Indicates whether the objective satisfaction is determined by a specific measure. This field
   * represents a boolean value that defines if the objective is considered satisfied based on its
   * associated performance measures or criteria.
   */
  private final Boolean satisfiedByMeasure;

  /**
   * Represents the minimum normalized measure associated with the objective. This value is used to
   * determine the threshold for satisfying the associated objective based on progress or
   * performance measures. A null value indicates that there is no minimum threshold requirement.
   */
  private final Double minNormalizedMeasure;

  /**
   * Represents the weight assigned to the progress measure for an objective within the AICC
   * metadata. This value is used to determine the significance of the progress measure when
   * evaluating the overall objective status or performance.
   * <p>
   * The value is immutable and is initialized upon the creation of an instance of
   * {@code AiccObjectiveMetadata}. A non-null value is expected to indicate a valid weight for the
   * progress measure.
   */
  private final Double progressMeasureWeight;

  /**
   * A map that represents the status information of objectives within an AICC (Aviation Industry
   * CBT Committee) context. Each key in the map corresponds to a specific objective identifier,
   * while the value represents the associated status as a string.
   */
  private final Map<String, String> statusMap;

  /**
   * Constructs an instance of AiccObjectiveMetadata with the specified details.
   *
   * @param id the unique identifier of the objective metadata
   * @param description a description of the objective metadata
   * @param associatedAuIds a list of associated AU (Assignable Unit) IDs; if null, an empty list is
   * assigned
   * @param satisfiedByMeasure indicates whether the objective is satisfied by measure; can be null
   * @param minNormalizedMeasure the minimum normalized measure for the objective; can be null
   * @param progressMeasureWeight the weight for the progress measure; can be null
   * @param statusMap a map representing status information for the objective; if null, an empty map
   * is assigned
   */
  public AiccObjectiveMetadata(String id, String description, List<String> associatedAuIds,
      Boolean satisfiedByMeasure, Double minNormalizedMeasure, Double progressMeasureWeight,
      Map<String, String> statusMap) {
    this.id = id;
    this.description = description;
    this.associatedAuIds = associatedAuIds == null ? List.of() : List.copyOf(associatedAuIds);
    this.satisfiedByMeasure = satisfiedByMeasure;
    this.minNormalizedMeasure = minNormalizedMeasure;
    this.progressMeasureWeight = progressMeasureWeight;
    this.statusMap = statusMap == null ? Map.of() : Map.copyOf(statusMap);
  }

  /**
   * Retrieves the unique identifier of the object.
   *
   * @return the unique identifier as a String
   */
  public String getId() {
    return this.id;
  }

  /**
   * Retrieves the description of the objective metadata.
   *
   * @return the description as a String
   */
  public String getDescription() {
    return this.description;
  }

  /**
   * Retrieves the list of associated AU (Assignable Unit) IDs for the objective metadata.
   *
   * @return a list of strings representing the associated AU IDs
   */
  public List<String> getAssociatedAuIds() {
    return this.associatedAuIds;
  }

  /**
   * Indicates whether the objective is satisfied by measure.
   *
   * @return true if the objective is satisfied by measure; false otherwise
   */
  public Boolean getSatisfiedByMeasure() {
    return this.satisfiedByMeasure;
  }

  /**
   * Retrieves the minimum normalized measure associated with the objective metadata.
   *
   * @return the minimum normalized measure as a Double
   */
  public Double getMinNormalizedMeasure() {
    return this.minNormalizedMeasure;
  }

  /**
   * Retrieves the progress measure weight associated with the objective metadata.
   *
   * @return the progress measure weight as a Double
   */
  public Double getProgressMeasureWeight() {
    return this.progressMeasureWeight;
  }

  /**
   * Retrieves the status mapping associated with the objective metadata.
   *
   * @return a map where the keys and values are strings representing the status information
   */
  public Map<String, String> getStatusMap() {
    return this.statusMap;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof AiccObjectiveMetadata that)) {
      return false;
    }
    return Objects.equals(this.id, that.id)
        && Objects.equals(this.description, that.description)
        && Objects.equals(this.associatedAuIds, that.associatedAuIds)
        && Objects.equals(this.satisfiedByMeasure, that.satisfiedByMeasure)
        && Objects.equals(this.minNormalizedMeasure, that.minNormalizedMeasure)
        && Objects.equals(this.progressMeasureWeight, that.progressMeasureWeight)
        && Objects.equals(this.statusMap, that.statusMap);
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.id, this.description, this.associatedAuIds, this.satisfiedByMeasure,
        this.minNormalizedMeasure, this.progressMeasureWeight, this.statusMap);
  }
}
