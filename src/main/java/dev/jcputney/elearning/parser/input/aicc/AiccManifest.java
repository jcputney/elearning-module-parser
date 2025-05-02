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

import com.fasterxml.jackson.annotation.JsonIgnore;
import dev.jcputney.elearning.parser.exception.ModuleParsingException;
import dev.jcputney.elearning.parser.input.PackageManifest;
import java.time.Duration;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * Represents the AICC manifest for a course.
 *
 * <p>This class is used to parse the AICC manifest file and extract information about the course,
 * assignable units, descriptors, and course structures.</p>
 *
 * <p>It also provides methods to retrieve the title, description, launch URL, identifier, version,
 * and duration of the course.</p>
 */
@Builder
@Getter
@Jacksonized
@AllArgsConstructor
public class AiccManifest implements PackageManifest {

  /**
   * Course information for the AICC manifest.
   */
  private AiccCourse course;

  /**
   * List of assignable units in the AICC manifest.
   */
  @SuppressWarnings("unused")
  private List<AssignableUnit> assignableUnits;

  /**
   * List of descriptors in the AICC manifest.
   */
  @SuppressWarnings("unused")
  private List<Descriptor> descriptors;

  /**
   * List of course structures in the AICC manifest.
   */
  @SuppressWarnings("unused")
  private List<CourseStructure> courseStructures;

  /**
   * The launch URL for the AICC manifest.
   */
  private String launchUrl;

  /**
   * Default constructor for the AiccManifest class.
   *
   * <p>This constructor initializes the AiccManifest object with the provided course,
   * assignable units, descriptors, and course structures.</p>
   *
   * @param course The AICC course information.
   * @param assignableUnits The list of assignable units.
   * @param descriptors The list of descriptors.
   * @param courseStructures The list of course structures.
   * @throws ModuleParsingException If there is an error parsing the AICC manifest.
   */
  public AiccManifest(AiccCourse course, List<AssignableUnit> assignableUnits,
      List<Descriptor> descriptors, List<CourseStructure> courseStructures)
      throws ModuleParsingException {
    this.course = course;
    this.assignableUnits = assignableUnits;
    this.descriptors = descriptors;
    this.courseStructures = courseStructures;

    for (Descriptor descriptor : descriptors) {
      for (AssignableUnit assignableUnit : assignableUnits) {
        if (descriptor.getSystemId().equals(assignableUnit.getSystemId())) {
          assignableUnit.setDescriptor(descriptor);
        }
      }
    }

    CourseStructure root = courseStructures
        .stream()
        .filter(cs -> cs.getBlock().equalsIgnoreCase("ROOT"))
        .findFirst()
        .orElse(null);

    if (root == null) {
      root = courseStructures.get(0);
    }
    String rootAssignableUnitId = root.getMember();
    if (rootAssignableUnitId == null || rootAssignableUnitId.isEmpty()) {
      throw new ModuleParsingException("No root assignable unit found.");
    }

    AssignableUnit rootAssignableUnit = assignableUnits
        .stream()
        .filter(au -> au.getSystemId().equals(rootAssignableUnitId))
        .findFirst()
        .orElseThrow(() -> new ModuleParsingException(
            "No assignable unit found with ID: " + rootAssignableUnitId));

    this.launchUrl = rootAssignableUnit.getFileName();
  }

  @Override
  @JsonIgnore
  public String getTitle() {
    return this.course.getCourse().getCourseTitle();
  }

  @Override
  @JsonIgnore
  public String getDescription() {
    return this.course.getCourseDescription();
  }

  @Override
  @JsonIgnore
  public String getLaunchUrl() {
    return launchUrl;
  }

  @Override
  @JsonIgnore
  public String getIdentifier() {
    return this.course.getCourse().getCourseId();
  }

  @Override
  @JsonIgnore
  public String getVersion() {
    return this.course.getCourse().getVersion();
  }

  @Override
  @JsonIgnore
  public Duration getDuration() {
    return Duration.ZERO;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    AiccManifest that = (AiccManifest) o;

    return new EqualsBuilder()
        .append(course, that.course)
        .append(assignableUnits, that.assignableUnits)
        .append(descriptors, that.descriptors)
        .append(courseStructures, that.courseStructures)
        .append(launchUrl, that.launchUrl)
        .isEquals();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder(17, 37)
        .append(course)
        .append(assignableUnits)
        .append(descriptors)
        .append(courseStructures)
        .append(launchUrl)
        .toHashCode();
  }
}
