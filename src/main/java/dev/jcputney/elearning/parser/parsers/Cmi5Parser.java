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

package dev.jcputney.elearning.parser.parsers;

import dev.jcputney.elearning.parser.api.FileAccess;
import dev.jcputney.elearning.parser.enums.ModuleType;
import dev.jcputney.elearning.parser.exception.ModuleParsingException;
import dev.jcputney.elearning.parser.input.cmi5.Cmi5Manifest;
import dev.jcputney.elearning.parser.output.metadata.cmi5.Cmi5Metadata;
import java.io.IOException;
import javax.xml.stream.XMLStreamException;

/**
 * Cmi5Parser is responsible for parsing cmi5-specific metadata from the cmi5.xml file.
 * <p>
 * The "cmi5" xAPI-based specification includes Assignable Units (AUs) as discrete learning objects
 * with metadata for LMS tracking and reporting. This parser extracts metadata such as the title,
 * launch URL, prerequisites, dependencies, detailed AU information, and custom metadata.
 * </p>
 */
public class Cmi5Parser extends BaseParser<Cmi5Metadata, Cmi5Manifest> {

  /**
   * The name of the cmi5 XML file that contains the module metadata.
   */
  public static final String CMI5_XML = "cmi5.xml";

  /**
   * Constructs a Cmi5Parser with the specified FileAccess instance.
   *
   * @param fileAccess An instance of FileAccess for reading files in the module package.
   */
  public Cmi5Parser(FileAccess fileAccess) {
    super(fileAccess);
  }

  /**
   * Parses the cmi5 module located at the specified modulePath.
   * <p>
   * This method reads the cmi5.xml file to extract metadata such as the title, launch URL,
   * prerequisites, dependencies, list of Assignable Units (AUs), and custom data for LMS tracking.
   * Since cmi5 is always xAPI-enabled, the isXapiEnabled flag is set to true for all cmi5 modules.
   * </p>
   *
   * @return A Cmi5Metadata object containing the parsed metadata.
   * @throws ModuleParsingException If the module's manifest cannot be parsed or if required fields
   * are missing.
   */
  @Override
  public Cmi5Metadata parse() throws ModuleParsingException {
    try {
      // Parse cmi5-specific metadata from cmi5.xml
      var manifest = parseManifest(CMI5_XML);

      String title = manifest.getTitle();
      if (title == null || title.isEmpty()) {
        throw new ModuleParsingException("cmi5 module missing required title field");
      }
      String launchUrl = manifest.getLaunchUrl();
      if (launchUrl == null || launchUrl.isEmpty()) {
        throw new ModuleParsingException("cmi5 module missing required launch URL field");
      }

      // Build and return the Cmi5Metadata
      return Cmi5Metadata.create(manifest, true); // cmi5 modules are always xAPI-enabled

    } catch (Exception e) {
      throw new ModuleParsingException(
          "Error parsing cmi5 module at path: " + this.moduleFileProvider.getRootPath(), e);
    }
  }

  @Override
  void loadExternalMetadata(Cmi5Manifest manifest) throws XMLStreamException, IOException {
    // No external metadata loading is required for cmi5
  }

  @Override
  protected Class<Cmi5Manifest> getManifestClass() {
    return Cmi5Manifest.class;
  }
}
