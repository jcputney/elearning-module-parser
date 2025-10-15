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

package dev.jcputney.elearning.parser.input.aicc;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import java.util.Map;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * Represents the AICC course information.
 *
 * <p>This class contains details about the course, including its creator, ID, title, system,
 * level, maximum fields, total AUs, total blocks, version, and course behavior.</p>
 *
 * <p>It also provides a method to retrieve the course description.</p>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonFormat(with = JsonFormat.Feature.ACCEPT_CASE_INSENSITIVE_PROPERTIES)
public final class AiccCourse implements Serializable {

  /**
   * Course information for the AICC manifest.
   */
  @JsonProperty(value = "Course", required = true)
  private Course course;

  /**
   * Course behavior information for the AICC manifest.
   */
  @JsonProperty(value = "Course_Behavior", required = true)
  private CourseBehavior courseBehavior;

  /**
   * Course description information for the AICC manifest.
   */
  @JsonProperty(value = "Course_Description", required = true)
  private Map<String, String> courseDescription;

  /**
   * Course description information for the AICC manifest.
   *
   * @return the course description, or null if not available
   */
  @JsonIgnore
  public String getCourseDescription() {
    if (courseDescription == null || courseDescription.isEmpty()) {
      return null;
    }

    return courseDescription
        .entrySet()
        .stream()
        .findFirst()
        .map(Map.Entry::getKey)
        .orElse(null);
  }

  /**
   * Sets the course description information for the AICC manifest.
   *
   * @param courseDescription a map representing course description details, where the key is a
   * language code and the value is the description in that language
   */
  public void setCourseDescription(Map<String, String> courseDescription) {
    this.courseDescription = courseDescription;
  }

  /**
   * Retrieves the course information associated with this instance.
   *
   * @return the course object containing details such as course ID, title, creator, system, and
   * other related fields
   */
  public Course getCourse() {
    return this.course;
  }

  /**
   * Updates the course information associated with this instance.
   *
   * @param course the course object containing details such as course ID, title, creator, system,
   * and other related fields
   */
  public void setCourse(Course course) {
    this.course = course;
  }

  /**
   * Retrieves the CourseBehavior object associated with this instance.
   *
   * @return the CourseBehavior object, which encapsulates the behavior properties such as mastery
   * score, maximum allowable time, and time limit actions, or null if not assigned.
   */
  public CourseBehavior getCourseBehavior() {
    return this.courseBehavior;
  }

  /**
   * Sets the course behavior properties for the AICC course.
   *
   * @param courseBehavior the CourseBehavior object encapsulating behavior properties such as
   * mastery score, maximum allowable time, and time limit actions
   */
  public void setCourseBehavior(CourseBehavior courseBehavior) {
    this.courseBehavior = courseBehavior;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (!(o instanceof AiccCourse that)) {
      return false;
    }

    return new EqualsBuilder()
        .append(getCourse(), that.getCourse())
        .append(getCourseBehavior(), that.getCourseBehavior())
        .append(getCourseDescription(), that.getCourseDescription())
        .isEquals();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder(17, 37)
        .append(getCourse())
        .append(getCourseBehavior())
        .append(getCourseDescription())
        .toHashCode();
  }

  /**
   * Represents a course with various properties such as creator, ID, title, system information, and
   * other specific metadata related to the course. This class is primarily designed for
   * serialization or deserialization using JSON.
   *
   * The class utilizes Jackson annotations to ensure compatibility with JSON processing, such as
   * case insensitivity and ignoring unknown properties. Implements {@code Serializable} for
   * possible persistence or transmission scenarios.
   *
   * The {@code Course} class provides getters and setters for all of its attributes, enabling
   * manipulation and retrieval of metadata values associated with a course.
   */
  @JsonIgnoreProperties(ignoreUnknown = true)
  @JsonFormat(with = JsonFormat.Feature.ACCEPT_CASE_INSENSITIVE_PROPERTIES)
  public static class Course implements Serializable {

    /**
     * The course creator.
     */
    @JsonProperty(value = "Course_Creator", required = true)
    private String courseCreator;

    /**
     * The course ID.
     */
    @JsonProperty(value = "Course_ID", required = true)
    private String courseId;

