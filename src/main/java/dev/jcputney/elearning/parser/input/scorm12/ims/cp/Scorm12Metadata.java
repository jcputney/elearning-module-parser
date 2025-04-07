/*
 * qlty-ignore: +qlty:similar-code
 *
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
 *
 * qlty-ignore: -qlty:similar-code
 */

package dev.jcputney.elearning.parser.input.scorm12.ims.cp;

import static lombok.AccessLevel.PRIVATE;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import dev.jcputney.elearning.parser.api.LoadableMetadata;
import dev.jcputney.elearning.parser.input.lom.LOM;
import dev.jcputney.elearning.parser.input.scorm12.Scorm12ADLCP;
import dev.jcputney.elearning.parser.input.scorm12.Scorm12Manifest;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

/**
 * Represents the metadata element in SCORM 1.2.
 *
 * <p>The <code>Metadata</code> element provides descriptive information about an item, resource,
 * or manifest. It can include inline metadata or reference an external metadata file.</p>
 *
 * <p>Schema Snippet:</p>
 * <pre>{@code
 * <xsd:element name="metadata">
 *   <xsd:complexType>
 *     <xsd:sequence>
 *       <xsd:element name="schema" type="xsd:string" minOccurs="0"/>
 *       <xsd:element name="schemaversion" type="xsd:string" minOccurs="0"/>
 *       <xsd:element ref="lom:lom" minOccurs="0"/>
 *       <xsd:element ref="adlcp:location" minOccurs="0"/>
 *     </xsd:sequence>
 *     <xsd:anyAttribute namespace="##other" processContents="lax"/>
 *   </xsd:complexType>
 * </xsd:element>
 * }</pre>
 *
 * <p>Example Usage in imsmanifest.xml:</p>
 * <pre>{@code
 * <metadata>
 *   <schema>ADL SCORM</schema>
 *   <schemaversion>1.2</schemaversion>
 *   <lom:lom xmlns:lom="http://ltsc.ieee.org/xsd/LOM">
 *     <general>
 *       <title>
 *         <string language="en">Introduction to Golf</string>
 *       </title>
 *     </general>
 *   </lom:lom>
 * </metadata>
 * }</pre>
 */
@SuperBuilder
@Getter
@Jacksonized
@AllArgsConstructor(access = PRIVATE)
@JsonFormat(with = JsonFormat.Feature.ACCEPT_CASE_INSENSITIVE_PROPERTIES)
public class Scorm12Metadata implements LoadableMetadata {

  /**
   * The schema used in the metadata description, such as "ADL SCORM". This element is optional.
   */
  @JacksonXmlProperty(localName = "schema", namespace = Scorm12Manifest.NAMESPACE_URI)
  private String schema;
  /**
   * The version of the schema used, such as "1.2". This element is optional.
   */
  @JacksonXmlProperty(localName = "schemaversion", namespace = Scorm12Manifest.NAMESPACE_URI)
  private String schemaVersion;
  /**
   * Inline metadata in the form of a Learning Object Metadata (LOM) element. This element is
   * optional.
   */
  @JacksonXmlProperty(localName = "lom", namespace = LOM.NAMESPACE_URI)
  @Setter
  private LOM lom;
  /**
   * A reference to an external metadata file, provided as a URI. This element is optional.
   */
  @JacksonXmlProperty(localName = "location", namespace = Scorm12ADLCP.NAMESPACE_URI)
  private String location;

  /**
   * Default constructor for the Scorm12Metadata class.
   */
  @SuppressWarnings("unused")
  public Scorm12Metadata() {
    // Default constructor
  }
}
