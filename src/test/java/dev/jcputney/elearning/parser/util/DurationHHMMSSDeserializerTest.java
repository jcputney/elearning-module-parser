/* Copyright (c) 2024-2025. Jonathan Putney
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

package dev.jcputney.elearning.parser.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.Duration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * Tests for {@link DurationHHMMSSDeserializer} to ensure proper parsing of duration strings in
 * various formats.
 */
class DurationHHMMSSDeserializerTest {

  /**
   * Tests that null or empty strings are parsed as Duration.ZERO.
   *
   * @param input the null or empty input string
   */
  @ParameterizedTest
  @NullAndEmptySource
  void parseDuration_nullOrEmpty_returnsZero(String input) {
    assertEquals(Duration.ZERO, DurationHHMMSSDeserializer.parseDuration(input));
  }

  @Test
  void parseDuration_semiColons_returnsZero() {
    assertEquals(Duration.ZERO, DurationHHMMSSDeserializer.parseDuration("::"));
  }

  /**
   * Tests that numeric values are interpreted as seconds.
   *
   * @param input the numeric input string
   * @param expectedSeconds the expected number of seconds
   */
  @ParameterizedTest
  @CsvSource({
      "0, 0",
      "1, 1",
      "60, 60",
      "3600, 3600",
      "123.45, 123"
  })
  void parseDuration_numericValue_returnsSeconds(String input, long expectedSeconds) {
    assertEquals(Duration.ofSeconds(expectedSeconds),
        DurationHHMMSSDeserializer.parseDuration(input));
  }

  /**
   * Tests parsing durations in HH:MM:SS format.
   *
   * @param input the HH:MM:SS format input string
   * @param expectedHours the expected hours component
   * @param expectedMinutes the expected minutes component
   * @param expectedSeconds the expected seconds component
   */
  @ParameterizedTest
  @CsvSource({
      "01:30:45, 1, 30, 45",
      "00:00:00, 0, 0, 0",
      "23:59:59, 23, 59, 59",
      "100:00:00, 100, 0, 0"
  })
  void parseDuration_hhmmssFormat_returnsDuration(
      String input, long expectedHours, long expectedMinutes, long expectedSeconds) {
    Duration expected = Duration.ofHours(expectedHours)
        .plusMinutes(expectedMinutes)
        .plusSeconds(expectedSeconds);
    assertEquals(expected, DurationHHMMSSDeserializer.parseDuration(input));
  }

  /**
   * Tests parsing durations in HH:MM format.
   *
   * @param input the HH:MM format input string
   * @param expectedHours the expected hours component
   * @param expectedMinutes the expected minutes component
   */
  @ParameterizedTest
  @CsvSource({
      "30:45, 30, 45",
      "00:00, 0, 0",
      "59:59, 59, 59",
      "100:00, 100, 0"
  })
  void parseDuration_hhmmFormat_returnsDuration(
      String input, long expectedHours, long expectedMinutes) {
    Duration expected = Duration.ofHours(expectedHours)
        .plusMinutes(expectedMinutes);
    assertEquals(expected, DurationHHMMSSDeserializer.parseDuration(input));
  }

  /**
   * Tests parsing durations in :MM format.
   *
   * @param input the :MM format input string
   * @param expectedMinutes the expected minutes
   */
  @ParameterizedTest
  @CsvSource({
      ":45, 45",
      ":00, 0",
      ":59, 59"
  })
  void parseDuration_minutesFormat_returnsDuration(String input, long expectedMinutes) {
    assertEquals(Duration.ofMinutes(expectedMinutes),
        DurationHHMMSSDeserializer.parseDuration(input));
  }

  /**
   * Tests that invalid formats throw an IllegalArgumentException.
   *
   * @param input the invalid format input string
   */
  @ParameterizedTest
  @ValueSource(strings = {
      "1:2:3:4",
      "abc",
      "1h2m3s",
      "01-30-45",
      "01/30/45"
  })
  void parseDuration_invalidFormat_throwsIllegalArgumentException(String input) {
    assertThrows(IllegalArgumentException.class,
        () -> DurationHHMMSSDeserializer.parseDuration(input));
  }

  /**
   * Tests that inputs with non-numeric components throw an IllegalArgumentException.
   *
   * @param input the input with non-numeric components
   */
  @ParameterizedTest
  @ValueSource(strings = {
      "aa:bb:cc",
      "1:a:3",
      "1:2:c"
  })
  void parseDuration_nonNumericComponents_throwsIllegalArgumentException(String input) {
    assertThrows(IllegalArgumentException.class,
        () -> DurationHHMMSSDeserializer.parseDuration(input));
  }

}
