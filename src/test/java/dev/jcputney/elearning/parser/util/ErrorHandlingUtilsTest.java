/*
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
 */

package dev.jcputney.elearning.parser.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import dev.jcputney.elearning.parser.exception.FileAccessException;
import dev.jcputney.elearning.parser.exception.ManifestParseException;
import dev.jcputney.elearning.parser.exception.ModuleDetectionException;
import dev.jcputney.elearning.parser.exception.ModuleException;
import dev.jcputney.elearning.parser.exception.ModuleParsingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;

/**
 * Unit tests for {@link ErrorHandlingUtils}.
 */
public class ErrorHandlingUtilsTest {

  /**
   * Tests creating a ModuleParsingException with a message, cause, and metadata.
   */
  @Test
  void createParsingException_withMessageCauseAndMetadata_returnsExceptionWithAllParameters() {
    // Arrange
    String message = "Test parsing error";
    Throwable cause = new RuntimeException("Original cause");
    Map<String, Object> metadata = new HashMap<>();
    metadata.put("key1", "value1");
    metadata.put("key2", 42);

    // Act
    ModuleParsingException exception = ErrorHandlingUtils.createParsingException(message, cause,
        metadata);

    // Assert
    assertNotNull(exception);
    assertEquals(message, exception.getMessage());
    assertEquals(cause, exception.getCause());
    assertEquals(metadata, exception.getMetadata());
  }

  /**
   * Tests creating a ModuleParsingException with a message and cause.
   */
  @Test
  void createParsingException_withMessageAndCause_returnsExceptionWithMessageAndCause() {
    // Arrange
    String message = "Test parsing error";
    Throwable cause = new RuntimeException("Original cause");

    // Act
    ModuleParsingException exception = ErrorHandlingUtils.createParsingException(message, cause);

    // Assert
    assertNotNull(exception);
    assertEquals(message, exception.getMessage());
    assertEquals(cause, exception.getCause());
    assertTrue(exception.getMetadata().isEmpty());
  }

  /**
   * Tests creating a ModuleParsingException with only a message.
   */
  @Test
  void createParsingException_withMessageOnly_returnsExceptionWithMessage() {
    // Arrange
    String message = "Test parsing error";

    // Act
    ModuleParsingException exception = ErrorHandlingUtils.createParsingException(message);

    // Assert
    assertNotNull(exception);
    assertEquals(message, exception.getMessage());
    assertSame(null, exception.getCause());
    assertTrue(exception.getMetadata().isEmpty());
  }

  /**
   * Tests creating a ModuleDetectionException with a message, cause, and metadata.
   */
  @Test
  void createDetectionException_withMessageCauseAndMetadata_returnsExceptionWithAllParameters() {
    // Arrange
    String message = "Test detection error";
    Throwable cause = new RuntimeException("Original cause");
    Map<String, Object> metadata = new HashMap<>();
    metadata.put("key1", "value1");
    metadata.put("key2", 42);

    // Act
    ModuleDetectionException exception = ErrorHandlingUtils.createDetectionException(message, cause,
        metadata);

    // Assert
    assertNotNull(exception);
    assertEquals(message, exception.getMessage());
    assertEquals(cause, exception.getCause());
    assertEquals(metadata, exception.getMetadata());
  }

  /**
   * Tests creating a ModuleDetectionException with a message and cause.
   */
  @Test
  void createDetectionException_withMessageAndCause_returnsExceptionWithMessageAndCause() {
    // Arrange
    String message = "Test detection error";
    Throwable cause = new RuntimeException("Original cause");

    // Act
    ModuleDetectionException exception = ErrorHandlingUtils.createDetectionException(message,
        cause);

    // Assert
    assertNotNull(exception);
    assertEquals(message, exception.getMessage());
    assertEquals(cause, exception.getCause());
    assertTrue(exception.getMetadata().isEmpty());
  }

  /**
   * Tests creating a ModuleDetectionException with only a message.
   */
  @Test
  void createDetectionException_withMessageOnly_returnsExceptionWithMessage() {
    // Arrange
    String message = "Test detection error";

    // Act
    ModuleDetectionException exception = ErrorHandlingUtils.createDetectionException(message);

    // Assert
    assertNotNull(exception);
    assertEquals(message, exception.getMessage());
    assertSame(null, exception.getCause());
    assertTrue(exception.getMetadata().isEmpty());
  }

