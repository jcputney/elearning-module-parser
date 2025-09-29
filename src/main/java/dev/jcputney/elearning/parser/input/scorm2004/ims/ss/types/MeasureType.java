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

package dev.jcputney.elearning.parser.input.scorm2004.ims.ss.types;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Feature;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * Represents a decimal measure with a value between -1 and 1, inclusive, with at least four
 * significant decimal digits.
 *
 * <p>This type enforces the range and precision for a measure as defined in the XML schema.
 * Valid values for this type fall within the range of -1 to 1.</p>
 *
 * @param value The decimal value for the measure must be between -1 and 1 with at least four
 * decimal digits.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonFormat(with = Feature.ACCEPT_CASE_INSENSITIVE_PROPERTIES)
public record MeasureType(BigDecimal value) implements Serializable {

  /**
   * The minimum and maximum values for the measure type.
   */
  private static final BigDecimal MIN_VALUE = new BigDecimal("-1");

  /**
   * The maximum value for the measure type.
   */
  private static final BigDecimal MAX_VALUE = new BigDecimal("1");

  /**
   * The scale for the measure type, which defines the number of decimal places.
   */
  private static final int SCALE = 4; // At least 4 significant decimal digits

  /**
   * Default constructor for the MeasureType class.
   */
  public MeasureType() {
    this(BigDecimal.ZERO.setScale(SCALE, RoundingMode.HALF_UP));
  }

  /**
   * Constructs a MeasureType instance with the specified value.
   *
   * @param value the decimal value for the measure must be between -1 and 1 with at least four
   * decimal digits.
   * @throws IllegalArgumentException if the value is out of range or does not have at least four
   * decimal digits.
   */
  public MeasureType(BigDecimal value) {
    if (value == null) {
      throw new IllegalArgumentException("Value cannot be null");
    }
    if (value.compareTo(MIN_VALUE) < 0 || value.compareTo(MAX_VALUE) > 0) {
      throw new IllegalArgumentException("Value must be between -1 and 1, inclusive.");
    }
    this.value = value.setScale(SCALE, RoundingMode.HALF_UP);
  }

  /**
   * Parses a string as a MeasureType.
   *
   * @param value the string representation of the measure must represent a decimal between -1 and
   * 1.
   * @return a new MeasureType instance.
   * @throws IllegalArgumentException if the value is out of range or not a valid decimal format.
   */
  public static MeasureType parse(String value) {
    try {
      return new MeasureType(new BigDecimal(value));
    } catch (NumberFormatException e) {
      throw new IllegalArgumentException("Invalid measure format: " + value, e);
    }
  }

  /**
   * Returns the string representation of the measure value, formatted to at least four decimal
   * places.
   *
   * @return the string representation of the measure value.
   */
  @Override
  public String toString() {
    return value
        .setScale(SCALE, RoundingMode.HALF_UP)
        .toPlainString();
  }

  /**
   * Retrieves the current decimal value of the measure.
   *
   * @return the decimal value of the measure as a BigDecimal.
   */
  public BigDecimal getValue() {
    return value;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (!(o instanceof MeasureType that)) {
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