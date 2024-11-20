package dev.jcputney.elearning.parser.output.aicc;

import dev.jcputney.elearning.parser.output.ModuleMetadata;
import java.util.List;
import java.util.Optional;

/**
 * Represents metadata for an AICC eLearning module, including AICC-specific fields such as course
 * structure, assignable units, prerequisites, objectives, credit type, time limit actions, and
 * objective relationships.
 * <p>
 * This class extends the base {@link ModuleMetadata} class to provide metadata specific to AICC
 * modules, enabling structured storage of AICC format details.
 * </p>
 */
public class AiccMetadata extends ModuleMetadata {

  private final CourseStructureData courseStructure;
  private final List<AssignableUnit> assignableUnits;
  private final String prerequisites;
  private final List<String> objectives;
  private final String credit;
  private final String timeLimitAction;
  private final List<ObjectiveRelationship> objectiveRelationships;

  /**
   * Constructs an AiccMetadata object using the provided Builder.
   *
   * @param builder The builder used to construct an AiccMetadata instance.
   */
  private AiccMetadata(Builder builder) {
    super(builder);
    this.courseStructure = builder.courseStructure;
    this.assignableUnits = builder.assignableUnits;
    this.prerequisites = builder.prerequisites;
    this.objectives = builder.objectives;
    this.credit = builder.credit;
    this.timeLimitAction = builder.timeLimitAction;
    this.objectiveRelationships = builder.objectiveRelationships;
  }

  /**
   * Retrieves the course structure data for the AICC module.
   *
   * @return The course structure data for the AICC module.
   */
  public CourseStructureData getCourseStructure() {
    return courseStructure;
  }

  /**
   * Retrieves the list of assignable units for the AICC module.
   *
   * @return A list of {@link AssignableUnit} objects, or an empty list if none are present.
   */
  public List<AssignableUnit> getAssignableUnits() {
    return assignableUnits;
  }

  /**
   * Retrieves prerequisites for the AICC module, if specified.
   *
   * @return An Optional containing prerequisites as a string, or empty if not specified.
   */
  public Optional<String> getPrerequisites() {
    return Optional.ofNullable(prerequisites);
  }

  /**
   * Retrieves learning objectives for the AICC module.
   *
   * @return A list of learning objectives, or an empty list if none are present.
   */
  public List<String> getObjectives() {
    return objectives;
  }

  /**
   * Retrieves the credit type for the AICC module.
   *
   * @return An Optional containing the credit type, or empty if not specified.
   */
  public Optional<String> getCredit() {
    return Optional.ofNullable(credit);
  }

  /**
   * Retrieves the time limit action for the AICC module, if specified.
   *
   * @return An Optional containing the time limit action, or empty if not specified.
   */
  public Optional<String> getTimeLimitAction() {
    return Optional.ofNullable(timeLimitAction);
  }

  /**
   * Retrieves the list of objective relationships within the AICC module.
   *
   * @return A list of {@link ObjectiveRelationship} objects representing relationships between
   * objectives.
   */
  public List<ObjectiveRelationship> getObjectiveRelationships() {
    return objectiveRelationships;
  }

  /**
   * Builder for constructing instances of {@link AiccMetadata}.
   * <p>
   * This builder provides methods for setting AICC-specific fields, in addition to the core fields
   * inherited from {@link ModuleMetadata}.
   * </p>
   */
  public static class Builder extends ModuleMetadata.Builder<Builder> {

    private CourseStructureData courseStructure;
    private List<AssignableUnit> assignableUnits = List.of();
    private String prerequisites;
    private List<String> objectives = List.of();
    private String credit;
    private String timeLimitAction;
    private List<ObjectiveRelationship> objectiveRelationships = List.of();

    public Builder courseStructure(CourseStructureData courseStructure) {
      this.courseStructure = courseStructure;
      return this;
    }

    public Builder assignableUnits(List<AssignableUnit> assignableUnits) {
      this.assignableUnits = assignableUnits != null ? assignableUnits : List.of();
      return this;
    }

    public Builder prerequisites(String prerequisites) {
      this.prerequisites = prerequisites;
      return this;
    }

    public Builder objectives(List<String> objectives) {
      this.objectives = objectives != null ? objectives : List.of();
      return this;
    }

    public Builder credit(String credit) {
      this.credit = credit;
      return this;
    }

    public Builder timeLimitAction(String timeLimitAction) {
      this.timeLimitAction = timeLimitAction;
      return this;
    }

    public Builder objectiveRelationships(List<ObjectiveRelationship> objectiveRelationships) {
      this.objectiveRelationships =
          objectiveRelationships != null ? objectiveRelationships : List.of();
      return this;
    }

    @Override
    public AiccMetadata build() {
      return new AiccMetadata(this);
    }

    @Override
    protected Builder self() {
      return this;
    }
  }
}