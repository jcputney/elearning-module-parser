/*
 * qlty-ignore: +qlty:similar-code
 *
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
 *
 * qlty-ignore: -qlty:similar-code
 */

package dev.jcputney.elearning.parser.input.scorm2004;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import dev.jcputney.elearning.parser.api.FileAccess;
import dev.jcputney.elearning.parser.exception.ModuleParsingException;
import dev.jcputney.elearning.parser.impl.access.LocalFileAccess;
import dev.jcputney.elearning.parser.input.scorm2004.SequencingUsageDetector.SequencingLevel;
import dev.jcputney.elearning.parser.input.scorm2004.adl.types.ScormType;
import dev.jcputney.elearning.parser.input.scorm2004.ims.cp.Scorm2004Item;
import dev.jcputney.elearning.parser.input.scorm2004.ims.cp.Scorm2004Organization;
import dev.jcputney.elearning.parser.input.scorm2004.ims.cp.Scorm2004Organizations;
import dev.jcputney.elearning.parser.input.scorm2004.ims.cp.Scorm2004Resource;
import dev.jcputney.elearning.parser.input.scorm2004.ims.cp.Scorm2004Resources;
import dev.jcputney.elearning.parser.input.scorm2004.sequencing.ActivityTree;
import dev.jcputney.elearning.parser.parsers.Scorm2004Parser;
import java.io.IOException;
import java.time.Duration;
import java.util.Set;
import javax.xml.stream.XMLStreamException;
import org.junit.jupiter.api.Test;

/**
 * Tests for the Scorm2004Manifest class.
 */
public class Scorm2004ManifestTest {

  /**
   * Tests the usesSequencing method with a manifest that uses sequencing.
   */
  @Test
  void testUsesSequencingWithSequencing()
      throws IOException, XMLStreamException, ModuleParsingException {
    // Parse a SCORM 2004 manifest that uses sequencing
    String modulePath = "src/test/resources/modules/scorm2004/SequencingForcedSequential_SCORM20043rdEdition";
    FileAccess fileAccess = new LocalFileAccess(modulePath);
    Scorm2004Parser parser = new Scorm2004Parser(fileAccess);
    Scorm2004Manifest manifest = parser.parseManifest(Scorm2004Parser.MANIFEST_FILE);

    // Verify that usesSequencing returns true
    assertTrue(manifest.usesSequencing(), "Manifest should use sequencing");
    assertEquals(SequencingLevel.FULL, manifest.getSequencingLevel());
  }

  @Test
  void testGetSequencingIndicatorsFromRealManifest()
      throws IOException, XMLStreamException, ModuleParsingException {
    String modulePath = "src/test/resources/modules/scorm2004/AdlAttributes_SCORM20043rdEdition";
    FileAccess fileAccess = new LocalFileAccess(modulePath);
    Scorm2004Parser parser = new Scorm2004Parser(fileAccess);
    Scorm2004Manifest manifest = parser.parseManifest(Scorm2004Parser.MANIFEST_FILE);

    Set<SequencingUsageDetector.SequencingIndicator> indicators = manifest.getSequencingIndicators();

    assertTrue(
        indicators.contains(SequencingUsageDetector.SequencingIndicator.ACTIVITY_SEQUENCING));
    assertTrue(
        indicators.contains(SequencingUsageDetector.SequencingIndicator.SEQUENCING_CONTROL_MODE));
    assertTrue(
        indicators.contains(SequencingUsageDetector.SequencingIndicator.PRESENTATION_CONTROLS));
    assertTrue(
        indicators.contains(SequencingUsageDetector.SequencingIndicator.COMPLETION_THRESHOLD));
    assertEquals(SequencingLevel.FULL, manifest.getSequencingLevel());
  }

  /**
   * Tests the usesSequencing method with a manifest that doesn't use sequencing.
   */
  @Test
  void testUsesSequencingWithNamespaceOnlyReturnsFalse()
      throws IOException, XMLStreamException, ModuleParsingException {
    // Parse a SCORM 2004 manifest that declares sequencing namespaces but relies on implicit rules
    String modulePath = "src/test/resources/modules/scorm2004/ContentPackagingOneFilePerSCO_SCORM20043rdEdition";
    FileAccess fileAccess = new LocalFileAccess(modulePath);
    Scorm2004Parser parser = new Scorm2004Parser(fileAccess);
    Scorm2004Manifest manifest = parser.parseManifest(Scorm2004Parser.MANIFEST_FILE);

    // Namespace declaration and SCO definitions imply sequencing involvement
    assertFalse(manifest.usesSequencing(),
        "Namespace declarations alone should not imply sequencing usage");
    assertEquals(SequencingLevel.NONE, manifest.getSequencingLevel());
  }

