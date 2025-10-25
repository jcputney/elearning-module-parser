package dev.jcputney.elearning.parser.validators.rules.cmi5;

import dev.jcputney.elearning.parser.input.cmi5.Cmi5Manifest;
import dev.jcputney.elearning.parser.validation.ValidationIssue;
import dev.jcputney.elearning.parser.validation.ValidationResult;
import dev.jcputney.elearning.parser.validators.rules.ValidationRule;

/**
 * Validates that a cmi5 course has a title.
 *
 * <p>According to cmi5 specification, the title element in the course is required
 * and must not be empty.</p>
 *
 * <p>This rule defers validation when the course is null, as that is handled by
 * {@link CourseRequiredRule}.</p>
 *
 * @see <a href="https://github.com/AICC/CMI-5_Spec_Current/blob/quartz/cmi5_spec.md">cmi5 Specification</a>
 */
public class TitleRequiredRule implements ValidationRule<Cmi5Manifest> {

  /**
   * Validates that the course has a non-empty title.
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

    // Defer to CourseRequiredRule for null course
    if (manifest.getCourse() == null) {
      return ValidationResult.valid();
    }

    String title = manifest.getTitle();
    if (title == null || title.isEmpty()) {
      return ValidationResult.of(
          ValidationIssue.error(
              "CMI5_MISSING_TITLE",
              "cmi5 course must have a title",
              "cmi5.xml/course/title",
              "Add a <title> element to the course"
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
    return "cmi5 Specification - Course Structure - title element";
  }
}
