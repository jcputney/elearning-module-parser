package dev.jcputney.elearning.parser.input.scorm12;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import dev.jcputney.elearning.parser.input.PackageManifest;
import dev.jcputney.elearning.parser.input.lom.General;
import dev.jcputney.elearning.parser.input.lom.LOM;
import dev.jcputney.elearning.parser.input.lom.types.UnboundLangString;
import dev.jcputney.elearning.parser.input.scorm12.ims.cp.Scorm12Metadata;
import dev.jcputney.elearning.parser.input.scorm12.ims.cp.Scorm12Organization;
import dev.jcputney.elearning.parser.input.scorm12.ims.cp.Scorm12Organizations;
import dev.jcputney.elearning.parser.input.scorm12.ims.cp.Scorm12Resources;
import java.util.Optional;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

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
@Data
@JacksonXmlRootElement(localName = "manifest", namespace = Scorm12Manifest.NAMESPACE_URI)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Scorm12Manifest implements PackageManifest {

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
  private String version;

  /**
   * The base URL for all resources in the content package. This URL is used to resolve relative
   * paths for resources.
   */
  @JacksonXmlProperty(isAttribute = true, localName = "base", namespace = "http://www.w3.org/XML/1998/namespace")
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

  @Override
  public String getTitle() {
    String organizationTitle = Optional.ofNullable(organizations)
        .map(Scorm12Organizations::getDefaultOrganization)
        .map(Scorm12Organization::getTitle)
        .orElse(null);
    if (StringUtils.isNotBlank(organizationTitle)) {
      return organizationTitle;
    }
    String metadataTitle = Optional.ofNullable(metadata)
        .map(Scorm12Metadata::getLom)
        .map(LOM::getGeneral)
        .map(General::getTitle)
        .map(UnboundLangString::getLangStrings)
        .filter(titles -> !titles.isEmpty())
        .map(titles -> titles.get(0).getValue())
        .orElse(null);
    if (StringUtils.isNotEmpty(metadataTitle)) {
      return metadataTitle;
    }
    return "";
  }

  @Override
  public String getDescription() {
    return "";
  }

  @Override
  public String getLaunchUrl() {
    return "";
  }
}