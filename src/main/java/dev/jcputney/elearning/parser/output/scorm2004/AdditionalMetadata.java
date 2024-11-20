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

package dev.jcputney.elearning.parser.output.scorm2004;

import java.util.List;
import java.util.Optional;

/**
 * Represents additional metadata fields that may be present in external metadata files or
 * additional manifest files across various eLearning standards (e.g., SCORM 2004, AICC).
 * <p>
 * This class provides a standardized way to store common fields without relying on generic types.
 * </p>
 */
public class AdditionalMetadata {

  private String title;
  private String description;
  private List<String> objectives;
  private List<String> keywords;

  // Constructors, getters, and setters for each field

  public AdditionalMetadata() {
  }

  public AdditionalMetadata(String title, String description, List<String> objectives,
      List<String> keywords) {
    this.title = title;
    this.description = description;
    this.objectives = objectives;
    this.keywords = keywords;
  }

  public Optional<String> getTitle() {
    return Optional.ofNullable(title);
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public Optional<String> getDescription() {
    return Optional.ofNullable(description);
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public List<String> getObjectives() {
    return objectives;
  }

  public void setObjectives(List<String> objectives) {
    this.objectives = objectives;
  }

  public List<String> getKeywords() {
    return keywords;
  }

  public void setKeywords(List<String> keywords) {
    this.keywords = keywords;
  }
}
