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
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import java.io.Serializable;
import java.util.List;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * Represents a list of objectives referenced by an AU or block in a CMI5 course structure.
 *
 * <p>Defined in the schema as:</p>
 *
 * <pre>{@code
 * <xs:complexType name="referencesObjectivesType">
 *   <xs:sequence>
 *     <xs:element name="objective" maxOccurs="unbounded">
 *       <xs:complexType>
 *         <xs:attribute name="idref" type="xs:anyURI"/>
 *       </xs:complexType>
 *     </xs:element>
 *     <xs:group ref="anyElement"/>
 *   </xs:sequence>
 *   <xs:attributeGroup ref="anyAttribute"/>
 * </xs:complexType>
 * }</pre>
 */
@JsonFormat(with = JsonFormat.Feature.ACCEPT_CASE_INSENSITIVE_PROPERTIES)
public final class ReferencesObjectives implements Serializable {

  /**
   * A list of referenced objectives, each represented by an {@link ObjectiveReference}.
   *
   * <pre>{@code
   * <xs:element name="objective" maxOccurs="unbounded">
   *   <xs:complexType>
   *     <xs:attribute name="idref" type="xs:anyURI"/>
   *   </xs:complexType>
   * }</pre>
   */
  @JacksonXmlElementWrapper(useWrapping = false)
  @JacksonXmlProperty(localName = "objective")
  private List<ObjectiveReference> objectives;

  /**
   * Constructs an instance of {@code ReferencesObjectives} with the specified list of objectives.
   *
   * @param objectives a list of {@link ObjectiveReference} objects that represent the objectives
   * referenced by this instance. Each objective holds an ID reference as defined in the schema.
   */
  public ReferencesObjectives(List<ObjectiveReference> objectives) {
    this.objectives = objectives;
  }

  /**
   * Constructs an instance of {@code ReferencesObjectives} with no initial objectives.
   * <p>
   * This no-argument constructor initializes an empty instance of {@code ReferencesObjectives}. It
   * is primarily used for deserialization or scenarios where the objectives will be set later.
   */
  public ReferencesObjectives() {
    // no-op
  }

  /**
   * Retrieves the list of objectives referenced by this instance.
   *
   * @return a list of {@link ObjectiveReference} objects representing the objectives.
   */
  public List<ObjectiveReference> getObjectives() {
    return this.objectives;
  }

  /**
   * Sets the list of objectives for this instance.
   *
   * @param objectives a list of {@link ObjectiveReference} objects representing the objectives to
   * be associated with this instance.
   */
  public void setObjectives(List<ObjectiveReference> objectives) {
    this.objectives = objectives;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (!(o instanceof ReferencesObjectives that)) {
      return false;
    }

    return new EqualsBuilder()
        .append(getObjectives(), that.getObjectives())
        .isEquals();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder(17, 37)
        .append(getObjectives())
        .toHashCode();
  }
}
