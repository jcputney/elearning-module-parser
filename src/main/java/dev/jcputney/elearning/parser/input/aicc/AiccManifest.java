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

import dev.jcputney.elearning.parser.exception.ModuleParsingException;
import dev.jcputney.elearning.parser.input.PackageManifest;
import java.time.Duration;
import java.util.List;
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
        if (descriptor
            .getSystemId()
            .equals(assignableUnit.getSystemId())) {
          assignableUnit.setDescriptor(descriptor);
        }
      }
    }

    CourseStructure root = courseStructures
        .stream()
        .filter(cs -> cs
            .getBlock()
            .equalsIgnoreCase("ROOT"))
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
        .filter(au -> au
            .getSystemId()
            .equals(rootAssignableUnitId))
        .findFirst()
        .orElseThrow(() -> new ModuleParsingException(
            "No assignable unit found with ID: " + rootAssignableUnitId));

    this.launchUrl = rootAssignableUnit.getFileName();
  }

  public AiccManifest() {
  }

  @Override
  public String getTitle() {
    return this.course
        .getCourse()
        .getCourseTitle();
  }

  @Override
  public String getDescription() {
    return this.course.getCourseDescription();
  }

  @Override
  public String getIdentifier() {
    return this.course
        .getCourse()
        .getCourseId();
  }

  @Override
  public String getVersion() {
    return this.course
        .getCourse()
        .getVersion();
  }

  @Override
  public Duration getDuration() {
    return Duration.ZERO;
  }


  public AiccCourse getCourse() {
    return this.course;
  }

  public void setCourse(AiccCourse course) {
    this.course = course;
  }

  public List<AssignableUnit> getAssignableUnits() {
    return this.assignableUnits;
  }

  public void setAssignableUnits(List<AssignableUnit> assignableUnits) {
    this.assignableUnits = assignableUnits;
  }

  public List<Descriptor> getDescriptors() {
    return this.descriptors;
  }

  public void setDescriptors(List<Descriptor> descriptors) {
    this.descriptors = descriptors;
  }

  public List<CourseStructure> getCourseStructures() {
    return this.courseStructures;
  }

  public void setCourseStructures(List<CourseStructure> courseStructures) {
    this.courseStructures = courseStructures;
  }

  @Override
  public String getLaunchUrl() {
    return this.launchUrl;
  }

  public void setLaunchUrl(String launchUrl) {
    this.launchUrl = launchUrl;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (!(o instanceof AiccManifest that)) {
      return false;
    }

    return new EqualsBuilder()
        .append(getCourse(), that.getCourse())
        .append(getAssignableUnits(), that.getAssignableUnits())
        .append(getDescriptors(), that.getDescriptors())
        .append(getCourseStructures(), that.getCourseStructures())
        .append(getLaunchUrl(), that.getLaunchUrl())
        .isEquals();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder(17, 37)
        .append(getCourse())
        .append(getAssignableUnits())
        .append(getDescriptors())
        .append(getCourseStructures())
        .append(getLaunchUrl())
        .toHashCode();
  }
}
