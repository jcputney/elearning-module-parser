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

/**
 * Represents a relationship between two "objectives" within an AICC module. Stores information
 * about the source "objective", the target objective, and the type of relationship (e.g.,
 * prerequisite, parent-child).
 */
public class ObjectiveRelationship {

  private final String sourceObjective;
  private final String targetObjective;
  private final String relationshipType;

  /**
   * Constructs an ObjectiveRelationship with the specified details.
   *
   * @param sourceObjective The identifier of the source "objective".
   * @param targetObjective The identifier of the target "objective".
   * @param relationshipType The type of relationship (e.g., "prerequisite").
   */
  public ObjectiveRelationship(String sourceObjective, String targetObjective,
      String relationshipType) {
    this.sourceObjective = sourceObjective;
    this.targetObjective = targetObjective;
    this.relationshipType = relationshipType;
  }

  public String getSourceObjective() {
    return sourceObjective;
  }

  public String getTargetObjective() {
    return targetObjective;
  }

  public String getRelationshipType() {
    return relationshipType;
  }
}