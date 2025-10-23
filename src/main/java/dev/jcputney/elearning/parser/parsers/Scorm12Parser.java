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
import dev.jcputney.elearning.parser.api.ModuleFileProvider;
import dev.jcputney.elearning.parser.config.FileExistenceValidator;
import dev.jcputney.elearning.parser.config.ModuleSizeCalculator;
import dev.jcputney.elearning.parser.exception.ManifestParseException;
import dev.jcputney.elearning.parser.exception.ModuleException;
import dev.jcputney.elearning.parser.exception.ModuleParsingException;
import dev.jcputney.elearning.parser.input.scorm12.Scorm12Manifest;
import dev.jcputney.elearning.parser.validation.ValidationIssue;
import dev.jcputney.elearning.parser.validation.ValidationResult;
import dev.jcputney.elearning.parser.input.scorm12.ims.cp.Scorm12File;
import dev.jcputney.elearning.parser.input.scorm12.ims.cp.Scorm12Item;
import dev.jcputney.elearning.parser.input.scorm12.ims.cp.Scorm12Organization;
import dev.jcputney.elearning.parser.input.scorm12.ims.cp.Scorm12Resource;
import dev.jcputney.elearning.parser.output.ModuleMetadata;
import dev.jcputney.elearning.parser.output.metadata.scorm12.Scorm12Metadata;
import dev.jcputney.elearning.parser.util.FileUtils;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.xml.stream.XMLStreamException;

/**
 * Parses SCORM 1.2 modules by reading the imsmanifest.xml file and extracting core metadata.
 * <p>
 * This parser extracts key information such as title, description, identifier, launch URL, version,
 * and optional fields like mastery score and custom data, storing them in a standardized
 * {@link ModuleMetadata} object.
 * </p>
 */
public final class Scorm12Parser extends BaseParser<Scorm12Metadata, Scorm12Manifest> {

  /**
   * The name of the manifest file for SCORM 1.2 modules.
   */
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
   * Constructs a Scorm12Parser with the specified FileAccess instance and parser options.
   *
   * @param fileAccess An instance of FileAccess for reading files in the module package.
   * @param options The parser options to control validation and calculation behavior.
   */
  public Scorm12Parser(FileAccess fileAccess,
      dev.jcputney.elearning.parser.api.ParserOptions options) {
    super(fileAccess, options);
  }

