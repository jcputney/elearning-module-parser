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
public class Scorm12Manifest implements PackageManifest {

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

  public Scorm12Manifest() {
  }

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

  @Override
  @JsonIgnore
  public String getDescription() {
    return Optional
        .ofNullable(metadata)
        .map(Scorm12Metadata::getLom)
        .map(LOM::getDescription)
        .orElse(null);
  }

  @Override
  @JsonIgnore
  public String getLaunchUrl() {
    // Find all items with non-null identifierRef at any level
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

  @Override
  public String getIdentifier() {
    return this.identifier;
  }

  public void setIdentifier(String identifier) {
    this.identifier = identifier;
  }

  @Override
  public String getVersion() {
    return this.version;
  }

  public void setVersion(String version) {
    this.version = version;
  }

  public String getBase() {
    return this.base;
  }

  public void setBase(String base) {
    this.base = base;
  }

  public Scorm12Metadata getMetadata() {
    return this.metadata;
  }

  public void setMetadata(Scorm12Metadata metadata) {
    this.metadata = metadata;
  }

  public Scorm12Organizations getOrganizations() {
    return this.organizations;
  }

  public void setOrganizations(Scorm12Organizations organizations) {
    this.organizations = organizations;
  }

  public Scorm12Resources getResources() {
    return this.resources;
  }

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
