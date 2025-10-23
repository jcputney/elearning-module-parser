package dev.jcputney.elearning.parser.validators.rules.scorm12;

import static org.assertj.core.api.Assertions.assertThat;

import dev.jcputney.elearning.parser.input.scorm12.Scorm12Manifest;
import dev.jcputney.elearning.parser.input.scorm12.ims.cp.Scorm12Item;
import dev.jcputney.elearning.parser.input.scorm12.ims.cp.Scorm12Organization;
import dev.jcputney.elearning.parser.input.scorm12.ims.cp.Scorm12Organizations;
import dev.jcputney.elearning.parser.validation.ValidationResult;
import java.util.Arrays;
import java.util.Collections;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class LaunchableResourceRequiredRuleTest {

  private LaunchableResourceRequiredRule rule;

  @BeforeEach
  void setUp() {
    rule = new LaunchableResourceRequiredRule();
  }

  @Test
  void validate_withOrganizationHavingLaunchableResource_returnsValid() {
    Scorm12Manifest manifest = new Scorm12Manifest();
    manifest.setIdentifier("MANIFEST-001");

    Scorm12Organizations organizations = new Scorm12Organizations();
    Scorm12Organization org = new Scorm12Organization();
    org.setIdentifier("org1");

    // Item with identifierref - launchable
    Scorm12Item item = new Scorm12Item();
    item.setIdentifier("item1");
    item.setIdentifierRef("res1");

    org.setItems(Collections.singletonList(item));
    organizations.setOrganizationList(Collections.singletonList(org));
    manifest.setOrganizations(organizations);

    ValidationResult result = rule.validate(manifest);

    assertThat(result.isValid()).isTrue();
  }

  @Test
  void validate_withOrganizationWithoutLaunchableResource_returnsError() {
    Scorm12Manifest manifest = new Scorm12Manifest();
    manifest.setIdentifier("MANIFEST-001");

    Scorm12Organizations organizations = new Scorm12Organizations();
    Scorm12Organization org = new Scorm12Organization();
    org.setIdentifier("org1");

    // Item without identifierref - container only
    Scorm12Item item = new Scorm12Item();
    item.setIdentifier("item1");
    // No identifierref set

    org.setItems(Collections.singletonList(item));
    organizations.setOrganizationList(Collections.singletonList(org));
    manifest.setOrganizations(organizations);

    ValidationResult result = rule.validate(manifest);

    assertThat(result.isValid()).isFalse();
    assertThat(result.getErrors()).hasSize(1);
    assertThat(result.getErrors().get(0).code()).isEqualTo("SCORM12_NO_LAUNCHABLE_RESOURCE");
    assertThat(result.getErrors().get(0).message()).contains("org1");
  }

  @Test
  void validate_withOrganizationWithNoItems_returnsError() {
    Scorm12Manifest manifest = new Scorm12Manifest();
    manifest.setIdentifier("MANIFEST-001");

    Scorm12Organizations organizations = new Scorm12Organizations();
    Scorm12Organization org = new Scorm12Organization();
    org.setIdentifier("org1");
    // No items

    organizations.setOrganizationList(Collections.singletonList(org));
    manifest.setOrganizations(organizations);

    ValidationResult result = rule.validate(manifest);

    assertThat(result.isValid()).isFalse();
    assertThat(result.getErrors()).hasSize(1);
    assertThat(result.getErrors().get(0).code()).isEqualTo("SCORM12_NO_LAUNCHABLE_RESOURCE");
  }

  @Test
  void validate_withMultipleOrganizationsAllWithLaunchableResources_returnsValid() {
    Scorm12Manifest manifest = new Scorm12Manifest();
    manifest.setIdentifier("MANIFEST-001");

    Scorm12Organizations organizations = new Scorm12Organizations();

    // Organization 1 with launchable resource
    Scorm12Organization org1 = new Scorm12Organization();
    org1.setIdentifier("org1");
    Scorm12Item item1 = new Scorm12Item();
    item1.setIdentifier("item1");
    item1.setIdentifierRef("res1");
    org1.setItems(Collections.singletonList(item1));

    // Organization 2 with launchable resource
    Scorm12Organization org2 = new Scorm12Organization();
    org2.setIdentifier("org2");
    Scorm12Item item2 = new Scorm12Item();
    item2.setIdentifier("item2");
    item2.setIdentifierRef("res2");
    org2.setItems(Collections.singletonList(item2));

    organizations.setOrganizationList(Arrays.asList(org1, org2));
    manifest.setOrganizations(organizations);

    ValidationResult result = rule.validate(manifest);

    assertThat(result.isValid()).isTrue();
  }

  @Test
  void validate_withMultipleOrganizationsOneWithoutLaunchableResource_returnsError() {
    Scorm12Manifest manifest = new Scorm12Manifest();
    manifest.setIdentifier("MANIFEST-001");

    Scorm12Organizations organizations = new Scorm12Organizations();

    // Organization 1 with launchable resource
    Scorm12Organization org1 = new Scorm12Organization();
    org1.setIdentifier("org1");
    Scorm12Item item1 = new Scorm12Item();
    item1.setIdentifier("item1");
    item1.setIdentifierRef("res1");
    org1.setItems(Collections.singletonList(item1));

    // Organization 2 without launchable resource
    Scorm12Organization org2 = new Scorm12Organization();
    org2.setIdentifier("org2");
    Scorm12Item item2 = new Scorm12Item();
    item2.setIdentifier("item2");
    // No identifierref
    org2.setItems(Collections.singletonList(item2));

    organizations.setOrganizationList(Arrays.asList(org1, org2));
    manifest.setOrganizations(organizations);

    ValidationResult result = rule.validate(manifest);

    assertThat(result.isValid()).isFalse();
    assertThat(result.getErrors()).hasSize(1);
    assertThat(result.getErrors().get(0).message()).contains("org2");
  }

  @Test
  void validate_withNestedItemsHavingLaunchableResource_returnsValid() {
    Scorm12Manifest manifest = new Scorm12Manifest();
    manifest.setIdentifier("MANIFEST-001");

    Scorm12Organizations organizations = new Scorm12Organizations();
    Scorm12Organization org = new Scorm12Organization();
    org.setIdentifier("org1");

    // Parent item without identifierref (container)
    Scorm12Item parentItem = new Scorm12Item();
    parentItem.setIdentifier("parent");

    // Child item with identifierref (launchable)
    Scorm12Item childItem = new Scorm12Item();
    childItem.setIdentifier("child");
    childItem.setIdentifierRef("res1");

    parentItem.setItems(Collections.singletonList(childItem));
    org.setItems(Collections.singletonList(parentItem));
    organizations.setOrganizationList(Collections.singletonList(org));
    manifest.setOrganizations(organizations);

    ValidationResult result = rule.validate(manifest);

    assertThat(result.isValid()).isTrue();  // Nested child counts as launchable
  }
}
