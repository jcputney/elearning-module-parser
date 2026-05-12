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
package dev.jcputney.elearning.parser.validators;

import dev.jcputney.elearning.parser.input.scorm12.Scorm12Manifest;
import dev.jcputney.elearning.parser.validation.ValidationResult;
import dev.jcputney.elearning.parser.validators.rules.ValidationRule;
import dev.jcputney.elearning.parser.validators.rules.common.DuplicateIdentifierRule;
import dev.jcputney.elearning.parser.validators.rules.common.OrphanedResourcesRule;
import dev.jcputney.elearning.parser.validators.rules.common.PathSecurityRule;
import dev.jcputney.elearning.parser.validators.rules.scorm12.DefaultOrganizationValidRule;
import dev.jcputney.elearning.parser.validators.rules.scorm12.LaunchableResourceRequiredRule;
import dev.jcputney.elearning.parser.validators.rules.scorm12.ManifestIdentifierRequiredRule;
import dev.jcputney.elearning.parser.validators.rules.scorm12.OrganizationsRequiredRule;
import dev.jcputney.elearning.parser.validators.rules.scorm12.ResourceHrefRequiredRule;
import dev.jcputney.elearning.parser.validators.rules.scorm12.ResourceReferenceValidRule;
import dev.jcputney.elearning.parser.validators.rules.scorm12.ResourcesRequiredRule;
import java.util.Arrays;
import java.util.List;

/**
 * Validator for SCORM 1.2 manifests and their resource references.
 * <p>
 * This validator checks that:
 * </p>
 * <ul>
 *   <li>All item identifierrefs point to valid resources</li>
 *   <li>Referenced resources have valid href attributes (launch URLs)</li>
 *   <li>Default organization exists if specified</li>
 *   <li>Organizations structure is valid</li>
 * </ul>
 */
public class Scorm12ResourceValidator {

  private final List<ValidationRule<Scorm12Manifest>> commonRules;

  /**
   * Constructs a new Scorm12ResourceValidator with default rules.
   */
  public Scorm12ResourceValidator() {
    this.commonRules = Arrays.asList(
        new DuplicateIdentifierRule(),
        new PathSecurityRule(),
        new OrphanedResourcesRule(),
        new ManifestIdentifierRequiredRule(),
        new ResourcesRequiredRule(),
        new OrganizationsRequiredRule(),
        new DefaultOrganizationValidRule(),
        new LaunchableResourceRequiredRule(),
        new ResourceReferenceValidRule(),
        new ResourceHrefRequiredRule()
    );
  }

  /**
   * Validates a SCORM 1.2 manifest for structural and reference integrity. Uses rule-based
   * validation for better testability and maintainability.
   *
   * @param manifest The SCORM 1.2 manifest to validate
   * @return ValidationResult containing any issues found
   */
  public ValidationResult validate(Scorm12Manifest manifest) {
    return commonRules
        .stream()
        .map(rule -> rule.validate(manifest))
        .reduce(ValidationResult.valid(), ValidationResult::merge);
  }
}
