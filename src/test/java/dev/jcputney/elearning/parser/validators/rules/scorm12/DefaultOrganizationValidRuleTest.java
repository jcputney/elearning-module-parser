package dev.jcputney.elearning.parser.validators.rules.scorm12;

import static org.assertj.core.api.Assertions.assertThat;

import dev.jcputney.elearning.parser.input.scorm12.Scorm12Manifest;
import dev.jcputney.elearning.parser.input.scorm12.ims.cp.Scorm12Organization;
import dev.jcputney.elearning.parser.input.scorm12.ims.cp.Scorm12Organizations;
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
  void validate_withDefaultReferencingExistingOrg_returnsValid() {
    Scorm12Manifest manifest = new Scorm12Manifest();
    Scorm12Organizations organizations = new Scorm12Organizations();

    Scorm12Organization org = new Scorm12Organization();
    org.setIdentifier("org1");
    organizations.setOrganizationList(Collections.singletonList(org));
    organizations.setDefaultOrganization("org1");

    manifest.setOrganizations(organizations);

    ValidationResult result = rule.validate(manifest);

    assertThat(result.isValid()).isTrue();
  }

  @Test
  void validate_withNoDefaultAttribute_returnsValid() {
    Scorm12Manifest manifest = new Scorm12Manifest();
    Scorm12Organizations organizations = new Scorm12Organizations();

    Scorm12Organization org = new Scorm12Organization();
    org.setIdentifier("org1");
    organizations.setOrganizationList(Collections.singletonList(org));
    // No default set

    manifest.setOrganizations(organizations);

    ValidationResult result = rule.validate(manifest);

    assertThat(result.isValid()).isTrue();
  }

  @Test
  void validate_withDefaultReferencingNonExistentOrg_returnsError() {
    Scorm12Manifest manifest = new Scorm12Manifest();
    Scorm12Organizations organizations = new Scorm12Organizations();

    Scorm12Organization org = new Scorm12Organization();
    org.setIdentifier("org1");
    organizations.setOrganizationList(Collections.singletonList(org));
    organizations.setDefaultOrganization("nonexistent");

    manifest.setOrganizations(organizations);

    ValidationResult result = rule.validate(manifest);

    assertThat(result.isValid()).isFalse();
    assertThat(result.getErrors()).hasSize(1);
    assertThat(result.getErrors().get(0).code()).isEqualTo("SCORM12_INVALID_DEFAULT_ORG");
    assertThat(result.getErrors().get(0).message()).contains("nonexistent");
  }

  @Test
  void validate_withEmptyDefaultAttribute_returnsValid() {
    Scorm12Manifest manifest = new Scorm12Manifest();
    Scorm12Organizations organizations = new Scorm12Organizations();

    Scorm12Organization org = new Scorm12Organization();
    org.setIdentifier("org1");
    organizations.setOrganizationList(Collections.singletonList(org));
    organizations.setDefaultOrganization("");  // Empty string

    manifest.setOrganizations(organizations);

    ValidationResult result = rule.validate(manifest);

    assertThat(result.isValid()).isTrue();  // Empty treated as unset
  }
}