  /**
   * Tests creating a ManifestParseException with a message, cause, and metadata.
   */
  @Test
  void createManifestException_withMessageCauseAndMetadata_returnsExceptionWithAllParameters() {
    // Arrange
    String message = "Test manifest error";
    Throwable cause = new RuntimeException("Original cause");
    Map<String, Object> metadata = new HashMap<>();
    metadata.put("key1", "value1");
    metadata.put("key2", 42);

    // Act
    ManifestParseException exception = ErrorHandlingUtils.createManifestException(message, cause,
        metadata);

    // Assert
    assertNotNull(exception);
    assertEquals(message, exception.getMessage());
    assertEquals(cause, exception.getCause());
    assertEquals(metadata, exception.getMetadata());
  }

  /**
   * Tests creating a ManifestParseException with a message and cause.
   */
  @Test
  void createManifestException_withMessageAndCause_returnsExceptionWithMessageAndCause() {
    // Arrange
    String message = "Test manifest error";
    Throwable cause = new RuntimeException("Original cause");

    // Act
    ManifestParseException exception = ErrorHandlingUtils.createManifestException(message, cause);

    // Assert
    assertNotNull(exception);
    assertEquals(message, exception.getMessage());
    assertEquals(cause, exception.getCause());
    assertTrue(exception.getMetadata().isEmpty());
  }

  /**
   * Tests creating a ManifestParseException with only a message.
   */
  @Test
  void createManifestException_withMessageOnly_returnsExceptionWithMessage() {
    // Arrange
    String message = "Test manifest error";

    // Act
    ManifestParseException exception = ErrorHandlingUtils.createManifestException(message);

    // Assert
    assertNotNull(exception);
    assertEquals(message, exception.getMessage());
    assertSame(null, exception.getCause());
    assertTrue(exception.getMetadata().isEmpty());
  }

  /**
   * Tests creating a FileAccessException with a message, cause, and metadata.
   */
  @Test
  void createFileAccessException_withMessageCauseAndMetadata_returnsExceptionWithAllParameters() {
    // Arrange
    String message = "Test file access error";
    Throwable cause = new RuntimeException("Original cause");
    Map<String, Object> metadata = new HashMap<>();
    metadata.put("key1", "value1");
    metadata.put("key2", 42);

    // Act
    FileAccessException exception = ErrorHandlingUtils.createFileAccessException(message, cause,
        metadata);

    // Assert
    assertNotNull(exception);
    assertEquals(message, exception.getMessage());
    assertEquals(cause, exception.getCause());
    assertEquals(metadata, exception.getMetadata());
  }

  /**
   * Tests creating a FileAccessException with a message and cause.
   */
  @Test
  void createFileAccessException_withMessageAndCause_returnsExceptionWithMessageAndCause() {
    // Arrange
    String message = "Test file access error";
    Throwable cause = new RuntimeException("Original cause");

    // Act
    FileAccessException exception = ErrorHandlingUtils.createFileAccessException(message, cause);

    // Assert
    assertNotNull(exception);
    assertEquals(message, exception.getMessage());
    assertEquals(cause, exception.getCause());
    assertTrue(exception.getMetadata().isEmpty());
  }

  /**
   * Tests creating a FileAccessException with only a message.
   */
  @Test
  void createFileAccessException_withMessageOnly_returnsExceptionWithMessage() {
    // Arrange
    String message = "Test file access error";

    // Act
    FileAccessException exception = ErrorHandlingUtils.createFileAccessException(message);

    // Assert
    assertNotNull(exception);
    assertEquals(message, exception.getMessage());
    assertSame(null, exception.getCause());
    assertTrue(exception.getMetadata().isEmpty());
  }

  /**
   * Tests executing a supplier that returns a value successfully.
   */
  @Test
  void executeWithParsingExceptionHandling_withSuccessfulSupplier_returnsValue()
      throws ModuleParsingException {
    // Arrange
    Supplier<String> supplier = () -> "success";
    String errorMessage = "Error executing supplier";
    Map<String, Object> contextData = new HashMap<>();
    contextData.put("context", "test");

    // Act
    String result = ErrorHandlingUtils.executeWithParsingExceptionHandling(supplier, null,
        errorMessage, contextData);

    // Assert
    assertEquals("success", result);
  }

  /**
   * Tests executing a supplier that throws an exception.
   */
  @Test
  void executeWithParsingExceptionHandling_withExceptionThrowingSupplier_wrapsException() {
    // Arrange
    Supplier<String> supplier = () -> {
      throw new RuntimeException("Test exception");
    };
    String errorMessage = "Error executing supplier";
    Logger logger = LoggerFactory.getLogger(ErrorHandlingUtilsTest.class);

    // Act & Assert
    ModuleParsingException exception = assertThrows(ModuleParsingException.class, () ->
        ErrorHandlingUtils.executeWithParsingExceptionHandling(supplier, logger, errorMessage,
            null));

    // Verify the exception has the expected message and cause
    assertEquals(errorMessage, exception.getMessage());
    assertEquals("Test exception", exception.getCause().getMessage());
  }

