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
package dev.jcputney.elearning.parser.input.aicc;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import java.io.Serializable;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * Represents the descriptor element in an AICC module.
 *
 * <p>This class is used to parse the descriptor element of an AICC module file.
 * It contains information about the system ID, developer ID, title, and description of the module.
 */
@JsonNaming(SnakeCaseStrategy.class)
@JsonFormat(with = JsonFormat.Feature.ACCEPT_CASE_INSENSITIVE_PROPERTIES)
public final class Descriptor implements Serializable {

  /**
   * The system ID of the descriptor element.
   */
  @JsonProperty(value = "System_ID", required = true)
  private String systemId;

  /**
   * The developer ID of the descriptor element.
   */
  @JsonProperty(value = "Developer_ID", required = true)
  private String developerId;

  /**
   * The title of the descriptor element.
   */
  @JsonProperty(value = "Title", required = true)
  private String title;

  /**
   * The version of the descriptor element.
   */
  @JsonProperty(value = "Description")
  private String description;

  /**
   * Constructs a Descriptor object with the specified system ID, developer ID, title, and
   * description.
   *
   * @param systemId the unique identifier for the system.
   * @param developerId the unique identifier for the developer.
   * @param title the title of the descriptor.
   * @param description a detailed description of the descriptor.
   */
  public Descriptor(String systemId, String developerId, String title, String description) {
    this.systemId = systemId;
    this.developerId = developerId;
    this.title = title;
    this.description = description;
  }

  /**
   * Default constructor for the Descriptor class. Initializes an instance of the Descriptor object
   * with no initial values set for its fields.
   */
  public Descriptor() {
    // no-op
  }

  /**
   * Retrieves the system ID of the descriptor element.
   *
   * @return the system ID as a String
   */
  public String getSystemId() {
    return this.systemId;
  }

  /**
   * Sets the system ID of the descriptor element.
   *
   * @param systemId the system ID to be assigned
   */
  public void setSystemId(String systemId) {
    this.systemId = systemId;
  }

  /**
   * Retrieves the developer ID of the descriptor element.
   *
   * @return the developer ID as a String
   */
  public String getDeveloperId() {
    return this.developerId;
  }

  /**
   * Sets the developer ID of the descriptor element.
   *
   * @param developerId the developer ID to be assigned
   */
  public void setDeveloperId(String developerId) {
    this.developerId = developerId;
  }

  /**
   * Retrieves the title of the descriptor element.
   *
   * @return the title as a String
   */
  public String getTitle() {
    return this.title;
  }

  /**
   * Sets the title of the descriptor element.
   *
   * @param title the title to be assigned
   */
  public void setTitle(String title) {
    this.title = title;
  }

  /**
   * Retrieves the description of the descriptor element.
   *
   * @return the description as a String
   */
  public String getDescription() {
    return this.description;
  }

  /**
   * Sets the description of the descriptor element.
   *
   * @param description the description to be assigned
   */
  public void setDescription(String description) {
    this.description = description;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (!(o instanceof Descriptor that)) {
      return false;
    }

    return new EqualsBuilder()
        .append(getSystemId(), that.getSystemId())
        .append(getDeveloperId(), that.getDeveloperId())
        .append(getTitle(), that.getTitle())
        .append(getDescription(), that.getDescription())
        .isEquals();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder(17, 37)
        .append(getSystemId())
        .append(getDeveloperId())
        .append(getTitle())
        .append(getDescription())
        .toHashCode();
  }
}
