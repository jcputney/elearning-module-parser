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

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Base exception class for all module-related exceptions in the elearning-module-parser library.
 *
 * <p>This class provides a foundation for a more extensible error handling framework by:
 * <ul>
 *   <li>Establishing a common base class for all module-related exceptions</li>
 *   <li>Supporting additional context information through a metadata map</li>
 *   <li>Providing consistent constructors and methods across all exception types</li>
 * </ul>
 *
 * <p>All specific exception types in the library should extend this class or one of its subclasses.
 */
public class ModuleException extends Exception {

  /**
   * A map to hold additional context information about the exception. This can be used to store
   * key-value pairs of metadata related to the exception.
   */
  private final Map<String, Object> metadata = new HashMap<>();

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
   * A null value is permitted, and indicates that the cause is nonexistent or unknown.
   */
  public ModuleException(String message, Throwable cause) {
    super(message, cause);
  }

  /**
   * Constructs a new ModuleException with the specified detail message, cause, and metadata.
   *
   * @param message the detail message (which is saved for later retrieval by the
   * {@link #getMessage()} method)
   * @param cause the cause (which is saved for later retrieval by the {@link #getCause()} method).
   * A null value is permitted, and indicates that the cause is nonexistent or unknown.
   * @param metadata a map of additional context information about the exception
   */
  public ModuleException(String message, Throwable cause, Map<String, Object> metadata) {
    super(message, cause);
    if (metadata != null) {
      this.metadata.putAll(metadata);
    }
  }

  /**
   * Adds a metadata entry to this exception.
   *
   * @param key the key for the metadata entry
   * @param value the value for the metadata entry
   * @return this exception instance for method chaining
   */
  public ModuleException addMetadata(String key, Object value) {
    if (key != null) {
      this.metadata.put(key, value);
    }
    return this;
  }

  /**
   * Gets the value of a metadata entry.
   *
   * @param key the key for the metadata entry
   * @return the value for the metadata entry, or null if the key doesn't exist
   */
  public Object getMetadata(String key) {
    return this.metadata.get(key);
  }

  /**
   * Gets all metadata entries as an unmodifiable map.
   *
   * @return an unmodifiable view of the metadata map
   */
  public Map<String, Object> getMetadata() {
    return Collections.unmodifiableMap(this.metadata);
  }

  /**
   * Returns a string representation of this exception, including the message, cause, and metadata.
   *
   * @return a string representation of this exception
   */
  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder(getClass().getName());
    String message = getLocalizedMessage();
    if (message != null) {
      sb
          .append(": ")
          .append(message);
    }
    if (!metadata.isEmpty()) {
      sb
          .append(" [Metadata: ")
          .append(metadata)
          .append("]");
    }
    return sb.toString();
  }
}