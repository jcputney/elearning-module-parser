/*
 * Copyright (c) 2024. Jonathan Putney
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
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;

@Builder
@Getter
@Jacksonized
@AllArgsConstructor
public class AiccManifest implements PackageManifest {

  private AiccCourse course;

  private List<AssignableUnit> assignableUnits;

  private List<Descriptor> descriptors;

  private List<CourseStructure> courseStructures;

  private String launchUrl;

  public AiccManifest(AiccCourse course, List<AssignableUnit> assignableUnits, List<Descriptor> descriptors, List<CourseStructure> courseStructures)
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

    CourseStructure root = courseStructures.stream()
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

    AssignableUnit rootAssignableUnit = assignableUnits.stream()
        .filter(au -> au.getSystemId().equals(rootAssignableUnitId))
        .findFirst()
        .orElseThrow(() -> new ModuleParsingException("No assignable unit found with ID: " + rootAssignableUnitId));

    this.launchUrl = rootAssignableUnit.getFileName();
  }

  @Override
  public String getTitle() {
    return this.course.getCourse().getCourseTitle();
  }

  @Override
  public String getDescription() {
    return this.course.getCourseDescription();
  }

  @Override
  public String getLaunchUrl() {
    return launchUrl;
  }

  @Override
  public String getIdentifier() {
    return this.course.getCourse().getCourseId();
  }

  @Override
  public String getVersion() {
    return this.course.getCourse().getVersion();
  }

  @Override
  public Duration getDuration() {
    return Duration.ZERO;
  }
}
