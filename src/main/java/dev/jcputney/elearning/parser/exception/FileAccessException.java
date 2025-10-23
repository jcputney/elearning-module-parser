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
 * Exception thrown when there's an error accessing files within a module.
 *
 * <p>This exception is typically thrown by {@link dev.jcputney.elearning.parser.api.FileAccess}
 * implementations when they encounter errors while accessing files, such as:
 * <ul>
 *   <li>File not found</li>
 *   <li>Permission denied</li>
 *   <li>I/O errors</li>
 *   <li>Network errors (for remote file access)</li>
 * </ul>
 */
public final class FileAccessException extends ModuleException {

  /**
   * Constructs a new FileAccessException with the specified detail message.
   *
   * @param message the detail message (which is saved for later retrieval by the
   * {@link #getMessage()} method)
   */
  public FileAccessException(String message) {
    super(message);
  }

  /**
   * Constructs a new FileAccessException with the specified detail message and cause.
   *
   * <p>Note that the detail message associated with {@code cause} is <i>not</i> automatically
   * incorporated into this exception's detail message.
   *
   * @param message the detail message (which is saved for later retrieval by the
   * {@link #getMessage()} method)
   * @param cause the cause (which is saved for later retrieval by the {@link #getCause()} method).
   * A null value is permitted and indicates that the cause is nonexistent or unknown.
   */
  public FileAccessException(String message, Throwable cause) {
    super(message, cause);
  }
}