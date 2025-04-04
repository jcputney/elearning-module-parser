/*
 * Copyright (c) 2024. Jonathan Putney
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
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.math.BigDecimal;
import lombok.Getter;

/**
 * Represents a percentage value constrained between 0 and 1, inclusive. This type enforces the
 * range but does not enforce specific decimal precision.
 */
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonFormat(with = JsonFormat.Feature.ACCEPT_CASE_INSENSITIVE_PROPERTIES)
public class PercentType {

  /**
   * The minimum and maximum values for the percentage.
   */
  private static final BigDecimal MIN_VALUE = BigDecimal.ZERO;

  /**
   * The maximum value for the percentage.
   */
  private static final BigDecimal MAX_VALUE = BigDecimal.ONE;

  /**
   * The decimal value for the percentage must be between 0 and 1.
   */
  private final BigDecimal value;

  /**
   * Default constructor for the PercentType class.
   */
  @SuppressWarnings("unused")
  public PercentType() {
    this.value = MIN_VALUE;
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
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PercentType that = (PercentType) o;
    return value.equals(that.value);
  }

  @Override
  public int hashCode() {
    return value.hashCode();
  }
}
