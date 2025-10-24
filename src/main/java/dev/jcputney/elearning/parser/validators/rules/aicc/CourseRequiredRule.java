package dev.jcputney.elearning.parser.validators.rules.aicc;

import dev.jcputney.elearning.parser.input.aicc.AiccManifest;
import dev.jcputney.elearning.parser.validation.ValidationIssue;
import dev.jcputney.elearning.parser.validation.ValidationResult;
import dev.jcputney.elearning.parser.validators.rules.ValidationRule;

/**
 * Validates that an AICC manifest contains course information.
 *
 * <p>According to AICC specification, every AICC package must have a course (.crs) file
 * that defines the basic course properties.</p>
 *
 * @see <a href="https://www.aicc.org/aicc-cmi-guidelines">AICC CMI Guidelines</a>
 */
public class CourseRequiredRule implements ValidationRule<AiccManifest> {

  /**
   * Validates that the manifest contains course information.
   *
   * @param manifest The AICC manifest to validate (must not be null)
   * @return ValidationResult containing any issues found
   * @throws IllegalArgumentException if manifest is null
   */
  @Override
  public ValidationResult validate(AiccManifest manifest) {
    if (manifest == null) {
      throw new IllegalArgumentException("manifest must not be null");
    }

    if (manifest.getCourse() == null) {
      return ValidationResult.of(
          ValidationIssue.error(
              "AICC_MISSING_COURSE",
              "AICC manifest must contain course information",
              "course.crs"
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
    return "AICC CMI Guidelines - Course Structure File (.crs)";
  }
}
