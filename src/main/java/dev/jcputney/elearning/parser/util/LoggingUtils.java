/*
 * qlty-ignore: +qlty:similar-code
 *
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
 *
 * qlty-ignore: -qlty:similar-code
 */

package dev.jcputney.elearning.parser.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Utility class for logging-related capability.
 * <p>
 * This class provides utility methods for obtaining loggers and defines guidelines for consistent
 * logging throughout the codebase.
 * </p>
 * <p>
 * Log levels should be used as follows:
 * <ul>
 *   <li>ERROR: For errors that prevent the app from functioning correctly</li>
 *   <li>WARN: For potentially harmful situations that don't prevent the app from functioning</li>
 *   <li>INFO: For informational messages about the app's state</li>
 *   <li>DEBUG: For detailed information useful for debugging</li>
 *   <li>TRACE: For very detailed information, typically only enabled during development</li>
 * </ul>
 */
public final class LoggingUtils {

  /**
   * Default logger for logging messages.
   */
  private LoggingUtils() {
    // Private constructor to prevent instantiation
  }

  /**
   * Gets a logger for the specified class.
   *
   * @param clazz The class to get a logger for
   * @return A logger instance for the specified class
   */
  public static Logger getLogger(Class<?> clazz) {
    return LoggerFactory.getLogger(clazz);
  }
}