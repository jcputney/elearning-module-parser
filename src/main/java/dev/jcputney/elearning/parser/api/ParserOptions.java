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
package dev.jcputney.elearning.parser.api;

import dev.jcputney.elearning.parser.config.ModuleSizeCalculator;
import dev.jcputney.elearning.parser.util.XmlParsingUtils;
import java.util.Objects;

/**
 * Configuration options for module parsing behavior. Controls validation strictness and other
 * parsing settings.
 */
public class ParserOptions {

  private boolean strictMode = true;
  private Boolean calculateModuleSize = null; // null = use system default
  private Long maxManifestSize = null; // null = use system default

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

  /**
   * Gets the maximum manifest size setting.
   *
   * @return the maximum manifest size in bytes, or null to use system default
   */
  public Long getMaxManifestSize() {
    return maxManifestSize;
  }

  /**
   * Sets the maximum allowed manifest size in bytes. Manifests exceeding this limit will be
   * rejected during parsing. If not explicitly set (null), uses the system property
   * {@code elearning.parser.maxXmlSize} or the default of 50MB.
   *
   * @param maxManifestSize maximum size in bytes, or null for system default
   * @return this ParserOptions instance for method chaining
   */
  public ParserOptions setMaxManifestSize(Long maxManifestSize) {
    this.maxManifestSize = maxManifestSize;
    return this;
  }

  /**
   * Returns the resolved maximum manifest size in bytes. Uses the explicit setting if present,
   * otherwise falls back to the system property or default.
   *
   * @return the maximum manifest size in bytes
   */
  public long getResolvedMaxManifestSize() {
    return Objects.requireNonNullElseGet(maxManifestSize, XmlParsingUtils::getMaxXmlSize);
  }
}
