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

import static org.assertj.core.api.Assertions.assertThat;

import dev.jcputney.elearning.parser.input.scorm12.Scorm12Manifest;
import dev.jcputney.elearning.parser.validation.ValidationResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ManifestIdentifierRequiredRuleTest {

  private ManifestIdentifierRequiredRule rule;

  @BeforeEach
  void setUp() {
    rule = new ManifestIdentifierRequiredRule();
  }

  @Test
  void validate_withManifestHavingIdentifier_returnsValid() {
    Scorm12Manifest manifest = new Scorm12Manifest();
    manifest.setIdentifier("MANIFEST-001");

    ValidationResult result = rule.validate(manifest);

    assertThat(result.isValid()).isTrue();
  }

  @Test
  void validate_withManifestMissingIdentifier_returnsError() {
    Scorm12Manifest manifest = new Scorm12Manifest();
    // No identifier set

    ValidationResult result = rule.validate(manifest);

    assertThat(result.isValid()).isFalse();
    assertThat(result.getErrors()).hasSize(1);
    assertThat(result.getErrors().get(0).code()).isEqualTo("SCORM12_MISSING_MANIFEST_IDENTIFIER");
    assertThat(result.getErrors().get(0).message()).contains("identifier");
  }

  @Test
  void validate_withManifestHavingEmptyIdentifier_returnsError() {
    Scorm12Manifest manifest = new Scorm12Manifest();
    manifest.setIdentifier("");

    ValidationResult result = rule.validate(manifest);

    assertThat(result.isValid()).isFalse();
    assertThat(result.getErrors()).hasSize(1);
    assertThat(result.getErrors().get(0).code()).isEqualTo("SCORM12_MISSING_MANIFEST_IDENTIFIER");
  }

  @Test
  void validate_withManifestHavingWhitespaceOnlyIdentifier_returnsError() {
    Scorm12Manifest manifest = new Scorm12Manifest();
    manifest.setIdentifier("   ");

    ValidationResult result = rule.validate(manifest);

    assertThat(result.isValid()).isFalse();
    assertThat(result.getErrors()).hasSize(1);
    assertThat(result.getErrors().get(0).code()).isEqualTo("SCORM12_MISSING_MANIFEST_IDENTIFIER");
  }
}
