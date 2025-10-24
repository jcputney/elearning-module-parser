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

package dev.jcputney.elearning.parser.validators.rules.scorm2004;

import dev.jcputney.elearning.parser.input.scorm2004.Scorm2004Manifest;
import dev.jcputney.elearning.parser.input.scorm2004.ims.cp.Scorm2004Item;
import dev.jcputney.elearning.parser.input.scorm2004.ims.cp.Scorm2004Organization;
import dev.jcputney.elearning.parser.input.scorm2004.ims.cp.Scorm2004Resource;
import dev.jcputney.elearning.parser.validation.ValidationIssue;
import dev.jcputney.elearning.parser.validation.ValidationResult;
import dev.jcputney.elearning.parser.validators.rules.ValidationRule;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Validates that all resources in SCORM 2004 manifest are referenced by at least one item.
 * Orphaned resources waste space and may indicate errors in the manifest.
 *
 * <p>This is a SHOULD requirement (warning, not error) but helps identify
 * potential issues in content packages.</p>
 */
public class Scorm2004OrphanedResourcesRule implements ValidationRule<Scorm2004Manifest> {

  @Override
  public String getRuleName() {
    return "Orphaned Resources Detection";
  }

  @Override
  public String getSpecReference() {
    return "Best Practice";
  }

  @Override
  public ValidationResult validate(Scorm2004Manifest manifest) {
    if (manifest == null) {
      throw new IllegalArgumentException("manifest must not be null");
    }

    List<ValidationIssue> issues = new ArrayList<>();

    // Collect all referenced resource IDs
    Set<String> referencedResourceIds = new HashSet<>();
    if (manifest.getOrganizations() != null && manifest.getOrganizations().getOrganizationList() != null) {
      for (Scorm2004Organization org : manifest.getOrganizations().getOrganizationList()) {
        if (org.getItems() != null) {
          collectReferencedResources(org.getItems(), referencedResourceIds);
        }
      }
    }

    // Check for orphaned resources
    if (manifest.getResources() != null && manifest.getResources().getResourceList() != null) {
      for (Scorm2004Resource resource : manifest.getResources().getResourceList()) {
        if (resource.getIdentifier() != null && !referencedResourceIds.contains(resource.getIdentifier())) {
          issues.add(ValidationIssue.warning(
              "ORPHANED_RESOURCE",
              String.format("Resource '%s' is not referenced by any item", resource.getIdentifier()),
              "resources/resource[@identifier='" + resource.getIdentifier() + "']",
              "Either reference this resource from an item or remove it to reduce package size"
          ));
        }
      }
    }

    return ValidationResult.of(issues.toArray(new ValidationIssue[0]));
  }

  private void collectReferencedResources(List<Scorm2004Item> items, Set<String> referencedResourceIds) {
    for (Scorm2004Item item : items) {
      if (item.getIdentifierRef() != null) {
        referencedResourceIds.add(item.getIdentifierRef());
      }
      if (item.getItems() != null) {
        collectReferencedResources(item.getItems(), referencedResourceIds);
      }
    }
  }
}
