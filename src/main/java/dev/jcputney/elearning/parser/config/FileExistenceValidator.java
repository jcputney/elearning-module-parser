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

package dev.jcputney.elearning.parser.config;

/**
 * Configuration utility for controlling file existence validation in module parsers.
 * <p>
 * File existence validation checks whether all files referenced in a module manifest actually exist
 * in the module package. While useful for validation reports, this can be expensive for modules
 * with many files (e.g., HLS video streams with thousands of .ts segments).
 * </p>
 * <p>
 * Validation is disabled by default. It can be enabled via:
 * </p>
 * <ul>
 *   <li>System property {@code elearning.parser.validateFileExists=true}</li>
 *   <li>Environment variable {@code ELEARNING_VALIDATE_FILE_EXISTS=true}</li>
 *   <li>Programmatically via {@link dev.jcputney.elearning.parser.api.ParserOptions}</li>
 * </ul>
 */
public final class FileExistenceValidator {

  /**
   * System property key used to determine whether file existence validation should be enabled.
   * <p>
   * When set to "true" (case-insensitive), parsers will check whether all files referenced in
   * module manifests actually exist in the module package. If not set or set to any other value,
   * validation is disabled by default.
   */
  private static final String VALIDATE_SYSPROP = "elearning.parser.validateFileExists";

  /**
   * Environment variable key used to enable file existence validation.
   * <p>
   * This variable specifies whether file existence checks should be performed during module
   * parsing. When set to "true" (case-insensitive), the parser will verify that all files
   * referenced in manifests exist.
   */
  private static final String VALIDATE_ENV = "ELEARNING_VALIDATE_FILE_EXISTS";

  /**
   * Private constructor to prevent instantiation of the utility class.
   * <p>
   * The FileExistenceValidator class is designed to provide static methods for checking whether
   * file existence validation is enabled. It should not be instantiated.
   */
  private FileExistenceValidator() {
    throw new AssertionError("Utility class should not be instantiated");
  }

  /**
   * Determines whether file existence validation is enabled based on system properties or
   * environment variables.
   *
   * @return {@code true} if validation is enabled; otherwise {@code false}.
   */
  public static boolean isEnabled() {
    // Check system property
    String fromSysProp = System.getProperty(VALIDATE_SYSPROP);
    if (fromSysProp != null) {
      return Boolean.parseBoolean(fromSysProp);
    }

    // Check environment variable
    String fromEnv = System.getenv(VALIDATE_ENV);
    return Boolean.parseBoolean(fromEnv);
  }
}
