package dev.jcputney.elearning.parser.output.cmi5;

/**
 * Represents an Assignable Unit (AU) in a cmi5 module.
 * <p>
 * An Assignable Unit is a learning object that can be launched and tracked in an LMS. It includes
 * metadata such as the title, launch URL, and completion conditions.
 * </p>
 */
public class AssignableUnit {

  private final String title;
  private final String launchUrl;
  private final String identifier;
  private final String completionStatus;
  private final String masteryScore;

  /**
   * Constructs an AssignableUnit with the specified properties.
   *
   * @param title The title of the assignable unit.
   * @param launchUrl The launch URL for the assignable unit.
   * @param identifier A unique identifier for the assignable unit.
   * @param completionStatus The completion status criteria for the assignable unit.
   * @param masteryScore The mastery score required to complete the assignable unit.
   */
  public AssignableUnit(String title, String launchUrl, String identifier, String completionStatus,
      String masteryScore) {
    this.title = title;
    this.launchUrl = launchUrl;
    this.identifier = identifier;
    this.completionStatus = completionStatus;
    this.masteryScore = masteryScore;
  }

  /**
   * Gets the title of the assignable unit.
   *
   * @return The title of the assignable unit.
   */
  public String getTitle() {
    return title;
  }

  /**
   * Gets the launch URL for the assignable unit.
   *
   * @return The launch URL of the assignable unit.
   */
  public String getLaunchUrl() {
    return launchUrl;
  }

  /**
   * Gets the unique identifier for the assignable unit.
   *
   * @return The identifier of the assignable unit.
   */
  public String getIdentifier() {
    return identifier;
  }

  /**
   * Gets the completion status criteria for the assignable unit.
   *
   * @return The completion status of the assignable unit.
   */
  public String getCompletionStatus() {
    return completionStatus;
  }

  /**
   * Gets the mastery score required for completing the assignable unit.
   *
   * @return The mastery score required for the assignable unit.
   */
  public String getMasteryScore() {
    return masteryScore;
  }

  @Override
  public String toString() {
    return "AssignableUnit{" +
        "title='" + title + '\'' +
        ", launchUrl='" + launchUrl + '\'' +
        ", identifier='" + identifier + '\'' +
        ", completionStatus='" + completionStatus + '\'' +
        ", masteryScore='" + masteryScore + '\'' +
        '}';
  }
}