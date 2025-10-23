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
 * Validates that resources referenced by items have valid href attributes (launch URLs).
 * Required by SCORM 1.2 CAM specification.
 */
public class ResourceHrefRequiredRule implements ValidationRule<Scorm12Manifest> {

  @Override
  public String getRuleName() {
    return "Resource Href Required";
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
            validateItem(item, resourceIndex, issues);
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

  private void validateItem(Scorm12Item item,
                            Map<String, Scorm12Resource> resourceIndex,
                            List<ValidationIssue> issues) {
    String identifierRef = item.getIdentifierRef();

    // If item references a resource, validate the resource has a valid href
    if (identifierRef != null && !identifierRef.isEmpty()) {
      Scorm12Resource resource = resourceIndex.get(identifierRef);
      if (resource != null) {
        validateResourceHref(resource, issues);
      }
    }

    // Recursively validate child items
    if (item.getItems() != null) {
      for (Scorm12Item childItem : item.getItems()) {
        validateItem(childItem, resourceIndex, issues);
      }
    }
  }

  private void validateResourceHref(Scorm12Resource resource, List<ValidationIssue> issues) {
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
