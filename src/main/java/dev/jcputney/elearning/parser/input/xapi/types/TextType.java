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
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import java.io.Serializable;
import java.util.List;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * Represents a localized text element, supporting multiple languages via langstring elements.
 *
 * <p>Defined in the schema as:</p>
 *
 * <pre>{@code
 * <xs:complexType name="textType">
 *   <xs:sequence>
 *     <xs:element name="langstring" maxOccurs="unbounded" minOccurs="1">
 *       <xs:complexType>
 *         <xs:simpleContent>
 *           <xs:extension base="xs:string">
 *             <xs:attribute name="lang" type="xs:language"/>
 *             <xs:attributeGroup ref="anyAttribute"/>
 *           </xs:extension>
 *         </xs:simpleContent>
 *       </xs:complexType>
 *     </xs:element>
 *     <xs:group ref="anyElement"/>
 *   </xs:sequence>
 *   <xs:attributeGroup ref="anyAttribute"/>
 * </xs:complexType>
 * }</pre>
 */
@JsonFormat(with = JsonFormat.Feature.ACCEPT_CASE_INSENSITIVE_PROPERTIES)
public final class TextType implements Serializable {

  /**
   * List of localized strings, each represented as a {@link LangString}.
   *
   * <pre>{@code
   * <xs:element name="langstring" maxOccurs="unbounded" minOccurs="1">
   *   <xs:complexType>
   *     <xs:simpleContent>
   *       <xs:extension base="xs:string">
   *         <xs:attribute name="lang" type="xs:language"/>
   *         <xs:attributeGroup ref="anyAttribute"/>
   *       </xs:extension>
   *     </xs:simpleContent>
   *   </xs:complexType>
   * }</pre>
   */
  @JacksonXmlElementWrapper(useWrapping = false)
  @JacksonXmlProperty(localName = "langstring")
  private List<LangString> strings;

  /**
   * Constructs a new {@code TextType} instance with a list of {@link LangString} elements.
   *
   * @param strings a list of {@link LangString} instances representing localized text elements,
   * where each element includes a string value and an optional language attribute
   */
  public TextType(List<LangString> strings) {
    this.strings = strings;
  }

  /**
   * Default constructor for the {@code TextType} class.
   * <p>
   * Initializes a new instance of the {@code TextType} class with default values. This constructor
   * does not perform any operations and is used primarily for object instantiation without initial
   * data.
   */
  public TextType() {
    // no-op
  }

  /**
   * Retrieves the list of localized strings associated with this object.
   *
   * @return a list of {@link LangString} instances, each representing a localized string with an
   * optional language attribute
   */
  public List<LangString> getStrings() {
    return this.strings;
  }

  /**
   * Sets the list of localized strings for this object.
   *
   * @param strings a list of {@link LangString} instances, each representing a localized string
   * with its associated language
   */
  public void setStrings(List<LangString> strings) {
    this.strings = strings;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (!(o instanceof TextType textType)) {
      return false;
    }

    return new EqualsBuilder()
        .append(getStrings(), textType.getStrings())
        .isEquals();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder(17, 37)
        .append(getStrings())
        .toHashCode();
  }
}
