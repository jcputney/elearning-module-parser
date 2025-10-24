package dev.jcputney.elearning.parser.validators.rules.scorm2004;

import dev.jcputney.elearning.parser.input.scorm2004.Scorm2004Manifest;
import dev.jcputney.elearning.parser.validation.ValidationIssue;
import dev.jcputney.elearning.parser.validation.ValidationResult;
import dev.jcputney.elearning.parser.validators.rules.ValidationRule;

/**
 * Validates that a SCORM 2004 manifest contains an organizations element.
 *
 * <p>Spec Reference: SCORM 2004 4th Edition CAM Section 2.3.2</p>
 */
public class OrganizationsRequiredRule implements ValidationRule<Scorm2004Manifest> {

  @Override
  public String getRuleName() {
    return "Organizations Element Required";
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

    if (manifest.getOrganizations() == null) {
      return ValidationResult.of(
          ValidationIssue.error(
              "SCORM2004_MISSING_ORGANIZATIONS",
              "Manifest must contain an <organizations> element",
              "manifest",
              "Add an <organizations> element to the manifest"
          )
      );
    }

    return ValidationResult.valid();
  }
}
