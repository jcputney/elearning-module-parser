package dev.jcputney.elearning.parser.validators.rules.common;

import static org.assertj.core.api.Assertions.assertThat;

import dev.jcputney.elearning.parser.input.scorm12.Scorm12Manifest;
import dev.jcputney.elearning.parser.input.scorm12.ims.cp.Scorm12Resource;
import dev.jcputney.elearning.parser.input.scorm12.ims.cp.Scorm12Resources;
import dev.jcputney.elearning.parser.validation.ValidationResult;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PathSecurityRuleTest {

  private PathSecurityRule rule;

  @BeforeEach
  void setUp() {
    rule = new PathSecurityRule();
  }

  @Test
  void validate_withSafePaths_returnsValid() {
    Scorm12Manifest manifest = new Scorm12Manifest();
    Scorm12Resources resources = new Scorm12Resources();

    Scorm12Resource resource = new Scorm12Resource();
    resource.setIdentifier("res1");
    resource.setHref("content/page.html");
    resources.setResourceList(Collections.singletonList(resource));
    manifest.setResources(resources);

    ValidationResult result = rule.validate(manifest);

    assertThat(result.isValid()).isTrue();
    assertThat(result.getAllIssues()).isEmpty();
  }

  @Test
  void validate_withPathTraversal_returnsError() {
    Scorm12Manifest manifest = new Scorm12Manifest();
    Scorm12Resources resources = new Scorm12Resources();

    Scorm12Resource resource = new Scorm12Resource();
    resource.setIdentifier("res1");
    resource.setHref("../../../etc/passwd");
    resources.setResourceList(Collections.singletonList(resource));
    manifest.setResources(resources);

    ValidationResult result = rule.validate(manifest);

    assertThat(result.isValid()).isFalse();
    assertThat(result.getErrors()).hasSize(1);
    assertThat(result.getErrors().get(0).code()).isEqualTo("UNSAFE_PATH_TRAVERSAL");
    assertThat(result.getErrors().get(0).message()).contains("../../../etc/passwd");
  }

  @Test
  void validate_withAbsolutePath_returnsError() {
    Scorm12Manifest manifest = new Scorm12Manifest();
    Scorm12Resources resources = new Scorm12Resources();

    Scorm12Resource resource = new Scorm12Resource();
    resource.setIdentifier("res1");
    resource.setHref("/usr/local/content.html");
    resources.setResourceList(Collections.singletonList(resource));
    manifest.setResources(resources);

    ValidationResult result = rule.validate(manifest);

    assertThat(result.isValid()).isFalse();
    assertThat(result.getErrors()).hasSize(1);
    assertThat(result.getErrors().get(0).code()).isEqualTo("UNSAFE_ABSOLUTE_PATH");
  }

  @Test
  void validate_withExternalUrl_returnsError() {
    Scorm12Manifest manifest = new Scorm12Manifest();
    Scorm12Resources resources = new Scorm12Resources();

    Scorm12Resource resource = new Scorm12Resource();
    resource.setIdentifier("res1");
    resource.setHref("http://evil.com/malware.js");
    resources.setResourceList(Collections.singletonList(resource));
    manifest.setResources(resources);

    ValidationResult result = rule.validate(manifest);

    assertThat(result.isValid()).isFalse();
    assertThat(result.getErrors()).hasSize(1);
    assertThat(result.getErrors().get(0).code()).isEqualTo("UNSAFE_EXTERNAL_URL");
  }

  @Test
  void validate_withWindowsAbsolutePath_returnsError() {
    Scorm12Manifest manifest = new Scorm12Manifest();
    Scorm12Resources resources = new Scorm12Resources();

    Scorm12Resource resource = new Scorm12Resource();
    resource.setIdentifier("res1");
    resource.setHref("C:\\Windows\\system32\\evil.exe");
    resources.setResourceList(Collections.singletonList(resource));
    manifest.setResources(resources);

    ValidationResult result = rule.validate(manifest);

    assertThat(result.isValid()).isFalse();
    assertThat(result.getErrors().get(0).code()).isEqualTo("UNSAFE_ABSOLUTE_PATH");
  }

  @Test
  void validate_withMultipleUnsafePaths_returnsMultipleErrors() {
    Scorm12Manifest manifest = new Scorm12Manifest();
    Scorm12Resources resources = new Scorm12Resources();

    Scorm12Resource res1 = new Scorm12Resource();
    res1.setIdentifier("res1");
    res1.setHref("../traversal.html");

    Scorm12Resource res2 = new Scorm12Resource();
    res2.setIdentifier("res2");
    res2.setHref("http://external.com/bad.js");

    resources.setResourceList(List.of(res1, res2));
    manifest.setResources(resources);

    ValidationResult result = rule.validate(manifest);

    assertThat(result.isValid()).isFalse();
    assertThat(result.getErrors()).hasSize(2);
  }
}
