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
package dev.jcputney.elearning.parser.input.scorm2004.ims.cp;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import dev.jcputney.elearning.parser.api.LoadableMetadata;
import dev.jcputney.elearning.parser.input.lom.LOM;
import dev.jcputney.elearning.parser.input.scorm2004.ADLCP;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * Represents metadata for a SCORM element, which can either be inline metadata using a LOM element
 * or an external reference to a metadata file.
 * <p>LOM Example:</p>
 * <pre>{@code
 * <metadata>
 *   <lom>
 *     <general>
 *       <description>
 *         <string language="en-us">Metadata description here.</string>
 *       </description>
 *     </general>
 *   </lom>
 * </metadata>
 * }</pre>
 * <p>External Metadata Example:</p>
 * <pre>{@code
 * <metadata>
 *   <adlcp:location>metadata.xml</adlcp:location>
 * </metadata>
 * }</pre>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonFormat(with = JsonFormat.Feature.ACCEPT_CASE_INSENSITIVE_PROPERTIES)
public final class Scorm2004SubMetadata implements LoadableMetadata {

  /**
   * The location of the external metadata file, referenced using the <code>adlcp:location</code>
   * element.
   */
  @JacksonXmlProperty(localName = "location", namespace = ADLCP.NAMESPACE_URI)
  private String location;

  /**
   * Inline metadata represented as a LOM element.
   */
  @JacksonXmlProperty(localName = "lom", namespace = LOM.NAMESPACE_URI)
  private LOM lom;

  /**
   * Default constructor for the Scorm2004SubMetadata class. Creates an instance of
   * Scorm2004SubMetadata with default values. This is a no-op constructor.
   */
  public Scorm2004SubMetadata() {
    // no-op
  }

  /**
   * Retrieves the location of the external metadata file referenced by the
   * <code>adlcp:location</code> element.
   *
   * @return the location of the external metadata file as a string
   */
  public String getLocation() {
    return this.location;
  }

  /**
   * Sets the location of the external metadata file. The location is represented by the
   * <code>adlcp:location</code> element and typically specifies the path or URL to the metadata
   * file.
   *
   * @param location the location of the external metadata file as a string
   */
  public void setLocation(String location) {
    this.location = location;
  }

  /**
   * Retrieves the inline metadata represented as a LOM (Learning Object Metadata) element.
   *
   * @return the LOM instance containing inline metadata
   */
  public LOM getLom() {
    return this.lom;
  }

  /**
   * Sets the inline metadata represented as a LOM (Learning Object Metadata) element.
   *
   * @param lom the LOM instance containing inline metadata
   */
  @JacksonXmlProperty(localName = "lom", namespace = LOM.NAMESPACE_URI)
  public void setLom(LOM lom) {
    this.lom = lom;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (!(o instanceof Scorm2004SubMetadata that)) {
      return false;
    }

    return new EqualsBuilder()
        .append(getLocation(), that.getLocation())
        .append(getLom(), that.getLom())
        .isEquals();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder(17, 37)
        .append(getLocation())
        .append(getLom())
        .toHashCode();
  }
}