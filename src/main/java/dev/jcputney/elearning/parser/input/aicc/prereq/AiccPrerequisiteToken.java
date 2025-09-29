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

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * Represents a token parsed from an AICC prerequisite expression. Each token has a specific type
 * that determines its role in the expression, such as an identifier, operator, or parenthesis.
 * <p>
 * The token optionally supports additional characteristics, such as being optional or unary, to
 * further describe its behavior and usage within the expression.
 * <p>
 * Instances of this class are immutable and thread-safe.
 */
@SuppressWarnings("ClassCanBeRecord")
public final class AiccPrerequisiteToken implements Serializable {

  @Serial
  private static final long serialVersionUID = 1L;

  /**
   * The type of this token, represented as an instance of {@link AiccPrerequisiteTokenType}.
   * <p>
   * This field determines the role of the token in an AICC prerequisite expression, such as whether
   * it is an identifier, operator, or a parenthesis. The type is immutable once assigned.
   */
  private final AiccPrerequisiteTokenType type;

  /**
   * The string value associated with this token. This represents the actual content or data carried
   * by the token, such as an identifier name or an operator symbol.
   * <p>
   * The value is immutable once assigned and may be null to indicate the absence of a specific
   * value for certain token scenarios.
   */
  private final String value;

  /**
   * Indicates whether this token is optional within the AICC prerequisite expression.
   * <p>
   * If true, the token represents an optional component of the expression, meaning its presence is
   * not strictly required for the expression to remain valid.
   * <p>
   * This field is immutable and is set during the creation of the token.
   */
  private final boolean optional;

  /**
   * Indicates whether the associated prerequisite token is unary in nature.
   * <p>
   * A unary token represents an operation or expression dealing with only one operand. It is
   * commonly used to signify the presence of a single condition or state within a prerequisite
   * expression, such as negation or a singular logical condition.
   * <p>
   * This field is immutable and is set during the creation of the {@code AiccPrerequisiteToken}
   * object.
   */
  private final boolean unary;

  public AiccPrerequisiteToken(AiccPrerequisiteTokenType type, String value) {
    this(type, value, false, false);
  }

  public AiccPrerequisiteToken(AiccPrerequisiteTokenType type, String value, boolean optional,
      boolean unary) {
    this.type = Objects.requireNonNull(type, "type");
    this.value = value;
    this.optional = optional;
    this.unary = unary;
  }

  /**
   * Retrieves the token type of this AICC prerequisite token.
   *
   * @return the type of the AICC prerequisite token, represented as an
   * {@code AiccPrerequisiteTokenType}.
   */
  public AiccPrerequisiteTokenType getType() {
    return this.type;
  }

  /**
   * Retrieves the value of this object.
   *
   * @return a string representing the value of this object, or null if no value is assigned.
   */
  public String getValue() {
    return this.value;
  }

  /**
   * Indicates whether this token is optional.
   *
   * @return true if the token is optional, false otherwise
   */
  public boolean isOptional() {
    return this.optional;
  }

  /**
   * Indicates whether this token is a unary token.
   *
   * @return true if the token is unary, false otherwise
   */
  public boolean isUnary() {
    return this.unary;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (!(o instanceof AiccPrerequisiteToken that)) {
      return false;
    }

    return new EqualsBuilder()
        .append(isOptional(), that.isOptional())
        .append(isUnary(), that.isUnary())
        .append(getType(), that.getType())
        .append(getValue(), that.getValue())
        .isEquals();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder(17, 37)
        .append(getType())
        .append(getValue())
        .append(isOptional())
        .append(isUnary())
        .toHashCode();
  }

  @Override
  public String toString() {
    return "AiccPrerequisiteToken{"
        + "type=" + this.type
        + ", value='" + this.value + '\''
        + ", optional=" + this.optional
        + ", unary=" + this.unary
        + '}';
  }
}
