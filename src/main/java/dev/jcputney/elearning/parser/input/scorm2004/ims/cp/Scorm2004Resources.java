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
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import dev.jcputney.elearning.parser.input.scorm2004.Scorm2004Manifest;
import java.io.Serializable;
import java.util.List;
import java.util.Optional;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * Represents the collection of resources within the content package. Each resource defines a
 * learning object or asset.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonFormat(with = JsonFormat.Feature.ACCEPT_CASE_INSENSITIVE_PROPERTIES)
public class Scorm2004Resources implements Serializable {

  /**
   * The base URL for all resources in the content package. This URL is used to resolve relative
   * paths for resources.
   */
  @JacksonXmlProperty(isAttribute = true, localName = "base", namespace = "http://www.w3.org/XML/1998/namespace")
  @JsonProperty("base")
  private String base;
  /**
   * A list of resources in the content package, each representing a specific learning object or
   * asset.
   */
  @JacksonXmlElementWrapper(useWrapping = false)
  @JacksonXmlProperty(localName = "resource", namespace = Scorm2004Manifest.NAMESPACE_URI)
  private List<Scorm2004Resource> resourceList;

  public Scorm2004Resources() {
  }

  public String getBase() {
    return this.base;
  }

  public void setBase(String base) {
    this.base = base;
  }

  public List<Scorm2004Resource> getResourceList() {
    return this.resourceList;
  }

  public void setResourceList(List<Scorm2004Resource> resourceList) {
    this.resourceList = resourceList;
  }

  /**
   * Retrieves a resource by its identifier.
   *
   * @param id The identifier of the resource to retrieve.
   * @return An Optional containing the resource if found, or an empty Optional if not found.
   */
  @JsonIgnore
  public Optional<Scorm2004Resource> getResourceById(String id) {
    if (id == null || resourceList == null) {
      return Optional.empty();
    }
    return resourceList
        .stream()
        .filter(r -> id.equals(r.getIdentifier()))
        .findFirst();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (!(o instanceof Scorm2004Resources that)) {
      return false;
    }

    return new EqualsBuilder()
        .append(getBase(), that.getBase())
        .append(getResourceList(), that.getResourceList())
        .isEquals();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder(17, 37)
        .append(getBase())
        .append(getResourceList())
        .toHashCode();
  }
}