  /**
   * Tests executing a supplier that returns a value successfully with detection exception
   * handling.
   */
  @Test
  void executeWithDetectionExceptionHandling_withSuccessfulSupplier_returnsValue()
      throws ModuleDetectionException {
    // Arrange
    Supplier<String> supplier = () -> "success";
    String errorMessage = "Error executing supplier";
    Map<String, Object> contextData = new HashMap<>();
    contextData.put("context", "test");

    // Act
    String result = ErrorHandlingUtils.executeWithDetectionExceptionHandling(supplier, null,
        errorMessage, contextData);

    // Assert
    assertEquals("success", result);
  }

  /**
   * Tests executing a supplier that throws an exception with detection exception handling.
   */
  @Test
  void executeWithDetectionExceptionHandling_withExceptionThrowingSupplier_wrapsException() {
    // Arrange
    Supplier<String> supplier = () -> {
      throw new RuntimeException("Test exception");
    };
    String errorMessage = "Error executing supplier";
    Logger logger = LoggerFactory.getLogger(ErrorHandlingUtilsTest.class);

    // Act & Assert
    ModuleDetectionException exception = assertThrows(ModuleDetectionException.class, () ->
        ErrorHandlingUtils.executeWithDetectionExceptionHandling(supplier, logger, errorMessage,
            null));

    // Verify the exception has the expected message and cause
    assertEquals(errorMessage, exception.getMessage());
    assertEquals("Test exception", exception.getCause().getMessage());
  }

  /**
   * Tests executing a supplier that returns a value successfully with file access exception
   * handling.
   */
  @Test
  void executeWithFileAccessExceptionHandling_withSuccessfulSupplier_returnsValue()
      throws FileAccessException {
    // Arrange
    Supplier<String> supplier = () -> "success";
    String errorMessage = "Error executing supplier";
    Map<String, Object> contextData = new HashMap<>();
    contextData.put("context", "test");

    // Act
    String result = ErrorHandlingUtils.executeWithFileAccessExceptionHandling(supplier, null,
        errorMessage, contextData);

    // Assert
    assertEquals("success", result);
  }

  /**
   * Tests executing a supplier that throws an exception with file access exception handling.
   */
  @Test
  void executeWithFileAccessExceptionHandling_withExceptionThrowingSupplier_wrapsException() {
    // Arrange
    Supplier<String> supplier = () -> {
      throw new RuntimeException("Test exception");
    };
    String errorMessage = "Error executing supplier";
    Logger logger = LoggerFactory.getLogger(ErrorHandlingUtilsTest.class);

    // Act & Assert
    FileAccessException exception = assertThrows(FileAccessException.class, () ->
        ErrorHandlingUtils.executeWithFileAccessExceptionHandling(supplier, logger, errorMessage,
            null));

    // Verify the exception has the expected message and cause
    assertEquals(errorMessage, exception.getMessage());
    assertEquals("Test exception", exception.getCause().getMessage());
  }

  /**
   * Tests the logException method with a ModuleException that has metadata.
   */
  @Test
  void logException_withModuleExceptionWithMetadata_logsMessageWithMetadata() {
    // Arrange
    TestLogger logger = new TestLogger();
    ModuleParsingException exception = new ModuleParsingException("Test exception");
    exception.addMetadata("key1", "value1");
    exception.addMetadata("key2", 42);
    String message = "Error occurred";

    // Act
    ErrorHandlingUtils.logException(logger, exception, message);

    // Assert
    assertEquals(1, logger.getErrorMessages().size());
    assertEquals(1, logger.getDebugMessages().size());
    assertEquals(1, logger.getDebugThrowables().size());
    assertTrue(logger.getErrorMessages().get(0).contains(message));
    assertTrue(logger.getErrorMessages().get(0).contains("Test exception"));
    assertTrue(logger.getErrorMessages().get(0).contains("Metadata"));
    assertTrue(logger.getErrorMessages().get(0).contains("key1"));
    assertTrue(logger.getErrorMessages().get(0).contains("value1"));
    assertTrue(logger.getErrorMessages().get(0).contains("key2"));
    assertTrue(logger.getErrorMessages().get(0).contains("42"));
    assertEquals("Exception details:", logger.getDebugMessages().get(0));
    assertEquals(exception, logger.getDebugThrowables().get(0));
  }

