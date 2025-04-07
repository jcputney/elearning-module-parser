/* Copyright (c) 2024-2025. Jonathan Putney
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
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import dev.jcputney.elearning.parser.api.FileAccess;
import dev.jcputney.elearning.parser.api.ModuleFileProvider;
import dev.jcputney.elearning.parser.exception.ModuleParsingException;
import dev.jcputney.elearning.parser.impl.LocalFileAccess;
import dev.jcputney.elearning.parser.input.scorm2004.Scorm2004Manifest;
import dev.jcputney.elearning.parser.output.metadata.scorm2004.Scorm2004Metadata;
import java.io.File;
import java.io.IOException;
import javax.xml.stream.XMLStreamException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

/**
 * Tests for the SCORM 2004 parser.
 */
public class Scorm2004ParserTestNew {

  @Test
  void testParseContentPackagingMetadataSCORM20043rdEdition()
      throws XMLStreamException, IOException, ModuleParsingException {
    String modulePath = "src/test/resources/modules/scorm2004/ContentPackagingMetadata_SCORM20043rdEdition/";
    Scorm2004Parser parser = new Scorm2004Parser(new LocalFileAccess(modulePath));

    Scorm2004Metadata metadata = parser.parse();
    assertNotNull(metadata);
    Scorm2004Manifest manifest = metadata.getManifest();
    assertNotNull(manifest);
    assertCommonFields(manifest);

    assertNotNull(manifest.getMetadata().getLom().getGeneral());
    assertEquals(2,
        manifest.getMetadata().getLom().getGeneral().getTitle().getLangStrings().size());
    assertEquals(3, manifest.getMetadata().getLom().getGeneral().getKeywords().size());
    assertEquals("Golf Explained - Metadata Example",
        manifest.getOrganizations().getDefault().getTitle());
    assertEquals("Golf Explained",
        manifest.getMetadata().getLom().getGeneral().getTitle().getLangStrings().get(0).getValue());
    assertEquals("Explicó Golf",
        manifest.getMetadata().getLom().getGeneral().getTitle().getLangStrings().get(1).getValue());
    assertEquals("Golf Explained - Metadata Example", manifest.getTitle());
    assertEquals(39, manifest.getResources().getResourceList().get(0).getFiles().size());
  }

  @Test
  void testParseContentPackagingOneFilePerSCOSCORM20043rdEdition()
      throws XMLStreamException, IOException, ModuleParsingException {
    String modulePath = "src/test/resources/modules/scorm2004/ContentPackagingOneFilePerSCO_SCORM20043rdEdition";
    Scorm2004Parser parser = new Scorm2004Parser(new LocalFileAccess(modulePath));

    Scorm2004Metadata metadata = parser.parse();
    assertNotNull(metadata);
    Scorm2004Manifest manifest = metadata.getManifest();
    assertNotNull(manifest);
    assertCommonFields(manifest);

    assertNull(manifest.getMetadata().getLom());
    assertEquals("Golf Explained - CP One File Per SCO",
        manifest.getOrganizations().getDefault().getTitle());
    assertEquals("Playing/Playing.html", manifest.getLaunchUrl());
    assertEquals(4, manifest.getOrganizations().getDefault().getItems().size());
    assertEquals(19, manifest.getResources().getResourceList().size());
  }

  @Test
  void testParseContentPackagingSingleSCOSCORM20043rdEdition()
      throws XMLStreamException, IOException, ModuleParsingException {
    String modulePath = "src/test/resources/modules/scorm2004/ContentPackagingSingleSCO_SCORM20043rdEdition";
    Scorm2004Parser parser = new Scorm2004Parser(new LocalFileAccess(modulePath));

    Scorm2004Metadata metadata = parser.parse();
    assertNotNull(metadata);
    Scorm2004Manifest manifest = metadata.getManifest();
    assertNotNull(manifest);
    assertCommonFields(manifest);

    assertEquals("Golf Explained - CP Single SCO",
        manifest.getOrganizations().getDefault().getTitle());
    assertEquals("shared/launchpage.html", manifest.getLaunchUrl());
    assertEquals(1, manifest.getOrganizations().getDefault().getItems().size());
    assertEquals(1, manifest.getResources().getResourceList().size());
  }

  @Test
  void testParseRuntimeBasicCallsSCORM20043rdEdition()
      throws XMLStreamException, IOException, ModuleParsingException {
    String modulePath = "src/test/resources/modules/scorm2004/RuntimeBasicCalls_SCORM20043rdEdition";
    Scorm2004Parser parser = new Scorm2004Parser(new LocalFileAccess(modulePath));

    Scorm2004Metadata metadata = parser.parse();
    assertNotNull(metadata);
    Scorm2004Manifest manifest = metadata.getManifest();
    assertNotNull(manifest);
    assertCommonFields(manifest);

    assertEquals("Golf Explained - Run-time Basic Calls",
        manifest.getOrganizations().getDefault().getTitle());
    assertEquals("shared/launchpage.html", manifest.getLaunchUrl());
    assertEquals(1, manifest.getOrganizations().getDefault().getItems().size());
    assertEquals(1, manifest.getResources().getResourceList().size());
  }

  @Test
  void testConstructor_withNullFileAccess_throwsIllegalArgumentException() {
    FileAccess fileAccess = null;
    assertThrows(IllegalArgumentException.class, () -> new Scorm2004Parser(fileAccess));
  }

  @Test
  void testConstructor_withNullModuleFileProvider_throwsIllegalArgumentException() {
    ModuleFileProvider moduleFileProvider = null;
    assertThrows(IllegalArgumentException.class, () -> new Scorm2004Parser(moduleFileProvider));
  }

  @Test
  void testParseManifest_withNullPath_throwsIllegalArgumentException() {
    Scorm2004Parser parser = new Scorm2004Parser(new LocalFileAccess(
        "src/test/resources/modules/scorm2004/ContentPackagingMetadata_SCORM20043rdEdition/"));
    assertThrows(IllegalArgumentException.class, () -> parser.parseManifest(null));
  }

  @Test
  void testParseManifest_withNonExistentFile_throwsIOException(@TempDir File tempDir) {
    String nonExistentFile = new File(tempDir, "nonexistent.xml").getAbsolutePath();
    Scorm2004Parser parser = new Scorm2004Parser(new LocalFileAccess(tempDir.getAbsolutePath()));

    assertThrows(IOException.class, () -> parser.parseManifest(nonExistentFile));
  }

  /**
   * Helper method to assert common fields in a SCORM 2004 manifest.
   */
  private void assertCommonFields(Scorm2004Manifest manifest) {
    assertNotNull(manifest);
    assertNotNull(manifest.getMetadata());
    assertNotNull(manifest.getOrganizations());
    assertNotNull(manifest.getOrganizations().getDefault());
    assertNotNull(manifest.getResources());
    assertNotNull(manifest.getResources().getResourceList());

    assertEquals("ADL SCORM", manifest.getMetadata().getSchema());
    assertEquals("2004 3rd Edition", manifest.getMetadata().getSchemaVersion());

    assertTrue(manifest.getResources().getResourceList().stream().allMatch(
        resource -> resource.getIdentifier() != null && !resource.getIdentifier().isEmpty()));
    assertTrue(manifest.getResources().getResourceList().stream()
        .allMatch(resource -> resource.getScormType() != null));
    assertTrue(manifest.getResources().getResourceList().stream()
        .filter(resource -> !resource.getIdentifier().equals("common_files"))
        .allMatch(resource -> resource.getHref() != null && !resource.getHref().isEmpty()));
  }
}
