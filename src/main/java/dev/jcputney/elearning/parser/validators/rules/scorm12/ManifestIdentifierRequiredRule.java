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

package dev.jcputney.elearning.parser.validators.rules.scorm12;

import dev.jcputney.elearning.parser.input.scorm12.Scorm12Manifest;
import dev.jcputney.elearning.parser.validation.ValidationIssue;
import dev.jcputney.elearning.parser.validation.ValidationResult;
import dev.jcputney.elearning.parser.validators.rules.ValidationRule;

/**
 * Validates that the manifest has a required identifier attribute. Required by SCORM 1.2 CAM
 * specification.
 */
public class ManifestIdentifierRequiredRule implements ValidationRule<Scorm12Manifest> {

  @Override
  public String getRuleName() {
    return "Manifest Identifier Required";
  }

  @Override
  public String getSpecReference() {
    return "SCORM 1.2 CAM 2.1";
  }

  @Override
  public ValidationResult validate(Scorm12Manifest manifest) {
    if (manifest == null) {
      throw new IllegalArgumentException("manifest must not be null");
    }

    String identifier = manifest.getIdentifier();

    if (identifier == null || identifier
        .trim()
        .isEmpty()) {
      return ValidationResult.of(
          ValidationIssue.error(
              "SCORM12_MISSING_MANIFEST_IDENTIFIER",
              "Manifest must have a valid identifier attribute",
              "manifest/@identifier",
              "Add an identifier attribute to the manifest element"
          )
      );
    }

    return ValidationResult.valid();
  }
}