  /**
   * Tests the logException method with a ModuleException that has no metadata.
   */
  @Test
  void logException_withModuleExceptionWithoutMetadata_logsMessageWithoutMetadata() {
    // Arrange
    TestLogger logger = new TestLogger();
    ModuleException exception = new ModuleParsingException("Test exception");
    String message = "Error occurred";

    // Act
    ErrorHandlingUtils.logException(logger, exception, message);

    // Assert
    assertEquals(1, logger.getErrorMessages().size());
    assertEquals(1, logger.getDebugMessages().size());
    assertEquals(1, logger.getDebugThrowables().size());
    assertTrue(logger.getErrorMessages().get(0).contains(message));
    assertTrue(logger.getErrorMessages().get(0).contains("Test exception"));
    assertFalse(logger.getErrorMessages().get(0).contains("Metadata"));
    assertEquals("Exception details:", logger.getDebugMessages().get(0));
    assertEquals(exception, logger.getDebugThrowables().get(0));
  }

  /**
   * Tests the logException method with a non-ModuleException.
   */
  @Test
  void logException_withNonModuleException_logsMessage() {
    // Arrange
    TestLogger logger = new TestLogger();
    RuntimeException exception = new RuntimeException("Test exception");
    String message = "Error occurred";

    // Act
    ErrorHandlingUtils.logException(logger, exception, message);

    // Assert
    assertEquals(1, logger.getErrorMessages().size());
    assertEquals(1, logger.getDebugMessages().size());
    assertEquals(1, logger.getDebugThrowables().size());
    assertTrue(logger.getErrorMessages().get(0).contains(message));
    assertTrue(logger.getErrorMessages().get(0).contains("Test exception"));
    assertFalse(logger.getErrorMessages().get(0).contains("Metadata"));
    assertEquals("Exception details:", logger.getDebugMessages().get(0));
    assertEquals(exception, logger.getDebugThrowables().get(0));
  }

  /**
   * Tests executing a supplier with parsing exception handling when the supplier throws a
   * ModuleParsingException.
   */
  @Test
  void executeWithParsingExceptionHandling_withModuleParsingException_preservesException() {
    // Arrange
    ModuleParsingException originalException = new ModuleParsingException("Original exception");
    originalException.addMetadata("key1", "value1");

    // Create a supplier that throws a RuntimeException with the original exception as the cause
    Supplier<String> supplier = () -> {
      throw new RuntimeException(originalException);
    };

    TestLogger logger = new TestLogger();
    String errorMessage = "Error executing supplier";
    Map<String, Object> contextData = new HashMap<>();
    contextData.put("context", "test");

    // Act & Assert
    ModuleParsingException exception = assertThrows(ModuleParsingException.class, () ->
        ErrorHandlingUtils.executeWithParsingExceptionHandling(supplier, logger, errorMessage,
            contextData));

    // Verify that a new exception was created with the expected properties
    assertEquals(errorMessage, exception.getMessage());

    // The original exception is the cause of the RuntimeException, which is the cause of the new exception
    Throwable cause = exception.getCause();
    assertInstanceOf(RuntimeException.class, cause);
    assertEquals(originalException, cause.getCause());

    // Verify the metadata
    assertTrue(exception.getMetadata().containsKey("context"));
    assertEquals("test", exception.getMetadata().get("context"));

    // Verify logging
    assertEquals(1, logger.getErrorMessages().size());
    assertEquals(1, logger.getDebugMessages().size());
    assertEquals(1, logger.getDebugThrowables().size());
  }

  /**
   * Tests executing a supplier with detection exception handling when the supplier throws a
   * ModuleDetectionException.
   */
  @Test
  void executeWithDetectionExceptionHandling_withModuleDetectionException_preservesException() {
    // Arrange
    ModuleDetectionException originalException = new ModuleDetectionException("Original exception");
    originalException.addMetadata("key1", "value1");

    // Create a supplier that throws a RuntimeException with the original exception as the cause
    Supplier<String> supplier = () -> {
      throw new RuntimeException(originalException);
    };

    TestLogger logger = new TestLogger();
    String errorMessage = "Error executing supplier";
    Map<String, Object> contextData = new HashMap<>();
    contextData.put("context", "test");

    // Act & Assert
    ModuleDetectionException exception = assertThrows(ModuleDetectionException.class, () ->
        ErrorHandlingUtils.executeWithDetectionExceptionHandling(supplier, logger, errorMessage,
            contextData));

    // Verify that a new exception was created with the expected properties
    assertEquals(errorMessage, exception.getMessage());

    // The original exception is the cause of the RuntimeException, which is the cause of the new exception
    Throwable cause = exception.getCause();
    assertInstanceOf(RuntimeException.class, cause);
    assertEquals(originalException, cause.getCause());

    // Verify the metadata
    assertTrue(exception.getMetadata().containsKey("context"));
    assertEquals("test", exception.getMetadata().get("context"));

    // Verify logging
    assertEquals(1, logger.getErrorMessages().size());
    assertEquals(1, logger.getDebugMessages().size());
    assertEquals(1, logger.getDebugThrowables().size());
  }

