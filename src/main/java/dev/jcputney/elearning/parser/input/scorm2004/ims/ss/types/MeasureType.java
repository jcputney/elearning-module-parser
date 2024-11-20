package dev.jcputney.elearning.parser.input.scorm2004.ims.ss.types;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.math.BigDecimal;
import java.math.RoundingMode;
import lombok.Getter;

/**
 * Represents a decimal measure with a value between -1 and 1, inclusive, with at least four
 * significant decimal digits.
 *
 * <p>This type enforces the range and precision for a measure as defined in the XML schema.
 * Valid values for this type fall within the range of -1 to 1.</p>
 */
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class MeasureType {

  private static final BigDecimal MIN_VALUE = new BigDecimal("-1");
  private static final BigDecimal MAX_VALUE = new BigDecimal("1");
  private static final int SCALE = 4; // At least 4 significant decimal digits

  /**
   * The decimal value for the measure must be between -1 and 1 with at least four decimal digits.
   */
  private final BigDecimal value;

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
    return value.setScale(SCALE, RoundingMode.HALF_UP).toPlainString();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    MeasureType that = (MeasureType) o;
    return value.equals(that.value);
  }

  @Override
  public int hashCode() {
    return value.hashCode();
  }
}