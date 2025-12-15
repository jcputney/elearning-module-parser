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
 * Represents a parsed prerequisite expression in AICC script format.
 *
 * <p>SCORM 1.2 prerequisites use AICC script syntax which supports:
 * <ul>
 *   <li>{@code &} - AND operator (all referenced items must be completed)</li>
 *   <li>{@code |} - OR operator (at least one referenced item must be completed)</li>
 *   <li>{@code ~} - NOT operator (referenced item must NOT be completed)</li>
 *   <li>Parentheses for grouping expressions</li>
 *   <li>Item identifiers referencing other items in the manifest</li>
 * </ul>
 *
 * <p>Example expressions:
 * <ul>
 *   <li>{@code ITEM-001} - Simple reference to a single item</li>
 *   <li>{@code ITEM-001 & ITEM-002} - Both items must be completed</li>
 *   <li>{@code ITEM-001 | ITEM-002} - Either item must be completed</li>
 *   <li>{@code ~ITEM-001} - Item must NOT be completed</li>
 *   <li>{@code (ITEM-001 & ITEM-002) | ITEM-003} - Grouped expression</li>
 * </ul>
 *
 * <p>This is a sealed interface with the following implementations:
 * <ul>
 *   <li>{@link AndExpression} - Represents an AND of multiple expressions</li>
 *   <li>{@link OrExpression} - Represents an OR of multiple expressions</li>
 *   <li>{@link NotExpression} - Represents negation of an expression</li>
 *   <li>{@link ItemReference} - Represents a reference to an item identifier</li>
 *   <li>{@link ParseError} - Represents a parse error for invalid expressions</li>
 * </ul>
 */
public sealed interface PrerequisiteExpression
    permits AndExpression, OrExpression, NotExpression, ItemReference, ParseError {

  /**
   * Visitor pattern for processing prerequisite expressions.
   *
   * @param <T> the return type of the visitor methods
   */
  interface Visitor<T> {
    T visitAnd(AndExpression and);
    T visitOr(OrExpression or);
    T visitNot(NotExpression not);
    T visitItemReference(ItemReference itemRef);
    T visitParseError(ParseError error);
  }

  /**
   * Accepts a visitor to process this expression.
   *
   * @param visitor the visitor to accept
   * @param <T> the return type of the visitor
   * @return the result of the visitor processing this expression
   */
  <T> T accept(Visitor<T> visitor);
}
