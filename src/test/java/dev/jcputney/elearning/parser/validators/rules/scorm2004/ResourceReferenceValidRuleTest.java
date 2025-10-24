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

class ResourceReferenceValidRuleTest {

  private ResourceReferenceValidRule rule;

  @BeforeEach
  void setUp() {
    rule = new ResourceReferenceValidRule();
  }

  @Test
  void validate_withValidResourceReference_returnsValid() {
    Scorm2004Manifest manifest = new Scorm2004Manifest();

    // Create resource
    Scorm2004Resources resources = new Scorm2004Resources();
    Scorm2004Resource resource = new Scorm2004Resource();
    resource.setIdentifier("res1");
    resource.setHref("content.html");
    resources.setResourceList(Collections.singletonList(resource));
    manifest.setResources(resources);

    // Create item referencing resource
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
  void validate_withInvalidResourceReference_returnsError() {
    Scorm2004Manifest manifest = new Scorm2004Manifest();

    // Create resource
    Scorm2004Resources resources = new Scorm2004Resources();
    Scorm2004Resource resource = new Scorm2004Resource();
    resource.setIdentifier("res1");
    resources.setResourceList(Collections.singletonList(resource));
    manifest.setResources(resources);

    // Create item referencing nonexistent resource
    Scorm2004Organizations organizations = new Scorm2004Organizations();
    Scorm2004Organization org = new Scorm2004Organization();
    org.setIdentifier("org1");

    Scorm2004Item item = new Scorm2004Item();
    item.setIdentifier("item1");
    item.setIdentifierRef("nonexistent"); // Invalid!
    org.setItems(Collections.singletonList(item));

    organizations.setOrganizationList(Collections.singletonList(org));
    manifest.setOrganizations(organizations);

    ValidationResult result = rule.validate(manifest);

    assertThat(result.isValid()).isFalse();
    assertThat(result.getErrors()).hasSize(1);
    assertThat(result.getErrors().get(0).code()).isEqualTo("SCORM2004_MISSING_RESOURCE_REF");
    assertThat(result.getErrors().get(0).message()).contains("nonexistent");
  }

  @Test
  void validate_withNestedInvalidReference_returnsError() {
    Scorm2004Manifest manifest = new Scorm2004Manifest();

    // Create resource
    Scorm2004Resources resources = new Scorm2004Resources();
    Scorm2004Resource resource = new Scorm2004Resource();
    resource.setIdentifier("res1");
    resources.setResourceList(Collections.singletonList(resource));
    manifest.setResources(resources);

    // Create nested items with invalid reference in child
    Scorm2004Organizations organizations = new Scorm2004Organizations();
    Scorm2004Organization org = new Scorm2004Organization();
    org.setIdentifier("org1");

    Scorm2004Item parentItem = new Scorm2004Item();
    parentItem.setIdentifier("parent");
    parentItem.setIdentifierRef("res1"); // Valid

    Scorm2004Item childItem = new Scorm2004Item();
    childItem.setIdentifier("child");
    childItem.setIdentifierRef("invalid"); // Invalid!

    parentItem.setItems(Collections.singletonList(childItem));
    org.setItems(Collections.singletonList(parentItem));

    organizations.setOrganizationList(Collections.singletonList(org));
    manifest.setOrganizations(organizations);

    ValidationResult result = rule.validate(manifest);

    assertThat(result.isValid()).isFalse();
    assertThat(result.getErrors()).hasSize(1);
    assertThat(result.getErrors().get(0).message()).contains("invalid");
  }

  @Test
  void validate_withNoIdentifierRef_returnsValid() {
    Scorm2004Manifest manifest = new Scorm2004Manifest();

    // Create item without identifierref (folder/container)
    Scorm2004Organizations organizations = new Scorm2004Organizations();
    Scorm2004Organization org = new Scorm2004Organization();
    org.setIdentifier("org1");

    Scorm2004Item item = new Scorm2004Item();
    item.setIdentifier("item1");
    // No identifierRef - this is valid for containers
    org.setItems(Collections.singletonList(item));

    organizations.setOrganizationList(Collections.singletonList(org));
    manifest.setOrganizations(organizations);

    ValidationResult result = rule.validate(manifest);

    assertThat(result.isValid()).isTrue();
  }
}
