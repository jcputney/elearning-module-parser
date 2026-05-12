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
 * Configuration utility for controlling module size calculation in module parsers.
 * <p>
 * Module size calculation determines the total disk size of all files in a module package. While
 * useful for metadata reporting, this can be expensive for modules with many files (e.g., HLS video
 * streams with thousands of .ts segments) as it requires listing and measuring all files in the
 * module.
 * </p>
 * <p>
 * Size calculation is disabled by default. It can be enabled via:
 * </p>
 * <ul>
 *   <li>System property {@code elearning.parser.calculateModuleSize=true}</li>
 *   <li>Environment variable {@code ELEARNING_CALCULATE_MODULE_SIZE=true}</li>
 *   <li>Programmatically via {@link dev.jcputney.elearning.parser.api.ParserOptions}</li>
 * </ul>
 */
public final class ModuleSizeCalculator {

  /**
   * System property key used to determine whether module size calculation should be enabled.
   * <p>
   * When set to "true" (case-insensitive), parsers will calculate the total size of all files in
   * the module package. If not set or set to any other value, size calculation is disabled by
   * default.
   */
  private static final String CALCULATE_SYSPROP = "elearning.parser.calculateModuleSize";

  /**
   * Environment variable key used to enable module size calculation.
   * <p>
   * This variable specifies whether module size calculation should be performed during module
   * parsing. When set to "true" (case-insensitive), the parser will calculate the total disk size
   * of all files in the module.
   */
  private static final String CALCULATE_ENV = "ELEARNING_CALCULATE_MODULE_SIZE";

  /**
   * Private constructor to prevent instantiation of the utility class.
   * <p>
   * The ModuleSizeCalculator class is designed to provide static methods for checking whether
   * module size calculation is enabled. It should not be instantiated.
   */
  private ModuleSizeCalculator() {
    throw new AssertionError("Utility class should not be instantiated");
  }

  /**
   * Determines whether module size calculation is enabled based on system properties or environment
   * variables.
   *
   * @return {@code true} if size calculation is enabled; otherwise {@code false}.
   */
  public static boolean isEnabled() {
    // Check system property
    String fromSysProp = System.getProperty(CALCULATE_SYSPROP);
    if (fromSysProp != null) {
      return Boolean.parseBoolean(fromSysProp);
    }

    // Check environment variable
    String fromEnv = System.getenv(CALCULATE_ENV);
    return Boolean.parseBoolean(fromEnv);
  }
}
