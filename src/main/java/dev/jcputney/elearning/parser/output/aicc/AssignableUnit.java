package dev.jcputney.elearning.parser.output.aicc;

/**
 * Represents an Assignable Unit (AU) in an AICC module, containing details such as the unit ID,
 * file name, maximum score, and minimum score.
 */
public class AssignableUnit {

  private final String id;
  private final String fileName;
  private final String maxScore;
  private final String minScore;

  /**
   * Constructs an AssignableUnit with the specified details.
   *
   * @param id The unique identifier of the assignable unit.
   * @param fileName The file name or path for the content associated with this unit.
   * @param maxScore The maximum score achievable in this unit.
   * @param minScore The minimum score required in this unit.
   */
  public AssignableUnit(String id, String fileName, String maxScore, String minScore) {
    this.id = id;
    this.fileName = fileName;
    this.maxScore = maxScore;
    this.minScore = minScore;
  }

  /**
   * Gets the unique identifier of the assignable unit.
   *
   * @return The assignable unit ID.
   */
  public String getId() {
    return id;
  }

  /**
   * Gets the file name or path associated with this assignable unit.
   *
   * @return The file name or path.
   */
  public String getFileName() {
    return fileName;
  }

  /**
   * Gets the maximum score achievable in this assignable unit.
   *
   * @return The maximum score as a string.
   */
  public String getMaxScore() {
    return maxScore;
  }

  /**
   * Gets the minimum score required for this assignable unit.
   *
   * @return The minimum score as a string.
   */
  public String getMinScore() {
    return minScore;
  }
}