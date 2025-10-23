package dev.jcputney.elearning.parser.validators.rules.scorm12;

import dev.jcputney.elearning.parser.input.scorm12.Scorm12Manifest;
import dev.jcputney.elearning.parser.input.scorm12.ims.cp.Scorm12Item;
import dev.jcputney.elearning.parser.input.scorm12.ims.cp.Scorm12Organization;
import dev.jcputney.elearning.parser.input.scorm12.ims.cp.Scorm12Resource;
import dev.jcputney.elearning.parser.validation.ValidationIssue;
import dev.jcputney.elearning.parser.validation.ValidationResult;
import dev.jcputney.elearning.parser.validators.rules.ValidationRule;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Validates that all item identifierref attributes reference existing resources.
 * Required by SCORM 1.2 CAM specification.
 */
public class ResourceReferenceValidRule implements ValidationRule<Scorm12Manifest> {

  @Override
  public String getRuleName() {
    return "Resource Reference Validity";
  }

  @Override
  public String getSpecReference() {
    return "SCORM 1.2 CAM 2.3.3";
  }

  @Override
  public ValidationResult validate(Scorm12Manifest manifest) {
    if (manifest == null) {
      throw new IllegalArgumentException("manifest must not be null");
    }

    List<ValidationIssue> issues = new ArrayList<>();

    // Build resource index for fast lookup
    Map<String, Scorm12Resource> resourceIndex = buildResourceIndex(manifest);

    // Validate all items in all organizations
    if (manifest.getOrganizations() != null && manifest.getOrganizations().getOrganizationList() != null) {
      for (Scorm12Organization org : manifest.getOrganizations().getOrganizationList()) {
        if (org.getItems() != null) {
          for (Scorm12Item item : org.getItems()) {
            validateItem(item, org.getIdentifier(), resourceIndex, issues);
          }
        }
      }
    }

    return ValidationResult.of(issues.toArray(new ValidationIssue[0]));
  }

  private Map<String, Scorm12Resource> buildResourceIndex(Scorm12Manifest manifest) {
    Map<String, Scorm12Resource> index = new HashMap<>();
    if (manifest.getResources() != null && manifest.getResources().getResourceList() != null) {
      for (Scorm12Resource resource : manifest.getResources().getResourceList()) {
        if (resource.getIdentifier() != null) {
          index.put(resource.getIdentifier(), resource);
        }
      }
    }
    return index;
  }

  private void validateItem(Scorm12Item item, String orgId,
                            Map<String, Scorm12Resource> resourceIndex,
                            List<ValidationIssue> issues) {
    String identifierRef = item.getIdentifierRef();

    // If item references a resource, validate it exists
    if (identifierRef != null && !identifierRef.isEmpty()) {
      if (!resourceIndex.containsKey(identifierRef)) {
        issues.add(ValidationIssue.error(
            "SCORM12_MISSING_RESOURCE_REF",
            "Item references non-existent resource '" + identifierRef + "'",
            "organization[@identifier='" + orgId + "']/item[@identifier='" +
                item.getIdentifier() + "']/@identifierref",
            "Ensure the identifierref attribute references a valid resource identifier"
        ));
      }
    }

    // Recursively validate child items
    if (item.getItems() != null) {
      for (Scorm12Item childItem : item.getItems()) {
        validateItem(childItem, orgId, resourceIndex, issues);
      }
    }
  }
}
