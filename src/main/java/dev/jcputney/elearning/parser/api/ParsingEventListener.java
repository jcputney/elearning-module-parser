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

package dev.jcputney.elearning.parser.api;

/**
 * Optional listener interface for receiving parsing events from the module parser. Applications can
 * implement this interface to receive diagnostic information about the parsing process without
 * forcing logging dependencies.
 *
 * <p>This interface replaces direct logging in the library, allowing applications
 * to decide how to handle diagnostic information (log it, store it, ignore it, etc.).</p>
 *
 * <p>Example usage:</p>
 * <pre>{@code
 * ParsingEventListener listener = new ParsingEventListener() {
 *     @Override
 *     public void onParsingStarted(String moduleType, String path) {
 *         // Application decides whether/how to log
 *         logger.info("Started parsing {} module at {}", moduleType, path);
 *     }
 * };
 *
 * ModuleParserFactory factory = new DefaultModuleParserFactory(fileAccess, listener);
 * }</pre>
 */
@SuppressWarnings("unused")
public interface ParsingEventListener {

  /**
   * A no-op implementation of ParsingEventListener that ignores all events. This is the default
   * when no listener is provided.
   */
  ParsingEventListener NO_OP = new ParsingEventListener() {
  };

  /**
   * Called when module type detection begins.
   *
   * @param rootPath the root path being analyzed
   */
  default void onDetectionStarted(String rootPath) {
    // Default no-op implementation
  }

  /**
   * Called when a module type is successfully detected.
   *
   * @param moduleType the detected module type
   * @param detectedBy the plugin that detected the type
   */
  default void onModuleTypeDetected(String moduleType, String detectedBy) {
    // Default no-op implementation
  }

  /**
   * Called when module parsing begins.
   *
   * @param moduleType the type of module being parsed
   * @param manifestPath the path to the manifest file
   */
  default void onParsingStarted(String moduleType, String manifestPath) {
    // Default no-op implementation
  }

  /**
   * Called when an external metadata file is being loaded.
   *
   * @param metadataPath the path to the external metadata file
   */
  default void onLoadingExternalMetadata(String metadataPath) {
    // Default no-op implementation
  }

  /**
   * Called when module parsing completes successfully.
   *
   * @param moduleType the type of module parsed
   * @param duration the time taken in milliseconds
   */
  default void onParsingCompleted(String moduleType, long duration) {
    // Default no-op implementation
  }

  /**
   * Called when a recoverable issue occurs during parsing. This replaces WARN level logging.
   *
   * @param message description of the issue
   * @param context additional context (e.g., file path, field name)
   */
  default void onParsingWarning(String message, String context) {
    // Default no-op implementation
  }

  /**
   * Called to report parsing progress for long operations.
   *
   * @param phase the current phase of parsing
   * @param percentComplete progress percentage (0-100)
   */
  default void onParsingProgress(String phase, int percentComplete) {
    // Default no-op implementation
  }
}