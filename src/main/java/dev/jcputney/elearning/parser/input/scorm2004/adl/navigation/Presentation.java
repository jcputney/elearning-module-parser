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
package dev.jcputney.elearning.parser.input.scorm2004.adl.navigation;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import dev.jcputney.elearning.parser.input.scorm2004.ADLNav;
import java.io.Serializable;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * Represents the presentation settings for the navigation controls. This class contains additional
 * settings that may define whether the navigation interface should be shown or hidden under certain
 * conditions.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonFormat(with = JsonFormat.Feature.ACCEPT_CASE_INSENSITIVE_PROPERTIES)
public final class Presentation implements Serializable {

  /**
   * Contains settings for the navigation interface within the LMS. This allows the LMS to control
   * which navigation components are displayed to the learner.
   */
  @JacksonXmlProperty(localName = "navigationInterface", namespace = ADLNav.NAMESPACE_URI)
  private NavigationInterface navigationInterface;

  /**
   * Constructs a new Presentation instance. This default constructor is provided to create an
   * instance of the Presentation class, which manages navigation interface settings that control
   * the visibility and behavior of navigation components in a learning management system (LMS).
   */
  public Presentation() {
    // no-op
  }

  /**
   * Retrieves the navigation interface settings associated with this presentation. The navigation
   * interface defines controls and options for interacting with the LMS navigation UI, specifying
   * which components are visible or hidden and how navigation behavior is managed.
   *
   * @return the NavigationInterface object containing the navigation controls and settings, or null
   * if no navigation interface has been defined.
   */
  public NavigationInterface getNavigationInterface() {
    return this.navigationInterface;
  }

  /**
   * Sets the navigation interface for the presentation. The navigation interface specifies controls
   * and options for interacting with the LMS navigation UI, determining the visibility and behavior
   * of navigation components.
   *
   * @param navigationInterface the NavigationInterface object containing the settings and controls
   * for the LMS navigation user interface
   */
  public void setNavigationInterface(NavigationInterface navigationInterface) {
    this.navigationInterface = navigationInterface;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (!(o instanceof Presentation that)) {
      return false;
    }

    return new EqualsBuilder()
        .append(getNavigationInterface(), that.getNavigationInterface())
        .isEquals();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder(17, 37)
        .append(getNavigationInterface())
        .toHashCode();
  }
}
