package dev.jcputney.elearning.parser.validators.rules.common;

import static org.assertj.core.api.Assertions.assertThat;

import dev.jcputney.elearning.parser.input.scorm12.Scorm12Manifest;
import dev.jcputney.elearning.parser.input.scorm12.ims.cp.Scorm12Item;
import dev.jcputney.elearning.parser.input.scorm12.ims.cp.Scorm12Organization;
import dev.jcputney.elearning.parser.input.scorm12.ims.cp.Scorm12Organizations;
import dev.jcputney.elearning.parser.input.scorm12.ims.cp.Scorm12Resource;
import dev.jcputney.elearning.parser.input.scorm12.ims.cp.Scorm12Resources;
import dev.jcputney.elearning.parser.validation.ValidationResult;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class OrphanedResourcesRuleTest {

  private OrphanedResourcesRule rule;

  @BeforeEach
  void setUp() {
    rule = new OrphanedResourcesRule();
  }

  @Test
  void validate_withAllResourcesReferenced_returnsValid() {
    Scorm12Manifest manifest = new Scorm12Manifest();

    // Create organization with item referencing resource
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
    resource.setHref("content.html");
    resources.setResourceList(Collections.singletonList(resource));
    manifest.setResources(resources);

    ValidationResult result = rule.validate(manifest);

    assertThat(result.isValid()).isTrue();
    assertThat(result.getAllIssues()).isEmpty();
  }

  @Test
  void validate_withOrphanedResource_returnsWarning() {
    Scorm12Manifest manifest = new Scorm12Manifest();

    // Create organization with item referencing one resource
    Scorm12Organizations organizations = new Scorm12Organizations();
    Scorm12Organization org = new Scorm12Organization();
    org.setIdentifier("org1");
    Scorm12Item item = new Scorm12Item();
    item.setIdentifier("item1");
    item.setIdentifierRef("res1");
    org.setItems(Collections.singletonList(item));
    organizations.setOrganizationList(Collections.singletonList(org));
    manifest.setOrganizations(organizations);

    // Create two resources, only one referenced
    Scorm12Resources resources = new Scorm12Resources();
    Scorm12Resource res1 = new Scorm12Resource();
    res1.setIdentifier("res1");
    res1.setHref("content.html");

    Scorm12Resource res2 = new Scorm12Resource();
    res2.setIdentifier("orphaned_resource");
    res2.setHref("unused.html");

    resources.setResourceList(List.of(res1, res2));
    manifest.setResources(resources);

    ValidationResult result = rule.validate(manifest);

    assertThat(result.isValid()).isTrue(); // Warnings don't invalidate
    assertThat(result.hasWarnings()).isTrue();
    assertThat(result.getWarnings()).hasSize(1);
    assertThat(result.getWarnings().get(0).code()).isEqualTo("ORPHANED_RESOURCE");
    assertThat(result.getWarnings().get(0).message()).contains("orphaned_resource");
  }

  @Test
  void validate_withNestedItemReferences_detectsOrphans() {
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

    // Create resources: 2 referenced, 1 orphaned
    Scorm12Resources resources = new Scorm12Resources();
    Scorm12Resource res1 = new Scorm12Resource();
    res1.setIdentifier("res1");
    Scorm12Resource res2 = new Scorm12Resource();
    res2.setIdentifier("res2");
    Scorm12Resource res3 = new Scorm12Resource();
    res3.setIdentifier("orphan");

    resources.setResourceList(List.of(res1, res2, res3));
    manifest.setResources(resources);

    ValidationResult result = rule.validate(manifest);

    assertThat(result.hasWarnings()).isTrue();
    assertThat(result.getWarnings()).hasSize(1);
    assertThat(result.getWarnings().get(0).message()).contains("orphan");
  }
}