    /**
     * The course title.
     */
    @JsonProperty(value = "Course_Title", required = true)
    private String courseTitle;

    /**
     * The course system.
     */
    @JsonProperty(value = "Course_System", required = true)
    private String courseSystem;

    /**
     * The course level.
     */
    @JsonProperty(value = "Level", required = true)
    private String level;

    /**
     * The maximum fields CST.
     */
    @JsonProperty(value = "Max_Fields_CST", required = true)
    private String maxFieldsCst;

    /**
     * The maximum fields ORT.
     */
    @JsonProperty(value = "Max_Fields_ORT")
    private String maxFieldsOrt;

    /**
     * The total AUs.
     */
    @JsonProperty(value = "Total_AUs", required = true)
    private String totalAus;

    /**
     * The total blocks.
     */
    @JsonProperty(value = "Total_Blocks", required = true)
    private String totalBlocks;

    /**
     * The version of the course.
     */
    @JsonProperty(value = "Version", required = true)
    private String version;

    /**
     * The total complex objects.
     */
    @JsonProperty(value = "Total_Complex_Obj")
    private String totalComplexObj;

    /**
     * The total objectives.
     */
    @JsonProperty(value = "Total_Objectives")
    private String totalObjectives;

    /**
     * Retrieves the creator of the course.
     *
     * @return the name of the course creator
     */
    public String getCourseCreator() {
      return this.courseCreator;
    }

    /**
     * Sets the creator of the course.
     *
     * @param courseCreator the name of the person or entity who created the course
     */
    public void setCourseCreator(String courseCreator) {
      this.courseCreator = courseCreator;
    }

    /**
     * Retrieves the course ID.
     *
     * @return the unique identifier of the course
     */
    public String getCourseId() {
      return this.courseId;
    }

    /**
     * Sets the unique identifier for the course.
     *
     * @param courseId the unique identifier to assign to the course
     */
    public void setCourseId(String courseId) {
      this.courseId = courseId;
    }

    /**
     * Retrieves the title of the course.
     *
     * @return the title of the course
     */
    public String getCourseTitle() {
      return this.courseTitle;
    }

    /**
     * Sets the title of the course.
     *
     * @param courseTitle the title to assign to the course
     */
    public void setCourseTitle(String courseTitle) {
      this.courseTitle = courseTitle;
    }

    /**
     * Retrieves the course system associated with the course.
     *
     * @return the system information of the course
     */
    public String getCourseSystem() {
      return this.courseSystem;
    }

    /**
     * Sets the course system associated with the course.
     *
     * @param courseSystem the system information to assign to the course
     */
    public void setCourseSystem(String courseSystem) {
      this.courseSystem = courseSystem;
    }

    /**
     * Retrieves the level of the course.
     *
     * @return the current level of the course
     */
    public String getLevel() {
      return this.level;
    }

    /**
     * Sets the level of the course.
     *
     * @param level the level to assign to the course
     */
    public void setLevel(String level) {
      this.level = level;
    }

    /**
     * Retrieves the maximum fields constant for the course.
     *
     * @return the maximum fields constant as a String
     */
    public String getMaxFieldsCst() {
      return this.maxFieldsCst;
    }

    /**
     * Sets the maximum fields constant for the course.
     *
     * @param maxFieldsCst the maximum fields constant to be set
     */
    public void setMaxFieldsCst(String maxFieldsCst) {
      this.maxFieldsCst = maxFieldsCst;
    }

    /**
     * Retrieves the maximum fields ort value for the course.
     *
     * @return the maximum fields ort as a String
     */
    public String getMaxFieldsOrt() {
      return this.maxFieldsOrt;
    }

    /**
     * Sets the maximum fields ort value for the course.
     *
     * @param maxFieldsOrt the maximum fields ort value to be set
     */
    public void setMaxFieldsOrt(String maxFieldsOrt) {
      this.maxFieldsOrt = maxFieldsOrt;
    }

    /**
     * Retrieves the total "Aus" value associated with the course.
     *
     * @return the total "Aus" value as a String
     */
    public String getTotalAus() {
      return this.totalAus;
    }

