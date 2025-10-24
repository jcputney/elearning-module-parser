package dev.jcputney.elearning.parser.validators.rules.scorm2004;

import static org.assertj.core.api.Assertions.assertThat;

import dev.jcputney.elearning.parser.input.scorm2004.Scorm2004Manifest;
import dev.jcputney.elearning.parser.input.scorm2004.ims.cp.Scorm2004Organization;
import dev.jcputney.elearning.parser.input.scorm2004.ims.cp.Scorm2004Organizations;
import dev.jcputney.elearning.parser.validation.ValidationResult;
import java.util.Collections;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DefaultOrganizationValidRuleTest {

  private DefaultOrganizationValidRule rule;

  @BeforeEach
  void setUp() {
    rule = new DefaultOrganizationValidRule();
  }

  @Test
  void validate_withValidDefaultOrganization_returnsValid() {
    Scorm2004Manifest manifest = new Scorm2004Manifest();
    Scorm2004Organizations organizations = new Scorm2004Organizations();
    organizations.setDefaultOrganization("org1");

    Scorm2004Organization org = new Scorm2004Organization();
    org.setIdentifier("org1");
    organizations.setOrganizationList(Collections.singletonList(org));

    manifest.setOrganizations(organizations);

    ValidationResult result = rule.validate(manifest);

    assertThat(result.isValid()).isTrue();
    assertThat(result.getAllIssues()).isEmpty();
  }

  @Test
  void validate_withInvalidDefaultOrganization_returnsError() {
    Scorm2004Manifest manifest = new Scorm2004Manifest();
    Scorm2004Organizations organizations = new Scorm2004Organizations();
    organizations.setDefaultOrganization("nonexistent");

    Scorm2004Organization org = new Scorm2004Organization();
    org.setIdentifier("org1");
    organizations.setOrganizationList(Collections.singletonList(org));

    manifest.setOrganizations(organizations);

    ValidationResult result = rule.validate(manifest);

    assertThat(result.isValid()).isFalse();
    assertThat(result.getErrors()).hasSize(1);
    assertThat(result.getErrors().get(0).code()).isEqualTo("SCORM2004_INVALID_DEFAULT_ORG");
    assertThat(result.getErrors().get(0).message()).contains("nonexistent");
  }

  @Test
  void validate_withNoDefaultAttribute_returnsValid() {
    Scorm2004Manifest manifest = new Scorm2004Manifest();
    Scorm2004Organizations organizations = new Scorm2004Organizations();
    // No default set

    Scorm2004Organization org = new Scorm2004Organization();
    org.setIdentifier("org1");
    organizations.setOrganizationList(Collections.singletonList(org));

    manifest.setOrganizations(organizations);

    ValidationResult result = rule.validate(manifest);

    assertThat(result.isValid()).isTrue();
  }

  @Test
  void validate_withEmptyDefaultAttribute_returnsValid() {
    Scorm2004Manifest manifest = new Scorm2004Manifest();
    Scorm2004Organizations organizations = new Scorm2004Organizations();
    organizations.setDefaultOrganization("  "); // Empty/whitespace

    manifest.setOrganizations(organizations);

    ValidationResult result = rule.validate(manifest);

    assertThat(result.isValid()).isTrue();
  }
}
