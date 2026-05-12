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
package dev.jcputney.elearning.parser.input.scorm2004.adl.cp;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import dev.jcputney.elearning.parser.input.scorm2004.ADLCP;
import java.io.Serializable;
import java.util.List;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * Represents the data element, which contains multiple map elements.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonFormat(with = JsonFormat.Feature.ACCEPT_CASE_INSENSITIVE_PROPERTIES)
public final class ADLData implements Serializable {

  /**
   * List of map elements, where each map specifies a target and shared data access.
   */
  @JacksonXmlElementWrapper(useWrapping = false)
  @JacksonXmlProperty(localName = "map", namespace = ADLCP.NAMESPACE_URI)
  private List<DataMap> mapList;

  /**
   * Default constructor for the ADLData class.
   * <p>
   * This constructor initializes an instance of the ADLData class without setting any properties or
   * performing any actions. It serves as a no-operation (no-op) placeholder for object
   * instantiation.
   */
  public ADLData() {
    // no-op
  }

  /**
   * Retrieves the list of map elements, where each map specifies a target and shared data access.
   *
   * @return a list of DataMap objects representing the target and shared data configuration
   */
  public List<DataMap> getMapList() {
    return this.mapList;
  }

  /**
   * Sets the list of map elements, where each map specifies a target ID and shared data access
   * configuration.
   *
   * @param mapList the list of {@code DataMap} objects to be assigned, representing the target and
   * shared data configuration
   */
  public void setMapList(List<DataMap> mapList) {
    this.mapList = mapList;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (!(o instanceof ADLData adlData)) {
      return false;
    }

    return new EqualsBuilder()
        .append(getMapList(), adlData.getMapList())
        .isEquals();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder(17, 37)
        .append(getMapList())
        .toHashCode();
  }
}
