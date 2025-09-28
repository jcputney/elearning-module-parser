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
 * Defines the types of nodes that can be part of an AICC prerequisite expression tree. Each node
 * type serves a specific role in representing logical or structural aspects of the prerequisite
 * expression used in e-learning systems. These types help structure and evaluate prerequisite
 * expressions.
 */
public enum AiccPrerequisiteNodeType {
  /**
   * Represents a specific identifier used within an AICC prerequisite expression. This type is
   * typically associated with a named course, module, or similar entity identifiable within the
   * expression tree.
   * <p>
   * The IDENTIFIER serves as a terminal or leaf node in the prerequisite structure, containing a
   * concrete reference that can be matched or evaluated during the parsing or evaluation process.
   */
  IDENTIFIER,
  /**
   * Represents a logical AND operation in an AICC prerequisite expression. This type serves as a
   * connector between two or more child nodes, where all child nodes must satisfy their conditions
   * for the AND operation to evaluate as true.
   * <p>
   * When used as part of a prerequisite expression tree, the AND node combines multiple
   * sub-expressions to collectively enforce logical conjunction.
   */
  AND,
  /**
   * Represents a logical OR operation in an AICC prerequisite expression. This type serves as a
   * connector between two or more child nodes, where at least one child node must satisfy its
   * condition for the OR operation to evaluate as true.
   * <p>
   * When used as part of a prerequisite expression tree, the OR node combines multiple
   * sub-expressions to enforce logical disjunction.
   */
  OR,
  /**
   * Represents a logical NOT operation in an AICC prerequisite expression. This type is used to
   * negate the logical outcome of its child node. The NOT node serves as a unary operator, meaning
   * it operates on a single child node.
   * <p>
   * By applying a NOT operation, the logical value of the child node is reversed during the
   * evaluation of the prerequisite expression: - If the child node evaluates to true, the NOT
   * operation results in false. - If the child node evaluates to false, the NOT operation results
   * in true.
   */
  NOT
}
