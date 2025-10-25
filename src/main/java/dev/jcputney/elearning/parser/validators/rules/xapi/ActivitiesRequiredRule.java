package dev.jcputney.elearning.parser.validators.rules.xapi;

import dev.jcputney.elearning.parser.input.xapi.tincan.TincanManifest;
import dev.jcputney.elearning.parser.validation.ValidationIssue;
import dev.jcputney.elearning.parser.validation.ValidationResult;
import dev.jcputney.elearning.parser.validators.rules.ValidationRule;

/**
 * Validates that an xAPI manifest contains at least one activity.
 *
 * <p>According to xAPI specification, every xAPI package must have at least one activity
 * that defines the learning experience.</p>
 *
 * @see <a href="https://github.com/adlnet/xAPI-Spec/blob/master/xAPI-About.md">xAPI Specification</a>
 */
public class ActivitiesRequiredRule implements ValidationRule<TincanManifest> {

  /**
   * Validates that the manifest contains at least one activity.
   *
   * @param manifest The xAPI manifest to validate (must not be null)
   * @return ValidationResult containing any issues found
   * @throws IllegalArgumentException if manifest is null
   */
  @Override
  public ValidationResult validate(TincanManifest manifest) {
    if (manifest == null) {
      throw new IllegalArgumentException("manifest must not be null");
    }

    if (manifest.getActivities() == null || manifest.getActivities().isEmpty()) {
      return ValidationResult.of(
          ValidationIssue.error(
              "XAPI_MISSING_ACTIVITIES",
              "xAPI manifest must contain at least one activity",
              "tincan.xml/activities"
          )
      );
    }

    return ValidationResult.valid();
  }

  @Override
  public String getRuleName() {
    return "ActivitiesRequired";
  }

  @Override
  public String getSpecReference() {
    return "xAPI Specification - Activities (tincan.xml)";
  }
}
