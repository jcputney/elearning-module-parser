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
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import dev.jcputney.elearning.parser.input.scorm12.Scorm12Manifest;
import java.io.Serializable;
import java.util.List;
import java.util.Optional;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * Represents the {@code <resources>} element in a SCORM 1.2 manifest file.
 * <p>
 * The {@code <resources>} element is a container for all {@code <resource>} elements in the
 * manifest, defining the resources used by the content package.
 * </p>
 *
 * <pre>{@code
 * <xsd:complexType name="resourcesType">
 *    <xsd:sequence>
 *        <xsd:element ref="resource" minOccurs="0" maxOccurs="unbounded"/>
 *        <xsd:group ref="grp.any"/>
 *    </xsd:sequence>
 *    <xsd:attributeGroup ref="attr.base"/>
 *    <xsd:anyAttribute namespace="##other" processContents="strict"/>
 * </xsd:complexType>
 * }</pre>
 */
@JsonFormat(with = JsonFormat.Feature.ACCEPT_CASE_INSENSITIVE_PROPERTIES)
public final class Scorm12Resources implements Serializable {

  /**
   * The base URL for all resources in the content package. This URL is used to resolve relative
   * paths for resources.
   */
  @JacksonXmlProperty(isAttribute = true, localName = "base", namespace = "http://www.w3.org/XML/1998/namespace")
  @JsonProperty("base")
  private String base;

  /**
   * A list of all {@code <resources>} elements defined within the {@code <resources>} element.
   * <p>
   * Each {@code <resources>} specifies a particular resource (e.g., SCO, asset, or other content)
   * in the package.
   * </p>
   */
  @JacksonXmlElementWrapper(useWrapping = false)
  @JacksonXmlProperty(localName = "resource", namespace = Scorm12Manifest.NAMESPACE_URI)
  private List<Scorm12Resource> resourceList;

  public Scorm12Resources() {
    // no-op
  }

  /**
   * Retrieves a resource by its identifier.
   *
   * @param id The identifier of the resource to retrieve.
   * @return An {@link Optional} containing the resource if found, or an empty Optional if not
   * found.
   */
  @JsonIgnore
  public Optional<Scorm12Resource> getResourceById(String id) {
    if (id == null || resourceList == null) {
      return Optional.empty();
    }
    return resourceList
        .stream()
        .filter(resource -> id.equals(resource.getIdentifier()))
        .findFirst();
  }

  /**
   * Retrieves the base URL associated with the resources in the content package. This URL is used
   * to resolve relative paths for the resources.
   *
   * @return The base URL as a {@code String}.
   */
  public String getBase() {
    return this.base;
  }

  /**
   * Sets the base URL associated with the resources in the SCORM content package.
   *
   * @param base A string representing the base URL. This URL is used to resolve relative paths for
   * the resources in the content package.
   */
  public void setBase(String base) {
    this.base = base;
  }

  /**
   * Retrieves the list of SCORM 1.2 resources.
   *
   * @return A list of {@link Scorm12Resource} objects representing the resources.
   */
  public List<Scorm12Resource> getResourceList() {
    return this.resourceList;
  }

  /**
   * Sets the list of SCORM 1.2 resources for this content package.
   *
   * @param resourceList A list of {@link Scorm12Resource} objects to set. These objects represent
   * the resources associated with the content package.
   */
  public void setResourceList(List<Scorm12Resource> resourceList) {
    this.resourceList = resourceList;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (!(o instanceof Scorm12Resources that)) {
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
