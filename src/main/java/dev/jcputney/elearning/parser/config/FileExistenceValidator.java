/*
 * Copyright (c) 2024-2026 Jonathan Putney
 *
 * Licensed under the Apache License, Version 2.0 (the "License").
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at the project root LICENSE file
 * or at http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0
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
