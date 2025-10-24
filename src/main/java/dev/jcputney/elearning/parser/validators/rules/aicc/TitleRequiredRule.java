package dev.jcputney.elearning.parser.validators.rules.aicc;

import dev.jcputney.elearning.parser.input.aicc.AiccManifest;
import dev.jcputney.elearning.parser.validation.ValidationIssue;
import dev.jcputney.elearning.parser.validation.ValidationResult;
import dev.jcputney.elearning.parser.validators.rules.ValidationRule;

/**
 * Validates that an AICC course has a title.
 *
 * <p>According to AICC specification, the course_title field in the .crs file is required
 * and must not be empty.</p>
 *
 * <p>This rule defers validation when the course is null, as that is handled by
 * {@link CourseRequiredRule}.</p>
 *
 * @see <a href="https://www.aicc.org/aicc-cmi-guidelines">AICC CMI Guidelines</a>
 */
public class TitleRequiredRule implements ValidationRule<AiccManifest> {

  /**
   * Validates that the course has a non-empty title.
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

    // Defer to CourseRequiredRule for null course
    if (manifest.getCourse() == null) {
      return ValidationResult.valid();
    }

    String title = manifest.getTitle();
    if (title == null || title.trim().isEmpty()) {
      return ValidationResult.of(
          ValidationIssue.error(
              "AICC_MISSING_TITLE",
              "AICC course must have a title",
              "course.crs",
              "Add a course_title field to the .crs file"
          )
      );
    }

    return ValidationResult.valid();
  }

  @Override
  public String getRuleName() {
    return "TitleRequired";
  }

  @Override
  public String getSpecReference() {
    return "AICC CMI Guidelines - Course Structure File (.crs) - course_title field";
  }
}
