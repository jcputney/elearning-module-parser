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
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import dev.jcputney.elearning.parser.input.common.serialization.NormalizedIdDeserializer;
import java.io.Serializable;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * Represents a dependency element within a resource, specifying a relationship to another resource
 * that this resource relies upon.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonFormat(with = JsonFormat.Feature.ACCEPT_CASE_INSENSITIVE_PROPERTIES)
public final class Scorm2004Dependency implements Serializable {

  /**
   * The identifier reference for the dependency. This points to another resource in the content
   * package that this resource depends on.
   */
  @JacksonXmlProperty(isAttribute = true, localName = "identifierref")
  @JsonProperty("identifierref")
  @JsonDeserialize(using = NormalizedIdDeserializer.class)
  private String identifierRef;

  /**
   * Constructs a Scorm2004Dependency object with the specified identifier reference.
   *
   * @param identifierRef the identifier reference pointing to another resource in the content
   * package that this resource depends on
   */
  public Scorm2004Dependency(String identifierRef) {
    this.identifierRef = identifierRef;
  }

  /**
   * Constructs a new instance of Scorm2004Dependency with default values. This no-argument
   * constructor initializes a Scorm2004Dependency object without setting any identifier reference.
   * Primarily used for deserialization or scenarios where an initial dependency reference is not
   * immediately required.
   */
  public Scorm2004Dependency() {
    // no-op
  }

  /**
   * Retrieves the identifier reference for the dependency. This refers to another resource in the
   * content package that this resource relies upon.
   *
   * @return the identifier reference of the dependency.
   */
  public String getIdentifierRef() {
    return this.identifierRef;
  }

  /**
   * Sets the identifier reference for the dependency. This reference points to another resource in
   * the content package that this resource depends on.
   *
   * @param identifierRef the identifier reference for the dependency
   */
  public void setIdentifierRef(String identifierRef) {
    this.identifierRef = identifierRef;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (!(o instanceof Scorm2004Dependency that)) {
      return false;
    }

    return new EqualsBuilder()
        .append(getIdentifierRef(), that.getIdentifierRef())
        .isEquals();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder(17, 37)
        .append(getIdentifierRef())
        .toHashCode();
  }
}
