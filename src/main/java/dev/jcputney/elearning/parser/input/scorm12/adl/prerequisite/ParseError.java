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
 * Represents a parse error when the prerequisite expression cannot be parsed.
 *
 * <p>This allows the parser to gracefully handle malformed expressions without
 * throwing exceptions. Consumers can check if the result is a ParseError to
 * determine if the expression was valid.</p>
 *
 * <p>Example error scenarios:
 * <ul>
 *   <li>Unbalanced parentheses: {@code (ITEM-001 & ITEM-002}</li>
 *   <li>Missing operand: {@code ITEM-001 &}</li>
 *   <li>Invalid characters: {@code ITEM-001 @ ITEM-002}</li>
 *   <li>Empty expression: {@code ""}</li>
 * </ul>
 *
 * @param originalExpression the original expression string that failed to parse
 * @param errorMessage a description of why parsing failed
 * @param errorPosition the character position where the error was detected (0-based), or -1 if unknown
 */
public record ParseError(
    String originalExpression,
    String errorMessage,
    int errorPosition
) implements PrerequisiteExpression {

  /**
   * Creates a parse error with an unknown position.
   *
   * @param originalExpression the original expression string
   * @param errorMessage the error description
   */
  public ParseError(String originalExpression, String errorMessage) {
    this(originalExpression, errorMessage, -1);
  }

  @Override
  public <T> T accept(Visitor<T> visitor) {
    return visitor.visitParseError(this);
  }

  @Override
  public String toString() {
    if (errorPosition >= 0) {
      return "ParseError at position " + errorPosition + ": " + errorMessage;
    }
    return "ParseError: " + errorMessage;
  }
}
