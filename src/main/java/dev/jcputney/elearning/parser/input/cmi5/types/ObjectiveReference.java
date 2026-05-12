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
package dev.jcputney.elearning.parser.input.cmi5.types;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import java.io.Serializable;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * Represents a single objective reference.
 */
@JsonFormat(with = JsonFormat.Feature.ACCEPT_CASE_INSENSITIVE_PROPERTIES)
public final class ObjectiveReference implements Serializable {

  /**
   * The ID reference to the objective, represented as an anyURI.
   *
   * <pre>{@code
   * <xs:attribute name="idref" type="xs:anyURI"/>
   * }</pre>
   */
  @JacksonXmlProperty(isAttribute = true)
  @JsonProperty("idref")
  private String idref;

  /**
   * Constructs an instance of {@code ObjectiveReference}. This is a no-operation constructor,
   * intended for default initialization.
   */
  public ObjectiveReference() {
    // no-op
  }

  /**
   * Retrieves the ID reference associated with this objective reference.
   *
   * @return the ID reference as a string, represented as an anyURI.
   */
  public String getIdref() {
    return this.idref;
  }

  /**
   * Sets the ID reference for this objective reference.
   *
   * @param idref the ID reference to set, represented as a string (anyURI).
   */
  public void setIdref(String idref) {
    this.idref = idref;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (!(o instanceof ObjectiveReference that)) {
      return false;
    }

    return new EqualsBuilder()
        .append(getIdref(), that.getIdref())
        .isEquals();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder(17, 37)
        .append(getIdref())
        .toHashCode();
  }
}
