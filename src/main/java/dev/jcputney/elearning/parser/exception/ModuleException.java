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

package dev.jcputney.elearning.parser.exception;

/**
 * Base exception class for all module-related exceptions in the elearning-module-parser library.
 *
 * <p>This class provides a foundation for a more extensible error handling framework by:
 * <ul>
 *   <li>Establishing a common base class for all module-related exceptions</li>
 *   <li>Providing consistent constructors and methods across all exception types</li>
 * </ul>
 *
 * <p>All specific exception types in the library should extend this class or one of its subclasses.
 */
public sealed class ModuleException extends Exception permits FileAccessException,
    ManifestParseException, ModuleDetectionException, ModuleParsingException {

  /**
   * Constructs a new ModuleException with the specified detail message.
   *
   * @param message the detail message (which is saved for later retrieval by the
   * {@link #getMessage()} method)
   */
  public ModuleException(String message) {
    super(message);
  }

  /**
   * Constructs a new ModuleException with the specified detail message and cause.
   *
   * @param message the detail message (which is saved for later retrieval by the
   * {@link #getMessage()} method)
   * @param cause the cause (which is saved for later retrieval by the {@link #getCause()} method).
   * A null value is permitted and indicates that the cause is nonexistent or unknown.
   */
  public ModuleException(String message, Throwable cause) {
    super(message, cause);
  }
}