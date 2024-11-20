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
 * Represents a collection of assignable units parsed from an AICC .au file.
 */
public class AssignableUnitData {

  private final List<AssignableUnit> assignableUnits;

  public AssignableUnitData(List<AssignableUnit> assignableUnits) {
    this.assignableUnits = assignableUnits;
  }

  public List<AssignableUnit> getAssignableUnits() {
    return assignableUnits;
  }
}
