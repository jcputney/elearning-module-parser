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
