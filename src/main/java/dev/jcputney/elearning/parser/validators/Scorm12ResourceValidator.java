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

package dev.jcputney.elearning.parser.validators;

import dev.jcputney.elearning.parser.input.scorm12.Scorm12Manifest;
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
import dev.jcputney.elearning.parser.validation.ValidationResult;
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
   * Validates a SCORM 1.2 manifest for structural and reference integrity.
   * Uses rule-based validation for better testability and maintainability.
   *
   * @param manifest The SCORM 1.2 manifest to validate
   * @return ValidationResult containing any issues found
   */
  public ValidationResult validate(Scorm12Manifest manifest) {
    return commonRules.stream()
        .map(rule -> rule.validate(manifest))
        .reduce(ValidationResult.valid(), ValidationResult::merge);
  }
}
