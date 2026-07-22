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
package dev.jcputney.elearning.parser.input.scorm2004.adl.sequencing;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import dev.jcputney.elearning.parser.input.scorm2004.ADLSeq;
import java.io.Serializable;
import java.util.List;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * Represents the ADL sequencing objectives container.
 *
 * <p>SCORM 2004 defines {@code adlseq:objectives} separately from IMS Simple Sequencing
 * {@code imsss:objectives}. The ADL container contains one or more {@code adlseq:objective}
 * elements whose {@code adlseq:mapInfo} children expose additional score, completion, and progress
 * mapping flags.</p>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonFormat(with = JsonFormat.Feature.ACCEPT_CASE_INSENSITIVE_PROPERTIES)
public final class ADLObjectives implements Serializable {

  /**
   * ADL objectives in document order.
   */
  @JacksonXmlElementWrapper(localName = "objective", useWrapping = false)
  @JacksonXmlProperty(localName = "objective", namespace = ADLSeq.NAMESPACE_URI)
  private List<ADLObjective> objectiveList;

  /**
   * Default constructor for Jackson and callers that populate the container manually.
   */
  public ADLObjectives() {
    // no-op
  }

  /**
   * Constructs an ADL objectives container with the supplied objective list.
   *
   * @param objectiveList ADL objectives in document order
   */
  public ADLObjectives(List<ADLObjective> objectiveList) {
    this.objectiveList = objectiveList;
  }

  /**
   * Returns the ADL objectives in document order.
   *
   * @return ADL objectives, or null if none were parsed
   */
  public List<ADLObjective> getObjectiveList() {
    return this.objectiveList;
  }

  /**
   * Sets the ADL objectives in document order.
   *
   * @param objectiveList ADL objectives to assign
   */
  public void setObjectiveList(List<ADLObjective> objectiveList) {
    this.objectiveList = objectiveList;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (!(o instanceof ADLObjectives that)) {
      return false;
    }

    return new EqualsBuilder()
        .append(getObjectiveList(), that.getObjectiveList())
        .isEquals();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder(17, 37)
        .append(getObjectiveList())
        .toHashCode();
  }
}
