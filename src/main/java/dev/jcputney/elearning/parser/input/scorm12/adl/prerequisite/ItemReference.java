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
 * Represents a reference to an item identifier in the AICC prerequisite script.
 *
 * <p>An item reference is a leaf node in the expression tree that refers to a specific
 * item in the SCORM manifest by its identifier. The prerequisite is satisfied when
 * the referenced item has been completed by the learner.</p>
 *
 * <p>Example: {@code ITEM-001}</p>
 *
 * @param identifier the item identifier being referenced
 */
public record ItemReference(String identifier) implements PrerequisiteExpression {

  @Override
  public <T> T accept(Visitor<T> visitor) {
    return visitor.visitItemReference(this);
  }

  @Override
  public String toString() {
    return identifier;
  }
}
