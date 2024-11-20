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
