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
  }

  public String getIdentifier() {
    return this.identifier;
  }

  public void setIdentifier(String identifier) {
    this.identifier = identifier;
  }

  public String getType() {
    return this.type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getHref() {
    return this.href;
  }

  public void setHref(String href) {
    this.href = href;
  }

  public String getBase() {
    return this.base;
  }

  public void setBase(String base) {
    this.base = base;
  }

  public ScormType getScormType() {
    return this.scormType;
  }

  public void setScormType(ScormType scormType) {
    this.scormType = scormType;
  }

  public Scorm2004SubMetadata getMetadata() {
    return this.metadata;
  }

  public void setMetadata(Scorm2004SubMetadata metadata) {
    this.metadata = metadata;
  }

  public List<Scorm2004File> getFiles() {
    return this.files;
  }

  public void setFiles(List<Scorm2004File> files) {
    this.files = files;
  }

  public List<Scorm2004Dependency> getDependencies() {
    return this.dependencies;
  }

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
