package dev.jcputney.elearning.parser.validators.rules.scorm12;

import static org.assertj.core.api.Assertions.assertThat;

import dev.jcputney.elearning.parser.input.scorm12.Scorm12Manifest;
import dev.jcputney.elearning.parser.input.scorm12.ims.cp.Scorm12Item;
import dev.jcputney.elearning.parser.input.scorm12.ims.cp.Scorm12Organization;
import dev.jcputney.elearning.parser.input.scorm12.ims.cp.Scorm12Organizations;
import dev.jcputney.elearning.parser.input.scorm12.ims.cp.Scorm12Resource;
import dev.jcputney.elearning.parser.input.scorm12.ims.cp.Scorm12Resources;
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
  void validate_withItemReferencingExistingResource_returnsValid() {
    Scorm12Manifest manifest = new Scorm12Manifest();

    // Create organization with item
    Scorm12Organizations organizations = new Scorm12Organizations();
    Scorm12Organization org = new Scorm12Organization();
    org.setIdentifier("org1");
    Scorm12Item item = new Scorm12Item();
    item.setIdentifier("item1");
    item.setIdentifierRef("res1");
    org.setItems(Collections.singletonList(item));
    organizations.setOrganizationList(Collections.singletonList(org));
    manifest.setOrganizations(organizations);

    // Create resource
    Scorm12Resources resources = new Scorm12Resources();
    Scorm12Resource resource = new Scorm12Resource();
    resource.setIdentifier("res1");
    resources.setResourceList(Collections.singletonList(resource));
    manifest.setResources(resources);

    ValidationResult result = rule.validate(manifest);

    assertThat(result.isValid()).isTrue();
  }

  @Test
  void validate_withItemWithoutIdentifierref_returnsValid() {
    Scorm12Manifest manifest = new Scorm12Manifest();

    // Create organization with container item (no identifierref)
    Scorm12Organizations organizations = new Scorm12Organizations();
    Scorm12Organization org = new Scorm12Organization();
    org.setIdentifier("org1");
    Scorm12Item item = new Scorm12Item();
    item.setIdentifier("item1");
    // No identifierref - container item
    org.setItems(Collections.singletonList(item));
    organizations.setOrganizationList(Collections.singletonList(org));
    manifest.setOrganizations(organizations);

    Scorm12Resources resources = new Scorm12Resources();
    resources.setResourceList(Collections.emptyList());
    manifest.setResources(resources);

    ValidationResult result = rule.validate(manifest);

    assertThat(result.isValid()).isTrue();
  }

  @Test
  void validate_withItemReferencingNonExistentResource_returnsError() {
    Scorm12Manifest manifest = new Scorm12Manifest();

    // Create organization with item referencing non-existent resource
    Scorm12Organizations organizations = new Scorm12Organizations();
    Scorm12Organization org = new Scorm12Organization();
    org.setIdentifier("org1");
    Scorm12Item item = new Scorm12Item();
    item.setIdentifier("item1");
    item.setIdentifierRef("missing_resource");
    org.setItems(Collections.singletonList(item));
    organizations.setOrganizationList(Collections.singletonList(org));
    manifest.setOrganizations(organizations);

    // Create different resource
    Scorm12Resources resources = new Scorm12Resources();
    Scorm12Resource resource = new Scorm12Resource();
    resource.setIdentifier("res1");
    resources.setResourceList(Collections.singletonList(resource));
    manifest.setResources(resources);

    ValidationResult result = rule.validate(manifest);

    assertThat(result.isValid()).isFalse();
    assertThat(result.getErrors()).hasSize(1);
    assertThat(result.getErrors().get(0).code()).isEqualTo("SCORM12_MISSING_RESOURCE_REF");
    assertThat(result.getErrors().get(0).message()).contains("missing_resource");
  }

  @Test
  void validate_withEmptyIdentifierref_returnsValid() {
    Scorm12Manifest manifest = new Scorm12Manifest();

    // Create organization with item with empty identifierref
    Scorm12Organizations organizations = new Scorm12Organizations();
    Scorm12Organization org = new Scorm12Organization();
    org.setIdentifier("org1");
    Scorm12Item item = new Scorm12Item();
    item.setIdentifier("item1");
    item.setIdentifierRef("");  // Empty string
    org.setItems(Collections.singletonList(item));
    organizations.setOrganizationList(Collections.singletonList(org));
    manifest.setOrganizations(organizations);

    Scorm12Resources resources = new Scorm12Resources();
    resources.setResourceList(Collections.emptyList());
    manifest.setResources(resources);

    ValidationResult result = rule.validate(manifest);

    assertThat(result.isValid()).isTrue();  // Empty treated as unset
  }

  @Test
  void validate_withNestedItems_validatesAll() {
    Scorm12Manifest manifest = new Scorm12Manifest();

    // Create nested items
    Scorm12Organizations organizations = new Scorm12Organizations();
    Scorm12Organization org = new Scorm12Organization();
    org.setIdentifier("org1");

    Scorm12Item parentItem = new Scorm12Item();
    parentItem.setIdentifier("parent");
    parentItem.setIdentifierRef("res1");

    Scorm12Item childItem = new Scorm12Item();
    childItem.setIdentifier("child");
    childItem.setIdentifierRef("missing");  // Non-existent

    parentItem.setItems(Collections.singletonList(childItem));
    org.setItems(Collections.singletonList(parentItem));
    organizations.setOrganizationList(Collections.singletonList(org));
    manifest.setOrganizations(organizations);

    // Create only one resource
    Scorm12Resources resources = new Scorm12Resources();
    Scorm12Resource resource = new Scorm12Resource();
    resource.setIdentifier("res1");
    resources.setResourceList(Collections.singletonList(resource));
    manifest.setResources(resources);

    ValidationResult result = rule.validate(manifest);

    assertThat(result.isValid()).isFalse();
    assertThat(result.getErrors()).hasSize(1);
    assertThat(result.getErrors().get(0).message()).contains("missing");
  }
}
