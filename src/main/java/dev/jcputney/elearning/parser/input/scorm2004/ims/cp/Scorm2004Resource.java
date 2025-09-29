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

package dev.jcputney.elearning.parser.input.scorm2004.ims.cp;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import dev.jcputney.elearning.parser.input.scorm2004.ADLCP;
import dev.jcputney.elearning.parser.input.scorm2004.Scorm2004Manifest;
import dev.jcputney.elearning.parser.input.scorm2004.adl.types.ScormType;
import java.io.Serializable;
import java.util.List;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * Represents a single resource within the content package, typically corresponding to a physical
 * file or collection of files that can be delivered within an LMS.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonFormat(with = JsonFormat.Feature.ACCEPT_CASE_INSENSITIVE_PROPERTIES)
public class Scorm2004Resource implements Serializable {

  /**
   * The unique identifier for this resource, which allows it to be referenced by items.
   */
  @JacksonXmlProperty(isAttribute = true)
  @JsonProperty("identifier")
  private String identifier;

  /**
   * Specifies the type of resource, such as "webcontent".
   */
  @JacksonXmlProperty(isAttribute = true)
  @JsonProperty("type")
  private String type = "webcontent";

  /**
   * The URL or path to the main entry point file for this resource.
   */
  @JacksonXmlProperty(isAttribute = true)
  @JsonProperty("href")
  private String href;

  /**
   * The base URL for all resources in the content package. This URL is used to resolve relative
   * paths for resources.
   */
  @JacksonXmlProperty(isAttribute = true, localName = "base", namespace = "http://www.w3.org/XML/1998/namespace")
  @JsonProperty("base")
  private String base;

  /**
   * Specifies the SCORM type (e.g., "sco" or "asset") to define if this resource is trackable or a
   * static asset.
   */
  @JacksonXmlProperty(namespace = ADLCP.NAMESPACE_URI, isAttribute = true)
  @JsonProperty("scormtype")
  private ScormType scormType;

  /**
   * Metadata associated with this resource, providing details such as author, creation date, and
   * other descriptive information relevant to the resource.
   */
  @JacksonXmlProperty(localName = "metadata", namespace = Scorm2004Manifest.NAMESPACE_URI)
  private Scorm2004SubMetadata metadata;

  /**
   * A list of files associated with this resource, representing the physical files that are part of
   * the learning object.
   */
  @JacksonXmlElementWrapper(localName = "file", useWrapping = false)
  @JacksonXmlProperty(localName = "file", namespace = Scorm2004Manifest.NAMESPACE_URI)
  private List<Scorm2004File> files;

  /**
   * A list of dependencies that this resource requires. Dependencies define other resources that
   * must be available for this resource to function correctly.
   */
  @JacksonXmlElementWrapper(localName = "dependency", useWrapping = false)
  @JacksonXmlProperty(localName = "dependency", namespace = Scorm2004Manifest.NAMESPACE_URI)
  private List<Scorm2004Dependency> dependencies;

  public Scorm2004Resource() {
    // no-op
  }

  /**
   * Retrieves the identifier of the SCORM 2004 resource.
   *
   * @return the identifier of the resource as a {@code String}
   */
  public String getIdentifier() {
    return this.identifier;
  }

  /**
   * Sets the identifier of the SCORM 2004 resource.
   *
   * @param identifier the identifier for the resource as a String
   */
  public void setIdentifier(String identifier) {
    this.identifier = identifier;
  }

  /**
   * Retrieves the type of the SCORM 2004 resource.
   *
   * @return the type of the resource as a {@code String}
   */
  public String getType() {
    return this.type;
  }

  /**
   * Sets the type of the SCORM 2004 resource.
   *
   * @param type the type of the resource as a String
   */
  public void setType(String type) {
    this.type = type;
  }

  /**
   * Retrieves the href value of the SCORM 2004 resource.
   *
   * @return the href representing the location of the resource as a String
   */
  public String getHref() {
    return this.href;
  }

  /**
   * Sets the href value of the SCORM 2004 resource. The href represents the location of the
   * resource.
   *
   * @param href the href representing the location of the resource as a String
   */
  public void setHref(String href) {
    this.href = href;
  }

  /**
   * Retrieves the base value of the SCORM 2004 resource.
   *
   * @return the base value as a String
   */
  public String getBase() {
    return this.base;
  }

  /**
   * Sets the base value of the SCORM 2004 resource.
   *
   * @param base the base value as a String
   */
  public void setBase(String base) {
    this.base = base;
  }

  /**
   * Retrieves the SCORM type of the resource.
   *
   * @return the SCORM type of the resource as an instance of {@code ScormType}
   */
  public ScormType getScormType() {
    return this.scormType;
  }

  /**
   * Sets the SCORM type of the resource. The SCORM type can represent various types of resources
   * defined in the SCORM ADLCP schema, such as SCOs (Shareable Content Objects) or assets.
   *
   * @param scormType an instance of {@code ScormType} representing the SCORM type of the resource
   * (e.g., SCO, asset, or unknown)
   */
  public void setScormType(ScormType scormType) {
    this.scormType = scormType;
  }

  /**
   * Retrieves the metadata associated with the SCORM 2004 resource.
   *
   * @return an instance of {@code Scorm2004SubMetadata} representing the metadata, which may
   * include inline LOM metadata or an external reference.
   */
  public Scorm2004SubMetadata getMetadata() {
    return this.metadata;
  }

  /**
   * Sets the metadata associated with the SCORM 2004 resource.
   *
   * @param metadata an instance of {@code Scorm2004SubMetadata} representing the metadata, which
   * may include inline LOM metadata or an external reference
   */
  public void setMetadata(Scorm2004SubMetadata metadata) {
    this.metadata = metadata;
  }

  /**
   * Retrieves the list of SCORM 2004 files associated with this resource. Each file represents a
   * physical file defined in the content package.
   *
   * @return a list of {@code Scorm2004File} objects representing the files associated with this
   * resource
   */
  public List<Scorm2004File> getFiles() {
    return this.files;
  }

  /**
   * Sets the list of SCORM 2004 files associated with this resource. Each file represents a
   * physical file defined in the content package.
   *
   * @param files a list of {@code Scorm2004File} objects representing the files associated with
   * this resource
   */
  public void setFiles(List<Scorm2004File> files) {
    this.files = files;
  }

  /**
   * Retrieves the list of dependencies associated with the SCORM 2004 resource. Each dependency
   * represents a relationship to another resource that this resource relies on within the content
   * package.
   *
   * @return a list of {@code Scorm2004Dependency} objects representing the dependencies associated
   * with this resource.
   */
  public List<Scorm2004Dependency> getDependencies() {
    return this.dependencies;
  }

  /**
   * Sets the list of dependencies associated with the SCORM 2004 resource. Each dependency
   * represents a relationship to another resource within the content package that this resource
   * relies upon.
   *
   * @param dependencies a list of {@code Scorm2004Dependency} objects representing the dependencies
   * associated with this resource
   */
  public void setDependencies(List<Scorm2004Dependency> dependencies) {
    this.dependencies = dependencies;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (!(o instanceof Scorm2004Resource that)) {
      return false;
    }

    return new EqualsBuilder()
        .append(getIdentifier(), that.getIdentifier())
        .append(getType(), that.getType())
        .append(getHref(), that.getHref())
        .append(getBase(), that.getBase())
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
        .append(getType())
        .append(getHref())
        .append(getBase())
        .append(getScormType())
        .append(getMetadata())
        .append(getFiles())
        .append(getDependencies())
        .toHashCode();
  }
}