  /**
   * Constructs a Scorm12Parser with the specified ModuleFileProvider instance.
   *
   * @param moduleFileProvider An instance of ModuleFileProvider for reading files in the module
   * package.
   */
  public Scorm12Parser(ModuleFileProvider moduleFileProvider) {
    super(moduleFileProvider);
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
  public Scorm12Metadata parse() throws ModuleException {
    try {
      // Parse and validate the manifest
      var manifest = parseAndValidateManifest();

      // Load external metadata
      loadExternalMetadata(manifest);

      // Validate required fields
      validateRequiredFields(manifest);

      // Check for xAPI support
      boolean hasXapi = checkForXapi();

      // Build and return ModuleMetadata
      return createMetadata(manifest, hasXapi);
    } catch (IOException | XMLStreamException e) {
      throw new ManifestParseException(
          String.format("Failed to parse SCORM 1.2 module at '%s': %s",
              this.moduleFileProvider.getRootPath(), e.getMessage()), e);
    } catch (ModuleParsingException e) {
      // Re-throw ModuleParsingException directly without wrapping
      throw e;
    } catch (Exception e) {
      // Catch any other unexpected exceptions
      throw new ManifestParseException(
          String.format("Unexpected error parsing SCORM 1.2 module at '%s': %s",
              this.moduleFileProvider.getRootPath(),
              e.getMessage()), e);
    }
  }

  /**
   * Loads external metadata files referenced in the manifest into the metadata object.
   *
   * @param manifest The SCORM 1.2 manifest object.
   * @throws XMLStreamException If an error occurs while parsing the XML.
   * @throws IOException If an error occurs while reading the file.
   */
  void loadExternalMetadata(Scorm12Manifest manifest) throws XMLStreamException, IOException {
    if (manifest == null) {
      return;
    }

    loadExternalMetadataIntoMetadata(manifest.getMetadata());

    loadResourcesMetadata(manifest
        .getResources()
        .getResourceList());
    loadOrganizationsMetadata(manifest
        .getOrganizations()
        .getOrganizationList());
  }

  /**
   * Returns the class type of the SCORM 1.2 manifest.
   *
   * @return The class object representing Scorm12Manifest.
   */
  @Override
  protected Class<Scorm12Manifest> getManifestClass() {
    return Scorm12Manifest.class;
  }

  /**
   * Parses and validates the SCORM 1.2 manifest file.
   *
   * @return The parsed manifest object.
   * @throws IOException If an error occurs while reading the file.
   * @throws XMLStreamException If an error occurs while parsing the XML.
   * @throws ModuleParsingException If the manifest file is not found.
   */
  private Scorm12Manifest parseAndValidateManifest()
      throws IOException, XMLStreamException, ManifestParseException, ModuleParsingException {
    // Find the manifest file (case-insensitive)
    var files = moduleFileProvider.listFiles("");
    String manifestFile = FileUtils.findFileIgnoreCase(files, MANIFEST_FILE);

    if (manifestFile == null) {
      ValidationResult result = ValidationResult.of(
          ValidationIssue.error("SCORM12_MISSING_MANIFEST",
              String.format("SCORM 1.2 manifest file not found: %s in module at '%s'", MANIFEST_FILE, moduleFileProvider.getRootPath()),
              "package root")
      );
      throw result.toException("Failed to parse SCORM 1.2 module");
    }

    // Parse the manifest XML file using a secure parser from BaseParser
    return parseManifest(manifestFile);
  }

  /**
   * Validates that the manifest contains all required fields.
   *
   * @param manifest The manifest to validate.
   * @throws ModuleParsingException If any required fields are missing.
   */
  private void validateRequiredFields(Scorm12Manifest manifest) throws ModuleParsingException {
    String title = manifest.getTitle();
    String launchUrl = manifest.getLaunchUrl();

    if (title == null || title.isEmpty()) {
      ValidationResult result = ValidationResult.of(
          ValidationIssue.error("SCORM12_MISSING_TITLE",
              String.format("SCORM 1.2 manifest at '%s' is missing required <title> element", moduleFileProvider.getRootPath()),
              "imsmanifest.xml")
      );
      throw result.toException("Failed to parse SCORM 1.2 module");
    }
    if (launchUrl == null || launchUrl.isEmpty()) {
      ValidationResult result = ValidationResult.of(
          ValidationIssue.error("SCORM12_MISSING_LAUNCH_URL",
              String.format("SCORM 1.2 manifest at '%s' is missing required launch URL in <resource> element", moduleFileProvider.getRootPath()),
              "imsmanifest.xml")
      );
      throw result.toException("Failed to parse SCORM 1.2 module");
    }
  }

  /**
   * Creates a metadata object from the parsed manifest.
   *
   * @param manifest The parsed manifest.
   * @param hasXapi Whether the module has xAPI support.
   * @return A new Scorm12Metadata object.
   */
  private Scorm12Metadata createMetadata(Scorm12Manifest manifest, boolean hasXapi) {
    Scorm12Metadata metadata = Scorm12Metadata.create(manifest, hasXapi);

    // Only calculate module size if enabled in options
    if (options.shouldCalculateModuleSize()) {
      // Calculate and set the module size
      try {
        long totalSize = moduleFileProvider.getTotalSize();
        metadata.setSizeOnDisk(totalSize);
      } catch (IOException e) {
        // Size remains -1 as default - this is not an error
      }
    }

    return metadata;
  }

  /**
   * Loads external metadata files for resources in the manifest.
   *
   * @param resources The list of resources to load metadata for.
   * @throws XMLStreamException If an error occurs while parsing the XML.
   * @throws IOException If an error occurs while reading the file.
   */
  private void loadResourcesMetadata(List<Scorm12Resource> resources)
      throws XMLStreamException, IOException {
    if (resources == null) {
      return;
    }

    for (Scorm12Resource resource : resources) {
      loadExternalMetadataIntoMetadata(resource.getMetadata());
      loadFilesMetadata(resource.getFiles());
    }
  }

  /**
   * Loads external metadata files for files in the manifest.
   *
   * @param files The list of files to load metadata for.
   * @throws XMLStreamException If an error occurs while parsing the XML.
   * @throws IOException If an error occurs while reading the file.
   */
  private void loadFilesMetadata(List<Scorm12File> files) throws XMLStreamException, IOException {
    if (files == null || files.isEmpty()) {
      return;
    }

    // Only check file existence if validation is enabled in options
    if (FileExistenceValidator.isEnabled()) {
      // Collect all file paths for batch checking
      List<String> filePaths = new ArrayList<>();
      for (Scorm12File file : files) {
        if (file.getHref() != null) {
          filePaths.add(file.getHref());
        }
      }

      // Check file existence in a batch
      if (!filePaths.isEmpty()) {
        Map<String, Boolean> existenceMap = moduleFileProvider.fileExistsBatch(filePaths);

        // Update file existence status
        for (Scorm12File file : files) {
          if (file.getHref() != null) {
            Boolean exists = existenceMap.get(file.getHref());
            file.setExists(exists != null && exists);
          }
        }
      }
    }

    // Always load external metadata for each file
    for (Scorm12File file : files) {
      loadExternalMetadataIntoMetadata(file.getMetadata());
    }
  }

  /**
   * Loads external metadata files for organizations in the manifest.
   *
   * @param organizations The list of organizations to load metadata for.
   * @throws XMLStreamException If an error occurs while parsing the XML.
   * @throws IOException If an error occurs while reading the file.
   */
  private void loadOrganizationsMetadata(List<Scorm12Organization> organizations)
      throws XMLStreamException, IOException {
    if (organizations == null) {
      return;
    }

    for (Scorm12Organization organization : organizations) {
      loadExternalMetadataIntoMetadata(organization.getMetadata());
      loadExternalMetadataForItems(organization.getItems());
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

        // Recursively process child items
        List<Scorm12Item> childItems = item.getItems();
        if (childItems != null && !childItems.isEmpty()) {
          loadExternalMetadataForItems(childItems);
        }
      }
    }
  }
}
