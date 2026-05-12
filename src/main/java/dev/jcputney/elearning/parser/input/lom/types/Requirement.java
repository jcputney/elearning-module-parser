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

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import java.util.List;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * Represents a requirement for a learning object in a Learning Object Metadata (LOM) document. A
 * requirement is a list of conditions that must be met to use the learning object.
 *
 * <p>Schema snippet:
 * <pre>{@code
 * <complexType name="requirement">
 *   <choice minOccurs="0" maxOccurs="unbounded">
 *     <group ref="orComposite"/>
 *     <group ref="ex:customElements"/>
 *   </choice>
 *   <attributeGroup ref="ag:requirement"/>
 * </complexType>
 * }</pre>
 */
@JsonFormat(with = JsonFormat.Feature.ACCEPT_CASE_INSENSITIVE_PROPERTIES)
public final class Requirement extends OrComposite {

  /**
   * The list of OR-composite elements representing a set of alternative conditions that must be met
   * to use the learning object. This is for SCORM 2004 compatibility.
   * <p>Schema snippet:</p>
   * <pre>{@code
   * <xs:group ref="orComposite">
   *   <xs:element name="orComposite" type="OrComposite" maxOccurs="unbounded"/>
   * </xs:group>
   * }</pre>
   */
  @JacksonXmlElementWrapper(localName = "orComposite", useWrapping = false)
  @JacksonXmlProperty(localName = "orComposite")
  private List<OrComposite> orCompositeList;

  /**
   * Default constructor for the {@code Requirement} class.
   * <p>
   * Initializes a new instance of the {@code Requirement} class without performing any specific
   * operations. This constructor allows object creation without the need for initial parameters or
   * state.
   */
  public Requirement() {
    // no-op
  }

  /**
   * Retrieves the list of OR-composite elements, which represent a set of alternative conditions
   * that must be met to use the associated learning object.
   *
   * @return a list of {@link OrComposite} instances, where each represents a platform or software
   * requirement for the learning object.
   */
  public List<OrComposite> getOrCompositeList() {
    return this.orCompositeList;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (!(o instanceof Requirement that)) {
      return false;
    }

    return new EqualsBuilder()
        .appendSuper(super.equals(o))
        .append(getOrCompositeList(), that.getOrCompositeList())
        .isEquals();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder(17, 37)
        .appendSuper(super.hashCode())
        .append(getOrCompositeList())
        .toHashCode();
  }
}
