/*
 * Copyright (c) 2024-2026 Jonathan Putney
 *
 * Licensed under the Apache License, Version 2.0 (the "License").
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at the project root LICENSE file
 * or at http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package dev.jcputney.elearning.parser.parsers;

import dev.jcputney.elearning.parser.api.FileAccess;
import dev.jcputney.elearning.parser.api.ModuleFileProvider;
import dev.jcputney.elearning.parser.api.ParserOptions;
import dev.jcputney.elearning.parser.config.FileExistenceValidator;
import dev.jcputney.elearning.parser.exception.ManifestParseException;
import dev.jcputney.elearning.parser.exception.ModuleException;
import dev.jcputney.elearning.parser.exception.ModuleParsingException;
import dev.jcputney.elearning.parser.input.common.serialization.Scorm2004SchemaValidator;
import dev.jcputney.elearning.parser.input.scorm2004.ADLSeq;
import dev.jcputney.elearning.parser.input.scorm2004.IMSSS;
import dev.jcputney.elearning.parser.input.scorm2004.Scorm2004Manifest;
import dev.jcputney.elearning.parser.input.scorm2004.adl.sequencing.ADLObjective;
import dev.jcputney.elearning.parser.input.scorm2004.adl.sequencing.ADLObjectives;
import dev.jcputney.elearning.parser.input.scorm2004.adl.sequencing.MapInfo;
import dev.jcputney.elearning.parser.input.scorm2004.ims.cp.Scorm2004File;
import dev.jcputney.elearning.parser.input.scorm2004.ims.cp.Scorm2004Item;
import dev.jcputney.elearning.parser.input.scorm2004.ims.cp.Scorm2004Organization;
import dev.jcputney.elearning.parser.input.scorm2004.ims.cp.Scorm2004Resource;
import dev.jcputney.elearning.parser.input.scorm2004.ims.ss.objective.Scorm2004Objective;
import dev.jcputney.elearning.parser.input.scorm2004.ims.ss.objective.Scorm2004ObjectiveMapping;
import dev.jcputney.elearning.parser.input.scorm2004.ims.ss.objective.Scorm2004Objectives;
import dev.jcputney.elearning.parser.input.scorm2004.ims.ss.sequencing.Sequencing;
import dev.jcputney.elearning.parser.output.metadata.scorm2004.Scorm2004Metadata;
import dev.jcputney.elearning.parser.util.XmlParsingUtils;
import dev.jcputney.elearning.parser.validation.ValidationIssue;
import dev.jcputney.elearning.parser.validation.ValidationResult;
import dev.jcputney.elearning.parser.validators.Scorm2004ResourceValidator;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLStreamException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

/**
 * Parses SCORM 2004 modules by reading the imsmanifest.xml file and extracting core metadata,
 * including sequencing information, custom data (adlcp:data), prerequisites, mastery score, and any
 * additional metadata files referenced within the manifest.
 */
