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