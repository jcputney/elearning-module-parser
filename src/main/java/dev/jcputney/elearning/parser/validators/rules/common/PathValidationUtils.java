package dev.jcputney.elearning.parser.validators.rules.common;

import dev.jcputney.elearning.parser.validation.ValidationIssue;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Shared utility for validating file paths against common security concerns such as directory
 * traversal, absolute paths, external URLs, and null bytes.
 *
 * <p>Used by both SCORM 1.2 and SCORM 2004 path security rules to eliminate duplicated
 * validation logic.</p>
 */
public final class PathValidationUtils {

  private static final Pattern PATH_TRAVERSAL = Pattern.compile("\\.\\./|\\.\\\\");
  private static final Pattern ABSOLUTE_PATH = Pattern.compile("^[/\\\\]|^[a-zA-Z]:");
  private static final Pattern EXTERNAL_URL = Pattern.compile("^(https?:)?//");
  private static final Pattern NULL_BYTE = Pattern.compile("\\x00");

  private PathValidationUtils() {
    throw new AssertionError("Utility class");
  }

  /**
   * Validates a file path for common security issues and adds any detected issues to the provided
   * list.
   *
   * @param path the file path to validate
   * @param location the XPath-like location in the manifest where this path was found
   * @param issues the list to which any detected validation issues will be added
   */
  public static void validatePath(String path, String location, List<ValidationIssue> issues) {
    if (PATH_TRAVERSAL
        .matcher(path)
        .find()) {
      issues.add(ValidationIssue.error(
          "UNSAFE_PATH_TRAVERSAL",
          String.format("Path contains directory traversal pattern: '%s'", path),
          location,
          "Remove '../' or '..' from the path. All content should be within the package."
      ));
    } else if (ABSOLUTE_PATH
        .matcher(path)
        .find()) {
      issues.add(ValidationIssue.error(
          "UNSAFE_ABSOLUTE_PATH",
          String.format("Path is absolute but should be relative: '%s'", path),
          location,
          "Use relative paths only. Remove leading '/' or drive letter."
      ));
    } else if (EXTERNAL_URL
        .matcher(path)
        .find()) {
      issues.add(ValidationIssue.error(
          "UNSAFE_EXTERNAL_URL",
          String.format("Path references external URL: '%s'", path),
          location,
          "All resources must be packaged within the content. Remove external URL."
      ));
    } else if (NULL_BYTE
        .matcher(path)
        .find()) {
      issues.add(ValidationIssue.error(
          "UNSAFE_NULL_BYTE",
          String.format("Path contains null byte: '%s'", path),
          location,
          "Remove null bytes from path."
      ));
    }
  }
}
