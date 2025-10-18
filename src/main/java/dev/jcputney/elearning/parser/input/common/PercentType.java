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

package dev.jcputney.elearning.parser.input.common;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Feature;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.math.BigDecimal;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * Represents a percentage value constrained between 0 and 1 (inclusive). It is designed for use
 * cases where percentages are represented as decimal values.
 * <p>
 * This class is: - Immutable: Once an instance is created, its value cannot be changed. -
 * Thread-safe: The immutability ensures no synchronization issues during concurrent access. -
 * Validated: Ensures the percentage value lies within the valid range [0, 1] during
 * initialization.
 * <p>
 * Features and behaviors: - Contains factory and parsing methods for creating instances. - Provides
 * a string representation of the percentage value. - Overridden {@code equals} and {@code hashCode}
 * methods for dictionary lookup or comparison purposes. - Integrated with JSON serialization
 * frameworks such as Jackson through custom configuration.
 * <p>
 * The intended use of this class includes scenarios where precise representation and validation of
 * percentages are required, for example in financial, academic, or mathematical applications.
 * <p>
 * Note: The numeric value does not enforce a specific scale or precision but must lie in the range
 * 0 to 1.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonFormat(with = Feature.ACCEPT_CASE_INSENSITIVE_PROPERTIES)
public final class PercentType implements Serializable {

  /**
   * The minimum and maximum values for the percentage.
   */
  private static final BigDecimal MIN_VALUE = BigDecimal.ZERO;

  /**
   * The maximum value for the percentage.
   */
  private static final BigDecimal MAX_VALUE = BigDecimal.ONE;

  /**
   * Represents the percentage value for an instance of {@code PercentType}.
   *
   * <ul>
   *   <li>This value is constrained between 0 and 1, inclusive.</li>
   *   <li>It enforces the range bounds but does not enforce specific decimal precision.</li>
   *   <li>Immutable once assigned, ensuring thread-safe usage and preventing unintended modification.</li>
   * </ul>
   */
  private final BigDecimal value;


  /**
   * Default constructor for the PercentType class.
   */
  public PercentType() {
    this(MIN_VALUE);
  }

  /**
   * Constructs a PercentType instance with the specified value.
   *
   * @param value the decimal value for the percentage must be between 0 and 1.
   * @throws IllegalArgumentException if the value is out of range.
   */
  public PercentType(BigDecimal value) {
    if (value == null) {
      throw new IllegalArgumentException("Value cannot be null");
    }
    if (value.compareTo(MIN_VALUE) < 0 || value.compareTo(MAX_VALUE) > 0) {
      throw new IllegalArgumentException("Value must be between 0 and 1, inclusive.");
    }
    this.value = value;
  }

  /**
   * Parses a string as a PercentType.
   *
   * @param value the string representation of the percentage must be a decimal between 0 and 1.
   * @return a new PercentType instance.
   * @throws IllegalArgumentException if the value is out of range or not a valid decimal format.
   */
  public static PercentType parse(String value) {
    try {
      return new PercentType(new BigDecimal(value));
    } catch (NumberFormatException e) {
      throw new IllegalArgumentException("Invalid percent format: " + value, e);
    }
  }

  /**
   * Returns the string representation of the percentage value.
   *
   * @return the string representation of the percentage value.
   */
  @Override
  public String toString() {
    return value.toPlainString();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (!(o instanceof PercentType that)) {
      return false;
    }

    return new EqualsBuilder()
        .append(value(), that.value())
        .isEquals();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder(17, 37)
        .append(value())
        .toHashCode();
  }

  /**
   * Retrieves the value of the percentage as a BigDecimal.
   *
   * @return the decimal representation of the percentage value.
   */
  public BigDecimal value() {
    return value;
  }

}
