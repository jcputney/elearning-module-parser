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
 * Represents a percentage value constrained between 0 and 1, inclusive. This type enforces the
 * range but does not enforce specific decimal precision.
 *
 * @param value The decimal value for the percentage must be between 0 and 1.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonFormat(with = Feature.ACCEPT_CASE_INSENSITIVE_PROPERTIES)
public record PercentType(BigDecimal value) implements Serializable {

  /**
   * The minimum and maximum values for the percentage.
   */
  private static final BigDecimal MIN_VALUE = BigDecimal.ZERO;
  /**
   * The maximum value for the percentage.
   */
  private static final BigDecimal MAX_VALUE = BigDecimal.ONE;

  /**
   * Default constructor for the PercentType class.
   */
  @SuppressWarnings("unused")
  public PercentType() {
    this(MIN_VALUE);
  }

  /**
   * Constructs a PercentType instance with the specified value.
   *
   * @param value the decimal value for the percentage must be between 0 and 1.
   * @throws IllegalArgumentException if the value is out of range.
   */
  public PercentType {
    if (value == null) {
      throw new IllegalArgumentException("Value cannot be null");
    }
    if (value.compareTo(MIN_VALUE) < 0 || value.compareTo(MAX_VALUE) > 0) {
      throw new IllegalArgumentException("Value must be between 0 and 1, inclusive.");
    }
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
}
