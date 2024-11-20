package dev.jcputney.elearning.parser.parsers;

import dev.jcputney.elearning.parser.api.FileAccess;
import dev.jcputney.elearning.parser.enums.ModuleType;
import dev.jcputney.elearning.parser.exception.ModuleParsingException;
import dev.jcputney.elearning.parser.input.scorm2004.Scorm2004Manifest;
import dev.jcputney.elearning.parser.input.scorm2004.ims.cp.Scorm2004File;
import dev.jcputney.elearning.parser.input.scorm2004.ims.cp.Scorm2004Item;
import dev.jcputney.elearning.parser.input.scorm2004.ims.cp.Scorm2004Organization;
import dev.jcputney.elearning.parser.input.scorm2004.ims.cp.Scorm2004Resource;
import dev.jcputney.elearning.parser.input.scorm2004.ims.cp.Scorm2004SubMetadata;
import dev.jcputney.elearning.parser.output.scorm2004.AdditionalMetadata;
import dev.jcputney.elearning.parser.output.scorm2004.Scorm2004Metadata;
import dev.jcputney.elearning.parser.output.scorm2004.SequencingInfo;
import java.io.IOException;
import java.util.List;
import javax.xml.stream.XMLStreamException;

/**
 * Parses SCORM 2004 modules by reading the imsmanifest.xml file and extracting core metadata,
 * including sequencing information, custom data (adlcp:data), prerequisites, mastery score, and any
 * additional metadata files referenced within the manifest.
 */
public class Scorm2004Parser extends BaseParser<Scorm2004Metadata, Scorm2004Manifest> {

  public static final String MANIFEST_FILE = "imsmanifest.xml";

  public Scorm2004Parser(FileAccess fileAccess) {
    super(fileAccess);
  }

  /**
   * Parses the SCORM 2004 module located at the specified modulePath. Extracts metadata, including
   * title, description, identifier, launch URL, version, sequencing information, custom data,
   * prerequisites, mastery score, and additional metadata from external files.
   *
   * @param modulePath The path to the module's root directory.
   * @return A populated ModuleMetadata object containing the extracted metadata.
   * @throws ModuleParsingException if an error occurs during parsing.
   */
  @Override
  public Scorm2004Metadata parse(String modulePath) throws ModuleParsingException {
    try {
      String manifestPath = modulePath + "/" + MANIFEST_FILE;
      if (!fileAccess.fileExists(manifestPath)) {
        throw new ModuleParsingException(
            "SCORM 2004 manifest file not found at path: " + manifestPath);
      }

      var manifest = parseManifest(manifestPath);
      loadExternalMetadata(manifest, modulePath);

      String title = manifest.getTitle();
      String description = manifest.getDescription();
      String launchUrl = manifest.getLaunchUrl();

      String identifier = manifest.getIdentifier();
      String version = manifest.getVersion();

      if (title.isEmpty()) {
        throw new ModuleParsingException(
            "SCORM 2004 manifest is missing a required <title> element at path: " + manifestPath);
      }
      if (launchUrl == null || launchUrl.isEmpty()) {
        throw new ModuleParsingException(
            "SCORM 2004 manifest is missing a required <launchUrl> in <resource> element at path: "
                + manifestPath);
      }

      List<SequencingInfo> sequencingInfo = null;
      List<String> customData = null;
      String prerequisites = null;
      Double masteryScore = null;

      // Extract additional metadata from external files
      List<AdditionalMetadata> externalMetadata = List.of();

      return new Scorm2004Metadata.Builder()
          .xapiEnabled(checkForXapi(modulePath))
          .title(title)
          .description(description)
          .version(version)
          .identifier(identifier)
          .launchUrl(launchUrl)
          .moduleType(ModuleType.SCORM_2004)
          .sequencingInfo(sequencingInfo)
          .customData(customData)
          .prerequisites(prerequisites)
          .masteryScore(masteryScore)
          .additionalMetadataList(externalMetadata)
          .build();
    } catch (IOException | XMLStreamException e) {
      throw new ModuleParsingException("Error parsing SCORM 2004 module at path: " + modulePath, e);
    }
  }

  @Override
  public Class<Scorm2004Manifest> getManifestClass() {
    return Scorm2004Manifest.class;
  }

  /**
   * Determines if the SCORM 2004 parser supports the module located at the specified modulePath.
   *
   * @param modulePath The path to the module's root directory.
   * @return true if the module is supported by the SCORM 2004 parser, false otherwise.
   */
  @Override
  public boolean isSupported(String modulePath) {
    return fileAccess.fileExists(modulePath + "/" + MANIFEST_FILE);
  }

  /**
   * Loads additional metadata files referenced in the manifest into the metadata object.
   *
   * @param manifest The SCORM 2004 manifest object.
   * @param modulePath The path to the module's root directory.
   */
  public void loadExternalMetadata(Scorm2004Manifest manifest, String modulePath)
      throws XMLStreamException, IOException {
    // Load additional metadata files referenced in the manifest
    // For each <resource> element, check for <file> elements with href attribute
    // pointing to additional metadata files
    if (manifest != null) {
      var courseMetadata = manifest.getMetadata();
      loadExternalMetadataIntoMetadata(courseMetadata, modulePath);

      List<Scorm2004Resource> resources = manifest.getResources().getResourceList();
      if (resources != null) {
        for (Scorm2004Resource resource : resources) {
          var resourceMetadata = resource.getMetadata();
          loadExternalMetadataIntoMetadata(resourceMetadata, modulePath);

          List<Scorm2004File> files = resource.getFiles();
          if (files != null) {
            for (Scorm2004File file : files) {
              file.setExists(fileAccess.fileExists(modulePath + "/" + file.getHref()));
              Scorm2004SubMetadata subMetadata = file.getMetadata();
              loadExternalMetadataIntoMetadata(subMetadata, modulePath);
            }
          }
        }
      }

      for (Scorm2004Organization organization : manifest.getOrganizations().getOrganizationList()) {
        var organizationMetadata = organization.getMetadata();
        loadExternalMetadataIntoMetadata(organizationMetadata, modulePath);

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
  private void loadExternalMetadataForItems(List<Scorm2004Item> items, String modulePath)
      throws XMLStreamException, IOException {
    if (items != null) {
      for (Scorm2004Item item : items) {
        var itemMetadata = item.getMetadata();
        loadExternalMetadataIntoMetadata(itemMetadata, modulePath);

        loadExternalMetadataForItems(item.getItems(), modulePath);
      }
    }
  }
}
