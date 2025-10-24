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
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Validates that all file paths in SCORM 2004 manifest are safe and don't contain
 * directory traversal patterns, absolute paths, or external references.
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

  private static final Pattern PATH_TRAVERSAL = Pattern.compile("\\.\\./|\\.\\\\");
  private static final Pattern ABSOLUTE_PATH = Pattern.compile("^[/\\\\]|^[a-zA-Z]:");
  private static final Pattern EXTERNAL_URL = Pattern.compile("^(https?:)?//");
  private static final Pattern NULL_BYTE = Pattern.compile("\\x00");

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

    if (manifest.getResources() != null && manifest.getResources().getResourceList() != null) {
      for (Scorm2004Resource resource : manifest.getResources().getResourceList()) {
        // Check resource href
        if (resource.getHref() != null) {
          validatePath(resource.getHref(),
              "resources/resource[@identifier='" + resource.getIdentifier() + "']/@href",
              issues);
        }

        // Check file hrefs
        if (resource.getFiles() != null) {
          for (Scorm2004File file : resource.getFiles()) {
            if (file.getHref() != null) {
              validatePath(file.getHref(),
                  "resources/resource[@identifier='" + resource.getIdentifier() + "']/file/@href",
                  issues);
            }
          }
        }
      }
    }

    return ValidationResult.of(issues.toArray(new ValidationIssue[0]));
  }

  private void validatePath(String path, String location, List<ValidationIssue> issues) {
    if (PATH_TRAVERSAL.matcher(path).find()) {
      issues.add(ValidationIssue.error(
          "UNSAFE_PATH_TRAVERSAL",
          String.format("Path contains directory traversal pattern: '%s'", path),
          location,
          "Remove '../' or '..\' from the path. All content should be within the package."
      ));
    } else if (ABSOLUTE_PATH.matcher(path).find()) {
      issues.add(ValidationIssue.error(
          "UNSAFE_ABSOLUTE_PATH",
          String.format("Path is absolute but should be relative: '%s'", path),
          location,
          "Use relative paths only. Remove leading '/' or drive letter."
      ));
    } else if (EXTERNAL_URL.matcher(path).find()) {
      issues.add(ValidationIssue.error(
          "UNSAFE_EXTERNAL_URL",
          String.format("Path references external URL: '%s'", path),
          location,
          "All resources must be packaged within the content. Remove external URL."
      ));
    } else if (NULL_BYTE.matcher(path).find()) {
      issues.add(ValidationIssue.error(
          "UNSAFE_NULL_BYTE",
          String.format("Path contains null byte: '%s'", path),
          location,
          "Remove null bytes from path."
      ));
    }
  }
}
