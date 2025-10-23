package dev.jcputney.elearning.parser.validators.rules.scorm12;

import static org.assertj.core.api.Assertions.assertThat;

import dev.jcputney.elearning.parser.input.scorm12.Scorm12Manifest;
import dev.jcputney.elearning.parser.input.scorm12.ims.cp.Scorm12Resources;
import dev.jcputney.elearning.parser.validation.ValidationResult;
import java.util.Collections;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ResourcesRequiredRuleTest {

  private ResourcesRequiredRule rule;

  @BeforeEach
  void setUp() {
    rule = new ResourcesRequiredRule();
  }

  @Test
  void validate_withManifestHavingResources_returnsValid() {
    Scorm12Manifest manifest = new Scorm12Manifest();
    manifest.setIdentifier("MANIFEST-001");

    Scorm12Resources resources = new Scorm12Resources();
    resources.setResourceList(Collections.emptyList());
    manifest.setResources(resources);

    ValidationResult result = rule.validate(manifest);

    assertThat(result.isValid()).isTrue();
  }

  @Test
  void validate_withManifestMissingResources_returnsError() {
    Scorm12Manifest manifest = new Scorm12Manifest();
    manifest.setIdentifier("MANIFEST-001");
    manifest.setResources(null);

    ValidationResult result = rule.validate(manifest);

    assertThat(result.isValid()).isFalse();
    assertThat(result.getErrors()).hasSize(1);
    assertThat(result.getErrors().get(0).code()).isEqualTo("SCORM12_MISSING_RESOURCES");
    assertThat(result.getErrors().get(0).message()).contains("resources");
  }
}