  /**
   * Ensures usesSequencing returns false when no sequencing indicators are present.
   */
  @Test
  void testUsesSequencingWithoutIndicators() {
    Scorm2004Manifest manifest = new Scorm2004Manifest();

    assertFalse(manifest.usesSequencing(), "Empty manifest should not use sequencing");
  }

  /**
   * Tests the getGlobalObjectiveIds method with a manifest that contains global objectives.
   */
  @Test
  void testGetGlobalObjectiveIds() throws IOException, XMLStreamException, ModuleParsingException {
    // Parse a SCORM 2004 manifest that contains global objectives
    String modulePath = "src/test/resources/modules/scorm2004/SequencingForcedSequential_SCORM20043rdEdition";
    FileAccess fileAccess = new LocalFileAccess(modulePath);
    Scorm2004Parser parser = new Scorm2004Parser(fileAccess);
    Scorm2004Manifest manifest = parser.parseManifest(Scorm2004Parser.MANIFEST_FILE);

    // Get the global objective IDs
    Set<String> globalObjectiveIds = manifest.getGlobalObjectiveIds();

    // Verify that the set is not null and contains expected IDs
    assertNotNull(globalObjectiveIds, "Global objective IDs should not be null");
    assertFalse(globalObjectiveIds.isEmpty(), "Global objective IDs should not be empty");

    // The SequencingForcedSequential example uses global objectives with IDs like:
    // com.scorm.golfsamples.sequencing.forcedsequential.playing_satisfied
    assertTrue(globalObjectiveIds
            .stream()
            .anyMatch(id -> id.contains("forcedsequential")),
        "Global objective IDs should contain expected IDs");
  }

  /**
   * Tests the getSCOIds method with a manifest that contains SCOs.
   */
  @Test
  void testGetSCOIds() throws IOException, XMLStreamException, ModuleParsingException {
    // Parse a SCORM 2004 manifest that contains SCOs
    String modulePath = "src/test/resources/modules/scorm2004/RuntimeMinimumCalls_SCORM20043rdEdition";
    FileAccess fileAccess = new LocalFileAccess(modulePath);
    Scorm2004Parser parser = new Scorm2004Parser(fileAccess);
    Scorm2004Manifest manifest = parser.parseManifest(Scorm2004Parser.MANIFEST_FILE);

    // Get the SCO IDs
    Set<String> scoIds = manifest.getSCOIds();

    // Verify that the set is not null and contains expected IDs
    assertNotNull(scoIds, "SCO IDs should not be null");
    assertFalse(scoIds.isEmpty(), "SCO IDs should not be empty");
    assertTrue(scoIds.contains("playing_playing_resource"),
        "SCO IDs should contain playing_playing_resource");
    assertTrue(scoIds.contains("etiquette_course_resource"),
        "SCO IDs should contain etiquette_course_resource");
    assertTrue(scoIds.contains("handicapping_overview_resource"),
        "SCO IDs should contain handicapping_overview_resource");
  }

  /**
   * Tests the getTitle method with a manifest that contains a title.
   */
  @Test
  void testGetTitle() throws IOException, XMLStreamException, ModuleParsingException {
    // Parse a SCORM 2004 manifest that contains a title
    String modulePath = "src/test/resources/modules/scorm2004/ContentPackagingMetadata_SCORM20043rdEdition";
    FileAccess fileAccess = new LocalFileAccess(modulePath);
    Scorm2004Parser parser = new Scorm2004Parser(fileAccess);
    Scorm2004Manifest manifest = parser.parseManifest(Scorm2004Parser.MANIFEST_FILE);

    // Get the title
    String title = manifest.getTitle();

    // Verify that the title is correct
    assertNotNull(title, "Title should not be null");
    assertEquals("Golf Explained - Metadata Example", title,
        "Title should match the expected value");
  }

  /**
   * Tests the getDescription method with a manifest that contains a description.
   */
  @Test
  void testGetDescription() throws IOException, XMLStreamException, ModuleParsingException {
    // Parse a SCORM 2004 manifest that contains a description
    String modulePath = "src/test/resources/modules/scorm2004/ContentPackagingMetadata_SCORM20043rdEdition";
    FileAccess fileAccess = new LocalFileAccess(modulePath);
    Scorm2004Parser parser = new Scorm2004Parser(fileAccess);
    Scorm2004Manifest manifest = parser.parseManifest(Scorm2004Parser.MANIFEST_FILE);

    // Get the description
    String description = manifest.getDescription();

    assertTrue(description.contains("A high level overview of the sport of golf."),
        "Description should contain expected text");
  }

