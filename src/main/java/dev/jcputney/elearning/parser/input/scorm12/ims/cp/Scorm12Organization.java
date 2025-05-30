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

import static lombok.AccessLevel.PRIVATE;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import dev.jcputney.elearning.parser.input.scorm12.Scorm12Manifest;
import java.io.Serializable;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.jackson.Jacksonized;

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
@Builder
@Getter
@Jacksonized
@NoArgsConstructor
@EqualsAndHashCode(doNotUseGetters = true)
@AllArgsConstructor(access = PRIVATE)
@JsonFormat(with = JsonFormat.Feature.ACCEPT_CASE_INSENSITIVE_PROPERTIES)
public class Scorm12Organization implements Serializable {

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
  @Default
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
}
