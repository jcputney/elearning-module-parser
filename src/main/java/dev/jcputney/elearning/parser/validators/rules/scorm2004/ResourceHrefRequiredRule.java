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
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Validates that resources referenced by items have valid href attributes (launch URLs).
 *
 * <p>Spec Reference: SCORM 2004 4th Edition CAM Section 2.3.4</p>
 */
public class ResourceHrefRequiredRule implements ValidationRule<Scorm2004Manifest> {

  @Override
  public String getRuleName() {
    return "Referenced Resource Href Validation";
  }

  @Override
  public String getSpecReference() {
    return "SCORM 2004 CAM 2.3.4";
  }

  @Override
  public ValidationResult validate(Scorm2004Manifest manifest) {
    if (manifest == null) {
      throw new IllegalArgumentException("manifest must not be null");
    }

    List<ValidationIssue> issues = new ArrayList<>();

    // Build resource index
    Map<String, Scorm2004Resource> resourceIndex = buildResourceIndex(manifest);

    // Collect all referenced resource IDs
    Set<String> referencedResourceIds = collectReferencedResourceIds(manifest);

    // Validate that referenced resources have hrefs
    for (String resourceId : referencedResourceIds) {
      Scorm2004Resource resource = resourceIndex.get(resourceId);
      if (resource != null) {
        String href = resource.getHref();
        if (href == null || href.trim().isEmpty()) {
          issues.add(ValidationIssue.error(
              "SCORM2004_MISSING_LAUNCH_URL",
              String.format("Resource '%s' is missing href attribute (launch URL)", resourceId),
              String.format("resource[@identifier='%s']/@href", resourceId),
              "Add an href attribute pointing to the SCO's launch file"
          ));
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

  private Set<String> collectReferencedResourceIds(Scorm2004Manifest manifest) {
    Set<String> referencedIds = new HashSet<>();
    Scorm2004Organizations organizations = manifest.getOrganizations();

    if (organizations != null && organizations.getOrganizationList() != null) {
      for (Scorm2004Organization org : organizations.getOrganizationList()) {
        if (org.getItems() != null) {
          collectReferencedIds(org.getItems(), referencedIds);
        }
      }
    }

    return referencedIds;
  }

  private void collectReferencedIds(List<Scorm2004Item> items, Set<String> referencedIds) {
    for (Scorm2004Item item : items) {
      if (item.getIdentifierRef() != null && !item.getIdentifierRef().trim().isEmpty()) {
        referencedIds.add(item.getIdentifierRef());
      }
      if (item.getItems() != null) {
        collectReferencedIds(item.getItems(), referencedIds);
      }
    }
  }
}
