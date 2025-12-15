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

/**
 * Represents a NOT expression in the AICC prerequisite script.
 *
 * <p>A NOT expression negates its operand. In the context of SCORM prerequisites,
 * this means the referenced item must NOT be completed.</p>
 *
 * <p>Example: {@code ~ITEM-001}</p>
 *
 * @param operand the expression to negate
 */
public record NotExpression(PrerequisiteExpression operand) implements PrerequisiteExpression {

  @Override
  public <T> T accept(Visitor<T> visitor) {
    return visitor.visitNot(this);
  }

  @Override
  public String toString() {
    return "~" + operand;
  }
}