  /**
   * Tests the getLaunchUrl method with a manifest that contains a launch URL.
   */
  @Test
  void testGetLaunchUrl() throws IOException, XMLStreamException, ModuleParsingException {
    // Parse a SCORM 2004 manifest that contains a launch URL
    String modulePath = "src/test/resources/modules/scorm2004/ContentPackagingMetadata_SCORM20043rdEdition";
    FileAccess fileAccess = new LocalFileAccess(modulePath);
    Scorm2004Parser parser = new Scorm2004Parser(fileAccess);
    Scorm2004Manifest manifest = parser.parseManifest(Scorm2004Parser.MANIFEST_FILE);

    // Get the launch URL
    String launchUrl = manifest.getLaunchUrl();

    // Verify that the launch URL is correct
    assertNotNull(launchUrl, "Launch URL should not be null");
    assertEquals("shared/launchpage.html", launchUrl, "Launch URL should match the expected value");
  }

  /**
   * Tests the getLaunchUrlForItem method with a manifest that contains items with launch URLs.
   */
  @Test
  void testGetLaunchUrlForItem() throws IOException, XMLStreamException, ModuleParsingException {
    // Parse a SCORM 2004 manifest that contains items with launch URLs
    String modulePath = "src/test/resources/modules/scorm2004/ContentPackagingMetadata_SCORM20043rdEdition";
    FileAccess fileAccess = new LocalFileAccess(modulePath);
    Scorm2004Parser parser = new Scorm2004Parser(fileAccess);
    Scorm2004Manifest manifest = parser.parseManifest(Scorm2004Parser.MANIFEST_FILE);

    // Get the launch URL for a specific item
    String launchUrl = manifest.getLaunchUrlForItem("item_1");

    // Verify that the launch URL is correct
    assertEquals("shared/launchpage.html", launchUrl,
        "Launch URL should match the expected value");
  }

  /**
   * Tests the buildActivityTree method with a manifest that contains an activity tree.
   */
  @Test
  void testBuildActivityTree() throws IOException, XMLStreamException, ModuleParsingException {
    // Parse a SCORM 2004 manifest that contains an activity tree
    String modulePath = "src/test/resources/modules/scorm2004/SequencingForcedSequential_SCORM20043rdEdition";
    FileAccess fileAccess = new LocalFileAccess(modulePath);
    Scorm2004Parser parser = new Scorm2004Parser(fileAccess);
    Scorm2004Manifest manifest = parser.parseManifest(Scorm2004Parser.MANIFEST_FILE);

    // Build the activity tree
    ActivityTree activityTree = manifest.buildActivityTree();

    // Verify that the activity tree is present and has the expected structure
    assertNotNull(activityTree, "Activity tree should be present");
    assertNotNull(activityTree
        .getRoot(), "Root node should not be null");
    assertFalse(activityTree
            .getRoot()
            .getChildren()
            .isEmpty(),
        "Root node should have children");
  }

  /**
   * Tests the getDuration method with a manifest that contains a duration.
   */
  @Test
  void testGetDuration() throws IOException, XMLStreamException, ModuleParsingException {
    // Parse a SCORM 2004 manifest
    String modulePath = "src/test/resources/modules/scorm2004/ContentPackagingMetadata_SCORM20043rdEdition";
    FileAccess fileAccess = new LocalFileAccess(modulePath);
    Scorm2004Parser parser = new Scorm2004Parser(fileAccess);
    Scorm2004Manifest manifest = parser.parseManifest(Scorm2004Parser.MANIFEST_FILE);

    // Get the duration
    Duration duration = manifest.getDuration();

    // Verify that the duration is not null
    assertNotNull(duration, "Duration should not be null");
    // The duration might be zero if not specified in the manifest
    // This is just checking that the method doesn't throw an exception
  }

