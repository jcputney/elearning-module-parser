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
package dev.jcputney.elearning.parser.input.xapi.types;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlText;
import java.io.Serializable;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * Represents a simple langstring element as defined in the TinCan XSD schema.
 *
 * <p>According to the official TinCan XSD schema, langstring elements have:</p>
 * <ul>
 *   <li>Text content directly in the element</li>
 *   <li>An optional {@code lang} attribute for language specification</li>
 * </ul>
 *
 * <p>Example XML:</p>
 * <pre>{@code
 * <name lang="en-US">Course Title</name>
 * <description lang="en-US">Course description</description>
 * <launch lang="en-US">index.html</launch>
 * }</pre>
 *
 * <p>This differs from the complex {@link TextType} which uses nested
 * {@code langstring} child elements.</p>
 */
@JsonFormat(with = JsonFormat.Feature.ACCEPT_CASE_INSENSITIVE_PROPERTIES)
public final class SimpleLangString implements Serializable {

  /**
   * The text value of the element.
   */
  @JacksonXmlText
  private String value;

  /**
   * The optional language code (e.g., "en-US", "und").
   */
  @JacksonXmlProperty(isAttribute = true, localName = "lang")
  private String lang;

  /**
   * Default no-argument constructor.
   */
  public SimpleLangString() {
  }

  /**
   * Constructor with value and language.
   *
   * @param value the text value
   * @param lang the language code (may be null)
   */
  public SimpleLangString(String value, String lang) {
    this.value = value;
    this.lang = lang;
  }

  /**
   * Gets the text value.
   *
   * @return the text value
   */
  public String getValue() {
    return value;
  }

  /**
   * Sets the text value.
   *
   * @param value the text value
   */
  public void setValue(String value) {
    this.value = value;
  }

  /**
   * Gets the language code.
   *
   * @return the language code, or null if not specified
   */
  public String getLang() {
    return lang;
  }

  /**
   * Sets the language code.
   *
   * @param lang the language code
   */
  public void setLang(String lang) {
    this.lang = lang;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof SimpleLangString that)) {
      return false;
    }
    return new EqualsBuilder()
        .append(getValue(), that.getValue())
        .append(getLang(), that.getLang())
        .isEquals();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder(17, 37)
        .append(getValue())
        .append(getLang())
        .toHashCode();
  }
}
