/*
 * qlty-ignore: +qlty:similar-code
 *
 * Copyright (c) 2025. Jonathan Putney
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
 *
 * qlty-ignore: -qlty:similar-code
 */

package dev.jcputney.elearning.parser.input.scorm12;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import dev.jcputney.elearning.parser.input.lom.General;
import dev.jcputney.elearning.parser.input.lom.LOM;
import dev.jcputney.elearning.parser.input.lom.Technical;
import dev.jcputney.elearning.parser.input.lom.types.LangString;
import dev.jcputney.elearning.parser.input.lom.types.LomDuration;
import dev.jcputney.elearning.parser.input.lom.types.UnboundLangString;
import dev.jcputney.elearning.parser.input.scorm12.ims.cp.Scorm12Item;
import dev.jcputney.elearning.parser.input.scorm12.ims.cp.Scorm12Metadata;
import dev.jcputney.elearning.parser.input.scorm12.ims.cp.Scorm12Organization;
import dev.jcputney.elearning.parser.input.scorm12.ims.cp.Scorm12Organizations;
import dev.jcputney.elearning.parser.input.scorm12.ims.cp.Scorm12Resource;
import dev.jcputney.elearning.parser.input.scorm12.ims.cp.Scorm12Resources;
import java.time.Duration;
import java.util.Collections;
import org.junit.jupiter.api.Test;

/**
 * Tests for the {@link Scorm12Manifest} class.
 */
class Scorm12ManifestTest {

  private static final String TEST_TITLE = "Test Title";
  private static final String TEST_DESCRIPTION = "Test Description";
  private static final String TEST_ID = "test-id";
  private static final String TEST_HREF = "http://example.com/test";
  private static final String TEST_VERSION = "1.0";
  private static final String TEST_BASE = "base/path/";

  /**
   * Tests that getTitle() returns the title from the default organization.
   */
  @Test
  void getTitleReturnsTitleFromDefaultOrganization() {
    // Arrange
    Scorm12Organization organization = new Scorm12Organization();
    organization.setTitle(TEST_TITLE);
    organization.setIdentifier("org-id");
    Scorm12Organizations organizations = new Scorm12Organizations();
    organizations.setOrganizationList(Collections.singletonList(organization));
    organizations.setDefaultOrganization("org-id");
    Scorm12Manifest manifest = new Scorm12Manifest();
    manifest.setIdentifier(TEST_ID);
    manifest.setOrganizations(organizations);

    // Act
    String title = manifest.getTitle();

    // Assert
    assertEquals(TEST_TITLE, title);
  }

  /**
   * Tests that getDescription() returns the description from LOM.
   */
  @Test
  void getDescriptionReturnsDescriptionFromLom() {
    // Arrange
    Scorm12Metadata metadata = new Scorm12Metadata();
    metadata.setLom(createTestLom());
    Scorm12Manifest manifest = new Scorm12Manifest();
    manifest.setIdentifier(TEST_ID);
    manifest.setMetadata(metadata);

    // Act
    String description = manifest.getDescription();

    // Assert
    assertEquals(TEST_DESCRIPTION, description);
  }

  /**
   * Tests that getLaunchUrl() returns the href from the first resource with matching
   * identifierRef.
   */
  @Test
  void getLaunchUrlReturnsHrefFromFirstResourceWithMatchingIdentifierRef() {
    // Arrange
    Scorm12Item item = new Scorm12Item();
    item.setIdentifier("item-id");
    item.setTitle("Item Title");
    item.setIdentifierRef("resource-id");
    Scorm12Organization organization = new Scorm12Organization();
    organization.setIdentifier("org-id");
    organization.setItems(Collections.singletonList(item));
    Scorm12Organizations organizations = new Scorm12Organizations();
    organizations.setOrganizationList(Collections.singletonList(organization));
    organizations.setDefaultOrganization("org-id");
    Scorm12Resource resource = new Scorm12Resource();
    resource.setIdentifier("resource-id");
    resource.setHref(TEST_HREF);
    resource.setType("webcontent");
    Scorm12Resources resources = new Scorm12Resources();
    resources.setResourceList(Collections.singletonList(resource));
    Scorm12Manifest manifest = new Scorm12Manifest();
    manifest.setIdentifier(TEST_ID);
    manifest.setOrganizations(organizations);
    manifest.setResources(resources);

    // Act
    String launchUrl = manifest.getLaunchUrl();

    // Assert
    assertEquals(TEST_HREF, launchUrl);
  }

