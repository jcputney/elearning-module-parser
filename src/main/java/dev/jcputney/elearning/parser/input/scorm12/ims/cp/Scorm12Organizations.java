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
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * Represents the {@code <organizations>} element in the SCORM 1.2 manifest file.
 * <p>
 * The {@code <organizations>} element defines a collection of organizations within the content
 * package. Each organization represents a hierarchical structure of learning resources and is
 * identified by a unique identifier.
 * </p>
 *
 * <pre>{@code
 * <xsd:complexType name="organizationsType">
 *    <xsd:sequence>
 *       <xsd:element ref="organization" minOccurs="0" maxOccurs="unbounded"/>
 *       <xsd:group ref="grp.any"/>
 *    </xsd:sequence>
 *    <xsd:attributeGroup ref="attr.default"/>
 *    <xsd:anyAttribute namespace="##other" processContents="strict"/>
 * </xsd:complexType>
 * }</pre>
 */
@JsonFormat(with = JsonFormat.Feature.ACCEPT_CASE_INSENSITIVE_PROPERTIES)
public class Scorm12Organizations implements Serializable {

  /**
   * The default organization identifier within the content package. Defines which organization to
   * use if multiple organizations are present.
   */
  @JacksonXmlProperty(isAttribute = true, localName = "default", namespace = Scorm12Manifest.NAMESPACE_URI)
  @JsonProperty("default")
  private String defaultOrganization;
  /**
   * A list of organizations within the content package, each representing a structured hierarchy of
   * learning items.
   */
  @JacksonXmlElementWrapper(useWrapping = false)
  @JacksonXmlProperty(localName = "organization", namespace = Scorm12Manifest.NAMESPACE_URI)
  private List<Scorm12Organization> organizationList;

  public Scorm12Organizations(String defaultOrganization,
      List<Scorm12Organization> organizationList) {
    this.defaultOrganization = defaultOrganization;
    this.organizationList = organizationList;
  }

  public Scorm12Organizations() {
  }

  /**
   * Retrieves an organization by its unique identifier.
   *
   * @param id The unique identifier for the organization.
   * @return The organization with the specified identifier, or null if not found.
   */
  @JsonIgnore
  public Scorm12Organization getOrganizationById(String id) {
    if (organizationList == null || id == null) {
      return null;
    }

    return organizationList
        .stream()
        .filter(org -> id.equals(org.getIdentifier()))
        .findFirst()
        .orElse(null);
  }

  /**
   * Retrieves the default organization for the content package.
   *
   * @return The default organization, or null if not found.
   */
  @JsonIgnore
  public Scorm12Organization getDefault() {
    Scorm12Organization defaultOrg = getOrganizationById(defaultOrganization);
    if (defaultOrg != null) {
      return defaultOrg;
    }

    if (organizationList == null || organizationList.isEmpty()) {
      return null;
    }

    return organizationList.get(0);
  }

  public String getDefaultOrganization() {
    return this.defaultOrganization;
  }

  public void setDefaultOrganization(String defaultOrganization) {
    this.defaultOrganization = defaultOrganization;
  }

  public List<Scorm12Organization> getOrganizationList() {
    return this.organizationList;
  }

  public void setOrganizationList(
      List<Scorm12Organization> organizationList) {
    this.organizationList = organizationList;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (!(o instanceof Scorm12Organizations that)) {
      return false;
    }

    return new EqualsBuilder()
        .append(getDefaultOrganization(),
            that.getDefaultOrganization())
        .append(getOrganizationList(), that.getOrganizationList())
        .isEquals();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder(17, 37)
        .append(getDefaultOrganization())
        .append(getOrganizationList())
        .toHashCode();
  }
}
