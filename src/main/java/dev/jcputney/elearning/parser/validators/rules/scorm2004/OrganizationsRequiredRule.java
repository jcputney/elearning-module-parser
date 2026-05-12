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

package dev.jcputney.elearning.parser.validators.rules.scorm2004;

import dev.jcputney.elearning.parser.input.scorm2004.Scorm2004Manifest;
import dev.jcputney.elearning.parser.validation.ValidationIssue;
import dev.jcputney.elearning.parser.validation.ValidationResult;
import dev.jcputney.elearning.parser.validators.rules.ValidationRule;

/**
 * Validates that a SCORM 2004 manifest contains an organizations element.
 *
 * <p>Spec Reference: SCORM 2004 4th Edition CAM Section 2.3.2</p>
 */
public class OrganizationsRequiredRule implements ValidationRule<Scorm2004Manifest> {

  @Override
  public String getRuleName() {
    return "Organizations Element Required";
  }

  @Override
  public String getSpecReference() {
    return "SCORM 2004 CAM 2.3.2";
  }

  @Override
  public ValidationResult validate(Scorm2004Manifest manifest) {
    if (manifest == null) {
      throw new IllegalArgumentException("manifest must not be null");
    }

    if (manifest.getOrganizations() == null) {
      return ValidationResult.of(
          ValidationIssue.error(
              "SCORM2004_MISSING_ORGANIZATIONS",
              "Manifest must contain an <organizations> element",
              "manifest",
              "Add an <organizations> element to the manifest"
          )
      );
    }

    return ValidationResult.valid();
  }
}
