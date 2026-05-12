/*
 * Copyright (c) 2024-2026 Jonathan Putney
 *
 * Licensed under the Apache License, Version 2.0 (the "License").
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at the project root LICENSE file
 * or at http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0
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
public final class Scorm12Metadata implements LoadableMetadata {

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

  /**
   * Default constructor for the Scorm12Metadata class.
   * <p>
   * This constructor initializes a new instance of the Scorm12Metadata class without setting any
   * initial values for its fields. All properties of the instance will need to be explicitly set
   * after construction.
   */
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
