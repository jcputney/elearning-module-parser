package dev.jcputney.elearning.parser.validators.rules.cmi5;

import dev.jcputney.elearning.parser.input.cmi5.Cmi5Manifest;
import dev.jcputney.elearning.parser.validation.ValidationIssue;
import dev.jcputney.elearning.parser.validation.ValidationResult;
import dev.jcputney.elearning.parser.validators.rules.ValidationRule;

/**
 * Validates that a cmi5 manifest contains course element.
 *
 * <p>According to cmi5 specification, every cmi5 package must have a course element
 * that defines the basic course structure.</p>
 *
 * @see <a href="https://github.com/AICC/CMI-5_Spec_Current/blob/quartz/cmi5_spec.md">cmi5 Specification</a>
 */
public class CourseRequiredRule implements ValidationRule<Cmi5Manifest> {

  /**
   * Validates that the manifest contains course element.
   *
   * @param manifest The cmi5 manifest to validate (must not be null)
   * @return ValidationResult containing any issues found
   * @throws IllegalArgumentException if manifest is null
   */
  @Override
  public ValidationResult validate(Cmi5Manifest manifest) {
    if (manifest == null) {
      throw new IllegalArgumentException("manifest must not be null");
    }

    if (manifest.getCourse() == null) {
      return ValidationResult.of(
          ValidationIssue.error(
              "CMI5_MISSING_COURSE",
              "cmi5 manifest must contain course element",
              "cmi5.xml/course"
          )
      );
    }

    return ValidationResult.valid();
  }

  @Override
  public String getRuleName() {
    return "CourseRequired";
  }

  @Override
  public String getSpecReference() {
    return "cmi5 Specification - Course Structure (cmi5.xml)";
  }
}