  /**
   * Tests executing a supplier with file access exception handling when the supplier throws a
   * FileAccessException.
   */
  @Test
  void executeWithFileAccessExceptionHandling_withFileAccessException_preservesException() {
    // Arrange
    FileAccessException originalException = new FileAccessException("Original exception");
    originalException.addMetadata("key1", "value1");

    // Create a supplier that throws a RuntimeException with the original exception as the cause
    Supplier<String> supplier = () -> {
      throw new RuntimeException(originalException);
    };

    TestLogger logger = new TestLogger();
    String errorMessage = "Error executing supplier";
    Map<String, Object> contextData = new HashMap<>();
    contextData.put("context", "test");

    // Act & Assert
    FileAccessException exception = assertThrows(FileAccessException.class, () ->
        ErrorHandlingUtils.executeWithFileAccessExceptionHandling(supplier, logger, errorMessage,
            contextData));

    // Verify that a new exception was created with the expected properties
    assertEquals(errorMessage, exception.getMessage());

    // The original exception is the cause of the RuntimeException, which is the cause of the new exception
    Throwable cause = exception.getCause();
    assertInstanceOf(RuntimeException.class, cause);
    assertEquals(originalException, cause.getCause());

    // Verify the metadata
    assertTrue(exception.getMetadata().containsKey("context"));
    assertEquals("test", exception.getMetadata().get("context"));

    // Verify logging
    assertEquals(1, logger.getErrorMessages().size());
    assertEquals(1, logger.getDebugMessages().size());
    assertEquals(1, logger.getDebugThrowables().size());
  }

  /**
   * Tests executing a supplier with parsing exception handling when the supplier throws a
   * ModuleException that is not a ModuleParsingException.
   */
  @Test
  void executeWithParsingExceptionHandling_withOtherModuleException_wrapsException() {
    // Arrange
    ModuleDetectionException originalException = new ModuleDetectionException("Original exception");
    originalException.addMetadata("key1", "value1");

    // Create a supplier that throws a RuntimeException with the original exception as the cause
    Supplier<String> supplier = () -> {
      throw new RuntimeException(originalException);
    };

    TestLogger logger = new TestLogger();
    String errorMessage = "Error executing supplier";
    Map<String, Object> contextData = new HashMap<>();
    contextData.put("context", "test");

    // Act & Assert
    ModuleParsingException exception = assertThrows(ModuleParsingException.class, () ->
        ErrorHandlingUtils.executeWithParsingExceptionHandling(supplier, logger, errorMessage,
            contextData));

    // Verify that a new exception was created with the expected properties
    assertEquals(errorMessage, exception.getMessage());

    // The original exception is the cause of the RuntimeException, which is the cause of the new exception
    Throwable cause = exception.getCause();
    assertInstanceOf(RuntimeException.class, cause);
    assertEquals(originalException, cause.getCause());

    // Verify the metadata
    assertTrue(exception.getMetadata().containsKey("context"));
    assertEquals("test", exception.getMetadata().get("context"));

    // Verify logging
    assertEquals(1, logger.getErrorMessages().size());
    assertEquals(1, logger.getDebugMessages().size());
    assertEquals(1, logger.getDebugThrowables().size());
  }

  /**
   * Tests executing a supplier with parsing exception handling when the supplier directly throws a
   * ModuleException.
   */
  @Test
  void executeWithParsingExceptionHandling_withDirectModuleException_wrapsException() {
    // Arrange
    ModuleException originalException = new ModuleException("Original exception");
    originalException.addMetadata("key1", "value1");

    // Create a supplier that throws a ModuleException directly
    Supplier<String> supplier = () -> {
      throw new RuntimeException(originalException);
    };

    TestLogger logger = new TestLogger();
    String errorMessage = "Error executing supplier";
    Map<String, Object> contextData = new HashMap<>();
    contextData.put("context", "test");

    // Act & Assert
    ModuleParsingException exception = assertThrows(ModuleParsingException.class, () ->
        ErrorHandlingUtils.executeWithParsingExceptionHandling(supplier, logger, errorMessage,
            contextData));

    // Verify that a new exception was created with the expected properties
    assertEquals(errorMessage, exception.getMessage());

    // The original exception is the cause of the RuntimeException, which is the cause of the new exception
    Throwable cause = exception.getCause();
    assertInstanceOf(RuntimeException.class, cause);
    assertEquals(originalException, cause.getCause());

    // Verify the metadata
    assertTrue(exception.getMetadata().containsKey("context"));
    assertEquals("test", exception.getMetadata().get("context"));

    // Verify logging
    assertEquals(1, logger.getErrorMessages().size());
    assertEquals(1, logger.getDebugMessages().size());
    assertEquals(1, logger.getDebugThrowables().size());
  }

