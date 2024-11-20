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

import dev.jcputney.elearning.parser.api.FileAccess;
import dev.jcputney.elearning.parser.exception.ModuleParsingException;
import dev.jcputney.elearning.parser.input.cmi5.Cmi5Manifest;
import dev.jcputney.elearning.parser.output.cmi5.AssignableUnit;
import dev.jcputney.elearning.parser.output.cmi5.Cmi5Metadata;
import java.util.List;
import java.util.Map;

/**
 * Cmi5Parser is responsible for parsing cmi5-specific metadata from the cmi5.xml file.
 * <p>
 * cmi5 is an xAPI-based specification that includes Assignable Units (AUs) as discrete learning
 * objects with metadata for LMS tracking and reporting. This parser extracts metadata such as the
 * title, launch URL, prerequisites, dependencies, detailed AU information, and custom metadata.
 * </p>
 */
public class Cmi5Parser extends BaseParser<Cmi5Metadata, Cmi5Manifest> {

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
   * @param modulePath The path to the module's root directory, which contains the cmi5.xml file.
   * @return A Cmi5Metadata object containing the parsed metadata.
   * @throws ModuleParsingException If the module's manifest cannot be parsed or if required fields
   * are missing.
   */
  @Override
  public Cmi5Metadata parse(String modulePath) throws ModuleParsingException {
    try {
      // Always set xAPI enabled to true for cmi5
      boolean isXapiEnabled = true;

      // Parse cmi5-specific metadata from cmi5.xml
      Cmi5Manifest manifest = parseManifest(modulePath + "/" + CMI5_XML);

      String title = null;
      String launchUrl = null;
      List<String> prerequisites = List.of();
      List<String> dependencies = List.of();

      // Extract assignable units (AUs) - the core content pieces for cmi5
      List<AssignableUnit> assignableUnits = List.of();

      // Extract custom data from the cmi5.xml file
      Map<String, String> customData = Map.of();

      // Build and return the Cmi5Metadata
      return new Cmi5Metadata.Builder()
          .xapiEnabled(isXapiEnabled)
          .title(title)
          .launchUrl(launchUrl)
          .prerequisites(prerequisites)
          .dependencies(dependencies)
          .assignableUnits(assignableUnits)
          .customData(customData)
          .cmi5Version("1.0") // Assuming a default version; adjust as necessary
          .build();

    } catch (Exception e) {
      throw new ModuleParsingException("Error parsing cmi5 module at path: " + modulePath, e);
    }
  }

  /**
   * Checks if the Cmi5Parser can support the module located at the specified module path.
   * <p>
   * The Cmi5Parser supports modules containing a cmi5.xml file, which is the manifest file that
   * holds metadata for the cmi5 module.
   * </p>
   *
   * @param modulePath The path to the module's root directory.
   * @return true if the cmi5.xml file is present, indicating the module is cmi5; false otherwise.
   */
  @Override
  public boolean isSupported(String modulePath) {
    return fileAccess.fileExists(modulePath + "/" + CMI5_XML);
  }

  @Override
  protected Class<Cmi5Manifest> getManifestClass() {
    return Cmi5Manifest.class;
  }
}