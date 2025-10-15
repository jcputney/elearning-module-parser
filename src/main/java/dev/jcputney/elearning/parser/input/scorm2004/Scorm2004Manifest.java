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

package dev.jcputney.elearning.parser.input.scorm2004;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import dev.jcputney.elearning.parser.input.PackageManifest;
import dev.jcputney.elearning.parser.input.lom.LOM;
import dev.jcputney.elearning.parser.input.scorm2004.adl.types.ScormType;
import dev.jcputney.elearning.parser.input.scorm2004.ims.cp.Scorm2004CourseMetadata;
import dev.jcputney.elearning.parser.input.scorm2004.ims.cp.Scorm2004Item;
import dev.jcputney.elearning.parser.input.scorm2004.ims.cp.Scorm2004Organization;
import dev.jcputney.elearning.parser.input.scorm2004.ims.cp.Scorm2004Organizations;
import dev.jcputney.elearning.parser.input.scorm2004.ims.cp.Scorm2004Resource;
import dev.jcputney.elearning.parser.input.scorm2004.ims.cp.Scorm2004Resources;
import dev.jcputney.elearning.parser.input.scorm2004.ims.ss.objective.Scorm2004Objective;
import dev.jcputney.elearning.parser.input.scorm2004.ims.ss.objective.Scorm2004ObjectiveMapping;
import dev.jcputney.elearning.parser.input.scorm2004.ims.ss.sequencing.SequencingCollection;
import dev.jcputney.elearning.parser.input.scorm2004.sequencing.ActivityTree;
import java.time.Duration;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * Represents the SCORM IMS Content Packaging (IMSCP) elements according to the imscp_v1p1 schema.
 *
 * <p>This class encapsulates properties and structure for SCORM content packages,
 * including manifest, metadata, organizations, resources, and dependencies. It is designed to
 * conform to the IMS Content Packaging specification, enabling SCORM-compliant content to be
 * organized and referenced in an LMS.</p>
 *
 * <p>The IMSCP namespace is specified by {@link #NAMESPACE_URI}, and this class
 * is designed to align with the SCORM 2004 standards.</p>
 * <p>
 * The following schema shows the structure of a "manifest" element:
 * <pre>{@code
 *   <?xml version = "1.0" encoding = "UTF-8"?>
 *   <xsd:schema xmlns = "http://www.imsglobal.org/xsd/imscp_v1p1"
 *   	 targetNamespace = "http://www.imsglobal.org/xsd/imscp_v1p1"
 *   	 xmlns:xsi = "http://www.w3.org/2001/XMLSchema-instance"
 *   	 xmlns:xsd = "http://www.w3.org/2001/XMLSchema"
 *   	 version = "IMS CP 1.1.4"
 *   	 elementFormDefault = "qualified">
 *   	<xsd:import namespace = "http://www.w3.org/XML/1998/namespace" schemaLocation = "xml.xsd"/>
 *
 *   	<xsd:attributeGroup name = "attr.base">
 *   		<xsd:attribute ref = "xml:base"/>
 *   	</xsd:attributeGroup>
 *   	<xsd:attributeGroup name = "attr.default">
 *   		<xsd:attribute name = "default" type = "xsd:IDREF"/>
 *   	</xsd:attributeGroup>
 *   	<xsd:attributeGroup name = "attr.href">
 *   		<xsd:attribute name = "href" type = "xsd:anyURI"/>
 *   	</xsd:attributeGroup>
 *   	<xsd:attributeGroup name = "attr.href.req">
 *   		<xsd:attribute name = "href" use = "required" type = "xsd:anyURI"/>
 *   	</xsd:attributeGroup>
 *   	<xsd:attributeGroup name = "attr.identifier.req">
 *   		<xsd:attribute name = "identifier" use = "required" type = "xsd:ID"/>
 *   	</xsd:attributeGroup>
 *   	<xsd:attributeGroup name = "attr.identifier">
 *   		<xsd:attribute name = "identifier" type = "xsd:ID"/>
 *   	</xsd:attributeGroup>
 *   	<xsd:attributeGroup name = "attr.isvisible">
 *   		<xsd:attribute name = "isvisible" type = "xsd:boolean"/>
 *   	</xsd:attributeGroup>
 *   	<xsd:attributeGroup name = "attr.parameters">
 *   		<xsd:attribute name = "parameters" type = "xsd:string"/>
 *   	</xsd:attributeGroup>
 *   	<xsd:attributeGroup name = "attr.identifierref">
 *   		<xsd:attribute name = "identifierref" type = "xsd:string"/>
 *   	</xsd:attributeGroup>
 *   	<xsd:attributeGroup name = "attr.identifierref.req">
 *   		<xsd:attribute name = "identifierref" use = "required" type = "xsd:string"/>
 *   	</xsd:attributeGroup>
 *   	<xsd:attributeGroup name = "attr.resourcetype.req">
 *   		<xsd:attribute name = "type" use = "required" type = "xsd:string"/>
 *   	</xsd:attributeGroup>
 *   	<xsd:attributeGroup name = "attr.structure.req">
 *   		<xsd:attribute name = "structure" default = "hierarchical" type = "xsd:string"/>
 *   	</xsd:attributeGroup>
 *   	<xsd:attributeGroup name = "attr.version">
 *   		<xsd:attribute name = "version" type = "xsd:string"/>
 *   	</xsd:attributeGroup>
 *   	<xsd:group name = "grp.any">
 *   		<xsd:annotation>
 *   		<xsd:documentation>Any namespaced element from any namespace may be included within an "any" element.  The namespace for the imported element must be defined in the instance, and the schema must be imported.  </xsd:documentation>
 *   		</xsd:annotation>
 *   		<xsd:sequence>
 *   			<xsd:any namespace = "##other" processContents = "lax" minOccurs = "0" maxOccurs = "unbounded"/>
 *   		</xsd:sequence>
 *   	</xsd:group>
 *
 *   	<xsd:element name = "dependency" type = "dependencyType"/>
 *   	<xsd:element name = "file" type = "fileType"/>
 *   	<xsd:element name = "item" type = "itemType"/>
 *   	<xsd:element name = "manifest" type = "manifestType"/>
 *   	<xsd:element name = "metadata" type = "metadataType"/>
 *   	<xsd:element name = "organization" type = "organizationType"/>
 *   	<xsd:element name = "organizations" type = "organizationsType"/>
 *   	<xsd:element name = "resource" type = "resourceType"/>
 *   	<xsd:element name = "resources" type = "resourcesType"/>
 *   	<xsd:element name = "schema" type = "schemaType"/>
 *   	<xsd:element name = "schemaversion" type = "schemaversionType"/>
 *   	<xsd:element name = "title" type = "titleType"/>
 *
 *   	<xsd:complexType name = "dependencyType">
 *   		<xsd:sequence>
 *   			<xsd:group ref = "grp.any"/>
 *   		</xsd:sequence>
 *   		<xsd:attributeGroup ref = "attr.identifierref.req"/>
 *   		<xsd:anyAttribute namespace = "##other" processContents = "strict"/>
 *   	</xsd:complexType>
 *
 *   	<xsd:complexType name = "fileType">
 *   		<xsd:sequence>
 *   			<xsd:element ref = "metadata" minOccurs = "0"/>
 *   			<xsd:group ref = "grp.any"/>
 *   		</xsd:sequence>
 *   		<xsd:attributeGroup ref = "attr.href.req"/>
 *   		<xsd:anyAttribute namespace = "##other" processContents = "lax"/>
 *   	</xsd:complexType>
 *
 *   	<xsd:complexType name = "itemType">
 *   		<xsd:sequence>
 *   			<xsd:element ref = "title" minOccurs = "0"/>
 *   			<xsd:element ref = "item" minOccurs = "0" maxOccurs = "unbounded"/>
 *   			<xsd:element ref = "metadata" minOccurs = "0"/>
 *   			<xsd:group ref = "grp.any"/>
 *   		</xsd:sequence>
 *   		<xsd:attributeGroup ref = "attr.identifier.req"/>
 *   		<xsd:attributeGroup ref = "attr.identifierref"/>
 *   		<xsd:attributeGroup ref = "attr.isvisible"/>
 *   		<xsd:attributeGroup ref = "attr.parameters"/>
 *   		<xsd:anyAttribute namespace = "##other" processContents = "lax"/>
 *   	</xsd:complexType>
 *
 *   	<xsd:complexType name = "manifestType">
 *   		<xsd:sequence>
 *   			<xsd:element ref = "metadata" minOccurs = "0"/>
 *   			<xsd:element ref = "organizations"/>
 *   			<xsd:element ref = "resources"/>
 *   			<xsd:element ref = "manifest" minOccurs = "0" maxOccurs = "unbounded"/>
 *   			<xsd:group ref = "grp.any"/>
 *   		</xsd:sequence>
 *   		<xsd:attributeGroup ref = "attr.identifier.req"/>
 *   		<xsd:attributeGroup ref = "attr.version"/>
 *   		<xsd:attribute ref = "xml:base"/>
 *   		<xsd:anyAttribute namespace = "##other" processContents = "lax"/>
 *   	</xsd:complexType>
 *
 *   	<xsd:complexType name = "metadataType">
 *   		<xsd:sequence>
 *   			<xsd:element ref = "schema" minOccurs = "0"/>
 *   			<xsd:element ref = "schemaversion" minOccurs = "0"/>
 *   			<xsd:group ref = "grp.any"/>
 *   		</xsd:sequence>
 *   		<xsd:anyAttribute namespace = "##other" processContents = "lax"/>
 *   	</xsd:complexType>
 *
 *   	<xsd:complexType name = "organizationsType">
 *   		<xsd:sequence>
 *   			<xsd:element ref = "organization" minOccurs = "0" maxOccurs = "unbounded"/>
 *   			<xsd:group ref = "grp.any"/>
 *   		</xsd:sequence>
 *   		<xsd:attributeGroup ref = "attr.default"/>
 *   		<xsd:anyAttribute namespace = "##other" processContents = "lax"/>
 *   	</xsd:complexType>
 *
 *   	<xsd:complexType name = "organizationType">
 *   		<xsd:sequence>
 *   			<xsd:element ref = "title" minOccurs = "0"/>
 *   			<xsd:element ref = "item" maxOccurs = "unbounded"/>
 *   			<xsd:element ref = "metadata" minOccurs = "0"/>
 *   			<xsd:group ref = "grp.any"/>
 *   		</xsd:sequence>
 *   		<xsd:attributeGroup ref = "attr.identifier.req"/>
 *   		<xsd:attributeGroup ref = "attr.structure.req"/>
 *   		<xsd:anyAttribute namespace = "##other" processContents = "lax"/>
 *   	</xsd:complexType>
 *
 *   	<xsd:complexType name = "resourcesType">
 *   		<xsd:sequence>
 *   			<xsd:element ref = "resource" minOccurs = "0" maxOccurs = "unbounded"/>
 *   			<xsd:group ref = "grp.any"/>
 *   		</xsd:sequence>
 *   		<xsd:attributeGroup ref = "attr.base"/>
 *   		<xsd:anyAttribute namespace = "##other" processContents = "lax"/>
 *   	</xsd:complexType>
 *
 *   	<xsd:complexType name = "resourceType">
 *   		<xsd:sequence>
 *   			<xsd:element ref = "metadata" minOccurs = "0"/>
 *   			<xsd:element ref = "file" minOccurs = "0" maxOccurs = "unbounded"/>
 *   			<xsd:element ref = "dependency" minOccurs = "0" maxOccurs = "unbounded"/>
 *   			<xsd:group ref = "grp.any"/>
 *   		</xsd:sequence>
 *   		<xsd:attributeGroup ref = "attr.identifier.req"/>
 *   		<xsd:attributeGroup ref = "attr.resourcetype.req"/>
 *   		<xsd:attributeGroup ref = "attr.base"/>
 *   		<xsd:attributeGroup ref = "attr.href"/>
 *   		<xsd:anyAttribute namespace = "##other" processContents = "lax"/>
 *   	</xsd:complexType>
 *
 *   	<xsd:simpleType name = "schemaType">
 *   		<xsd:restriction base = "xsd:string"/>
 *   	</xsd:simpleType>
 *   	<xsd:simpleType name = "schemaversionType">
 *   		<xsd:restriction base = "xsd:string"/>
 *   	</xsd:simpleType>
 *   	<xsd:simpleType name = "titleType">
 *   		<xsd:restriction base = "xsd:string"/>
 *   	</xsd:simpleType>
 *
 *   </xsd:schema>
 * }</pre>
 */
@JacksonXmlRootElement(localName = "manifest", namespace = Scorm2004Manifest.NAMESPACE_URI)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonFormat(with = JsonFormat.Feature.ACCEPT_CASE_INSENSITIVE_PROPERTIES)
public final class Scorm2004Manifest implements PackageManifest {

  /**
   * The XML namespace URI for SCORM IMS Content Packaging (imscp_v1p1).
   */
  public static final String NAMESPACE_URI = "http://www.imsglobal.org/xsd/imscp_v1p1";

  /**
   * Captures the declared ADLCP namespace on the manifest root element (e.g.,
   * <a href="http://www.adlnet.org/xsd/adlcp_v1p2">...</a> or <a
   * href="http://www.adlnet.org/xsd/adlcp_v1p3">...</a>). This helps with SCORM 2004 edition
   * detection when schemaversion is absent.
   */
  @JacksonXmlProperty(isAttribute = true, localName = "adlcp", namespace = "http://www.w3.org/2000/xmlns/")
  @JsonProperty("xmlns:adlcp")
  private String adlcpNamespaceUri;

  /**
   * Captures the declared IMSSS namespace on the manifest root element. Its presence is a strong
   * signal that sequencing is implemented.
   */
  @JacksonXmlProperty(isAttribute = true, localName = "imsss", namespace = "http://www.w3.org/2000/xmlns/")
  @JsonProperty("xmlns:imsss")
  private String imsssNamespaceUri;

  /**
   * Captures the declared ADL Sequencing namespace on the manifest root element. This namespace
   * exposes ADL sequencing extensions such as constrained choice and rollup considerations.
   */
  @JacksonXmlProperty(isAttribute = true, localName = "adlseq", namespace = "http://www.w3.org/2000/xmlns/")
  @JsonProperty("xmlns:adlseq")
  private String adlseqNamespaceUri;

  /**
   * Captures the declared ADL Navigation namespace on the manifest root element. This namespace
   * enables presentation controls such as adlnav:presentation and hideLMSUI.
   */
  @JacksonXmlProperty(isAttribute = true, localName = "adlnav", namespace = "http://www.w3.org/2000/xmlns/")
  @JsonProperty("xmlns:adlnav")
  private String adlnavNamespaceUri;

  /**
   * Captures the xsi:schemaLocation attribute value to assist in edition detection when
   * schemaversion is missing.
   */
  @JacksonXmlProperty(isAttribute = true, localName = "schemaLocation", namespace = "http://www.w3.org/2001/XMLSchema-instance")
  @JsonProperty("xsi:schemaLocation")
  private String schemaLocation;

  /**
   * The unique identifier for the manifest. This attribute is used to uniquely identify the content
   * package within an LMS.
   */
  @JacksonXmlProperty(isAttribute = true)
  @JsonProperty("identifier")
  private String identifier;

  /**
   * The version of the manifest. Specifies the version of the content package, which may be used by
   * the LMS to manage content versions.
   */
  @JacksonXmlProperty(isAttribute = true)
  @JsonProperty("version")
  private String version;

  /**
   * Metadata associated with the manifest, typically including schema and version information,
   * which provide context for the content package.
   */
  @JacksonXmlProperty(localName = "metadata", namespace = NAMESPACE_URI)
  private Scorm2004CourseMetadata metadata;

  /**
   * Contains the set of organizations that represent the structure of the content. Each
   * organization defines a hierarchical structure of learning resources.
   */
  @JacksonXmlProperty(localName = "organizations", namespace = NAMESPACE_URI)
  private Scorm2004Organizations organizations;

  /**
   * Contains the list of resources within the content package, each representing a learning object
   * or asset to be delivered within the LMS.
   */
  @JacksonXmlProperty(localName = "resources", namespace = NAMESPACE_URI)
  private Scorm2004Resources resources;

  /**
   * Contains the sequencing collection for the content package, which defines rules, objectives,
   * and rollup behaviors for the content.
   */
  @JacksonXmlProperty(localName = "sequencingCollection", namespace = IMSSS.NAMESPACE_URI)
  private SequencingCollection sequencingCollection;

  public Scorm2004Manifest() {
    // no-op
  }

  /**
   * Returns the title of the content package, which is typically the name or title of the course.
   *
   * @return the title of the content package
   */
  @Override
  @JsonIgnore
  public String getTitle() {
    //noinspection DuplicatedCode
    String organizationTitle = Optional
        .ofNullable(organizations)
        .map(Scorm2004Organizations::getDefault)
        .map(Scorm2004Organization::getTitle)
        .orElse(null);
    if (organizationTitle != null && !organizationTitle.isEmpty()) {
      return organizationTitle;
    }

    return Optional
        .ofNullable(metadata)
        .map(Scorm2004CourseMetadata::getLom)
        .map(LOM::getTitle)
        .orElse(null);
  }

  /**
   * Returns the description of the content package, which is typically a summary or overview of the
   * content.
   *
   * @return the description of the content package
   */
  @Override
  @JsonIgnore
  public String getDescription() {
    return Optional
        .ofNullable(metadata)
        .map(Scorm2004CourseMetadata::getLom)
        .map(LOM::getDescription)
        .orElse(null);
  }

  /**
   * Returns the launch URL for the content package, which is typically the URL of the first
   * resource in the package.
   *
   * @return the launch URL for the content package
   */
  @Override
  @JsonIgnore
  public String getLaunchUrl() {
    // Resolve launch URL by walking the default organization items and using identifierref → resource href
    //noinspection DuplicatedCode
    List<String> resourceIds = Optional
        .ofNullable(organizations)
        .map(Scorm2004Organizations::getDefault)
        .map(Scorm2004Organization::getItems)
        .map(this::findAllItemsWithIdentifierRef)
        .orElse(List.of());

    for (String resourceId : resourceIds) {
      String href = Optional
          .ofNullable(resources)
          .flatMap(r -> r.getResourceById(resourceId))
          .map(Scorm2004Resource::getHref)
          .orElse(null);
      if (href != null && !href.isEmpty()) {
        return href;
      }
    }

    return null;
  }

  /**
   * Returns the launch URL for a specific item within the content package, which is typically the
   * URL of the first resource associated with the item.
   *
   * @param itemId the unique identifier of the item
   * @return an Optional containing the launch URL for the item, or empty if no URL is available
   */
  @JsonIgnore
  public String getLaunchUrlForItem(String itemId) {
    // search all organizations for an item with itemId
    Optional<Scorm2004Item> resourceItemOpt = Optional
        .ofNullable(organizations)
        .map(orgs -> orgs.getItemById(itemId));

    if (resourceItemOpt.isEmpty()) {
      return null;
    }

    Scorm2004Item resourceItem = resourceItemOpt.get();

    // Resolve the resource referenced by this item via identifierref
    String identifierRef = resourceItem.getIdentifierRef();
    if (identifierRef == null || identifierRef.isEmpty()) {
      return null;
    }

    String href = Optional
        .ofNullable(resources)
        .flatMap(r -> r.getResourceById(identifierRef))
        .map(Scorm2004Resource::getHref)
        .filter(x -> !x.isEmpty())
        .orElse(null);

    if (href == null) {
      return null;
    }

    String parameters = resourceItem.getParameters();

    if (parameters != null && !parameters.isEmpty()) {
      return parameters.startsWith("?") ? href + parameters : href + "?" + parameters;
    }

    return href;
  }

  /**
   * Retrieves the duration of the content package in a null-safe manner. If no duration is
   * available, a default value of Duration.ZERO is returned.
   *
   * @return the duration of the content package, or Duration.ZERO if unavailable
   */
  @Override
  @JsonIgnore
  public Duration getDuration() {
    return Optional
        .ofNullable(metadata)
        .filter(m -> m.getLom() != null && m
            .getLom()
            .getTechnical() != null && m
            .getLom()
            .getTechnical()
            .getDuration() != null)
        .map(Scorm2004CourseMetadata::getLom)
        .map(lom -> lom
            .getTechnical()
            .getDuration()
            .getDuration())
        .orElse(Duration.ZERO);
  }

  /**
   * Retrieves the set of global objective IDs from the manifest.
   * <p>
   * A global objective ID is defined by the presence of a targetObjectiveID in a mapInfo element.
   * Global objectives are used to share data between different SCOs.
   * </p>
   *
   * @return A set of global objective IDs, or an empty set if none are found
   */
  @JsonProperty(access = JsonProperty.Access.READ_ONLY)
  public Set<String> getGlobalObjectiveIds() {
    if (organizations == null) {
      return Collections.emptySet();
    }

    return organizations
        .getOrganizationList()
        .stream()
        .flatMap(org -> safeStream(org.getItems())) // Null-safe stream for items
        .flatMap(item -> safeStream(getObjectives(item))) // Null-safe stream for objectives
        .flatMap(obj -> safeStream(obj.getMapInfo())) // Null-safe stream for mapInfo
        .map(Scorm2004ObjectiveMapping::getTargetObjectiveID)
        .filter(id -> id != null && !id.isEmpty()) // Filter non-null and non-empty IDs
        .collect(Collectors.toSet());
  }

  /**
   * Retrieves the set of SCO IDs from the manifest.
   * <p>
   * A SCO (Shareable Content Object) is a resource with scormType="sco". SCOs are the trackable,
   * interactive components of a SCORM package that communicate with the LMS.
   * </p>
   * <p>
   * JSON serialization note: This property is exposed as "scoids" for backward compatibility but is
   * marked as read-only to prevent Jackson from attempting to populate it during deserialization
   * (which could cause UnsupportedOperationException due to immutable Sets).
   * </p>
   *
   * @return A set of SCO IDs, or an empty set if none are found
   */
  @JsonProperty(value = "scoids", access = JsonProperty.Access.READ_ONLY)
  public Set<String> getSCOIds() {
    if (resources == null || resources.getResourceList() == null) {
      return Collections.emptySet();
    }

    return resources
        .getResourceList()
        .stream()
        .filter(resource -> resource.getScormType() == ScormType.SCO)
        .map(Scorm2004Resource::getIdentifier)
        .collect(Collectors.toSet());
  }

  /**
   * Builds an ActivityTree from this manifest.
   * <p>
   * The ActivityTree represents the hierarchical structure of learning activities in the content
   * package, including sequencing information. It can be used by an LMS to implement SCORM 2004
   * sequencing and navigation.
   * </p>
   *
   * @return An Optional containing the ActivityTree or empty if no default organization is found
   */
  public ActivityTree buildActivityTree() {
    return ActivityTree.buildFromManifest(this);
  }

  /**
   * Checks if this manifest uses sequencing.
   * <p>
   * A manifest uses sequencing if it has a sequencing collection or if any organization or item in
   * the manifest has sequencing information.
   * </p>
   *
   * @return true if this manifest uses sequencing, false otherwise
   */
  public boolean usesSequencing() {
    return SequencingUsageDetector
        .detect(this)
        .hasSequencing();
  }

  /**
   * Returns the detected sequencing level for this manifest.
   *
   * @return the sequencing level (NONE, MINIMAL, or FULL)
   */
  @JsonIgnore
  public SequencingUsageDetector.SequencingLevel getSequencingLevel() {
    return SequencingUsageDetector
        .detect(this)
        .getLevel();
  }

  /**
   * Returns the set of sequencing indicators discovered within this manifest.
   *
   * @return immutable set of detected sequencing indicators
   */
  @JsonIgnore
  public Set<SequencingUsageDetector.SequencingIndicator> getSequencingIndicators() {
    return SequencingUsageDetector
        .detect(this)
        .getIndicators();
  }

  /**
   * Retrieves the identifier of this SCORM 2004 manifest.
   *
   * @return the identifier of the manifest as a String
   */
  @Override
  public String getIdentifier() {
    return this.identifier;
  }

  /**
   * Sets the identifier for the SCORM 2004 manifest.
   *
   * @param identifier the unique identifier to be assigned to the manifest
   */
  public void setIdentifier(String identifier) {
    this.identifier = identifier;
  }

  /**
   * Retrieves the version of the SCORM 2004 manifest.
   *
   * @return the version as a String
   */
  @Override
  public String getVersion() {
    return this.version;
  }

  /**
   * Sets the version of the SCORM 2004 manifest.
   *
   * @param version the version to be assigned to the manifest
   */
  public void setVersion(String version) {
    this.version = version;
  }

  /**
   * Retrieves the metadata associated with the SCORM 2004 course manifest.
   *
   * @return the metadata of the SCORM 2004 course as an instance of Scorm2004CourseMetadata
   */
  public Scorm2004CourseMetadata getMetadata() {
    return this.metadata;
  }

  /**
   * Sets the metadata for the SCORM 2004 course manifest.
   *
   * @param metadata the metadata to be associated with the SCORM 2004 course
   */
  public void setMetadata(Scorm2004CourseMetadata metadata) {
    this.metadata = metadata;
  }

  /**
   * Retrieves the organizations defined in the SCORM 2004 manifest.
   *
   * @return an instance of Scorm2004Organizations representing the organizations in the manifest
   */
  public Scorm2004Organizations getOrganizations() {
    return this.organizations;
  }

  /**
   * Sets the organizations information for the SCORM 2004 manifest.
   *
   * @param organizations an instance of Scorm2004Organizations representing the organizations to be
   * defined in the manifest
   */
  public void setOrganizations(Scorm2004Organizations organizations) {
    this.organizations = organizations;
  }

  /**
   * Retrieves the SCORM 2004 resources.
   *
   * @return the Scorm2004Resources object containing the resources.
   */
  public Scorm2004Resources getResources() {
    return this.resources;
  }

  /**
   * Sets the SCORM 2004 resources.
   *
   * @param resources the SCORM 2004 resources to be set
   */
  public void setResources(Scorm2004Resources resources) {
    this.resources = resources;
  }

  /**
   * Retrieves the sequencing collection instance associated with this object.
   *
   * @return the SequencingCollection instance maintained by this object
   */
  public SequencingCollection getSequencingCollection() {
    return this.sequencingCollection;
  }

  /**
   * Sets the sequencingCollection with the provided SequencingCollection object.
   *
   * @param sequencingCollection the SequencingCollection to be assigned
   */
  public void setSequencingCollection(SequencingCollection sequencingCollection) {
    this.sequencingCollection = sequencingCollection;
  }

  /**
   * Returns the declared ADLCP namespace URI on the manifest root element, if present.
   */
  public String getAdlcpNamespaceUri() {
    return this.adlcpNamespaceUri;
  }

  public void setAdlcpNamespaceUri(String adlcpNamespaceUri) {
    this.adlcpNamespaceUri = adlcpNamespaceUri;
  }

  /**
   * Returns the declared IMSSS namespace URI on the manifest root element, if present.
   */
  public String getImsssNamespaceUri() {
    return this.imsssNamespaceUri;
  }

  public void setImsssNamespaceUri(String imsssNamespaceUri) {
    this.imsssNamespaceUri = imsssNamespaceUri;
  }

  /**
   * Returns the declared ADL Sequencing namespace URI on the manifest root element, if present.
   */
  public String getAdlseqNamespaceUri() {
    return this.adlseqNamespaceUri;
  }

  /**
   * Sets the namespace URI for ADL sequencing.
   *
   * @param adlseqNamespaceUri the namespace URI to be set for ADL sequencing
   */
  public void setAdlseqNamespaceUri(String adlseqNamespaceUri) {
    this.adlseqNamespaceUri = adlseqNamespaceUri;
  }

  /**
   * Returns the declared ADL Navigation namespace URI on the manifest root element, if present.
   */
  public String getAdlnavNamespaceUri() {
    return this.adlnavNamespaceUri;
  }

  /**
   * Sets the ADL Navigation namespace URI.
   *
   * @param adlnavNamespaceUri the namespace URI to set for ADL Navigation
   */
  public void setAdlnavNamespaceUri(String adlnavNamespaceUri) {
    this.adlnavNamespaceUri = adlnavNamespaceUri;
  }

  /**
   * Returns the declared schemaLocation attribute value from the manifest root element.
   */
  public String getSchemaLocation() {
    return this.schemaLocation;
  }

  /**
   * Sets the schema location for the current instance.
   *
   * @param schemaLocation the schema location to be set, typically a URI or path that specifies the
   * location of the schema.
   */
  public void setSchemaLocation(String schemaLocation) {
    this.schemaLocation = schemaLocation;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (!(o instanceof Scorm2004Manifest that)) {
      return false;
    }

    return new EqualsBuilder()
        .append(identifier, that.identifier)
        .append(version, that.version)
        .append(getMetadata(), that.getMetadata())
        .append(getOrganizations(), that.getOrganizations())
        .append(getResources(), that.getResources())
        .append(getSequencingCollection(), that.getSequencingCollection())
        .isEquals();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder(17, 37)
        .append(identifier)
        .append(version)
        .append(getMetadata())
        .append(getOrganizations())
        .append(getResources())
        .append(getSequencingCollection())
        .toHashCode();
  }

  /**
   * Retrieves the list of objectives from a given item in a null-safe manner.
   *
   * @param item The SCORM item to retrieve objectives from.
   * @return A list of objectives, or an empty list if null.
   */
  private List<Scorm2004Objective> getObjectives(Scorm2004Item item) {
    if (item.getSequencing() != null && item
        .getSequencing()
        .getObjectives() != null) {
      return item
          .getSequencing()
          .getObjectives()
          .getObjectiveList();
    }
    return List.of(); // Return an empty list if objectives are null
  }

  /**
   * Wraps a potentially null collection in a stream.
   *
   * @param collection The collection to wrap.
   * @param <T> The type of elements in the collection.
   * @return A stream of elements, or an empty stream if the collection is null.
   */
  private <T> Stream<T> safeStream(Collection<T> collection) {
    return collection != null ? collection.stream() : Stream.empty();
  }

  /**
   * Recursively searches for all items with a non-null identifierRef in reading order.
   *
   * @param items The list of items to search.
   * @return Ordered list of identifierRefs for items that reference a resource.
   */
  private List<String> findAllItemsWithIdentifierRef(List<Scorm2004Item> items) {
    if (items == null || items.isEmpty()) {
      return List.of();
    }

    List<String> result = new java.util.ArrayList<>();

    // First, collect identifierRefs at this level
    for (Scorm2004Item item : items) {
      if (item.getIdentifierRef() != null && !item
          .getIdentifierRef()
          .isEmpty()) {
        result.add(item.getIdentifierRef());
      }
    }

    // Then, recurse into children
    for (Scorm2004Item item : items) {
      if (item.getItems() != null && !item
          .getItems()
          .isEmpty()) {
        result.addAll(findAllItemsWithIdentifierRef(item.getItems()));
      }
    }

    return result;
  }
}