    /**
     * Sets the total "Aus" value for the course.
     *
     * @param totalAus the total "Aus" value to be assigned to the course
     */
    public void setTotalAus(String totalAus) {
      this.totalAus = totalAus;
    }

    /**
     * Retrieves the total blocks associated with the course.
     *
     * @return the total blocks as a String
     */
    public String getTotalBlocks() {
      return this.totalBlocks;
    }

    /**
     * Sets the total number of blocks associated with the course.
     *
     * @param totalBlocks the total number of blocks to be assigned to the course
     */
    public void setTotalBlocks(String totalBlocks) {
      this.totalBlocks = totalBlocks;
    }

    /**
     * Retrieves the version of the course.
     *
     * @return the version of the course as a String
     */
    public String getVersion() {
      return this.version;
    }

    /**
     * Sets the version of the course.
     *
     * @param version the version to assign to the course
     */
    public void setVersion(String version) {
      this.version = version;
    }

    /**
     * Retrieves the total complex object value for the course.
     *
     * @return the total complex object as a String
     */
    public String getTotalComplexObj() {
      return this.totalComplexObj;
    }

    /**
     * Sets the total complex object value for the course.
     *
     * @param totalComplexObj the total complex object to assign to the course
     */
    public void setTotalComplexObj(String totalComplexObj) {
      this.totalComplexObj = totalComplexObj;
    }

    /**
     * Retrieves the total objectives associated with the course.
     *
     * @return the total objectives as a String
     */
    public String getTotalObjectives() {
      return this.totalObjectives;
    }

    /**
     * Sets the total number of objectives associated with the course.
     *
     * @param totalObjectives the total objectives to assign to the course
     */
    public void setTotalObjectives(String totalObjectives) {
      this.totalObjectives = totalObjectives;
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) {
        return true;
      }

      if (!(o instanceof Course course)) {
        return false;
      }

      return new EqualsBuilder()
          .append(getCourseCreator(), course.getCourseCreator())
          .append(getCourseId(), course.getCourseId())
          .append(getCourseTitle(), course.getCourseTitle())
          .append(getCourseSystem(), course.getCourseSystem())
          .append(getLevel(), course.getLevel())
          .append(getMaxFieldsCst(), course.getMaxFieldsCst())
          .append(getMaxFieldsOrt(), course.getMaxFieldsOrt())
          .append(getTotalAus(), course.getTotalAus())
          .append(getTotalBlocks(), course.getTotalBlocks())
          .append(getVersion(), course.getVersion())
          .append(getTotalComplexObj(), course.getTotalComplexObj())
          .append(getTotalObjectives(), course.getTotalObjectives())
          .isEquals();
    }

