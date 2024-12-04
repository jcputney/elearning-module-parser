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

package dev.jcputney.elearning.parser.input.scorm2004.ims.cp;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import dev.jcputney.elearning.parser.input.scorm2004.Scorm2004Manifest;
import java.util.List;
import lombok.Data;

/**
 * Represents the hierarchical structure of organizations in the content package. Organizations
 * define the arrangement of items within the package.
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonFormat(with = JsonFormat.Feature.ACCEPT_CASE_INSENSITIVE_PROPERTIES)
public class Scorm2004Organizations {

  /**
   * The default organization identifier within the content package. Defines which organization to
   * use if multiple organizations are present.
   */
  @JacksonXmlProperty(isAttribute = true, localName = "default", namespace = Scorm2004Manifest.NAMESPACE_URI)
  @JsonProperty("default")
  private String defaultOrganization;

  /**
   * A list of organizations within the content package, each representing a structured hierarchy of
   * learning items.
   */
  @JacksonXmlElementWrapper(useWrapping = false)
  @JacksonXmlProperty(localName = "organization", namespace = Scorm2004Manifest.NAMESPACE_URI)
  private List<Scorm2004Organization> organizationList;

  /**
   * Retrieves an organization by its unique identifier.
   *
   * @param id The unique identifier for the organization.
   * @return The organization with the specified identifier, or null if not found.
   */
  public Scorm2004Organization getOrganizationById(String id) {
    return organizationList.stream()
        .filter(org -> org.getIdentifier().equals(id))
        .findFirst()
        .orElse(null);
  }

  /**
   * Retrieves the default organization for the content package.
   *
   * @return The default organization, or null if not found.
   */
  public Scorm2004Organization getDefaultOrganization() {
    return getOrganizationById(defaultOrganization);
  }

  /**
   * Retrieves an item by its unique identifier.
   *
   * @param itemId The unique identifier for the item.
   * @return The item with the specified identifier, or null if not found.
   */
  public Scorm2004Item getItemById(String itemId) {
    Scorm2004Item result = organizationList.stream()
        .map(Scorm2004Organization::getItems)
        .flatMap(List::stream)
        .filter(item -> item.getIdentifier().equals(itemId))
        .findFirst()
        .orElse(null);

    if (result == null) {
      // search child items
      for (Scorm2004Organization org : organizationList) {
        result = org.getItems().stream()
            .filter(item -> item.getIdentifier().equals(itemId))
            .findFirst()
            .orElse(null);

        if (result != null) {
          break;
        }
      }
    }

    return result;
  }
}
