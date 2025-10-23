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
import dev.jcputney.elearning.parser.validators.rules.scorm12.OrganizationsRequiredRule;
import dev.jcputney.elearning.parser.validators.rules.scorm12.ResourceReferenceValidRule;
import dev.jcputney.elearning.parser.input.scorm12.ims.cp.Scorm12Item;
import dev.jcputney.elearning.parser.input.scorm12.ims.cp.Scorm12Organization;
import dev.jcputney.elearning.parser.input.scorm12.ims.cp.Scorm12Organizations;
import dev.jcputney.elearning.parser.input.scorm12.ims.cp.Scorm12Resource;
import dev.jcputney.elearning.parser.input.scorm12.ims.cp.Scorm12Resources;
import dev.jcputney.elearning.parser.validation.ValidationIssue;
import dev.jcputney.elearning.parser.validation.ValidationIssue.Severity;
import dev.jcputney.elearning.parser.validation.ValidationResult;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        new OrganizationsRequiredRule(),
        new DefaultOrganizationValidRule(),
        new ResourceReferenceValidRule()
    );
  }

  /**
   * Validates a SCORM 1.2 manifest for structural and reference integrity.
   * Now uses rule-based validation for better testability and maintainability.
   *
   * @param manifest The SCORM 1.2 manifest to validate
   * @return ValidationResult containing any issues found
   */
  public ValidationResult validate(Scorm12Manifest manifest) {
    // Run common rules first
    ValidationResult commonRulesResult = commonRules.stream()
        .map(rule -> rule.validate(manifest))
        .reduce(ValidationResult.valid(), ValidationResult::merge);

    // Keep existing validation logic (will be extracted to rules later)
    ValidationResult existingValidation = validateExisting(manifest);

    // Merge all results
    return commonRulesResult.merge(existingValidation);
  }

  /**
   * Existing validation logic - will be extracted to individual rules in future tasks.
   */
  private ValidationResult validateExisting(Scorm12Manifest manifest) {
    List<ValidationIssue> issues = new ArrayList<>();

    // Build resource index for fast lookup
    Map<String, Scorm12Resource> resourceIndex = buildResourceIndex(manifest);

    // Validate organizations
    validateOrganizations(manifest, resourceIndex, issues);

    return ValidationResult.of(issues.toArray(new ValidationIssue[0]));
  }

  /**
   * Builds an index of resources by their identifier for fast lookup.
   *
   * @param manifest The manifest containing resources
   * @return Map of resource identifier to resource
   */
  private Map<String, Scorm12Resource> buildResourceIndex(Scorm12Manifest manifest) {
    Map<String, Scorm12Resource> index = new HashMap<>();
    Scorm12Resources resources = manifest.getResources();

    if (resources != null && resources.getResourceList() != null) {
      for (Scorm12Resource resource : resources.getResourceList()) {
        if (resource.getIdentifier() != null) {
          index.put(resource.getIdentifier(), resource);
        }
      }
    }

    return index;
  }

  /**
   * Validates the organizations structure and default organization reference.
   *
   * @param manifest The manifest to validate
   * @param resourceIndex Index of available resources
   * @param issues List to collect validation issues
   */
  private void validateOrganizations(Scorm12Manifest manifest,
                                     Map<String, Scorm12Resource> resourceIndex,
                                     List<ValidationIssue> issues) {
    Scorm12Organizations organizations = manifest.getOrganizations();

    // Organizations null check now handled by OrganizationsRequiredRule
    // Default organization validity now handled by DefaultOrganizationValidRule
    if (organizations == null) {
      return;
    }

    // Validate all organizations
    if (organizations.getOrganizationList() != null) {
      for (Scorm12Organization org : organizations.getOrganizationList()) {
        validateOrganization(org, resourceIndex, issues);
      }
    }
  }

  /**
   * Validates a single organization and its items.
   *
   * @param organization The organization to validate
   * @param resourceIndex Index of available resources
   * @param issues List to collect validation issues
   */
  private void validateOrganization(Scorm12Organization organization,
                                    Map<String, Scorm12Resource> resourceIndex,
                                    List<ValidationIssue> issues) {
    if (organization.getItems() != null) {
      for (Scorm12Item item : organization.getItems()) {
        validateItem(item, organization.getIdentifier(), resourceIndex, issues);
      }
    }
  }

  /**
   * Recursively validates an item and its children.
   *
   * @param item The item to validate
   * @param orgId The parent organization identifier
   * @param resourceIndex Index of available resources
   * @param issues List to collect validation issues
   */
  private void validateItem(Scorm12Item item, String orgId,
                            Map<String, Scorm12Resource> resourceIndex,
                            List<ValidationIssue> issues) {
    String identifierRef = item.getIdentifierRef();

    // Resource reference validation now handled by ResourceReferenceValidRule
    // If item references a resource, validate the resource has a launch URL
    if (identifierRef != null && !identifierRef.isEmpty()) {
      Scorm12Resource resource = resourceIndex.get(identifierRef);
      if (resource != null) {
        // Validate that the resource has a launch URL (will be extracted in Task 4)
        validateResourceHref(resource, item.getIdentifier(), orgId, issues);
      }
    }

    // Recursively validate child items
    if (item.getItems() != null) {
      for (Scorm12Item childItem : item.getItems()) {
        validateItem(childItem, orgId, resourceIndex, issues);
      }
    }
  }

  /**
   * Validates that a resource has a valid href (launch URL).
   *
   * @param resource The resource to validate
   * @param itemId The item identifier that references this resource
   * @param orgId The organization identifier
   * @param issues List to collect validation issues
   */
  private void validateResourceHref(Scorm12Resource resource, String itemId, String orgId,
                                    List<ValidationIssue> issues) {
    String href = resource.getHref();

    if (href == null || href.trim().isEmpty()) {
      issues.add(ValidationIssue.error(
          "SCORM12_MISSING_LAUNCH_URL",
          "Resource '" + resource.getIdentifier() + "' is missing href attribute (launch URL)",
          "resource[@identifier='" + resource.getIdentifier() + "']/@href",
          "Add an href attribute pointing to the SCO's launch file"
      ));
    }
  }
}
