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
package dev.jcputney.elearning.parser.input.common;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link PercentType} to ensure proper functionality of percentage value representation.
 */
class PercentTypeTest {

  @Test
  void defaultConstructor_shouldSetValueToZero() {
    // Act
    PercentType percentType = new PercentType();

    // Assert
    assertEquals(BigDecimal.ZERO, percentType.value());
  }

  @Test
  void constructor_withValidValue_shouldSetValue() {
    // Arrange
    BigDecimal value = new BigDecimal("0.5");

    // Act
    PercentType percentType = new PercentType(value);

    // Assert
    assertEquals(value, percentType.value());
  }

  @Test
  void constructor_withNullValue_shouldThrowIllegalArgumentException() {
    // Act & Assert
    IllegalArgumentException exception = assertThrows(
        IllegalArgumentException.class,
        () -> new PercentType(null)
    );
    assertEquals("Value cannot be null", exception.getMessage());
  }

  @Test
  void constructor_withValueLessThanZero_shouldThrowIllegalArgumentException() {
    // Arrange
    BigDecimal value = new BigDecimal("-0.1");

    // Act & Assert
    IllegalArgumentException exception = assertThrows(
        IllegalArgumentException.class,
        () -> new PercentType(value)
    );
    assertEquals("Value must be between 0 and 1, inclusive.", exception.getMessage());
  }

  @Test
  void constructor_withValueGreaterThanOne_shouldThrowIllegalArgumentException() {
    // Arrange
    BigDecimal value = new BigDecimal("1.1");

    // Act & Assert
    IllegalArgumentException exception = assertThrows(
        IllegalArgumentException.class,
        () -> new PercentType(value)
    );
    assertEquals("Value must be between 0 and 1, inclusive.", exception.getMessage());
  }

  @Test
  void constructor_withZeroValue_shouldAcceptValue() {
    // Arrange
    BigDecimal value = BigDecimal.ZERO;

    // Act
    PercentType percentType = new PercentType(value);

    // Assert
    assertEquals(value, percentType.value());
  }

  @Test
  void constructor_withOneValue_shouldAcceptValue() {
    // Arrange
    BigDecimal value = BigDecimal.ONE;

    // Act
    PercentType percentType = new PercentType(value);

    // Assert
    assertEquals(value, percentType.value());
  }

  @Test
  void parse_withValidString_shouldReturnPercentType() {
    // Arrange
    String value = "0.75";

    // Act
    PercentType percentType = PercentType.parse(value);

    // Assert
    assertEquals(new BigDecimal("0.75"), percentType.value());
  }

  @Test
  void parse_withInvalidString_shouldThrowIllegalArgumentException() {
    // Arrange
    String value = "invalid";

    // Act & Assert
    IllegalArgumentException exception = assertThrows(
        IllegalArgumentException.class,
        () -> PercentType.parse(value)
    );
    assertTrue(exception.getMessage().contains("Invalid percent format"));
  }

  @Test
  void parse_withOutOfRangeString_shouldThrowIllegalArgumentException() {
    // Arrange
    String value = "1.5";

    // Act & Assert
    IllegalArgumentException exception = assertThrows(
        IllegalArgumentException.class,
        () -> PercentType.parse(value)
    );
    assertEquals("Value must be between 0 and 1, inclusive.", exception.getMessage());
  }

  @Test
  void equals_withSameObject_shouldReturnTrue() {
    // Arrange
    PercentType percentType = new PercentType(new BigDecimal("0.5"));

    // Act & Assert
    assertEquals(percentType, percentType);
  }

  @Test
  void equals_withEqualObject_shouldReturnTrue() {
    // Arrange
    PercentType percentType1 = new PercentType(new BigDecimal("0.5"));
    PercentType percentType2 = new PercentType(new BigDecimal("0.5"));

    // Act & Assert
    assertEquals(percentType1, percentType2);
    assertEquals(percentType1.hashCode(), percentType2.hashCode());
  }

  @Test
  void equals_withDifferentObject_shouldReturnFalse() {
    // Arrange
    PercentType percentType1 = new PercentType(new BigDecimal("0.5"));
    PercentType percentType2 = new PercentType(new BigDecimal("0.6"));

    // Act & Assert
    assertNotEquals(percentType1, percentType2);
  }

  @Test
  void equals_withNull_shouldReturnFalse() {
    // Arrange
    PercentType percentType = new PercentType(new BigDecimal("0.5"));

    // Act & Assert
    assertNotEquals(null, percentType);
  }

  @Test
  void equals_withDifferentClass_shouldReturnFalse() {
    // Arrange
    PercentType percentType = new PercentType(new BigDecimal("0.5"));
    Object other = "not a PercentType";

    // Act & Assert
    assertNotEquals(percentType, other);
  }

  @Test
  void toString_shouldReturnStringRepresentation() {
    // Arrange
    PercentType percentType = new PercentType(new BigDecimal("0.5"));

    // Act
    String result = percentType.toString();

    // Assert
    assertEquals("0.5", result);
  }
}