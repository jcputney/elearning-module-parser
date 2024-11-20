package dev.jcputney.elearning.parser.output.aicc;

import java.util.Optional;

/**
 * Represents prerequisite data parsed from an AICC .pre file.
 */
public class PrerequisiteData {

  private final String prerequisites;

  public PrerequisiteData(String prerequisites) {
    this.prerequisites = prerequisites;
  }

  public Optional<String> getPrerequisites() {
    return Optional.ofNullable(prerequisites);
  }
}
