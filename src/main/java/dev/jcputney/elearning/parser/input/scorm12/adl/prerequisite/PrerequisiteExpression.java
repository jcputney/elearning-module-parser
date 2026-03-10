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
   * Accepts a visitor to process this expression.
   *
   * @param visitor the visitor to accept
   * @param <T> the return type of the visitor
   * @return the result of the visitor processing this expression
   */
  <T> T accept(Visitor<T> visitor);

  /**
   * Visitor pattern for processing prerequisite expressions.
   *
   * @param <T> the return type of the visitor methods
   */
  interface Visitor<T> {

    /**
     * Visits an AND expression and processes it using the logic defined by the visitor.
     *
     * @param and the AND expression to be visited
     * @return the result of applying the visitor's logic to the given AND expression
     */
    T visitAnd(AndExpression and);

    /**
     * Visits an OR expression and processes it using the logic defined by the visitor.
     *
     * @param or the OR expression to be visited
     * @return the result of applying the visitor's logic to the given OR expression
     */
    T visitOr(OrExpression or);

    /**
     * Visits a NOT expression and processes it using the logic defined by the visitor.
     *
     * @param not the NOT expression to be visited
     * @return the result of applying the visitor's logic to the given NOT expression
     */
    T visitNot(NotExpression not);

    /**
     * Processes an {@link ItemReference} in the context of the visitor pattern.
     *
     * @param itemRef the {@link ItemReference} to be visited, representing a reference to a
     * specific item in a SCORM prerequisite script.
     * @return the result of applying the visitor's logic to the given {@link ItemReference}.
     */
    T visitItemReference(ItemReference itemRef);

    /**
     * Visits a {@link ParseError} and processes it using the logic defined by the visitor.
     *
     * @param error the {@link ParseError} instance representing a parsing error in a prerequisite
     * expression
     * @return the result of applying the visitor's logic to the given {@link ParseError}
     */
    T visitParseError(ParseError error);
  }
}
