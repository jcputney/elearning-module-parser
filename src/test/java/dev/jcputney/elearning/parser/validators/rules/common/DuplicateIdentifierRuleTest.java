package dev.jcputney.elearning.parser.validators.rules.common;

import static org.assertj.core.api.Assertions.assertThat;

import dev.jcputney.elearning.parser.input.scorm12.Scorm12Manifest;
import dev.jcputney.elearning.parser.input.scorm12.ims.cp.Scorm12Organization;
import dev.jcputney.elearning.parser.input.scorm12.ims.cp.Scorm12Organizations;
import dev.jcputney.elearning.parser.input.scorm12.ims.cp.Scorm12Resource;
import dev.jcputney.elearning.parser.input.scorm12.ims.cp.Scorm12Resources;
import dev.jcputney.elearning.parser.validation.ValidationResult;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DuplicateIdentifierRuleTest {

  private DuplicateIdentifierRule rule;

  @BeforeEach
  void setUp() {
    rule = new DuplicateIdentifierRule();
  }

  @Test
  void validate_withUniqueIdentifiers_returnsValid() {
    // Create manifest with unique identifiers
    Scorm12Manifest manifest = new Scorm12Manifest();
    manifest.setIdentifier("manifest1");

    Scorm12Organizations organizations = new Scorm12Organizations();
    Scorm12Organization org = new Scorm12Organization();
    org.setIdentifier("org1");
    organizations.setOrganizationList(Collections.singletonList(org));
    manifest.setOrganizations(organizations);

    Scorm12Resources resources = new Scorm12Resources();
    Scorm12Resource resource = new Scorm12Resource();
    resource.setIdentifier("res1");
    resources.setResourceList(Collections.singletonList(resource));
    manifest.setResources(resources);

    ValidationResult result = rule.validate(manifest);

    assertThat(result.isValid()).isTrue();
    assertThat(result.getAllIssues()).isEmpty();
  }

  @Test
  void validate_withDuplicateOrgIdentifiers_returnsError() {
    Scorm12Manifest manifest = new Scorm12Manifest();
    manifest.setIdentifier("manifest1");

    Scorm12Organizations organizations = new Scorm12Organizations();
    Scorm12Organization org1 = new Scorm12Organization();
    org1.setIdentifier("duplicate_id");
    Scorm12Organization org2 = new Scorm12Organization();
    org2.setIdentifier("duplicate_id"); // Same ID!
    organizations.setOrganizationList(List.of(org1, org2));
    manifest.setOrganizations(organizations);

    ValidationResult result = rule.validate(manifest);

    assertThat(result.isValid()).isFalse();
    assertThat(result.hasErrors()).isTrue();
    assertThat(result.getErrors()).hasSize(1);
    assertThat(result.getErrors().get(0).code()).isEqualTo("DUPLICATE_IDENTIFIER");
    assertThat(result.getErrors().get(0).message()).contains("duplicate_id");
    assertThat(result.getErrors().get(0).message()).contains("2 times");
  }

  @Test
  void validate_withDuplicateResourceIdentifiers_returnsError() {
    Scorm12Manifest manifest = new Scorm12Manifest();

    Scorm12Resources resources = new Scorm12Resources();
    Scorm12Resource res1 = new Scorm12Resource();
    res1.setIdentifier("res_duplicate");
    Scorm12Resource res2 = new Scorm12Resource();
    res2.setIdentifier("res_duplicate"); // Same ID!
    resources.setResourceList(List.of(res1, res2));
    manifest.setResources(resources);

    ValidationResult result = rule.validate(manifest);

    assertThat(result.isValid()).isFalse();
    assertThat(result.getErrors()).hasSize(1);
    assertThat(result.getErrors().get(0).code()).isEqualTo("DUPLICATE_IDENTIFIER");
  }

  @Test
  void validate_withManifestIdMatchingOrgId_returnsError() {
    Scorm12Manifest manifest = new Scorm12Manifest();
    manifest.setIdentifier("shared_id");

    Scorm12Organizations organizations = new Scorm12Organizations();
    Scorm12Organization org = new Scorm12Organization();
    org.setIdentifier("shared_id"); // Same as manifest!
    organizations.setOrganizationList(Collections.singletonList(org));
    manifest.setOrganizations(organizations);

    ValidationResult result = rule.validate(manifest);

    assertThat(result.isValid()).isFalse();
    assertThat(result.getErrors()).hasSize(1);
    assertThat(result.getErrors().get(0).message()).contains("shared_id");
  }
}
