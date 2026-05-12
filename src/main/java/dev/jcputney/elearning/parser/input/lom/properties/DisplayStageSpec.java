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
 * <p>Represents the <strong>displayStageSpec</strong> complex type.</p>
 *
 * <p>The following schema snippet specifies its contents:</p>
 * <pre>{@code
 * <xs:complexType name="displayStageSpec">
 *   <xs:all>
 *     <xs:element name="required" type="stageSpec" minOccurs="0" maxOccurs="1" />
 *     <xs:element name="desired" type="stageSpec" minOccurs="0" maxOccurs="1" />
 *   </xs:all>
 * </xs:complexType>
 * }</pre>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonFormat(with = Feature.ACCEPT_CASE_INSENSITIVE_PROPERTIES)
public final class DisplayStageSpec implements Serializable {

  /**
   * The {@code <required>} element, which specifies the required display stage.
   */
  @JacksonXmlProperty(localName = "required")
  private StageSpec required;

  /**
   * The {@code <desired>} element, which specifies the desired display stage.
   */
  @JacksonXmlProperty(localName = "desired")
  private StageSpec desired;

  /**
   * Default constructor for the DisplayStageSpec class. Initializes a new instance of the class
   * without setting any properties. This constructor is intended for frameworks or tools that
   * require a no-argument constructor for deserialization or instantiation purposes.
   */
  public DisplayStageSpec() {
    // no-op
  }

  /**
   * Retrieves the {@code required} element, which specifies the required display stage.
   *
   * @return the {@link StageSpec} instance representing the required stage, or null if not set.
   */
  public StageSpec getRequired() {
    return this.required;
  }

  /**
   * Sets the {@code required} element, which specifies the required display stage.
   *
   * @param required an instance of {@link StageSpec} representing the required display stage.
   */
  public void setRequired(StageSpec required) {
    this.required = required;
  }

  /**
   * Retrieves the {@code desired} element, which specifies the desired display stage.
   *
   * @return the {@link StageSpec} instance representing the desired stage, or null if not set.
   */
  public StageSpec getDesired() {
    return this.desired;
  }

  /**
   * Sets the {@code desired} element, which specifies the desired display stage.
   *
   * @param desired an instance of {@link StageSpec} representing the desired display stage.
   */
  public void setDesired(StageSpec desired) {
    this.desired = desired;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (!(o instanceof DisplayStageSpec that)) {
      return false;
    }

    return new EqualsBuilder()
        .append(getRequired(), that.getRequired())
        .append(getDesired(), that.getDesired())
        .isEquals();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder(17, 37)
        .append(getRequired())
        .append(getDesired())
        .toHashCode();
  }
}
