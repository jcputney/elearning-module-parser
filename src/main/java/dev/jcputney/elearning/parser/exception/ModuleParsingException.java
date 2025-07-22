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

import java.util.Map;

/**
 * Exception thrown when there's an error parsing an eLearning module.
 *
 * <p>This exception is typically thrown by {@link dev.jcputney.elearning.parser.ModuleParser}
 * implementations when they encounter errors while parsing module content. It is a higher-level
 * exception that may wrap more specific exceptions like {@link ManifestParseException}.
 *
 * <p>Common scenarios that might cause this exception include:
 * <ul>
 *   <li>Missing or invalid manifest files</li>
 *   <li>Missing required metadata elements</li>
 *   <li>Invalid module structure</li>
 *   <li>I/O errors when accessing module files</li>
 * </ul>
 *
 * <p>Applications should catch this exception and provide appropriate feedback to users about
 * the invalid or problematic module.
 *
 * <p>This exception extends {@link ModuleException} and inherits its ability to store
 * additional context information as metadata.
 */
public class ModuleParsingException extends ModuleException {

  /**
   * Constructs a new ModuleParsingException with the specified detail message.
   *
   * @param message the detail message (which is saved for later retrieval by the
   * {@link #getMessage()} method)
   */
  public ModuleParsingException(String message) {
    super(message);
  }

  /**
   * Constructs a new ModuleParsingException with the specified detail message and cause.
   *
   * <p>Note that the detail message associated with {@code cause} is <i>not</i> automatically
   * incorporated into this exception's detail message.
   *
   * @param message the detail message (which is saved for later retrieval by the
   * {@link #getMessage()} method)
   * @param cause the cause (which is saved for later retrieval by the {@link #getCause()} method).
   * A null value is permitted and indicates that the cause is nonexistent or unknown.
   */
  public ModuleParsingException(String message, Throwable cause) {
    super(message, cause);
  }

  /**
   * Constructs a new ModuleParsingException with the specified detail message, cause, and
   * metadata.
   *
   * @param message the detail message (which is saved for later retrieval by the
   * {@link #getMessage()} method)
   * @param cause the cause (which is saved for later retrieval by the {@link #getCause()} method).
   * A null value is permitted and indicates that the cause is nonexistent or unknown.
   * @param metadata a map of additional context information about the exception
   */
  public ModuleParsingException(String message, Throwable cause, Map<String, Object> metadata) {
    super(message, cause, metadata);
  }
}
