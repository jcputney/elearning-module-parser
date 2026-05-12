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
import dev.jcputney.elearning.parser.input.xapi.types.TextType;
import java.io.Serializable;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * Represents a single objective with a title, description, and unique identifier.
 */
@JsonFormat(with = JsonFormat.Feature.ACCEPT_CASE_INSENSITIVE_PROPERTIES)
public final class Objective implements Serializable {

  /**
   * The title of the objective, represented as a localized text type.
   *
   * <pre>{@code
   * <xs:element name="title" type="textType"/>
   * }</pre>
   */
  @JacksonXmlProperty(localName = "title")
  private TextType title;

  /**
   * The description of the objective, represented as a localized text type.
   *
   * <pre>{@code
   * <xs:element name="description" type="textType"/>
   * }</pre>
   */
  @JacksonXmlProperty(localName = "description")
  private TextType description;

  /**
   * The unique identifier for the objective, represented as an anyURI.
   *
   * <pre>{@code
   * <xs:attribute name="id" type="xs:anyURI" use="required"/>
   * }</pre>
   */
  @JacksonXmlProperty(isAttribute = true)
  @JsonProperty("id")
  private String id;

  /**
   * Default constructor for the Objective class. This constructor initializes an Objective instance
   * with no predefined values.
   */
  public Objective() {
    // no-op
  }

  /**
   * Retrieves the title of the objective, represented as a localized text type.
   *
   * @return the title of the objective
   */
  public TextType getTitle() {
    return this.title;
  }

  /**
   * Sets the title of the objective.
   *
   * @param title the new title of the objective, represented as a {@link TextType} object
   */
  public void setTitle(TextType title) {
    this.title = title;
  }

  /**
   * Retrieves the description of the objective, represented as a localized text type.
   *
   * @return the description of the objective as a {@link TextType}
   */
  public TextType getDescription() {
    return this.description;
  }

  /**
   * Sets the description of the objective.
   *
   * @param description the new description of the objective, represented as a {@link TextType}
   * object
   */
  public void setDescription(TextType description) {
    this.description = description;
  }

  /**
   * Retrieves the unique identifier for the objective.
   *
   * @return the unique identifier of the objective as a string
   */
  public String getId() {
    return this.id;
  }

  /**
   * Sets the unique identifier for the objective.
   *
   * @param id the new unique identifier for the objective, represented as a string
   */
  public void setId(String id) {
    this.id = id;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (!(o instanceof Objective objective)) {
      return false;
    }

    return new EqualsBuilder()
        .append(getTitle(), objective.getTitle())
        .append(getDescription(), objective.getDescription())
        .append(getId(), objective.getId())
        .isEquals();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder(17, 37)
        .append(getTitle())
        .append(getDescription())
        .append(getId())
        .toHashCode();
  }
}
