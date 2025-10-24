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

import dev.jcputney.elearning.parser.input.scorm2004.Scorm2004Manifest;
import dev.jcputney.elearning.parser.validation.ValidationResult;
import dev.jcputney.elearning.parser.validators.rules.ValidationRule;
import dev.jcputney.elearning.parser.validators.rules.scorm2004.DefaultOrganizationValidRule;
import dev.jcputney.elearning.parser.validators.rules.scorm2004.OrganizationsRequiredRule;
import dev.jcputney.elearning.parser.validators.rules.scorm2004.ResourceHrefRequiredRule;
import dev.jcputney.elearning.parser.validators.rules.scorm2004.ResourceReferenceValidRule;
import dev.jcputney.elearning.parser.validators.rules.scorm2004.Scorm2004DuplicateIdentifierRule;
import dev.jcputney.elearning.parser.validators.rules.scorm2004.Scorm2004OrphanedResourcesRule;
import dev.jcputney.elearning.parser.validators.rules.scorm2004.Scorm2004PathSecurityRule;
import java.util.Arrays;
import java.util.List;

/**
 * Validator for SCORM 2004 manifests and their resource references.
 * <p>
 * This validator uses a rule-based architecture for better testability
 * and maintainability. Each validation concern is encapsulated in a discrete rule.
 * </p>
 * <p>
 * This validator checks that:
 * </p>
 * <ul>
 *   <li>All identifiers are unique (no duplicates)</li>
 *   <li>All file paths are safe (no path traversal, absolute paths, or external URLs)</li>
 *   <li>Resources are not orphaned (all resources should be referenced)</li>
 *   <li>Organizations element exists</li>
 *   <li>Default organization reference is valid</li>
 *   <li>All item identifierrefs point to valid resources</li>
 *   <li>Referenced resources have valid href attributes (launch URLs)</li>
 * </ul>
 */
public class Scorm2004ResourceValidator {

  private final List<ValidationRule<Scorm2004Manifest>> rules;

  /**
   * Constructs a new Scorm2004ResourceValidator with default rules.
   */
  public Scorm2004ResourceValidator() {
    this.rules = Arrays.asList(
        // Common rules (SCORM 2004 adapted versions)
        new Scorm2004DuplicateIdentifierRule(),
        new Scorm2004PathSecurityRule(),
        new Scorm2004OrphanedResourcesRule(),

        // SCORM 2004 specific rules
        new OrganizationsRequiredRule(),
        new DefaultOrganizationValidRule(),
        new ResourceReferenceValidRule(),
        new ResourceHrefRequiredRule()
    );
  }

  /**
   * Validates a SCORM 2004 manifest for structural and reference integrity.
   * Uses rule-based validation for better testability and maintainability.
   *
   * @param manifest The SCORM 2004 manifest to validate
   * @return ValidationResult containing any issues found
   */
  public ValidationResult validate(Scorm2004Manifest manifest) {
    return rules.stream()
        .map(rule -> rule.validate(manifest))
        .reduce(ValidationResult.valid(), ValidationResult::merge);
  }
}
