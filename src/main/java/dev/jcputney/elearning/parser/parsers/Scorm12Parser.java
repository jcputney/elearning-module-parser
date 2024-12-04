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
import dev.jcputney.elearning.parser.enums.ModuleType;
import dev.jcputney.elearning.parser.exception.ModuleParsingException;
import dev.jcputney.elearning.parser.input.scorm12.Scorm12Manifest;
import dev.jcputney.elearning.parser.input.scorm12.ims.cp.Scorm12File;
import dev.jcputney.elearning.parser.input.scorm12.ims.cp.Scorm12Item;
import dev.jcputney.elearning.parser.input.scorm12.ims.cp.Scorm12Organization;
import dev.jcputney.elearning.parser.input.scorm12.ims.cp.Scorm12Resource;
import dev.jcputney.elearning.parser.output.ModuleMetadata;
import dev.jcputney.elearning.parser.output.scorm12.Scorm12Metadata;
import java.io.IOException;
import java.util.List;
import javax.xml.stream.XMLStreamException;

/**
 * Parses SCORM 1.2 modules by reading the imsmanifest.xml file and extracting core metadata.
 * <p>
 * This parser extracts key information such as title, description, identifier, launch URL, version,
 * and optional fields like mastery score and custom data, storing them in a standardized
 * {@link ModuleMetadata} object.
 * </p>
 */
public class Scorm12Parser extends BaseParser<Scorm12Metadata, Scorm12Manifest> {

  public static final String MANIFEST_FILE = "imsmanifest.xml";

  /**
   * Constructs a Scorm12Parser with the specified FileAccess instance.
   *
   * @param fileAccess An instance of FileAccess for reading files in the module package.
   */
  public Scorm12Parser(FileAccess fileAccess) {
    super(fileAccess);
  }

  /**
   * Parses the SCORM 1.2 module located at the specified modulePath. Extracts metadata, including
   * title, description, identifier, launch URL, a default version string, and optional fields like
   * mastery score and custom data.
   *
   * @return A populated ModuleMetadata object containing the extracted metadata.
   * @throws ModuleParsingException if an error occurs during parsing.
   */
  @Override
  public Scorm12Metadata parse() throws ModuleParsingException {
    try {
      // Define the manifest file path and verify its existence
      if (!fileAccess.fileExists(MANIFEST_FILE)) {
        throw new ModuleParsingException(
            "SCORM 1.2 manifest file not found at path: " + MANIFEST_FILE);
      }

      // Parse the manifest XML file using a secure parser from BaseParser
      var manifest = parseManifest(MANIFEST_FILE);
      loadExternalMetadata(manifest);

      // Extract metadata fields using helper methods
      String title = manifest.getTitle();
      String launchUrl = manifest.getLaunchUrl();

      // Validate required fields
      if (title == null || title.isEmpty()) {
        throw new ModuleParsingException(
            "SCORM 1.2 manifest is missing a required <title> element.");
      }
      if (launchUrl == null || launchUrl.isEmpty()) {
        throw new ModuleParsingException(
            "SCORM 1.2 manifest is missing a required <launchUrl> in <resource> element.");
      }

      // Build and return ModuleMetadata using the extracted values
      return new Scorm12Metadata(
          manifest,
          ModuleType.SCORM_12,
          checkForXapi()
      );
    } catch (Exception e) {
      throw new ModuleParsingException("Error parsing SCORM 1.2 module at path: " + this.fileAccess.getRootPath(), e);
    }
  }

  @Override
  protected Class<Scorm12Manifest> getManifestClass() {
    return Scorm12Manifest.class;
  }

  /**
   * Loads external metadata files referenced in the manifest into the metadata object.
   *
   * @param manifest The manifest object to load metadata into.
   */
  private void loadExternalMetadata(Scorm12Manifest manifest)
      throws XMLStreamException, IOException {
    // Load additional metadata files referenced in the manifest
    // For each <resource> element, check for <file> elements with href attribute
    // pointing to additional metadata files
    if (manifest != null) {
      loadExternalMetadataIntoMetadata(manifest.getMetadata());

      List<Scorm12Resource> resources = manifest.getResources().getResourceList();
      if (resources != null) {
        for (Scorm12Resource resource : resources) {
          loadExternalMetadataIntoMetadata(resource.getMetadata());

          List<Scorm12File> files = resource.getFiles();
          if (files != null) {
            for (Scorm12File file : files) {
              file.setExists(fileAccess.fileExists(file.getHref()));
              loadExternalMetadataIntoMetadata(file.getMetadata());
            }
          }
        }
      }

      for (Scorm12Organization organization : manifest.getOrganizations().getOrganizationList()) {
        loadExternalMetadataIntoMetadata(organization.getMetadata());

        loadExternalMetadataForItems(organization.getItems());
      }
    }
  }

  /**
   * Recursively loads external metadata files for each item in the organization.
   *
   * @param items The list of items to load external metadata for.
   */
  private void loadExternalMetadataForItems(List<Scorm12Item> items)
      throws XMLStreamException, IOException {
    if (items != null) {
      for (Scorm12Item item : items) {
        loadExternalMetadataIntoMetadata(item.getMetadata());

        loadExternalMetadataForItems(item.getItems());
      }
    }
  }
}