  @Test
  void getLaunchUrlFallsBackToSingleOrganizationWhenDefaultMissing() {
    Scorm12Item item = new Scorm12Item();
    item.setIdentifier("item-id");
    item.setTitle("Item Title");
    item.setIdentifierRef("resource-id");

    Scorm12Organization organization = new Scorm12Organization();
    organization.setIdentifier("org-id");
    organization.setItems(Collections.singletonList(item));

    Scorm12Organizations organizations = new Scorm12Organizations();
    organizations.setOrganizationList(Collections.singletonList(organization));
    organizations.setDefaultOrganization("typo-id");

    Scorm12Resource resource = new Scorm12Resource();
    resource.setIdentifier("resource-id");
    resource.setHref(TEST_HREF);
    resource.setType("webcontent");
    Scorm12Resources resources = new Scorm12Resources();
    resources.setResourceList(Collections.singletonList(resource));

    Scorm12Manifest manifest = new Scorm12Manifest();
    manifest.setIdentifier(TEST_ID);
    manifest.setOrganizations(organizations);
    manifest.setResources(resources);

    String launchUrl = manifest.getLaunchUrl();

    assertEquals(TEST_HREF, launchUrl);
  }

  /**
   * Tests that getDuration() returns the duration from LOM.
   */
  @Test
  void getDurationReturnsDurationFromLom() {
    // Arrange
    Duration testDuration = Duration.ofHours(2);
    Scorm12Metadata metadata = new Scorm12Metadata();
    metadata.setLom(createTestLomWithDuration(testDuration));
    Scorm12Manifest manifest = new Scorm12Manifest();
    manifest.setIdentifier(TEST_ID);
    manifest.setMetadata(metadata);

    // Act
    Duration duration = manifest.getDuration();

    // Assert
    assertEquals(testDuration, duration);
  }

  /**
   * Tests that equals() returns true for equal manifests.
   */
  @Test
  void equalsReturnsTrueForEqualManifests() {
    // Arrange
    Scorm12Manifest manifest1 = new Scorm12Manifest();
    manifest1.setIdentifier(TEST_ID);
    manifest1.setVersion(TEST_VERSION);
    manifest1.setBase(TEST_BASE);
    Scorm12Manifest manifest2 = new Scorm12Manifest();
    manifest2.setIdentifier(TEST_ID);
    manifest2.setVersion(TEST_VERSION);
    manifest2.setBase(TEST_BASE);

    // Act & Assert
    assertEquals(manifest1, manifest2);
    assertEquals(manifest1.hashCode(), manifest2.hashCode());
  }

  /**
   * Tests that equals() returns false for different manifests.
   */
  @Test
  void equalsReturnsFalseForDifferentManifests() {
    // Arrange
    Scorm12Manifest manifest1 = new Scorm12Manifest();
    manifest1.setIdentifier(TEST_ID);
    manifest1.setVersion(TEST_VERSION);
    manifest1.setBase(TEST_BASE);
    Scorm12Manifest manifest2 = new Scorm12Manifest();
    manifest2.setIdentifier("different-id");
    manifest2.setVersion(TEST_VERSION);
    manifest2.setBase(TEST_BASE);

    // Act & Assert
    assertNotEquals(manifest1, manifest2);
    assertNotEquals(manifest1.hashCode(), manifest2.hashCode());
  }

  /**
   * Creates a test LOM with title and description.
   */
  private LOM createTestLom() {
    LOM lom = new LOM();
    General general = new General();
    general.setTitle(
        new UnboundLangString(Collections.singletonList(new LangString("en", TEST_TITLE))));
    general.setDescription(
        new UnboundLangString(Collections.singletonList(new LangString("en", TEST_DESCRIPTION))));
    lom.setGeneral(general);
    return lom;
  }

  /**
   * Creates a test LOM with title, description, and duration.
   */
  private LOM createTestLomWithDuration(Duration duration) {
    Technical technical = new Technical();
    technical.setDuration(new LomDuration(duration));
    General general = new General();
    general.setTitle(
        new UnboundLangString(Collections.singletonList(new LangString("en", TEST_TITLE))));
    general.setDescription(
        new UnboundLangString(Collections.singletonList(new LangString("en", TEST_DESCRIPTION))));
    LOM lom = new LOM();
    lom.setGeneral(general);
    lom.setTechnical(technical);
    return lom;
  }
}
