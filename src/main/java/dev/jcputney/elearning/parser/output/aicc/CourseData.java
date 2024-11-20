package dev.jcputney.elearning.parser.output.aicc;

import java.util.Optional;

/**
 * Represents core course data parsed from an AICC .crs file.
 */
public class CourseData {

  private final String id;
  private final String version;
  private final String credit;
  private final String timeLimitAction;

  public CourseData(String id, String version, String credit, String timeLimitAction) {
    this.id = id;
    this.version = version;
    this.credit = credit;
    this.timeLimitAction = timeLimitAction;
  }

  public String getId() {
    return id;
  }

  public Optional<String> getVersion() {
    return Optional.ofNullable(version);
  }

  public Optional<String> getCredit() {
    return Optional.ofNullable(credit);
  }

  public Optional<String> getTimeLimitAction() {
    return Optional.ofNullable(timeLimitAction);
  }
}
