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

package dev.jcputney.elearning.parser.validators.rules.aicc;

import dev.jcputney.elearning.parser.input.aicc.AiccManifest;
import dev.jcputney.elearning.parser.validation.ValidationIssue;
import dev.jcputney.elearning.parser.validation.ValidationResult;
import dev.jcputney.elearning.parser.validators.rules.ValidationRule;

/**
 * Validates that an AICC course has a launch URL.
 *
 * <p>According to AICC specification, at least one assignable unit must have a file_name
 * that serves as the entry point for the course. This is represented as the manifest's launch
 * URL.</p>
 *
 * @see <a href="https://www.aicc.org/aicc-cmi-guidelines">AICC CMI Guidelines</a>
 */
public class LaunchUrlRequiredRule implements ValidationRule<AiccManifest> {

  /**
   * Validates that the course has a non-empty launch URL.
   *
   * @param manifest The AICC manifest to validate (must not be null)
   * @return ValidationResult containing any issues found
   * @throws IllegalArgumentException if manifest is null
   */
  @Override
  public ValidationResult validate(AiccManifest manifest) {
    if (manifest == null) {
      throw new IllegalArgumentException("manifest must not be null");
    }

    String launchUrl = manifest.getLaunchUrl();
    if (launchUrl == null || launchUrl
        .trim()
        .isEmpty()) {
      return ValidationResult.of(
          ValidationIssue.error(
              "AICC_MISSING_LAUNCH_URL",
              "AICC course must have a launch URL",
              "assignable_unit",
              "Ensure at least one assignable unit has a file_name"
          )
      );
    }

    return ValidationResult.valid();
  }

  @Override
  public String getRuleName() {
    return "LaunchUrlRequired";
  }

  @Override
  public String getSpecReference() {
    return "AICC CMI Guidelines - Assignable Unit File (.au) - file_name field";
  }
}
