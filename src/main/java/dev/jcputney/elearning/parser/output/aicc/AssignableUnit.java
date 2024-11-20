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
 * Represents an Assignable Unit (AU) in an AICC module, containing details such as the unit ID,
 * file name, maximum score, and minimum score.
 */
public class AssignableUnit {

  private final String id;
  private final String fileName;
  private final String maxScore;
  private final String minScore;

  /**
   * Constructs an AssignableUnit with the specified details.
   *
   * @param id The unique identifier of the assignable unit.
   * @param fileName The file name or path for the content associated with this unit.
   * @param maxScore The maximum score achievable in this unit.
   * @param minScore The minimum score required in this unit.
   */
  public AssignableUnit(String id, String fileName, String maxScore, String minScore) {
    this.id = id;
    this.fileName = fileName;
    this.maxScore = maxScore;
    this.minScore = minScore;
  }

  /**
   * Gets the unique identifier of the assignable unit.
   *
   * @return The assignable unit ID.
   */
  public String getId() {
    return id;
  }

  /**
   * Gets the file name or path associated with this assignable unit.
   *
   * @return The file name or path.
   */
  public String getFileName() {
    return fileName;
  }

  /**
   * Gets the maximum score achievable in this assignable unit.
   *
   * @return The maximum score as a string.
   */
  public String getMaxScore() {
    return maxScore;
  }

  /**
   * Gets the minimum score required for this assignable unit.
   *
   * @return The minimum score as a string.
   */
  public String getMinScore() {
    return minScore;
  }
}