  /**
   * Tests executing a supplier with parsing exception handling when the supplier directly throws a
   * ModuleParsingException.
   */
  @Test
  void executeWithParsingExceptionHandling_withDirectModuleParsingException_preservesException() {
    // Arrange
    ModuleParsingException originalException = new ModuleParsingException("Original exception");
    originalException.addMetadata("key1", "value1");

    // Create a supplier that throws a ModuleParsingException directly
    Supplier<String> supplier = () -> {
      throw new RuntimeException(originalException);
    };

    TestLogger logger = new TestLogger();
    String errorMessage = "Error executing supplier";
    Map<String, Object> contextData = new HashMap<>();
    contextData.put("context", "test");

    // Act & Assert
    ModuleParsingException exception = assertThrows(ModuleParsingException.class, () ->
        ErrorHandlingUtils.executeWithParsingExceptionHandling(supplier, logger, errorMessage,
            contextData));

    // Verify that a new exception was created with the expected properties
    assertEquals(errorMessage, exception.getMessage());

    // The original exception is the cause of the RuntimeException, which is the cause of the new exception
    Throwable cause = exception.getCause();
    assertInstanceOf(RuntimeException.class, cause);
    assertEquals(originalException, cause.getCause());

    // Verify the metadata
    assertTrue(exception.getMetadata().containsKey("context"));
    assertEquals("test", exception.getMetadata().get("context"));

    // Verify logging
    assertEquals(1, logger.getErrorMessages().size());
    assertEquals(1, logger.getDebugMessages().size());
    assertEquals(1, logger.getDebugThrowables().size());
  }

  /**
   * Tests executing a supplier with detection exception handling when the supplier directly throws
   * a ModuleException.
   */
  @Test
  void executeWithDetectionExceptionHandling_withDirectModuleException_wrapsException() {
    // Arrange
    ModuleException originalException = new ModuleException("Original exception");
    originalException.addMetadata("key1", "value1");

    // Create a supplier that throws a ModuleException directly
    Supplier<String> supplier = () -> {
      throw new RuntimeException(originalException);
    };

    TestLogger logger = new TestLogger();
    String errorMessage = "Error executing supplier";
    Map<String, Object> contextData = new HashMap<>();
    contextData.put("context", "test");

    // Act & Assert
    ModuleDetectionException exception = assertThrows(ModuleDetectionException.class, () ->
        ErrorHandlingUtils.executeWithDetectionExceptionHandling(supplier, logger, errorMessage,
            contextData));

    // Verify that a new exception was created with the expected properties
    assertEquals(errorMessage, exception.getMessage());

    // The original exception is the cause of the RuntimeException, which is the cause of the new exception
    Throwable cause = exception.getCause();
    assertInstanceOf(RuntimeException.class, cause);
    assertEquals(originalException, cause.getCause());

    // Verify the metadata
    assertTrue(exception.getMetadata().containsKey("context"));
    assertEquals("test", exception.getMetadata().get("context"));

    // Verify logging
    assertEquals(1, logger.getErrorMessages().size());
    assertEquals(1, logger.getDebugMessages().size());
    assertEquals(1, logger.getDebugThrowables().size());
  }

  /**
   * Tests executing a supplier with detection exception handling when the supplier directly throws
   * a ModuleDetectionException.
   */
  @Test
  void executeWithDetectionExceptionHandling_withDirectModuleDetectionException_preservesException() {
    // Arrange
    ModuleDetectionException originalException = new ModuleDetectionException("Original exception");
    originalException.addMetadata("key1", "value1");

    // Create a supplier that throws a ModuleDetectionException directly
    Supplier<String> supplier = () -> {
      throw new RuntimeException(originalException);
    };

    TestLogger logger = new TestLogger();
    String errorMessage = "Error executing supplier";
    Map<String, Object> contextData = new HashMap<>();
    contextData.put("context", "test");

    // Act & Assert
    ModuleDetectionException exception = assertThrows(ModuleDetectionException.class, () ->
        ErrorHandlingUtils.executeWithDetectionExceptionHandling(supplier, logger, errorMessage,
            contextData));

    // Verify that a new exception was created with the expected properties
    assertEquals(errorMessage, exception.getMessage());

    // The original exception is the cause of the RuntimeException, which is the cause of the new exception
    Throwable cause = exception.getCause();
    assertInstanceOf(RuntimeException.class, cause);
    assertEquals(originalException, cause.getCause());

    // Verify the metadata
    assertTrue(exception.getMetadata().containsKey("context"));
    assertEquals("test", exception.getMetadata().get("context"));

    // Verify logging
    assertEquals(1, logger.getErrorMessages().size());
    assertEquals(1, logger.getDebugMessages().size());
    assertEquals(1, logger.getDebugThrowables().size());
  }

