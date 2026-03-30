package dev.jcputney.elearning.parser.validators.rules.common;

import static org.assertj.core.api.Assertions.assertThat;

import dev.jcputney.elearning.parser.validation.ValidationIssue;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

class PathValidationUtilsTest {

  @Test
  void validatePath_withSafePath_producesNoIssues() {
    List<ValidationIssue> issues = new ArrayList<>();

    PathValidationUtils.validatePath("content/page.html", "test/location", issues);

    assertThat(issues).isEmpty();
  }

  @Test
  void validatePath_withPathTraversal_detectsTraversal() {
    List<ValidationIssue> issues = new ArrayList<>();

    PathValidationUtils.validatePath("../../../etc/passwd", "test/location", issues);

    assertThat(issues).hasSize(1);
    assertThat(issues.get(0).code()).isEqualTo("UNSAFE_PATH_TRAVERSAL");
    assertThat(issues.get(0).message()).contains("../../../etc/passwd");
  }

  @Test
  void validatePath_withWindowsPathTraversal_detectsTraversal() {
    List<ValidationIssue> issues = new ArrayList<>();

    PathValidationUtils.validatePath("..\\windows\\system32", "test/location", issues);

    assertThat(issues).hasSize(1);
    assertThat(issues.get(0).code()).isEqualTo("UNSAFE_PATH_TRAVERSAL");
    assertThat(issues.get(0).message()).contains("..\\windows\\system32");
  }

  @Test
  void validatePath_withAbsolutePath_detectsAbsolutePath() {
    List<ValidationIssue> issues = new ArrayList<>();

    PathValidationUtils.validatePath("/absolute/path", "test/location", issues);

    assertThat(issues).hasSize(1);
    assertThat(issues.get(0).code()).isEqualTo("UNSAFE_ABSOLUTE_PATH");
    assertThat(issues.get(0).message()).contains("/absolute/path");
  }

  @Test
  void validatePath_withWindowsDriveLetter_detectsAbsolutePath() {
    List<ValidationIssue> issues = new ArrayList<>();

    PathValidationUtils.validatePath("C:\\windows\\file.txt", "test/location", issues);

    assertThat(issues).hasSize(1);
    assertThat(issues.get(0).code()).isEqualTo("UNSAFE_ABSOLUTE_PATH");
    assertThat(issues.get(0).message()).contains("C:\\windows\\file.txt");
  }

  @Test
  void validatePath_withExternalUrl_detectsExternalUrl() {
    List<ValidationIssue> issues = new ArrayList<>();

    PathValidationUtils.validatePath("https://evil.com/payload", "test/location", issues);

    assertThat(issues).hasSize(1);
    assertThat(issues.get(0).code()).isEqualTo("UNSAFE_EXTERNAL_URL");
    assertThat(issues.get(0).message()).contains("https://evil.com/payload");
  }

  @Test
  void validatePath_withNullByte_detectsNullByte() {
    List<ValidationIssue> issues = new ArrayList<>();

    PathValidationUtils.validatePath("file\u0000.txt", "test/location", issues);

    assertThat(issues).hasSize(1);
    assertThat(issues.get(0).code()).isEqualTo("UNSAFE_NULL_BYTE");
    assertThat(issues.get(0).message()).contains("file");
  }
}
