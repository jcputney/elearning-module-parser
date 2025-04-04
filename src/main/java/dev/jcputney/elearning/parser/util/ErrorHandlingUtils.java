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

import dev.jcputney.elearning.parser.exception.FileAccessException;
import dev.jcputney.elearning.parser.exception.ManifestParseException;
import dev.jcputney.elearning.parser.exception.ModuleDetectionException;
import dev.jcputney.elearning.parser.exception.ModuleException;
import dev.jcputney.elearning.parser.exception.ModuleParsingException;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;
import org.slf4j.Logger;

/**
 * Utility class for handling exceptions in a consistent way across the codebase.
 *
 * <p>This class provides methods for:
 * <ul>
 *   <li>Creating exceptions with metadata</li>
 *   <li>Logging exceptions</li>
 *   <li>Handling exceptions in a standardized way</li>
 *   <li>Executing code that might throw exceptions with proper handling</li>
 * </ul>
 *
 * <p>Using this class helps ensure consistent error handling and logging throughout the app.
 */
public final class ErrorHandlingUtils {

  /**
   * Private constructor to prevent instantiation of this utility class.
   */
  private ErrorHandlingUtils() {
    // Private constructor to prevent instantiation
  }

  /**
   * Creates a new ModuleParsingException with the specified message, cause, and metadata.
   *
   * @param message the detail message
   * @param cause the cause of the exception
   * @param metadata a map of additional context information
   * @return a new ModuleParsingException
   */
  public static ModuleParsingException createParsingException(String message, Throwable cause,
      Map<String, Object> metadata) {
    return new ModuleParsingException(message, cause, metadata);
  }

  /**
   * Creates a new ModuleParsingException with the specified message and cause.
   *
   * @param message the detail message
   * @param cause the cause of the exception
   * @return a new ModuleParsingException
   */
  public static ModuleParsingException createParsingException(String message, Throwable cause) {
    return new ModuleParsingException(message, cause);
  }

  /**
   * Creates a new ModuleParsingException with the specified message.
   *
   * @param message the detail message
   * @return a new ModuleParsingException
   */
  public static ModuleParsingException createParsingException(String message) {
    return new ModuleParsingException(message);
  }

  /**
   * Creates a new ModuleDetectionException with the specified message, cause, and metadata.
   *
   * @param message the detail message
   * @param cause the cause of the exception
   * @param metadata a map of additional context information
   * @return a new ModuleDetectionException
   */
  public static ModuleDetectionException createDetectionException(String message, Throwable cause,
      Map<String, Object> metadata) {
    return new ModuleDetectionException(message, cause, metadata);
  }

  /**
   * Creates a new ModuleDetectionException with the specified message and cause.
   *
   * @param message the detail message
   * @param cause the cause of the exception
   * @return a new ModuleDetectionException
   */
  public static ModuleDetectionException createDetectionException(String message, Throwable cause) {
    return new ModuleDetectionException(message, cause);
  }

  /**
   * Creates a new ModuleDetectionException with the specified message.
   *
   * @param message the detail message
   * @return a new ModuleDetectionException
   */
  public static ModuleDetectionException createDetectionException(String message) {
    return new ModuleDetectionException(message);
  }

  /**
   * Creates a new ManifestParseException with the specified message, cause, and metadata.
   *
   * @param message the detail message
   * @param cause the cause of the exception
   * @param metadata a map of additional context information
   * @return a new ManifestParseException
   */
  public static ManifestParseException createManifestException(String message, Throwable cause,
      Map<String, Object> metadata) {
    return new ManifestParseException(message, cause, metadata);
  }

  /**
   * Creates a new ManifestParseException with the specified message and cause.
   *
   * @param message the detail message
   * @param cause the cause of the exception
   * @return a new ManifestParseException
   */
  public static ManifestParseException createManifestException(String message, Throwable cause) {
    return new ManifestParseException(message, cause);
  }

  /**
   * Creates a new ManifestParseException with the specified message.
   *
   * @param message the detail message
   * @return a new ManifestParseException
   */
  public static ManifestParseException createManifestException(String message) {
    return new ManifestParseException(message);
  }

  /**
   * Creates a new FileAccessException with the specified message, cause, and metadata.
   *
   * @param message the detail message
   * @param cause the cause of the exception
   * @param metadata a map of additional context information
   * @return a new FileAccessException
   */
  public static FileAccessException createFileAccessException(String message, Throwable cause,
      Map<String, Object> metadata) {
    return new FileAccessException(message, cause, metadata);
  }

  /**
   * Creates a new FileAccessException with the specified message and cause.
   *
   * @param message the detail message
   * @param cause the cause of the exception
   * @return a new FileAccessException
   */
  public static FileAccessException createFileAccessException(String message, Throwable cause) {
    return new FileAccessException(message, cause);
  }

  /**
   * Creates a new FileAccessException with the specified message.
   *
   * @param message the detail message
   * @return a new FileAccessException
   */
  public static FileAccessException createFileAccessException(String message) {
    return new FileAccessException(message);
  }

  /**
   * Logs an exception with the specified logger at the error level.
   *
   * @param logger the logger to use
   * @param exception the exception to log
   * @param message the message to log
   */
  public static void logException(Logger logger, Throwable exception, String message) {
    if (exception instanceof ModuleException moduleException) {
      if (!moduleException.getMetadata().isEmpty()) {
        logger.error("{} - {} [Metadata: {}]", message, exception.getMessage(),
            moduleException.getMetadata());
      } else {
        logger.error("{} - {}", message, exception.getMessage());
      }
    } else {
      logger.error("{} - {}", message, exception.getMessage());
    }
    logger.debug("Exception details:", exception);
  }

