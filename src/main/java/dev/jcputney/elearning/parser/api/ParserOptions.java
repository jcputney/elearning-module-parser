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

import dev.jcputney.elearning.parser.config.ModuleSizeCalculator;
import java.util.Objects;

/**
 * Configuration options for module parsing behavior. Controls validation strictness and other
 * parsing settings.
 */
public class ParserOptions {

  private boolean strictMode = true;
  private Boolean calculateModuleSize = null; // null = use system default

  /**
   * Creates parser options with default settings (strict mode enabled).
   */
  public ParserOptions() {
    // Default constructor
  }

  /**
   * Creates parser options with strict mode enabled. Parsing will fail on any validation errors.
   *
   * @return ParserOptions configured for strict mode
   */
  public static ParserOptions strict() {
    return new ParserOptions().setStrictMode(true);
  }

  /**
   * Creates parser options with lenient mode enabled. Parsing will continue despite validation
   * errors.
   *
   * @return ParserOptions configured for lenient mode
   */
  public static ParserOptions lenient() {
    return new ParserOptions().setStrictMode(false);
  }

  /**
   * Checks if strict validation mode is enabled.
   *
   * @return true if strict mode is enabled
   */
  public boolean isStrictMode() {
    return strictMode;
  }

  /**
   * Sets whether to use strict validation mode. In strict mode, parsing fails on any validation
   * errors. In lenient mode, parsing continues despite errors.
   *
   * @param strict true for strict mode, false for lenient
   * @return this ParserOptions instance for method chaining
   */
  public ParserOptions setStrictMode(boolean strict) {
    this.strictMode = strict;
    return this;
  }

  /**
   * Gets the module size calculation setting.
   *
   * @return true to enable, false to disable, null to use system default
   */
  public Boolean getCalculateModuleSize() {
    return calculateModuleSize;
  }

  /**
   * Sets whether to calculate module size (total disk size of all files). If not explicitly set
   * (null), uses system property or environment variable.
   *
   * @param calculate true to enable size calculation, false to disable, null for system default
   * @return this ParserOptions instance for method chaining
   */
  public ParserOptions setCalculateModuleSize(Boolean calculate) {
    this.calculateModuleSize = calculate;
    return this;
  }

  /**
   * Checks if module size calculation is enabled based on this options object. Returns the explicit
   * setting if present, otherwise falls back to system default.
   *
   * @return true if size calculation should be performed
   */
  public boolean shouldCalculateModuleSize() {
    return Objects.requireNonNullElseGet(calculateModuleSize, ModuleSizeCalculator::isEnabled);
  }
}
