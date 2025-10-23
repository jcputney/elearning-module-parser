package dev.jcputney.elearning.parser.validators.rules.scorm12;

import dev.jcputney.elearning.parser.input.scorm12.Scorm12Manifest;
import dev.jcputney.elearning.parser.validation.ValidationIssue;
import dev.jcputney.elearning.parser.validation.ValidationResult;
import dev.jcputney.elearning.parser.validators.rules.ValidationRule;

/**
 * Validates that the manifest contains an organizations element.
 * Required by SCORM 1.2 CAM specification.
 */
public class OrganizationsRequiredRule implements ValidationRule<Scorm12Manifest> {

  @Override
  public String getRuleName() {
    return "Organizations Element Required";
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

    if (manifest.getOrganizations() == null) {
      return ValidationResult.of(
          ValidationIssue.error(
              "SCORM12_MISSING_ORGANIZATIONS",
              "Manifest must contain an <organizations> element",
              "manifest",
              "Add <organizations> element to manifest"
          )
      );
    }

    return ValidationResult.valid();
  }
}