  /**
   * Tests executing a supplier with file access exception handling when the supplier directly
   * throws a ModuleException.
   */
  @Test
  void executeWithFileAccessExceptionHandling_withDirectModuleException_wrapsException() {
    // Arrange
    ModuleException originalException = new ModuleException("Original exception");
    originalException.addMetadata("key1", "value1");

    // Create a supplier that throws a ModuleException directly
    Supplier<String> supplier = () -> {
      throw new RuntimeException(originalException);
    };

    TestLogger logger = new TestLogger();
    String errorMessage = "Error executing supplier";
    Map<String, Object> contextData = new HashMap<>();
    contextData.put("context", "test");

    // Act & Assert
    FileAccessException exception = assertThrows(FileAccessException.class, () ->
        ErrorHandlingUtils.executeWithFileAccessExceptionHandling(supplier, logger, errorMessage,
            contextData));

    // Verify that a new exception was created with the expected properties
    assertEquals(errorMessage, exception.getMessage());

    // The original exception is the cause of the RuntimeException, which is the cause of the new exception
    Throwable cause = exception.getCause();
    assertInstanceOf(RuntimeException.class, cause);
    assertEquals(originalException, cause.getCause());

    // Verify the metadata
    assertTrue(exception.getMetadata().containsKey("context"));
    assertEquals("test", exception.getMetadata().get("context"));

    // Verify logging
    assertEquals(1, logger.getErrorMessages().size());
    assertEquals(1, logger.getDebugMessages().size());
    assertEquals(1, logger.getDebugThrowables().size());
  }

  /**
   * Tests executing a supplier with file access exception handling when the supplier directly
   * throws a FileAccessException.
   */
  @Test
  void executeWithFileAccessExceptionHandling_withDirectFileAccessException_preservesException() {
    // Arrange
    FileAccessException originalException = new FileAccessException("Original exception");
    originalException.addMetadata("key1", "value1");

    // Create a supplier that throws a FileAccessException directly
    Supplier<String> supplier = () -> {
      throw new RuntimeException(originalException);
    };

    TestLogger logger = new TestLogger();
    String errorMessage = "Error executing supplier";
    Map<String, Object> contextData = new HashMap<>();
    contextData.put("context", "test");

    // Act & Assert
    FileAccessException exception = assertThrows(FileAccessException.class, () ->
        ErrorHandlingUtils.executeWithFileAccessExceptionHandling(supplier, logger, errorMessage,
            contextData));

    // Verify that a new exception was created with the expected properties
    assertEquals(errorMessage, exception.getMessage());

    // The original exception is the cause of the RuntimeException, which is the cause of the new exception
    Throwable cause = exception.getCause();
    assertInstanceOf(RuntimeException.class, cause);
    assertEquals(originalException, cause.getCause());

    // Verify the metadata
    assertTrue(exception.getMetadata().containsKey("context"));
    assertEquals("test", exception.getMetadata().get("context"));

    // Verify logging
    assertEquals(1, logger.getErrorMessages().size());
    assertEquals(1, logger.getDebugMessages().size());
    assertEquals(1, logger.getDebugThrowables().size());
  }

  /**
   * A test implementation of Logger that records log messages.
   */
  private static class TestLogger implements Logger {

    private final List<String> errorMessages = new ArrayList<>();
    private final List<String> debugMessages = new ArrayList<>();
    private final List<Throwable> debugThrowables = new ArrayList<>();

    public List<String> getErrorMessages() {
      return errorMessages;
    }

    public List<String> getDebugMessages() {
      return debugMessages;
    }

    public List<Throwable> getDebugThrowables() {
      return debugThrowables;
    }

    @Override
    public String getName() {
      return "TestLogger";
    }

    @Override
    public boolean isTraceEnabled() {
      return false;
    }

    @Override
    public void trace(String msg) {
    }

    @Override
    public void trace(String format, Object arg) {
    }

    @Override
    public void trace(String format, Object arg1, Object arg2) {
    }

    @Override
    public void trace(String format, Object... arguments) {
    }

    @Override
    public void trace(String msg, Throwable t) {
    }

    @Override
    public boolean isTraceEnabled(Marker marker) {
      return false;
    }

    @Override
    public void trace(Marker marker, String msg) {
    }

    @Override
    public void trace(Marker marker, String format, Object arg) {
    }

    @Override
    public void trace(Marker marker, String format, Object arg1, Object arg2) {
    }

    @Override
    public void trace(Marker marker, String format, Object... argArray) {
    }

    @Override
    public void trace(Marker marker, String msg, Throwable t) {
    }

