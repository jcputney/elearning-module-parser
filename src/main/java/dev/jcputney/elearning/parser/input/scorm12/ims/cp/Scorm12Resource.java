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

package dev.jcputney.elearning.parser.input.scorm12.ims.cp;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import dev.jcputney.elearning.parser.input.scorm12.Scorm12ADLCP;
import dev.jcputney.elearning.parser.input.scorm12.Scorm12Manifest;
import dev.jcputney.elearning.parser.input.scorm2004.adl.types.ScormType;
import java.io.Serializable;
import java.util.List;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * Represents the {@code <resource>} element in SCORM 1.2 manifest file.
 * <p>
 * The {@code <resource>} element defines a single resource in the content package, specifying its
 * type, dependencies, and the files associated with it.
 * </p>
 *
 * <pre>{@code
 * <xsd:complexType name="resourceType">
 *   <xsd:sequence>
 *     <xsd:element ref="metadata" minOccurs="0"/>
 *     <xsd:element ref="file" minOccurs="0" maxOccurs="unbounded"/>
 *     <xsd:element ref="dependency" minOccurs="0" maxOccurs="unbounded"/>
 *   </xsd:sequence>
 *   <xsd:attributeGroup ref="attr.resource.req"/>
 *   <xsd:attribute ref="xml:base"/>
 *   <xsd:anyAttribute namespace="##other" processContents="strict"/>
 * </xsd:complexType>
 * }</pre>
 */
@JsonFormat(with = JsonFormat.Feature.ACCEPT_CASE_INSENSITIVE_PROPERTIES)
public class Scorm12Resource implements Serializable {

  /**
   * The unique identifier for this resource, which allows it to be referenced by items.
   */
  @JacksonXmlProperty(isAttribute = true)
  @JsonProperty(value = "identifier", required = true)
  private String identifier;

  /**
   * The base URL for this resource. This URL is used to resolve relative paths for resources.
   */
  @JacksonXmlProperty(isAttribute = true, localName = "base", namespace = "http://www.w3.org/XML/1998/namespace")
  @JsonProperty("base")
  private String base;

  /**
   * The URL or path to the main entry point file for this resource.
   */
  @JacksonXmlProperty(isAttribute = true)
  @JsonProperty(value = "href")
  private String href;

  /**
   * Specifies the type of resource, such as "webcontent".
   */
  @JacksonXmlProperty(isAttribute = true)
  @JsonProperty(value = "type", required = true)
  private String type;

  /**
   * The SCORM type of the resource.
   * <p>
   * Specifies the SCORM-specific type of the resource (e.g., "sco" or "asset").
   * </p>
   */
  @JacksonXmlProperty(isAttribute = true, namespace = Scorm12ADLCP.NAMESPACE_URI, localName = "scormType")
  @JsonProperty("scormType")
  private ScormType scormType;

  /**
   * The unique identifier for this resource, used to distinguish it from other resources within the
   * same manifest.
   */
  @JacksonXmlProperty(localName = "metadata", namespace = Scorm12Manifest.NAMESPACE_URI)
  private Scorm12Metadata metadata;

  /**
   * A list of files associated with this resource, representing the physical files that are part of
   * the learning object.
   */
  @JacksonXmlElementWrapper(localName = "file", useWrapping = false)
  @JacksonXmlProperty(localName = "file", namespace = Scorm12Manifest.NAMESPACE_URI)
  private List<Scorm12File> files;

  /**
   * A list of dependencies that this resource requires. Dependencies define other resources that
   * must be available for this resource to function correctly.
   */
  @JacksonXmlElementWrapper(localName = "dependency", useWrapping = false)
  @JacksonXmlProperty(localName = "dependency", namespace = Scorm12Manifest.NAMESPACE_URI)
  private List<Scorm12Dependency> dependencies;

  public Scorm12Resource() {
    // no-op
  }

  public String getIdentifier() {
    return this.identifier;
  }

  public void setIdentifier(String identifier) {
    this.identifier = identifier;
  }

  public String getBase() {
    return this.base;
  }

  public void setBase(String base) {
    this.base = base;
  }

  public String getHref() {
    return this.href;
  }

  public void setHref(String href) {
    this.href = href;
  }

  public String getType() {
    return this.type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public ScormType getScormType() {
    return this.scormType;
  }

  public void setScormType(ScormType scormType) {
    this.scormType = scormType;
  }

  public Scorm12Metadata getMetadata() {
    return this.metadata;
  }

  public void setMetadata(Scorm12Metadata metadata) {
    this.metadata = metadata;
  }

  public List<Scorm12File> getFiles() {
    return this.files;
  }

  public void setFiles(List<Scorm12File> files) {
    this.files = files;
  }

  public List<Scorm12Dependency> getDependencies() {
    return this.dependencies;
  }

  public void setDependencies(
      List<Scorm12Dependency> dependencies) {
    this.dependencies = dependencies;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (!(o instanceof Scorm12Resource that)) {
      return false;
    }

    return new EqualsBuilder()
        .append(getIdentifier(), that.getIdentifier())
        .append(getBase(), that.getBase())
        .append(getHref(), that.getHref())
        .append(getType(), that.getType())
        .append(getScormType(), that.getScormType())
        .append(getMetadata(), that.getMetadata())
        .append(getFiles(), that.getFiles())
        .append(getDependencies(), that.getDependencies())
        .isEquals();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder(17, 37)
        .append(getIdentifier())
        .append(getBase())
        .append(getHref())
        .append(getType())
        .append(getScormType())
        .append(getMetadata())
        .append(getFiles())
        .append(getDependencies())
        .toHashCode();
  }
}
