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
import java.util.List;
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
    Scorm12Organization organization = Scorm12Organization
        .builder()
        .identifier("org-id")
        .title(TEST_TITLE)
        .build();
    Scorm12Organizations organizations = Scorm12Organizations
        .builder()
        .organizationList(Collections.singletonList(organization))
        .defaultOrganization("org-id")
        .build();
    Scorm12Manifest manifest = Scorm12Manifest
        .builder()
        .identifier(TEST_ID)
        .organizations(organizations)
        .build();

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
    Scorm12Metadata metadata = Scorm12Metadata
        .builder()
        .lom(createTestLom())
        .build();
    Scorm12Manifest manifest = Scorm12Manifest
        .builder()
        .identifier(TEST_ID)
        .metadata(metadata)
        .build();

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
    Scorm12Item item = Scorm12Item
        .builder()
        .identifier("item-id")
        .title("Item Title")
        .identifierRef("resource-id")
        .build();
    Scorm12Organization organization = Scorm12Organization
        .builder()
        .identifier("org-id")
        .items(Collections.singletonList(item))
        .build();
    Scorm12Organizations organizations = Scorm12Organizations
        .builder()
        .organizationList(Collections.singletonList(organization))
        .defaultOrganization("org-id")
        .build();
    Scorm12Resource resource = Scorm12Resource
        .builder()
        .identifier("resource-id")
        .href(TEST_HREF)
        .type("webcontent")
        .build();
    Scorm12Resources resources = Scorm12Resources
        .builder()
        .resourceList(Collections.singletonList(resource))
        .build();
    Scorm12Manifest manifest = Scorm12Manifest
        .builder()
        .identifier(TEST_ID)
        .organizations(organizations)
        .resources(resources)
        .build();

    // Act
    String launchUrl = manifest.getLaunchUrl();

    // Assert
    assertEquals(TEST_HREF, launchUrl);
  }

  /**
   * Tests that getDuration() returns the duration from LOM.
   */
  @Test
  void getDurationReturnsDurationFromLom() {
    // Arrange
    Duration testDuration = Duration.ofHours(2);
    Scorm12Metadata metadata = Scorm12Metadata
        .builder()
        .lom(createTestLomWithDuration(testDuration))
        .build();
    Scorm12Manifest manifest = Scorm12Manifest
        .builder()
        .identifier(TEST_ID)
        .metadata(metadata)
        .build();

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
    Scorm12Manifest manifest1 = Scorm12Manifest
        .builder()
        .identifier(TEST_ID)
        .version(TEST_VERSION)
        .base(TEST_BASE)
        .build();
    Scorm12Manifest manifest2 = Scorm12Manifest
        .builder()
        .identifier(TEST_ID)
        .version(TEST_VERSION)
        .base(TEST_BASE)
        .build();

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
    Scorm12Manifest manifest1 = Scorm12Manifest
        .builder()
        .identifier(TEST_ID)
        .version(TEST_VERSION)
        .base(TEST_BASE)
        .build();
    Scorm12Manifest manifest2 = Scorm12Manifest
        .builder()
        .identifier("different-id")
        .version(TEST_VERSION)
        .base(TEST_BASE)
        .build();

    // Act & Assert
    assertNotEquals(manifest1, manifest2);
    assertNotEquals(manifest1.hashCode(), manifest2.hashCode());
  }

  /**
   * Creates a test LOM with title and description.
   */
  private LOM createTestLom() {
    return LOM
        .builder()
        .general(General
            .builder()
            .title(UnboundLangString
                .builder()
                .langStrings(List.of(
                    LangString
                        .builder()
                        .language("en")
                        .value(TEST_TITLE)
                        .build()
                ))
                .build())
            .description(UnboundLangString
                .builder()
                .langStrings(List.of(
                    LangString
                        .builder()
                        .language("en")
                        .value(TEST_DESCRIPTION)
                        .build()
                ))
                .build())
            .build())
        .build();
  }

  /**
   * Creates a test LOM with title, description, and duration.
   */
  private LOM createTestLomWithDuration(Duration duration) {
    Technical technical = Technical
        .builder()
        .duration(LomDuration
            .builder()
            .duration(duration)
            .build())
        .build();

    return LOM
        .builder()
        .general(General
            .builder()
            .title(UnboundLangString
                .builder()
                .langStrings(List.of(
                    LangString
                        .builder()
                        .language("en")
                        .value(TEST_TITLE)
                        .build()
                ))
                .build())
            .description(UnboundLangString
                .builder()
                .langStrings(List.of(
                    LangString
                        .builder()
                        .language("en")
                        .value(TEST_DESCRIPTION)
                        .build()
                ))
                .build())
            .build())
        .technical(technical)
        .build();
  }
}