  /**
   * Executes the specified supplier and returns its result. If an exception is thrown, it is
   * caught, logged, and wrapped in a ModuleParsingException.
   *
   * @param <T> the type of the result
   * @param supplier the supplier to execute
   * @param logger the logger to use
   * @param errorMessage the error message to use if an exception is thrown
   * @param contextData additional context data to include in the exception metadata
   * @return the result of the supplier
   * @throws ModuleParsingException if an exception is thrown by the supplier
   */
  public static <T> T executeWithParsingExceptionHandling(Supplier<T> supplier, Logger logger,
      String errorMessage, Map<String, Object> contextData) throws ModuleParsingException {
    try {
      return supplier.get();
    } catch (Exception e) {
      Map<String, Object> metadata = new HashMap<>();
      if (contextData != null) {
        metadata.putAll(contextData);
      }

      ModuleParsingException parsingException;
      if (e instanceof ModuleException moduleException) {
        // If it's already a ModuleException, add our metadata to it
        metadata.putAll(moduleException.getMetadata());

        if (e instanceof ModuleParsingException) {
          // If it's already a ModuleParsingException, just add our metadata
          parsingException = (ModuleParsingException) e;
          for (Map.Entry<String, Object> entry : metadata.entrySet()) {
            parsingException.addMetadata(entry.getKey(), entry.getValue());
          }
        } else {
          // If it's another type of ModuleException, wrap it
          parsingException = createParsingException(errorMessage, e, metadata);
        }
      } else {
        // If it's not a ModuleException, wrap it
        parsingException = createParsingException(errorMessage, e, metadata);
      }

      logException(logger, parsingException, errorMessage);
      throw parsingException;
    }
  }

  /**
   * Executes the specified supplier and returns its result. If an exception is thrown, it is
   * caught, logged, and wrapped in a ModuleDetectionException.
   *
   * @param <T> the type of the result
   * @param supplier the supplier to execute
   * @param logger the logger to use
   * @param errorMessage the error message to use if an exception is thrown
   * @param contextData additional context data to include in the exception metadata
   * @return the result of the supplier
   * @throws ModuleDetectionException if an exception is thrown by the supplier
   */
  public static <T> T executeWithDetectionExceptionHandling(Supplier<T> supplier, Logger logger,
      String errorMessage, Map<String, Object> contextData) throws ModuleDetectionException {
    try {
      return supplier.get();
    } catch (Exception e) {
      Map<String, Object> metadata = new HashMap<>();
      if (contextData != null) {
        metadata.putAll(contextData);
      }

      ModuleDetectionException detectionException;
      if (e instanceof ModuleException moduleException) {
        // If it's already a ModuleException, add our metadata to it
        metadata.putAll(moduleException.getMetadata());

        if (e instanceof ModuleDetectionException) {
          // If it's already a ModuleDetectionException, just add our metadata
          detectionException = (ModuleDetectionException) e;
          for (Map.Entry<String, Object> entry : metadata.entrySet()) {
            detectionException.addMetadata(entry.getKey(), entry.getValue());
          }
        } else {
          // If it's another type of ModuleException, wrap it
          detectionException = createDetectionException(errorMessage, e, metadata);
        }
      } else {
        // If it's not a ModuleException, wrap it
        detectionException = createDetectionException(errorMessage, e, metadata);
      }

      logException(logger, detectionException, errorMessage);
      throw detectionException;
    }
  }

  /**
   * Executes the specified supplier and returns its result. If an exception is thrown, it is
   * caught, logged, and wrapped in a FileAccessException.
   *
   * @param <T> the type of the result
   * @param supplier the supplier to execute
   * @param logger the logger to use
   * @param errorMessage the error message to use if an exception is thrown
   * @param contextData additional context data to include in the exception metadata
   * @return the result of the supplier
   * @throws FileAccessException if an exception is thrown by the supplier
   */
  public static <T> T executeWithFileAccessExceptionHandling(Supplier<T> supplier, Logger logger,
      String errorMessage, Map<String, Object> contextData) throws FileAccessException {
    try {
      return supplier.get();
    } catch (Exception e) {
      Map<String, Object> metadata = new HashMap<>();
      if (contextData != null) {
        metadata.putAll(contextData);
      }

      FileAccessException fileAccessException;
      if (e instanceof ModuleException moduleException) {
        // If it's already a ModuleException, add our metadata to it
        metadata.putAll(moduleException.getMetadata());

        if (e instanceof FileAccessException) {
          // If it's already a FileAccessException, just add our metadata
          fileAccessException = (FileAccessException) e;
          for (Map.Entry<String, Object> entry : metadata.entrySet()) {
            fileAccessException.addMetadata(entry.getKey(), entry.getValue());
          }
        } else {
          // If it's another type of ModuleException, wrap it
          fileAccessException = createFileAccessException(errorMessage, e, metadata);
        }
      } else {
        // If it's not a ModuleException, wrap it
        fileAccessException = createFileAccessException(errorMessage, e, metadata);
      }

      logException(logger, fileAccessException, errorMessage);
      throw fileAccessException;
    }
  }
}
