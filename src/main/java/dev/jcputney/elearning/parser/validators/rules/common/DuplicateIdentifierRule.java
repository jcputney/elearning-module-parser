package dev.jcputney.elearning.parser.validators.rules.common;

import dev.jcputney.elearning.parser.input.scorm12.Scorm12Manifest;
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
 * Validates that all identifiers within the manifest are unique.
 * Duplicate identifiers cause unpredictable behavior in SCORM players.
 *
 * <p>Spec References:</p>
 * <ul>
 *   <li>SCORM 1.2 CAM Section 2.3.1: "identifier must be unique"</li>
 *   <li>SCORM 2004 CAM Section 2.3.1: "identifier must be unique within scope"</li>
 * </ul>
 */
public class DuplicateIdentifierRule implements ValidationRule<Scorm12Manifest> {

  @Override
  public String getRuleName() {
    return "Duplicate Identifier Validation";
  }

  @Override
  public String getSpecReference() {
    return "SCORM 1.2 CAM 2.3.1";
  }

  @Override
  public ValidationResult validate(Scorm12Manifest manifest) {
    if (manifest == null) {
      throw new IllegalArgumentException("manifest must not be null");
    }

    List<ValidationIssue> issues = new ArrayList<>();
    Map<String, List<String>> identifierLocations = new HashMap<>();

    // Collect manifest identifier
    if (manifest.getIdentifier() != null) {
      identifierLocations.computeIfAbsent(manifest.getIdentifier(), k -> new ArrayList<>())
          .add("manifest/@identifier");
    }

    // Collect organization identifiers
    if (manifest.getOrganizations() != null && manifest.getOrganizations().getOrganizationList() != null) {
      for (Scorm12Organization org : manifest.getOrganizations().getOrganizationList()) {
        if (org.getIdentifier() != null) {
          identifierLocations.computeIfAbsent(org.getIdentifier(), k -> new ArrayList<>())
              .add("organizations/organization[@identifier='" + org.getIdentifier() + "']");
        }
      }
    }

    // Collect resource identifiers
    if (manifest.getResources() != null && manifest.getResources().getResourceList() != null) {
      for (Scorm12Resource resource : manifest.getResources().getResourceList()) {
        if (resource.getIdentifier() != null) {
          identifierLocations.computeIfAbsent(resource.getIdentifier(), k -> new ArrayList<>())
              .add("resources/resource[@identifier='" + resource.getIdentifier() + "']");
        }
      }
    }

    // Find duplicates
    for (Map.Entry<String, List<String>> entry : identifierLocations.entrySet()) {
      if (entry.getValue().size() > 1) {
        issues.add(ValidationIssue.error(
            "DUPLICATE_IDENTIFIER",
            String.format("Identifier '%s' is used %d times but must be unique",
                entry.getKey(), entry.getValue().size()),
            String.join(", ", entry.getValue()),
            String.format("Rename duplicate identifiers to be unique. Locations: %s",
                String.join(", ", entry.getValue()))
        ));
      }
    }

    return ValidationResult.of(issues.toArray(new ValidationIssue[0]));
  }
}
