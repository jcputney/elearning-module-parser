package dev.jcputney.elearning.parser.validators.rules.common;

import dev.jcputney.elearning.parser.input.scorm12.Scorm12Manifest;
import dev.jcputney.elearning.parser.input.scorm12.ims.cp.Scorm12File;
import dev.jcputney.elearning.parser.input.scorm12.ims.cp.Scorm12Resource;
import dev.jcputney.elearning.parser.validation.ValidationIssue;
import dev.jcputney.elearning.parser.validation.ValidationResult;
import dev.jcputney.elearning.parser.validators.rules.ValidationRule;
import java.util.ArrayList;
import java.util.List;

/**
 * Validates that all file paths in the manifest are safe and don't contain directory traversal
 * patterns, absolute paths, or external references.
 *
 * <p>Security Requirements:</p>
 * <ul>
 *   <li>No path traversal (../ or ..\)</li>
 *   <li>No absolute paths (/path or C:\path)</li>
 *   <li>No external URLs (http://, https://, //)</li>
 *   <li>No null bytes or control characters</li>
 * </ul>
 */
public class PathSecurityRule implements ValidationRule<Scorm12Manifest> {

  @Override
  public String getRuleName() {
    return "Path Security Validation";
  }

  @Override
  public String getSpecReference() {
    return "Security Best Practice";
  }

  @Override
  public ValidationResult validate(Scorm12Manifest manifest) {
    if (manifest == null) {
      throw new IllegalArgumentException("manifest must not be null");
    }

    List<ValidationIssue> issues = new ArrayList<>();

    if (manifest.getResources() != null && manifest
        .getResources()
        .getResourceList() != null) {
      for (Scorm12Resource resource : manifest
          .getResources()
          .getResourceList()) {
        // Check resource href
        if (resource.getHref() != null) {
          PathValidationUtils.validatePath(resource.getHref(),
              "resources/resource[@identifier='" + resource.getIdentifier() + "']/@href",
              issues);
        }

        // Check file hrefs
        if (resource.getFiles() != null) {
          for (Scorm12File file : resource.getFiles()) {
            if (file.getHref() != null) {
              PathValidationUtils.validatePath(file.getHref(),
                  "resources/resource[@identifier='" + resource.getIdentifier() + "']/file/@href",
                  issues);
            }
          }
        }
      }
    }

    return ValidationResult.of(issues.toArray(new ValidationIssue[0]));
  }

}
