package dev.jcputney.elearning.parser.validators.rules.scorm2004;

import dev.jcputney.elearning.parser.input.scorm2004.Scorm2004Manifest;
import dev.jcputney.elearning.parser.input.scorm2004.ims.cp.Scorm2004Organization;
import dev.jcputney.elearning.parser.input.scorm2004.ims.cp.Scorm2004Organizations;
import dev.jcputney.elearning.parser.validation.ValidationIssue;
import dev.jcputney.elearning.parser.validation.ValidationResult;
import dev.jcputney.elearning.parser.validators.rules.ValidationRule;

/**
 * Validates that the default organization attribute references an existing organization.
 *
 * <p>Spec Reference: SCORM 2004 4th Edition CAM Section 2.3.2</p>
 */
public class DefaultOrganizationValidRule implements ValidationRule<Scorm2004Manifest> {

  @Override
  public String getRuleName() {
    return "Default Organization Reference Validation";
  }

  @Override
  public String getSpecReference() {
    return "SCORM 2004 CAM 2.3.2";
  }

  @Override
  public ValidationResult validate(Scorm2004Manifest manifest) {
    if (manifest == null) {
      throw new IllegalArgumentException("manifest must not be null");
    }

    Scorm2004Organizations organizations = manifest.getOrganizations();
    if (organizations == null) {
      return ValidationResult.valid(); // OrganizationsRequiredRule handles this
    }

    String defaultOrgId = organizations.getDefaultOrganization();
    if (defaultOrgId == null || defaultOrgId.trim().isEmpty()) {
      return ValidationResult.valid(); // Optional attribute
    }

    // Check if default organization exists
    if (organizations.getOrganizationList() != null) {
      boolean found = organizations.getOrganizationList().stream()
          .anyMatch(org -> defaultOrgId.equals(org.getIdentifier()));

      if (!found) {
        return ValidationResult.of(
            ValidationIssue.error(
                "SCORM2004_INVALID_DEFAULT_ORG",
                String.format("Default organization '%s' not found", defaultOrgId),
                "organizations/@default",
                String.format("Ensure the default attribute references a valid organization identifier. " +
                    "Found: '%s'", defaultOrgId)
            )
        );
      }
    }

    return ValidationResult.valid();
  }
}
