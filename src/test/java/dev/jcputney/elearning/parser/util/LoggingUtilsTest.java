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

package dev.jcputney.elearning.parser.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;

/**
 * Tests for {@link LoggingUtils} to ensure proper logger creation.
 */
class LoggingUtilsTest {

  /**
   * Tests that getLogger returns a logger with the correct name.
   */
  @Test
  void getLogger_withClass_returnsLoggerWithCorrectName() {
    // Arrange
    Class<?> testClass = LoggingUtilsTest.class;

    // Act
    Logger logger = LoggingUtils.getLogger(testClass);

    // Assert
    assertNotNull(logger);
    assertEquals(testClass.getName(), logger.getName());
  }

  /**
   * Tests that getLogger returns different loggers for different classes.
   */
  @Test
  void getLogger_withDifferentClasses_returnsDifferentLoggers() {
    // Arrange
    Class<?> class1 = LoggingUtilsTest.class;
    Class<?> class2 = LoggingUtils.class;

    // Act
    Logger logger1 = LoggingUtils.getLogger(class1);
    Logger logger2 = LoggingUtils.getLogger(class2);

    // Assert
    assertNotNull(logger1);
    assertNotNull(logger2);
    assertEquals(class1.getName(), logger1.getName());
    assertEquals(class2.getName(), logger2.getName());
  }

  /**
   * Tests that getLogger returns the same logger when called multiple times with the same class.
   */
  @Test
  void getLogger_calledMultipleTimesWithSameClass_returnsSameLogger() {
    // Arrange
    Class<?> testClass = LoggingUtilsTest.class;

    // Act
    Logger logger1 = LoggingUtils.getLogger(testClass);
    Logger logger2 = LoggingUtils.getLogger(testClass);

    // Assert
    assertNotNull(logger1);
    assertNotNull(logger2);
    assertEquals(logger1, logger2);
  }

  /**
   * Tests that getLogger throws NullPointerException when called with null.
   */
  @Test
  void getLogger_withNullClass_throwsNullPointerException() {
    // Act & Assert
    org.junit.jupiter.api.Assertions.assertThrows(NullPointerException.class,
        () -> LoggingUtils.getLogger(null));
  }

  /**
   * Tests that getLogger works with anonymous inner classes.
   */
  @Test
  void getLogger_withAnonymousInnerClass_returnsLoggerWithCorrectName() {
    // Arrange
    Object anonymousObject = new Object() {
    };
    Class<?> anonymousClass = anonymousObject.getClass();

    // Act
    Logger logger = LoggingUtils.getLogger(anonymousClass);

    // Assert
    assertNotNull(logger);
    assertEquals(anonymousClass.getName(), logger.getName());
  }

  /**
   * Tests that getLogger works with interfaces.
   */
  @Test
  void getLogger_withInterface_returnsLoggerWithCorrectName() {
    // Arrange
    Class<?> interfaceClass = Runnable.class;

    // Act
    Logger logger = LoggingUtils.getLogger(interfaceClass);

    // Assert
    assertNotNull(logger);
    assertEquals(interfaceClass.getName(), logger.getName());
  }

  /**
   * Tests that getLogger works with enums.
   */
  @Test
  void getLogger_withEnum_returnsLoggerWithCorrectName() {
    // Arrange
    Class<?> enumClass = java.util.concurrent.TimeUnit.class;

    // Act
    Logger logger = LoggingUtils.getLogger(enumClass);

    // Assert
    assertNotNull(logger);
    assertEquals(enumClass.getName(), logger.getName());
  }

  /**
   * Tests that getLogger works with array types.
   */
  @Test
  void getLogger_withArrayType_returnsLoggerWithCorrectName() {
    // Arrange
    Class<?> arrayClass = String[].class;

    // Act
    Logger logger = LoggingUtils.getLogger(arrayClass);

    // Assert
    assertNotNull(logger);
    assertEquals(arrayClass.getName(), logger.getName());
  }

  /**
   * Tests that LoggingUtils has a private constructor.
   */
  @Test
  void constructor_isPrivate() {
    // Arrange
    java.lang.reflect.Constructor<LoggingUtils> constructor;
    try {
      constructor = LoggingUtils.class.getDeclaredConstructor();
    } catch (NoSuchMethodException e) {
      org.junit.jupiter.api.Assertions.fail("Constructor not found");
      return;
    }

    // Act & Assert
    org.junit.jupiter.api.Assertions.assertTrue(
        java.lang.reflect.Modifier.isPrivate(constructor.getModifiers()),
        "Constructor should be private");
  }
}
