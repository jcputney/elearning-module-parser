package dev.jcputney.elearning.parser.output.aicc;

/**
 * Represents a relationship between two "objectives" within an AICC module. Stores information
 * about the source "objective", the target objective, and the type of relationship (e.g.,
 * prerequisite, parent-child).
 */
public class ObjectiveRelationship {

  private final String sourceObjective;
  private final String targetObjective;
  private final String relationshipType;

  /**
   * Constructs an ObjectiveRelationship with the specified details.
   *
   * @param sourceObjective The identifier of the source "objective".
   * @param targetObjective The identifier of the target "objective".
   * @param relationshipType The type of relationship (e.g., "prerequisite").
   */
  public ObjectiveRelationship(String sourceObjective, String targetObjective,
      String relationshipType) {
    this.sourceObjective = sourceObjective;
    this.targetObjective = targetObjective;
    this.relationshipType = relationshipType;
  }

  public String getSourceObjective() {
    return sourceObjective;
  }

  public String getTargetObjective() {
    return targetObjective;
  }

  public String getRelationshipType() {
    return relationshipType;
  }
}