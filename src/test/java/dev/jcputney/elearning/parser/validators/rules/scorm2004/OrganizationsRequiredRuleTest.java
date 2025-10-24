package dev.jcputney.elearning.parser.validators.rules.scorm2004;

import static org.assertj.core.api.Assertions.assertThat;

import dev.jcputney.elearning.parser.input.scorm2004.Scorm2004Manifest;
import dev.jcputney.elearning.parser.input.scorm2004.ims.cp.Scorm2004Organizations;
import dev.jcputney.elearning.parser.validation.ValidationResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class OrganizationsRequiredRuleTest {

  private OrganizationsRequiredRule rule;

  @BeforeEach
  void setUp() {
    rule = new OrganizationsRequiredRule();
  }

  @Test
  void validate_withOrganizations_returnsValid() {
    Scorm2004Manifest manifest = new Scorm2004Manifest();
    Scorm2004Organizations organizations = new Scorm2004Organizations();
    manifest.setOrganizations(organizations);

    ValidationResult result = rule.validate(manifest);

    assertThat(result.isValid()).isTrue();
    assertThat(result.getAllIssues()).isEmpty();
  }

  @Test
  void validate_withoutOrganizations_returnsError() {
    Scorm2004Manifest manifest = new Scorm2004Manifest();
    // No organizations set

    ValidationResult result = rule.validate(manifest);

    assertThat(result.isValid()).isFalse();
    assertThat(result.hasErrors()).isTrue();
    assertThat(result.getErrors()).hasSize(1);
    assertThat(result.getErrors().get(0).code()).isEqualTo("SCORM2004_MISSING_ORGANIZATIONS");
    assertThat(result.getErrors().get(0).message()).contains("organizations");
  }
}
