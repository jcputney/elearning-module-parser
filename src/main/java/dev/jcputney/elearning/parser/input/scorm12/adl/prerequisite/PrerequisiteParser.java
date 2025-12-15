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

import java.util.ArrayList;
import java.util.List;

/**
 * Parser for AICC prerequisite script expressions used in SCORM 1.2.
 *
 * <p>The parser implements a recursive descent parser for the following grammar:
 * <pre>
 * expression  → term (('|') term)*
 * term        → factor (('&') factor)*
 * factor      → '~'? atom
 * atom        → IDENTIFIER | '(' expression ')'
 * IDENTIFIER  → [A-Za-z0-9_-]+
 * </pre>
 *
 * <p>Operator precedence (highest to lowest):
 * <ol>
 *   <li>NOT (~) - unary prefix</li>
 *   <li>AND (&amp;) - left associative</li>
 *   <li>OR (|) - left associative</li>
 * </ol>
 *
 * <p>This parser never throws exceptions. Instead, it returns a {@link ParseError}
 * for malformed expressions, allowing consumers to gracefully handle errors.</p>
 */
public final class PrerequisiteParser {

  private final String input;
  private final String originalInput;
  private int position;

  private PrerequisiteParser(String input) {
    this.originalInput = input;
    this.input = input;
    this.position = 0;
  }

  /**
   * Parses an AICC prerequisite script expression.
   *
   * <p>This method never throws exceptions. For invalid expressions, it returns
   * a {@link ParseError} containing details about the parsing failure.</p>
   *
   * @param expression the prerequisite expression to parse
   * @return the parsed expression tree, or a {@link ParseError} if parsing fails
   */
  public static PrerequisiteExpression parse(String expression) {
    if (expression == null || expression.isBlank()) {
      return new ParseError(expression, "Empty or null expression");
    }

    PrerequisiteParser parser = new PrerequisiteParser(expression.trim());
    PrerequisiteExpression result = parser.parseExpression();

    // Check for trailing content
    parser.skipWhitespace();
    if (!parser.isAtEnd()) {
      return new ParseError(
          expression,
          "Unexpected content after expression: '" + parser.remaining() + "'",
          parser.position
      );
    }

    return result;
  }

  /**
   * Parses an expression (OR level - lowest precedence).
   */
  private PrerequisiteExpression parseExpression() {
    PrerequisiteExpression left = parseTerm();

    // Handle potential error from parseTerm
    if (left instanceof ParseError) {
      return left;
    }

    List<PrerequisiteExpression> orOperands = new ArrayList<>();
    orOperands.add(left);

    while (matchChar('|')) {
      PrerequisiteExpression right = parseTerm();
      if (right instanceof ParseError) {
        return right;
      }
      orOperands.add(right);
    }

    // Return single operand without wrapping in OR
    if (orOperands.size() == 1) {
      return orOperands.get(0);
    }

    return new OrExpression(orOperands);
  }

  /**
   * Parses a term (AND level - higher precedence than OR).
   */
  private PrerequisiteExpression parseTerm() {
    PrerequisiteExpression left = parseFactor();

    // Handle potential error from parseFactor
    if (left instanceof ParseError) {
      return left;
    }

    List<PrerequisiteExpression> andOperands = new ArrayList<>();
    andOperands.add(left);

    while (matchChar('&')) {
      PrerequisiteExpression right = parseFactor();
      if (right instanceof ParseError) {
        return right;
      }
      andOperands.add(right);
    }

    // Return single operand without wrapping in AND
    if (andOperands.size() == 1) {
      return andOperands.get(0);
    }

    return new AndExpression(andOperands);
  }

  /**
   * Parses a factor (NOT level - highest precedence).
   */
  private PrerequisiteExpression parseFactor() {
    skipWhitespace();

    // Check for NOT operator
    if (matchChar('~')) {
      PrerequisiteExpression operand = parseFactor();
      if (operand instanceof ParseError) {
        return operand;
      }
      return new NotExpression(operand);
    }

    return parseAtom();
  }

  /**
   * Parses an atom (identifier or parenthesized expression).
   */
  private PrerequisiteExpression parseAtom() {
    skipWhitespace();

    if (isAtEnd()) {
      return new ParseError(originalInput, "Unexpected end of expression", position);
    }

    // Parenthesized expression
    if (matchChar('(')) {
      PrerequisiteExpression expr = parseExpression();
      if (expr instanceof ParseError) {
        return expr;
      }
      skipWhitespace();
      if (!matchChar(')')) {
        return new ParseError(originalInput, "Missing closing parenthesis", position);
      }
      return expr;
    }

    // Identifier
    return parseIdentifier();
  }

  /**
   * Parses an identifier (item reference).
   */
  private PrerequisiteExpression parseIdentifier() {
    skipWhitespace();
    int start = position;

    while (!isAtEnd() && isIdentifierChar(peek())) {
      advance();
    }

    if (start == position) {
      char unexpected = isAtEnd() ? '\0' : peek();
      return new ParseError(
          originalInput,
          "Expected identifier but found '" + (unexpected == '\0' ? "end of input" : unexpected) + "'",
          position
      );
    }

    String identifier = input.substring(start, position);
    return new ItemReference(identifier);
  }

  /**
   * Checks if a character is valid in an identifier.
   * AICC allows alphanumeric characters, underscores, hyphens, and periods.
   */
  private boolean isIdentifierChar(char c) {
    return Character.isLetterOrDigit(c) || c == '_' || c == '-' || c == '.';
  }

  /**
   * Attempts to match and consume a specific character.
   */
  private boolean matchChar(char expected) {
    skipWhitespace();
    if (isAtEnd() || peek() != expected) {
      return false;
    }
    advance();
    return true;
  }

  /**
   * Returns the current character without consuming it.
   */
  private char peek() {
    return input.charAt(position);
  }

  /**
   * Consumes and returns the current character.
   */
  private char advance() {
    return input.charAt(position++);
  }

  /**
   * Checks if the parser has reached the end of input.
   */
  private boolean isAtEnd() {
    return position >= input.length();
  }

  /**
   * Skips whitespace characters.
   */
  private void skipWhitespace() {
    while (!isAtEnd() && Character.isWhitespace(peek())) {
      advance();
    }
  }

  /**
   * Returns the remaining unparsed input.
   */
  private String remaining() {
    return input.substring(position);
  }
}
