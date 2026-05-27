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
package dev.jcputney.elearning.parser.input.cmi5;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import dev.jcputney.elearning.parser.input.xapi.types.TextType;
import java.io.Serializable;
import java.util.List;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * Represents the root course element in a CMI5 course structure, including metadata such as title,
 * description, and optional custom extensions.
 *
 * <p>Defined in the schema as:</p>
 *
 * <pre>{@code
 * <xs:element name="course">
 *   <xs:complexType>
 *     <xs:sequence>
 *       <xs:element name="title" type="textType"/>
 *       <xs:element name="description" type="textType"/>
 *       <xs:group ref="anyElement"/>
 *     </xs:sequence>
 *     <xs:attributeGroup ref="anyAttribute"/>
 *     <xs:attribute name="id" type="xs:anyURI" use="required"/>
 *   </xs:complexType>
 * </xs:element>
 * }</pre>
 */
@JsonFormat(with = JsonFormat.Feature.ACCEPT_CASE_INSENSITIVE_PROPERTIES)
public final class Course implements Serializable {

  /**
   * The title of the course, represented as a localized text type.
   *
   * <pre>{@code
   * <xs:element name="title" type="textType"/>
   * }</pre>
   */
  @JacksonXmlProperty(localName = "title")
  private TextType title;

  /**
   * The description of the course, represented as a localized text type.
   *
   * <pre>{@code
   * <xs:element name="description" type="textType"/>
   * }</pre>
   */
  @JacksonXmlProperty(localName = "description")
  private TextType description;

  /**
   * Optional manifest-provided context template for this course.
   * <p>
   * The cmi5 Quartz Course Structure schema does not define {@code contextTemplate} as a standard
   * course element, but some packages may include it as extension data. It is preserved as structured
   * JSON so an LMS can merge course-scoped template data with AU-scoped overrides when constructing
   * runtime {@code LMS.LaunchData}.
   * </p>
   */
  @JacksonXmlProperty(localName = "contextTemplate")
  private JsonNode contextTemplate;

  /**
   * A list of additional custom elements (extensions) included in the course definition.
   *
   * <pre>{@code
   * <xs:group ref="anyElement"/>
   * }</pre>
   */
  @JacksonXmlElementWrapper(useWrapping = false)
  @JacksonXmlProperty(localName = "anyElement")
  private List<Object> customExtensions;

  /**
   * The unique identifier for the course, represented as an anyURI.
   *
   * <pre>{@code
   * <xs:attribute name="id" type="xs:anyURI" use="required"/>
   * }</pre>
   */
  @JacksonXmlProperty(isAttribute = true)
  @JsonProperty("id")
  private String id;

  /**
   * Constructs a new instance of the Course class with specified title, description, custom
   * extensions, and identifier.
   *
   * @param title a {@link TextType} instance representing the title of the course
   * @param description a {@link TextType} instance representing the description of the course
   * @param customExtensions a list of objects representing the custom extensions of the course
   * @param id a string representing the unique identifier of the course
   */
  public Course(TextType title, TextType description, List<Object> customExtensions, String id) {
    this.title = title;
    this.description = description;
    this.customExtensions = customExtensions;
    this.id = id;
  }

  /**
   * Default constructor for the Course class. Initializes a new instance of the Course class
   * without setting any fields or properties.
   */
  public Course() {
    // no-op
  }

  /**
   * Retrieves the title of the course.
   *
   * @return a {@link TextType} instance representing the title of the course
   */
  public TextType getTitle() {
    return this.title;
  }

  /**
   * Sets the title of the course.
   *
   * @param title a {@link TextType} instance representing the title of the course
   */
  public void setTitle(TextType title) {
    this.title = title;
  }

  /**
   * Retrieves the description of the course.
   *
   * @return a {@link TextType} instance representing the description of the course
   */
  public TextType getDescription() {
    return this.description;
  }

  /**
   * Sets the description of the course.
   *
   * @param description a {@link TextType} instance representing the description of the course
   */
  public void setDescription(TextType description) {
    this.description = description;
  }

  /**
   * Retrieves the course-level context template extension, if present.
   *
   * @return the course-level context template as structured JSON, or {@code null} if absent
   */
  public JsonNode getContextTemplate() {
    return this.contextTemplate;
  }

  /**
   * Sets the course-level context template extension.
   *
   * @param contextTemplate the structured context template extension
   */
  public void setContextTemplate(JsonNode contextTemplate) {
    this.contextTemplate = contextTemplate;
  }

  /**
   * Retrieves the custom extensions associated with the course.
   *
   * @return a list of objects representing the custom extensions of the course
   */
  public List<Object> getCustomExtensions() {
    return this.customExtensions;
  }

  /**
   * Sets the custom extensions associated with the course.
   *
   * @param customExtensions a list of objects representing the custom extensions of the course
   */
  public void setCustomExtensions(List<Object> customExtensions) {
    this.customExtensions = customExtensions;
  }

  /**
   * Retrieves the identifier of the course.
   *
   * @return a string representing the unique identifier of the course
   */
  public String getId() {
    return this.id;
  }

  /**
   * Sets the identifier of the course.
   *
   * @param id a string representing the unique identifier of the course
   */
  public void setId(String id) {
    this.id = id;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (!(o instanceof Course course)) {
      return false;
    }

    return new EqualsBuilder()
        .append(getTitle(), course.getTitle())
        .append(getDescription(), course.getDescription())
        .append(getContextTemplate(), course.getContextTemplate())
        .append(getCustomExtensions(), course.getCustomExtensions())
        .append(getId(), course.getId())
        .isEquals();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder(17, 37)
        .append(getTitle())
        .append(getDescription())
        .append(getContextTemplate())
        .append(getCustomExtensions())
        .append(getId())
        .toHashCode();
  }
}
