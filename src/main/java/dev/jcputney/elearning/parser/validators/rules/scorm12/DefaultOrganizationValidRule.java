package dev.jcputney.elearning.parser.validators.rules.scorm12;

import dev.jcputney.elearning.parser.input.scorm12.Scorm12Manifest;
import dev.jcputney.elearning.parser.input.scorm12.ims.cp.Scorm12Organization;
import dev.jcputney.elearning.parser.input.scorm12.ims.cp.Scorm12Organizations;
import dev.jcputney.elearning.parser.validation.ValidationIssue;
import dev.jcputney.elearning.parser.validation.ValidationResult;
import dev.jcputney.elearning.parser.validators.rules.ValidationRule;

/**
 * Validates that if a default organization is specified, it references an existing organization.
 * Required by SCORM 1.2 CAM specification.
 */
public class DefaultOrganizationValidRule implements ValidationRule<Scorm12Manifest> {

  @Override
  public String getRuleName() {
    return "Default Organization Validity";
  }

  @Override
  public String getSpecReference() {
    return "SCORM 1.2 CAM 2.3.2";
  }

  @Override
  public ValidationResult validate(Scorm12Manifest manifest) {
    if (manifest == null) {
      throw new IllegalArgumentException("manifest must not be null");
    }

    Scorm12Organizations organizations = manifest.getOrganizations();
    if (organizations == null) {
      return ValidationResult.valid();  // OrganizationsRequiredRule handles this
    }

    String defaultOrgId = organizations.getDefaultOrganization();
    if (defaultOrgId == null || defaultOrgId.isEmpty()) {
      return ValidationResult.valid();  // No default specified is valid
    }

    // Check if the default organization exists in the list
    boolean found = false;
    if (organizations.getOrganizationList() != null) {
      for (Scorm12Organization org : organizations.getOrganizationList()) {
        if (defaultOrgId.equals(org.getIdentifier())) {
          found = true;
          break;
        }
      }
    }

    if (!found) {
      return ValidationResult.of(
          ValidationIssue.error(
              "SCORM12_INVALID_DEFAULT_ORG",
              "Default organization '" + defaultOrgId + "' not found",
              "organizations/@default",
              "Ensure the default attribute references a valid organization identifier"
          )
      );
    }

    return ValidationResult.valid();
  }
}
