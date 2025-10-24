package dev.jcputney.elearning.parser.validators.rules.scorm2004;

import static org.assertj.core.api.Assertions.assertThat;

import dev.jcputney.elearning.parser.input.scorm2004.Scorm2004Manifest;
import dev.jcputney.elearning.parser.input.scorm2004.ims.cp.Scorm2004Item;
import dev.jcputney.elearning.parser.input.scorm2004.ims.cp.Scorm2004Organization;
import dev.jcputney.elearning.parser.input.scorm2004.ims.cp.Scorm2004Organizations;
import dev.jcputney.elearning.parser.input.scorm2004.ims.cp.Scorm2004Resource;
import dev.jcputney.elearning.parser.input.scorm2004.ims.cp.Scorm2004Resources;
import dev.jcputney.elearning.parser.validation.ValidationResult;
import java.util.Collections;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ResourceHrefRequiredRuleTest {

  private ResourceHrefRequiredRule rule;

  @BeforeEach
  void setUp() {
    rule = new ResourceHrefRequiredRule();
  }

  @Test
  void validate_withResourceHref_returnsValid() {
    Scorm2004Manifest manifest = new Scorm2004Manifest();

    // Create resource with href
    Scorm2004Resources resources = new Scorm2004Resources();
    Scorm2004Resource resource = new Scorm2004Resource();
    resource.setIdentifier("res1");
    resource.setHref("content.html");
    resources.setResourceList(Collections.singletonList(resource));
    manifest.setResources(resources);

    // Create item referencing it
    Scorm2004Organizations organizations = new Scorm2004Organizations();
    Scorm2004Organization org = new Scorm2004Organization();
    org.setIdentifier("org1");

    Scorm2004Item item = new Scorm2004Item();
    item.setIdentifier("item1");
    item.setIdentifierRef("res1");
    org.setItems(Collections.singletonList(item));

    organizations.setOrganizationList(Collections.singletonList(org));
    manifest.setOrganizations(organizations);

    ValidationResult result = rule.validate(manifest);

    assertThat(result.isValid()).isTrue();
    assertThat(result.getAllIssues()).isEmpty();
  }

  @Test
  void validate_withMissingHref_returnsError() {
    Scorm2004Manifest manifest = new Scorm2004Manifest();

    // Create resource WITHOUT href
    Scorm2004Resources resources = new Scorm2004Resources();
    Scorm2004Resource resource = new Scorm2004Resource();
    resource.setIdentifier("res1");
    // No href set!
    resources.setResourceList(Collections.singletonList(resource));
    manifest.setResources(resources);

    // Create item referencing it
    Scorm2004Organizations organizations = new Scorm2004Organizations();
    Scorm2004Organization org = new Scorm2004Organization();
    org.setIdentifier("org1");

    Scorm2004Item item = new Scorm2004Item();
    item.setIdentifier("item1");
    item.setIdentifierRef("res1");
    org.setItems(Collections.singletonList(item));

    organizations.setOrganizationList(Collections.singletonList(org));
    manifest.setOrganizations(organizations);

    ValidationResult result = rule.validate(manifest);

    assertThat(result.isValid()).isFalse();
    assertThat(result.getErrors()).hasSize(1);
    assertThat(result.getErrors().get(0).code()).isEqualTo("SCORM2004_MISSING_LAUNCH_URL");
    assertThat(result.getErrors().get(0).message()).contains("res1");
  }

  @Test
  void validate_withEmptyHref_returnsError() {
    Scorm2004Manifest manifest = new Scorm2004Manifest();

    // Create resource with empty href
    Scorm2004Resources resources = new Scorm2004Resources();
    Scorm2004Resource resource = new Scorm2004Resource();
    resource.setIdentifier("res1");
    resource.setHref("   "); // Empty/whitespace
    resources.setResourceList(Collections.singletonList(resource));
    manifest.setResources(resources);

    // Create item referencing it
    Scorm2004Organizations organizations = new Scorm2004Organizations();
    Scorm2004Organization org = new Scorm2004Organization();
    org.setIdentifier("org1");

    Scorm2004Item item = new Scorm2004Item();
    item.setIdentifier("item1");
    item.setIdentifierRef("res1");
    org.setItems(Collections.singletonList(item));

    organizations.setOrganizationList(Collections.singletonList(org));
    manifest.setOrganizations(organizations);

    ValidationResult result = rule.validate(manifest);

    assertThat(result.isValid()).isFalse();
    assertThat(result.getErrors()).hasSize(1);
  }

  @Test
  void validate_withUnreferencedResourceMissingHref_returnsValid() {
    Scorm2004Manifest manifest = new Scorm2004Manifest();

    // Create two resources: one referenced (with href), one not referenced (without href)
    Scorm2004Resources resources = new Scorm2004Resources();
    Scorm2004Resource res1 = new Scorm2004Resource();
    res1.setIdentifier("res1");
    res1.setHref("content.html");

    Scorm2004Resource res2 = new Scorm2004Resource();
    res2.setIdentifier("unreferenced");
    // No href, but also not referenced - should be OK for this rule

    resources.setResourceList(java.util.List.of(res1, res2));
    manifest.setResources(resources);

    // Create item only referencing res1
    Scorm2004Organizations organizations = new Scorm2004Organizations();
    Scorm2004Organization org = new Scorm2004Organization();
    org.setIdentifier("org1");

    Scorm2004Item item = new Scorm2004Item();
    item.setIdentifier("item1");
    item.setIdentifierRef("res1");
    org.setItems(Collections.singletonList(item));

    organizations.setOrganizationList(Collections.singletonList(org));
    manifest.setOrganizations(organizations);

    ValidationResult result = rule.validate(manifest);

    assertThat(result.isValid()).isTrue();
  }
}
