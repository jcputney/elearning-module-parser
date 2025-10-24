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

/**
 * Tests for the SCORM 2004 resource validator.
 */
class Scorm2004ResourceValidatorTest {

  private Scorm2004ResourceValidator validator;

  @BeforeEach
  void setUp() {
    validator = new Scorm2004ResourceValidator();
  }

  @Test
  void validate_validManifest_noIssues() {
    // Create a valid manifest
    Scorm2004Manifest manifest = createValidManifest();

    ValidationResult result = validator.validate(manifest);

    assertThat(result.isValid()).isTrue();
    assertThat(result.hasErrors()).isFalse();
    assertThat(result.getAllIssues()).isEmpty();
  }

  @Test
  void validate_missingResourceRef_hasError() {
    // Create manifest with item referencing non-existent resource
    Scorm2004Manifest manifest = new Scorm2004Manifest();

    Scorm2004Organizations organizations = new Scorm2004Organizations();
    Scorm2004Organization org = new Scorm2004Organization();
    org.setIdentifier("org1");

    Scorm2004Item item = new Scorm2004Item();
    item.setIdentifier("item1");
    item.setIdentifierRef("resource_1"); // References non-existent resource

    org.setItems(Collections.singletonList(item));
    organizations.setOrganizationList(Collections.singletonList(org));
    manifest.setOrganizations(organizations);

    // Add resources but with wrong identifier
    Scorm2004Resources resources = new Scorm2004Resources();
    Scorm2004Resource resource = new Scorm2004Resource();
    resource.setIdentifier("SCO_ID1_RES"); // Different from what item references
    resource.setHref("sco.html");
    resources.setResourceList(Collections.singletonList(resource));
    manifest.setResources(resources);

    ValidationResult result = validator.validate(manifest);

    assertThat(result.isValid()).isFalse();
    assertThat(result.hasErrors()).isTrue();
    assertThat(result.getErrors()).hasSize(1);
    assertThat(result.getErrors().get(0).code()).isEqualTo("SCORM2004_MISSING_RESOURCE_REF");
    assertThat(result.getErrors().get(0).message()).contains("resource_1");
  }

  @Test
  void validate_missingLaunchUrl_hasError() {
    // Create manifest with resource missing href
    Scorm2004Manifest manifest = new Scorm2004Manifest();

    Scorm2004Organizations organizations = new Scorm2004Organizations();
    Scorm2004Organization org = new Scorm2004Organization();
    org.setIdentifier("org1");

    Scorm2004Item item = new Scorm2004Item();
    item.setIdentifier("item1");
    item.setIdentifierRef("resource_1");

    org.setItems(Collections.singletonList(item));
    organizations.setOrganizationList(Collections.singletonList(org));
    manifest.setOrganizations(organizations);

    // Add resource without href
    Scorm2004Resources resources = new Scorm2004Resources();
    Scorm2004Resource resource = new Scorm2004Resource();
    resource.setIdentifier("resource_1");
    resource.setHref(null); // Missing href
    resources.setResourceList(Collections.singletonList(resource));
    manifest.setResources(resources);

    ValidationResult result = validator.validate(manifest);

    assertThat(result.isValid()).isFalse();
    assertThat(result.hasErrors()).isTrue();
    assertThat(result.getErrors()).hasSize(1);
    assertThat(result.getErrors().get(0).code()).isEqualTo("SCORM2004_MISSING_LAUNCH_URL");
    assertThat(result.getErrors().get(0).message()).contains("resource_1");
  }

  @Test
  void validate_invalidDefaultOrg_hasError() {
    // Create manifest with invalid default organization
    Scorm2004Manifest manifest = new Scorm2004Manifest();

    Scorm2004Organizations organizations = new Scorm2004Organizations();
    organizations.setDefaultOrganization("nonexistent");
    organizations.setOrganizationList(Collections.emptyList());
    manifest.setOrganizations(organizations);

    Scorm2004Resources resources = new Scorm2004Resources();
    resources.setResourceList(Collections.emptyList());
    manifest.setResources(resources);

    ValidationResult result = validator.validate(manifest);

    assertThat(result.isValid()).isFalse();
    assertThat(result.hasErrors()).isTrue();
    assertThat(result.getErrors().get(0).code()).isEqualTo("SCORM2004_INVALID_DEFAULT_ORG");
  }

  @Test
  void validate_missingOrganizations_hasError() {
    // Create manifest without organizations
    Scorm2004Manifest manifest = new Scorm2004Manifest();
    manifest.setOrganizations(null);

    Scorm2004Resources resources = new Scorm2004Resources();
    resources.setResourceList(Collections.emptyList());
    manifest.setResources(resources);

    ValidationResult result = validator.validate(manifest);

    assertThat(result.isValid()).isFalse();
    assertThat(result.hasErrors()).isTrue();
    assertThat(result.getErrors().get(0).code()).isEqualTo("SCORM2004_MISSING_ORGANIZATIONS");
  }

  @Test
  void validate_withRuleBasedValidation_returnsAllIssues() {
    // Create manifest with multiple violations:
    // 1. No organizations
    // 2. Invalid resource reference (if we add organizations)
    // 3. Missing href (if resource is referenced)
    Scorm2004Manifest manifest = new Scorm2004Manifest();
    // No organizations set - violation 1
    manifest.setOrganizations(null);

    Scorm2004Resources resources = new Scorm2004Resources();
    Scorm2004Resource resource = new Scorm2004Resource();
    resource.setIdentifier("res1");
    // No href - would be violation if referenced
    resources.setResourceList(Collections.singletonList(resource));
    manifest.setResources(resources);

    ValidationResult result = validator.validate(manifest);

    assertThat(result.isValid()).isFalse();
    assertThat(result.hasErrors()).isTrue();
    // Should have at least 1 error (missing organizations)
    assertThat(result.getErrors().size()).isGreaterThanOrEqualTo(1);
    assertThat(result.getErrors().get(0).code()).isEqualTo("SCORM2004_MISSING_ORGANIZATIONS");
  }

  /**
   * Creates a valid SCORM 2004 manifest for testing.
   */
  private Scorm2004Manifest createValidManifest() {
    Scorm2004Manifest manifest = new Scorm2004Manifest();

    // Create organizations
    Scorm2004Organizations organizations = new Scorm2004Organizations();
    Scorm2004Organization org = new Scorm2004Organization();
    org.setIdentifier("org1");

    Scorm2004Item item = new Scorm2004Item();
    item.setIdentifier("item1");
    item.setIdentifierRef("resource_1");

    org.setItems(Collections.singletonList(item));
    organizations.setOrganizationList(Collections.singletonList(org));
    organizations.setDefaultOrganization("org1");
    manifest.setOrganizations(organizations);

    // Create resources
    Scorm2004Resources resources = new Scorm2004Resources();
    Scorm2004Resource resource = new Scorm2004Resource();
    resource.setIdentifier("resource_1");
    resource.setHref("sco.html");
    resources.setResourceList(Collections.singletonList(resource));
    manifest.setResources(resources);

    return manifest;
  }
}
