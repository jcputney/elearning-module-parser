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

import java.util.List;

/**
 * Represents a single element within the course structure of an AICC module. Each element may
 * contain a unique identifier, title, and potentially child elements for hierarchical course
 * structures.
 */
public record CourseStructureElement(String id, String title,
                                     List<CourseStructureElement> children) {

  /**
   * Constructs a CourseStructureElement with the specified details.
   *
   * @param id The unique identifier of the course structure element.
   * @param title The title or name of the course structure element.
   * @param children A list of child elements under this course structure element.
   */
  public CourseStructureElement(String id, String title, List<CourseStructureElement> children) {
    this.id = id;
    this.title = title;
    this.children = children != null ? children : List.of();
  }
}