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

class ResourceHrefRequiredRuleTest {

  private ResourceHrefRequiredRule rule;

  @BeforeEach
  void setUp() {
    rule = new ResourceHrefRequiredRule();
  }

  @Test
  void validate_withResourceHavingValidHref_returnsValid() {
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

    // Create resource with valid href
    Scorm12Resources resources = new Scorm12Resources();
    Scorm12Resource resource = new Scorm12Resource();
    resource.setIdentifier("res1");
    resource.setHref("launch.html");
    resources.setResourceList(Collections.singletonList(resource));
    manifest.setResources(resources);

    ValidationResult result = rule.validate(manifest);

    assertThat(result.isValid()).isTrue();
  }

  @Test
  void validate_withResourceMissingHref_returnsError() {
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

    // Create resource without href
    Scorm12Resources resources = new Scorm12Resources();
    Scorm12Resource resource = new Scorm12Resource();
    resource.setIdentifier("res1");
    // No href set
    resources.setResourceList(Collections.singletonList(resource));
    manifest.setResources(resources);

    ValidationResult result = rule.validate(manifest);

    assertThat(result.isValid()).isFalse();
    assertThat(result.getErrors()).hasSize(1);
    assertThat(result.getErrors().get(0).code()).isEqualTo("SCORM12_MISSING_LAUNCH_URL");
    assertThat(result.getErrors().get(0).message()).contains("res1");
  }

  @Test
  void validate_withResourceHavingEmptyHref_returnsError() {
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

    // Create resource with empty href
    Scorm12Resources resources = new Scorm12Resources();
    Scorm12Resource resource = new Scorm12Resource();
    resource.setIdentifier("res1");
    resource.setHref("");
    resources.setResourceList(Collections.singletonList(resource));
    manifest.setResources(resources);

    ValidationResult result = rule.validate(manifest);

    assertThat(result.isValid()).isFalse();
    assertThat(result.getErrors()).hasSize(1);
    assertThat(result.getErrors().get(0).code()).isEqualTo("SCORM12_MISSING_LAUNCH_URL");
  }

  @Test
  void validate_withResourceHavingWhitespaceOnlyHref_returnsError() {
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

    // Create resource with whitespace-only href
    Scorm12Resources resources = new Scorm12Resources();
    Scorm12Resource resource = new Scorm12Resource();
    resource.setIdentifier("res1");
    resource.setHref("   ");
    resources.setResourceList(Collections.singletonList(resource));
    manifest.setResources(resources);

    ValidationResult result = rule.validate(manifest);

    assertThat(result.isValid()).isFalse();
    assertThat(result.getErrors()).hasSize(1);
    assertThat(result.getErrors().get(0).code()).isEqualTo("SCORM12_MISSING_LAUNCH_URL");
  }

  @Test
  void validate_withUnreferencedResourceWithoutHref_returnsValid() {
    Scorm12Manifest manifest = new Scorm12Manifest();

    // Create organization with item referencing res1
    Scorm12Organizations organizations = new Scorm12Organizations();
    Scorm12Organization org = new Scorm12Organization();
    org.setIdentifier("org1");
    Scorm12Item item = new Scorm12Item();
    item.setIdentifier("item1");
    item.setIdentifierRef("res1");
    org.setItems(Collections.singletonList(item));
    organizations.setOrganizationList(Collections.singletonList(org));
    manifest.setOrganizations(organizations);

    // Create two resources - res1 with href, res2 without href but not referenced
    Scorm12Resources resources = new Scorm12Resources();

    Scorm12Resource res1 = new Scorm12Resource();
    res1.setIdentifier("res1");
    res1.setHref("launch.html");

    Scorm12Resource res2 = new Scorm12Resource();
    res2.setIdentifier("res2");
    // No href - but this resource is not referenced by any item

    resources.setResourceList(java.util.Arrays.asList(res1, res2));
    manifest.setResources(resources);

    ValidationResult result = rule.validate(manifest);

    assertThat(result.isValid()).isTrue();  // res2 not referenced, so no error
  }

  @Test
  void validate_withNestedItemsReferencingResourceWithoutHref_returnsError() {
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
    childItem.setIdentifierRef("res2");

    parentItem.setItems(Collections.singletonList(childItem));
    org.setItems(Collections.singletonList(parentItem));
    organizations.setOrganizationList(Collections.singletonList(org));
    manifest.setOrganizations(organizations);

    // Create resources - res1 with href, res2 without
    Scorm12Resources resources = new Scorm12Resources();

    Scorm12Resource res1 = new Scorm12Resource();
    res1.setIdentifier("res1");
    res1.setHref("launch1.html");

    Scorm12Resource res2 = new Scorm12Resource();
    res2.setIdentifier("res2");
    // No href

    resources.setResourceList(java.util.Arrays.asList(res1, res2));
    manifest.setResources(resources);

    ValidationResult result = rule.validate(manifest);

    assertThat(result.isValid()).isFalse();
    assertThat(result.getErrors()).hasSize(1);
    assertThat(result.getErrors().get(0).message()).contains("res2");
  }
}