  /**
   * Tests the getOrganizations method with a manifest that contains organizations.
   */
  @Test
  void testGetOrganizations() throws IOException, XMLStreamException, ModuleParsingException {
    // Parse a SCORM 2004 manifest that contains organizations
    String modulePath = "src/test/resources/modules/scorm2004/ContentPackagingMetadata_SCORM20043rdEdition";
    FileAccess fileAccess = new LocalFileAccess(modulePath);
    Scorm2004Parser parser = new Scorm2004Parser(fileAccess);
    Scorm2004Manifest manifest = parser.parseManifest(Scorm2004Parser.MANIFEST_FILE);

    // Get the organizations
    Scorm2004Organizations organizations = manifest.getOrganizations();

    // Verify that the organizations are correct
    assertNotNull(organizations, "Organizations should not be null");
    assertNotNull(organizations.getDefault(), "Default organization should not be null");
    assertNotNull(organizations.getOrganizationList(), "Organization list should not be null");
    assertFalse(organizations
        .getOrganizationList()
        .isEmpty(), "Organization list should not be empty");

    // Verify the first organization
    Scorm2004Organization organization = organizations
        .getOrganizationList()
        .get(0);
    assertEquals("golf_sample_default_org", organization.getIdentifier(),
        "Organization identifier should match the expected value");
    assertEquals("Golf Explained - Metadata Example", organization.getTitle(),
        "Organization title should match the expected value");

    // Verify the organization's items
    assertNotNull(organization.getItems(), "Organization items should not be null");
    assertFalse(organization
        .getItems()
        .isEmpty(), "Organization items should not be empty");

    // Verify the first item
    Scorm2004Item item = organization
        .getItems()
        .get(0);
    assertEquals("item_1", item.getIdentifier(), "Item identifier should match the expected value");
    assertEquals("resource_1", item.getIdentifierRef(),
        "Item identifierRef should match the expected value");
    assertEquals("Golf Explained", item.getTitle(), "Item title should match the expected value");
  }

  /**
   * Tests the getResources method with a manifest that contains resources.
   */
  @Test
  void testGetResources() throws IOException, XMLStreamException, ModuleParsingException {
    // Parse a SCORM 2004 manifest that contains resources
    String modulePath = "src/test/resources/modules/scorm2004/ContentPackagingMetadata_SCORM20043rdEdition";
    FileAccess fileAccess = new LocalFileAccess(modulePath);
    Scorm2004Parser parser = new Scorm2004Parser(fileAccess);
    Scorm2004Manifest manifest = parser.parseManifest(Scorm2004Parser.MANIFEST_FILE);

    // Get the resources
    Scorm2004Resources resources = manifest.getResources();

    // Verify that the resources are correct
    assertNotNull(resources, "Resources should not be null");
    assertNotNull(resources.getResourceList(), "Resource list should not be null");
    assertFalse(resources
        .getResourceList()
        .isEmpty(), "Resource list should not be empty");

    // Verify the first resource
    Scorm2004Resource resource = resources
        .getResourceList()
        .get(0);
    assertEquals("resource_1", resource.getIdentifier(),
        "Resource identifier should match the expected value");
    assertEquals("webcontent", resource.getType(), "Resource type should match the expected value");
    assertEquals(ScormType.SCO, resource.getScormType(),
        "Resource SCORM type should match the expected value");
    assertEquals("shared/launchpage.html", resource.getHref(),
        "Resource href should match the expected value");

    // Verify the resource's files
    assertNotNull(resource.getFiles(), "Resource files should not be null");
    assertFalse(resource
        .getFiles()
        .isEmpty(), "Resource files should not be empty");
    assertTrue(resource
            .getFiles()
            .stream()
            .anyMatch(file -> "shared/launchpage.html".equals(file.getHref())),
        "Resource files should contain the launch page");
  }

  /**
   * Tests the getIdentifier method with a manifest that contains an identifier.
   */
  @Test
  void testGetIdentifier() throws IOException, XMLStreamException, ModuleParsingException {
    // Parse a SCORM 2004 manifest that contains an identifier
    String modulePath = "src/test/resources/modules/scorm2004/ContentPackagingMetadata_SCORM20043rdEdition";
    FileAccess fileAccess = new LocalFileAccess(modulePath);
    Scorm2004Parser parser = new Scorm2004Parser(fileAccess);
    Scorm2004Manifest manifest = parser.parseManifest(Scorm2004Parser.MANIFEST_FILE);

    // Get the identifier
    String identifier = manifest.getIdentifier();

    // Verify that the identifier is correct
    assertNotNull(identifier, "Identifier should not be null");
    assertEquals("com.scorm.golfsamples.contentpackaging.metadata.20043rd", identifier,
        "Identifier should match the expected value");
  }

  /**
   * Tests the getVersion method with a manifest that contains a version.
   */
  @Test
  void testGetVersion() throws IOException, XMLStreamException, ModuleParsingException {
    // Parse a SCORM 2004 manifest that contains a version
    String modulePath = "src/test/resources/modules/scorm2004/ContentPackagingMetadata_SCORM20043rdEdition";
    FileAccess fileAccess = new LocalFileAccess(modulePath);
    Scorm2004Parser parser = new Scorm2004Parser(fileAccess);
    Scorm2004Manifest manifest = parser.parseManifest(Scorm2004Parser.MANIFEST_FILE);

    // Get the version
    String version = manifest.getVersion();

    // Verify that the version is correct
    assertNotNull(version, "Version should not be null");
    assertEquals("1", version, "Version should match the expected value");
  }

}
