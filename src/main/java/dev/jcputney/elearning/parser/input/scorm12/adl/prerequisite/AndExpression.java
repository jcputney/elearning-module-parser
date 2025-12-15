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

package dev.jcputney.elearning.parser.input.scorm12.adl.prerequisite;

import java.util.List;

/**
 * Represents an AND expression in the AICC prerequisite script.
 *
 * <p>An AND expression requires all of its operands to evaluate to true for the
 * overall expression to be satisfied. In the context of SCORM prerequisites,
 * this means all referenced items must be completed.</p>
 *
 * <p>Example: {@code ITEM-001 & ITEM-002 & ITEM-003}</p>
 *
 * @param operands the list of expressions that must all be satisfied
 */
public record AndExpression(List<PrerequisiteExpression> operands) implements PrerequisiteExpression {

  /**
   * Creates an AND expression with the given operands.
   *
   * @param operands the list of expressions that must all be satisfied
   */
  public AndExpression {
    operands = List.copyOf(operands);
  }

  @Override
  public <T> T accept(Visitor<T> visitor) {
    return visitor.visitAnd(this);
  }

  @Override
  public String toString() {
    return "(" + String.join(" & ", operands.stream().map(Object::toString).toList()) + ")";
  }
}
