/*
 * Copyright (c) 2024. Jonathan Putney
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

import static lombok.AccessLevel.PRIVATE;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import dev.jcputney.elearning.parser.input.scorm12.Scorm12Manifest;
import java.util.List;
import java.util.Optional;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;

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
@Builder
@Getter
@Jacksonized
@AllArgsConstructor(access = PRIVATE)
@JsonFormat(with = JsonFormat.Feature.ACCEPT_CASE_INSENSITIVE_PROPERTIES)
public class Scorm12Resources {

  /**
   * The base URL for all resources in the content package. This URL is used to resolve relative
   * paths for resources.
   */
  @JacksonXmlProperty(isAttribute = true, localName = "base", namespace = "http://www.w3.org/XML/1998/namespace")
  @JsonProperty("base")
  private String base;
  /**
   * A list of all <resource> elements defined within the <resources> element.
   * <p>
   * Each <resource> specifies a particular resource (e.g., SCO, asset, or other content) in the
   * package.
   * </p>
   */
  @JacksonXmlElementWrapper(useWrapping = false)
  @JacksonXmlProperty(localName = "resource", namespace = Scorm12Manifest.NAMESPACE_URI)
  private List<Scorm12Resource> resourceList;

  /**
   * Default constructor for the {@code Scorm12Resources} class.
   */
  @SuppressWarnings("unused")
  public Scorm12Resources() {
    // Default constructor
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
    return resourceList.stream()
        .filter(resource -> id.equals(resource.getIdentifier()))
        .findFirst();
  }
}
