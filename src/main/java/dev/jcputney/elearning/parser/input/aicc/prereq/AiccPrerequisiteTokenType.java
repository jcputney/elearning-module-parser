/*
 * Copyright (c) 2024-2026 Jonathan Putney
 *
 * Licensed under the Apache License, Version 2.0 (the "License").
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at the project root LICENSE file
 * or at http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0
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
