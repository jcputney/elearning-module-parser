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
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import dev.jcputney.elearning.parser.api.LoadableMetadata;
import dev.jcputney.elearning.parser.input.lom.LOM;
import dev.jcputney.elearning.parser.input.scorm12.Scorm12ADLCP;
import dev.jcputney.elearning.parser.input.scorm12.Scorm12Manifest;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

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
  private LOM lom;

  /**
   * A reference to an external metadata file, provided as a URI. This element is optional.
   */
  @JacksonXmlProperty(localName = "location", namespace = Scorm12ADLCP.NAMESPACE_URI)
  private String location;

  public Scorm12Metadata() {
    // no-op
  }

  /**
   * Retrieves the schema associated with the SCORM 1.2 metadata.
   *
   * @return A string representing the schema of the SCORM 1.2 metadata.
   */
  public String getSchema() {
    return this.schema;
  }

  /**
   * Sets the schema for the SCORM 1.2 metadata.
   *
   * @param schema A string representing the schema to be associated with the SCORM 1.2 metadata.
   */
  public void setSchema(String schema) {
    this.schema = schema;
  }

  /**
   * Retrieves the schema version associated with the SCORM 1.2 metadata.
   *
   * @return A string representing the schema version of the SCORM 1.2 metadata.
   */
  public String getSchemaVersion() {
    return this.schemaVersion;
  }

  /**
   * Sets the schema version for the SCORM 1.2 metadata.
   *
   * @param schemaVersion A string representing the schema version to be associated with the SCORM
   * 1.2 metadata.
   */
  public void setSchemaVersion(String schemaVersion) {
    this.schemaVersion = schemaVersion;
  }

  /**
   * Retrieves the LOM (Learning Object Metadata) associated with the SCORM 1.2 metadata.
   *
   * @return The LOM object representing the learning object metadata.
   */
  public LOM getLom() {
    return this.lom;
  }

  /**
   * Sets the LOM (Learning Object Metadata) for the SCORM 1.2 metadata.
   *
   * @param lom The LOM object representing the learning object metadata to be associated with the
   * SCORM 1.2 metadata.
   */
  @JacksonXmlProperty(localName = "lom", namespace = LOM.NAMESPACE_URI)
  public void setLom(LOM lom) {
    this.lom = lom;
  }

  /**
   * Retrieves the location associated with the SCORM 1.2 metadata.
   *
   * @return A string representing the location of the SCORM 1.2 metadata.
   */
  public String getLocation() {
    return this.location;
  }

  /**
   * Sets the location associated with the SCORM 1.2 metadata.
   *
   * @param location A string representing the location to be associated with the SCORM 1.2
   * metadata.
   */
  public void setLocation(String location) {
    this.location = location;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (!(o instanceof Scorm12Metadata that)) {
      return false;
    }

    return new EqualsBuilder()
        .append(getSchema(), that.getSchema())
        .append(getSchemaVersion(), that.getSchemaVersion())
        .append(getLom(), that.getLom())
        .append(getLocation(), that.getLocation())
        .isEquals();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder(17, 37)
        .append(getSchema())
        .append(getSchemaVersion())
        .append(getLom())
        .append(getLocation())
        .toHashCode();
  }
}
