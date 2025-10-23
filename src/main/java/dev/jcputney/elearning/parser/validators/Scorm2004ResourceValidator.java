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
import dev.jcputney.elearning.parser.input.scorm2004.ims.cp.Scorm2004Item;
import dev.jcputney.elearning.parser.input.scorm2004.ims.cp.Scorm2004Organization;
import dev.jcputney.elearning.parser.input.scorm2004.ims.cp.Scorm2004Organizations;
import dev.jcputney.elearning.parser.input.scorm2004.ims.cp.Scorm2004Resource;
import dev.jcputney.elearning.parser.input.scorm2004.ims.cp.Scorm2004Resources;
import dev.jcputney.elearning.parser.validation.ValidationIssue;
import dev.jcputney.elearning.parser.validation.ValidationResult;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Validator for SCORM 2004 manifests and their resource references.
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
public class Scorm2004ResourceValidator {

  /**
   * Validates a SCORM 2004 manifest for structural and reference integrity.
   *
   * @param manifest The SCORM 2004 manifest to validate
   * @return ValidationResult containing any issues found
   */
  public ValidationResult validate(Scorm2004Manifest manifest) {
    List<ValidationIssue> issues = new ArrayList<>();

    // Build resource index for fast lookup
    Map<String, Scorm2004Resource> resourceIndex = buildResourceIndex(manifest);

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
  private Map<String, Scorm2004Resource> buildResourceIndex(Scorm2004Manifest manifest) {
    Map<String, Scorm2004Resource> index = new HashMap<>();
    Scorm2004Resources resources = manifest.getResources();

    if (resources != null && resources.getResourceList() != null) {
      for (Scorm2004Resource resource : resources.getResourceList()) {
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
  private void validateOrganizations(Scorm2004Manifest manifest,
                                     Map<String, Scorm2004Resource> resourceIndex,
                                     List<ValidationIssue> issues) {
    Scorm2004Organizations organizations = manifest.getOrganizations();

    if (organizations == null) {
      issues.add(ValidationIssue.error(
          "SCORM2004_MISSING_ORGANIZATIONS",
          "Manifest must contain an <organizations> element",
          "manifest"
      ));
      return;
    }

    // Validate default organization if specified
    String defaultOrgId = organizations.getDefaultOrganization();
    if (defaultOrgId != null && !defaultOrgId.isEmpty()) {
      Scorm2004Organization defaultOrg = organizations.getDefault();
      if (defaultOrg == null) {
        issues.add(ValidationIssue.error(
            "SCORM2004_INVALID_DEFAULT_ORG",
            "Default organization '" + defaultOrgId + "' not found",
            "organizations/@default",
            "Ensure the default attribute references a valid organization identifier"
        ));
      } else {
        // Validate the default organization's items
        validateOrganization(defaultOrg, resourceIndex, issues);
      }
    }

    // Validate all organizations
    if (organizations.getOrganizationList() != null) {
      for (Scorm2004Organization org : organizations.getOrganizationList()) {
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
  private void validateOrganization(Scorm2004Organization organization,
                                    Map<String, Scorm2004Resource> resourceIndex,
                                    List<ValidationIssue> issues) {
    if (organization.getItems() != null) {
      for (Scorm2004Item item : organization.getItems()) {
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
  private void validateItem(Scorm2004Item item, String orgId,
                            Map<String, Scorm2004Resource> resourceIndex,
                            List<ValidationIssue> issues) {
    String identifierRef = item.getIdentifierRef();

    // If item references a resource, validate it
    if (identifierRef != null && !identifierRef.isEmpty()) {
      Scorm2004Resource resource = resourceIndex.get(identifierRef);

      if (resource == null) {
        issues.add(ValidationIssue.error(
            "SCORM2004_MISSING_RESOURCE_REF",
            "Item references non-existent resource '" + identifierRef + "'",
            "organization[@identifier='" + orgId + "']/item[@identifier='" +
                item.getIdentifier() + "']/@identifierref",
            "Ensure the identifierref attribute references a valid resource identifier"
        ));
      } else {
        // Validate that the resource has a launch URL
        validateResourceHref(resource, item.getIdentifier(), orgId, issues);
      }
    }

    // Recursively validate child items
    if (item.getItems() != null) {
      for (Scorm2004Item childItem : item.getItems()) {
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
  private void validateResourceHref(Scorm2004Resource resource, String itemId, String orgId,
                                    List<ValidationIssue> issues) {
    String href = resource.getHref();

    if (href == null || href.trim().isEmpty()) {
      issues.add(ValidationIssue.error(
          "SCORM2004_MISSING_LAUNCH_URL",
          "Resource '" + resource.getIdentifier() + "' is missing href attribute (launch URL)",
          "resource[@identifier='" + resource.getIdentifier() + "']/@href",
          "Add an href attribute pointing to the SCO's launch file"
      ));
    }
  }
}
