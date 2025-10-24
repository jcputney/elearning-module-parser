package dev.jcputney.elearning.parser.validators.rules.scorm2004;

import dev.jcputney.elearning.parser.input.scorm2004.Scorm2004Manifest;
import dev.jcputney.elearning.parser.input.scorm2004.ims.cp.Scorm2004Item;
import dev.jcputney.elearning.parser.input.scorm2004.ims.cp.Scorm2004Organization;
import dev.jcputney.elearning.parser.input.scorm2004.ims.cp.Scorm2004Organizations;
import dev.jcputney.elearning.parser.input.scorm2004.ims.cp.Scorm2004Resource;
import dev.jcputney.elearning.parser.input.scorm2004.ims.cp.Scorm2004Resources;
import dev.jcputney.elearning.parser.validation.ValidationIssue;
import dev.jcputney.elearning.parser.validation.ValidationResult;
import dev.jcputney.elearning.parser.validators.rules.ValidationRule;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Validates that all item identifierref attributes reference existing resources.
 *
 * <p>Spec Reference: SCORM 2004 4th Edition CAM Section 2.3.3</p>
 */
public class ResourceReferenceValidRule implements ValidationRule<Scorm2004Manifest> {

  @Override
  public String getRuleName() {
    return "Item Resource Reference Validation";
  }

  @Override
  public String getSpecReference() {
    return "SCORM 2004 CAM 2.3.3";
  }

  @Override
  public ValidationResult validate(Scorm2004Manifest manifest) {
    if (manifest == null) {
      throw new IllegalArgumentException("manifest must not be null");
    }

    List<ValidationIssue> issues = new ArrayList<>();

    // Build resource index
    Map<String, Scorm2004Resource> resourceIndex = buildResourceIndex(manifest);

    // Validate all item references
    Scorm2004Organizations organizations = manifest.getOrganizations();
    if (organizations != null && organizations.getOrganizationList() != null) {
      for (Scorm2004Organization org : organizations.getOrganizationList()) {
        if (org.getItems() != null) {
          validateItems(org.getItems(), org.getIdentifier(), resourceIndex, issues);
        }
      }
    }

    return ValidationResult.of(issues.toArray(new ValidationIssue[0]));
  }

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

  private void validateItems(List<Scorm2004Item> items, String orgId,
                             Map<String, Scorm2004Resource> resourceIndex,
                             List<ValidationIssue> issues) {
    for (Scorm2004Item item : items) {
      String identifierRef = item.getIdentifierRef();

      if (identifierRef != null && !identifierRef.trim().isEmpty()) {
        if (!resourceIndex.containsKey(identifierRef)) {
          issues.add(ValidationIssue.error(
              "SCORM2004_MISSING_RESOURCE_REF",
              String.format("Item references non-existent resource '%s'", identifierRef),
              String.format("organization[@identifier='%s']/item[@identifier='%s']/@identifierref",
                  orgId, item.getIdentifier()),
              String.format("Ensure the identifierref attribute references a valid resource identifier. " +
                  "Referenced: '%s'", identifierRef)
          ));
        }
      }

      // Recursively validate child items
      if (item.getItems() != null) {
        validateItems(item.getItems(), orgId, resourceIndex, issues);
      }
    }
  }
}
