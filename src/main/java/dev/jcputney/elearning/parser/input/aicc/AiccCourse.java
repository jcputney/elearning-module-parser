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

import static lombok.AccessLevel.PRIVATE;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;
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
@Builder
@Getter
@Jacksonized
@AllArgsConstructor(access = PRIVATE)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonFormat(with = JsonFormat.Feature.ACCEPT_CASE_INSENSITIVE_PROPERTIES)
public class AiccCourse implements Serializable {

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
   * Default constructor for the AiccCourse class.
   */
  @SuppressWarnings("unused")
  public AiccCourse() {
    // Default constructor
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    AiccCourse that = (AiccCourse) o;

    return new EqualsBuilder()
        .append(course, that.course)
        .append(courseBehavior, that.courseBehavior)
        .append(courseDescription, that.courseDescription)
        .isEquals();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder(17, 37)
        .append(course)
        .append(courseBehavior)
        .append(courseDescription)
        .toHashCode();
  }

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
   * Represents the course information in the AICC manifest.
   */
  @Builder
  @Getter
  @Jacksonized
  @AllArgsConstructor(access = PRIVATE)
  @JsonFormat(with = JsonFormat.Feature.ACCEPT_CASE_INSENSITIVE_PROPERTIES)
  public static class Course {

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
     * Default constructor for the Course class.
     */
    @SuppressWarnings("unused")
    public Course() {
      // Default constructor
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) {
        return true;
      }

      if (o == null || getClass() != o.getClass()) {
        return false;
      }

      Course course = (Course) o;

      return new EqualsBuilder()
          .append(courseCreator, course.courseCreator)
          .append(courseId, course.courseId)
          .append(courseTitle, course.courseTitle)
          .append(courseSystem, course.courseSystem)
          .append(level, course.level)
          .append(maxFieldsCst, course.maxFieldsCst)
          .append(maxFieldsOrt, course.maxFieldsOrt)
          .append(totalAus, course.totalAus)
          .append(totalBlocks, course.totalBlocks)
          .append(version, course.version)
          .append(totalComplexObj, course.totalComplexObj)
          .append(totalObjectives, course.totalObjectives)
          .isEquals();
    }

    @Override
    public int hashCode() {
      return new HashCodeBuilder(17, 37)
          .append(courseCreator)
          .append(courseId)
          .append(courseTitle)
          .append(courseSystem)
          .append(level)
          .append(maxFieldsCst)
          .append(maxFieldsOrt)
          .append(totalAus)
          .append(totalBlocks)
          .append(version)
          .append(totalComplexObj)
          .append(totalObjectives)
          .toHashCode();
    }
  }

  /**
   * Represents the course behavior information in the AICC manifest.
   */
  @Builder
  @Getter
  @Jacksonized
  @AllArgsConstructor(access = PRIVATE)
  @JsonFormat(with = JsonFormat.Feature.ACCEPT_CASE_INSENSITIVE_PROPERTIES)
  public static class CourseBehavior {

    /**
     * The maximum normal value.
     */
    @JsonProperty(value = "Max_Normal", required = true)
    private String maxNormal;

    /**
     * Default constructor for the CourseBehavior class.
     */
    @SuppressWarnings("unused")
    public CourseBehavior() {
      // Default constructor
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) {
        return true;
      }

      if (o == null || getClass() != o.getClass()) {
        return false;
      }

      CourseBehavior that = (CourseBehavior) o;

      return new EqualsBuilder()
          .append(maxNormal, that.maxNormal)
          .isEquals();
    }

    @Override
    public int hashCode() {
      return new HashCodeBuilder(17, 37)
          .append(maxNormal)
          .toHashCode();
    }
  }
}
