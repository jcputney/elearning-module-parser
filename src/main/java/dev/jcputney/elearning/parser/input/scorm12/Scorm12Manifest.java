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

package dev.jcputney.elearning.parser.input.scorm12;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import dev.jcputney.elearning.parser.input.PackageManifest;
import dev.jcputney.elearning.parser.input.lom.LOM;
import dev.jcputney.elearning.parser.input.scorm12.ims.cp.Scorm12Item;
import dev.jcputney.elearning.parser.input.scorm12.ims.cp.Scorm12Metadata;
import dev.jcputney.elearning.parser.input.scorm12.ims.cp.Scorm12Organization;
import dev.jcputney.elearning.parser.input.scorm12.ims.cp.Scorm12Organizations;
import dev.jcputney.elearning.parser.input.scorm12.ims.cp.Scorm12Resource;
import dev.jcputney.elearning.parser.input.scorm12.ims.cp.Scorm12Resources;
import java.time.Duration;
import java.util.List;
import java.util.Optional;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * Represents the SCORM IMS Content Packaging (IMSCP) elements according to the imscp_rootv1p1p2
 * schema.
 *
 * <p>This class encapsulates properties and structure for SCORM content packages,
 * including manifest, metadata, organizations, resources, and dependencies. It is designed to
 * conform to the IMS Content Packaging specification, enabling SCORM-compliant content to be
 * organized and referenced in an LMS.</p>
 *
 * <p>The IMSCP namespace is specified by {@link #NAMESPACE_URI}, and this class
 * is designed to align with the SCORM 1.2 specification.</p>
 *
 * The following schema snippet shows the structure of a "manifest" element:
 * <pre>{@code
 * <?xml version="1.0"?>
 * <xsd:schema xmlns="http://www.imsproject.org/xsd/imscp_rootv1p1p2"
 *             targetNamespace="http://www.imsproject.org/xsd/imscp_rootv1p1p2"
 *             xmlns:xml="http://www.w3.org/XML/1998/namespace"
 *             xmlns:xsd="http://www.w3.org/2001/XMLSchema"
 *             elementFormDefault="unqualified" version="IMS CP 1.1.2">
 *
 *    <xsd:import namespace="http://www.w3.org/XML/1998/namespace" schemaLocation="ims_xml.xsd"/>
 *
 *    <xsd:attributeGroup name="attr.base">
 *       <xsd:attribute ref="xml:base" use="optional"/>
 *    </xsd:attributeGroup>
 *
 *    <xsd:attributeGroup name="attr.default">
 *       <xsd:attribute name="default" type="xsd:IDREF" use="optional"/>
 *    </xsd:attributeGroup>
 *
 *    <xsd:attributeGroup name="attr.href">
 *       <xsd:attribute name="href" use="optional">
 *          <xsd:simpleType>
 *             <xsd:restriction base="xsd:anyURI">
 *                <xsd:maxLength value="2000"/>
 *             </xsd:restriction>
 *          </xsd:simpleType>
 *       </xsd:attribute>
 *    </xsd:attributeGroup>
 *
 *    <xsd:attributeGroup name="attr.href.req">
 *       <xsd:attribute name="href" use="required">
 *          <xsd:simpleType>
 *             <xsd:restriction base="xsd:anyURI">
 *                <xsd:maxLength value="2000"/>
 *             </xsd:restriction>
 *          </xsd:simpleType>
 *       </xsd:attribute>
 *    </xsd:attributeGroup>
 *
 *    <xsd:attributeGroup name="attr.identifier.req">
 *       <xsd:attribute name="identifier" type="xsd:ID" use="required"/>
 *    </xsd:attributeGroup>
 *
 *    <xsd:attributeGroup name="attr.identifier">
 *       <xsd:attribute name="identifier" type="xsd:ID" use="optional"/>
 *    </xsd:attributeGroup>
 *
 *    <xsd:attributeGroup name="attr.isvisible">
 *       <xsd:attribute name="isvisible" type="xsd:boolean" use="optional"/>
 *    </xsd:attributeGroup>
 *
 *    <xsd:attributeGroup name="attr.parameters">
 *       <xsd:attribute name="parameters" use="optional">
 *          <xsd:simpleType>
 *             <xsd:restriction base="xsd:string">
 *                <xsd:maxLength value="1000"/>
 *             </xsd:restriction>
 *          </xsd:simpleType>
 *       </xsd:attribute>
 *    </xsd:attributeGroup>
 *
 *    <xsd:attributeGroup name="attr.identifierref">
 *       <xsd:attribute name="identifierref" use="optional">
 *          <xsd:simpleType>
 *             <xsd:restriction base="xsd:string">
 *                <xsd:maxLength value="2000"/>
 *             </xsd:restriction>
 *          </xsd:simpleType>
 *       </xsd:attribute>
 *    </xsd:attributeGroup>
 *
 *    <xsd:attributeGroup name="attr.identifierref.req">
 *       <xsd:attribute name="identifierref" use="required">
 *          <xsd:simpleType>
 *             <xsd:restriction base="xsd:string">
 *                <xsd:maxLength value="2000"/>
 *             </xsd:restriction>
 *          </xsd:simpleType>
 *       </xsd:attribute>
 *    </xsd:attributeGroup>
 *
 *    <xsd:attributeGroup name="attr.resourcetype.req">
 *       <xsd:attribute name="type" use="required">
 *          <xsd:simpleType>
 *             <xsd:restriction base="xsd:string">
 *                <xsd:maxLength value="1000"/>
 *             </xsd:restriction>
 *          </xsd:simpleType>
 *       </xsd:attribute>
 *    </xsd:attributeGroup>
 *
 *    <xsd:attributeGroup name="attr.structure.req">
 *       <xsd:attribute name="structure" use="optional" default="hierarchical">
 *          <xsd:simpleType>
 *             <xsd:restriction base="xsd:string">
 *                <xsd:maxLength value="200"/>
 *             </xsd:restriction>
 *          </xsd:simpleType>
 *       </xsd:attribute>
 *    </xsd:attributeGroup>
 *
 *    <xsd:attributeGroup name="attr.version">
 *       <xsd:attribute name="version" use="optional">
 *          <xsd:simpleType>
 *             <xsd:restriction base="xsd:string">
 *                <xsd:maxLength value="20"/>
 *             </xsd:restriction>
 *          </xsd:simpleType>
 *       </xsd:attribute>
 *    </xsd:attributeGroup>
 *
 *    <xsd:annotation>
 *        <xsd:documentation>element groups</xsd:documentation>
 *    </xsd:annotation>
 *
 *    <xsd:group name="grp.any">
 *       <xsd:annotation>
 *          <xsd:documentation>Any namespaced element from any namespace may be included within an "any" element.  The namespace for the imported element must be defined in the instance, and the schema must be imported.  </xsd:documentation>
 *       </xsd:annotation>
 *       <xsd:sequence>
 *          <xsd:any namespace="##other" processContents="strict" minOccurs="0" maxOccurs="unbounded"/>
 *       </xsd:sequence>
 *    </xsd:group>
 *
 *    <xsd:element name="dependency" type="dependencyType"/>
 *    <xsd:element name="file" type="fileType"/>
 *    <xsd:element name="item" type="itemType"/>
 *    <xsd:element name="manifest" type="manifestType"/>
 *    <xsd:element name="metadata" type="metadataType"/>
 *    <xsd:element name="organization" type="organizationType"/>
 *    <xsd:element name="organizations" type="organizationsType"/>
 *    <xsd:element name="resource" type="resourceType"/>
 *    <xsd:element name="resources" type="resourcesType"/>
 *    <xsd:element name="schema" type="schemaType"/>
 *    <xsd:element name="schemaversion" type="schemaversionType"/>
 *    <xsd:element name="title" type="titleType"/>
 *
 *    <xsd:complexType name="dependencyType">
 *       <xsd:sequence>
 *          <xsd:group ref="grp.any"/>
 *       </xsd:sequence>
 *       <xsd:attributeGroup ref="attr.identifierref.req"/>
 *       <xsd:anyAttribute namespace="##other" processContents="strict"/>
 *    </xsd:complexType>
 *
 *    <xsd:complexType name="fileType">
 *       <xsd:sequence>
 *          <xsd:element ref="metadata" minOccurs="0"/>
 *          <xsd:group ref="grp.any"/>
 *       </xsd:sequence>
 *       <xsd:attributeGroup ref="attr.href.req"/>
 *       <xsd:anyAttribute namespace="##other" processContents="strict"/>
 *    </xsd:complexType>
 *
 *    <xsd:complexType name="itemType">
 *       <xsd:sequence>
 *          <xsd:element ref="title" minOccurs="0"/>
 *          <xsd:element ref="item" minOccurs="0" maxOccurs="unbounded"/>
 *          <xsd:element ref="metadata" minOccurs="0"/>
 *          <xsd:group ref="grp.any"/>
 *       </xsd:sequence>
 *       <xsd:attributeGroup ref="attr.identifier.req"/>
 *       <xsd:attributeGroup ref="attr.identifierref"/>
 *       <xsd:attributeGroup ref="attr.isvisible"/>
 *       <xsd:attributeGroup ref="attr.parameters"/>
 *       <xsd:anyAttribute namespace="##other" processContents="strict"/>
 *    </xsd:complexType>
 *
 *    <xsd:complexType name="manifestType">
 *       <xsd:sequence>
 *          <xsd:element ref="metadata" minOccurs="0"/>
 *          <xsd:element ref="organizations"/>
 *          <xsd:element ref="resources"/>
 *          <xsd:element ref="manifest" minOccurs="0" maxOccurs="unbounded"/>
 *          <xsd:group ref="grp.any"/>
 *       </xsd:sequence>
 *       <xsd:attributeGroup ref="attr.identifier.req"/>
 *       <xsd:attributeGroup ref="attr.version"/>
 *       <xsd:attribute ref="xml:base"/>
 *       <xsd:anyAttribute namespace="##other" processContents="strict"/>
 *    </xsd:complexType>
 *
 *    <xsd:complexType name="metadataType">
 *       <xsd:sequence>
 *          <xsd:element ref="schema" minOccurs="0"/>
 *          <xsd:element ref="schemaversion" minOccurs="0"/>
 *          <xsd:group ref="grp.any"/>
 *       </xsd:sequence>
 *    </xsd:complexType>
 *
 *    <xsd:complexType name="organizationsType">
 *       <xsd:sequence>
 *          <xsd:element ref="organization" minOccurs="0" maxOccurs="unbounded"/>
 *          <xsd:group ref="grp.any"/>
 *       </xsd:sequence>
 *       <xsd:attributeGroup ref="attr.default"/>
 *       <xsd:anyAttribute namespace="##other" processContents="strict"/>
 *    </xsd:complexType>
 *
 *    <xsd:complexType name="organizationType">
 *       <xsd:sequence>
 *          <xsd:element ref="title" minOccurs="0"/>
 *          <xsd:element ref="item" minOccurs="0" maxOccurs="unbounded"/>
 *          <xsd:element ref="metadata" minOccurs="0"/>
 *          <xsd:group ref="grp.any"/>
 *       </xsd:sequence>
 *       <xsd:attributeGroup ref="attr.identifier.req"/>
 *       <xsd:attributeGroup ref="attr.structure.req"/>
 *       <xsd:anyAttribute namespace="##other" processContents="strict"/>
 *    </xsd:complexType>
 *
 *    <xsd:complexType name="resourcesType">
 *       <xsd:sequence>
 *           <xsd:element ref="resource" minOccurs="0" maxOccurs="unbounded"/>
 *           <xsd:group ref="grp.any"/>
 *       </xsd:sequence>
 *       <xsd:attributeGroup ref="attr.base"/>
 *       <xsd:anyAttribute namespace="##other" processContents="strict"/>
 *    </xsd:complexType>
 *
 *    <xsd:complexType name="resourceType">
 *       <xsd:sequence>
 *          <xsd:element ref="metadata" minOccurs="0"/>
 *          <xsd:element ref="file" minOccurs="0" maxOccurs="unbounded"/>
 *          <xsd:element ref="dependency" minOccurs="0" maxOccurs="unbounded"/>
 *          <xsd:group ref="grp.any"/>
 *       </xsd:sequence>
 *       <xsd:attributeGroup ref="attr.identifier.req"/>
 *       <xsd:attributeGroup ref="attr.resourcetype.req"/>
 *       <xsd:attributeGroup ref="attr.base"/>
 *       <xsd:attributeGroup ref="attr.href"/>
 *       <xsd:anyAttribute namespace="##other" processContents="strict"/>
 *    </xsd:complexType>
 *
 *    <xsd:simpleType name="schemaType">
 *       <xsd:restriction base="xsd:string">
 *          <xsd:maxLength value="100"/>
 *       </xsd:restriction>
 *    </xsd:simpleType>
 *
 *    <xsd:simpleType name="schemaversionType">
 *       <xsd:restriction base="xsd:string">
 *          <xsd:maxLength value="20"/>
 *       </xsd:restriction>
 *    </xsd:simpleType>
 *
 *    <xsd:simpleType name="titleType">
 *       <xsd:restriction base="xsd:string">
 *          <xsd:maxLength value="200"/>
 *       </xsd:restriction>
 *    </xsd:simpleType>
 *
 * </xsd:schema>
 * }</pre>
 */
@JacksonXmlRootElement(localName = "manifest", namespace = Scorm12Manifest.NAMESPACE_URI)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonFormat(with = JsonFormat.Feature.ACCEPT_CASE_INSENSITIVE_PROPERTIES)
public final class Scorm12Manifest implements PackageManifest {

  /**
   * The namespace URI for the SCORM 1.2 manifest, as defined in the IMS Content Packaging
   * specification.
   */
  public static final String NAMESPACE_URI = "http://www.imsproject.org/xsd/imscp_rootv1p1p2";
  /**
   * The unique identifier for the manifest. This attribute is used to uniquely identify the content
   * package within an LMS.
   */
  @JacksonXmlProperty(isAttribute = true)
  @JsonProperty(value = "identifier", required = true)
  private String identifier;
  /**
   * The version of the manifest. Specifies the version of the content package, which may be used by
   * the LMS to manage content versions.
   */
  @JacksonXmlProperty(isAttribute = true)
  @JsonProperty(value = "version")
  private String version;
  /**
   * The base URL for all resources in the content package. This URL is used to resolve relative
   * paths for resources.
   */
  @JacksonXmlProperty(isAttribute = true, localName = "base", namespace = "http://www.w3.org/XML/1998/namespace")
  @JsonProperty("base")
  private String base;
  /**
   * Metadata associated with the manifest, typically including schema and version information,
   * which provide context for the content package.
   */
  @JacksonXmlProperty(localName = "metadata", namespace = NAMESPACE_URI)
  private Scorm12Metadata metadata;
  /**
   * Contains the set of organizations that represent the structure of the content. Each
   * organization defines a hierarchical structure of learning resources.
   */
  @JacksonXmlProperty(localName = "organizations", namespace = NAMESPACE_URI)
  private Scorm12Organizations organizations;
  /**
   * Contains the list of resources within the content package, each representing a learning object
   * or asset to be delivered within the LMS.
   */
  @JacksonXmlProperty(localName = "resources", namespace = NAMESPACE_URI)
  private Scorm12Resources resources;

  /**
   * Default constructor for the Scorm12Manifest class. This constructor initializes an instance of
   * the Scorm12Manifest class without performing any operations. Designed primarily for scenarios
   * where explicit initialization of fields is not required.
   */
  public Scorm12Manifest() {
    // no-op
  }

  /**
   * Retrieves the title of the SCORM 1.2 manifest. The title is determined based on the following
   * logic: 1. If the default organization's title is available and non-empty, it is returned. 2.
   * Otherwise, the title from the metadata (LOM structure) is returned if present. 3. If neither is
   * available, the method returns null.
   *
   * @return The title of the SCORM 1.2 manifest, or null if no title is found.
   */
  @Override
  @JsonIgnore
  public String getTitle() {
    //noinspection DuplicatedCode
    String organizationTitle = Optional
        .ofNullable(organizations)
        .map(Scorm12Organizations::getDefault)
        .map(Scorm12Organization::getTitle)
        .orElse(null);
    if (organizationTitle != null && !organizationTitle.isEmpty()) {
      return organizationTitle;
    }
    return Optional
        .ofNullable(metadata)
        .map(Scorm12Metadata::getLom)
        .map(LOM::getTitle)
        .orElse(null);
  }

  /**
   * Retrieves the description from the SCORM 1.2 metadata LOM structure if available. If the
   * metadata or the LOM description is not present, returns null.
   *
   * @return The description text from the SCORM 1.2 metadata, or null if no description is found.
   */
  @Override
  @JsonIgnore
  public String getDescription() {
    return Optional
        .ofNullable(metadata)
        .map(Scorm12Metadata::getLom)
        .map(LOM::getDescription)
        .orElse(null);
  }

  /**
   * Retrieves the launch URL of the SCORM 1.2 resource. The method attempts to identify the first
   * non-empty launchable resource by finding items with a non-null `identifierRef` in the
   * organization hierarchy, then resolving it to a resource URL using the resource collection.
   *
   * @return The launch URL as a string if a valid resource is found, or null if no launchable
   * resource exists.
   */
  @Override
  @JsonIgnore
  public String getLaunchUrl() {
    // Find all items with non-null identifierRef at any level
    //noinspection DuplicatedCode
    List<String> resourceIds = Optional
        .ofNullable(organizations)
        .map(Scorm12Organizations::getDefault)
        .map(Scorm12Organization::getItems)
        .map(this::findAllItemsWithIdentifierRef)
        .orElse(List.of());

    // Find the first resource that exists
    for (String resourceId : resourceIds) {
      String href = Optional
          .ofNullable(resources)
          .flatMap(r -> r.getResourceById(resourceId))
          .map(Scorm12Resource::getHref)
          .orElse(null);

      if (href != null && !href.isEmpty()) {
        return href;
      }
    }

    return null;
  }

  /**
   * Retrieves the duration of the SCORM 1.2 resource. The method resolves the duration by
   * navigating through the metadata hierarchy. If the metadata, LOM, technical details, or duration
   * fields are not present, it defaults to {@link Duration#ZERO}.
   *
   * @return A {@link Duration} object representing the duration of the resource, or
   * {@link Duration#ZERO} if not available.
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
        .map(Scorm12Metadata::getLom)
        .map(lom -> lom
            .getTechnical()
            .getDuration()
            .getDuration())
        .orElse(Duration.ZERO);
  }

  /**
   * Retrieves the identifier of the SCORM 1.2 manifest. The identifier is a unique string that
   * represents this specific manifest.
   *
   * @return the identifier of the SCORM 1.2 manifest
   */
  @Override
  public String getIdentifier() {
    return this.identifier;
  }

  /**
   * Sets the identifier for the SCORM 1.2 manifest. The identifier is a unique string used to
   * represent this specific manifest.
   *
   * @param identifier the unique identifier for the SCORM 1.2 manifest
   */
  public void setIdentifier(String identifier) {
    this.identifier = identifier;
  }

  /**
   * Retrieves the version of the SCORM 1.2 manifest. The version is a string representing the
   * specific version information associated with this manifest.
   *
   * @return the version of the SCORM 1.2 manifest
   */
  @Override
  public String getVersion() {
    return this.version;
  }

  /**
   * Sets the version of the SCORM 1.2 manifest. The version represents a string that indicates the
   * specific version of this manifest.
   *
   * @param version the version string to set for the SCORM 1.2 manifest
   */
  public void setVersion(String version) {
    this.version = version;
  }

  /**
   * Retrieves the base URL or path specified in the SCORM 1.2 manifest. The base value is typically
   * used for resolving relative paths within the manifest.
   *
   * @return the base value of the SCORM 1.2 manifest, or null if not set
   */
  public String getBase() {
    return this.base;
  }

  /**
   * Sets the base URL or path for the SCORM 1.2 manifest. The base value is used to resolve
   * relative paths within the manifest.
   *
   * @param base the base URL or path to set for the SCORM 1.2 manifest
   */
  public void setBase(String base) {
    this.base = base;
  }

  /**
   * Retrieves the metadata of the SCORM 1.2 manifest. The metadata contains information such as
   * title, description, and other relevant details defined within the SCORM 1.2 specification.
   *
   * @return The {@code Scorm12Metadata} object representing the metadata of the SCORM 1.2 manifest.
   */
  public Scorm12Metadata getMetadata() {
    return this.metadata;
  }

  /**
   * Sets the metadata for the SCORM 1.2 manifest. The metadata includes information such as title,
   * description, and other relevant details defined within the SCORM 1.2 specification.
   *
   * @param metadata the {@link Scorm12Metadata} object representing the metadata to be assigned to
   * the SCORM 1.2 manifest
   */
  public void setMetadata(Scorm12Metadata metadata) {
    this.metadata = metadata;
  }

  /**
   * Retrieves the organizations defined within the SCORM 1.2 manifest. The organizations represent
   * the hierarchical structure of the learning resources, such as courses, lessons, or modules, as
   * specified in the SCORM 1.2 manifest.
   *
   * @return A {@code Scorm12Organizations} object representing the organizations within the SCORM
   * 1.2 manifest.
   */
  public Scorm12Organizations getOrganizations() {
    return this.organizations;
  }

  /**
   * Sets the organizations for the SCORM 1.2 manifest. The organizations define the hierarchical
   * structure of learning resources, which may include courses, lessons, or modules as specified in
   * the SCORM 1.2 manifest.
   *
   * @param organizations the {@link Scorm12Organizations} object representing the organizational
   * hierarchy to be assigned to the SCORM 1.2 manifest
   */
  public void setOrganizations(Scorm12Organizations organizations) {
    this.organizations = organizations;
  }

  /**
   * Retrieves the resources defined in the SCORM 1.2 manifest. The resources typically consist of
   * the content and assets associated with the SCORM package, such as files, dependencies, or other
   * resources required for the learning experience.
   *
   * @return A {@code Scorm12Resources} object representing the resources within the SCORM 1.2
   * manifest.
   */
  public Scorm12Resources getResources() {
    return this.resources;
  }

  /**
   * Sets the resources for the SCORM 1.2 manifest. The resources represent the content and
   * associated assets required for the SCORM package, such as files, dependencies, or other
   * structures.
   *
   * @param resources the {@link Scorm12Resources} object representing the resources to be assigned
   * to the SCORM 1.2 manifest
   */
  public void setResources(Scorm12Resources resources) {
    this.resources = resources;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (!(o instanceof Scorm12Manifest that)) {
      return false;
    }

    return new EqualsBuilder()
        .append(identifier, that.identifier)
        .append(version, that.version)
        .append(getBase(), that.getBase())
        .append(getMetadata(), that.getMetadata())
        .append(getOrganizations(), that.getOrganizations())
        .append(getResources(), that.getResources())
        .isEquals();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder(17, 37)
        .append(identifier)
        .append(version)
        .append(getBase())
        .append(getMetadata())
        .append(getOrganizations())
        .append(getResources())
        .toHashCode();
  }

  /**
   * Recursively searches for all items with a non-null identifierRef.
   *
   * @param items The list of items to search.
   * @return A list of identifierRefs from all items with a non-null identifierRef, in the order
   * they were found.
   */
  private List<String> findAllItemsWithIdentifierRef(List<Scorm12Item> items) {
    if (items == null || items.isEmpty()) {
      return List.of();
    }

    List<String> result = new java.util.ArrayList<>();

    // First, check if any top-level items have an identifierRef
    for (Scorm12Item item : items) {
      if (item.getIdentifierRef() != null && !item
          .getIdentifierRef()
          .isEmpty()) {
        result.add(item.getIdentifierRef());
      }
    }

    // Then, recursively check child items
    for (Scorm12Item item : items) {
      if (item.getItems() != null && !item
          .getItems()
          .isEmpty()) {
        result.addAll(findAllItemsWithIdentifierRef(item.getItems()));
      }
    }

    return result;
  }
}
