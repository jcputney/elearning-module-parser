/*
 * Copyright (c) 2024. Jonathan Putney
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

package dev.jcputney.elearning.parser.parsers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import dev.jcputney.elearning.parser.exception.ModuleParsingException;
import dev.jcputney.elearning.parser.impl.LocalFileAccess;
import dev.jcputney.elearning.parser.input.lom.LOM;
import dev.jcputney.elearning.parser.input.lom.properties.YesNoType;
import dev.jcputney.elearning.parser.input.scorm12.Scorm12Manifest;
import dev.jcputney.elearning.parser.output.metadata.scorm12.Scorm12Metadata;
import org.junit.jupiter.api.Test;

/**
 * Tests for the SCORM 1.2 parser.
 */
public class Scorm12ParserTest {

  @Test
  void testParseScorm12Course_ContentPackagingOneFilePerSCO_SCORM12()
      throws ModuleParsingException {
    String modulePath = "src/test/resources/modules/scorm12/ContentPackagingOneFilePerSCO_SCORM12/";
    Scorm12Parser parser = new Scorm12Parser(new LocalFileAccess(modulePath));
    Scorm12Metadata metadata = parser.parse();
    assertNotNull(metadata);
    Scorm12Manifest manifest = metadata.getManifest();
    assertCommonFields(manifest);

    assertEquals("Golf Explained - CP One File Per SCO", manifest.getTitle());
    assertEquals("Playing/Playing.html", manifest.getLaunchUrl());
    assertEquals(4, manifest.getOrganizations().getDefault().getItems().size());
    assertEquals(4, manifest.getOrganizations().getDefault().getItems()
        .stream()
        .filter(item -> item.getItems() != null && !item.getItems().isEmpty())
        .filter(item -> !item.getIdentifier().isEmpty())
        .filter(item -> item.getIdentifierRef() == null || item.getIdentifierRef().isEmpty())
        .filter(item -> item.getTitle() != null && !item.getTitle().isEmpty())
        .count());
    assertEquals(19, manifest.getResources().getResourceList().size());
  }

  @Test
  void testParseScorm12Course_ContentPackagingSingleSCO_SCORM12()
      throws ModuleParsingException {
    String modulePath = "src/test/resources/modules/scorm12/ContentPackagingSingleSCO_SCORM12";
    Scorm12Parser parser = new Scorm12Parser(new LocalFileAccess(modulePath));
    Scorm12Metadata metadata = parser.parse();
    assertNotNull(metadata);
    Scorm12Manifest manifest = metadata.getManifest();
    assertCommonFields(manifest);

    assertEquals("Golf Explained - CP Single SCO", manifest.getTitle());
    assertEquals("shared/launchpage.html", manifest.getLaunchUrl());
    assertEquals(1, manifest.getOrganizations().getDefault().getItems().size());
    assertEquals(1, manifest.getResources().getResourceList().size());
    assertEquals(39, manifest.getResources().getResourceList().get(0).getFiles().stream()
        .filter(file -> file.getHref() != null && !file.getHref().isEmpty()).count());
  }

  @Test
  void testParseScorm12Course_RuntimeBasicCalls_SCORM12() throws ModuleParsingException {
    String modulePath = "src/test/resources/modules/scorm12/RuntimeBasicCalls_SCORM12";
    Scorm12Parser parser = new Scorm12Parser(new LocalFileAccess(modulePath));
    Scorm12Metadata metadata = parser.parse();
    assertNotNull(metadata);
    Scorm12Manifest manifest = metadata.getManifest();
    assertCommonFields(manifest);

    assertEquals("Golf Explained - Run-time Basic Calls", manifest.getTitle());
    assertEquals("shared/launchpage.html", manifest.getLaunchUrl());
    assertEquals(1, manifest.getOrganizations().getDefault().getItems().size());
    assertEquals(1, manifest.getResources().getResourceList().size());
    assertEquals(39, manifest.getResources().getResourceList().get(0).getFiles().stream()
        .filter(file -> file.getHref() != null && !file.getHref().isEmpty()).count());
  }

  @Test
  void testParseScorm12Course_RuntimeMinimumCalls_SCORM12() throws ModuleParsingException {
    String modulePath = "src/test/resources/modules/scorm12/RuntimeMinimumCalls_SCORM12";
    Scorm12Parser parser = new Scorm12Parser(new LocalFileAccess(modulePath));
    Scorm12Metadata metadata = parser.parse();
    assertNotNull(metadata);
    Scorm12Manifest manifest = metadata.getManifest();
    assertCommonFields(manifest);

    assertEquals("Golf Explained - Minimum Run-time Calls", manifest.getTitle());
    assertEquals("Playing/Playing.html", manifest.getLaunchUrl());
    assertEquals(4, manifest.getOrganizations().getDefault().getItems().size());
    assertEquals(4, manifest.getOrganizations().getDefault().getItems()
        .stream()
        .filter(item -> item.getItems() != null && !item.getItems().isEmpty())
        .filter(item -> !item.getIdentifier().isEmpty())
        .filter(item -> item.getIdentifierRef() == null || item.getIdentifierRef().isEmpty())
        .filter(item -> item.getTitle() != null && !item.getTitle().isEmpty())
        .count());
    assertEquals(19, manifest.getResources().getResourceList().size());
  }

  @Test
  void testParseScorm12Course_ContentPackagingWithMetadata_SCORM12() throws ModuleParsingException {
    String modulePath = "src/test/resources/modules/scorm12/ContentPackagingWithMetadata_SCORM12";
    Scorm12Parser parser = new Scorm12Parser(new LocalFileAccess(modulePath));
    Scorm12Metadata metadata = parser.parse();
    assertNotNull(metadata);
    Scorm12Manifest manifest = metadata.getManifest();
    assertCommonFields(manifest);

    LOM lom = manifest.getMetadata().getLom();
    assertNotNull(lom);

    assertEquals("Catalog", lom.getGeneral().getCatalogEntries().get(0).getCatalog());
    assertEquals(YesNoType.YES,
        lom.getTechnical().getPackageProperties().getBehavior().getAlwaysFlowToFirstSco());
    assertEquals("SCORM 1.2 With Metadata", manifest.getTitle());
    assertEquals("index.html", manifest.getLaunchUrl());
    assertEquals(1, manifest.getOrganizations().getDefault().getItems().size());
    assertEquals(1, manifest.getResources().getResourceList().size());
  }

  private void assertCommonFields(Scorm12Manifest manifest) {
    assertNotNull(manifest);
    assertNull(manifest.getDescription());

    assertEquals("ADL SCORM", manifest.getMetadata().getSchema());
    assertEquals("1.2", manifest.getMetadata().getSchemaVersion());

    assertTrue(manifest.getResources().getResourceList().stream().allMatch(
        resource -> resource.getIdentifier() != null && !resource.getIdentifier().isEmpty()));
    assertTrue(manifest.getResources().getResourceList().stream()
        .allMatch(resource -> resource.getScormType() != null));
    assertTrue(manifest.getResources().getResourceList().stream()
        .filter(resource -> !resource.getIdentifier().equals("common_files"))
        .allMatch(resource -> resource.getHref() != null && !resource.getHref().isEmpty()));
  }
}
