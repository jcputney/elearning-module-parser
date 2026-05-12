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
