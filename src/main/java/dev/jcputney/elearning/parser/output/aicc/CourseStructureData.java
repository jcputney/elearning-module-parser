package dev.jcputney.elearning.parser.output.aicc;

import java.util.List;

/**
 * Represents the hierarchical course structure in an AICC module as parsed from a .cst file.
 */
public class CourseStructureData {

  private final List<CourseStructureElement> elements;

  public CourseStructureData(List<CourseStructureElement> elements) {
    this.elements = elements;
  }

  public List<CourseStructureElement> getElements() {
    return elements;
  }
}