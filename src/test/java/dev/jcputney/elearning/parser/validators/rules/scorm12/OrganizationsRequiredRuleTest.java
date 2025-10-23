package dev.jcputney.elearning.parser.validators.rules.scorm12;

import static org.assertj.core.api.Assertions.assertThat;

import dev.jcputney.elearning.parser.input.scorm12.Scorm12Manifest;
import dev.jcputney.elearning.parser.input.scorm12.ims.cp.Scorm12Organizations;
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
    Scorm12Manifest manifest = new Scorm12Manifest();
    manifest.setOrganizations(new Scorm12Organizations());

    ValidationResult result = rule.validate(manifest);

    assertThat(result.isValid()).isTrue();
  }

  @Test
  void validate_withoutOrganizations_returnsError() {
    Scorm12Manifest manifest = new Scorm12Manifest();
    manifest.setOrganizations(null);

    ValidationResult result = rule.validate(manifest);

    assertThat(result.isValid()).isFalse();
    assertThat(result.getErrors()).hasSize(1);
    assertThat(result.getErrors().get(0).code()).isEqualTo("SCORM12_MISSING_ORGANIZATIONS");
  }
}
