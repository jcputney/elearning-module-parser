/*
 * Copyright (c) 2024-2025. Jonathan Putney
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package dev.jcputney.elearning.parser.validators;

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

/**
 * Tests for the SCORM 1.2 resource validator.
 */
class Scorm12ResourceValidatorTest {

  private Scorm12ResourceValidator validator;

  @BeforeEach
  void setUp() {
    validator = new Scorm12ResourceValidator();
  }

  @Test
  void validate_validManifest_noIssues() {
    // Create a valid manifest
    Scorm12Manifest manifest = createValidManifest();

    ValidationResult result = validator.validate(manifest);

    assertThat(result.isValid()).isTrue();
    assertThat(result.hasErrors()).isFalse();
    assertThat(result.getAllIssues()).isEmpty();
  }

  @Test
  void validate_missingResourceRef_hasError() {
    // Create manifest with item referencing non-existent resource
    Scorm12Manifest manifest = new Scorm12Manifest();

    Scorm12Organizations organizations = new Scorm12Organizations();
    Scorm12Organization org = new Scorm12Organization();
    org.setIdentifier("org1");

    Scorm12Item item = new Scorm12Item();
    item.setIdentifier("item1");
    item.setIdentifierRef("resource_1"); // References non-existent resource

    org.setItems(Collections.singletonList(item));
    organizations.setOrganizationList(Collections.singletonList(org));
    manifest.setOrganizations(organizations);

    // Add resources but with wrong identifier
    Scorm12Resources resources = new Scorm12Resources();
    Scorm12Resource resource = new Scorm12Resource();
    resource.setIdentifier("SCO_ID1_RES"); // Different from what item references
    resource.setHref("sco.html");
    resources.setResourceList(Collections.singletonList(resource));
    manifest.setResources(resources);

    ValidationResult result = validator.validate(manifest);

    assertThat(result.isValid()).isFalse();
    assertThat(result.hasErrors()).isTrue();
    assertThat(result.getErrors()).hasSize(1);
    assertThat(result.getErrors().get(0).code()).isEqualTo("SCORM12_MISSING_RESOURCE_REF");
    assertThat(result.getErrors().get(0).message()).contains("resource_1");
  }

  @Test
  void validate_missingLaunchUrl_hasError() {
    // Create manifest with resource missing href
    Scorm12Manifest manifest = new Scorm12Manifest();

    Scorm12Organizations organizations = new Scorm12Organizations();
    Scorm12Organization org = new Scorm12Organization();
    org.setIdentifier("org1");

    Scorm12Item item = new Scorm12Item();
    item.setIdentifier("item1");
    item.setIdentifierRef("resource_1");

    org.setItems(Collections.singletonList(item));
    organizations.setOrganizationList(Collections.singletonList(org));
    manifest.setOrganizations(organizations);

    // Add resource without href
    Scorm12Resources resources = new Scorm12Resources();
    Scorm12Resource resource = new Scorm12Resource();
    resource.setIdentifier("resource_1");
    resource.setHref(null); // Missing href
    resources.setResourceList(Collections.singletonList(resource));
    manifest.setResources(resources);

    ValidationResult result = validator.validate(manifest);

    assertThat(result.isValid()).isFalse();
    assertThat(result.hasErrors()).isTrue();
    assertThat(result.getErrors()).hasSize(1);
    assertThat(result.getErrors().get(0).code()).isEqualTo("SCORM12_MISSING_LAUNCH_URL");
    assertThat(result.getErrors().get(0).message()).contains("resource_1");
  }

  @Test
  void validate_invalidDefaultOrg_hasError() {
    // Create manifest with invalid default organization
    Scorm12Manifest manifest = new Scorm12Manifest();

    Scorm12Organizations organizations = new Scorm12Organizations();
    organizations.setDefaultOrganization("nonexistent");
    organizations.setOrganizationList(Collections.emptyList());
    manifest.setOrganizations(organizations);

    Scorm12Resources resources = new Scorm12Resources();
    resources.setResourceList(Collections.emptyList());
    manifest.setResources(resources);

    ValidationResult result = validator.validate(manifest);

    assertThat(result.isValid()).isFalse();
    assertThat(result.hasErrors()).isTrue();
    assertThat(result.getErrors().get(0).code()).isEqualTo("SCORM12_INVALID_DEFAULT_ORG");
  }

  @Test
  void validate_missingOrganizations_hasError() {
    // Create manifest without organizations
    Scorm12Manifest manifest = new Scorm12Manifest();
    manifest.setOrganizations(null);

    Scorm12Resources resources = new Scorm12Resources();
    resources.setResourceList(Collections.emptyList());
    manifest.setResources(resources);

    ValidationResult result = validator.validate(manifest);

    assertThat(result.isValid()).isFalse();
    assertThat(result.hasErrors()).isTrue();
    assertThat(result.getErrors().get(0).code()).isEqualTo("SCORM12_MISSING_ORGANIZATIONS");
  }

  @Test
  void validate_withMultipleRuleViolations_returnsAllIssues() {
    // Create manifest with multiple violations:
    // 1. Duplicate identifiers
    // 2. Orphaned resource
    // 3. Missing resource reference
    Scorm12Manifest manifest = new Scorm12Manifest();
    manifest.setIdentifier("dup_id");

    Scorm12Organizations organizations = new Scorm12Organizations();
    Scorm12Organization org = new Scorm12Organization();
    org.setIdentifier("dup_id"); // Duplicate!

    Scorm12Item item = new Scorm12Item();
    item.setIdentifier("item1");
    item.setIdentifierRef("missing_resource"); // Missing ref!
    org.setItems(Collections.singletonList(item));
    organizations.setOrganizationList(Collections.singletonList(org));
    manifest.setOrganizations(organizations);

    Scorm12Resources resources = new Scorm12Resources();
    Scorm12Resource res1 = new Scorm12Resource();
    res1.setIdentifier("orphaned"); // Orphaned!
    res1.setHref("unused.html");
    resources.setResourceList(Collections.singletonList(res1));
    manifest.setResources(resources);

    Scorm12ResourceValidator validator = new Scorm12ResourceValidator();
    ValidationResult result = validator.validate(manifest);

    assertThat(result.isValid()).isFalse();
    assertThat(result.hasErrors()).isTrue();
    assertThat(result.hasWarnings()).isTrue();

    // Should have errors from duplicate ID and missing ref
    assertThat(result.getErrors().size()).isGreaterThanOrEqualTo(2);
    // Should have warning from orphaned resource
    assertThat(result.getWarnings().size()).isGreaterThanOrEqualTo(1);
  }

  /**
   * Creates a valid SCORM 1.2 manifest for testing.
   */
  private Scorm12Manifest createValidManifest() {
    Scorm12Manifest manifest = new Scorm12Manifest();

    // Create organizations
    Scorm12Organizations organizations = new Scorm12Organizations();
    Scorm12Organization org = new Scorm12Organization();
    org.setIdentifier("org1");

    Scorm12Item item = new Scorm12Item();
    item.setIdentifier("item1");
    item.setIdentifierRef("resource_1");

    org.setItems(Collections.singletonList(item));
    organizations.setOrganizationList(Collections.singletonList(org));
    organizations.setDefaultOrganization("org1");
    manifest.setOrganizations(organizations);

    // Create resources
    Scorm12Resources resources = new Scorm12Resources();
    Scorm12Resource resource = new Scorm12Resource();
    resource.setIdentifier("resource_1");
    resource.setHref("sco.html");
    resources.setResourceList(Collections.singletonList(resource));
    manifest.setResources(resources);

    return manifest;
  }
}
