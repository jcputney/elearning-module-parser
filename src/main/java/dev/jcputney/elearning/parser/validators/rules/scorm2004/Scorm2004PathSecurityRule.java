/*
 * Copyright (c) 2024-2025. Jonathan Putney
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package dev.jcputney.elearning.parser.validators.rules.scorm2004;

import dev.jcputney.elearning.parser.input.scorm2004.Scorm2004Manifest;
import dev.jcputney.elearning.parser.input.scorm2004.ims.cp.Scorm2004File;
import dev.jcputney.elearning.parser.input.scorm2004.ims.cp.Scorm2004Resource;
import dev.jcputney.elearning.parser.validation.ValidationIssue;
import dev.jcputney.elearning.parser.validation.ValidationResult;
import dev.jcputney.elearning.parser.validators.rules.ValidationRule;
import dev.jcputney.elearning.parser.validators.rules.common.PathValidationUtils;
import java.util.ArrayList;
import java.util.List;

/**
 * Validates that all file paths in SCORM 2004 manifest are safe and don't contain directory
 * traversal patterns, absolute paths, or external references.
 *
 * <p>Security Requirements:</p>
 * <ul>
 *   <li>No path traversal (../ or ..\)</li>
 *   <li>No absolute paths (/path or C:\path)</li>
 *   <li>No external URLs (http://, https://, //)</li>
 *   <li>No null bytes or control characters</li>
 * </ul>
 */
public class Scorm2004PathSecurityRule implements ValidationRule<Scorm2004Manifest> {

  @Override
  public String getRuleName() {
    return "Path Security Validation";
  }

  @Override
  public String getSpecReference() {
    return "Security Best Practice";
  }

  @Override
  public ValidationResult validate(Scorm2004Manifest manifest) {
    if (manifest == null) {
      throw new IllegalArgumentException("manifest must not be null");
    }

    List<ValidationIssue> issues = new ArrayList<>();

    if (manifest.getResources() != null && manifest
        .getResources()
        .getResourceList() != null) {
      for (Scorm2004Resource resource : manifest
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
          for (Scorm2004File file : resource.getFiles()) {
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
