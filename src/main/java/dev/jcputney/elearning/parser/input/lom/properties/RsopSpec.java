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
 * <p>Represents the <strong>rsopSpec</strong> complex type.</p>
 *
 * <p>The following schema snippet specifies its contents:</p>
 * <pre>{@code
 * <xs:complexType name="rsopSpec">
 *   <xs:all>
 *     <xs:element name="offlineSynchMode" type="offlineSynchModeType" minOccurs="0" maxOccurs="1" />
 *   </xs:all>
 * </xs:complexType>
 * }</pre>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonFormat(with = Feature.ACCEPT_CASE_INSENSITIVE_PROPERTIES)
public final class RsopSpec implements Serializable {

  /**
   * The offline synchronization mode for the resource.
   */
  @JacksonXmlProperty(localName = "offlineSynchMode")
  private OfflineSynchModeType offlineSynchMode;

  /**
   * Default constructor for the RsopSpec class. Initializes a new instance of the RsopSpec class
   * with no specific configuration.
   */
  public RsopSpec() {
    // no-op
  }

  /**
   * Retrieves the offline synchronization mode associated with the current resource.
   *
   * @return the offline synchronization mode as an {@code OfflineSynchModeType}.
   */
  public OfflineSynchModeType getOfflineSynchMode() {
    return this.offlineSynchMode;
  }

  /**
   * Sets the offline synchronization mode for the resource.
   *
   * @param offlineSynchMode the offline synchronization mode to be applied, represented as an
   * {@code OfflineSynchModeType}.
   */
  public void setOfflineSynchMode(OfflineSynchModeType offlineSynchMode) {
    this.offlineSynchMode = offlineSynchMode;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (!(o instanceof RsopSpec rsopSpec)) {
      return false;
    }

    return new EqualsBuilder()
        .append(getOfflineSynchMode(), rsopSpec.getOfflineSynchMode())
        .isEquals();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder(17, 37)
        .append(getOfflineSynchMode())
        .toHashCode();
  }
}