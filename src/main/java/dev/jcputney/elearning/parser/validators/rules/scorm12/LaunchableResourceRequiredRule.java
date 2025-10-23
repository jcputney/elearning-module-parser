package dev.jcputney.elearning.parser.validators.rules.scorm12;

import dev.jcputney.elearning.parser.input.scorm12.Scorm12Manifest;
import dev.jcputney.elearning.parser.input.scorm12.ims.cp.Scorm12Item;
import dev.jcputney.elearning.parser.input.scorm12.ims.cp.Scorm12Organization;
import dev.jcputney.elearning.parser.validation.ValidationIssue;
import dev.jcputney.elearning.parser.validation.ValidationResult;
import dev.jcputney.elearning.parser.validators.rules.ValidationRule;
import java.util.ArrayList;
import java.util.List;

/**
 * Validates that every organization has at least one launchable resource.
 * A launchable resource is an item with a non-null, non-empty identifierref.
 * Required by SCORM 1.2 CAM specification.
 */
public class LaunchableResourceRequiredRule implements ValidationRule<Scorm12Manifest> {

  @Override
  public String getRuleName() {
    return "Launchable Resource Required";
  }

  @Override
  public String getSpecReference() {
    return "SCORM 1.2 CAM 2.3.3";
  }

  @Override
  public ValidationResult validate(Scorm12Manifest manifest) {
    if (manifest == null) {
      throw new IllegalArgumentException("manifest must not be null");
    }

    List<ValidationIssue> issues = new ArrayList<>();

    // Check each organization for at least one launchable resource
    if (manifest.getOrganizations() != null && manifest.getOrganizations().getOrganizationList() != null) {
      for (Scorm12Organization org : manifest.getOrganizations().getOrganizationList()) {
        if (!hasLaunchableResource(org)) {
          issues.add(ValidationIssue.error(
              "SCORM12_NO_LAUNCHABLE_RESOURCE",
              "Organization '" + org.getIdentifier() + "' has no launchable resources",
              "organization[@identifier='" + org.getIdentifier() + "']",
              "Add at least one item with an identifierref attribute to reference a launchable resource"
          ));
        }
      }
    }

    return ValidationResult.of(issues.toArray(new ValidationIssue[0]));
  }

  /**
   * Checks if an organization has at least one launchable resource.
   * Recursively checks all items and their children.
   */
  private boolean hasLaunchableResource(Scorm12Organization org) {
    if (org.getItems() == null) {
      return false;
    }

    for (Scorm12Item item : org.getItems()) {
      if (isLaunchable(item)) {
        return true;
      }
    }

    return false;
  }

  /**
   * Recursively checks if an item or any of its children is launchable.
   */
  private boolean isLaunchable(Scorm12Item item) {
    // Check if this item is launchable
    String identifierRef = item.getIdentifierRef();
    if (identifierRef != null && !identifierRef.isEmpty()) {
      return true;
    }

    // Recursively check child items
    if (item.getItems() != null) {
      for (Scorm12Item childItem : item.getItems()) {
        if (isLaunchable(childItem)) {
          return true;
        }
      }
    }

    return false;
  }
}
