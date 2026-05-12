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
package dev.jcputney.elearning.parser.input.scorm2004.ims.ss.types;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.math.RoundingMode;
import org.junit.jupiter.api.Test;

/**
 * Tests for the {@link MeasureType} class.
 */
public class MeasureTypeTest {

  @Test
  void defaultConstructor_createsZeroValue() {
    MeasureType measure = new MeasureType();
    assertEquals(BigDecimal.ZERO.setScale(4, RoundingMode.HALF_UP), measure.value());
    assertEquals("0.0000", measure.toString());
  }

  @Test
  void constructor_withValidValue_createsInstance() {
    BigDecimal value = new BigDecimal("0.5");
    MeasureType measure = new MeasureType(value);
    assertEquals(value.setScale(4, RoundingMode.HALF_UP), measure.value());
    assertEquals("0.5000", measure.toString());
  }

  @Test
  void constructor_withMinValue_createsInstance() {
    BigDecimal value = new BigDecimal("-1");
    MeasureType measure = new MeasureType(value);
    assertEquals(value.setScale(4, RoundingMode.HALF_UP), measure.value());
    assertEquals("-1.0000", measure.toString());
  }

  @Test
  void constructor_withMaxValue_createsInstance() {
    BigDecimal value = new BigDecimal("1");
    MeasureType measure = new MeasureType(value);
    assertEquals(value.setScale(4, RoundingMode.HALF_UP), measure.value());
    assertEquals("1.0000", measure.toString());
  }

  @Test
  void constructor_withNullValue_throwsException() {
    assertThrows(IllegalArgumentException.class, () -> new MeasureType(null));
  }

  @Test
  void constructor_withValueBelowMin_throwsException() {
    BigDecimal value = new BigDecimal("-1.1");
    assertThrows(IllegalArgumentException.class, () -> new MeasureType(value));
  }

  @Test
  void constructor_withValueAboveMax_throwsException() {
    BigDecimal value = new BigDecimal("1.1");
    assertThrows(IllegalArgumentException.class, () -> new MeasureType(value));
  }

  @Test
  void parse_withValidString_createsInstance() {
    MeasureType measure = MeasureType.parse("0.5");
    assertEquals(new BigDecimal("0.5").setScale(4, RoundingMode.HALF_UP), measure.value());
    assertEquals("0.5000", measure.toString());
  }

  @Test
  void parse_withInvalidString_throwsException() {
    assertThrows(IllegalArgumentException.class, () -> MeasureType.parse("not a number"));
  }

  @Test
  void parse_withValueBelowMin_throwsException() {
    assertThrows(IllegalArgumentException.class, () -> MeasureType.parse("-1.1"));
  }

  @Test
  void parse_withValueAboveMax_throwsException() {
    assertThrows(IllegalArgumentException.class, () -> MeasureType.parse("1.1"));
  }

  @Test
  void toString_returnsFormattedValue() {
    MeasureType measure = new MeasureType(new BigDecimal("0.5"));
    assertEquals("0.5000", measure.toString());
  }

  @Test
  void equals_withSameValue_returnsTrue() {
    MeasureType measure1 = new MeasureType(new BigDecimal("0.5"));
    MeasureType measure2 = new MeasureType(new BigDecimal("0.5"));
    assertEquals(measure1, measure2);
    assertEquals(measure1.hashCode(), measure2.hashCode());
  }

  @Test
  void equals_withDifferentValue_returnsFalse() {
    MeasureType measure1 = new MeasureType(new BigDecimal("0.5"));
    MeasureType measure2 = new MeasureType(new BigDecimal("0.6"));
    assertNotEquals(measure1, measure2);
    assertNotEquals(measure1.hashCode(), measure2.hashCode());
  }

  @Test
  void equals_withSameObject_returnsTrue() {
    MeasureType measure = new MeasureType(new BigDecimal("0.5"));
    assertEquals(measure, measure);
  }

  @Test
  void equals_withNull_returnsFalse() {
    MeasureType measure = new MeasureType(new BigDecimal("0.5"));
    assertNotEquals(null, measure);
  }

  @Test
  void equals_withDifferentClass_returnsFalse() {
    MeasureType measure = new MeasureType(new BigDecimal("0.5"));
    assertNotEquals("not a measure", measure);
  }

  @Test
  void constructor_withMoreThanFourDecimalPlaces_scalesCorrectly() {
    BigDecimal value = new BigDecimal("0.12345");
    MeasureType measure = new MeasureType(value);
    assertEquals(new BigDecimal("0.1235"), measure.value()); // Rounded up
    assertEquals("0.1235", measure.toString());
  }

  @Test
  void parse_withMoreThanFourDecimalPlaces_scalesCorrectly() {
    MeasureType measure = MeasureType.parse("0.12345");
    assertEquals(new BigDecimal("0.1235"), measure.value()); // Rounded up
    assertEquals("0.1235", measure.toString());
  }
}