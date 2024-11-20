package dev.jcputney.elearning.parser.output.aicc;

import java.util.Optional;

/**
 * Represents description data parsed from an AICC .des file, including the course name and
 * description.
 */
public class DescriptionData {

  private final String courseName;
  private final String courseDescription;

  public DescriptionData(String courseName, String courseDescription) {
    this.courseName = courseName;
    this.courseDescription = courseDescription;
  }

  public Optional<String> getCourseName() {
    return Optional.ofNullable(courseName);
  }

  public Optional<String> getCourseDescription() {
    return Optional.ofNullable(courseDescription);
  }
}
