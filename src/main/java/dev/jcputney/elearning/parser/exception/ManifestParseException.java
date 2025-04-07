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

package dev.jcputney.elearning.parser.exception;

import java.util.Map;

/**
 * Exception thrown when there's an error parsing a module manifest file.
 *
 * <p>This exception is typically thrown by parser implementations when they encounter
 * errors while parsing manifest files (e.g., imsmanifest.xml for SCORM, .crs/.des/.au/.cst for
 * AICC, cmi5.xml for cmi5).
 *
 * <p>Common scenarios that might cause this exception include:
 * <ul>
 *   <li>Malformed XML or INI files</li>
 *   <li>Missing required elements in the manifest</li>
 *   <li>Invalid format or structure in the manifest</li>
 * </ul>
 *
 * <p>This exception extends {@link ModuleException} and inherits its ability to store
 * additional context information as metadata.
 */
public class ManifestParseException extends ModuleException {

  /**
   * Constructs a new ManifestParseException with the specified detail message.
   *
   * @param message the detail message (which is saved for later retrieval by the
   *                {@link #getMessage()} method)
   */
  public ManifestParseException(String message) {
    super(message);
  }

  /**
   * Constructs a new ManifestParseException with the specified detail message and cause.
   *
   * <p>Note that the detail message associated with {@code cause} is <i>not</i> automatically
   * incorporated into this exception's detail message.
   *
   * @param message the detail message (which is saved for later retrieval by the
   *                {@link #getMessage()} method)
   * @param cause   the cause (which is saved for later retrieval by the {@link #getCause()} method).
   *                A null value is permitted, and indicates that the cause is nonexistent or unknown.
   */
  public ManifestParseException(String message, Throwable cause) {
    super(message, cause);
  }

  /**
   * Constructs a new ManifestParseException with the specified detail message, cause, and
   * metadata.
   *
   * @param message  the detail message (which is saved for later retrieval by the
   *                 {@link #getMessage()} method)
   * @param cause    the cause (which is saved for later retrieval by the {@link #getCause()} method).
   *                 A null value is permitted, and indicates that the cause is nonexistent or unknown.
   * @param metadata a map of additional context information about the exception
   */
  public ManifestParseException(String message, Throwable cause, Map<String, Object> metadata) {
    super(message, cause, metadata);
  }
}
