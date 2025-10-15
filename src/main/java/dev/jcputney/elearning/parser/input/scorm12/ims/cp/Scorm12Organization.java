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
import dev.jcputney.elearning.parser.input.scorm12.Scorm12Manifest;
import java.io.Serializable;
import java.util.List;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * Represents the <code>organization</code> element in SCORM 1.2.
 *
 * <p>The <code>organization</code> element defines the structure and hierarchy of content items
 * within a content package. It also allows for metadata to describe the organization and an
 * optional <code>structure</code> attribute.</p>
 *
 * <p>Schema Snippet:</p>
 * <pre>{@code
 * <xsd:complexType name="organizationType">
 *    <xsd:sequence>
 *       <xsd:element ref="title" minOccurs="0"/>
 *       <xsd:element ref="item" minOccurs="0" maxOccurs="unbounded"/>
 *       <xsd:element ref="metadata" minOccurs="0"/>
 *       <xsd:group ref="grp.any"/>
 *    </xsd:sequence>
 *    <xsd:attributeGroup ref="attr.identifier.req"/>
 *    <xsd:attributeGroup ref="attr.structure.req"/>
 *    <xsd:anyAttribute namespace="##other" processContents="strict"/>
 * </xsd:complexType>
 * }</pre>
 *
 * <p>Example Usage in imsmanifest.xml:</p>
 * <pre>{@code
 * <organization identifier="org_1" structure="hierarchical">
 *   <title>Sample Organization</title>
 *   <metadata>
 *     <schema>ADL SCORM</schema>
 *     <schemaversion>1.2</schemaversion>
 *   </metadata>
 *   <item identifier="item_1" identifierref="resource_1">
 *     <title>Sample Item</title>
 *   </item>
 * </organization>
 * }</pre>
 */
@JsonFormat(with = JsonFormat.Feature.ACCEPT_CASE_INSENSITIVE_PROPERTIES)
public final class Scorm12Organization implements Serializable {

  /**
   * The unique identifier for this organization.
   */
  @JacksonXmlProperty(isAttribute = true)
  @JsonProperty(value = "identifier", required = true)
  private String identifier;

  /**
   * The structure of the organization. Common values include hierarchical or flat.
   */
  @JacksonXmlProperty(isAttribute = true, localName = "structure")
  @JsonProperty(value = "structure")
  private String structure = "hierarchical";

  /**
   * The title of the organization, providing a descriptive label.
   */
  @JacksonXmlProperty(localName = "title", namespace = Scorm12Manifest.NAMESPACE_URI)
  private String title;

  /**
   * The metadata associated with this organization.
   */
  @JacksonXmlProperty(localName = "metadata", namespace = Scorm12Manifest.NAMESPACE_URI)
  private Scorm12Metadata metadata;

  /**
   * A list of items that belong to this organization, defining the hierarchical structure.
   */
  @JacksonXmlElementWrapper(useWrapping = false)
  @JacksonXmlProperty(localName = "item", namespace = Scorm12Manifest.NAMESPACE_URI)
  private List<Scorm12Item> items;

  public Scorm12Organization(String identifier, String structure, String title,
      Scorm12Metadata metadata, List<Scorm12Item> items) {
    this.identifier = identifier;
    this.structure = structure;
    this.title = title;
    this.metadata = metadata;
    this.items = items;
  }

  public Scorm12Organization() {
    // no-op
  }

  /**
   * Retrieves the identifier associated with the SCORM 1.2 organization.
   *
   * @return A string representing the unique identifier of the organization.
   */
  public String getIdentifier() {
    return this.identifier;
  }

  /**
   * Assigns a unique identifier to the SCORM 1.2 organization.
   *
   * @param identifier A string representing the unique identifier for the organization. The
   * identifier should match the required format or expectations of the SCORM 1.2 specification.
   */
  public void setIdentifier(String identifier) {
    this.identifier = identifier;
  }

  /**
   * Retrieves the structure of the SCORM 1.2 organization.
   *
   * @return A string representing the structure of the organization.
   */
  public String getStructure() {
    return this.structure;
  }

  /**
   * Sets the structure associated with the SCORM 1.2 organization.
   *
   * @param structure A string representing the structure of the organization. This parameter should
   * adhere to the expected format as defined by the SCORM 1.2 specification.
   */
  public void setStructure(String structure) {
    this.structure = structure;
  }

  /**
   * Retrieves the title associated with the SCORM 1.2 organization.
   *
   * @return A string representing the title of the organization.
   */
  public String getTitle() {
    return this.title;
  }

  /**
   * Sets the title associated with the SCORM 1.2 organization.
   *
   * @param title A string representing the title of the organization. This value should provide a
   * human-readable description or label for the organization.
   */
  public void setTitle(String title) {
    this.title = title;
  }

  /**
   * Retrieves the metadata associated with the SCORM 1.2 organization.
   *
   * @return A Scorm12Metadata object representing the metadata of the organization.
   */
  public Scorm12Metadata getMetadata() {
    return this.metadata;
  }

  /**
   * Sets the metadata associated with the SCORM 1.2 organization.
   *
   * @param metadata A Scorm12Metadata object representing the metadata of the organization. This
   * metadata provides descriptive information conforming to the SCORM 1.2 specification.
   */
  public void setMetadata(Scorm12Metadata metadata) {
    this.metadata = metadata;
  }

  /**
   * Retrieves the list of SCORM 1.2 items associated with the organization.
   *
   * @return A list of Scorm12Item objects representing the items defined within the organization.
   */
  public List<Scorm12Item> getItems() {
    return this.items;
  }

  /**
   * Sets the list of SCORM 1.2 items associated with the organization.
   *
   * @param items A list of Scorm12Item objects representing the items to be defined within the
   * organization. Each item should comply with the SCORM 1.2 specification.
   */
  public void setItems(List<Scorm12Item> items) {
    this.items = items;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (!(o instanceof Scorm12Organization that)) {
      return false;
    }

    return new EqualsBuilder()
        .append(getIdentifier(), that.getIdentifier())
        .append(getStructure(), that.getStructure())
        .append(getTitle(), that.getTitle())
        .append(getMetadata(), that.getMetadata())
        .append(getItems(), that.getItems())
        .isEquals();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder(17, 37)
        .append(getIdentifier())
        .append(getStructure())
        .append(getTitle())
        .append(getMetadata())
        .append(getItems())
        .toHashCode();
  }
}
