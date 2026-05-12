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

import java.util.List;

/**
 * Represents an AND expression in the AICC prerequisite script.
 *
 * <p>An AND expression requires all of its operands to evaluate to true for the
 * overall expression to be satisfied. In the context of SCORM prerequisites,
 * this means all referenced items must be completed.</p>
 *
 * <p>Example: {@code ITEM-001 & ITEM-002 & ITEM-003}</p>
 *
 * @param operands the list of expressions that must all be satisfied
 */
public record AndExpression(List<PrerequisiteExpression> operands) implements PrerequisiteExpression {

  /**
   * Creates an AND expression with the given operands.
   *
   * @param operands the list of expressions that must all be satisfied
   */
  public AndExpression {
    operands = List.copyOf(operands);
  }

  @Override
  public <T> T accept(Visitor<T> visitor) {
    return visitor.visitAnd(this);
  }

  @Override
  public String toString() {
    return "(" + String.join(" & ", operands.stream().map(Object::toString).toList()) + ")";
  }
}
