/*
 * Copyright (c) 2024-2025. Jonathan Putney
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package dev.jcputney.elearning.parser.input.scorm2004.adl.navigation;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import dev.jcputney.elearning.parser.input.scorm2004.ADLNav;
import java.io.Serializable;
import java.util.List;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * Represents the navigation interface controls, which specify options for interacting with the LMS
 * navigation UI. This includes defining the appearance and functionality of the navigation buttons
 * and controls.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonFormat(with = JsonFormat.Feature.ACCEPT_CASE_INSENSITIVE_PROPERTIES)
public final class NavigationInterface implements Serializable {

  /**
   * A list of elements defining which parts of the LMS UI should be hidden. Each item specifies an
   * individual UI component to be hidden. Common values are options like "exit" or "continue".
   */
  @JacksonXmlElementWrapper(useWrapping = false)
  @JacksonXmlProperty(localName = "hideLMSUI", namespace = ADLNav.NAMESPACE_URI)
  private List<HideLMSUI> hideLMSUI;

  public NavigationInterface() {
    // no-op
  }

  /**
   * Retrieves a list of elements defining which parts of the LMS user interface should be hidden.
   * Each element in the list corresponds to a specific UI component that is configured to be hidden
   * based on SCORM navigation requirements.
   *
   * @return a list of HideLMSUI elements representing the LMS UI components to be hidden.
   */
  public List<HideLMSUI> getHideLMSUI() {
    return this.hideLMSUI;
  }

  /**
   * Sets the list of LMS UI components to be hidden. This method defines which elements of the
   * Learning Management System's user interface should be made invisible, based on the specified
   * SCORM navigation requirements.
   *
   * @param hideLMSUI a list of {@link HideLMSUI} enums representing the UI components to be hidden.
   * Each element in the list corresponds to a specific LMS UI feature, such as "exit", "continue",
   * or "abandon".
   */
  public void setHideLMSUI(List<HideLMSUI> hideLMSUI) {
    this.hideLMSUI = hideLMSUI;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (!(o instanceof NavigationInterface that)) {
      return false;
    }

    return new EqualsBuilder()
        .append(getHideLMSUI(), that.getHideLMSUI())
        .isEquals();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder(17, 37)
        .append(getHideLMSUI())
        .toHashCode();
  }
}
