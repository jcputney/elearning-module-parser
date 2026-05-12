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
package dev.jcputney.elearning.parser.input.scorm2004.ims.ss.sequencing;

import static dev.jcputney.elearning.parser.input.scorm2004.IMSSS.NAMESPACE_URI;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import java.io.Serializable;
import java.util.List;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * Represents a collection of sequencing elements within the SCORM IMS Simple Sequencing schema. A
 * sequencing collection groups multiple {@link Sequencing} definitions, each of which can specify
 * rules, objectives, and rollup behaviors for a learning activity.
 */
@JsonFormat(with = JsonFormat.Feature.ACCEPT_CASE_INSENSITIVE_PROPERTIES)
public final class SequencingCollection implements Serializable {

  /**
   * A list of {@link Sequencing} elements within the sequencing collection. Each sequencing element
   * defines navigation, rollup, and tracking settings for a specific set of learning activities.
   *
   * <p>These sequencing definitions are used by the LMS to control the flow of activities,
   * based on rules defined for completion, satisfaction, and objectives.</p>
   */
  @JacksonXmlElementWrapper(localName = "sequencing", useWrapping = false)
  @JacksonXmlProperty(localName = "sequencing", namespace = NAMESPACE_URI)
  private List<Sequencing> sequencingList;

  /**
   * Default constructor for the {@code SequencingCollection} class.
   * <p>
   * This constructor initializes an empty {@code SequencingCollection} instance. It does not
   * perform any additional setup or initialization.
   */
  public SequencingCollection() {
    // no-op
  }

  /**
   * Retrieves the list of {@link Sequencing} elements associated with this sequencing collection.
   * Each sequencing element defines navigation, rollup, and tracking settings for a specific set of
   * learning activities. The list may be empty if no sequencing elements are defined.
   *
   * @return a {@code List} of {@link Sequencing} objects representing the sequencing definitions
   * within the collection; may return {@code null} if the sequencing list is uninitialized.
   */
  public List<Sequencing> getSequencingList() {
    return this.sequencingList;
  }

  /**
   * Sets the list of {@link Sequencing} elements for the sequencing collection. Each
   * {@link Sequencing} element defines navigation, rollup, and tracking settings for a specific set
   * of learning activities. This method replaces any existing list of sequencing elements with the
   * provided one.
   *
   * @param sequencingList the new {@code List} of {@link Sequencing} objects to associate with this
   * sequencing collection. May be empty or {@code null} if no sequencing elements are defined.
   */
  public void setSequencingList(List<Sequencing> sequencingList) {
    this.sequencingList = sequencingList;
  }

  /**
   * Resolves and retrieves the {@link DeliveryControls} associated with the given sequencing
   * identifier. This method searches through the list of {@link Sequencing} elements within the
   * sequencing collection and returns the delivery controls of the matching sequence, if found.
   *
   * @param sequenceId the identifier of the sequencing element for which the delivery controls need
   * to be resolved. Must not be null or blank.
   * @return the {@link DeliveryControls} associated with the specified sequence identifier, or null
   * if no matching sequencing element or delivery controls are found.
   */
  public DeliveryControls resolveDeliveryControlsById(String sequenceId) {
    if (sequenceId == null || sequenceId.isBlank()) {
      return null;
    }
    if (sequencingList == null || sequencingList.isEmpty()) {
      return null;
    }
    for (Sequencing sequencing : sequencingList) {
      if (sequencing != null && sequenceId.equals(sequencing.getId())
          && sequencing.getDeliveryControls() != null) {
        return sequencing.getDeliveryControls();
      }
    }
    return null;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (!(o instanceof SequencingCollection that)) {
      return false;
    }

    return new EqualsBuilder()
        .append(getSequencingList(), that.getSequencingList())
        .isEquals();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder(17, 37)
        .append(getSequencingList())
        .toHashCode();
  }
}
