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
import java.math.BigInteger;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * <p>Represents the <strong>stageSpec</strong> complex type.</p>
 *
 * <p>The following schema snippet specifies its contents:</p>
 * <pre>{@code
 * <xs:complexType name="stageSpec">
 *   <xs:all>
 *     <xs:element name="width" type="xs:nonNegativeInteger" minOccurs="0" maxOccurs="1" />
 *     <xs:element name="height" type="xs:nonNegativeInteger" minOccurs="0" maxOccurs="1" />
 *     <xs:element name="fullscreen" type="yesNoType" minOccurs="0" maxOccurs="1" />
 *   </xs:all>
 * </xs:complexType>
 * }</pre>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonFormat(with = Feature.ACCEPT_CASE_INSENSITIVE_PROPERTIES)
public final class StageSpec implements Serializable {

  /**
   * Per XSD, this is xs:nonNegativeInteger, so we use BigInteger.
   */
  @JacksonXmlProperty(localName = "width")
  private BigInteger width;

  /**
   * Per XSD, this is xs:nonNegativeInteger, so we use BigInteger.
   */
  @JacksonXmlProperty(localName = "height")
  private BigInteger height;

  /**
   * Per XSD, this is yesNoType.
   */
  @JacksonXmlProperty(localName = "fullscreen")
  private YesNoType fullscreen;

  /**
   * Default constructor for the StageSpec class.
   * <p>
   * Initializes a new instance of the StageSpec class with no specific configuration.
   */
  public StageSpec() {
    // no-op
  }

  /**
   * Retrieves the width associated with the current stage specification.
   *
   * @return the width as a {@code BigInteger}, representing a non-negative integer.
   */
  public BigInteger getWidth() {
    return this.width;
  }

  /**
   * Sets the width for the current stage specification.
   *
   * @param width the width to be set as a {@code BigInteger}, representing a non-negative integer.
   */
  public void setWidth(BigInteger width) {
    this.width = width;
  }

  /**
   * Retrieves the height associated with the current stage specification.
   *
   * @return the height as a {@code BigInteger}, representing a non-negative integer.
   */
  public BigInteger getHeight() {
    return this.height;
  }

  /**
   * Sets the height for the current stage specification.
   *
   * @param height the height to be set as a {@code BigInteger}, representing a non-negative
   * integer.
   */
  public void setHeight(BigInteger height) {
    this.height = height;
  }

  /**
   * Retrieves the fullscreen setting associated with the current stage specification.
   *
   * @return the fullscreen setting as a {@code YesNoType}, which can represent values like "yes",
   * "no", "true", or "false".
   */
  public YesNoType getFullscreen() {
    return this.fullscreen;
  }

  /**
   * Updates the fullscreen setting for the current stage specification.
   *
   * @param fullscreen the fullscreen setting to be applied, represented as a {@code YesNoType}.
   * Possible values include "yes", "no", "true", or "false".
   */
  public void setFullscreen(YesNoType fullscreen) {
    this.fullscreen = fullscreen;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (!(o instanceof StageSpec stageSpec)) {
      return false;
    }

    return new EqualsBuilder()
        .append(getWidth(), stageSpec.getWidth())
        .append(getHeight(), stageSpec.getHeight())
        .append(getFullscreen(), stageSpec.getFullscreen())
        .isEquals();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder(17, 37)
        .append(getWidth())
        .append(getHeight())
        .append(getFullscreen())
        .toHashCode();
  }
}