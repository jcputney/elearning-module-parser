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
package dev.jcputney.elearning.parser.validators.rules.xapi;

import dev.jcputney.elearning.parser.input.xapi.tincan.TincanManifest;
import dev.jcputney.elearning.parser.validation.ValidationIssue;
import dev.jcputney.elearning.parser.validation.ValidationResult;
import dev.jcputney.elearning.parser.validators.rules.ValidationRule;

/**
 * Validates that an xAPI package has a launch URL.
 *
 * <p>According to xAPI specification, at least one activity must have a launch attribute
 * that serves as the entry point for the learning experience.</p>
 *
 * <p>This rule defers validation when the activities list is null or empty, as that is
 * handled by {@link ActivitiesRequiredRule}.</p>
 *
 * @see <a href="https://github.com/adlnet/xAPI-Spec/blob/master/xAPI-About.md">xAPI
 * Specification</a>
 */
public class LaunchUrlRequiredRule implements ValidationRule<TincanManifest> {

  /**
   * Validates that the package has a launch URL.
   *
   * @param manifest The xAPI manifest to validate (must not be null)
   * @return ValidationResult containing any issues found
   * @throws IllegalArgumentException if manifest is null
   */
  @Override
  public ValidationResult validate(TincanManifest manifest) {
    if (manifest == null) {
      throw new IllegalArgumentException("manifest must not be null");
    }

    // Defer to ActivitiesRequiredRule for null/empty activities
    if (manifest.getActivities() == null || manifest
        .getActivities()
        .isEmpty()) {
      return ValidationResult.valid();
    }

    String launchUrl = manifest.getLaunchUrl();
    if (launchUrl == null || launchUrl
        .trim()
        .isEmpty()) {
      return ValidationResult.of(
          ValidationIssue.error(
              "XAPI_MISSING_LAUNCH_URL",
              "xAPI package must have a launch URL",
              "tincan.xml/activities/activity",
              "Ensure at least one activity has a launch attribute"
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
    return "xAPI Specification - Activity Launch URL";
  }
}
