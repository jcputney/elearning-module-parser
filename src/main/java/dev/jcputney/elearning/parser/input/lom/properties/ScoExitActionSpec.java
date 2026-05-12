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
package dev.jcputney.elearning.parser.input.lom.properties;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Feature;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import java.io.Serializable;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * <p>Represents the <strong>scoExitActionSpec</strong> complex type.</p>
 *
 * <p>The following schema snippet specifies its contents:</p>
 * <pre>{@code
 * <xs:complexType name="scoExitActionSpec">
 *   <xs:all>
 *     <xs:element name="satisfied" type="exitTypesSpec" minOccurs="0" maxOccurs="1" />
 *     <xs:element name="notSatisfied" type="exitTypesSpec" minOccurs="0" maxOccurs="1" />
 *   </xs:all>
 * </xs:complexType>
 * }</pre>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonFormat(with = Feature.ACCEPT_CASE_INSENSITIVE_PROPERTIES)
public final class ScoExitActionSpec implements Serializable {

  /**
   * The action to take when the SCO is satisfied.
   */
  @JacksonXmlProperty(localName = "satisfied")
  private ExitTypesSpec satisfied;

  /**
   * The action to take when the SCO is not satisfied.
   */
  @JacksonXmlProperty(localName = "notSatisfied")
  private ExitTypesSpec notSatisfied;

  /**
   * Constructs an instance of the ScoExitActionSpec class with specified actions to take based on
   * the satisfaction or non-satisfaction of the condition.
   *
   * @param satisfied the {@code ExitTypesSpec} defining the action to be executed when the
   * condition is satisfied
   * @param notSatisfied the {@code ExitTypesSpec} defining the action to be executed when the
   * condition is not satisfied
   */
  public ScoExitActionSpec(ExitTypesSpec satisfied, ExitTypesSpec notSatisfied) {
    this.satisfied = satisfied;
    this.notSatisfied = notSatisfied;
  }

  /**
   * Default constructor for the ScoExitActionSpec class. Initializes a new instance of the
   * ScoExitActionSpec class with no specific configuration.
   */
  public ScoExitActionSpec() {
    // no-op
  }

  /**
   * Retrieves the action to take when the SCO is satisfied.
   *
   * @return the {@code ExitTypesSpec} representing the satisfied action, or null if not defined
   */
  public ExitTypesSpec getSatisfied() {
    return this.satisfied;
  }

  /**
   * Updates the action to take when the SCO is satisfied.
   *
   * @param satisfied an {@code ExitTypesSpec} object representing the set of exit actions to be
   * taken when the SCO is satisfied
   */
  public void setSatisfied(ExitTypesSpec satisfied) {
    this.satisfied = satisfied;
  }

  /**
   * Retrieves the action to take when the SCO is not satisfied.
   *
   * @return the {@code ExitTypesSpec} representing the not satisfied action, or null if not defined
   */
  public ExitTypesSpec getNotSatisfied() {
    return this.notSatisfied;
  }

  /**
   * Sets the action to take when the SCO is not satisfied.
   *
   * @param notSatisfied an {@code ExitTypesSpec} object representing the set of exit actions to be
   * taken when the SCO is not satisfied
   */
  public void setNotSatisfied(ExitTypesSpec notSatisfied) {
    this.notSatisfied = notSatisfied;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (!(o instanceof ScoExitActionSpec that)) {
      return false;
    }

    return new EqualsBuilder()
        .append(getSatisfied(), that.getSatisfied())
        .append(getNotSatisfied(), that.getNotSatisfied())
        .isEquals();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder(17, 37)
        .append(getSatisfied())
        .append(getNotSatisfied())
        .toHashCode();
  }
}