public final class Scorm2004Parser extends BaseParser<Scorm2004Metadata, Scorm2004Manifest> {

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
   * Constructs a Scorm2004Parser with the specified FileAccess instance and parser options.
   *
   * @param fileAccess An instance of FileAccess for reading files in the module package.
   * @param options The parser options to control validation and calculation behavior.
   */
  public Scorm2004Parser(FileAccess fileAccess, ParserOptions options) {
    super(fileAccess, options);
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
   * Retrieves the class type of the SCORM 2004 manifest.
   *
   * @return The class type of Scorm2004Manifest.
   */
  @Override
  public Class<Scorm2004Manifest> getManifestClass() {
    return Scorm2004Manifest.class;
  }

  /**
   * Parses and optionally XSD-validates the SCORM 2004 manifest.
   */
  @Override
  public Scorm2004Manifest parseManifest(String manifestPath)
      throws IOException, XMLStreamException, ManifestParseException {
    if (manifestPath == null) {
      throw new IllegalArgumentException("Manifest path cannot be null");
    }
    try (InputStream manifestStream = moduleFileProvider.getFileContents(manifestPath)) {
      long maxSize = options.getResolvedMaxManifestSize();
      byte[] bytes = manifestStream.readNBytes((int) Math.min(maxSize + 1, Integer.MAX_VALUE));
      if (bytes.length > maxSize) {
        throw new ManifestParseException(
            String.format("SCORM 2004 manifest exceeds maximum allowed size of %d bytes", maxSize));
      }

      try {
        if (Scorm2004SchemaValidator.isEnabled()) {
          validateSchema(bytes);
        }
      } catch (ModuleParsingException e) {
        throw new ManifestParseException("SCORM 2004 XSD validation failed: " + e.getMessage(), e);
      }

      Scorm2004Manifest manifest = XmlParsingUtils
          .parseXmlToObject(new ByteArrayInputStream(bytes), getManifestClass(),
              manifestPath);
      restoreSequencingObjectives(manifest, bytes);
      loadExternalMetadata(manifest);
      return manifest;
    } catch (IOException e) {
      throw new ManifestParseException(
          String.format("Failed to read manifest file '%s': %s", manifestPath, e.getMessage()), e);
    } catch (XMLStreamException e) {
      throw new ManifestParseException(
          String.format("Failed to parse manifest XML at '%s': %s", manifestPath, e.getMessage()),
          e);
    }
  }

  /**
   * Loads additional metadata files referenced in the manifest into the metadata object.
   *
   * @param manifest The SCORM 2004 manifest object.
   * @throws XMLStreamException If an error occurs while parsing the XML.
   * @throws IOException If an error occurs while reading the file.
   */
  void loadExternalMetadata(Scorm2004Manifest manifest) throws XMLStreamException, IOException {
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

  @Override
  protected ValidationResult validateManifest(Scorm2004Manifest manifest) {
    Scorm2004ResourceValidator validator = new Scorm2004ResourceValidator();
    return validator.validate(manifest);
  }

  @Override
  protected Scorm2004Metadata extractMetadata(Scorm2004Manifest manifest,
      ValidationResult validation)
      throws ModuleException {
    // Validate required fields
    String title = manifest.getTitle();
    String launchUrl = manifest.getLaunchUrl();
    if (title == null || title.isEmpty()) {
      ValidationResult result = ValidationResult.of(
          ValidationIssue.error("SCORM2004_MISSING_TITLE",
              "SCORM 2004 manifest is missing a required <title> element",
              "imsmanifest.xml")
      );
      throw result.toException("Failed to parse SCORM 2004 module");
    }
    if (launchUrl == null || launchUrl.isEmpty()) {
      ValidationResult result = ValidationResult.of(
          ValidationIssue.error("SCORM2004_MISSING_LAUNCH_URL",
              "SCORM 2004 manifest is missing a required <launchUrl> in <resource> element",
              "imsmanifest.xml")
      );
      throw result.toException("Failed to parse SCORM 2004 module");
    }

    Scorm2004Metadata metadata = Scorm2004Metadata.create(manifest, checkForXapi());
    calculateAndSetModuleSize(metadata);
    return metadata;
  }

  @Override
  protected String getManifestFileName() {
    return MANIFEST_FILE;
  }

  /**
   * Calculates the total size of the SCORM module and sets it in the provided metadata object. If
   * an error occurs while calculating the size, the size remains unset (-1 by default).
   *
   * @param metadata The metadata object where the module size will be set.
   */
  private void calculateAndSetModuleSize(Scorm2004Metadata metadata) {
    // Only calculate module size if enabled in options
    if (!options.shouldCalculateModuleSize()) {
      return;
    }

    // Calculate and set the module size
    try {
      long totalSize = moduleFileProvider.getTotalSize();
      metadata.setSizeOnDisk(totalSize);
    } catch (IOException e) {
      // Size remains -1 as default
    }
  }

  /**
   * Validates the provided schema data against the SCORM 2004 XSD standards. If the schema
   * validation fails, an exception is thrown.
   *
   * @param bytes The byte array representation of the SCORM schema to validate.
   * @throws IOException If an I/O error occurs during validation.
   * @throws ModuleParsingException If the schema fails validation or other parsing errors occur.
   */
  private void validateSchema(byte[] bytes) throws IOException, ModuleParsingException {
    try {
      Scorm2004SchemaValidator.validate(bytes);
    } catch (SAXException e) {
      throw new ModuleParsingException("SCORM 2004 XSD validation failed: " + e.getMessage(),
          ValidationResult.of(
              ValidationIssue.error(
                  "SCORM2004_XSD_VALIDATION_FAILED",
                  "SCORM 2004 XSD validation failed: " + e.getMessage(),
                  "imsmanifest.xml"
              )
          ));
    }
  }

  /**
   * Restores namespace-distinct IMS and ADL objective containers after Jackson's XML binding has
   * collapsed same-local-name {@code objectives} elements.
   */
  private void restoreSequencingObjectives(Scorm2004Manifest manifest, byte[] bytes)
      throws ManifestParseException {
    if (manifest == null || bytes == null || bytes.length == 0) {
      return;
    }

    try {
      Document document = parseNamespaceAwareDocument(bytes);
      Element root = document.getDocumentElement();
      restoreOrganizationSequencingObjectives(manifest, root);
      restoreSequencingCollectionObjectives(manifest, root);
    } catch (IOException | ParserConfigurationException | SAXException e) {
      throw new ManifestParseException(
          "Failed to restore SCORM 2004 sequencing objectives: " + e.getMessage(), e);
    }
  }

  private Document parseNamespaceAwareDocument(byte[] bytes)
      throws ParserConfigurationException, IOException, SAXException {
    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    factory.setNamespaceAware(true);
    factory.setXIncludeAware(false);
    factory.setExpandEntityReferences(false);
    factory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
    factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
    factory.setFeature("http://xml.org/sax/features/external-general-entities", false);
    factory.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
    try {
      factory.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, "");
      factory.setAttribute(XMLConstants.ACCESS_EXTERNAL_SCHEMA, "");
    } catch (IllegalArgumentException ignored) {
      // Some parsers do not support JAXP access attributes.
    }
    return factory
        .newDocumentBuilder()
        .parse(new ByteArrayInputStream(bytes));
  }

  private void restoreOrganizationSequencingObjectives(Scorm2004Manifest manifest, Element root) {
    if (manifest.getOrganizations() == null || manifest
        .getOrganizations()
        .getOrganizationList() == null) {
      return;
    }

    Element organizationsElement = firstDirectChild(root, Scorm2004Manifest.NAMESPACE_URI,
        "organizations");
    if (organizationsElement == null) {
      return;
    }

    List<Element> organizationElements = directChildren(organizationsElement,
        Scorm2004Manifest.NAMESPACE_URI, "organization");
    int organizationIndex = 0;
    for (Element organizationElement : organizationElements) {
      Scorm2004Organization organization = findOrganization(manifest
          .getOrganizations()
          .getOrganizationList(), organizationElement, organizationIndex);
      organizationIndex++;
      if (organization == null) {
        continue;
      }

      restoreSequencingObjectives(firstDirectChild(organizationElement, IMSSS.NAMESPACE_URI,
          "sequencing"), organization.getSequencing());
      restoreItemSequencingObjectives(organization.getItems(), organizationElement);
    }
  }

  private Scorm2004Organization findOrganization(List<Scorm2004Organization> organizations,
      Element organizationElement, int fallbackIndex) {
    String identifier = optionalAttribute(organizationElement, "identifier");
    if (identifier != null) {
      for (Scorm2004Organization organization : organizations) {
        if (organization != null && identifier.equals(organization.getIdentifier())) {
          return organization;
        }
      }
    }
    return fallbackIndex < organizations.size() ? organizations.get(fallbackIndex) : null;
  }

  private void restoreItemSequencingObjectives(List<Scorm2004Item> items, Element parentElement) {
    if (items == null || items.isEmpty()) {
      return;
    }

    List<Element> itemElements = directChildren(parentElement, Scorm2004Manifest.NAMESPACE_URI,
        "item");
    int itemIndex = 0;
    for (Element itemElement : itemElements) {
      Scorm2004Item item = findItem(items, itemElement, itemIndex);
      itemIndex++;
      if (item == null) {
        continue;
      }

      restoreSequencingObjectives(firstDirectChild(itemElement, IMSSS.NAMESPACE_URI, "sequencing"),
          item.getSequencing());
      restoreItemSequencingObjectives(item.getItems(), itemElement);
    }
  }

  private Scorm2004Item findItem(List<Scorm2004Item> items, Element itemElement,
      int fallbackIndex) {
    String identifier = optionalAttribute(itemElement, "identifier");
    if (identifier != null) {
      for (Scorm2004Item item : items) {
        if (item != null && identifier.equals(item.getIdentifier())) {
          return item;
        }
      }
    }
    return fallbackIndex < items.size() ? items.get(fallbackIndex) : null;
  }

  private void restoreSequencingCollectionObjectives(Scorm2004Manifest manifest, Element root) {
    if (manifest.getSequencingCollection() == null || manifest
        .getSequencingCollection()
        .getSequencingList() == null) {
      return;
    }

    Element collectionElement = firstDirectChild(root, IMSSS.NAMESPACE_URI,
        "sequencingCollection");
    if (collectionElement == null) {
      return;
    }

    List<Element> sequencingElements = directChildren(collectionElement, IMSSS.NAMESPACE_URI,
        "sequencing");
    List<Sequencing> sequencingList = manifest
        .getSequencingCollection()
        .getSequencingList();
    int sequencingIndex = 0;
    for (Element sequencingElement : sequencingElements) {
      Sequencing sequencing = findSequencing(sequencingList, sequencingElement, sequencingIndex);
      sequencingIndex++;
      restoreSequencingObjectives(sequencingElement, sequencing);
    }
  }

  private Sequencing findSequencing(List<Sequencing> sequencingList, Element sequencingElement,
      int fallbackIndex) {
    String id = optionalAttribute(sequencingElement, "ID");
    if (id != null) {
      for (Sequencing sequencing : sequencingList) {
        if (sequencing != null && id.equals(sequencing.getId())) {
          return sequencing;
        }
      }
    }
    return fallbackIndex < sequencingList.size() ? sequencingList.get(fallbackIndex) : null;
  }

  private void restoreSequencingObjectives(Element sequencingElement, Sequencing sequencing) {
    if (sequencingElement == null || sequencing == null) {
      return;
    }

    Element imsObjectivesElement = firstDirectChild(sequencingElement, IMSSS.NAMESPACE_URI,
        "objectives");
    Element adlObjectivesElement = firstDirectChild(sequencingElement, ADLSeq.NAMESPACE_URI,
        "objectives");
    if (imsObjectivesElement != null || adlObjectivesElement != null) {
      sequencing.setObjectives(parseImsObjectives(imsObjectivesElement));
      sequencing.setAdlObjectives(parseAdlObjectives(adlObjectivesElement));
    }
  }

  private Scorm2004Objectives parseImsObjectives(Element objectivesElement) {
    if (objectivesElement == null) {
      return null;
    }

    Scorm2004Objectives objectives = new Scorm2004Objectives();
    objectives.setPrimaryObjective(parseImsObjective(firstDirectChild(objectivesElement,
        IMSSS.NAMESPACE_URI, "primaryObjective")));

    List<Scorm2004Objective> objectiveList = directChildren(objectivesElement,
        IMSSS.NAMESPACE_URI, "objective")
        .stream()
        .map(this::parseImsObjective)
        .toList();
    if (!objectiveList.isEmpty()) {
      objectives.setObjectiveList(objectiveList);
    }
    return objectives;
  }

  private Scorm2004Objective parseImsObjective(Element objectiveElement) {
    if (objectiveElement == null) {
      return null;
    }

    Scorm2004Objective objective = new Scorm2004Objective();
    objective.setObjectiveID(optionalAttribute(objectiveElement, "objectiveID"));
    setBooleanAttribute(objectiveElement, "satisfiedByMeasure",
        objective::setSatisfiedByMeasure);

    Element minNormalizedMeasure = firstDirectChild(objectiveElement, IMSSS.NAMESPACE_URI,
        "minNormalizedMeasure");
    if (minNormalizedMeasure != null) {
      String value = minNormalizedMeasure
          .getTextContent()
          .trim();
      if (!value.isEmpty()) {
        try {
          objective.setMinNormalizedMeasure(Double.valueOf(value));
        } catch (NumberFormatException ignored) {
          // Minimum normalized measure remains null as default.
        }
      }
    }

    List<Scorm2004ObjectiveMapping> mapInfo = directChildren(objectiveElement,
        IMSSS.NAMESPACE_URI, "mapInfo")
        .stream()
        .map(this::parseImsMapInfo)
        .toList();
    if (!mapInfo.isEmpty()) {
      objective.setMapInfo(mapInfo);
    }
    return objective;
  }

  private Scorm2004ObjectiveMapping parseImsMapInfo(Element mapInfoElement) {
    Scorm2004ObjectiveMapping mapping = new Scorm2004ObjectiveMapping();
    mapping.setTargetObjectiveID(optionalAttribute(mapInfoElement, "targetObjectiveID"));
    setBooleanAttribute(mapInfoElement, "readSatisfiedStatus", mapping::setReadSatisfiedStatus);
    setBooleanAttribute(mapInfoElement, "readNormalizedMeasure",
        mapping::setReadNormalizedMeasure);
    setBooleanAttribute(mapInfoElement, "writeSatisfiedStatus",
        mapping::setWriteSatisfiedStatus);
    setBooleanAttribute(mapInfoElement, "writeNormalizedMeasure",
        mapping::setWriteNormalizedMeasure);
    setBooleanAttribute(mapInfoElement, "readCompletionStatus",
        mapping::setReadCompletionStatus);
    setBooleanAttribute(mapInfoElement, "writeCompletionStatus",
        mapping::setWriteCompletionStatus);
    return mapping;
  }

  private ADLObjectives parseAdlObjectives(Element objectivesElement) {
    if (objectivesElement == null) {
      return null;
    }

    List<ADLObjective> objectiveList = directChildren(objectivesElement, ADLSeq.NAMESPACE_URI,
        "objective")
        .stream()
        .map(this::parseAdlObjective)
        .toList();
    ADLObjectives objectives = new ADLObjectives();
    if (!objectiveList.isEmpty()) {
      objectives.setObjectiveList(objectiveList);
    }
    return objectives;
  }

  private ADLObjective parseAdlObjective(Element objectiveElement) {
    ADLObjective objective = new ADLObjective();
    objective.setObjectiveID(optionalAttribute(objectiveElement, "objectiveID"));
    List<MapInfo> mapInfoList = directChildren(objectiveElement, ADLSeq.NAMESPACE_URI, "mapInfo")
        .stream()
        .map(this::parseAdlMapInfo)
        .toList();
    if (!mapInfoList.isEmpty()) {
      objective.setMapInfoList(mapInfoList);
    }
    return objective;
  }

  private MapInfo parseAdlMapInfo(Element mapInfoElement) {
    MapInfo mapInfo = new MapInfo();
    mapInfo.setTargetObjectiveID(optionalAttribute(mapInfoElement, "targetObjectiveID"));
    setBooleanAttribute(mapInfoElement, "readRawScore", mapInfo::setReadRawScore);
    setBooleanAttribute(mapInfoElement, "readMinScore", mapInfo::setReadMinScore);
    setBooleanAttribute(mapInfoElement, "readMaxScore", mapInfo::setReadMaxScore);
    setBooleanAttribute(mapInfoElement, "readCompletionStatus",
        mapInfo::setReadCompletionStatus);
    setBooleanAttribute(mapInfoElement, "readProgressMeasure",
        mapInfo::setReadProgressMeasure);
    setBooleanAttribute(mapInfoElement, "writeRawScore", mapInfo::setWriteRawScore);
    setBooleanAttribute(mapInfoElement, "writeMinScore", mapInfo::setWriteMinScore);
    setBooleanAttribute(mapInfoElement, "writeMaxScore", mapInfo::setWriteMaxScore);
    setBooleanAttribute(mapInfoElement, "writeCompletionStatus",
        mapInfo::setWriteCompletionStatus);
    setBooleanAttribute(mapInfoElement, "writeProgressMeasure",
        mapInfo::setWriteProgressMeasure);
    return mapInfo;
  }

  private void setBooleanAttribute(Element element, String attributeName,
      Consumer<Boolean> setter) {
    String value = optionalAttribute(element, attributeName);
    if (value != null) {
      setter.accept(Boolean.parseBoolean(value));
    }
  }

  private String optionalAttribute(Element element, String attributeName) {
    if (element == null || !element.hasAttribute(attributeName)) {
      return null;
    }
    String value = element
        .getAttribute(attributeName)
        .trim();
    return value.isEmpty() ? null : value;
  }

  private Element firstDirectChild(Element parent, String namespaceUri, String localName) {
    List<Element> children = directChildren(parent, namespaceUri, localName);
    return children.isEmpty() ? null : children.get(0);
  }

  private List<Element> directChildren(Element parent, String namespaceUri, String localName) {
    if (parent == null) {
      return List.of();
    }

    List<Element> children = new ArrayList<>();
    Node child = parent.getFirstChild();
    while (child != null) {
      if (child instanceof Element element
          && namespaceUri.equals(element.getNamespaceURI())
          && localName.equals(element.getLocalName())) {
        children.add(element);
      }
      child = child.getNextSibling();
    }
    return children;
  }

  /**
   * Loads resources metadata files referenced in the manifest into the metadata object.
   *
   * @param resources The list of resources to load metadata for.
   * @throws XMLStreamException If an error occurs while parsing the XML.
   * @throws IOException If an error occurs while reading the file.
   */
  private void loadResourcesMetadata(List<Scorm2004Resource> resources)
      throws XMLStreamException, IOException {
    if (resources == null) {
      return;
    }

    for (Scorm2004Resource resource : resources) {
      loadExternalMetadataIntoMetadata(resource.getMetadata());
      loadFilesMetadata(resource.getFiles());
    }
  }

  /**
   * Loads files metadata files referenced in the manifest into the metadata object.
   *
   * @param files The list of files to load metadata for.
   * @throws XMLStreamException If an error occurs while parsing the XML.
   * @throws IOException If an error occurs while reading the file.
   */
  private void loadFilesMetadata(List<Scorm2004File> files) throws XMLStreamException, IOException {
    if (files == null) {
      return;
    }

    // Only check file existence if validation is enabled in options
    if (FileExistenceValidator.isEnabled()) {
      // Collect all file paths for batch checking
      List<String> filePaths = new ArrayList<>();
      for (Scorm2004File file : files) {
        if (file.getHref() != null) {
          filePaths.add(file.getHref());
        }
      }

      // Check file existence in a batch
      Map<String, Boolean> existenceMap = Map.of();
      if (!filePaths.isEmpty()) {
        existenceMap = moduleFileProvider.fileExistsBatch(filePaths);
      }

      // Apply results
      for (Scorm2004File file : files) {
        if (file.getHref() != null) {
          file.setExists(existenceMap.getOrDefault(file.getHref(), false));
        } else {
          file.setExists(false);
        }
      }
    }

    // Always load external metadata
    for (Scorm2004File file : files) {
      loadExternalMetadataIntoMetadata(file.getMetadata());
    }
  }

  /**
   * Loads organizations metadata files referenced in the manifest into the metadata object.
   *
   * @param organizations The list of organizations to load metadata for.
   * @throws XMLStreamException If an error occurs while parsing the XML.
   * @throws IOException If an error occurs while reading the file.
   */
  private void loadOrganizationsMetadata(List<Scorm2004Organization> organizations)
      throws XMLStreamException, IOException {
    if (organizations == null) {
      return;
    }

    for (Scorm2004Organization organization : organizations) {
      loadExternalMetadataIntoMetadata(organization.getMetadata());
      loadExternalMetadataForItems(organization.getItems());
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
