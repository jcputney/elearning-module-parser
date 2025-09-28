/*
 * qlty-ignore: +qlty:similar-code
 *
 * Copyright (c) 2025. Jonathan Putney
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
 *
 * qlty-ignore: -qlty:similar-code
 */

package dev.jcputney.elearning.parser.input.aicc.prereq;

/**
 * Represents the types of tokens that can be encountered when parsing an AICC prerequisite
 * expression. Each token type defines the role the token plays within the expression.
 * <p>
 * The following token types are supported: - IDENTIFIER: Represents a learning object or a literal
 * value. - OPERATOR: Represents logical operators (e.g., AND, OR, NOT). - LEFT_PAREN: Represents
 * the opening parenthesis in an expression. - RIGHT_PAREN: Represents the closing parenthesis in an
 * expression.
 */
public enum AiccPrerequisiteTokenType {

  /**
   * Identifier token representing an AU or literal value.
   */
  IDENTIFIER,
  /**
   * Logical operator token (AND, OR, NOT).
   */
  OPERATOR,
  /**
   * Left parenthesis token.
   */
  LEFT_PAREN,
  /**
   * Right parenthesis token.
   */
  RIGHT_PAREN
}
