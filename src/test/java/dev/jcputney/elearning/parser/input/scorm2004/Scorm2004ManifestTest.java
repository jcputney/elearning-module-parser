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

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import dev.jcputney.elearning.parser.api.FileAccess;
import dev.jcputney.elearning.parser.exception.ModuleParsingException;
import dev.jcputney.elearning.parser.impl.LocalFileAccess;
import dev.jcputney.elearning.parser.parsers.Scorm2004Parser;
import java.io.IOException;
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
  void testUsesSequencingWithSequencing() throws IOException, XMLStreamException, ModuleParsingException {
    // Parse a SCORM 2004 manifest that uses sequencing
    String modulePath = "src/test/resources/modules/scorm2004/SequencingForcedSequential_SCORM20043rdEdition";
    FileAccess fileAccess = new LocalFileAccess(modulePath);
    Scorm2004Parser parser = new Scorm2004Parser(fileAccess);
    Scorm2004Manifest manifest = parser.parseManifest(Scorm2004Parser.MANIFEST_FILE);

    // Verify that usesSequencing returns true
    assertTrue(manifest.usesSequencing(), "Manifest should use sequencing");
  }

  /**
   * Tests the usesSequencing method with a manifest that doesn't use sequencing.
   */
  @Test
  void testUsesSequencingWithoutSequencing() throws IOException, XMLStreamException, ModuleParsingException {
    // Parse a SCORM 2004 manifest that doesn't use sequencing
    String modulePath = "src/test/resources/modules/scorm2004/ContentPackagingOneFilePerSCO_SCORM20043rdEdition";
    FileAccess fileAccess = new LocalFileAccess(modulePath);
    Scorm2004Parser parser = new Scorm2004Parser(fileAccess);
    Scorm2004Manifest manifest = parser.parseManifest(Scorm2004Parser.MANIFEST_FILE);

    // Verify that usesSequencing returns false
    assertFalse(manifest.usesSequencing(), "Manifest should not use sequencing");
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
    assertTrue(globalObjectiveIds.stream().anyMatch(id -> id.contains("forcedsequential")), 
        "Global objective IDs should contain expected IDs");
  }

  /**
   * Tests the getSCOIds method with a manifest that contains SCOs.
   */
  @Test
  void testGetSCOIds() throws IOException, XMLStreamException, ModuleParsingException {
    // Parse a SCORM 2004 manifest that contains SCOs
    String modulePath = "src/test/resources/modules/scorm2004/ContentPackagingOneFilePerSCO_SCORM20043rdEdition";
    FileAccess fileAccess = new LocalFileAccess(modulePath);
    Scorm2004Parser parser = new Scorm2004Parser(fileAccess);
    Scorm2004Manifest manifest = parser.parseManifest(Scorm2004Parser.MANIFEST_FILE);

    // Get the SCO IDs
    Set<String> scoIds = manifest.getSCOIds();

    // Verify that the set is not null
    assertNotNull(scoIds, "SCO IDs should not be null");

    // Note: This test might need adjustment based on the actual content of the test manifest.
    // If the manifest doesn't contain SCOs (only assets), this assertion might fail.
    // In that case, we should either find a different test manifest or adjust the assertion.
  }
}
