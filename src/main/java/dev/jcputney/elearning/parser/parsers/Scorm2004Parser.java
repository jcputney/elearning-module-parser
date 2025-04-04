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
import dev.jcputney.elearning.parser.api.ModuleFileProvider;
import dev.jcputney.elearning.parser.exception.ModuleParsingException;
import dev.jcputney.elearning.parser.input.scorm2004.Scorm2004Manifest;
import dev.jcputney.elearning.parser.input.scorm2004.ims.cp.Scorm2004File;
import dev.jcputney.elearning.parser.input.scorm2004.ims.cp.Scorm2004Item;
import dev.jcputney.elearning.parser.input.scorm2004.ims.cp.Scorm2004Organization;
import dev.jcputney.elearning.parser.input.scorm2004.ims.cp.Scorm2004Resource;
import dev.jcputney.elearning.parser.input.scorm2004.ims.cp.Scorm2004SubMetadata;
import dev.jcputney.elearning.parser.output.metadata.scorm2004.Scorm2004Metadata;
import java.io.IOException;
import java.util.List;
import javax.xml.stream.XMLStreamException;

/**
 * Parses SCORM 2004 modules by reading the imsmanifest.xml file and extracting core metadata,
 * including sequencing information, custom data (adlcp:data), prerequisites, mastery score, and any
 * additional metadata files referenced within the manifest.
 */
public class Scorm2004Parser extends BaseParser<Scorm2004Metadata, Scorm2004Manifest> {

  /**
   * The name of the manifest file for SCORM 2004 modules.
   */
  public static final String MANIFEST_FILE = "imsmanifest.xml";

  /**
   * Constructs a Scorm2004Parser with the specified FileAccess instance.
   *
   * @param fileAccess An instance of FileAccess for reading files in the module package.
   */
  public Scorm2004Parser(FileAccess fileAccess) {
    super(fileAccess);
  }

  /**
   * Constructs a Scorm2004Parser with the specified ModuleFileProvider instance.
   *
   * @param moduleFileProvider An instance of ModuleFileProvider for reading files in the module
   * package.
   */
  public Scorm2004Parser(ModuleFileProvider moduleFileProvider) {
    super(moduleFileProvider);
  }

  /**
   * Parses the SCORM 2004 module located at the specified modulePath. Extracts metadata, including
   * title, description, identifier, launch URL, version, sequencing information, custom data,
   * prerequisites, mastery score, and additional metadata from external files.
   *
   * @return A populated ModuleMetadata object containing the extracted metadata.
   * @throws ModuleParsingException if an error occurs during parsing.
   */
  @Override
  public Scorm2004Metadata parse() throws ModuleParsingException {
    try {
      if (!moduleFileProvider.fileExists(MANIFEST_FILE)) {
        throw new ModuleParsingException(
            "SCORM 2004 manifest file not found at path: " + MANIFEST_FILE);
      }

      var manifest = parseManifest(MANIFEST_FILE);
      loadExternalMetadata(manifest);

      String title = manifest.getTitle();
      String launchUrl = manifest.getLaunchUrl();
      if (title.isEmpty()) {
        throw new ModuleParsingException(
            "SCORM 2004 manifest is missing a required <title> element at path: " + MANIFEST_FILE);
      }
      if (launchUrl == null || launchUrl.isEmpty()) {
        throw new ModuleParsingException(
            "SCORM 2004 manifest is missing a required <launchUrl> in <resource> element at path: "
                + MANIFEST_FILE);
      }

      return Scorm2004Metadata.create(manifest, checkForXapi());
    } catch (IOException | XMLStreamException e) {
      throw new ModuleParsingException(
          "Error parsing SCORM 2004 module at path: " + this.moduleFileProvider.getRootPath(), e);
    }
  }

  @Override
  public Class<Scorm2004Manifest> getManifestClass() {
    return Scorm2004Manifest.class;
  }

  /**
   * Loads additional metadata files referenced in the manifest into the metadata object.
   *
   * @param manifest The SCORM 2004 manifest object.
   * @throws XMLStreamException If an error occurs while parsing the XML.
   * @throws IOException If an error occurs while reading the file.
   */
  public void loadExternalMetadata(Scorm2004Manifest manifest)
      throws XMLStreamException, IOException {
    // Load additional metadata files referenced in the manifest
    // For each <resource> element, check for <file> elements with href attribute
    // pointing to additional metadata files
    if (manifest != null) {
      var courseMetadata = manifest.getMetadata();
      loadExternalMetadataIntoMetadata(courseMetadata);

      List<Scorm2004Resource> resources = manifest.getResources().getResourceList();
      if (resources != null) {
        for (Scorm2004Resource resource : resources) {
          var resourceMetadata = resource.getMetadata();
          loadExternalMetadataIntoMetadata(resourceMetadata);

          List<Scorm2004File> files = resource.getFiles();
          if (files != null) {
            for (Scorm2004File file : files) {
              file.setExists(moduleFileProvider.fileExists(file.getHref()));
              Scorm2004SubMetadata subMetadata = file.getMetadata();
              loadExternalMetadataIntoMetadata(subMetadata);
            }
          }
        }
      }

      for (Scorm2004Organization organization : manifest.getOrganizations().getOrganizationList()) {
        var organizationMetadata = organization.getMetadata();
        loadExternalMetadataIntoMetadata(organizationMetadata);

        loadExternalMetadataForItems(organization.getItems());
      }
    }
  }

  /**
   * Recursively loads external metadata files for each item in the organization.
   *
   * @param items The list of items to load external metadata for.
   */
  private void loadExternalMetadataForItems(List<Scorm2004Item> items)
      throws XMLStreamException, IOException {
    if (items != null) {
      for (Scorm2004Item item : items) {
        var itemMetadata = item.getMetadata();
        loadExternalMetadataIntoMetadata(itemMetadata);

        loadExternalMetadataForItems(item.getItems());
      }
    }
  }
}
