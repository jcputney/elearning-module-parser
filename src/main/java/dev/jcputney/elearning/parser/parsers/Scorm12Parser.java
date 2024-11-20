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

  private static final String MANIFEST_FILE = "imsmanifest.xml";

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
   * @param modulePath The path to the module's root directory.
   * @return A populated ModuleMetadata object containing the extracted metadata.
   * @throws ModuleParsingException if an error occurs during parsing.
   */
  @Override
  public Scorm12Metadata parse(String modulePath) throws ModuleParsingException {
    try {
      // Define the manifest file path and verify its existence
      String manifestPath = modulePath + "/" + MANIFEST_FILE;
      if (!fileAccess.fileExists(manifestPath)) {
        throw new ModuleParsingException(
            "SCORM 1.2 manifest file not found at path: " + manifestPath);
      }

      // Parse the manifest XML file using a secure parser from BaseParser
      var manifest = parseManifest(manifestPath);
      loadExternalMetadata(manifest, modulePath);

      // Extract metadata fields using helper methods
      String title = manifest.getTitle();
      String description = manifest.getDescription();
      String identifier = manifest.getIdentifier();
      String launchUrl = manifest.getLaunchUrl();
      String version = "SCORM 1.2";  // Default version, as SCORM 1.2 does not typically specify a version

      // Extract optional fields if present
      Double masteryScore = null;
      List<String> customData = List.of();

      // Validate required fields
      if (title.isEmpty()) {
        throw new ModuleParsingException(
            "SCORM 1.2 manifest is missing a required <title> element at path: " + manifestPath);
      }
      if (launchUrl.isEmpty()) {
        throw new ModuleParsingException(
            "SCORM 1.2 manifest is missing a required <launchUrl> in <resource> element at path: "
                + manifestPath);
      }

      // Build and return ModuleMetadata using the extracted values
      return new Scorm12Metadata.Builder()
          .xapiEnabled(checkForXapi(modulePath))
          .title(title)
          .description(description)
          .version(version)
          .identifier(identifier)
          .launchUrl(launchUrl)
          .moduleType(ModuleType.SCORM_12)
          .masteryScore(masteryScore)
          .customData(customData)
          .build();
    } catch (Exception e) {
      throw new ModuleParsingException("Error parsing SCORM 1.2 module at path: " + modulePath, e);
    }
  }

  /**
   * Checks if this parser can handle the module located at the specified path by verifying the
   * presence of imsmanifest.xml.
   * <p>
   * SCORM 1.2 modules are identified by the presence of an imsmanifest.xml file in the root
   * directory.
   * </p>
   *
   * @param modulePath The path to the module's root directory.
   * @return True if imsmanifest.xml is present; false otherwise.
   */
  @Override
  public boolean isSupported(String modulePath) {
    return fileAccess.fileExists(modulePath + "/" + MANIFEST_FILE);
  }

  @Override
  protected Class<Scorm12Manifest> getManifestClass() {
    return Scorm12Manifest.class;
  }

  private void loadExternalMetadata(Scorm12Manifest manifest, String modulePath)
      throws XMLStreamException, IOException {
    // Load additional metadata files referenced in the manifest
    // For each <resource> element, check for <file> elements with href attribute
    // pointing to additional metadata files
    if (manifest != null) {
      loadExternalMetadataIntoMetadata(manifest.getMetadata(), modulePath);

      List<Scorm12Resource> resources = manifest.getResources().getResources();
      if (resources != null) {
        for (Scorm12Resource resource : resources) {
          loadExternalMetadataIntoMetadata(resource.getMetadata(), modulePath);

          List<Scorm12File> files = resource.getFiles();
          if (files != null) {
            for (Scorm12File file : files) {
              file.setExists(fileAccess.fileExists(modulePath + "/" + file.getHref()));
              loadExternalMetadataIntoMetadata(file.getMetadata(), modulePath);
            }
          }
        }
      }

      for (Scorm12Organization organization : manifest.getOrganizations().getOrganizationList()) {
        loadExternalMetadataIntoMetadata(organization.getMetadata(), modulePath);

        loadExternalMetadataForItems(organization.getItems(), modulePath);
      }
    }
  }

  /**
   * Recursively loads external metadata files for each item in the organization.
   *
   * @param items The list of items to load external metadata for.
   * @param modulePath The path to the module's root directory.
   */
  private void loadExternalMetadataForItems(List<Scorm12Item> items, String modulePath)
      throws XMLStreamException, IOException {
    if (items != null) {
      for (Scorm12Item item : items) {
        loadExternalMetadataIntoMetadata(item.getMetadata(), modulePath);

        loadExternalMetadataForItems(item.getItems(), modulePath);
      }
    }
  }
}