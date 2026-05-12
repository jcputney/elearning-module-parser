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