    @Override
    public int hashCode() {
      return new HashCodeBuilder(17, 37)
          .append(getCourseCreator())
          .append(getCourseId())
          .append(getCourseTitle())
          .append(getCourseSystem())
          .append(getLevel())
          .append(getMaxFieldsCst())
          .append(getMaxFieldsOrt())
          .append(getTotalAus())
          .append(getTotalBlocks())
          .append(getVersion())
          .append(getTotalComplexObj())
          .append(getTotalObjectives())
          .toHashCode();
    }
  }

  /**
   * Represents the behavior properties of a course in an AICC module.
   * <p>
   * This class is designed to capture and manage configurable behavior properties such as maximum
   * normal value, mastery score, maximum time allowed, and time limit action. It supports
   * case-insensitive property mapping for JSON deserialization.
   * <p>
   * This class implements {@link Serializable}, allowing its instances to be serialized and
   * deserialized as part of larger processes.
   * <p>
   * Properties: - maxNormal: Represents the maximum normal value for the course. - masteryScore:
   * Indicates the score required to achieve mastery for the course. - maxTimeAllowed: Specifies the
   * maximum allowable time for the course. - timeLimitAction: Defines the actions taken when the
   * time limit is exceeded.
   */
  @JsonIgnoreProperties(ignoreUnknown = true)
  @JsonFormat(with = JsonFormat.Feature.ACCEPT_CASE_INSENSITIVE_PROPERTIES)
  public static class CourseBehavior implements Serializable {

    /**
     * The maximum normal value.
     */
    @JsonProperty(value = "Max_Normal", required = true)
    private String maxNormal;

    /**
     * Represents the score required to achieve mastery for the course.
     * <p>
     * This field is used to define the threshold score that determines when a learner has
     * successfully mastered the course material. It is mapped to the "Mastery_Score" JSON property
     * for serialization and deserialization, ensuring compatibility with external systems following
     * the defined schema.
     */
    @JsonProperty(value = "Mastery_Score")
    private String masteryScore;

    /**
     * Specifies the maximum amount of time allowed for a course or learning module.
     * <p>
     * This field represents a time limit set for the course, if applicable, and is typically
     * expressed in a specific format (e.g., HH:MM:SS). It is annotated to map to the JSON property
     * with the name "Max_Time_Allowed". The value of this field is used to control the permissible
     * duration a learner has to complete the course or module.
     */
    @JsonProperty(value = "Max_Time_Allowed")
    private String maxTimeAllowed;

    /**
     * Defines the action to be taken when the time limit for a course is exceeded.
     * <p>
     * This field is mapped to the JSON property "Time_Limit_Action" and determines how the course
     * behaves when the maximum allowed time is surpassed. The value of this property typically
     * indicates a specific behavior, such as suspending or terminating the course session, or
     * providing a warning to the user.
     */
    @JsonProperty(value = "Time_Limit_Action")
    private String timeLimitAction;

    /**
     * Retrieves the maximum normal value associated with the instance.
     *
     * @return the value of the maxNormal field, which represents the maximum normal value.
     */
    public String getMaxNormal() {
      return this.maxNormal;
    }

    /**
     * Sets the maximum normal value associated with the instance.
     *
     * @param maxNormal the value to set for the maxNormal field, representing the maximum normal
     * value
     */
    public void setMaxNormal(String maxNormal) {
      this.maxNormal = maxNormal;
    }

    /**
     * Retrieves the mastery score associated with this instance.
     *
     * @return the value of the masteryScore field, representing the defined mastery score.
     */
    public String getMasteryScore() {
      return this.masteryScore;
    }

    /**
     * Sets the mastery score for this instance.
     *
     * @param masteryScore the value to set for the masteryScore field, representing the defined
     * mastery score
     */
    public void setMasteryScore(String masteryScore) {
      this.masteryScore = masteryScore;
    }

    /**
     * Retrieves the maximum time allowed associated with this instance.
     *
     * @return the value of the maxTimeAllowed field, representing the maximum time allowed.
     */
    public String getMaxTimeAllowed() {
      return this.maxTimeAllowed;
    }

    /**
     * Sets the maximum time allowed for the instance.
     *
     * @param maxTimeAllowed the value to set for the maxTimeAllowed field, representing the maximum
     * duration allowed
     */
    public void setMaxTimeAllowed(String maxTimeAllowed) {
      this.maxTimeAllowed = maxTimeAllowed;
    }

    /**
     * Retrieves the time limit action associated with this instance.
     *
     * @return the value of the timeLimitAction field, representing the action to take when the time
     * limit is reached.
     */
    public String getTimeLimitAction() {
      return this.timeLimitAction;
    }

    /**
     * Sets the action to be taken when the time limit is reached for this instance.
     *
     * @param timeLimitAction the value to set for the timeLimitAction field, representing the
     * action to take when the time limit is reached
     */
    public void setTimeLimitAction(String timeLimitAction) {
      this.timeLimitAction = timeLimitAction;
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) {
        return true;
      }

      if (!(o instanceof CourseBehavior that)) {
        return false;
      }

      return new EqualsBuilder()
          .append(getMaxNormal(), that.getMaxNormal())
          .append(getMasteryScore(), that.getMasteryScore())
          .append(getMaxTimeAllowed(), that.getMaxTimeAllowed())
          .append(getTimeLimitAction(), that.getTimeLimitAction())
          .isEquals();
    }

    @Override
    public int hashCode() {
      return new HashCodeBuilder(17, 37)
          .append(getMaxNormal())
          .append(getMasteryScore())
          .append(getMaxTimeAllowed())
          .append(getTimeLimitAction())
          .toHashCode();
    }
  }
}
