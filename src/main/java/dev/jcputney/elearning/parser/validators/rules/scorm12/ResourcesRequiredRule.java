package dev.jcputney.elearning.parser.validators.rules.scorm12;

import dev.jcputney.elearning.parser.input.scorm12.Scorm12Manifest;
import dev.jcputney.elearning.parser.validation.ValidationIssue;
import dev.jcputney.elearning.parser.validation.ValidationResult;
import dev.jcputney.elearning.parser.validators.rules.ValidationRule;

/**
 * Validates that the manifest has a required resources element.
 * Required by SCORM 1.2 CAM specification.
 */
public class ResourcesRequiredRule implements ValidationRule<Scorm12Manifest> {

  @Override
  public String getRuleName() {
    return "Resources Element Required";
  }

  @Override
  public String getSpecReference() {
    return "SCORM 1.2 CAM 2.3.4";
  }

  @Override
  public ValidationResult validate(Scorm12Manifest manifest) {
    if (manifest == null) {
      throw new IllegalArgumentException("manifest must not be null");
    }

    if (manifest.getResources() == null) {
      return ValidationResult.of(
          ValidationIssue.error(
              "SCORM12_MISSING_RESOURCES",
              "Manifest must contain a <resources> element",
              "manifest",
              "Add <resources> element to manifest"
          )
      );
    }

    return ValidationResult.valid();
  }
}
