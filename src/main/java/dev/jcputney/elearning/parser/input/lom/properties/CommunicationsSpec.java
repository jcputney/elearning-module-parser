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
 * <p>Represents the <strong>communicationsSpec</strong> complex type.</p>
 *
 * <p>The following schema snippet specifies its contents:</p>
 * <pre>{@code
 * <xs:complexType name="communicationsSpec">
 *   <xs:all>
 *     <xs:element name="maxFailedSubmissions" type="xs:int" minOccurs="0" maxOccurs="1" />
 *     <xs:element name="commitFrequency" type="xs:int" minOccurs="0" maxOccurs="1" />
 *   </xs:all>
 * </xs:complexType>
 * }</pre>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonFormat(with = Feature.ACCEPT_CASE_INSENSITIVE_PROPERTIES)
public final class CommunicationsSpec implements Serializable {

  /**
   * The maximum number of failed submissions allowed.
   */
  @JacksonXmlProperty(localName = "maxFailedSubmissions")
  private Integer maxFailedSubmissions;

  /**
   * The frequency of commits.
   */
  @JacksonXmlProperty(localName = "commitFrequency")
  private Integer commitFrequency;

  /**
   * Default constructor for the {@code CommunicationsSpec} class.
   * <p>
   * Initializes a new instance of the {@code CommunicationsSpec} class with default values. This
   * constructor performs no specific operation.
   */
  public CommunicationsSpec() {
    // no-op
  }

  /**
   * Retrieves the maximum number of failed submissions allowed.
   *
   * @return the maximum number of failed submissions, or null if not specified.
   */
  public Integer getMaxFailedSubmissions() {
    return this.maxFailedSubmissions;
  }

  /**
   * Sets the maximum number of failed submissions allowed.
   *
   * @param maxFailedSubmissions the maximum number of failed submissions, or null to indicate no
   * limit.
   */
  public void setMaxFailedSubmissions(Integer maxFailedSubmissions) {
    this.maxFailedSubmissions = maxFailedSubmissions;
  }

  /**
   * Retrieves the commit frequency for the current instance.
   *
   * @return the frequency of commits, or null if not specified.
   */
  public Integer getCommitFrequency() {
    return this.commitFrequency;
  }

  /**
   * Sets the commit frequency for the current instance.
   *
   * @param commitFrequency the frequency of commits, or null to indicate no specific frequency.
   */
  public void setCommitFrequency(Integer commitFrequency) {
    this.commitFrequency = commitFrequency;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (!(o instanceof CommunicationsSpec that)) {
      return false;
    }

    return new EqualsBuilder()
        .append(getMaxFailedSubmissions(),
            that.getMaxFailedSubmissions())
        .append(getCommitFrequency(), that.getCommitFrequency())
        .isEquals();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder(17, 37)
        .append(getMaxFailedSubmissions())
        .append(getCommitFrequency())
        .toHashCode();
  }
}
