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
package dev.jcputney.elearning.parser.input.lom.types;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import java.io.Serializable;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * Represents a single language string in LOM metadata. This type is used for fields that only
 * require a single string value with a language attribute.
 *
 * <pre>{@code
 * <xs:complexType name="singleLangString">
 *   <xs:sequence>
 *     <xs:element name="string" type="langString"/>
 *   </xs:sequence>
 * </xs:complexType>
 * }</pre>
 */
@JsonFormat(with = JsonFormat.Feature.ACCEPT_CASE_INSENSITIVE_PROPERTIES)
public final class SingleLangString implements Serializable {

  /**
   * The string value for a given language.
   */
  @JacksonXmlProperty(localName = "string")
  @JsonAlias("langstring")
  private LangString langString;

  /**
   * Default constructor for the {@code SingleLangString} class. Creates an empty instance of the
   * {@code SingleLangString} class with no initial state. Primarily used for object instantiation
   * without predefined data.
   */
  public SingleLangString() {
    // no-op
  }

  /**
   * Retrieves the {@code LangString} object associated with this instance.
   *
   * @return the {@code LangString} representing a single string value with a language attribute
   */
  public LangString getLangString() {
    return this.langString;
  }

  /**
   * Sets the {@code LangString} value for this instance.
   *
   * @param langString the {@code LangString} object to set, representing a string value with an
   * associated language attribute
   */
  @JsonAlias("langstring")
  @JacksonXmlProperty(localName = "string")
  public void setLangString(LangString langString) {
    this.langString = langString;
  }

  public String toString() {
    return "SingleLangString(langString=" + this.getLangString() + ")";
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (!(o instanceof SingleLangString that)) {
      return false;
    }

    return new EqualsBuilder()
        .append(getLangString(), that.getLangString())
        .isEquals();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder(17, 37)
        .append(getLangString())
        .toHashCode();
  }
}
