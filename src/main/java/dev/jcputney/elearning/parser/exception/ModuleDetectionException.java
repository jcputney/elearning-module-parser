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
 * Exception thrown when a module type cannot be detected or when there's an error during the module
 * detection process.
 *
 * <p>This exception is typically thrown by
 * {@link dev.jcputney.elearning.parser.api.ModuleTypeDetector} implementations when they're unable
 * to determine the type of eLearning module (SCORM 1.2, SCORM 2004, AICC, cmi5) based on the
 * module's structure and manifest files.
 *
 * <p>Common scenarios that might cause this exception include:
 * <ul>
 *   <li>Missing or invalid manifest files</li>
 *   <li>Corrupt module package</li>
 *   <li>Unsupported module type</li>
 *   <li>I/O errors when accessing module files</li>
 * </ul>
 *
 * <p>Applications should catch this exception and provide appropriate feedback to users about
 * the invalid or unsupported module.
 */
public final class ModuleDetectionException extends ModuleException {

  /**
   * Constructs a new ModuleDetectionException with the specified detail message.
   *
   * @param message the detail message (which is saved for later retrieval by the
   * {@link #getMessage()} method)
   */
  public ModuleDetectionException(String message) {
    super(message);
  }

  /**
   * Constructs a new ModuleDetectionException with the specified detail message and cause.
   *
   * <p>Note that the detail message associated with {@code cause} is <i>not</i> automatically
   * incorporated into this exception's detail message.
   *
   * @param message the detail message (which is saved for later retrieval by the
   * {@link #getMessage()} method)
   * @param cause the cause (which is saved for later retrieval by the {@link #getCause()} method).
   * A null value is permitted and indicates that the cause is nonexistent or unknown.
   */
  public ModuleDetectionException(String message, Throwable cause) {
    super(message, cause);
  }
}
