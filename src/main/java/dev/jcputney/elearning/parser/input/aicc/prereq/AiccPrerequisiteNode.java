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

import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * Represents a node in an AICC prerequisite expression tree. Each node has a specific type, an
 * optional value, and may contain child nodes to represent nested expressions.
 * <p>
 * This class is immutable and thread-safe.
 * <p>
 * The nodes are categorized by type using the {@code AiccPrerequisiteNodeType}, which defines their
 * logical or structural meaning, such as an identifier, logical operators, etc.
 * <p>
 * Attributes: - `type`: Indicates the type of the node (e.g. IDENTIFIER, AND, OR, NOT). - `value`:
 * Represents the value associated with the node, if applicable (e.g. an identifier for a
 * course/module). - `children`: A list of child nodes used to represent nested logical structures
 * in the prerequisite expression.
 * <p>
 * Functionalities: - Supports creation of both leaf nodes and tree structures with children. -
 * Provides equality, hash code, and string representation for comparing and describing the node. -
 * Ensures immutability by copying and requiring non-null values for its components during
 * initialization.
 */
@SuppressWarnings("ClassCanBeRecord")
public final class AiccPrerequisiteNode implements Serializable {

  /**
   * Defines the structural or logical type of the current node in the prerequisite expression tree.
   * This variable is integral to determining the purpose and behavior of the node within the
   * logical framework of the AICC prerequisite expression.
   * <p>
   * This can include, for instance: - IDENTIFIER: Represents an individual course or module. - AND,
   * OR, NOT: Denotes logical operators, forming connections or operations between nodes.
   * <p>
   * It ensures that the node has a clearly defined type, enabling consistent operations and
   * validations while constructing or evaluating prerequisite expressions.
   * <p>
   * This field is immutable and must be non-null.
   */
  private final AiccPrerequisiteNodeType type;

  /**
   * Holds the value associated with an AICC prerequisite node. This value typically represents a
   * specific criterion or identifier used within AICC prerequisite parsing and evaluation. It is
   * immutable and set during the creation of the node.
   */
  private final String value;

  /**
   * Represents the child nodes of the current {@code AiccPrerequisiteNode}. Each child node is an
   * instance of {@code AiccPrerequisiteNode}, allowing a hierarchical structure to represent
   * complex prerequisite expressions.
   * <p>
   * This list is immutable, ensuring thread safety and data consistency.
   */
  private final List<AiccPrerequisiteNode> children;

  public AiccPrerequisiteNode(AiccPrerequisiteNodeType type, String value,
      List<AiccPrerequisiteNode> children) {
    this.type = Objects.requireNonNull(type, "type");
    this.value = value;
    this.children = children == null ? List.of() : List.copyOf(children);
  }

  /**
   * Creates a new {@code AiccPrerequisiteNode} representing a leaf node in the prerequisite
   * expression tree. The leaf node is identified by the specified value and is of type
   * {@code AiccPrerequisiteNodeType.IDENTIFIER}. It has no child nodes.
   *
   * @param value the identifier value for the leaf node, representing a specific course or module.
   * @return a new {@code AiccPrerequisiteNode} instance of type {@code IDENTIFIER} with the
   * specified value and no child nodes.
   */
  public static AiccPrerequisiteNode leaf(String value) {
    return new AiccPrerequisiteNode(AiccPrerequisiteNodeType.IDENTIFIER, value, List.of());
  }

  public AiccPrerequisiteNodeType getType() {
    return this.type;
  }

  public String getValue() {
    return this.value;
  }

  public List<AiccPrerequisiteNode> getChildren() {
    return this.children;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (!(o instanceof AiccPrerequisiteNode that)) {
      return false;
    }

    return new EqualsBuilder()
        .append(getType(), that.getType())
        .append(getValue(), that.getValue())
        .append(getChildren(), that.getChildren())
        .isEquals();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder(17, 37)
        .append(getType())
        .append(getValue())
        .append(getChildren())
        .toHashCode();
  }

  @Override
  public String toString() {
    return "AiccPrerequisiteNode{"
        + "type=" + this.type
        + ", value='" + this.value + '\''
        + ", children=" + this.children
        + '}';
  }
}