    @Override
    public boolean isDebugEnabled() {
      return true;
    }

    @Override
    public void debug(String msg) {
      debugMessages.add(msg);
    }

    @Override
    public void debug(String format, Object arg) {
      debugMessages.add(String.format(format, arg));
    }

    @Override
    public void debug(String format, Object arg1, Object arg2) {
      debugMessages.add(String.format(format, arg1, arg2));
    }

    @Override
    public void debug(String format, Object... arguments) {
      debugMessages.add(String.format(format, arguments));
    }

    @Override
    public void debug(String msg, Throwable t) {
      debugMessages.add(msg);
      debugThrowables.add(t);
    }

    @Override
    public boolean isDebugEnabled(Marker marker) {
      return true;
    }

    @Override
    public void debug(Marker marker, String msg) {
      debug(msg);
    }

    @Override
    public void debug(Marker marker, String format, Object arg) {
      debug(format, arg);
    }

    @Override
    public void debug(Marker marker, String format, Object arg1, Object arg2) {
      debug(format, arg1, arg2);
    }

    @Override
    public void debug(Marker marker, String format, Object... arguments) {
      debug(format, arguments);
    }

    @Override
    public void debug(Marker marker, String msg, Throwable t) {
      debug(msg, t);
    }

    @Override
    public boolean isInfoEnabled() {
      return false;
    }

    @Override
    public void info(String msg) {
    }

    @Override
    public void info(String format, Object arg) {
    }

    @Override
    public void info(String format, Object arg1, Object arg2) {
    }

    @Override
    public void info(String format, Object... arguments) {
    }

    @Override
    public void info(String msg, Throwable t) {
    }

    @Override
    public boolean isInfoEnabled(Marker marker) {
      return false;
    }

    @Override
    public void info(Marker marker, String msg) {
    }

    @Override
    public void info(Marker marker, String format, Object arg) {
    }

    @Override
    public void info(Marker marker, String format, Object arg1, Object arg2) {
    }

    @Override
    public void info(Marker marker, String format, Object... arguments) {
    }

    @Override
    public void info(Marker marker, String msg, Throwable t) {
    }

    @Override
    public boolean isWarnEnabled() {
      return false;
    }

    @Override
    public void warn(String msg) {
    }

    @Override
    public void warn(String format, Object arg) {
    }

    @Override
    public void warn(String format, Object... arguments) {
    }

    @Override
    public void warn(String format, Object arg1, Object arg2) {
    }

    @Override
    public void warn(String msg, Throwable t) {
    }

    @Override
    public boolean isWarnEnabled(Marker marker) {
      return false;
    }

    @Override
    public void warn(Marker marker, String msg) {
    }

    @Override
    public void warn(Marker marker, String format, Object arg) {
    }

    @Override
    public void warn(Marker marker, String format, Object arg1, Object arg2) {
    }

    @Override
    public void warn(Marker marker, String format, Object... arguments) {
    }

    @Override
    public void warn(Marker marker, String msg, Throwable t) {
    }

    @Override
    public boolean isErrorEnabled() {
      return true;
    }

    @Override
    public void error(String msg) {
      errorMessages.add(msg);
    }

    @Override
    public void error(String format, Object arg) {
      // Simple implementation to handle SLF4J's {} placeholders
      String message = format;
      message = message.replaceFirst("\\{\\}", arg.toString());
      errorMessages.add(message);
    }

    @Override
    public void error(String format, Object arg1, Object arg2) {
      // Simple implementation to handle SLF4J's {} placeholders
      String message = format;
      message = message.replaceFirst("\\{\\}", arg1.toString());
      message = message.replaceFirst("\\{\\}", arg2.toString());
      errorMessages.add(message);
    }

    @Override
    public void error(String format, Object... arguments) {
      // Simple implementation to handle SLF4J's {} placeholders
      String message = format;
      for (Object arg : arguments) {
        message = message.replaceFirst("\\{\\}", arg.toString());
      }
      errorMessages.add(message);
    }

    @Override
    public void error(String msg, Throwable t) {
      errorMessages.add(msg);
    }

    @Override
    public boolean isErrorEnabled(Marker marker) {
      return true;
    }

    @Override
    public void error(Marker marker, String msg) {
      error(msg);
    }

    @Override
    public void error(Marker marker, String format, Object arg) {
      error(format, arg);
    }

    @Override
    public void error(Marker marker, String format, Object arg1, Object arg2) {
      error(format, arg1, arg2);
    }

    @Override
    public void error(Marker marker, String format, Object... arguments) {
      error(format, arguments);
    }

    @Override
    public void error(Marker marker, String msg, Throwable t) {
      error(msg, t);
    }
  }
}
