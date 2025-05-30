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

import static lombok.AccessLevel.PRIVATE;

import com.fasterxml.jackson.annotation.JsonFormat;
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
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.jackson.Jacksonized;

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
 *
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
@Builder
@Getter
@Jacksonized
@NoArgsConstructor
@EqualsAndHashCode(doNotUseGetters = true)
@AllArgsConstructor(access = PRIVATE)
@JacksonXmlRootElement(localName = "manifest", namespace = Scorm2004Manifest.NAMESPACE_URI)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonFormat(with = JsonFormat.Feature.ACCEPT_CASE_INSENSITIVE_PROPERTIES)
public class Scorm2004Manifest implements PackageManifest {

  /**
   * The XML namespace URI for SCORM IMS Content Packaging (imscp_v1p1).
   */
  public static final String NAMESPACE_URI = "http://www.imsglobal.org/xsd/imscp_v1p1";
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

  /**
   * Returns the title of the content package, which is typically the name or title of the course.
   *
   * @return the title of the content package
   */
  @Override
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
  public String getLaunchUrl() {
    // get relative URL from first resource
    return Optional
        .ofNullable(resources)
        .map(Scorm2004Resources::getResourceList)
        .filter(resourceList -> !resourceList.isEmpty())
        .map(resourceList -> resourceList.get(0))
        .map(Scorm2004Resource::getHref)
        .orElse(null);
  }

  /**
   * Returns the launch URL for a specific item within the content package, which is typically the
   * URL of the first resource associated with the item.
   *
   * @param itemId the unique identifier of the item
   * @return an Optional containing the launch URL for the item, or empty if no URL is available
   */
  public Optional<String> getLaunchUrlForItem(String itemId) {
    // search all organizations for an item with itemId
    Optional<Scorm2004Item> resourceItemOpt = Optional
        .ofNullable(organizations)
        .map(orgs -> orgs.getItemById(itemId));

    if (resourceItemOpt.isEmpty()) {
      return Optional.empty();
    }

    Scorm2004Item resourceItem = resourceItemOpt.get();

    // get relative URL from item
    Optional<String> hrefOpt = Optional
        .ofNullable(resources)
        .map(Scorm2004Resources::getResourceList)
        .filter(resourceList -> !resourceList.isEmpty())
        .map(resourceList -> resourceList.get(0))
        .map(Scorm2004Resource::getHref)
        .filter(href -> !href.isEmpty());

    if (hrefOpt.isEmpty()) {
      return Optional.empty();
    }

    String href = hrefOpt.get();
    String parameters = resourceItem.getParameters();

    if (parameters != null && !parameters.isEmpty()) {
      return Optional.of(parameters.startsWith("?") ? href + parameters : href + "?" + parameters);
    }

    return Optional.of(href);
  }

  @Override
  public Duration getDuration() {
    return Optional
        .ofNullable(metadata)
        .filter(
            m -> m.getLom() != null && m
                .getLom()
                .getTechnical() != null
                && m
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
   *
   * @return A set of SCO IDs, or an empty set if none are found
   */
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
   * @return An Optional containing the ActivityTree, or empty if no default organization is found
   */
  public Optional<ActivityTree> buildActivityTree() {
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
    if (sequencingCollection != null) {
      return true;
    }

    return organizations != null && organizations
        .getOrganizationList()
        .stream()
        .anyMatch(
            org -> org.getSequencing() != null || org
                .getItems()
                .stream()
                .anyMatch(this::hasSequencing));
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
   * Recursively checks if an item or any of its children has sequencing information.
   *
   * @param item The item to check
   * @return true if the item or any of its children has sequencing, false otherwise
   */
  private boolean hasSequencing(Scorm2004Item item) {
    if (item.getSequencing() != null) {
      return true;
    }

    if (item.getItems() != null) {
      for (Scorm2004Item child : item.getItems()) {
        if (hasSequencing(child)) {
          return true;
        }
      }
    }

    return false;
  }
}
